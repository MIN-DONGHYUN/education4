package chap05;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/personSelect.do")
public class PersonSelect_06 extends HttpServlet {
	private static final long serialVersionUID = 1L;

	InterPersonDAO_03 dao = new PersonDAO_04();
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// tbl_person_interset 테이블에 저장되어진 행(데이터)을 읽어다가(select) 웹페이지에 보여주어야 한다.
		String path ="";
		
		try {
			
			List<PersonDTO_02> personList = dao.selectAll();     // 데이터를 읽어오는 것은 List<DTO>
			request.setAttribute("personList", personList);
			
			path = "/WEB-INF/chap05_right/personSelectAll.jsp";
			
		} catch (SQLException e) {
			e.printStackTrace();
			path = "/WEB-INF/chap05_right/sql_error.jsp";
		}
		
		RequestDispatcher dispatcher = request.getRequestDispatcher(path);
		dispatcher.forward(request, response);
		 
		
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doGet(request, response);
	}

}
