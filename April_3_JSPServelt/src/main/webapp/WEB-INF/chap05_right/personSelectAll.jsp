<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>



<%@ page import="java.util.*, chap05.PersonDTO_02" %>


<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>   
 
 <%
 	String ctxPath = request.getContextPath();
 	// ctxPath ==> /JSPServletBegin
 %>
 
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>개인성향 모든 정보 출력 페이지</title>

<!-- Required meta tags -->

<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">  <!-- 부트스트랩 사용시   -->



<!-- Bootstrap CSS -->
<link rel="stylesheet" href="<%= ctxPath %>/bootstrap-4.6.0-dist/css/bootstrap.min.css" type="text/css">
<script src="<%= ctxPath %>/js/bootstrap.bundle.min.js" type="text/javascript"></script>



<style type="text/css">

	div#div1 {
		/* border: solid 1px red; */
		width: 80%;
		margin: 20px auto;
	}
	div#div1 > table {
	   border: solid 1px gray; 
	   width: 100%;
	   border-collapse: collapse;
	}
	div#div1 > table  th, div#div1 > table  td {
	   border: solid 1px gray; 
	   
	}
	
	div#div1 > table > tbody > tr:hover {
		
		background-color: blue;
		color: white;
		cursor: pointer;				/*  마우스 모양 손가락  */
		
	}
	
	table > tbody > tr > td:first-child > span {    /* Seq 를 잡아서 보이지 않게 하기 위해서 */
		display: none;     /* 보이지 마라 */
	}

</style>

<script type="text/javascript" src="<%= ctxPath%>/js/jquery-3.6.4.min.js"></script>   <!-- JQuery 사용 위해 --> 
<script type="text/javascript">

	$(document).ready(function() {
		
		$("tbody > tr").click((e) => {			/*  화살표 함수  */
			
			//alert("확인용 : " + $(e.target).html());  // 내가 클릭한 곳의 html을 확인해보자 		
			//alert("회원번호 : " + $(e.target).parent().find("span").text());
		
			const seq = $(e.target).parent().find("span").text();
			
			location.href="personDetail.do?seq="+seq;   /* 데이터를 달아서 넘겨줘야 한다. 그러므로 ?를 달아서 보내준다. */
														/* URL로 넘기는데 데이터 값을 넘기기 위해 ?GET파라미터 + 변수 로 넘겨준다. */		
			
		});
		
	});
	





</script>



</head>
<body>

	<div id="div1">
	
		<h2>개인 성향 모든 정보 출력 페이지(스트립틀릿 사용하여 작성한 것)</h2>
<%
	List<PersonDTO_02> personList = (List<PersonDTO_02>)request.getAttribute("personList");
	
	if(personList.size() > 0) {
%>
	<table>
		<thead>
			<tr>
				<th>성명</th>
				<th>학력</th>
				<th>색상</th>
				<th>음식</th>
				<th>등록일자</th>
			</tr>
		</thead>
		
		<tbody>
<%
		for( PersonDTO_02 psdto : personList) {
%>			
			<tr>
				<td> <span><%=psdto.getSeq() %></span> <%=psdto.getName() %></td>
				<td><%=psdto.getSchool() %></td>
				<td><%=psdto.getColor() %></td>
				<td><%=psdto.getStrFood() %></td>
				<td><%=psdto.getRegisterday() %></td>
			</tr>
<% 			
			
		}// end of for 




%>					
		</tbody>
	</table>
<% 		
	}
	else {
	%>
		<span>데이터가 존재하지 않습니다.</span>
<%	 
	}

%>
		
	</div>

	<hr style="border: solid 1px red; margin: 40px 0;">

	
	<div id="div2" class="container">
	
		<h2>개인 성향 모든 정보 출력 페이지(JSTL 사용하여 작성한 것)</h2>
		
		<c:if test="${not empty requestScope.personList}">
		
		<table class="table table-hover">
			<thead>
				<tr>
					<th>성명</th>
					<th>학력</th>
					<th>색상</th>
					<th>음식</th>
					<th>등록일자</th>
				</tr>
			</thead>
			
			<tbody>
				<c:forEach var="psdto" items="${requestScope.personList}">
					<tr style="cursor: pointer;">
						<td><span>${psdto.seq}</span>${psdto.name}</td>
						<td>${psdto.school}</td>
						<td>${psdto.color}</td>
						<td>${psdto.strFood}</td>
						<td>${psdto.registerday}</td>
					</tr>
				</c:forEach>
			</tbody>
		
		</table>
		
		</c:if>
		
		<c:if test="${empty requestScope.personList}">
		
		</c:if>
	</div>
	
	<p class= "text-center mt-5">
		<button type="button" class="btn btn-info" onclick="javascript:location.href='personRegister.do'">개인성향 입력페이지로 가기 </button>
		<!-- locatio.href 를 잘 알아두자   -->
		
		
	</p>


</body>
</html>