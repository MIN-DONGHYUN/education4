package common.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ErrorController extends AbstractController {

	// sql 에러일대 재정의 
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		//super.setRedirect(false);    // 부모 클래스에서 이미 false 로 설정됨 
		super.setViewPage("/WEB-INF/error/error.jsp");   // 에러 페이지로 이동 
		
	}

}
