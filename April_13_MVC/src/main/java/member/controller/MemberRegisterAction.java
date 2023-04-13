package member.controller;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.controller.AbstractController;
import member.model.InterMemberDAO;
import member.model.MemberDAO;
import member.model.MemberVO;

public class MemberRegisterAction extends AbstractController {		// 부모 클래스는 AbstractController

	// 부모클래스의 것을 매소드 오버라이딩 한다.
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		
		// 무조건 회원가입 페이지만 나오게 하지 않고 get 방식과 post 방식을 구분해서 get 방식으로 받아 왔다면 회원가입 form 으로 가고 post 방법으로 받으면 회원가입 제출 한 곳으로 가자
		String method = request.getMethod(); // get or post
		
		if("GET".equalsIgnoreCase(method)) {   
			 
			// super 든 this 든 가능
			super.setRedirect(false);    // 기본 부모에 설정되어 있음 다시 반복하려면 주석을 풀어야만 한다.     // isRedurect ==> false
			super.setViewPage("/WEB-INF/member/memberRegister.jsp");  // 회원가입 form 태그 페이지 이다. 
			
		}
		else {			// "POST" 방식
			
			//  임시방편
			//this.setRedirect(true);         // isRediret ==> true;    
			//super.setViewPage(request.getContextPath()+"/index.up");
			
			// 본격 시작 부분
			String name = request.getParameter("name");			// form 태그의 것을 받아온다.	
			String userid = request.getParameter("userid");	
			String pwd = request.getParameter("pwd");	
			String email = request.getParameter("email");	
			String hp1 = request.getParameter("hp1");	
			String hp2 = request.getParameter("hp2");	
			String hp3 = request.getParameter("hp3");		
			String postcode = request.getParameter("postcode");	
			String address = request.getParameter("address");
			String detailAddress = request.getParameter("detailAddress");
			String extraAddress = request.getParameter("extraAddress");
			String gender = request.getParameter("gender");		
			String birthyyyy = request.getParameter("birthyyyy");	
			String birthmm = request.getParameter("birthmm");	
			String birthdd = request.getParameter("birthdd");	
			
			// 온전한 연락처
			String mobile = hp1 + hp2 + hp3;
			// 온전한 생년월일
			String birthday = birthyyyy + "-" + birthmm + "-" + birthdd;    // 1993-04-25
			
			// 파라미터가 있는 MemberVO를 가져온다.
			MemberVO member = new MemberVO(userid, pwd, name, email, mobile, postcode, address, detailAddress, extraAddress, gender, birthday);
			// DAO 에 MemberVO member를 보내주려고 한다. 
			
			String message = "";   // 이 메시지는 alert에 나올 메시지
			String loc = "";	   // 위치를 받아올 것 
			
			// 매소드 호출 하기 
			try {
				
				InterMemberDAO mdao = new MemberDAO();
				int n = mdao.registerMember(member);    // 리턴 타입 int
			
				if(n==1) {   // 값을 받아왔다면 
					message = "회원가입 성공";
					loc = request.getContextPath()+"/index.up";  						 // loc 는 들아갈 페이지를 말하며 여기에서는 시작페이지로 이동한다.
				}
				
			} catch(SQLException e) {
				message = "SQL 구문 오류 발생";
				loc = "javascript:history.back()";    						 			 // 자바 스크립트를 이용한 이전페이지로 이동한다.
				e.printStackTrace();
			}
			
			//request 에 저장한다.
			request.setAttribute("message", message);   // request 영역에 저장시킨다.  (key 값, )
			request.setAttribute("loc", loc);			// loc는 돌아가야할 위치값
			
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");		// 이동 한다.
			
			
			
		}		
	}
}
