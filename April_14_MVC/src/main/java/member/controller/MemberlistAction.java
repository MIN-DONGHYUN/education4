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
			
			// 맨 처음에 들어왔을때를 말함
			if(currentShowPageNo == null) {
				currentShowPageNo = "1";    // 기본 값을 1로 줘서 1페이지부터 보이게 한다.
			}
			
			
			// 맨처음에 들어왔을대를 말함 
			if(sizePerPage == null) {
				sizePerPage = "10";			// 기본값으로 10개를 주겠다.
			}
			
			paraMap.put("currentShowPageNo", currentShowPageNo);// 조회하고자하는 페이지 번호 와 
			paraMap.put("sizePerPage", sizePerPage);   // 한페이지당 보여줄 행의 개수 를 맵에 담아서 보여줘야 함
			
			
			
			
			List<MemberVO> memberList = mdao.selectPagingMember(paraMap);     // 페이징 처리 ok!
			
			
			
			
			request.setAttribute("memberList", memberList);
			
			if(searchType == null) {
				searchType = "";
			}
			
			// 처음 회원전체 목록에 들어갔을때 검색창에 저장되어 나오게 된다.
			if(searchWord == null) {
				searchWord = "";
			}
			
			
			// request 에 저장 
			request.setAttribute("searchType", searchType);
			request.setAttribute("searchWord", searchWord);
			request.setAttribute("sizePerPage", sizePerPage);
			
			
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
