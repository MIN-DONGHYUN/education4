// 공통으로 사용할 부모 클래스를 나타낸 곳이다. 

package common.controller;

import java.sql.SQLException;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import member.model.MemberVO;
import myshop.model.*;

// 부모 클래스를 일반 class 가 아닌 abstract 클래스로 변경한다. 그렇게 되면 상속 받은 자식들이 반드시 재정의를 해야만 한다.
public abstract class AbstractController implements InterCommand {
	
	/*
	   === 다음에 나오는 것은 우리끼리 한 약속이다.
	   
	   ※ view 단 페이지(.jsp) 로 이동시 forward 방법( dispatcher) 으로 해야 하는데 
	   자식 클래스에서는 부모 클래스에서 생성해둔 매소드 호출시 아래와 같이 하면 된다.
	   
	  	super.setRedirect(false); 를 주면 false 가 필드로 들어가게 된다.
	  	super.setviewPage("WEB-INF/index.jsp); 를 주면 forward 방식으로 쓰여짐
	  	
	  	 
	   
	   ※ URL 주소를 변경하여 페이지 이동시 sendRedirect 방법으로 해야 하는데 
	   자식 클래스에서는 부모 클래스에서 생성해둔 매소드 호출시 아래와 같이 하면 된다.
	 
	 	super.setRedirect(true); 를 주면 sendRedirect로 쓰인다.
	 	super.setviewPage("registerMember.up"); 로 URL 주소를 넣는다.
	 
	 
	*/
	
	// 필드
	private boolean isRedirect = false; 
	// isRedirect 변수의 값이 false 이라면 view 단 페이지(.jsp)로 forward(dispatcher) 하겠다는 말이다.
	// isRedirect 변수의 값이 ture 이라면 sendRedirect 로 하겠다는 말이다.
	
	private String viewPage;
	// viewPage 는 isRedirect 값이 false 이라면 view 단 페이지(.jsp)의 경로명이 되고, 
	// viewPage 는 isRedirect 값이 true 이라면 이동해야할 URL 주소가 된다.

	
	// GET, SET 정의 
	public boolean isRedirect() {
		return isRedirect;
	}

	public void setRedirect(boolean isRedirect) {
		this.isRedirect = isRedirect;
	}

	public String getViewPage() {
		return viewPage;
	}

	public void setViewPage(String viewPage) {
		this.viewPage = viewPage;
	}
	
	
	////////////////////////////////////////////////////////////////////////////
	
	// 로그인 유무를 검사해서 로그인을 했으면 true 를 리턴 해주고 
	// 로그인을 안했으면 false 을 리턴해주도록 한다.
	
	public boolean checkLogin(HttpServletRequest request) {
		
		HttpSession session = request.getSession();							  // session 에 있는것을 받아온다.
		MemberVO loginuser = (MemberVO) session.getAttribute("loginuser");    // 애는 리턴타입이 object 이지만 캐스팅 해버린다.
		
		if(loginuser != null)
		{
			// 로그인 한 경우 
			return true;
		}
		else {
			// 로그인 안한 경우
			return false;
		}
		
		
	}// end of public boolean checkLogin(HttpServletRequest request) {
	
	
	////////////////////////////////////////////////////////////////////////////////
	// ***** 제품목록(Category) 를 보여줄 메소드 생성하기 ***** //
	// VO 를 사용하지 않고 Map 으로 처리해 보겠습니다.

	public void getCategoryList(HttpServletRequest request) throws SQLException {
		
		InterProductDAO pdao = new ProductDAO();
		
		List<Map<String, String>> categotyList =  pdao.getCategoryList();   // Map으로 값을 저장하기 위해 넣어줄것 
		
		request.setAttribute("categotyList", categotyList);   //request 에 저장 함 
		
	}
	
	
	
	
}
