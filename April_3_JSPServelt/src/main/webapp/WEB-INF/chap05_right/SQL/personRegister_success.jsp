<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%
	String ctxPath = request.getContextPath();
	// ctxPath =  
%>    
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>개인성향 입력한 결과가 성공인 경우에 보여주는 페이지</title>

<script type="text/javascript">

	// Function Declaration
	function goSelect() {
		
		// === !!! 암기 부분  !!! ===
		// 자바스크립트에서 URL 페이지의 이동은 lication.href="이동할 URL 경로명"; 으로 한다.
		
		location.href="<%= ctxPath%>/personSelect.do";
		
	} // end of function goSelect() {

</script>


</head>
<body>
	<h2>개인성향 입력 성공</h2>
	<br>
	<button onclick="goSelect()">입력결과 조회하기 </button>
</body>
</html>