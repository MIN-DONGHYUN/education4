<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:if test="${not empty requestScope.categotyList}">

	<div style="width: 95%;
               border: solid 1px gray;
               padding-top: 5px;
               padding-bottom: 5px;
               text-align: center;
               color: navy;
               font-size: 14pt;
               font-weight: bold;">
      CATEGORY LIST
   </div>
	
	<div style="width: 95%;
               border: solid 1px gray;
               border-top: hidden;
               padding-top: 5px;
               padding-bottom: 5px;
               text-align: center;">
         <a href="<%= request.getContextPath()%>/shop/mallHome1.up">전체</a>&nbsp;
         
         <%-- 반복문  --%>
         <c:forEach var="map" items="${requestScope.categotyList}">
         
         	<a href="<%= request.getContextPath()%>/shop/mallByCategory.up?cnum=${map.cnum}">${map.cname}</a>&nbsp;	
         
         </c:forEach>
         
   </div>
	
	
</c:if>
 


        
    