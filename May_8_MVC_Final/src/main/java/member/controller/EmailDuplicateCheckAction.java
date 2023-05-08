package member.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import common.controller.AbstractController;
import member.model.InterMemberDAO;
import member.model.MemberDAO;

public class EmailDuplicateCheckAction extends AbstractController {

	// 이메일 중복검사
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		
		String method = request.getMethod();		// "GET" 또는 "POST"
		
		if("post".equalsIgnoreCase(method)) {	// POST 이라면   이따 post 로 바꾸기
			String email = request.getParameter("email");
			
			System.out.println("확인용 email => " + email);
			// 결과 : leess@naver.com
			
			
			// DB에 보내자 (DAO에 보내서 계속함)
			InterMemberDAO mdao = new MemberDAO();
			
			boolean isExists = mdao.emailDuplicatecheck(email);
			// true 아니면 false 둘중 하나만 나옴 
			
			JSONObject jsonObj = new JSONObject();	// import는 simple보단 그냥 simple 아닌것을 쓰자 (simple 은 휴대폰 문자 보내기만 )
															// {} 는 자바스크립트의 객체 
			
			jsonObj.put("isExists", isExists);              // 뒤에는 value 값    
															//{"isExists":true} 또는 
															//{"isExists":false} 이 나올 것이다.
			
			String json = jsonObj.toString();			// 문자열 형태인 "{"isExists":true}" 또는 "{"isExists":false}" 으로 만들어 준다.
			//System.out.println("확인용 => json " + json);
			
				//확인용 => json {"isExists":false} 없을 경우
				//확인용 => json {"isExists":true} 있을 경우
			
			
			request.setAttribute("json", json);
			
			
			
			//super.setRedirect(false);
			super.setViewPage("/WEB-INF/jsonview.jsp");
			
		}

	}

}
