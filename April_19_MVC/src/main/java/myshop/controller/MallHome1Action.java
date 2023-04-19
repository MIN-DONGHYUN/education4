package myshop.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.controller.AbstractController;
import myshop.model.*;

public class MallHome1Action extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// Ajax(JSON)을 사용하여 HIT 상품목록을 "더보기" 방식으로 페이징 처리해서 보여주겠다. === //
		
		InterProductDAO pdao = new ProductDAO();  //ProductDAO 불러옴 
		
		int totalHITCount = pdao.totalPspecCount("1");    // HIT 상품의 전체 개수를 알아온다.  HIT 상품은 1로 고정을 했기 때문에 1인걸 받아옴
		
		//System.out.println("확인용 : totalHITCount : " + totalHITCount);
		// 확인용 : totalHITCount : 36
		
		request.setAttribute("totalHITCount", totalHITCount);	// 값 넘겨주기 위해 request에 저장 
		
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/myshop/mailHome1.jsp");
		
		

	}

}
