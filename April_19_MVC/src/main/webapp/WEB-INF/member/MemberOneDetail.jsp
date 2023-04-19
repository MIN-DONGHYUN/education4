<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<jsp:include page="../header.jsp"/>


<style type="text/css">

   div#mvoInfo {
      width: 60%; 
      text-align: left;
      border: solid 0px red;
      margin-top: 30px; 
      font-size: 13pt;
      line-height: 200%;
   }
   
   span.myli {
      display: inline-block;
      width: 90px;
      border: solid 0px blue;
   }
   
/* ============================================= */
   div#sms {
      margin: 0 auto; 
      /* border: solid 1px red; */ 
      overflow: hidden; 
      width: 50%;
      padding: 10px 0 10px 80px;
   }
   
   span#smsTitle {
      display: block;
      font-size: 13pt;
      font-weight: bold;
      margin-bottom: 10px;
   }
   
   textarea#smsContent {
      float: left;
      height: 100px;
   }
   
   button#btnSend {
      float: left;
      border: none;
      width: 50px;
      height: 100px;
      background-color: navy;
      color: white;
   }
   
   div#smsResult {
      clear: both;
      color: red;
      padding: 20px;
   }   

</style>


<script type="text/javascript">

	$(document).ready(function(){
		
		$("div#smsResult").hide();     // 맨 처음에는 감춘다.
		
		$("button#btnSend").click(() => {
			
			//console.log($("input#reservedate").val() + " " + $("input#reservetime").val());  // 문자 보낼 연월일 과 시간 
			// 결과 :  2023-04-07 02:36
			
			// 연 월 일
			let reservedate = $("input#reservedate").val();
			reservedate = reservedate.split("-").join("");		// split을 구분자로 배열이 되어진다.  그 배열을 다시 join으로 합친다. 
			
			// 시간 
			let reservetime = $("input#reservetime").val()
			reservetime = reservetime.split(":").join("");// split을 구분자로 배열이 되어진다.  그 배열을 다시 join으로 합친다. 
			
			
			const datetime = reservedate + reservetime;
			
			//console.log(datetime);
			// 202304070236
			
			let dataObj;
			
			if(reservedate  == "" || reservetime == "" ) {
				// 문자를 바로 보내기인 경우
				dataObj = {"mobile":"${requestScope.mvo.mobile}",
				           "smsContent":$("textarea#smsContent").val()};     //smsSendAction 의 mobile,  smsContent 가져다 줌
				 
				
			}
			else {
				// 예약문자 보내기인 경우 
				dataObj = {"mobile":"${requestScope.mvo.mobile}",
				           "smsContent":$("textarea#smsContent").val(),
				           "datetime":datetime};
				          
			}
			
			
			// 데이터를 서버에 HTTP POST, GET 방식으로 전송, HTML, XML, JSON, TEXT 유형에 데이터를 요청할 수 있는 통합적인 메서드, 
			// $.post(), $.get(), $.getJSON() 메서드의 기능을 합쳐 놓은 것
			
			$.ajax({
				
				
				url : "<%=request.getContextPath()%>/member/smsSend.up",
				type : "post",
				data : dataObj,
				dataType:"json",     // 	서버에서 받아올 데이터의 형식을 지정, 생략하면 요청한 자료에 맞게 자동으로 형식이 설정
				success:function(json){
					//json 은 객체 타입이다. 즉 json 은 자바스크립트의 객체이다. 이것을 화면에 출력해서 확인해본다. 
					// console.log(json);		// console.log(json); 어러면 객체가 나옴 
					
					// console.log(JSON.stringify(json));
					// 결과 : {"group_id":"R2Gd8d6vNq3x1DcX","success_count":1,"error_count":0}
					
					if(json.success_count == 1) { //. 표기법
						$("div#smsResult").html("문자전송이 성공되었습니다.");
					}
					else if(json.error_count != 0) {     // 오류 발생 
						$("div#smsResult").html("문자전송이 실패되었습니다.");					
					}
					
					$("div#smsResult").show();              //  보여준다.
					$("textarea#smsContent").val("");		// 초기화 시킨다.
					
					
				},
				
				 // 에러 일때 실행된다.
                error: function(request, status, error){
                    alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
                }
				
				
			});
			
			
			
			
			
		});// end of $("button#btnSend").click(() => { }
			
		
		
		
		
	}); // end of $(document).ready(function(){}



</script>

<c:if test="${empty mvo}">
   존재하지 않는 회원입니다.<br>
</c:if>

<c:if test="${not empty requestScope.mvo}">								<%-- requestScope. 는 생략 가능함  --%>
   <c:set var="mobile" value="${requestScope.mvo.mobile}" />      
   <c:set var="birthday" value="${requestScope.mvo.birthday}" />
   <h3>::: ${requestScope.mvo.name}님의 회원 상세정보 :::</h3>

   <div id="mvoInfo">
    <ol>   
       <li><span class="myli">아이디 : </span>${mvo.userid}</li>
       <li><span class="myli">회원명 : </span>${mvo.name}</li>
       <li><span class="myli">이메일 : </span>${mvo.email}</li>
       <li><span class="myli">휴대폰 : </span>${fn:substring(mobile, 0, 3)}-${fn:substring(mobile, 3, 7)}-${fn:substring(mobile, 7, 11)}</li>   <%-- 여기서 moblie 은 c:set 에서 설정해둔 moblie 입니다. --%>
       <li><span class="myli">우편번호 : </span>${mvo.postcode}</li>
       <li><span class="myli">주소 : </span>${mvo.address}&nbsp;${mvo.detailaddress}&nbsp;${mvo.extraaddress}</li>
       <li><span class="myli">성별 : </span><c:choose><c:when test="${mvo.gender eq '1'}">남</c:when><c:otherwise>여</c:otherwise></c:choose></li> 
       <li><span class="myli">생년월일 : </span>${fn:substring(birthday, 0, 4)}.${fn:substring(birthday, 4, 6)}.${fn:substring(birthday, 6, 8)}</li> 
       <li><span class="myli">나이 : </span>${mvo.age}세</li> 
       <li><span class="myli">코인액 : </span><fmt:formatNumber value="${mvo.coin}" pattern="###,###" /> 원</li>
       <li><span class="myli">포인트 : </span><fmt:formatNumber value="${mvo.point}" pattern="###,###" /> POINT</li>
       <li><span class="myli">가입일자 : </span>${mvo.registerday}</li>
     </ol>
   </div>
   
   <%-- ==== 휴대폰 SMS(문자) 보내기 ==== --%>
   <div id="sms" align="left">
        <span id="smsTitle">&gt;&gt;휴대폰 SMS(문자) 보내기 내용 입력란&lt;&lt;</span>
        <div style="margin: 10px 0 20px 0">
           발송예약일&nbsp;<input type="date" id="reservedate" />&nbsp;<input type="time" id="reservetime" />
        </div>
        <textarea rows="4" cols="40" id="smsContent"></textarea>
        <button id="btnSend">전송</button>
        <div id="smsResult"></div>
   </div>
   
</c:if>


<div>
   <button style="margin-top: 50px;" type="button" class="btn btn-secondary" onclick="javascript:history.back();">회원목록[history.back()]</button>
   &nbsp;&nbsp;
   <button style="margin-top: 50px;" type="button" class="btn btn-secondary" onclick="goMemberList()">회원목록[검색된결과]</button>
   &nbsp;&nbsp;
   <button style="margin-top: 50px;" type="button" class="btn btn-secondary" onclick="javascript:location.href='memberList.up'">회원목록[처음으로]</button> 
</div>



<jsp:include page="../footer.jsp"/>