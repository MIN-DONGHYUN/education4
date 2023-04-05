package member.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.controller.AbstractController;

public class MemberRegisterAction extends AbstractController {		// 부모 클래스는 AbstractController

	// 부모클래스의 것을 매소드 오버라이딩 한다.
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		
		// 무조건 회원가입 페이지만 나오게 하지 않고 get 방식과 post 방식을 구분해서 get 방식으로 받아 왔다면 회원가입 form 으로 가고 post 방법으로 받으면 회원가입 제출 한 곳으로 가자
		String method = request.getMethod(); // get or post
		
		if("GET".equalsIgnoreCase(method)) {
			
			//super.setRedirect(false);    // 기본 부모에 설정되어 있음
			super.setViewPage("/WEB-INF/member/memberRegister.jsp");  // 회원가입 form 태그 페이지 이다. 
			
		}
		else {			// "POST" 방식
			
			// 내일 이어서 할것이기 때문에  임시방편
			super.setRedirect(true);
			super.setViewPage(request.getContextPath()+"/index.up");
			
			
		}		
	}
}
