package myshop.controller;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import common.controller.AbstractController;
import myshop.model.*;

public class IsOrderAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String fk_pnum = request.getParameter("fk_pnum");
		String fk_userid = request.getParameter("fk_userid");   // ajax 로 넘겨준 prodView.jsp 파일에서 받아옴 
		
		Map<String, String> paraMap = new HashMap<>();
		
		
		// Map 저장 
		paraMap.put("fk_pnum", fk_pnum);
		paraMap.put("fk_userid", fk_userid);
		
		InterProductDAO pdao = new ProductDAO();
		
		boolean bool = pdao.isOrder(paraMap);
		
		//ajax 에 json 썻으므로 
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("isOrder", bool);
		
		
		String json = jsonObj.toString();   // json을 String 타입으로 나타내주기 
		request.setAttribute("json", json);
		
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/jsonview.jsp");
	
	}

}
