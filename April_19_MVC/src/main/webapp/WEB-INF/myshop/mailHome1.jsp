<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<jsp:include page="../header2.jsp"/>

<style type="text/css">


</style>


<script type="text/javascript">

	$(document).ready(function(){
	
		// HIT상품 게시물을 더보기 위하여 "더보기..." 버튼 클릭액션에 대한 초기값 호출하기 
	    // 즉, 맨처음에는 "더보기..." 버튼을 클릭하지 않더라도 클릭한 것 처럼 8개의 HIT상품을 게시해주어야 한다는 말이다. 
		
		displayHIT(1);  // 문서가 로딩 되자마자 한번 함수 호출 (처음에는 더보기 안눌러도 나와야 하므로 처음에는 1로 설정  )
	
		
		// HIT 상품 게시물을 더보기 위하여 "더보기..." 버튼 클릭액션 이벤트 등록하기 
		$("button#btnMoreHIT").click(function() {
			
			if($(this).text() == "처음으로") { // $(this) 는 여기서 button 을 말함 
				
				$("span#end").empty();    // 비운다 				
				displayHIT(1);            // 처음부터 다시 가기 위해 비운 후 함수 다시 실행 
				$(this).text("더보기...");
			}
			else {
				displayHIT($(this).val());  // 함수 호출 this로 가져와 val() 내용을 가져온다. 
			}
			
			
			
		}); // end of $(button#btnMoreHIT).click(function() { } )
		
		
		
	}); // end of $(document).ready(function(){ } )

	
	
	
	// function Declaration
	
	let lenHIT = 8;
	// HIT 상품 "더보기..." 버튼을 클릭할때 보여줄 상품의 개수(단위)크기
	
	
	// display 할  HIT상품 정보를 추가 요청하기(Ajax 로 처리함)
	function displayHIT(start) {    // 1 ~ 8번 // 9 ~ 16번  // 17 ~ 24번 이런 순으로 나와야 한다.
									// start 는 1 9 17 등등이 되어질 것이다.
		
		$.ajax({
			url:"<%= request.getContextPath()%>/shop/mailDisplayJSON.up",
			type:"get",          // 생략 가능함
			data:{"sname":"HIT"
				 ,"start":start		// "1" , "9", "17" , "25" , "33" ... 
				 ,"len":lenHIT},		//  8     8    8      8      8 
			dataType:"json",
			async:true,			// 비동기 방식 (생략 가능)
			success:function(json) {
				/*
					console.log("확인용 json => " + json);
					console.log("json 의 타입 => " + typeof json);
				*/
				console.log("확인용 json => " + JSON.stringify(json));    // JSON 객체를 String 타입으로 변경시켜주는 것이다.
				console.log("확인용 json => " + typeof JSON.stringify(json));
				// 확인용 : json => [{"pnum":36,"code":"100000","pname":"노트북30","pcompany":"삼성전자","saleprice":1000000,"discoutPercent":17,"point":60,"pinputdate":"2023-04-19","pimage1":"59.jpg","pqty":100,"pimage2":"60.jpg","pcontent":"30번 노트북","price":1200000,"sname":0},{"pnum":35,"code":"100000","pname":"노트북29","pcompany":"레노버","saleprice":1000000,"discoutPercent":17,"point":60,"pinputdate":"2023-04-19","pimage1":"57.jpg","pqty":100,"pimage2":"58.jpg","pcontent":"29번 노트북","price":1200000,"sname":0},{"pnum":34,"code":"100000","pname":"노트북28","pcompany":"아수스","saleprice":1000000,"discoutPercent":17,"point":60,"pinputdate":"2023-04-19","pimage1":"55.jpg","pqty":100,"pimage2":"56.jpg","pcontent":"28번 노트북","price":1200000,"sname":0},{"pnum":33,"code":"100000","pname":"노트북27","pcompany":"애플","saleprice":1000000,"discoutPercent":17,"point":60,"pinputdate":"2023-04-19","pimage1":"53.jpg","pqty":100,"pimage2":"54.jpg","pcontent":"27번 노트북","price":1200000,"sname":0},{"pnum":32,"code":"100000","pname":"노트북26","pcompany":"MSI","saleprice":1000000,"discoutPercent":17,"point":60,"pinputdate":"2023-04-19","pimage1":"51.jpg","pqty":100,"pimage2":"52.jpg","pcontent":"26번 노트북","price":1200000,"sname":0},{"pnum":31,"code":"100000","pname":"노트북25","pcompany":"삼성전자","saleprice":1000000,"discoutPercent":17,"point":60,"pinputdate":"2023-04-19","pimage1":"49.jpg","pqty":100,"pimage2":"50.jpg","pcontent":"25번 노트북","price":1200000,"sname":0},{"pnum":30,"code":"100000","pname":"노트북24","pcompany":"한성컴퓨터","saleprice":1000000,"discoutPercent":17,"point":60,"pinputdate":"2023-04-19","pimage1":"47.jpg","pqty":100,"pimage2":"48.jpg","pcontent":"24번 노트북","price":1200000,"sname":0},{"pnum":29,"code":"100000","pname":"노트북23","pcompany":"DELL","saleprice":1000000,"discoutPercent":17,"point":60,"pinputdate":"2023-04-19","pimage1":"45.jpg","pqty":100,"pimage2":"46.jpg","pcontent":"23번 노트북","price":1200000,"sname":0}]
			},
			// 에러 일때 실행된다.
             error: function(request, status, error){
                 alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
             }
			
		});
		
		
		
	
		/// >>> 중요 <<< /// 더보기... 버튼의 value 속성에 값을 지정하기 
		$("button#btnMoreHIT").val( Number(start) + lenHIT );
		// start가 1   이라면  1~8     까지 상품 8개를 보여준다.
        // start가 9   이라면  9~16   까지 상품 8개를 보여준다.
        // start가 17 이라면  17~24 까지 상품 8개를 보여준다.
        // start가 25 이라면  25~32 까지 상품 8개를 보여준다.
        // start가 33 이라면  33~36 까지 상품 4개를 보여준다.(마지막 상품)  
		
		// span#countHIT 에 지금가지 출력된 상품의 개수를 누럭해서 기록한다.
		$("span#countHIT").text( Number($("span#countHIT").text()) + 8 );    /// val() 대신 text 를 사용해야 한다.				
		
		
		// 만약 카운트 hit 의 값이 총 hit 상품 개수보다 크면 
		// 더보기... 버튼을 계속해서 클릭하여 countHIT 값과 totalHITCount 값이 일치하는 경우 
		if( Number($("span#totalHITCount").text()) <= Number($("span#countHIT").text()) )  {
			
			$("span#end").html("더이상 조회할 제품이 없습니다.");		// id 가 end 인 span 태그에 추가하겠다.
			$("button#btnMoreHIT").text("처음으로");
			$("span#countHIT").text(0);
		}
		
		
	}// end of function displayHIT() 


</script>


	<%-- === HIT 상품을 모두 가져와서 디스플레이(더보기 방식으로 페이징 처리한 것) === --%>
   <div>
      <p class="h3 my-3 text-center">- HIT 상품 -</p>
      
       <div class="row" id="displayHIT"></div>
   
      <div>
         <p class="text-center">
            <span id="end" style="display:block; margin:20px; font-size: 14pt; font-weight: bold; color: red;"></span> 
            <button type="button" class="btn btn-secondary btn-lg" id="btnMoreHIT" value="">더보기...</button>   <%-- value 값이 초기에는 없었는데 값을 집어넣어주면  --%>
            <span id="totalHITCount">${requestScope.totalHITCount}</span>
            <span id="countHIT">0</span>
         </p>
      </div>
   </div>



<jsp:include page="../footer2.jsp"/>