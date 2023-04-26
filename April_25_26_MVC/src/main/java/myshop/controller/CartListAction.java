package myshop.controller;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.controller.AbstractController;
import member.model.*;
import myshop.model.*;

public class CartListAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// 로그인 또는 로그아웃을 하면 시작페이지로 가는 것이 아니라 방금 보았던 그 페이지로 그대로 가기 위한 것임. 
	    //super.goBackURL(request);
		// 이 위에 두개는 절대 쓰면 안된다 다른 사람 로그인 하면 다른사람의 장바구니를 볼 수 있을수도 있기 때문이다.

		
		// 카테고리 목록을 조회해오기 
		super.getCategoryList(request);
		
		
		// 장바구니 보기는 반드시 해당 사용자가 로그인을 해야 만 볼 수 있다.
		boolean isLogin = super.checkLogin(request);	  // 로그인 했냐 안했냐를 알아보는 것이다.
		
		if(!isLogin) {
			request.setAttribute("message", "장바구니를 보려면 먼저 로그인 부터 하세요!!");
	        request.setAttribute("loc", "javascript:history.back()"); 
	         
	        super.setRedirect(false);
	        super.setViewPage("/WEB-INF/msg.jsp");
	        return;
		}
		else {
			// **** 페이징 처리 안한 상태로 장바구니 목록을 보여주기 **** //
			
			HttpSession session = request.getSession();
	        MemberVO loginuser = (MemberVO) session.getAttribute("loginuser");
	         
	        InterProductDAO pdao = new ProductDAO(); 
	         
	        List<CartVO> cartList = pdao.selectProductCart(loginuser.getUserid());
	        HashMap<String,String> sumMap = pdao.selectCartSumPricePoint(loginuser.getUserid());
	         
	        request.setAttribute("cartList", cartList);
	        request.setAttribute("sumMap", sumMap);
			
	        super.setRedirect(false);
	        super.setViewPage("/WEB-INF/myshop/cartList.jsp");
	        
			
		}
		
	}

}
