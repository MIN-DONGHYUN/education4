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
		
		
		// 문자로 장난 못치게 막는다.
		try {
			Integer.parseInt(cnum);
		}
		catch (NumberFormatException e) {
			super.setRedirect(true);   // 우리끼리 짠 약속
			super.setViewPage(request.getContextPath()+"/index.up");
			return;   // 함수 종료
		}
		
		InterProductDAO pdao = new ProductDAO();
		
		// 입력받은 cnum 이 DB 에 존재하지 않는 경우는 사용자가 장난친 경우이다. 
		if( !pdao.isExist_cnum(cnum) ) {   // 여기서 새로운 메소드 생성할 것 
			
			super.setRedirect(true);   // 우리끼리 짠 약속
			super.setViewPage(request.getContextPath()+"/index.up");
			return;   // 함수 종료
			
		}
		else {			// 입력받은 cnum 이 DB 에 존재하는 경우
			
		
		
		
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
		
		
		
		//맵 사용
		Map<String, String> paraMap = new HashMap<>();
		paraMap.put("cnum", cnum);
		paraMap.put("currentShowPageNo", currentShowPageNo);
		
		
		
		
		
		

		// 페이징 처리를 위한 특정카테고리의 제품개수에 대한 총 페이지 알아오기
					
		int totalPage = pdao.getTotalPage(cnum);		// TotalPage 매소드 생성해서 받아옴  (interMemberDAO 로 넘어감)
		
		//System.out.println("확인용 : totalPage => " +  totalPage);
		
		
		// === GET 방식이므로 사용자가 웹브라우저 주소창에서 currentShowPageNo 에
		//     totalPage 보다 큰 값을 입력하여 장난친 경우에는 1페이지로 가게끔 막아주기
					
		if( Integer.parseInt(currentShowPageNo) > totalPage) {
			currentShowPageNo = "1";
			paraMap.put("currentShowPageNo", currentShowPageNo);
		}
		
		// 특정 카테고리에 속하는 제품들을 페이지바를 이용한 페이징 처리하여 조회(select) 해오기 
		List<ProductVO> productList = pdao.selectProductByCategory(paraMap);
		
		request.setAttribute("productList", productList);			
		
		
		
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
				
			
			
			
			
			/// *** [맨처음][이전] 만들기 *** ///
			
			
			pageBar += "<li class='page-item'><a class='page-link' href='mallByCategory.up?currentShowPageNo=1&cnum="+cnum+"'>[맨처음]</a></li>"; 
			
			if(pageNo != 1) {   // 페이지 넘버가 1이 아닐때만 보여라 
				pageBar += "<li class='page-item'><a class='page-link' href='mallByCategory.up?currentShowPageNo="+(pageNo-1)+"&cnum="+cnum+"'>[이전]</a></li>"; 
			}
			
			
			
			
			while( !(loop > blockSize || pageNo > totalPage)) {   // pageNo > totalPage 요 부분이 없으면 페이지는 42 까지가 끝인 반면에 페이징 바는 50까지 나온다.
				
				if(pageNo == Integer.parseInt(currentShowPageNo)) {
					pageBar += "<li class='page-item active'><a class='page-link' href= '#'>" + pageNo + "</a></li>";    // 차곡차솟 쌓는다.   <li> 는 부트스트랩에 있는 네비게이션을 쓰려는 것이다. (페이지바) (부트스트랩 26번 파일 pagination)
				}																	// 즉 부트스트랩을 이용하여 모양을 나타내는 것이다. 
				else {
					pageBar += "<li class='page-item'><a class='page-link' href='mallByCategory.up?currentShowPageNo="+pageNo+"&cnum="+cnum+"'>" + pageNo + "</a></li>"; 
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
				pageBar += "<li class='page-item'><a class='page-link' href='mallByCategory.up?currentShowPageNo="+pageNo+"&cnum="+cnum+"'>[다음]</a></li>"; 
			}

			pageBar += "<li class='page-item'><a class='page-link' href='mallByCategory.up?currentShowPageNo="+totalPage+"&cnum="+cnum+"'>[마지막]</a></li>"; 
			
			request.setAttribute("pageBar", pageBar);

			super.setRedirect(false);
			super.setViewPage("/WEB-INF/myshop/mallByCategory.jsp");
		} // end of else
	}

}
