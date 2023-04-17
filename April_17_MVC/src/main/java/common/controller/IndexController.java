package common.controller;

import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import myshop.model.*;

public class IndexController extends AbstractController {

	// 부모클래스 재정의 
	//시작 페이지가 될것이다.
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// 읽어야 하므로 DAO가 필요하다.
		InterProductDAO pdao = new ProductDAO();
		

		try {
			//dao 에 있던 매소드 불러오기
			List<ImageVO> imgList = pdao.imageSelectAll();
		
			request.setAttribute("imgList", imgList);
			
			// 아래 세개 모두 가능하다.
			
			// super.setRedirect(false);  // forward 방식이다.   부모 클래스에서 defalut 가 false 이기 때문에 생략이 가능하다.
			// this.setRedirect(false);
			// setRedirect(false);      
			
			
			super.setViewPage("/WEB-INF/index.jsp");    // 위처럼 super, this, 모두 생략햐도 문제 없다.
															   //() 안에 있는것에 따라 프로그램 작동을 함
		} catch (SQLException e) {
			e.printStackTrace();
			super.setRedirect(true);		// sendRidirect 를 사용한다
			super.setViewPage(request.getContextPath() + "/error.up");   //   /MyMVC/error.up으로 이동
			
			
		}
	}

}
