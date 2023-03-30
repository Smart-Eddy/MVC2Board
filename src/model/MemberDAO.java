package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class MemberDAO {
	
	Connection conn;
	PreparedStatement pstmt;
	ResultSet rs;
	
	public void getConnection() {
		
		try {
			
			Context ctx = new InitialContext();
			Context envContext = (Context) ctx.lookup("java:comp/env");
			DataSource dataFactory  = (DataSource) envContext.lookup("jdbc/oracle");
			conn = dataFactory.getConnection();
			
		}catch (Exception e) {
			e.printStackTrace();
			
		}
		
	}
	
	//회원 한사람에 대한 정보를 저장하는 메소드
	public void insertMember(MemberBean bean) {
		
		getConnection();
		
		try {
			String sql = "insert into member values(?,?,?,?,?,?,?,?)";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, bean.getId());
			pstmt.setString(2, bean.getPass1());
			pstmt.setString(3, bean.getEmail());
			pstmt.setString(4, bean.getTel());
			pstmt.setString(5, bean.getHobby());
			pstmt.setString(6, bean.getJob());
			pstmt.setString(7, bean.getAge());
			pstmt.setString(8, bean.getInfo());
			
			pstmt.executeUpdate();
			
			pstmt.close();
			conn.close();
		}catch(Exception e) {
			
			e.printStackTrace();
		}
		
	}
}
