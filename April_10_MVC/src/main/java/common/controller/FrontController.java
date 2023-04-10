// 서블릿 페이지

package common.controller;

import java.io.*;
import java.lang.reflect.Constructor;
import java.util.*;

import javax.servlet.RequestDispatcher;
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

	
	// Object 는 다형성을 위함 (모든 다 들어가야함)
	Map<String, Object> cmdMap = new HashMap<>();
		
	
	// init 는 한번만 실행 
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
			
			Properties pr = new Properties();    // properties를 만든다.
			/*
			 	Properties 는 HashMap의 구버전인 Hashtable을 상속받아 구현한 것으로,
				Hashtable 은 키와 값(Object, Object)의 형태로 저장하는데 비해서
				Properties 는 (String 키, String 밸류값)의 형태로 저장하는 단순화된 컬렉션 클래스이다. (오로지 String 키, 벨류값이다.)
			    키는 고유해야 한다. 즉, Key 는 중복을 허락하지 않는다. 중복된 값을 넣으면 마지막에 넣은 값으로 덮어씌운다.
			    주로 어플리케이션의 환경설정과 관련된 속성(property)을 저장하는데 사용되며, 
			    데이터를 파일로 부터 읽고 쓰는 편리한 기능을 제공한다.    
			*/
			
			pr.load(fis);     // 로드에 inputStream
			/*
				pr.load(fis); fis 객체를 사용하여 C:\NCS\workspace(JSP)\MyMVC\src\main\webapp\WEB-INF\Command.properties 파일의 내용을 읽어다가 
				properties 클래스의 객체인 pr 에 로드시킨다.
				그러면 pr 에 읽어온 파일(Command.properties)의 내용에서
				= 를 기준으로 왼쪽은 key로 보고, 오른쪽은 value 값으로 인식한다. 
			*/
			
			Enumeration<Object> en = pr.keys();			// = 중심으로 왼편에 있는 key 값만 다 읽어온다.
			/*
				C:\NCS\workspace(JSP)\MyMVC\src\main\webapp\WEB-INF\Command.properties 파일의 내용물 에서 
				= 을 기준으로 왼쪽에 있는 모든 key 들만 가져오는 것이다.
			*/
			
			while(en.hasMoreElements()) {           // 키가 있냐를 판별하는 것이 hasMoreElements()
				
				String key = (String) en.nextElement();                   // 키가 있다면 키를 가져오는것이다.  (Command.properties 파일에서 키를 가져옴)
				
				//System.out.println("~~~~~~ 확인용 key => " + key);
				// 결과 : ~~~~~~ 확인용 key => /test/test2.up
				// 결과 : ~~~~~~ 확인용 key => /test1.up
				// 결과는 뒤에서부터 앞 순으로 나오는것 같다.
				
				
				
				// key를 알았으므로 value 값을 알아보자 
				//System.out.println("### 확인용 value => " + pr.getProperty(key));     // key를 넣어주면은 그 키에 매핑되어져 있는 value 값이 나오게 된다. // 리턴 타입은 String 
				// 결과 : ### 확인용 value => test.controller.Test2Controller
				// 결과 : ### 확인용 value => test.controller.Test1Controller
				
				
				String className = pr.getProperty(key);     // String 타입
				
				// value 값이 없는것을 방지하기 위해 
				if(className != null)
				{
					
					className = className.trim();    //혹시 모를 좌,우 공백을 제거하자 
					
					Class<?> cls = Class.forName(className);        // Class.forName은 실제로 클래스 화를 시키는 것이다. 
																	// Class 가 존재하지 않으면 오류가 발생한다. 
																	// 오류는 >>> 문자열로 명명되어진 클래스가 존재하지 않습니다. 
																	// java.lang.ClassNotFoundException: test.controller.Test2Controller 이렇게 나온다.
					// <?> 은 아무거나 들어갈 수 있다는 말이다.(클래스 타입이 뭔지 모른다는 것);
					// 즉, <?> 은 generic 인데 어떤 클래스 타입인지는 모르지만 하여튼 클래스 타입이 들어온다는 뜻이다.
					// String 타입으로 되어진 className 을 클래스화 시켜주는 것이다.
					// 주의 할 점은 실제로 String 으로 되어져 있는 문자열이 클래스로 존재해야만 한다는 것이다.
					
					
					// 생성자 만들기 부분
					Constructor<?> constrt =  cls.getDeclaredConstructor();
					
					// 생성자로 부터 실제 객체(인스턴스) 를 생성해 주는 것이다.
					Object obj = constrt.newInstance();     // 생성자 매소드를 호출해 오는 것이다.
					
					//System.out.println("~~~ 확인용 => " + obj);
					// 그냥 obj 할 때 결과들  
					// 결과 : ~~~ 확인용 => test.controller.Test2Controller@4b39b82
					// 결과 : ~~~ 확인용 => test.controller.Test1Controller@8c1fcc
					
					
					//System.out.println("~~~ 확인용 => " + obj.toString() );
					// obj.toString() 할 때 결과 들 (해당 메소드를 호출 해왓다,
					// 결과 : ~~~ 확인용 => ### 클래스 Test2Controller 의 인스턴스 매소드 toString() 을 호출함 ###
					// 결과 : ~~~ 확인용 => @@@ 클래스 Test1Controller 의 인스턴스 매소드 toString() 을 호출함 @@@
					
					
					cmdMap.put(key, obj);    //Map 에 넣는다.
					// 여기서 키는 /test1.up 이거 이거나 /test/test2.up 이것 이다. 
					/*
						cmdMap 에서 키 값으로 Command.properties 파일에 저장되어진 url 을 주면 
						cmdMap 에서 해당 클래스에 대한 obj(객체)(인스턴스) 를 얻어오도록 만든 것이다.
					*/
					
					
				} // end of if(className != null)
				
			}// end of while(en.hasMoreElements()) 
			
			
		} 
		catch (FileNotFoundException e) {								 // 파일이름이 없는것을 써왔다면 File 이 없다는 오류를 발생시킨다.
			System.out.println(">>> C:\\NCS\\workspace(JSP)\\MyMVC\\src\\main\\webapp\\WEB-INF\\Command.properties 이러한 파일이 없습니다. <<<");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch(ClassNotFoundException e) {								// 클래스가 없으면 오류
			System.out.println(">>> 문자열로 명명되어진 클래스가 존재하지 않습니다.");
			e.printStackTrace();
		} catch (Exception e) {											// 위 에러 이후 나머지 에러들은 이것이 처리해준다.
			e.printStackTrace();
		}
		
		
		
		
		
		
		
		
	}// end of public void init(ServletConfig config) throws ServletException {


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		// 웹브라우저 주소 입력창에서 
		//http://localhost:9090/MyMVC/member/idDuplicateCheck.up?userid=leess 와 같이 입력되었더라면 
		
		// 실제 url 만 나타내보자 
		//String url = request.getRequestURL().toString();
		//System.out.println("~~~ 확인용 url => " + url);
		// 결과 : ~~~ 확인용 url => http://localhost:9090/MyMVC/member/idDuplicateCheck.up 
		
		
		
		// uri를 통해 나타내 보자 
		// 웹브라우저 주소 입력창에서 
		//http://localhost:9090/MyMVC/member/idDuplicateCheck.up?userid=leess 와 같이 입력되었더라면 
		String uri = request.getRequestURI();   // 리턴 타입은 String		
		//System.out.println("~~~ 확인용 uri => " + uri);
		// 결과 : ~~~ 확인용 uri => /MyMVC/member/idDuplicateCheck.up   (앞에 포트 번호는 없다.)
		// 결과 : ~~~ 확인용 uri => /MyMVC/test1.up
		// 결과 : ~~~ 확인용 uri => /MyMVC/test/test2.up
		
		
		
		//request.getContextPath().length();  // /MYMVC
		
		// uri 를 통해 나타내는데 /MVMVC 는 변경 가능성이 있으니 빼보자 
		String key = uri.substring(request.getContextPath().length());
		// 현재 이 값은 /MYMVC 이고 길이는 6이므로 6을 subString 에 넣어준다.  값은 =>  /member/idDuplicateCheck.up
		//System.out.println(key);
		// 결과 : /member/idDuplicateCheck.up
		// 결과 : /test1.up
		// 결과 : /test/test2.up
		
		
		
		// 값을 꺼내오자 
		AbstractController action = (AbstractController)cmdMap.get(key);   // 타입은 Object 로 나오게 된다. () 으로 캐스팅 할 수 있음
						// 맵에 저장되어진 객체를 꺼내온다.
		
		
		// key 가 없는 것을 가져오면 
		if(action == null) {
			System.out.println(">>> " + key + "은 URI 패턴에 매핑된 클래스가 없습니다. <<<");
			// 실행 : http://localhost:9090/MyMVC/member/idDuplicateCheck.up?userid=leess 이것을 uri 에 쳤을때 
			// 결과 : >>> /member/idDuplicateCheck.up은 URI 패턴에 매핑된 클래스가 없습니다. <<<
			// 실행 : http://localhost:9090/MyMVC/test1.up
			// 결과 없음 
		}
		else {
			try {
				
				/*
	                post 방식으로 넘어온 데이터중 영어는 글자가 안깨지지만,
	                한글은 글자모양이 깨져나온다.
	                그래서  post 방식에서 넘어온 한글 데이터가 글자가 안깨지게 하려면 
	                아래처럼 request.setCharacterEncoding("UTF-8"); 을 해야 한다.
	                주의할 것은 request.getParameter("변수명"); 보다 먼저 기술을 해주어야 한다는 것이다.      
				*/
				
				request.setCharacterEncoding("UTF-8");
				
				
				
				action.execute(request, response);
				// test1 이면 호출되는것은 test1Controller 에 오버라이딩을 호출
				
				// 불러오기
				boolean bool = action.isRedirect();     // boolean 으로 해왔기 때문에 isRedirect() 를 사용
				String viewPage = action.getViewPage();   // view 단 페이지를 읽어왔다.
				
				// false 이라면 (지금은 test1 가 돌아감)
				if(!bool) {
					// ViewPage 에 명기된 view 단 페이지로 forward(dispatcher)를 하겠다는 말이다.
					// forward 되어지먄 웹브라우저의 URL 주소는 변경되지 않고 그대로 이면서 화면에 보여지는 내용은 forward 되어지는 .jsp 파일이다.
					// 또한 forward 방식은 forward 되어지는 페이지로 데이터를 전달할 수 있다는 것이다.
					
					
					// viewPage 가 null 이 아니여야만 한다.
					if(viewPage != null) {
						RequestDispatcher dispatcher = request.getRequestDispatcher(viewPage);
						dispatcher.forward(request, response);
					}
					
				}
				// true 라면 (지금은 test2 가 돌아감)
				else {
					// viewPage 에 명기된 주소로 sendRedirect(웹브라우저의 URL 주소 변경됨)를 하겠다는 말이다.
					// 즉, 단순히 페이지 이동을 하겠다는 말이다.
					// 암기할 내용은 sendRidirect 방식은 sendRidirect 되어지는 페이지로 데이터를 전달 할 수 없다는 것이다. (데이터 전달은 forward 밖에 못한다.)
					
					if(viewPage != null) {
						response.sendRedirect(viewPage);
					}
				} // end of else
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
	}

}
