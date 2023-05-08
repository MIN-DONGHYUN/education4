package member.controller;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.controller.AbstractController;
import member.model.*;

public class PwdUpdateEndAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String method = request.getMethod();	// "GET"  또는  "POST"
		
		String userid = request.getParameter("userid");   // 값을 받아온다.
		
		// POST 방식이니?
		if("post".equalsIgnoreCase(method)) {
			// 암호 변경하기 버튼을 클릭한 경우
			
			String pwd = request.getParameter("pwd");		// post 방식으로 넘긴 값들을 받아온다.
			
			Map<String, String> paraMap = new HashMap<>();
			
			paraMap.put("pwd", pwd);   // Map 에 저장 
			paraMap.put("userid", userid);   // Map 에 저장 
			
			//sql 쓰기 위해 넘김
			InterMemberDAO mdao = new MemberDAO();
			int n = mdao.pwdUpdate(paraMap);
			
			
			request.setAttribute("n", n);    // n값을 MemberDAO 에 넘겨준다.
			
			
			
			
		}
		
		request.setAttribute("method", method);  // pwdUpdateEnd.jsp 페이지에 넘긴것이다.
		request.setAttribute("userid", userid);  // pwdUpdateEnd.jsp 페이지에 넘긴것이다.
		

		super.setRedirect(false);
		super.setViewPage("/WEB-INF/login/pwdUpdateEnd.jsp");  // view 단 페이지를 볼 수 있다.
	}

}
