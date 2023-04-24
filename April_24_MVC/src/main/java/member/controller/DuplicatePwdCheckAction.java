package member.controller;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import common.controller.AbstractController;
import member.model.*;

public class DuplicatePwdCheckAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String method = request.getMethod();
		//"Get" 또는 "POST"
		
		if("POST".equalsIgnoreCase(method))
		{
			
			String new_pwd = request.getParameter("new_pwd");		// memberEdit 에서 가져온다 ajax 부분 
			String userid = request.getParameter("userid");    // 누구 것의 비밀번호인지 확인하기 위해 써야함 
			
			//DB에 가자 
			InterMemberDAO mdao = new MemberDAO();
			
			Map<String, String> paraMap = new HashMap<>();
			
			paraMap.put("new_pwd", new_pwd);   //Map 에 저장 
			paraMap.put("userid", userid);
			
			int n = mdao.duplicatePwdCheck(paraMap);
			// n ==> 1 이라면 현재 사용중인 암호를 동일한 새 암호로 준 경우 
			// n ==> 0 이라면 현재 사용중인 암호와 다른 새 암호로 준 경우 
			
			
			JSONObject jsonObj = new JSONObject();    //{} 자바 스크립트 객체
			jsonObj.put("n", n);   // 생성된 객체 넣음   // {"n":0} 또는 {"n":1}
			
			String json = jsonObj.toString();   // String 타입으로 바꾼다.   "{"n":0}" 또는 "{"n":1}"
			
			request.setAttribute("json", json);   // request 에 저장 
			
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/jsonview.jsp");
		}
	}

}
