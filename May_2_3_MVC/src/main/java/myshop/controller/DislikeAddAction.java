package myshop.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import common.controller.AbstractController;
import myshop.model.InterProductDAO;
import myshop.model.ProductDAO;

public class DislikeAddAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String fk_userid = request.getParameter("fk_userid");
		String fk_pnum = request.getParameter("fk_pnum");
		
		Map<String, String> paraMap = new HashMap<>();
		
		paraMap.put("fk_userid", fk_userid);
		paraMap.put("fk_pnum", fk_pnum);
		
		
		InterProductDAO pdao = new ProductDAO();
		
		int n = pdao.dislikeAdd(paraMap);
		// n ==> 1 이라면 insert 되어 정상 투표 
		// ㅜ ==> 0 이라면 중복된 투표 
		
		String message = "";
		
		if(n == 1) {
			message = "해당제품에 \n 싫어요를 클릭하셨습니다. ";
			
		}
		else {
			message = "이미 싫어요를 클릭하셨기에 \n 두번 이상 싫어요는 불가합니다.";
		}
		
		JSONObject jsonObj = new JSONObject();
		
		jsonObj.put("message", message);   
		// {"message":"해당제품에 \n 싫어요를 클릭하셨습니다."}
		// 또는
		// {"message":"이미 싫어요를 클릭하셨기에 \n 두번 이상 좋아요는 불가합니다.")
		
		String json = jsonObj.toString();
		
		request.setAttribute("json", json);
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/jsonview.jsp");

	}

}
