package myshop.controller;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import common.controller.AbstractController;
import myshop.model.*;

public class MailDisplayJSONAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String sname = request.getParameter("sname");    // "HIT",    "NEW" 등 중에 지금은 "HIT" 가 들어옴 
		String start = request.getParameter("start");
		String len = request.getParameter("len");

		InterProductDAO pdao = new ProductDAO();   // ProduectDAO 을 쓸것 
		
		// 값을 넘기기 위해 MAP 사용
		Map<String, String> paraMap = new HashMap<>();
		
		// Map 에 값을 넣어준다.
		paraMap.put("sname", sname);		// "HIT",    "NEW"
		paraMap.put("start", start);		// "1"  "9"   "17"   "25"  "33"
			
		
		String end = String.valueOf(Integer.parseInt(start) + Integer.parseInt(len) -1);   // String.valueof 를 쓴 이유는 MAP에 저장을 해야 하기 때문에 String 타입으로 변경하기 위해 사용
											//end => start + len - 1; 
		paraMap.put("end", end);            //end =>  "8"   "16"   "24"   "32"   "40"	
		
		
		List<ProductVO> prodList = pdao.selectBySpecName(paraMap);    // 리스트를 사용 select 받아올것이 많으므로 
		
		// JSON의 복수개 이므로 기존에 했던것과 다르다
		
		JSONArray jsonArr = new JSONArray();		// [] 이런 모양이다.
		
		// 값이 존재 한다면 
		if(prodList.size() > 0) {
			// DB 에서 조회해 온 결과물이 있을 경우

			for(ProductVO pvo : prodList) {
				
				JSONObject jsonObj = new JSONObject();  			// {}
				jsonObj.put("pnum", pvo.getPnum());     			// {"pnum" : 36, }    //pvo.getPnum 은 제춤 번호
				jsonObj.put("pname", pvo.getPname());   			// ("pname" : 36, "pname":"노트북30" }   //pvo.getPname 은 제품 이름 			
				jsonObj.put("code", pvo.getCategvo().getCode());	// ("pname" : 36, "pname":"노트북30" , "code":"100000"}
				jsonObj.put("pcompany", pvo.getPcompany());         // ("pname" : 36, "pname":"노트북30" , "code":"100000" , "pcompany": "삼성전자"}
				jsonObj.put("pimage1", pvo.getPimage1());
	            jsonObj.put("pimage2", pvo.getPimage2());
	            jsonObj.put("pqty", pvo.getPqty());
	            jsonObj.put("price", pvo.getPrice());
	            jsonObj.put("saleprice", pvo.getSaleprice());
	            jsonObj.put("sname", pvo.getSpvo().getSnum()); 
	            jsonObj.put("pcontent", pvo.getPcontent());
	            jsonObj.put("point", pvo.getPoint());
	            jsonObj.put("pinputdate", pvo.getPinputdate());
	            jsonObj.put("discoutPercent", pvo.getDiscountPercent());   // ProductVO.java에 할인률 메소드 불러온것임
	            													// ("pname" : 36, "pname":"노트북30" , "code":"100000" , "pcompany": "삼성전자" , ....... , "pinputdate"":"2023-04-19" , "discoutPercent":17}
				
	            jsonArr.put(jsonObj);  // [{}]
	            					   // [{},{}]
	            					   // [{},{},{}]
	            
				
			}// end of for
				
		} // end of if
		
		String json = jsonArr.toString();     //[ {},{},{},......{} ]  이것을 문자열로 변환하기 
		// "[ {},{},{},......{} ]"  로 변경됨

		//System.out.println("확인용 : json => " + json);
		// DB애서 조회해온 결과물이 있을 경우 
		// 확인용 json => [{"pnum":36,"code":"100000","pname":"노트북30","pcompany":"삼성전자","saleprice":1000000,"discoutPercent":17,"point":60,"pinputdate":"2023-04-19","pimage1":"59.jpg","pqty":100,"pimage2":"60.jpg","pcontent":"30번 노트북","price":1200000,"sname":0},{"pnum":35,"code":"100000","pname":"노트북29","pcompany":"레노버","saleprice":1000000,"discoutPercent":17,"point":60,"pinputdate":"2023-04-19","pimage1":"57.jpg","pqty":100,"pimage2":"58.jpg","pcontent":"29번 노트북","price":1200000,"sname":0},{"pnum":34,"code":"100000","pname":"노트북28","pcompany":"아수스","saleprice":1000000,"discoutPercent":17,"point":60,"pinputdate":"2023-04-19","pimage1":"55.jpg","pqty":100,"pimage2":"56.jpg","pcontent":"28번 노트북","price":1200000,"sname":0},{"pnum":33,"code":"100000","pname":"노트북27","pcompany":"애플","saleprice":1000000,"discoutPercent":17,"point":60,"pinputdate":"2023-04-19","pimage1":"53.jpg","pqty":100,"pimage2":"54.jpg","pcontent":"27번 노트북","price":1200000,"sname":0},{"pnum":32,"code":"100000","pname":"노트북26","pcompany":"MSI","saleprice":1000000,"discoutPercent":17,"point":60,"pinputdate":"2023-04-19","pimage1":"51.jpg","pqty":100,"pimage2":"52.jpg","pcontent":"26번 노트북","price":1200000,"sname":0},{"pnum":31,"code":"100000","pname":"노트북25","pcompany":"삼성전자","saleprice":1000000,"discoutPercent":17,"point":60,"pinputdate":"2023-04-19","pimage1":"49.jpg","pqty":100,"pimage2":"50.jpg","pcontent":"25번 노트북","price":1200000,"sname":0},{"pnum":30,"code":"100000","pname":"노트북24","pcompany":"한성컴퓨터","saleprice":1000000,"discoutPercent":17,"point":60,"pinputdate":"2023-04-19","pimage1":"47.jpg","pqty":100,"pimage2":"48.jpg","pcontent":"24번 노트북","price":1200000,"sname":0},{"pnum":29,"code":"100000","pname":"노트북23","pcompany":"DELL","saleprice":1000000,"discoutPercent":17,"point":60,"pinputdate":"2023-04-19","pimage1":"45.jpg","pqty":100,"pimage2":"46.jpg","pcontent":"23번 노트북","price":1200000,"sname":0}]
		
		// 또는 DB에서 조회해 온 결과물이 없을 경우 
		
		// 확인용 json => []
		// 만약에 Select 되어진 정보가 없다라면 [] 로 나오므로 NULL 이 아닌 요소가 없는 빈배열이다.
		
		request.setAttribute("json", json);
		
		
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/jsonview.jsp");
		
	}

}
