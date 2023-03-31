package controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.BoardDAO;


@WebServlet("/BoardUpdateProc.do")
public class BoardUpdateProcController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doHandle(request,response);
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doHandle(request,response);
	}

	protected void doHandle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		int num = Integer.parseInt(request.getParameter("num"));
		//사용자가 입력한 패스워드
		String paasword = request.getParameter("password");
		//기존 DB에 저장되어 있는 패스워드
		String originalPassword = request.getParameter("originalPassword");
		String subject = request.getParameter("subject");
		String content = request.getParameter("content");
		
		//비밀번호 비교
		if(paasword.equals(originalPassword)) {
			
			BoardDAO bDao = new BoardDAO();
			bDao.updateBoard(num,subject,content);
			
			//비밀번호가 같아서 수정이 완료됬다면
			request.setAttribute("msg", "수정이 완료되었습니다.");
			RequestDispatcher dis = request.getRequestDispatcher("BoardList.do");
			dis.forward(request, response);
				
		}else {
			//비밀번호가 다르다면
			request.setAttribute("msg", "수정 시 비밀번호가 맞지 않습니다.");
			RequestDispatcher dis = request.getRequestDispatcher("BoardList.do");
			dis.forward(request, response);
		}
		
		
	}

}
