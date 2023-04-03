package chap05;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/personUpdateEnd.do")
public class PersonUpdateEnd_10 extends HttpServlet {
	private static final long serialVersionUID = 1L;

	InterPersonDAO_03 dao = new PersonDAO_04();

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String method = request.getMethod();   // "GET", 또는 "POST"
		
		if("POST".equalsIgnoreCase(method)) {
					
			String seq = request.getParameter("seq");
			String name = request.getParameter("name");
			String school = request.getParameter("school");
			String color = request.getParameter("color");
			String[] arr_food = request.getParameterValues("food");   // 한개가 아니라면 values 붙어 있는 것으로 해야 한다. 
			
			PersonDTO_02 psdto = new PersonDTO_02();				//psdto 를 사용하기 위해서 
			psdto.setSeq(Integer.parseInt(seq));
			
			psdto.setName(name);
			psdto.setSchool(school);
			psdto.setColor(color);
			psdto.setFood(arr_food);
			
			try {
				
				int n = dao.updatePerson(psdto);
				
				if(n==1) {
					// n==1 이라면 
					// 회원정보를 변경한 이루에 변경된 회원 정보를 조회해 주도록 한다.
					// 페이지 이동 ==> // sendRedirect("URL") 으로 사용한다.  // response.sendRedirect("URL 경로"); 는 웹브라우저의 주소창에 URL 경로명으로 이동시킨다.
					response.sendRedirect(request.getContextPath()+"/personUpdate.do?seq="+seq);   // sendRedirect("URL") 으로 사용한다.  여기서는 절대경로 사용했다.
					// response.sendRedirect("URL 경로"); 는 웹브라우저의 주소창에 URL 경로명으로 이동시킨다. 
					
				}
			} catch (SQLException e) {

				e.printStackTrace();
				RequestDispatcher dispatcher =  request.getRequestDispatcher("/WEB-INF/chap05_right/sql_error.jsp");
				dispatcher.forward(request, response);
			}
			
				
			
			
		}
		else {  // GET 방식으로 접근한 경우라면  
		        // 아무것도 일어나지 않도록 하려면 코딩을 안해주면 된다.
		}
		
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
	}

}
