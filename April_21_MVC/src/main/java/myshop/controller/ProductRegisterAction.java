package myshop.controller;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.controller.AbstractController;
import member.model.MemberVO;
import myshop.model.*;

public class ProductRegisterAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		//  === 관리자(admin) 로 로그인 했을 때만 제품등록이 가능하도록 해야 한다. === //
		HttpSession session =  request.getSession();   // 세션 값을 가져 온다.
		
		MemberVO loginuser = (MemberVO) session.getAttribute("loginuser");
		
		if( loginuser != null && "admin".equals(loginuser.getUserid())) {
			// 관리자 admin 으로 로그인 했을 경우 
			
			// 카테고리 목록을 조회해오기
			super.getCategoryList(request);   // 카테고리의 목록을 조회해 오는 코드이다.
			
			// 스캑 목록을 조회해오기 
			InterProductDAO pdao = new ProductDAO();
			
			List<SpecVO> specList = pdao.selectSpecList();    // 메소드 생성
			request.setAttribute("specList", specList);
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/myshop/admin/productRegister.jsp");
		}
		else {
			// 로그인을 안한 경우 + 로그인을 일반 사용자로만 한 경우 
			String message = "관리자만 접근이 가능합니디.";
			String loc ="javascript:history.back()";
			
			request.setAttribute("message", message);
			request.setAttribute("loc", loc);
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");
		}
		
		
	}

}
