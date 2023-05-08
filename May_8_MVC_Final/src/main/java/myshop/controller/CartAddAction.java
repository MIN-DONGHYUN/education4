package myshop.controller;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.controller.AbstractController;
import member.model.*;
import myshop.model.*;

public class CartAddAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// == 로그인 유무 검사하기 == // 
		boolean isLogin = super.checkLogin(request);

		// 2023-04-25 화요일 처음 배우는 부분 
		if(!isLogin) {   // 로그인을 하지 않은 상태이라면 
			/*
	            사용자가 로그인을 하지 않은 상태에서 특정제품을 장바구니에 담고자 하는 경우 
	            사용자가 로그인을 하면 장바구니에 담고자 했던 그 특정제품 페이지로 이동하도록 해야 한다.
	            이와 같이 하려고 ProdViewAction 클래스에서 super.goBackURL(request); 을 해두었음.   
	         */
	            
	         request.setAttribute("message", "장바구니에 담으려면 먼저 로그인 부터 하세요!!");
	         request.setAttribute("loc", "javascript:history.back()");
	            
	         super.setRedirect(false);
	         super.setViewPage("/WEB-INF/msg.jsp");
	            
	         return;
		}
		else {   // 로그인을 한 상태이라면 
			
			// 장바구니 테이블(tbl_cart)에 해당 제품을 담아야 한다.
	        // 장바구니 테이블에 해당 제품이 존재하지 않는 경우에는 tbl_cart 테이블에 insert 를 해야하고, 
	        // 장바구니 테이블에 해당 제품이 존재하는 경우에는 또 그 제품을 추가해서 장바구니 담기를 한다라면 tbl_cart 테이블에 update 를 해야한다.
			
			String method = request.getMethod();
			
			if("POST".equalsIgnoreCase(method)) {
				
				// post 방식이라면 
				
				String pnum = request.getParameter("pnum");    // 제품 번호
				String oqty = request.getParameter("oqty");    // 주문량
				
				HttpSession session = request.getSession();
				
				MemberVO loginuser = (MemberVO) session.getAttribute("loginuser");
				
				InterProductDAO pdao = new ProductDAO();
				
				
				Map<String,String> paraMap = new HashMap<>();
				paraMap.put("fk_userid", loginuser.getUserid());
				paraMap.put("fk_pnum", pnum);
				paraMap.put("oqty", oqty);
				
				int n = pdao.addCart(paraMap);   //Map 을 보내겠다.
				
				
				if(n==1) {
					request.setAttribute("message", "장바구니 담기 성공 !!");
					
					request.setAttribute("loc", "cartList.up");   //상대 경로 
					// 장바구니 목록보여주기 페이지로 이동 (상대 경로)
					
					
				}
				else {
					request.setAttribute("message", "장바구니 담기 실페 ㅠㅠ");
					request.setAttribute("loc", "javascript.history.back()");
				}

				super.setRedirect(false);   
				super.setViewPage("/WEB-INF/msg.jsp");
				
				
			} else {
				
				// get방식 이라면 
				String message = "비정상적인 경로로 들어왔습니다";
	            String loc = "javascript:history.back()";
	               
	            request.setAttribute("message", message);
	            request.setAttribute("loc", loc);
	              
	            super.setRedirect(false);   
	            super.setViewPage("/WEB-INF/msg.jsp");
			}
			
			
			
		}
		
	}

}
