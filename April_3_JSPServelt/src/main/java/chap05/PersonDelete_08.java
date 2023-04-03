package chap05;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/personDelete.do")
public class PersonDelete_08 extends HttpServlet {
	private static final long serialVersionUID = 1L;

	InterPersonDAO_03 dao= new PersonDAO_04();

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String method = request.getMethod();// "GET" 또는 "POST"
		
		if("POST".equalsIgnoreCase(method)) {
			
			String seq = request.getParameter("seq");
			String path = "";
			
			try {
				// 특정 회원을 삭제하기 전 삭제할 회원의 정보를 먼저 알아온다.
				PersonDTO_02 psdto = dao.selectOne(seq);
				request.setAttribute("psdto", psdto);
				
				int n = dao.deletePerson(seq);   // seq 를 보내기 위해 
				
				if(n==1) {
					path = "/WEB-INF/chap05_right/personDelete.jsp";
				}
				
			}catch (SQLException e) {
				
				e.printStackTrace();
				path = "/WEB-INF/chap05_right/sql_error.jsp";
				
			}
			RequestDispatcher dispatcher = request.getRequestDispatcher(path);
			dispatcher.forward(request, response);
			
		}
		else {   // get 방식으로 접근한 경우라면 
				 // 아무것도 일어나지 않도록 하려면 코딩을 안해주면 된다.
		}
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
