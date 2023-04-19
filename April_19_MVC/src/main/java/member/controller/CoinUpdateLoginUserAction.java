package member.controller;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.controller.AbstractController;
import member.model.InterMemberDAO;
import member.model.MemberDAO;
import member.model.MemberVO;

public class CoinUpdateLoginUserAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String method = request.getMethod();	// "GET"  또는  "POST"
		
		// POST 방식이니?
		if("post".equalsIgnoreCase(method)) {
			// POST방식이라면 
			
			String userid = request.getParameter("userid");			//login.jsp 에서 받아온다. 
			String coinmoney = request.getParameter("coinmoney");
			
			Map<String, String> paraMap = new HashMap<>();  // MAP 사용하겠다. 
			
			paraMap.put("userid", userid);
			paraMap.put("coinmoney", coinmoney);
			
			
			InterMemberDAO mdao = new MemberDAO();
			
			int n = mdao.coinUpdate(paraMap);  // 메소드를 만들것이다.  // DB에 코인 및 포인트 증가하기
			
			String message = "";
		    String loc = "";
			
			
			if(n==1) {
				
				// session 에 있는 user 의 정보를 불러온다.
				HttpSession session = request.getSession();
				MemberVO loginuser = (MemberVO) session.getAttribute("loginuser");			
				
				
				// 로그아웃 하고 다시 로그인을 안해도 point 와 금액이 나올수 있도록 세션값을 변경하기 				
				loginuser.setCoin(loginuser.getCoin() + Integer.parseInt(coinmoney));
				loginuser.setPoint(loginuser.getPoint() + (int) (Integer.parseInt(coinmoney) * 0.01));
				
				
				// coinmoney 에 각 3자리마다 ,를 넣는다.
				DecimalFormat df = new DecimalFormat("#,###");
				//System.out.println(df.format(coinmoney));
				
				
				message = loginuser.getName() + "님의 " + df.format(Long.parseLong(coinmoney)) + "원이 결제 되었습니다.";    // coinmoney 는 숫자로 되어진 문자열 
				loc = request.getContextPath()+"/index.up";
				
			}
			else {
				message = "코인에 결제가 실패되었습니다.";
				loc = "javascript:history.back()";
			}
			
			
			request.setAttribute("message", message);
			request.setAttribute("loc", loc); 
			

			super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");
			
			
			
		}
		else {
			// POST방식이 아니라면 (유저가 장난쳐올때)
			String message = "비정상적인 경로로 들어왔습니다.";
		    String loc = "javascript:history.back()";
		      
		    request.setAttribute("message", message);
		    request.setAttribute("loc", loc);
		    
		    super.setRedirect(false);
		    super.setViewPage("/WEB-INF/msg.jsp");
			
		}		

	}

}
