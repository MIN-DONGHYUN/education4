package member.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.controller.AbstractController;
import member.model.MemberVO;

public class LogoutAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// 로그아웃 처리하기 
		HttpSession session = request.getSession();// 세션을 불러오기 (로그인 된것을 가져오는 것이다.)
				
		////////////////////////////////////////////////////////////////////////////////////////
			
		// 로그아웃을 하면 시작페이지로 가는 것이 아니라 방금 보았던 그 페이지로 그대로 가기 위한 것임.
		String goBackURL = (String) session.getAttribute("goBackURL");
		
		// 유저 아이디 받아오기 
		MemberVO loginuser = (MemberVO) session.getAttribute("loginuser");
		

		// 첫번째 방법 : 세션은 그대로 존재하게 끔 해두고 세션에 저장되어진 어떤 값(지금은 로그인 되어진 회원 객체)을 삭제한다.   (특정값만 없애는것)
		//session.removeAttribute("loginuser");   		
		
		
		// 두번재 방법 : WAS의 메모리 상에서 세션을 아예 clear(없애는것) 해오는 것이다. (아예 없애는 것) 다시 연결할려면 session 이 다시 생성되어진다.   
		session.invalidate();
				
		///////////////////////////////////////////////////////////////////////////////////////////
		
		super.setRedirect(true);
		
		if(goBackURL != null && !"admin".equals(loginuser.getUserid()) ) {
			
			super.setViewPage(request.getContextPath()+goBackURL);	
		}
		else {
			// 로그아웃이 되면은 첫페이지로 가자
			super.setViewPage(request.getContextPath()+"/index.up");
		}
			
	}

}
