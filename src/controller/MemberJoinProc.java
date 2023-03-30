package controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.MemberBean;
import model.MemberDAO;


@WebServlet("/proc.do")
public class MemberJoinProc extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
  
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doHandle(request, response);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doHandle(request, response);
	}
	
	protected void doHandle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		//encoding
		request.setCharacterEncoding("UTF-8");
		
		MemberBean bean = new MemberBean();
		//id
		bean.setId(request.getParameter("id"));
		//password
		String pass1 = request.getParameter("pass1");
		String pass2 = request.getParameter("pass2");
		bean.setPass1(pass1);
		bean.setPass2(pass2);
		//email
		bean.setEmail(request.getParameter("email"));
		//tel
		bean.setTel(request.getParameter("tel"));
		
		String [] arr = request.getParameterValues("hobby");
		String data = "";
		for (String string : arr) {
			data += string + " "; //하나의 스트링으로 데이터 연결
		}
		bean.setHobby(data);
		
		bean.setJob(request.getParameter("job"));
		bean.setAge(request.getParameter("age"));
		bean.setInfo(request.getParameter("info"));
		
		
		//패스워드1,2가 동일하다면
		if(pass1.equals(pass2)) {
			
			MemberDAO memberDao = new MemberDAO();
			memberDao.insertMember(bean);
			
			RequestDispatcher dis = request.getRequestDispatcher("MemberList.jsp");
			dis.forward(request, response);
			
		//패스워드가 동일하지 않다면
		}else {
			
			request.setAttribute("msg", "패스워드가 일치하지 않습니다");
			RequestDispatcher dis = request.getRequestDispatcher("LoginError.jsp");
			dis.forward(request, response);
		}
		
		//데이터 베이스 객체 선언한 후 저장
		
	
		
		
		
	}
}
