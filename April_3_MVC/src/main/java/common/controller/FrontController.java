package common.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(
		description = "사용자가 웹에서 *.up 을 했을 경우 이 서블릿이 응답을 해주도록 한다.",
		urlPatterns = { "*.up" }, 								// naver.up 이든 kakao.up 이든 .up 붙은 것 모두 가능하다.
		initParams = { 
				@WebInitParam(name = "propertyConfig", value = "C:\\NCS\\workspace(JSP)\\MyMVC\\src\\main\\webapp\\WEB-INF\\Command.properties", description = "*.up 에 대한 클래스의 매핑파일")
		})														// \\ 를 해야만  \ 로 알아듣는다. 또는  / 로 변경하면 된다.

public class FrontController extends HttpServlet {
	
	private static final long serialVersionUID = 1L;


	public void init(ServletConfig config) throws ServletException {
	/*
		  웹 브라우저 주소창에서 *.up 을 하면 FrontController 서블릿이 응대를 해오는데 
		  맨 처음에 자동적으로 실행되어지는 메소드가 init(ServletConfig config) 이다.
		  여기서 중요한 것은 init(ServletConfig config) 매소드는 WAS(톰캣) 가 구동되어진 후
		  딱 1번만 init(ServletConfig config) 매소드가 실행되어지고, 그 이후에는 실행이 되지 않는다.
		  그러므로 init(ServletConfig config) 매소드에는 FrontController 서블릿이 동작해야할 환경설정을 잡아주는데 사용된다.
	*/	
		// *** 확인용 *** //
		//System.out.println("~~~ 확인용 => 서블릿 FrontController 의 init(ServletConfig config) 매소드가 실행됨.");   //한번만 실행이 된다.
		// 결과 => ~~~ 확인용 => 서블릿 FrontController 의 init(ServletConfig config) 매소드가 실행됨.
		
		FileInputStream fis = null;   			// 파일을 읽어 들어오는 용도로 쓸 예정 
		// 특정 파일에 있는 내용을 읽어오기 위한 용도로 쓰이는 객체
		
		String props = config.getInitParameter("propertyConfig");     // 이것은 String 타입을 사용하며 propertyConfig 는 value 값인 C:\\NCS\\workspace(JSP)\\MyMVC\\src\\main\\webapp\\WEB-INF\\Command.properties 를 가리켜 이것을 출력한다.
		
		//System.out.println("~~~확인용 props => " + props);
		// 결과 : ~~~확인용 props => C:\NCS\workspace(JSP)\MyMVC\src\main\webapp\WEB-INF\Command.properties

		try {
			fis = new FileInputStream(props);							 // 파라미터에는 경로 이름을 쓰자 
			// fis 는 props 즉 C:\NCS\workspace(JSP)\MyMVC\src\main\webapp\WEB-INF\Command.properties 파일의 내용을 읽어오기 위한 용도로 쓰이는 객체이다.
			
		} 
		catch (FileNotFoundException e) {								 // 파일이름이 없는것을 써왔다면 File 이 없다는 오류를 발생시킨다.
			System.out.println(">>> C:\\NCS\\workspace(JSP)\\MyMVC\\src\\main\\webapp\\WEB-INF\\Command.properties 이러한 파일이 없습니다. <<<");
			e.printStackTrace();
		}  							  
		
		
		
		
		
		
		
	}// end of public void init(ServletConfig config) throws ServletException {


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
	
		
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
	}

}
