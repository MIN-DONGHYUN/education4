package member.controller;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.controller.AbstractController;
import member.model.*;

public class LoginAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String method = request.getMethod();    // "GET" 또는 "POST" 
		
		if(!"post".equalsIgnoreCase(method)) {
			// POST 방식으로 넘어온것이 아니라면 
			
			String message = "비정상적인 경로로 들어왔습니다.";
			String loc = "javascript:history.back()";			// 이전 페이지로 이동하는 것이다.
			
			request.setAttribute("message", message); 			// message 를 msg.jsp 로 넘긴다.
			request.setAttribute("loc", loc); 					// loc 를 msg.jsp 로 넘긴다.
			
			super.setRedirect(false);							// post 방식이 아니라면 
			super.setViewPage("/WEB-INF/msg.jsp");				// msg.jsp 로 넘긴다.
			
			return;   // execute(HttpServletRequest request, HttpServletResponse response) 종료 
		}
		
		
		// ---> 클라이언트의 IP 주소를 알아오는 것 <----
		String clientip = request.getRemoteAddr();
		// C:\NCS\workspace(JSP)\MyMVC\src\main\webapp\JSP 파일을 실행시켰을 때 IP 주소가 제대로 출력되기위한 방법.txt 이 파일 참조할 것
		
		// POST 방식일때만 실행되는 곳이다. 
		String userid = request.getParameter("userid");
		String pwd = request.getParameter("pwd");
		
		
		//System.out.println("~~~~ 확인용 userid => " + userid);
		//System.out.println("~~~~ 확인용 pwd => " + pwd + ", ip주소" + clientip);
		// ~~~~ 확인용 userid => leess
		// ~~~~ 확인용 pwd => qwer1234$,  ip주소127.0.0.1
		
		
		// Map 쓰는 이유 DAO 에 보내겠다.
		Map<String, String> paraMap = new HashMap<>();    // DAO에 보내겠다.
		
		paraMap.put("userid", userid);
		paraMap.put("pwd", pwd);
		paraMap.put("clientip", clientip);
		
		
		InterMemberDAO mdao = new MemberDAO();
		
		
		MemberVO loginuser = mdao.selectOneMember(paraMap);       // memberdao로   즉 Map 에 담아서 넘겨줄것이다.
		
		// null 이 아니라면 DB에 존재한다는 것이다. 로그인 할 수 있음 
		if(loginuser != null ) {
			
			if(loginuser.getIdle() == 1) {
				
				String message = "로그인을 한지 1년이 지나서 휴먼상태로 되었습니다. 관리자에게 문의 바랍니다.";
				String loc = request.getContextPath() + "/index.up";		// 이전 페이지를 이동 시키기 위해 
				// 원래는 위와 같이 index.up 이 아니라 휴먼인 계정을 풀어주는 페이지로 잡아주어야 한다. (프로젝트에서 해보자)
				
				
				request.setAttribute("message", message);
				request.setAttribute("loc", loc);
				
				super.setRedirect(false);
				super.setViewPage("/WEB-INF/msg.jsp");
				
				return;			// 매소드 종료
				
			}
			
			// 로그인이 성공시
			System.out.println(">>> 확인용 로그인한 사용자명 : " + loginuser.getName());
			
			// !!!! session(세션) 이라는 저장소에 로그인 되어진 loginuser 을 저장시켜두어야 한다.!!!! //
	         // session(세션) 이란 ? WAS 컴퓨터의 메모리(RAM)의 일부분을 사용하는 것으로 접속한 클라이언트 컴퓨터에서 보내온 정보를 저장하는 용도로 쓰인다. 
	         // 클라이언트 컴퓨터가 WAS 컴퓨터에 웹으로 접속을 하기만 하면 무조건 자동적으로 WAS 컴퓨터의 메모리(RAM)의 일부분에 session 이 생성되어진다.
	         // session 은 클라이언트 컴퓨터 웹브라우저당 1개씩 생성되어진다. 
	         // 예를 들면 클라이언트 컴퓨터가 크롬웹브라우저로 WAS 컴퓨터에 웹으로 연결하면 session이 하나 생성되어지고 ,
	         // 또 이어서 동일한 클라이언트 컴퓨터가 엣지웹브라우저로 WAS 컴퓨터에 웹으로 연결하면 또 하나의 새로운 session이 생성되어진다. 
	         /*
	               -------------
	               | 클라이언트    |             ---------------------
	               | A 웹브라우저  | ----------- |   WAS 서버         |
	               -------------             |                  |
	                                         |  RAM (A session) |
	               --------------            |      (B session) | 
	               | 클라이언트     |            |                  |
	               | B 웹브라우저   | ---------- |                  |
	               ---------------           --------------------
	               
	           !!!! 세션(session)이라는 저장 영역에 loginuser 를 저장시켜두면
	                Command.properties 파일에 기술된 모든 클래스 및  모든 JSP 페이지(파일)에서 
	                세션(session)에 저장되어진 loginuser 정보를 사용할 수 있게 된다. !!!! 
	                그러므로 어떤 정보를 여러 클래스 또는 여러 jsp 페이지에서 공통적으로 사용하고자 한다라면
	                세션(session)에 저장해야 한다.!!!!          
	          */
			
			HttpSession session = request.getSession();    // 암기하기 !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
			// WAS 메모리 RAM 에 생성되어져 있는 session 을 불러오는 것이다.
			
			session.setAttribute("loginuser", loginuser);      // key 값은 login.jsp 파일의 168줄에 간다.
			// session(세션) 에 로그인 되어진 사용자 정보인 loginuser 을 키 이름을 "loginuser" 으로 저장시켜두는 것이다.
			
			
			// 휴먼처리를 풀기 위해서 만듬
			if(loginuser.isRequirepwdChange() == true) {    //session 에 저장된 로그인정보 한개를 불러오자
				String message = "비밀번호를 변경하신지 3개월이 지났습니다. 암호를 변경하세요!!";				//msg로 alert 띄운다.
				String loc = request.getContextPath() + "/index.up";		// 시작 페이지를 이동 시키기 위해 
				// 원래는 위와 같이 index.up 이 아니라 사용자의 암호를 변경해주는 페이지로 잡아 주어야 한다. 프로젝트에서 그렇게 하자!
					
				
				request.setAttribute("message", message);
				request.setAttribute("loc", loc);
				
				super.setRedirect(false);
				super.setViewPage("/WEB-INF/msg.jsp");
			}
			else {		// 비밀번호 바꾼지 3개월 이내인 경우에는 휴먼 풀기를 안해도 된다.
				super.setRedirect(true);     // 페이지 이동을 시킨다.
				
				// 로그인을 하면 시작 페이지(index.up) 로 가는 것이 아니라 로그인을 시도 하려고 머물렀던 그 페이지로 가기 위한 것이다.
				String goBackURL = (String) session.getAttribute("goBackURL");   // session 값에서 가져온다.
				// goBackURL 은 /shop/prodView.up?pnum=65
				// 또는 
				// goBackURL 은 null 
				
				// goBackURL 이 session에 있다면 
				if(goBackURL != null) {
					super.setViewPage(request.getContextPath() + goBackURL);
				}
				else {
					super.setViewPage(request.getContextPath() + "/index.up");
				}

			}
			
		}
		else {   // null 이면 로그인 실패를 뜻한다.
			String message = "로그인 실패";
			String loc = "javascript:history.back()";		// 이전 페이지를 이동 시키기 위해 
			
			request.setAttribute("message", message);
			request.setAttribute("loc", loc);
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");
		}
		
		
		
	}

}
