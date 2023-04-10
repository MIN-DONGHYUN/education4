<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
    String ctxPath = request.getContextPath();
    //    /MyMVC
%>


<!-- Required meta tags -->
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

<!-- Bootstrap CSS -->
<link rel="stylesheet" type="text/css" href="<%= ctxPath%>/bootstrap-4.6.0-dist/css/bootstrap.min.css" > 

<!-- 직접 만든 CSS -->
<link rel="stylesheet" type="text/css" href="<%= ctxPath%>/css/style.css" />

<!-- Optional JavaScript -->
<script type="text/javascript" src="<%= ctxPath%>/js/jquery-3.6.4.min.js"></script>
<script type="text/javascript" src="<%= ctxPath%>/bootstrap-4.6.0-dist/js/bootstrap.bundle.min.js" ></script>



<script type="text/javascript"> 

	$(document).ready(function(){
		
		// GET방식이나 POST 방식이냐 따져야 한다. 
		
		//const method = ${requestScope.method};
		// 이것은 꽝이다.
		//const method = GET; 말과 같기 때문에 틀린 것이다.
		// 우리는 String 타입이기 때문에
		//const method = "GET"; 를 해야 하기 때문에 아래처럼 써본다.
		
		const method = "${requestScope.method}";   // 이렇게 사용해야만 String 타입을 나타낼 수 있다.
		//console.log("method ==> " + method);
		
		// GET 방식이라면 
		if(method == "GET") {
			$("div#div_findResult").hide();    //숨겨라 라는 말이다.  POST 방식일때는 보여줄 것이다.
		}
		else if(method == "POST") {   		  // POST 방식일때 
			$("input#name").val("${requestScope.name}");		// input 태그의 아이디 name 부분의 값에 idFindAction 에서 가져온 name 을 넣어준다.
			$("input#email").val("${requestScope.email}");
		}
		
		
		// button 의 btnFind 부분을 클릭하면 실행  (찾기버튼)
		$("button#btnFind").click(function() {
			
			// 성명 및 e메일에 대한 유효성 검사 (정규표현식)은 생략하겠습니다.
			
			const frm = document.idFindFrm;
			frm.action = "<%= ctxPath %>/login/idFind.up";    // action 부분
			frm.method = "post";    // 무슨 타입으로 보낼래? get? POST?
			frm.submit();	 	// 보내자 
			
		});// end of $("button#btnFind").click(function() {
		
	}); // end of $(document).ready(function(){

	
		
					// !!!!!! 중요!!!!!!!!! //
	// 아이디 찾기 모달창에 입력한 input 태그 value 값 초기화 시키기
	function func_form_reset_empty() {
		
		document.querySelector("form[name='idFindFrm']").reset();    // 이 함수를 호출하면 form 태그 초기화 
		$("div#div_findResult > p.text-center").empty();    		 // 결과물도 비우자 
		
	} // end of function func_form_reset_empty() {
	
	
		
		
		

</script>





<form name="idFindFrm">

	<ul style="list-style-type: none">
         <li style="margin: 25px 0">
            <label for="userid" style="display: inline-block; width: 90px">성명</label>
            <input type="text" name="name" id="name" size="25" placeholder="홍길동" autocomplete="off" required />
         </li>
         <li style="margin: 25px 0">
            <label for="userid" style="display: inline-block; width: 90px">이메일</label>
            <input type="text" name="email" id="email" size="25" placeholder="abc@def.com" autocomplete="off" required />
         </li>
   </ul>
   
   <div class="my-3">
    <p class="text-center">
       <button type="button" class="btn btn-success" id="btnFind">찾기</button>
    </p>
   </div>
   
</form>


<div class="my-3" id="div_findResult">
      <p class="text-center">
           ID : <span style="color: red; font-size: 16pt; font-weight: bold;">${requestScope.userid}</span> 
      </p>
</div>

    