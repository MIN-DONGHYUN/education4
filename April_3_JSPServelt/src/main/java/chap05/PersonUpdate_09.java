package chap05;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/personUpdate.do")
public class PersonUpdate_09 extends HttpServlet {
	private static final long serialVersionUID = 1L;

	InterPersonDAO_03 dao = new PersonDAO_04();		// 필드 생성

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// post 방식만 허락해주고 get 방식은 하지 않도록 하겠다. (메소드가 무엇인지 알아야 한다.)
		String method = request.getMethod();   // "GET" 또는 :POST"
		
		String seq = request.getParameter("seq");   //seq 를 사용하기 위해 받아온다. 
		
		
		if("POST".equalsIgnoreCase(method)) {    // "POST" 방식이라면  
			
			String path = "/";
			
			// select 를 해와야하니까 dao가 필요하기에 필드 한개를 만든다.
			try {
				PersonDTO_02 psdto = dao.selectOne(seq);
				
				request.setAttribute("psdto", psdto);
				
				path = "/WEB-INF/chap05_right/personUpdate.jsp";
				
			} catch (SQLException e) {
				
				e.printStackTrace();
				path = "/WEB-INF/chap05_right/sql_error.jsp";
			}
			RequestDispatcher dispatcher = request.getRequestDispatcher(path);
			dispatcher.forward(request, response);
			
		}
		else {   // "GET" 방식이라면 
			
			// GET 방식을 할 수 없도록 오류 메세지를 띄울 것이다. (데이터에 없는 seq 는 사용자님 장난치지 마세요가 나오고 있으면 디테일이 보여짐)
			
			response.sendRedirect(request.getContextPath()+"/personDetail.do?seq="+seq);   // sendRedirect("URL") 으로 사용한다.  여기서는 절대경로 사용했다.
			// response.sendRedirect("URL 경로"); 는 웹브라우저의 주소창에 URL 경로명으로 이동시킨다. 
		}
		
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
	}

}
