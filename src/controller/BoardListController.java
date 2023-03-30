package controller;

import java.io.IOException;
import java.util.Vector;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.BoardBean;
import model.BoardDAO;

@WebServlet("/BoardList.do")
public class BoardListController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doHandle(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doHandle(request, response);
	}
	//httpMethod get,post 요청 둘 다 받는 메서드 
	protected void doHandle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//페이징
		
		//화면에 보여질 게시글의 갯수를 지정
		int pageSize = 10;
		
		//현재 보여지고 있는 페이지의 넘버값을 읽어드림
		String pageNum = request.getParameter("pageNum");
		
		//null 처리 (처음 요청시 pageNum값이 없다)
		if(pageNum == null) {
			pageNum = "1";
		}
		
		//전체 게시글의 갯수
		int count = 0;
		
		//JSP페이지 내에서 보여질 넘버링 숫자값을 저장하는 변수
		int number = 0;
		
		//현재 보여지고 있는 페이지 문자를 숫자로 parsing
		int currentPage = Integer.parseInt(pageNum);
		
		//전체 게시글의 갯수를 가져와야 하기에 데이터베이스 객체 생성
		BoardDAO bDao = new BoardDAO();
		count = bDao.getAllCount();
		
		//현재 보여질 페이지 시작 번호를 설정
		int startRow = (currentPage - 1) * pageSize + 1;
		int endRow = currentPage * pageSize;
		
		//최신글 10개를 기준으로 게시글을 리턴 받아주는 메소드 호출
		Vector<BoardBean> v = bDao.getAllBoard(startRow, endRow);
		number = count - (currentPage-1) * pageSize;
		
		//request객체에 바인딩
		request.setAttribute("v", v);
		request.setAttribute("number", number);
		request.setAttribute("pageSize", pageSize);
		request.setAttribute("count", count);
		request.setAttribute("currentPage", currentPage);
		
		RequestDispatcher dis = request.getRequestDispatcher("BoardList.jsp");
		dis.forward(request, response);
		
		
		
	}
}