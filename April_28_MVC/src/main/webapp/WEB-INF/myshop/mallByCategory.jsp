<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="../header2.jsp" />  


<style type="text/css">
	
	label.prodInfo {
		display: inline-block;  /* width 를 주기 위해 */
		width: 48px;
		margin-left: 5px;
	}

</style>

<%-- === 특정 카테고리에 속하는 제품들을 페이지바를 사용한 페이징 처리하여 조회(select)해온 결과 ===  --%>
  
 <div>
 	<c:if test="${not empty requestScope.productList}">
	 		
 		<c:forEach begin="1" end="1" var="pvo" items="${requestScope.productList}">  <%-- begin, end 가 모두 1이므로 1개만 나온다. --%>			
 			<p class="h3 text-center my-3">- ${pvo.categvo.cname} - </p>
 		</c:forEach>
 		
 		<div class = "row">
 			<c:forEach var="pvo" items="${requestScope.productList}" varStatus="status">  
	 			<c:choose>
	 				<c:when test="${status.count==1 or status.count==6}">
				 			<div class = "col-md-6 col-lg-2 offset-lg-1">
			                   <div class="card mb-3">
				                   <img src='/MyMVC/images/${pvo.pimage1}' class='card-img-top' style='width: 100%'/>
				                   <div class='card-body' style='padding: 0; font-size: 9pt;'>
				                     <ul class='list-unstyled mt-2'> 
				                          <li><label class='prodInfo'>스펙명</label>${pvo.spvo.sname}</li>
				                          <li><label class='prodInfo'>제품명</label>${pvo.pname}</li> 
				                          <li><label class='prodInfo'>정가</label><span style="color: red; text-decoration: line-through;"><fmt:formatNumber value="${pvo.price}" pattern="#,###" /> 원</span></li> 
				                          <li><label class='prodInfo'>판매가</label><span style="color: red; font-weight: bold;"><fmt:formatNumber value="${pvo.saleprice}" pattern="#,###" /> 원</span></li> 
				                          <li><label class='prodInfo'>할인율</label><span style="color: blue; font-weight: bold;">[${pvo.discountPercent}%] 할인</span></li> 
				                          <li><label class='prodInfo'>포인트</label><span style="color: orange;">${pvo.point} POINT</span></li> 
				                          <li class='text-center'><a href='/MyMVC/shop/prodView.up?pnum=${pvo.pnum}' class='stretched-link btn btn-outline-dark btn-sm' role='button'>자세히보기</a></li> 
				                              <%-- 카드 내부의 링크에 .stretched-link 클래스를 추가하면 전체 카드를 클릭할 수 있고 호버링할 수 있습니다(카드가 링크 역할을 함). --%>
				                       </ul>
				                    </div>
			                   </div>
		                    </div>
                 	</c:when>
                 	
                 	<c:otherwise>
                 		
			 			<div class = "col-lg-2 ">
			 				<div class="card mb-3">
			                   <img src='/MyMVC/images/${pvo.pimage1}' class='card-img-top' style='width: 100%'/>
			                   <div class='card-body' style='padding: 0; font-size: 9pt;'>
			                     <ul class='list-unstyled mt-2'> 
			                          <li><label class='prodInfo'>스펙명</label>${pvo.spvo.sname}</li>
			                          <li><label class='prodInfo'>제품명</label>${pvo.pname}</li> 
			                          <li><label class='prodInfo'>정가</label><span style="color: red; text-decoration: line-through;"><fmt:formatNumber value="${pvo.price}" pattern="#,###" /> 원</span></li> 
			                          <li><label class='prodInfo'>판매가</label><span style="color: red; font-weight: bold;"><fmt:formatNumber value="${pvo.saleprice}" pattern="#,###" /> 원</span></li> 
			                          <li><label class='prodInfo'>할인율</label><span style="color: blue; font-weight: bold;">[${pvo.discountPercent}%] 할인</span></li> 
			                          <li><label class='prodInfo'>포인트</label><span style="color: orange;">${pvo.point} POINT</span></li> 
			                          <li class='text-center'><a href='/MyMVC/shop/prodView.up?pnum=${pvo.pnum}' class='stretched-link btn btn-outline-dark btn-sm' role='button'>자세히보기</a></li> 
			                              <%-- 카드 내부의 링크에 .stretched-link 클래스를 추가하면 전체 카드를 클릭할 수 있고 호버링할 수 있습니다(카드가 링크 역할을 함). --%>
			                       </ul>
			                    </div>
		                 </div>
		               </div>
		                
                 	
                 	
                 	</c:otherwise>
                 </c:choose>
	 		</c:forEach>
 		</div>
 		
 		
 		<nav class="my-5">
           <div style='display:flex; width:80%;'>
             <ul class="pagination" style='margin:auto;'>${requestScope.pageBar}</ul>
          </div>
       </nav> 
 		
 		
 		
 		
 		
 	</c:if>
 </div> 
 
 	<c:if test="${empty requestScope.productList}">
 		
 		제품진열 준비중 입니다.
 	
 	</c:if>
 
 	




<jsp:include page="../footer2.jsp" />  