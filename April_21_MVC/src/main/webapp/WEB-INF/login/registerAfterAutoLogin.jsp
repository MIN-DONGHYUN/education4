<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>

<script type="text/javascript">

	window.onload = function() {
		
		alert("회원가입에 감사드립니다.!!");
		
		const frm = document.loginFrm;
		frm.action = "<%= request.getContextPath() %>/login/login.up";   // login 쪽으로 보내라(uri임)
		frm.method = "post";					// post 방식
		frm.submit();
		
	} //end of window.onload = function() {



</script>


</head>
<body>

	<form name="loginFrm">
		<input type="hidden" name="userid" value="${requestScope.userid}" />		<%-- name 에는 그 컬럼값인 것을 써야한다. 즉 loginAction 파일을 참고해야 한다. --%>
		<input type="hidden" name="pwd" value="${requestScope.pwd}" />				<%-- value 는 request를 저장한 MemberRegister 에서 받아온것을 쓴다. --%>
	
	
	</form>


</body>
</html>