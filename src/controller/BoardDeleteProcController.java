package controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.BoardDAO;

@WebServlet("/BoardDeleteProc.do")
public class BoardDeleteProcController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doHandle(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doHandle(request, response);
	}

	private void doHandle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		
		int num = Integer.parseInt(request.getParameter("num"));
		String originalPasswrod = request.getParameter("originalPassword");
		String password = request.getParameter("password");
		
		//패스워드가 같을 경우 삭제
		if(password.equals(originalPasswrod)) {
			
			BoardDAO bDao = new BoardDAO();
			bDao.deleteBoard(num);
			
			RequestDispatcher dis = request.getRequestDispatcher("BoardList.do");
			dis.forward(request, response);
			
		}else {
			
			request.setAttribute("msg", "삭제 시 비밀번호가 다릅니다.");
			
			RequestDispatcher dis = request.getRequestDispatcher("BoardList.do");
			dis.forward(request, response);
			
		}
		
	}

}
