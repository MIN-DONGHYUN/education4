package my.util;

import javax.servlet.http.HttpServletRequest;

public class MyUtil {

	// *** ? 다음의 데이터까지 포함한 현재 URL 주소를 알려주는 메소드를 생성  *** //
	public static String getCurrentURL(HttpServletRequest request) {
		
		// 웹브라우저 주소 입력창에서 
		// http://localhost:9090/MyMVC/member/memberlist.up?searchType=name&searchWord=유&currentShowPageNo=5&sizePerPage=3 와 같이 입력되었더라면 
		
		// 실제 url 만 나타내보자 
		String currentURL = request.getRequestURL().toString();
		//System.out.println("~~~ 확인용 currentURL => " + currentURL);
		// 결과 : ~~~ 확인용 currentURL => http://localhost:9090/MyMVC/member/memberList.up 
				
		String queryString = request.getQueryString();
		//System.out.println("~~~확인용 quertString =>" + queryString );
		// ~~~확인용 quertString => searchType=name&searchWord=유&currentShowPageNo=5&sizePerPage=3 (GET 방식일 경우)
		// ~~~확인용 quertString => null (POST 방식일 경우)
		
		
		if(queryString != null) {   // GET 방식일 경우 
			currentURL += "?" + queryString;  
			// http://localhost:9090/MyMVC/member/memberlist.up?searchType=name&searchWord=유&currentShowPageNo=5&sizePerPage=3
			
		}
		
		String ctxPath = request.getContextPath();
		// MyMVC
		
		int beginIndex = currentURL.indexOf(ctxPath) + ctxPath.length();		// ctxPath가 시작되어지는 index 값을 알아온다.
				// 지금은 21          +      6    이다.   27 부분은 /member 에서의 / 를 나타낸다.
		
		currentURL = currentURL.substring(beginIndex);   // MyMVC 뒤부터 끝까지 나오게 하기 위헤 
		// "/member/memberlist.up?searchType=name&searchWord=유&currentShowPageNo=5&sizePerPage=3"
		
		
		
		
		return currentURL;
		
	}
	
	
}
