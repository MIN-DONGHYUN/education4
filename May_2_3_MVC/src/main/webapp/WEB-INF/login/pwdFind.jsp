<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    String ctxPath = request.getContextPath();
    //    /MyMVC
%>


<!-- Required meta tags -->
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

<!-- Bootstrap CSS -->
<link rel="stylesheet" type="text/css" href="<%= ctxPath%>/bootstrap-4.6.0-dist/css/bootstrap.min.css" > 

<!-- 직접 만든 CSS -->
<link rel="stylesheet" type="text/css" href="<%= ctxPath%>/css/style.css" />

<!-- Optional JavaScript -->
<script type="text/javascript" src="<%= ctxPath%>/js/jquery-3.6.4.min.js"></script>
<script type="text/javascript" src="<%= ctxPath%>/bootstrap-4.6.0-dist/js/bootstrap.bundle.min.js" ></script>



<script type="text/javascript"> 

	$(document).ready(function(){
		
		// 찾기  (버튼을 눌렀을때) 
		$("button#btnFind").click(function() {
			
			// 아이디에 대한 유효성 검사 
			const useridVal = $("input#userid").val().trim();   // 값을 알아오자 
			
			if(useridVal == "") {
				alert("아이디를 입력하세요!!");
				return;  // 종료
			}
			
			// 이메일에 대한 정규표현식을 사용한 유효성 검사 
			
			// 정규표현식 이메일 부분 
			const regExp = /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i; 

			
			const bool = regExp.test($("input#email").val());  // 값을 넣어준다. 정규표현식이면 true 아니면 false
			
			
			if(!bool) {   // bool 이 false 이라면 
				alert("올바른 email 을 입력하세요!!");
				return;
			}
			
			const frm = document.pwdFindFrm;		// 폼을 잡는다.
			frm.action = "<%= ctxPath %>/login/pwdFind.up";			// 여기에 post 방식으로 보낸다.
			frm.method = "post";
			frm.submit();
			
		}); // end of $("button#btnFind").click(function() {
			
		
		// GET 방식일때 div_findResult를 나타내지 않게 하기 위해서 다음과 같이 한다. 
		
		const method = "${requestScope.method}";   // 이렇게 사용해야만 String 타입을 나타낼 수 있다.
		//console.log("method ==> " + method);
		
		// GET 방식이라면 
		if(method == "GET") {
			$("div#div_findResult").hide();    //숨겨라 라는 말이다.  POST 방식일때는 보여줄 것이다.
		}
		else if(method == "POST") {   		  // POST 방식일때
			
			// 결과물도 보이면서 아이디와 이메일을 나타내어 주겠다.
			$("input#userid").val("${requestScope.userid}");
			$("input#email").val("${requestScope.email}");	
			$("div#div_findResult").show();   // 보여라  
			
			if(${requestScope.sendMailSuccess == true}) {
				$("div#div_btnFind").hide();   // 찾기 버튼을 숨기자 
			}
		}	
		
		
		// 인증하기 
		$("button#btnConfirmCode").click(function() {		//"POST" 방식으로 보낼것이므로 form 태그를 써야 한다.
			
			const frm = document.verifyCertificationFrm;
		
			frm.userCertificationCode.value = $("input#input_confirmCode").val();   // input_confirmCode의 값을 담아서 보내자 
			frm.userid.value = $("input#userid").val();
			
			frm.action = "<%= ctxPath %>/login/verifyCertification.up";
			frm.method = "post";
			frm.submit();
			
		});
		
			
			
		
	}); // end of $(document).ready(function(){


		
		
		

</script>





<form name="pwdFindFrm">

	<ul style="list-style-type: none">
         <li style="margin: 25px 0">
            <label for="userid" style="display: inline-block; width: 90px">아이디</label>
            <input type="text" name="userid" id="userid" size="25" placeholder="아이디" autocomplete="off" required />   <!-- autocomplete 은 자동완성 기능 -->
         </li>
         <li style="margin: 25px 0">
            <label for="email" style="display: inline-block; width: 90px">이메일</label>
            <input type="text" name="email" id="email" size="25" placeholder="abc@def.com" autocomplete="off" required />
         </li>
   </ul>
   
   <div class="my-3" id="div_btnFind">
    <p class="text-center">
       <button type="button" class="btn btn-success" id="btnFind">찾기</button>
    </p>
   </div>
   
</form>


<div class="my-3" id="div_findResult">
	
      <p class="text-center">
           
           <!-- isUserExist 에 받아온 값이 없다면 즉 false 라면  -->
           <c:if test="${requestScope.isUserExist == false}">
           		<span style="color: red;"> 사용자 정보가 없습니다.</span>
           </c:if>
           
           <!-- isUserExist 에 받아온 값이 있다면 즉 true 라면  -->
           <c:if test="${requestScope.isUserExist == true && requestScope.sendMailSuccess == true}">
           		<span style="font-size: 10pt;"> 인증코드가 ${requestScope.email}로 발송되었습니다.</span><br>
           		<span style="font-size: 10pt;">인증코드를 입력해주세요.</span><br>
	            <input type="text" name="input_confirmCode" id="input_confirmCode" required />
	            <br><br>
	            <button type="button" class="btn btn-info" id="btnConfirmCode">인증하기</button>
           </c:if>
           
           <c:if test="${requestScope.isUserExist == true && requestScope.sendMailSuccess == false}">
           		<span style="color: red;"> 메일발송이 실패했습니다.</span>
           </c:if>
      </p>
</div>

<%-- 인증하기 form 을 만들었다. --%>
<form name="verifyCertificationFrm">

	<input type="hidden" name="userCertificationCode" />   <%-- 사용자가 입력한 인증코드 --%>
	<input type="hidden" name="userid" /> 
	
</form>

    