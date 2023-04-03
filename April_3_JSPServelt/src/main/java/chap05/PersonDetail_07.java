package chap05;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/personDetail.do")
public class PersonDetail_07 extends HttpServlet {
	private static final long serialVersionUID = 1L;

	InterPersonDAO_03 dao = new PersonDAO_04();
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String seq = request.getParameter("seq");  // seq 는 이름  // seq 가 숫자로 넘어올때만 해줘여 한다. 
												   // "3" 또는 "123123" 또는 "ㅁㄴㅇㅁㄴㅁㄴㄹ" 일때 숫자 제외는 오류 발생하게 함
		String path = "";
		
		try {
			Integer.parseInt(seq);    // 숫자를 제외한 것은 오류 발생 
			
			// 넘어온 회원정보 번호가 숫자일 경우에는 select로 데이터를 받아온다.
			PersonDTO_02 psdto = dao.selectOne(seq);
			
			if(psdto != null) {
				request.setAttribute("psdto", psdto);
				path = "/WEB-INF/chap05_right/personDetail.jsp";
			}
			else {	// 장난쳐왔을때 
				path = "/WEB-INF/chap05_right/warning.jsp";  // 경고 페이지로 넘어간다.
			}
			
		}
		catch(NumberFormatException e) {
			
			path = "/WEB-INF/chap05_right/warning.jsp";  // 경고 페이지로 넘어간다.
		}
		catch(SQLException e) {
			path="/WEB-INF/chap05_right/sql_error.jsp";
		}
		
		RequestDispatcher dispatcher = request.getRequestDispatcher(path);
		dispatcher.forward(request, response);
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
	}

}
