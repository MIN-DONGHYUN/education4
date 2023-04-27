package member.controller;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.controller.AbstractController;
import member.model.*;

public class IdFindAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String method = request.getMethod();
		// "GET" 또는 "POST" 
		
		// POST 방식이라면 
		if("POST".equalsIgnoreCase(method)) {  
			
			// 아이디 찾기 모달창에서 찾기 버튼을 클릭했을 경우 
			// idFind 에서 form 태그에 있는 name, email 을 받아온다. (POST 방식으로 받아옴)
			
			String name = request.getParameter("name");   // 이름 값을 받아온다. 
			String email = request.getParameter("email");   // 이메일 값을 받아온다. 
			
			//DB에 보내자 
			InterMemberDAO mdao = new MemberDAO();
			
			Map<String, String> paraMap = new HashMap<>();		// 맵에 저장하기 위해 선언
			
			paraMap.put("name", name);
			paraMap.put("email", email);		// Map 에 저장한다.
			
			
			String userid =  mdao.findUserid(paraMap); // 보내 주자 // 아이디 값이므로 리턴 값은 String
			
			//userid 값이 null 인지 판단 
			if(userid != null) {
				request.setAttribute("userid" , userid);     // request 영역에 저장 (값을 저장함)
			}
			else {   //userid가 null 이라면 
				request.setAttribute("userid" , "존재하지 않습니다."); // null 이므로 존재하지 않습니다를 저장 
			}
			
			
			request.setAttribute("name", name);			// POST 방식일때 이름, 이메일이 넘어간다.
			request.setAttribute("email", email);
			
		}// end of if("post".equalsIgnoreCase(method)) { 
		
		request.setAttribute("method", method);   //GET 방식은 이것만 넘어간다.
		
		// GET이든 POST든 똑같은 페이지에서 나타낸다.
		super.setRedirect(false);   // form 태그가 나오게 하기 위해서 false를 준다.
		super.setViewPage("/WEB-INF/login/idFind.jsp"); 		// form 태그를 가져오기 위해 

	}

}
