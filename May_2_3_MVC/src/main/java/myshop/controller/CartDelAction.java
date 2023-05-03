package myshop.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import common.controller.AbstractController;
import myshop.model.InterProductDAO;
import myshop.model.ProductDAO;

public class CartDelAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String method = request.getMethod();
		// "GET", "POST" 
		
		if(!"POST".equalsIgnoreCase(method)) {
			// GET 방식이라면
	         
	         String message = "비정상적인 경로로 들어왔습니다";
	         String loc = "javascript:history.back()";
	         
	         request.setAttribute("message", message);
	         request.setAttribute("loc", loc);
	         
	         super.setViewPage("/WEB-INF/msg.jsp");
	         return;
		}
		else if("POST".equalsIgnoreCase(method) && super.checkLogin(request)) {
			// POST 방식이고 로그인을했다 한다면  
			
			String cartno = request.getParameter("cartno");
			
			InterProductDAO pdao = new ProductDAO();
			
			// 장바구나 테이블에서 특정 제품을 장바구니 에서 비운다.
			int n = pdao.delCart(cartno);
			
			
			JSONObject jsobj = new JSONObject();   // {}  자바스크립트 객체를 뜻함 거기에 넣어줄것임 
			jsobj.put("n",  n);   // {"n":1}
			
			
			String json = jsobj.toString();  //"{\"n\":1}"
			
			request.setAttribute("json", json);
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/jsonview.jsp");
			
			
			
		}

	}

}
