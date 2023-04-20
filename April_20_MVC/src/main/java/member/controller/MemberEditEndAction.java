package member.controller;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.controller.AbstractController;
import member.model.InterMemberDAO;
import member.model.MemberDAO;
import member.model.MemberVO;

public class MemberEditEndAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String method = request.getMethod(); // get or post
		
		String message = "";   // 이 메시지는 alert에 나올 메시지
		String loc = "";	   // 위치를 받아올 것 
		
		if("POST".equalsIgnoreCase(method)) {   // "POST" 방식 으로 넘어온 것이라면 
			 
			
			// 본격 시작 부분
			String userid = request.getParameter("userid");		// form 태그의 것을 받아온다.
			String name = request.getParameter("name");				
			String pwd = request.getParameter("pwd");	
			String email = request.getParameter("email");	
			String hp1 = request.getParameter("hp1");	
			String hp2 = request.getParameter("hp2");	
			String hp3 = request.getParameter("hp3");		
			String postcode = request.getParameter("postcode");	
			String address = request.getParameter("address");
			String detailAddress = request.getParameter("detailAddress");
			String extraAddress = request.getParameter("extraAddress");		
			String birthyyyy = request.getParameter("birthyyyy");	
			String birthmm = request.getParameter("birthmm");	
			String birthdd = request.getParameter("birthdd");	
			
			// 온전한 연락처
			String mobile = hp1 + hp2 + hp3;
			// 온전한 생년월일
			String birthday = birthyyyy + "-" + birthmm + "-" + birthdd;    // 1993-04-25
			
			// 파라미터가 있는 MemberVO를 가져온다.
			MemberVO member = new MemberVO(userid, pwd, name, email, mobile, postcode, address, detailAddress, extraAddress, birthday);
			// DAO 에 MemberVO member를 보내주려고 한다. 
			
			
			
			// 매소드 호출 하기 
			try {
				
				InterMemberDAO mdao = new MemberDAO();
				int n = mdao.updateMember(member);    // 리턴 타입 int
			
				if(n==1) {   // 값을 받아왔다면 
					
					// !!! session 에 저장된 loginuser 를 변경된 사용자의 정보값으로 변경해주어야 한다.
					HttpSession session = request.getSession();		   // session 에 있는것을 받아온다.
					
					MemberVO loginuser = (MemberVO) session.getAttribute("loginuser");    // 애는 리턴타입이 object 이지만 캐스팅 해버린다.
					  																	  // if 안에 있기 때문에 값은 무조건 존재한다. null 걱정 X 
																				
					loginuser.setName(name);
					loginuser.setPwd(pwd);
					loginuser.setEmail(email);
					loginuser.setMobile(mobile);
					loginuser.setPostcode(postcode);
					loginuser.setAddress(extraAddress);
					loginuser.setDetailaddress(detailAddress);
					loginuser.setExtraaddress(extraAddress);
					loginuser.setBirthday(birthyyyy + birthmm + birthdd);
					
					
					message = "회원정보 수정 성공 ";
					
					
				}
				
			} catch(SQLException e) {
				message = "SQL 구문 오류 발생";				
				e.printStackTrace();
			}
			
		}
		else {			// "GET" 방식 으로 넘어온 것이라면 즉 "post" 방식으로 넘어온 것이 아니라면 
			
			message = "비정상적인 경로를 통해 들어왔습니다.";   // 이 메시지는 alert에 나올 메시지
			
		}	
		
		loc = "javascript:history.back()";	   // 위치를 받아올 것
		
		//request 에 저장한다.
		request.setAttribute("message", message);   // request 영역에 저장시킨다.  (key 값, )
		request.setAttribute("loc", loc);			// loc는 돌아가야할 위치값
		
		
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/msg.jsp");		// 이동 한다.

	}

}
