<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    


<%
	String ctxPath = request.getContextPath();
	// ctxPath ==> /JSPServletBegin

%>

    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>개별 회언의 성향 결과</title>


<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">  <!-- 부트스트랩 -->

<!-- Bootstrap CSS -->   <!-- 절대 경로 -->
<link rel="stylesheet" href="<%= ctxPath%>/bootstrap-4.6.0-dist/css/bootstrap.min.css" type="text/css">



<style type="text/css">

	span#colorbox {  /* display가 inline 이기 때문에 width 못함 */
		display:inline-block;
		width: 30px;
		height: 30px;
		margin-left: 20px;
		
		position: relative;
		top: 8px;
	}

</style>




<script src="<%= ctxPath%>/js/jquery-3.6.4.min.js" type="text/javascript"></script>
<script src="<%= ctxPath%>/bootstrap-4.6.0-dist/js/bootstrap.bundle.min.js" type="text/javascript"></script>


<script type="text/javascript">


				
	$(document).ready(function(){
		
		$("span#colorbox").css('background-color', '${requestScope.psdto.color}');  
		//55 번줄 설명 :  ${requestScope.psdto.color} 는 그사람에 대한 색상을 가져온다. 
		
	});
	
	//Function Declaration
	function deletePerson(seq) {
		
		if(confirm("정말로 삭제하시겠습니까?")){
			
			/*location.href = "personDelete.do?seq="+seq;*/   // 이것은 GET방식으로 이렇게 쓰지 말자
			const frm = document.dml_frm;
			frm.seq.value = seq;
			
			frm.action = "personDelete.do";
			frm.method = "post";		// post 방식
			frm.submit();				// 보낸다.
			
		}
		
		
	} // end of deletePerson(seq) {
		
		
	function updatePerson(seq) {
			
		// 전송방식 GET 방식은 위험하므로 POST 방식으로 한다. (회원번호를 사용)
		const frm = document.dml_frm;
		
		frm.seq.value = seq;    // seq 를 담는다.
		
		frm.action = "personUpdate.do";  // 이 form 에 대한 action
		frm.method = "post";    // post 방식 사용
		frm.submit();  		    // 이 form 을 전송한다. 
		
	}// end of function updatePerson(seq) {
		
		
		

</script>



</head>
<body class="py-3">

	<div class="container">
			<div class="card">
				<div class="card-header">  <%-- card header 에는 카드 머리말이 들어갑니다. --%>
					<p class="h2 text-center">
						<span class="text-primary">${requestScope.psdto.name}</span>&nbsp;<small>님의 개인성향 결과</small> 
					</p>
				</div>
				<div class="card-body">  			 <%-- card body 에는 카드의 본문이 들어갑니다. --%>
					<div class="row  mx-4 my-3 border border-top-0 border-left-0 border-right-0">  <%-- 반응형 웹으로 12분할 하겠다. --%>   <%-- mx 는 margin 좌우를 뜻함 my는 margin 상하--%>		
						<div class="col-md-3 py-2">회원번호</div>    
						<div class="col-md-9 py-2"><span class="h5">${requestScope.psdto.seq}</span></div>
					</div>
					<div class="row border mx-4 my-3 border border-top-0 border-left-0 border-right-0"> 			<%-- border 는 각 자리에 테두리 생성 --%>
						<div class="col-md-3 py-2">성명</div>															<%-- py-2 는 padding 상하  --%>
						<div class="col-md-9 py-2"><span class="h5">${requestScope.psdto.name}</span></div>
					</div>
					<div class="row mx-4 my-3 border border-top-0 border-left-0 border-right-0"> 	
						<div class="col-md-3 py-2">학력</div>
						<div class="col-md-9 py-2"><span class="h5">${requestScope.psdto.school}</span></div>
					
					</div>		
					
					<div class="row mx-4 my-3 border border-top-0 border-left-0 border-right-0"> 	
						<div class="col-md-3 py-2">색상</div>
						<div class="col-md-9 py-2">
							<c:choose>
			                     <c:when test="${requestScope.psdto.color eq 'red'}"><span class="h5">빨강</span></c:when>
			                     <c:when test="${requestScope.psdto.color eq 'blue'}"><span class="h5">파랑</span></c:when>
			                     <c:when test="${requestScope.psdto.color eq 'yellow'}"><span class="h5">노랑</span></c:when>
			                     <c:when test="${requestScope.psdto.color eq 'green'}"><span class="h5">초록</span></c:when>
								 <c:otherwise><span class="h5">기타</span></c:otherwise>
					
							</c:choose>
							
							 <span id="colorbox" class="rounded-circle"></span>      <!-- rounded-circle 는 동그랗게 해주는 것이다. -->
						
						</div>
						
					</div>	
					
					<div class="row mx-4 my-3 border border-top-0 border-left-0 border-right-0"> 		
						<div class="col-md-3 py-2">음식</div>
						<div class="col-md-9 py-2"><span class="h5">${requestScope.psdto.strFood}</span></div>
			
					</div>
					
					
					<div class="row mx-4 my-3 border border-top-0 border-left-0 border-right-0 ">
		               <div class="col-md-3 py-2">음식이미지 파일명</div>
		               <div class="col-md-9 py-2"><span class="h5">${requestScope.psdto.strFood_imgFileName}</span></div>
		            </div>
					
					<div class="row mx-4 my-3 border border-top-0 border-left-0 border-right-0"> 		
						<div class="col-md-3 py-2">음식사진</div>
						<div class="col-md-9 py-2">
						
						<span class="h5"></span>
						<c:if test="${not empty requestScope.psdto.strFood_imgFileName}">
							<c:forTokens var="imgFilename" items="${requestScope.psdto.strFood_imgFileName}" delims=","> <!-- delims 는 구분자 이 전체는 배열로 된다. -->
								<img src="<%= ctxPath%>/chap05_images/${imgFilename}" width="130" class="img-fluid  rounded  mr-1"/>  <!-- img-fluid 는 반응형  rounded 는 둥글게 해준다. mr-1 은 marggin right-->
							</c:forTokens>
						</c:if>
			
						<c:if test="${empty requestScope.psdto.strFood_imgFileName}">
							<span class="h5">좋아하는 음식이 없습니다.</span>
						</c:if>
						
						</div>
						
						
					</div>
					
					
				</div>
					
					
				
				<div class="card-footer">   <%-- card footer 에는 카드의 꼬리말이 들어갑니다. --%>
							
					<div class="row" style="position: relative; top: 10px;">
						<div class="col-md-3">
							<p class="text-center"><a href="personSelect.do" class="btn btn-info" role="button">목록보기1</a></p>   <!-- 여긴 상대경로 btn-info는 버튼에 색을 준다.   role 은 주석문을 쓰는 것이다.-->
						</div>
						
						<div class="col-md-3">
							<p class="text-center"><button type="button" class="btn btn-success" onclick="javascript:location.href='personSelect.do'">목록보기2</button></p>   <!-- 여긴 상대경로 btn-success는 버튼에 색을 준다.   role 은 주석문을 쓰는 것이다.-->
						</div>	
						
						<div class="col-md-3">														<%-- ${} 쓰기전에 '' 를 꼭 하자 !!!!!!!! --%>
							<p class="text-center"><button type="button" class="btn btn-danger" onclick="deletePerson('${requestScope.psdto.seq}')">삭제하기</button></p>   <!-- 여긴 상대경로 btn-success는 버튼에 색을 준다.   role 은 주석문을 쓰는 것이다.-->
						</div>																		<%-- 자바스크립트에 있는 deletePerson() 함수를 호출하겠다 라는 뜻이며 javascript 를 제거해도 문제 없다. --%>
						
						<div class="col-md-3">
							<p class="text-center"><button type="button" class="btn btn-primary" onclick="updatePerson('${requestScope.psdto.seq}')">내정보수정하기</button></p>   <!-- 여긴 상대경로 btn-success는 버튼에 색을 준다.   role 은 주석문을 쓰는 것이다.-->
						</div>																		<!-- post 방식으로 한다. -->																		
					</div>
			
				</div>
				
			</div>
		
	</div>
	
	
	
	<%-- POST 방식을 통해 회원의 정보를 변경하든지 또는 삭제하는 form 태그를 만든다. --%>
	<%-- 그런데 이 form 태그는 화면에 보이지 않도록 해야 한다. --%>
	<form name="dml_frm">
		<input type="hidden" name="seq" />
	</form>
	




</body>
</html>