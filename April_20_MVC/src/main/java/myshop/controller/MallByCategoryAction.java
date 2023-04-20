package myshop.controller;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.controller.AbstractController;
import myshop.model.*;

public class MallByCategoryAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// 카테고리 목록을 조회해오기 
		super.getCategoryList(request);   // 부모 클래스에서 불러오는 것이다. (메소드 호출 )
		
		// cnum 을 받아오자 
		String cnum = request.getParameter("cnum");    // 카테고리 번호를 가져옴 
		
		// *** 카테고리번호에 해당하는 제품들을 페이징 처리하여 보여주기 ***// 
		
		String currentShowPageNo = request.getParameter("currentShowPageNo");
		// currentShowPageNo 는 사용자가 보고자 하는 페이지바의 페이지 번호이다. 
		// 카테고리 메뉴에서 카테고리 만을 클릭했을 경우에는 currentShowPageNo 는 null 이 된다.
		// currentShowPageNo 가 null 이라면 currentShowPageNo 를 1 페이지로 바꾸어야 한다.


		
		if(currentShowPageNo == null) {
			currentShowPageNo = "1";
		}
		
	    // 한 페이지당 화면상에 보여줄 제품의 갯수는 10으로 한다. sizePerPage는 ProductDAO에서 상수로 설정해뒀음.
		
		// === GET 방식 이므로 사용자가 웹 브아우저 주소창에서 currentShowPageNo 에 숫자가 아닌 문자를 입력한 경우 또는
		//     int 범위를 초과한 숫자를 입력한 경우라면 (21억) currentShowPageNo 는 1 페이지로 만들도록 한다.
		//     또한 currentShowPageNo 이 0을 포함한 0 이하이면 currentShowPageNo 는 1페이지로 만들도록 한다.
					
		try {
			if(Integer.parseInt(currentShowPageNo) < 1) {    // 0 이하이면 1페이지로
				currentShowPageNo = "1";
			}
		}
		catch(NumberFormatException e) {			// 문자로 장난칠 경우 1페이지로
			currentShowPageNo = "1";
		}
		
		InterProductDAO pdao = new ProductDAO();
		
		//맵 사용
		Map<String, String> paraMap = new HashMap<>();
		paraMap.put("cnum", cnum);
		paraMap.put("currentShowPageNo", currentShowPageNo);
		
		// 특정 카테고리에 속하는 제품들을 페이지바를 이용한 페이징 처리하여 조회(select) 해오기 
		List<ProductVO> productList = pdao.selectProductByCategory(paraMap);
		
		request.setAttribute("productList", productList);
		
		
		// **** ===== 페이지바 만들기 ===== **** // 
		
		
		
		
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/myshop/mallByCategory.jsp");
		
	}

}
