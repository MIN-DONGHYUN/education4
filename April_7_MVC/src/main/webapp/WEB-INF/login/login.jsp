<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<style type="text/css">

	table#loginTbl {
      width: 95%;
      border: solid 1px gray;
      border-collapse: collapse;
      margin-top: 20px;
   }
   
   table#loginTbl #th {
      background-color: silver;
      font-size: 14pt;
      text-align: center;
      height: 30px;
   }
   
   table#loginTbl td {
      border: solid 1px gray;
      line-height: 30px;
   }




</style>


<script type="text/javascript">

	$(document).ready(function() {
		
		// 클릭했을때
		$("button#btnSubmit").click(function() {
			
			//alert("로그인 하러 간다.");
			goLogin();   // 로그인 시도한다.
			
		});// end of $("button#btnSubmit").click(function() {
		
		// 엔터를 눌렀을때 
		$("input#loginPwd").bind("keydown", function(e) {
			
			// 암호입력란에 엔터를 했을 경우
			if(e.keyCode == 13)        // e.keyCode 라고 Code 에 대문자를 써야 한다.
			{							
				//alert("로그인 하러 간다.");
				goLogin();   // 로그인 시도한다.
			}
			
		});// end of $("button#loginPwd").bind("keydown", function() {
		
			
		//function gologin() {
			
		function goLogin() {
			// alert("로그인 시도함");		
			
			const loginUserid = $("input#loginUserid").val().trim();    // 값을 가져온다.
			const loginPwd = $("input#loginPwd").val().trim(); 
			
			if(loginUserid == ""){					// 로그인 아이디가 없으면 
				alert("아이디를 입력하세요!!");
				$("input#loginUserid").val("");		// 공백
				$("input#loginUserid").focus();     // focus 준다.
				return;   // goLogin 함수 종료 
			}
			
			if(loginPwd == ""){					// 로그인 암호가 없으면 
				alert("암호를 입력하세요!!");
				$("input#loginPwd").val("");		// 공백
				$("input#loginPwd").focus();     // focus 준다.
				return;   // goLogin 함수 종료 
			}
			
			const frm = document.loginFrm;
			frm.action = "<%= request.getContextPath() %>/login/login.up";
			frm.method = "post";
			frm.submit();
			
		}	// end of function goLogin() {
		
		
		
		
		
	});

</script>



<%-- *** 로그인을 하기 위한 폼을 생성 --%>

<%-- <c:if test="${sessionScope.loginuser == null}">			jsp에서 session 영역에 넣는다
	로그인 폼태그 생성
</c:if> 

	또는 

--%>

<c:if test="${ empty sessionScope.loginuser}">			<!-- jsp에서 session 영역에 넣는다 -->
	
	<form name="loginFrm">
       <table id="loginTbl">
         <thead>
            <tr>
               <th colspan="2" id="th">LOGIN</th>
            </tr>
         </thead>
         
         <tbody>
            <tr>
               <td style="width: 20%; border-bottom: hidden; border-right: hidden; padding: 10px;">ID</td>
               <td style="width: 80%; border-bottom: hidden; border-left: hidden; padding: 10px;"><input type="text" id="loginUserid" name="userid" size="20" class="box" autocomplete="off" /></td>
            </tr>   
            <tr>
               <td style="width: 20%; border-top: hidden; border-bottom: hidden; border-right: hidden; padding: 10px;">암호</td>
               <td style="width: 80%; border-top: hidden; border-bottom: hidden; border-left: hidden; padding: 10px;"><input type="password" id="loginPwd" name="pwd" size="20" class="box" /></td>
            </tr>
            
            <%-- === 아이디 찾기, 비밀번호 찾기 === --%>
            <tr>
               <td colspan="2" align="center">
                  <a style="cursor: pointer;" data-toggle="modal" data-target="#userIdfind" data-dismiss="modal">아이디찾기</a> /
                  <a style="cursor: pointer;" data-toggle="modal" data-target="#passwdFind" data-dismiss="modal" data-backdrop="static">비밀번호찾기</a>
               </td>
            </tr>
            
            <tr>
               <td colspan="2" align="center" style="padding: 10px;">
                  <input type="checkbox" id="saveid" name="saveid" /><label for="saveid">아이디저장</label>
               <%--    
                  <button type="button" id="btnSubmit" style="width: 67px; height: 27px; background-image: url('<%= request.getContextPath()%>/images/login.png'); vertical-align: middle; border: none;"></button>
               --%>
                   <button type="button" id="btnSubmit" class="btn btn-primary btn-sm ml-5">로그인</button>
               </td>
            </tr>
         </tbody>
       </table>
   </form>
	
	
</c:if> 





<%-- *** 로그인되어진 화면 --%>
<%-- 
<c:if test="${sessionScope.loginuser != null}">			jsp에서 session 영역에 넣는다
	로그인 되어진 사용자 정보 출력
</c:if> 

	또는 

--%>

<c:if test="${ not empty sessionScope.loginuser}">			<!-- jsp에서 session 영역에 넣는다 -->
	<div>
		${sessionScope.loginuser.name} 님으로 로그인중...   
	</div>
</c:if> 


