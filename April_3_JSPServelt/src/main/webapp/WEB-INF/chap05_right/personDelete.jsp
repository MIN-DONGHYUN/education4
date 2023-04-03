<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원삭제 성공시 메시지 보여주기</title>

<script type="text/javascript">

	window.onload = function() {
		alert("회원번호 ${requestScope.psdto.seq}번 ${requestScope.psdto.name}님 을 삭제했습니다.");
		location.href = "personSelect.do";		// 모든 회원을 보여주는 페이지로 가자 
	}

</script>


</head>
<body>

	
	
</body>
</html>