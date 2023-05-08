package myshop.controller;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import common.controller.AbstractController;
import myshop.model.InterProductDAO;
import myshop.model.ProductDAO;
import myshop.model.PurchaseReviewsVO;

public class ReviewRegisterAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		
		String method = request.getMethod();
	      
	    if("POST".equalsIgnoreCase(method)) {
		
		
			String contents = request.getParameter("contents");
			String fk_userid = request.getParameter("fk_userid");
			String fk_pnum = request.getParameter("fk_pnum");
			
			// *** 크로스 사이트 스크립트 공격에 대응하는 안전한 코드(시큐어 코드) 작성하기 *** //
			
			contents = contents.replaceAll("<", "&lt;");
			contents = contents.replaceAll(">", "&gt;");
			// 입력한 내용에서 엔터는 <br>로 변환시키기
			contents = contents.replaceAll("\r\n", "<br>");
			
			PurchaseReviewsVO reviewvo = new PurchaseReviewsVO();
			
			reviewvo.setContents(contents);
			reviewvo.setFk_userid(fk_userid);
			reviewvo.setFk_pnum(Integer.parseInt(fk_pnum));   // fk_pnum 은 int형이기 때문에 int 형으로 형 변환 시킴
			
			InterProductDAO pdao = new ProductDAO();
			
			int n = 0;
			
			try {
				n = pdao.addReview(reviewvo);
			}
			catch(SQLIntegrityConstraintViolationException e) {
				// 제약조건에 위배된 경우 (동일한 제품에 대하여 동일한 회원이 제품후기를 2번 쓴 경우 unique 제약에 위배됨)
				e.printStackTrace();
				n = -1;
			}
			catch(SQLException e) {
				e.printStackTrace();
			}
			
			JSONObject jsonObj = new JSONObject();  // {}
			jsonObj.put("n", n);    // {"n":1} 또는 {"n":-1} 또는 {"n":0}
			
			String json = jsonObj.toString();  // 문자열로 변경
			
			request.setAttribute("json", json);
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/jsonview.jsp");
		
	    }
	      else {
	         // **** POST 방식으로 넘어온 것이 아니라면 **** //
	         
	         String message = "비정상적인 경로를 통해 들어왔습니다.!!";
	         String loc = "javascript:history.back()";
	         
	         request.setAttribute("message", message);
	         request.setAttribute("loc", loc);
	         
	         super.setViewPage("/WEB-INF/msg.jsp");
	      }
		
		

	}

}
