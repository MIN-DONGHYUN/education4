package member.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.controller.AbstractController;
import member.model.MemberVO;

public class MemberlistAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// 세션값 얻어오기 
		HttpSession session = request.getSession();
		MemberVO loginuser= (MemberVO) session.getAttribute("loginuser");
		
		
		// 관리자로 로그인 했을때만 적용하도록 한다.
		if(loginuser != null && loginuser.getUserid().equals("admin")) {  
			//System.out.println("~~~~ 관리자만 보는 페이지 입니다.!!!!");
			// ~~~~ 관리자만 보는 페이지 입니다.!!!!
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/member/memberList.jsp");
			
			
			
			
		}
		else {		// 로그인 아이디가 admin 이 아닌경우와 로그인을 안한 경우에는 
			//System.out.println(" !!! 접근금지 !!! ");
			//  !!! 접근금지 !!! 
			String message = "관리자만 접근이 가능합니다.";
			String loc = "javascript:history.back()";

			request.setAttribute("message", message);
			request.setAttribute("loc", loc);
			
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");
			
		}
		
		
		

	}

}
