package member.controller;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.controller.AbstractController;
import member.model.*;

public class MemberlistAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// 세션값 얻어오기 
		HttpSession session = request.getSession();
		MemberVO loginuser= (MemberVO) session.getAttribute("loginuser");
		
		
		// 관리자로 로그인 했을때만 적용하도록 한다.
		if(loginuser != null && loginuser.getUserid().equals("admin")) {  
			//System.out.println("~~~~ 관리자만 보는 페이지 입니다.!!!!");
			// ~~~~ 관리자만 보는 페이지 입니다.!!!!
			
		// 데이터 베이스 가져오기 위해 
			InterMemberDAO mdao = new MemberDAO();
			
			
			
		// 검색어 부분과 검색 분류의 부분은 회원목록에서 "검색" 버튼을 클릭시 넘어온다.
			
			String searchType = request.getParameter("searchType");   // 뷰를 해주는 곳에서 값을 가져오자(검색분류) (memberList.jsp 파일) 
			// 회원목록 페이지로 보여줄때 즉, 처음에는 넘어오는 값이 없다. 그러므로 searchType 에는 null 이 들어온다.
			// 회원목록 페이지에서 "검색" 버튼을 클릭시 넘어온다. 그러므로 searchType 에는 null 이 아닌 어떤 값이 들어온다.
			
			String searchWord = request.getParameter("searchWord");   // 검색어부분
			// 회원목록 페이지로 보여줄때 즉, 처음에는 넘어오는 값이 없다. 그러므로 searchType 에는 null 이 들어온다.
			// 회원목록 페이지에서 "검색" 버튼을 클릭시 넘어온다. 그러므로 searchType 에는 null 이 아닌 어떤 값이 들어온다.
						
			////////////////////////////////////////////////////////////////
			
			

			if(searchType == null || 
			   (!"name".equals(searchType) && 
			    !"userid".equals(searchType) && 
			    !"email".equals(searchType)	)) {    // 찾는 타입이 name, userid, email 이 아니라면  
				searchType = "";
			}
			
			// 처음 회원전체 목록에 들어갔을때 검색창에 저장되어 나오게 된다.
			if(searchWord == null || 											// null이든 null이 아니지만 공백으로만 들어온다면 
			  (searchWord != null && searchWord.trim().isEmpty())	) {
				searchWord = "";
			}
			
			Map<String, String> paraMap = new HashMap<>();
			
			System.out.println(searchWord);
			
			paraMap.put("searchType", searchType);			// Map에 저장하자 
			paraMap.put("searchWord", searchWord);	
			
			
			
			
			
			
			// *** 페이징 처리를 안한 모든 회원 또는 검색한 회원 목록 보여주기 *** //
			/*  List<MemberVO> memberList = mdao.selectMember(paraMap);   // 메소드 하나 생성하다 페이징 처리 X*/
			
			
			// *** 페이징 처리를 한 모든 회원 또는 검색한 회원 목록 보여주기 *** //
			
			String currentShowPageNo = request.getParameter("currentShowPageNo");
			// currentShowPageNo 는 사용자가 보고자 하는 페이지바의 페이지 번호이다. 
			// 메뉴에서 회원 목록 만을 클릭했을 경우에는 currentShowPageNo 는 null 이 된다.
			// currentShowPageNo 가 null 이라면 currentShowPageNo 를 1 페이지로 바꾸어야 한다.
			
			
			
			String sizePerPage = request.getParameter("sizePerPage");   // memberList 에서 name="sizePerPage" 를 가져온다.
			// 한 페이지당 화면상에 보여줄 회원의 개수 
			// 메뉴에서 회원 목록 만을 클릭했을 경우에는 sizePerPage 는 null 이 된다.
			// sizePerPage 가 null 이라면 sizePerPage 를 10으로 바꾸어야 한다.
			// sizePerPage 가 null 이 아니라면 "10"개 또는 "5"개 또는 "3"개 로 넘어온다.
			
			// 맨 처음에 들어왔을때를 말함 (첫번쨰 페이지를 말함)
			if(currentShowPageNo == null) {
				currentShowPageNo = "1";    // 기본 값을 1로 줘서 1페이지부터 보이게 한다.
			}
			
			
			// 맨처음에 들어왔을대를 말함 
			if(sizePerPage == null ||
			   !("3".equals(sizePerPage) || "5".equals(sizePerPage) || "10".equals(sizePerPage))  )  {  //ssizePerPage 가 3,5,10 이 아니라면 보여주면 안되게 한다. 무조건 10개가 나오게 된다 (기본값) 
				sizePerPage = "10";			// 기본값으로 10개를 주겠다.
			}
			
			
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
			
			
			
		
			paraMap.put("currentShowPageNo", currentShowPageNo);// 조회하고자하는 페이지 번호 와 
			paraMap.put("sizePerPage", sizePerPage);   // 한페이지당 보여줄 행의 개수 를 맵에 담아서 보여줘야 함
			
			
			// 페이징 처리를 위한 검색이 있는 또는 검색이 없는 전체 회원 에 대한 총 페이지 알아오기
			//( total 몇페이지 인지 알아오자 )
						
			int totalPage = mdao.getTotalPage(paraMap);		// TotalPage 매소드 생성해서 받아옴  (interMemberDAO 로 넘어감)
			
			//System.out.println("확인용 : totalPage => " +  totalPage);
			/*
			
				
				확인용 : totalPage => 21
				
				확인용 : totalPage => 42
				
				확인용 : totalPage => 70
			
			*/
						

			// === GET 방식이므로 사용자가 웹브라우저 주소창에서 currentShowPageNo 에
			//     totalPage 보다 큰 값을 입력하여 장난친 경우에는 1페이지로 가게끔 막아주기
						
			if( Integer.parseInt(currentShowPageNo) > totalPage) {
				currentShowPageNo = "1";
				paraMap.put("currentShowPageNo", currentShowPageNo);
			}
			
			
			List<MemberVO> memberList = mdao.selectPagingMember(paraMap);     // 페이징 처리 ok!
			
			
			
			
			request.setAttribute("memberList", memberList);
			
			
			
			// request 에 저장 
			request.setAttribute("searchType", searchType);
			request.setAttribute("searchWord", searchWord);
			request.setAttribute("sizePerPage", sizePerPage);
			
			
			/////////////////////////////////////////////////////////////////////////////
			
			
			
			
			
			// **** ==== 페이지바 만들기 시작 ==== **** //
			
			/*
	            1개 블럭당 10개씩 잘라서 페이지 만든다.
	            1개 페이지당 3개행 또는 5개행 또는  10개행을 보여주는데
	                만약에 1개 페이지당 5개행을 보여준다라면 
	                총 몇개 블럭이 나와야 할까? 
	                총 회원수가 208명 이고, 1개 페이지당 보여줄 회원수가 5 이라면
	            208/5 = 41.6 ==> 42(totalPage)        
	                
	            1블럭               1 2 3 4 5 6 7 8 9 10 [다음]
	            2블럭   [이전] 11 12 13 14 15 16 17 18 19 20 [다음]
	            3블럭   [이전] 21 22 23 24 25 26 27 28 29 30 [다음]
	            4블럭   [이전] 31 32 33 34 35 36 37 38 39 40 [다음]
	            5블럭   [이전] 41 42 
	         */
			
			// ==== !!! pageNo 구하는 공식 !!! ==== // 
		      /*
		          1  2  3  4  5  6  7  8  9  10  -- 첫번째 블럭의 페이지번호 시작값(pageNo)은  1 이다.
		          11 12 13 14 15 16 17 18 19 20  -- 두번째 블럭의 페이지번호 시작값(pageNo)은 11 이다.   
		          21 22 23 24 25 26 27 28 29 30  -- 세번째 블럭의 페이지번호 시작값(pageNo)은 21 이다.
		          
		           currentShowPageNo        pageNo  ==> ( (currentShowPageNo - 1)/blockSize ) * blockSize + 1 
		          ---------------------------------------------------------------------------------------------
		                 1                   1 = ( (1 - 1)/10 ) * 10 + 1 
		                 2                   1 = ( (2 - 1)/10 ) * 10 + 1 
		                 3                   1 = ( (3 - 1)/10 ) * 10 + 1 
		                 4                   1 = ( (4 - 1)/10 ) * 10 + 1  
		                 5                   1 = ( (5 - 1)/10 ) * 10 + 1 
		                 6                   1 = ( (6 - 1)/10 ) * 10 + 1 
		                 7                   1 = ( (7 - 1)/10 ) * 10 + 1 
		                 8                   1 = ( (8 - 1)/10 ) * 10 + 1 
		                 9                   1 = ( (9 - 1)/10 ) * 10 + 1 
		                10                   1 = ( (10 - 1)/10 ) * 10 + 1 
		                 
		                11                  11 = ( (11 - 1)/10 ) * 10 + 1 
		                12                  11 = ( (12 - 1)/10 ) * 10 + 1
		                13                  11 = ( (13 - 1)/10 ) * 10 + 1
		                14                  11 = ( (14 - 1)/10 ) * 10 + 1
		                15                  11 = ( (15 - 1)/10 ) * 10 + 1
		                16                  11 = ( (16 - 1)/10 ) * 10 + 1
		                17                  11 = ( (17 - 1)/10 ) * 10 + 1
		                18                  11 = ( (18 - 1)/10 ) * 10 + 1 
		                19                  11 = ( (19 - 1)/10 ) * 10 + 1
		                20                  11 = ( (20 - 1)/10 ) * 10 + 1
		                 
		                21                  21 = ( (21 - 1)/10 ) * 10 + 1 
		                22                  21 = ( (22 - 1)/10 ) * 10 + 1
		                23                  21 = ( (23 - 1)/10 ) * 10 + 1
		                24                  21 = ( (24 - 1)/10 ) * 10 + 1
		                25                  21 = ( (25 - 1)/10 ) * 10 + 1
		                26                  21 = ( (26 - 1)/10 ) * 10 + 1
		                27                  21 = ( (27 - 1)/10 ) * 10 + 1
		                28                  21 = ( (28 - 1)/10 ) * 10 + 1 
		                29                  21 = ( (29 - 1)/10 ) * 10 + 1
		                30                  21 = ( (30 - 1)/10 ) * 10 + 1                    

		       */
			
			
			
			
			// 페이지 바 부분
			String pageBar = "";
			
			int blockSize = 10;   // blockSize 는 블럭(토막) 당 보여주는 페이지 번호의 개수이다.
			
			
			int loop = 1; // loop는 1부터 증가하여ㅕ 1개 블럭을 이루는 페이지 번호의 개수(지금은 10개) 까지만 증가하는 용도이다.

			
			// !!! 다음은 pageNo를 구하는 공식이다. !!! //
			
			int pageNo  = ( ( Integer.parseInt(currentShowPageNo) - 1)/blockSize ) * blockSize + 1;
			// pageNo 는 페이지 바 에서 보여지는 첫번째 번호이다. 
			
			
			////////////////////////////////////////////////////////////////////////////////
		
			
		/// *** [맨처음][이전] 만들기 *** ///
			
			
			pageBar += "<li class='page-item'><a class='page-link' href='memberlist.up?searchType="+searchType+"&searchWord="+searchWord+"&currentShowPageNo=1&sizePerPage="+sizePerPage+"'>[맨처음]</a></li>"; 
			
			if(pageNo != 1) {   // 페이지 넘버가 1이 아닐때만 보여라 
				pageBar += "<li class='page-item'><a class='page-link' href='memberlist.up?searchType="+searchType+"&searchWord="+searchWord+"&currentShowPageNo="+(pageNo-1)+"&sizePerPage="+sizePerPage+"'>[이전]</a></li>"; 
			}
			
			
			
			
			while( !(loop > blockSize || pageNo > totalPage)) {   // pageNo > totalPage 요 부분이 없으면 페이지는 42 까지가 끝인 반면에 페이징 바는 50까지 나온다.
				
				if(pageNo == Integer.parseInt(currentShowPageNo)) {
					pageBar += "<li class='page-item active'><a class='page-link' href= '#'>" + pageNo + "</a></li>";    // 차곡차솟 쌓는다.   <li> 는 부트스트랩에 있는 네비게이션을 쓰려는 것이다. (페이지바) (부트스트랩 26번 파일 pagination)
				}																	// 즉 부트스트랩을 이용하여 모양을 나타내는 것이다. 
				else {
					pageBar += "<li class='page-item'><a class='page-link' href='memberlist.up?searchType="+searchType+"&searchWord="+searchWord+"&currentShowPageNo="+pageNo+"&sizePerPage="+sizePerPage+"'>" + pageNo + "</a></li>"; 
				}
				
				loop++;  // 1 2 3 4 5 6 7 8 9 10
				
				pageNo++; //  1  2  3  4  5  6  7  8  9 10 
				          // 11 12 13 14 15 16 17 18 19 20
				          // 21 22 23 24 25 26 27 28 29 30
				          // 31 32 33 34 35 36 37 38 39 40
				          // 41 42
				
			} // end of while()~ 
			
			
			
		/// **** [다음][마지막] 만들기 ***** ///
			
			// pageNo = ==> 11
			
			if(pageNo <= totalPage) {
				pageBar += "<li class='page-item'><a class='page-link' href='memberlist.up?searchType="+searchType+"&searchWord="+searchWord+"&currentShowPageNo="+pageNo+"&sizePerPage="+sizePerPage+"'>[다음]</a></li>"; 
			}

			pageBar += "<li class='page-item'><a class='page-link' href='memberlist.up?searchType="+searchType+"&searchWord="+searchWord+"&currentShowPageNo="+totalPage+"&sizePerPage="+sizePerPage+"'>[마지막]</a></li>"; 
			
			
			
			request.setAttribute("pageBar", pageBar);
			
			
			
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/member/memberList.jsp");
			
			
			
			
		}
		else {		// 로그인 아이디가 admin 이 아닌경우와 로그인을 안한 경우에는 
			//System.out.println(" !!! 접근금지 !!! ");
			//  !!! 접근금지 !!! 
			String message = "관리자만 접근이 가능합니다.";
			String loc = "javascript:history.back()";

			request.setAttribute("message", message);
			request.setAttribute("loc", loc);
			
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");
			
		}
		
		
		

	}

}
