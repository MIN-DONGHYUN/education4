package myshop.controller;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;

import common.controller.AbstractController;
import member.controller.GoogleMail;
import member.model.MemberVO;
import myshop.model.*;

public class OrderAddAction extends AbstractController {

	private InterProductDAO pdao = new ProductDAO();   // 필드로 빼주었다.
	
	//주문코드(명세서번호)를 생성해주는 메소드
	private String getOdrcode() throws SQLException {
		
		//주문형식 : s+날짜+sequence => s20230427-1, s20230427-2, s20230427-3
		
		// 날짜 생성
		Date now = new Date();
		SimpleDateFormat smdatefm = new SimpleDateFormat("yyyyMMdd");
		String today = smdatefm.format(now);   // 날짜를 구해온것이다
		
		
		int seq = pdao.getSeq_tbl_order();
		// pdao.getSeq_tbl_order(); 는 시퀀스 seq_tbl_order 값을 채번해오는 것 
		
		return "s"+today+"-"+seq;
	}
	
	
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		

		// 4월 27일 이어서 하겠다.   cartList 에서 ajax 로 보낸것을 받는 것이다. 
		String sum_totalPrice = request.getParameter("sum_totalPrice");
		String sum_totalPoint = request.getParameter("sum_totalPoint");
		String pnum_join = request.getParameter("pnum_join");
		String oqty_join = request.getParameter("oqty_join");
		String totalPrice_join = request.getParameter("totalPrice_join");
		String cartno_join = request.getParameter("cartno_join");
		
		/*
			System.out.println("확인용 !!! sum_totalPrice : " + sum_totalPrice);    // 확인용 !!! sum_totalPrice : 175000
			System.out.println("확인용 !!! sum_totalPoint : " + sum_totalPoint);    // 확인용 !!! sum_totalPoint : 105
			System.out.println("확인용 !!! pnum_join : " + pnum_join);    			 // 확인용 !!! pnum_join : 5,3,4
			System.out.println("확인용 !!! oqty_join : " + oqty_join);              // 확인용 !!! oqty_join: 3,5,2
			System.out.println("확인용 !!! totalPrice_join : " + totalPrice_join);  // 확인용 !!! totalPrice_join : 99000,50000,26000
			System.out.println("확인용 !!! cartno_join : " + cartno_join);          // 확인용 !!! cartno_join : 9,6,5
		*/
		
		// ===== Transaction 처리하기 ===== // 
	    // 1. 주문 테이블에 입력되어야할 주문전표를 채번(select)하기 
	    // 2. 주문 테이블에 채번해온 주문전표, 로그인한 사용자, 현재시각을 insert 하기(수동커밋처리)
	    // 3. 주문상세 테이블에 채번해온 주문전표, 제품번호, 주문량, 주문금액을 insert 하기(수동커밋처리)
	    // 4. 제품 테이블에서 제품번호에 해당하는 잔고량을 주문량 만큼 감하기(수동커밋처리) 
	        
	    // 5. 장바구니 테이블에서 cartnojoin 값에 해당하는 행들을 삭제(delete)하기(수동커밋처리)
	    // >> 장바구니에서 주문을 한 것이 아니라 특정제품을 바로주문하기를 한 경우에는 장바구니 테이블에서 행들을 삭제할 작업은 없다. << 
	        
	    // 6. 회원 테이블에서 로그인한 사용자의 coin 액을 sumtotalPrice 만큼 감하고, point 를 sumtotalPoint 만큼 더하기(update)(수동커밋처리) 
	    // 7. **** 모든처리가 성공되었을시 commit 하기(commit) **** 
	    // 8. **** SQL 장애 발생시 rollback 하기(rollback) **** 
	   
	    // === Transaction 처리가 성공시 세션에 저장되어져 있는 loginuser 정보를 새로이 갱신하기 ===
	    // === 주문이 완료되었을시 주문이 완료되었다라는 email 보내주기  === // 
		
		HashMap<String, Object> paraMap = new HashMap<>();    //Object 는 다형성이기 때문에 어떤 타입이든 다 받아온다.
		
		
		
		// 주문 테이블에 insert 할 데이터
		String odrcode = getOdrcode(); // 주문코드(명세서번호)를 생성해주는 메소드 호출하기 
		                               //주문형식 : s+날짜+sequence => s20230427-1, s20230427-2, s20230427-3
		
		paraMap.put("odrcode", odrcode);  // 주문 코드 
		HttpSession session =  request.getSession();
		
		MemberVO loginuser = (MemberVO) session.getAttribute("loginuser");
		
		paraMap.put("userid", loginuser.getUserid());    // 회원 아이디
		paraMap.put("sum_totalPrice", sum_totalPrice);    // 주문 총액
		paraMap.put("sum_totalPoint", sum_totalPoint);    // 주문 총 포인트 
		
		
		
		
		// 주문상세 테이블에 insert 할 데이터
		
		paraMap.put("pnum_arr", pnum_join.split("\\,"));      // 제품 번호 {"5","3","4"}   // 애는 배열이다.
		paraMap.put("oqty_arr", oqty_join.split("\\,"));      // 주문량 {"3","5","2"}
		paraMap.put("totalPrice_arr", totalPrice_join.split("\\,"));      // 주문가격 {"99000","50000","26000"} 
 		
		
		// 장바구니 테이블에 delete 할 데이터 
		// 특정 제품을 "바로 주문하기"를 한 경우라면 cartno_join 의 값은 null 이다.
		if(cartno_join != null) {
			paraMap.put("cartno_arr", cartno_join.split("\\,"));    // 삭제할 장바구니 번호 {"9","6","5"}
		}
		
		
		// *** Transaction 처리를 해주는 메소드 호출하기 *** //
		// 위에 설명한 2~8번까지를 나타내는 부분이다.
		int isSuccess = pdao.orderAdd(paraMap);  // Transaction 처리를 해주는 메소드 
		
		
		// **** 주문이 완료되었을시 세션에 저장되어져 있는 loginuser 정보를 갱신하고
	    //      이어서 주문이 완료되었다라는 email 보내주기  **** //
		
		if(isSuccess == 1) {
			
			// 세션에 저장 되어져 있는 login user의 정보를 갱신한다.
			loginuser.setCoin( loginuser.getCoin() - Integer.parseInt(sum_totalPrice));  // 현재 코인 및 포인트에서 변동사항을 빼주거나 더한다..
			loginuser.setPoint( loginuser.getPoint() + Integer.parseInt(sum_totalPoint));
			
			
			
			/// 주문이 완료되었다는 mail 보내기 시작 부분 !!!!!!!!!!!!!!!!!!!!!!!!!!
			
			
			GoogleMail mail = new GoogleMail();
			
			String[] pnum_arr = pnum_join.split("\\,");  // pnum_join.split("\\,")은 배열 
			
			String pnumes = String.join("','", pnum_arr);
			// '5','3','4' 로 해주고 싶다
			
			pnumes = "'"+pnumes+"'";
			// '5','3','4' 로 해주고 싶다 완료
			
			// 주문한 제품에 대해 email 보내기시 email 내용에 넣을 주문한 제품번호에 대한 제품정보를 얻어오는 것 .
			List<ProductVO> jumunProductList = pdao.getJumunProductList(pnumes);
			
			String[] oqty_arr = oqty_join.split("\\,");    // 주문 수량 
			
			StringBuilder sb = new StringBuilder();
			
			sb.append("<div>");
			sb.append("주문코드번호 : <span style='color: blue; font-weight: bold;'>"+odrcode+"</span><br><br>");    // odrcode 는 주문 번호 
			sb.append("<주문상품><br>");
			
			// 사진을 주문힌 제품 숫자 만큼 이메일 주기 위해서 
			for (int i = 0; i < jumunProductList.size(); i++ ) {
				
				sb.append(jumunProductList.get(i).getPname()+"&nbsp;" + oqty_arr[i] + "개&nbsp;&nbsp;"  );  // 제품 명을 넣는다 List 안에 있는 걸 부르는건 get 
				sb.append("<img src = 'http://127.0.0.1:9090/MyMVC/images/"+ jumunProductList.get(i).getPimage1() + "' />");
				sb.append("<br>");
				
			}// end of for
			
			sb.append("<br>이용해 주셔서 감사합니다.");
			sb.append("</div>");
			
			
			 String emailContents = sb.toString();
			
			 mail.sendmail_Orderfinish(loginuser.getEmail(), loginuser.getName(), emailContents);   // 이러한 메소드를 보내겠다.
			
		}
		
		JSONObject jsobj = new JSONObject(); // {}
		jsobj.put("isSuccess", isSuccess);    // {"isSuccess":1} 또는 {"isSuccess":0}  앞에꺼는 성공 뒤에는 실패 
		
		
		String json = jsobj.toString();
		request.setAttribute("json", json);
		
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/jsonview.jsp");
		
	}

}
