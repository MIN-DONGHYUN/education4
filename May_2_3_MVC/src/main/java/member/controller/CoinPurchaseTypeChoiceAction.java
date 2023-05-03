package member.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.controller.AbstractController;
import member.model.MemberVO;

public class CoinPurchaseTypeChoiceAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// 코인충전을 하기 위한 전제조건은 먼저 로그인을 해야 하는 것이다. 
		if(super.checkLogin(request) ) {		// 부모에 있는 checkLogin 함수를 실행해서 이것이 true 라면 실행 
			// 로그인을 했으면

			// 로그인 한 사람의 코인 충전이 아닌 다른 사람의 코인 충전을 하게 된다면 못하도록 막아두도록 한다.
			String userid = request.getParameter("userid");    // request 영역의 userid 를 받아온다.
			HttpSession session = request.getSession();		   // session 에 있는것을 받아온다.
			
			MemberVO loginuser = (MemberVO) session.getAttribute("loginuser");    // 애는 리턴타입이 object 이지만 캐스팅 해버린다.
			  																	  // if 안에 있기 때문에 값은 무조건 존재한다. null 걱정 X 
			/*
				이 코드는 HttpSession 객체에서 "loginuser" 속성을 가져와서 MemberVO 클래스의 객체로 형변환하여 
				loginuser 변수에 할당하는 코드입니다.

				즉, 로그인한 사용자의 정보를 담고 있는 MemberVO 클래스의 객체를 세션에서 가져와서 loginuser 변수에 할당합니다. 
				이렇게 가져온 로그인 사용자 정보를 이용하여 로그인한 사용자에게만 허용되는 기능이나 페이지를 제한적으로 제공하거나,
				사용자 정보를 화면에 출력하는 등의 기능을 수행할 수 있습니다.
			*/
			if(loginuser.getUserid().equals(userid)) {
				// 로그인한 사용자가 자신의 코인을 충전하는 경우 
				super.setRedirect(false);
				super.setViewPage("/WEB-INF/member/coinPurchaseTypeChoice.jsp");			// 이 페이지로 이동시켜 실행 시킨다.
			}
			else {
				// 로그인한 사용자가 다른 사용자의 코인을 충전하려고 시도하는 경우 
				String message = "다른 사용자의 코인 충전 시도는 불가합니다.";
				String loc = "javascript:history.back()";     // 이전 페이지로 이동시켜버린다.
				
				request.setAttribute("message", message);		// 메세지를 나타내기 위해 
				request.setAttribute("loc", loc);
				
				super.setRedirect(false);
				super.setViewPage("/WEB-INF/msg.jsp");			// 이 페이지로 이동시켜 실행 시킨다.
			}
								
			
			
			
		}
		else {
			// 로그인을 안했으면
			String message = "코인충전을 하기 위해서는 먼저 로그인을 하세요!!";
			String loc = "javascript:history.back()";     // 이전 페이지로 이동시켜버린다.
			
			request.setAttribute("message", message);		// 메세지를 나타내기 위해 
			request.setAttribute("loc", loc);
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");			// 이 페이지로 이동시켜 실행 시킨다.
		}

	}
}
