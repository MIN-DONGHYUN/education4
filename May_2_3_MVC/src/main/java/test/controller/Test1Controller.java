package test.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.controller.AbstractController;

public class Test1Controller extends AbstractController {    // 부모 클래스는  AbstractController

	// 기본 생성자가 생략되어 있다. 
	
	@Override						// Static 이 없으므로 인스턴스 이다.
	public String toString() {     // toString 매소드는 Object 것인데 상속 받아서 재정의 한다.
		
		return "@@@ 클래스 Test1Controller 의 인스턴스 매소드 toString() 을 호출함 @@@";
	}

	// 부모클래스에서 미완성 된것을 재정의 한다.
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		request.setAttribute("name", "민동현");   // 값을 저장 시킬 것 
		// request 영역에 저장??
		
		super.setRedirect(false);   // 이것은 forword
		super.setViewPage("/WEB-INF/test/test1.jsp");
			
	}

}
