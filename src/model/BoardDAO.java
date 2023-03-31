package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class BoardDAO {
	
	Connection conn;
	PreparedStatement pstmt;
	ResultSet rs;
	
	//DB Connection Method
	public void getConnection() {
		
		try {
			Context initctx = new InitialContext();
			Context envCtx  = (Context) initctx.lookup("java:comp/env");
			DataSource ds = (DataSource) envCtx.lookup("jdbc/oracle");
			conn = ds.getConnection();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}

	//전체 글의 갯수를 리턴하는 Method 
	public int getAllCount() {
		
		getConnection();
		
		int count = 0;
		try {
			
			String sql = "select count(*) from board";
			
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) { //결과가 단일 행일 때는 while문 X
				
				count = rs.getInt(1); // 전체 게시글 수
			}
			
			rs.close();
			pstmt.close();
			conn.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return count;
	}
	
	//모든(화면에 보여줄 게시글(10개씩)을) 게시물을 리턴해주는 Method
	public Vector<BoardBean> getAllBoard(int startRow, int endRow) {
		
		Vector<BoardBean> v = new Vector<>();
		
		getConnection();
		
		try {
			String sql =  "select * "
						+ "from (select A.*, Rownum Rnum "
						+ "from(select * from board order by ref desc, re_step asc) A)"
						+ "where Rnum >= ? and Rnum <= ?";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, startRow);
			pstmt.setInt(2, endRow);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				BoardBean bean = new BoardBean();
				bean.setNum(rs.getInt(1));
				bean.setWriter(rs.getString(2));
				bean.setEmail(rs.getString(3));
				bean.setSubject(rs.getString(4));
				bean.setPassword(rs.getString(5));
				bean.setReg_date(rs.getDate(6).toString());
				bean.setRef(rs.getInt(7));
				bean.setRe_step(rs.getInt(8));
				bean.setRe_level(rs.getInt(9));
				bean.setReadcount(rs.getInt(10));
				bean.setContent(rs.getString(11));
				
				v.add(bean);
			}
			
			rs.close();
			pstmt.close();
			conn.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return v;
	}
	
	//글작성 Method
	public void insertBoard(BoardBean bean) {
		
		getConnection();
		
		int ref = 0;
		int re_step = 1; 
		int re_level = 1; 
		
		try {
			String sql = "select max(ref) from board";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				ref = rs.getInt(1) + 1; //가장 큰 값에 1더해줌
			}
			//데이터를 삽입하는 쿼리
			String sql2 = "insert into board values(board_seq.nextval,?,?,?,?,sysdate,?,?,?,0,?)";
			pstmt = conn.prepareStatement(sql2);
			
			pstmt.setString(1, bean.getWriter());
			pstmt.setString(2, bean.getEmail());
			pstmt.setString(3, bean.getSubject());
			pstmt.setString(4, bean.getPassword());
			pstmt.setInt(5, ref);
			pstmt.setInt(6, re_step);
			pstmt.setInt(7, re_level);
			pstmt.setString(8, bean.getContent());
			
			pstmt.executeUpdate();
			
			pstmt.close();
			conn.close();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	//글 상세정보 조회 -> 조회수가 증가해야한다. (트랜잭션)
	public BoardBean getOneBoard(int num) {
		
		getConnection();
		BoardBean bean = null;
		try {
		//조회수
		String sql = "update board set readcount = readcount+1 where num = ?";
		pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, num);
		
		pstmt.executeUpdate();
		//게시글 조회
		String sql2 = "select * from board where num = ?";
		pstmt = conn.prepareStatement(sql2);
		pstmt.setInt(1, num);
		rs = pstmt.executeQuery();
		
		if(rs.next()) {
			bean = new BoardBean();
			
			bean.setNum(rs.getInt(1));
			bean.setWriter(rs.getString(2));
			bean.setEmail(rs.getString(3));
			bean.setSubject(rs.getString(4));
			bean.setPassword(rs.getString(5));
			bean.setReg_date(rs.getDate(6).toString());
			bean.setRef(rs.getInt(7));
			bean.setRe_step(rs.getInt(8));
			bean.setRe_level(rs.getInt(9));
			bean.setReadcount(rs.getInt(10));
			bean.setContent(rs.getString(11));
		}
		
		rs.close();
		pstmt.close();
		conn.close();

		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return bean;
	}
	//답글쓰기 Method
	public void reInsertBoard(BoardBean bean) {
		
		getConnection();
		
		int ref = bean.getRef();
		int re_step = bean.getRe_step(); 
		int re_level = bean.getRe_level(); 
		
		try {
			
			String sql = "update board set re_level = re_level+1 where ref= ? and re_level > ?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, ref);
			pstmt.setInt(2, re_level);
			
			pstmt.executeUpdate();
			
			//데이터를 삽입하는 쿼리
			String sql2 = "insert into board values(board_seq.nextval,?,?,?,?,sysdate,?,?,?,0,?)";
			pstmt = conn.prepareStatement(sql2);
			
			pstmt.setString(1, bean.getWriter());
			pstmt.setString(2, bean.getEmail());
			pstmt.setString(3, bean.getSubject());
			pstmt.setString(4, bean.getPassword());
			pstmt.setInt(5, ref);
			pstmt.setInt(6, re_step+1); // 기존 부모글에 setp보다 1을 증가 시켜야함
			pstmt.setInt(7, re_level+1); //
			pstmt.setString(8, bean.getContent());
			
			pstmt.executeUpdate();
			
			pstmt.close();
			conn.close();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	//글 수정 method
	public BoardBean getOneUpdateBoard(int num) {
		
		getConnection();
		BoardBean bean = null;
		try {
		
		//게시글 조회
		String sql2 = "select * from board where num = ?";
		pstmt = conn.prepareStatement(sql2);
		pstmt.setInt(1, num);
		rs = pstmt.executeQuery();
		
		if(rs.next()) {
			bean = new BoardBean();
			
			bean.setNum(rs.getInt(1));
			bean.setWriter(rs.getString(2));
			bean.setEmail(rs.getString(3));
			bean.setSubject(rs.getString(4));
			bean.setPassword(rs.getString(5));
			bean.setReg_date(rs.getDate(6).toString());
			bean.setRef(rs.getInt(7));
			bean.setRe_step(rs.getInt(8));
			bean.setRe_level(rs.getInt(9));
			bean.setReadcount(rs.getInt(10));
			bean.setContent(rs.getString(11));
		}
		
		rs.close();
		pstmt.close();
		conn.close();

		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return bean;
	}
	
	//비밀번호가 같다면 게시글 수정Method
	public void updateBoard(int num, String subject, String content) {
		
		getConnection();
		
		try {
			String sql = "update board set subject= ?, content= ? where num = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, subject);
			pstmt.setString(2, content);
			pstmt.setInt(3, num);
			
			pstmt.executeUpdate();
			
			pstmt.close();
			conn.close();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		
	}
	//게시글 삭제
	public void deleteBoard(int num) {
		
		getConnection();
		try {
			
		String sql = "delete from board where num = ?";
		pstmt = conn.prepareStatement(sql);
		
		pstmt.setInt(1, num);
		pstmt.executeUpdate();
		
		pstmt.close();
		conn.close();
		
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	

}//class
