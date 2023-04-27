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


<style type="text/css">

	#div_pwd {
      width: 70%;
      height: 15%;
      margin-bottom: 5%;
      margin-left: 10%;
      position: relative;
   }
   
   #div_pwd2 {
      width: 70%;
      height: 15%;
      margin-bottom: 5%;
      margin-left: 10%;
      position: relative;
   }
   
   #div_updateResult {
      width: 90%;
      height: 15%;
      margin-bottom: 5%;
      margin-left: 10%;      
      position: relative;
   }
   
   #div_btnUpdate {
      width: 70%;
      height: 15%;
      margin-bottom: 5%;
      margin-left: 10%;
      position: relative;
   }


</style>



<script type="text/javascript"> 

	$(document).ready(function(){
		
		//암호 변경하기 버튼을 클릭시 이벤트 발생
		$("button#btnUpdate").click(function(){
			
			const pwd = $("input#pwd").val();			// 값을 가져온다.
			const pwd2 = $("input#pwd2").val();
			
			// 정규표현식 비밀번호 부분 
			const regExp = /^.*(?=^.{8,15}$)(?=.*\d)(?=.*[a-zA-Z])(?=.*[^a-zA-Z0-9]).*$/g;
			
			const bool = regExp.test(pwd);  // 값을 넣어준다. 정규표현식이면 true 아니면 false
			
			// false 라면 
			if(!bool) {
				alert("암호는 8글자 이상 15글자 이하의 영문자, 숫자, 특수기호가 혼합되어야만 합니다.");
				$("input#pwd").val("");		// 암호를 비워라 
				$("input#pwd2").val("");
				return;						// 종료
			}
			else if(bool && pwd != pwd2)   // 암호는 정규 표현식 통과 but 암호확인 부분에서 틀린 경우 
			{
				alert("암호가 일치하지 않습니다.");
				$("input#pwd").val("");		// 암호를 비워라 
				$("input#pwd2").val("");
				return;	
			}
			else {							// 암호 확인과 같고 정규 표현식도 통과 하면 POST 방식으로 준다.
				const frm = document.pwdUpdateEndFrm;
				frm.action = "<%= ctxPath %>/login/pwdUpdateEnd.up";
				frm.method = "post";
				frm.submit();
			}
			
		});// end of $("button#btnUpdate").click(function(){
			
		
	}); // end of $(document).ready(function(){


		
		
		

</script>





<form name="pwdUpdateEndFrm">


	<div id="div_pwd" align="center">
      <span style="color: blue; font-size: 12pt;">새암호</span><br/> 
      <input type="password" name="pwd" id="pwd" size="25" placeholder="PASSWORD" required />
   </div>
   
   <div id="div_pwd2" align="center">
        <span style="color: blue; font-size: 12pt;">새암호확인</span><br/>
      <input type="password" id="pwd2" size="25" placeholder="PASSWORD" required />
   </div>

	<input type="hidden" name="userid" value="${requestScope.userid}" />   <%-- value 값이 넘어와야만 한다. --%>


	<c:if test="${requestScope.method == 'GET'}">    <%-- GET 방식일떼  --%>
		<div id="div_btnUpdate" align="center">
	           <button type="button" class="btn btn-success" id="btnUpdate">암호변경하기</button>
	    </div> 
	</c:if>

</form>


<c:if test="${requestScope.method == 'POST' && requestScope.n == 1}">    <%-- GET 방식일떼  --%>
	<div id="div_btnUpdateResult" align="center">
           사용자 ID ${requestScope.userid} 님의 암호가 새로이 변경되었습니다.
    </div> 
</c:if>