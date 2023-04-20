package member.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.controller.AbstractController;
import member.model.InterMemberDAO;
import member.model.MemberDAO;
import member.model.MemberVO;

public class MemberOneDetailAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {


		// 세션값 얻어오기 
		HttpSession session = request.getSession();
		MemberVO loginuser= (MemberVO) session.getAttribute("loginuser");
		
		
		// 관리자로 로그인 했을때만 적용하도록 한다.
		if(loginuser != null && loginuser.getUserid().equals("admin")) {  
			//System.out.println("~~~~ 관리자만 보는 페이지 입니다.!!!!");
			// ~~~~ 관리자만 보는 페이지 입니다.!!!!
			
			
			String userid = request.getParameter("userid");   // userid 를 가져온다.
			
			// 데이터 베이스 가져오기 위해 
			InterMemberDAO mdao = new MemberDAO();
			
			MemberVO mvo = mdao.MemberOneDetail(userid);		// 한명만 상세히 보이기 위해 메소드 생성 
			
			
			request.setAttribute("mvo", mvo);			// request 에 저장 
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/member/MemberOneDetail.jsp");   // 이동 
			
			
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
