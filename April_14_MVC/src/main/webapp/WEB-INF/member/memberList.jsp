<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>   <!--  fn은 함수를 뜻함  -->

<%
	String ctxPath = request.getContextPath();
	//        /MyMVC
%>


<jsp:include page="../header.jsp"></jsp:include>				<!-- header 불러오기-->



<style type="text/css">



</style>


<script type="text/javascript">

	$(document).ready(function(){
		
		 
		// 처음 실행될때 "" 로 받아올때는 하지 말고 나머지만 해준다.
		if( "${fn:trim(requestScope.searchWord)}" != "" ) {     // 공백만 써왔을때를 대비해서 fn 함수로 trim() 해준다.
			// 문서가 로딩 되자마자 아무것도 없는 값을 가져오므로 처음에는 모두 없는 상태로 나오게 된다.
			// request에 저장된 요소를 가져오자 
			$("select#searchType").val("${requestScope.searchType}");
			$("input#searchWord").val("${requestScope.searchWord}");
				
		}
		
		$("select#sizePerPage").val("${requestScope.sizePerPage}");  // 값을 꽂아준다.
		
		
		
		$("input#searchWord").bind("keyup", (e) => {
			if(e.keyCode == 13) {					// Code는 대문자를 써야함   // 13은 엔터 검색어에서 엔터를 하면 검색하러 가도록 한다.
				
				goSearch();
			}
		});
		
		// 입력한 것이 있는지 확인하자
		//console.log("${param.searchWord}");   // 이것은 request.getparameter 와 같은 역할을 한다. 
	    
		
		
		//***** select 태그라면은 이벤트는 change 를 사용해야 한다. *****//
		$("select#sizePerPage").bind("change", function(){
			
			goSearch();    // 개수만큼만 가져오도록 할것이기 때문에 일단 찾는다.
			
			
		});
		
		
		
		
		
	}); // end of $(document).ready(function(){
	
	// 검색을 눌렀을때 기능을 해주는 것이다.
	//function Deciatation
	function goSearch(){
		const frm = document.memberFrm;
		
		<%--
			if( frm.searchWord.value.trim() == "" ) {
				alert("검색어를 올바르게 입력하세요!!");
				return;   // 함수 종료 
			}
		--%>
		
		frm.action = "memberlist.up";
		frm.method = "get";				// 보안 위험이 없으므로 get 방식으로 찾겠다.
		frm.submit();		// 보낸다.
		
	}

</script>


<h2 style="margin: 20px;">::: 회원전체 목록 :::</h2>
 
	<form name="memberFrm">   <!--  검색하기 위해 form 태그 사용  -->
   		
   		<select id="searchType" name="searchType">
   			<option value="">선택하세요</option> 
   			<option value="name">회원명</option>    	<!-- value는 DB에 있는 그대로 써와야 한다. -->
   			<option value="userid">아이디</option>
   			<option value="email">이메일</option>
   		
   		</select>
   		
   		<input type="text" id="searchWord" name="searchWord" />   <!--  검색어 부분 -->
   		
   		<%-- 
			form 태그내에서 데이터를 전송해야할 input 태그가 만약에 1개 밖에 없을 경우에는 
			input 태그에 값을 넣고나서 그냥 엔터를 해버리면 submit 되어져 버린다.
			그래도 유효성 검사를 거친후 submit 을 하고 싶어도 input 태그가 만약 에 1개 밖에 없을 경우라면 유효성검사가 있더라도 유효성검사를 거치지 않고 바로 submit 되어진다.
			이것을 막으려면 form 태그 내에 input 태그가 2개 이상 존재하면 된다. 
			그런데 실제 화면에 보여질 input 태그는 1개 이여야 한다. 
			이럴경우 아래와 같이 하면 된다. 
   		 --%>
   		
   		<input type="text" style="display: none;"> <%-- 조심할 것은 type="hidden" 이 아닌것이다. --%>
   		
   		
   		<button type="button" class="btn btn-secondary" style="margin-left: 30px; margin-right: 30px;" onclick="goSearch()">검색</button> 
   		
   		<span style="color: red; font-weight: bold; font-size: 12pt;">페이지당 회원명수-</span>
	    <select id="sizePerPage" name="sizePerPage">			<%-- select 태그라면은 이벤트는 change 를 사용해야 한다. --%>
	         <option value="10">10개</option>
	         <option value="5">5개</option>
	         <option value="3">3개</option>
	    </select>
   		
   		
   
	</form>


	<table id="memberTbl" class="table table-bordered" style="width: 90%; margin-top: 20px;">
        <thead>
           <tr>
              <th>아이디</th>
              <th>회원명</th>
              <th>이메일</th>
              <th>성별</th>
           </tr>
        </thead>
    	
    	<tbody>
    		<c:if test="${not empty requestScope.memberList}">    <!-- 넘어온 값이 있다면  != null 로 사용하지 말고 not empty 라고 쓰지  -->
	    		<c:forEach var="mvo" items="${requestScope.memberList}">  <!-- 반복문 사용   member vo 를 가짐 -->
	     			 <tr>
		                 <td>${mvo.userid}</td>    <!--  항상 첫글자는 소문자 mvo. 다음에는 get이 생략되어있다고 생각하면 된다. getuserid 라고 생각하면 이해하기 쉽다. -->
		                 <td>${mvo.name}</td>
		                 <td>${mvo.email}</td>
		                 
		                 <!--  1일때는 남자 2일때는 여자 로 표시하자 -->
			             <td>
			             	
							<c:choose>
								<c:when test="${mvo.gender eq '1'}">
									남
								</c:when>
							
								<c:when test="${mvo.gender eq '2'}">
									여
								</c:when>								
							</c:choose>
							
						</td>
		                 
		            </tr>
	    		</c:forEach>
    		</c:if>
 
 			<c:if test="${empty requestScope.memberList}">    <!-- 넘어온 값이 없다면    -->
 				 <tr>
	                 <td colspan="4">가입된 회원이 없습니다.</td>    <!--  colspan 은 4개의 컬럼을 묶어서 나타낸다. -->
	            </tr>
 			</c:if>
    	</tbody>
	</table>



















<jsp:include page="../footer.jsp"></jsp:include>				=<!-- footer 불러오기-->    