<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<jsp:include page="../header2.jsp"/>

<style type="text/css">
	
	label.prodInfo {
		display: inline-block;  /* width 를 주기 위해 */
		width: 65px;
		margin-left: 5px;
	}

</style>


<script type="text/javascript">

	let start = 1;
	
	let lenHIT = 8;
	// HIT 상품 "스크롤"을 할 때 보여줄 상품의 개수(단위)크기

	$(document).ready(function(){
	
		$("span#totalHITCount").hide();
		$("span#countHIT").hide();
		
		// HIT상품 게시물을 더보기 위하여 "스크롤" 이벤트에 대한 초기값 호출하기 
	    // 즉, 맨처음에는 "스크롤" 하지 않더라도 스크롤 한 것 처럼 8개의 HIT상품을 게시해주어야 한다는 말이다. 
		
		displayHIT(start); 
	
		// ===  스크롤 이벤트 발생시키기 시작 === 
		
		$(window).scroll(function() {
			
			// 스크롤탑의 위치값 
		    // console.log( "$(window).scrollTop() => " + $(window).scrollTop() );
			
			// 보여주어야할 문서의 높이값(더보기를 해주므로 append 되어져서 높이가 계속 증가 될것이다)
      		// console.log( "$(document).height() => " + $(document).height() );
			
      	    // 웹브라우저창의 높이값(디바이스마다 다르게 표현되는 고정값) 
            // console.log( "$(window).height() => " + $(window).height() );
			
      	    
      	    
      	    // 아래는 스크롤 되어진 스크롤탑의 위치 값이 웹브라우저창의 높이 만큼 내려갔을 경우를 알아보는 것이다.
			//console.log( "$(window).scrollTop() => " + $(window).scrollTop() );
	        //console.log( "$(document).height() - $(window).height() => " + ( $(document).height() - $(window).height() ) );
      	    
	        
	        // 아래는 만약에 위의 값이 제대로 안나오는 경우 이벤트가 발생되는 숫자를 만들기 위해서 스크롤탑의 위치값에 +1 을 더해서 보정해준 것이다. 
	        //console.log( "$(window).scrollTop() + 1  => " + ( $(window).scrollTop() + 1  ) );
       		//console.log( "$(document).height() - $(window).height() => " + ( $(document).height() - $(window).height() ) ); 
      	    
	        
	        //if( $(window).scrollTop() >= $(document).height() - $(window).height() ) {
				// 만약에 위의 값대로 잘 안되면 아래의 것을 하도록 한다. 
			if( $(window).scrollTop() + 1 >= $(document).height() - $(window).height() ) {
		
		    		
				//alert("기존 문서 내용을 문서내용을 끝까지 받습니다. 이제 새로운 내용을 읽어다가 보여드리겠습니다.");
				
				$("span#totalHITCount").text();
				$("span#countHIT").text();
				
				if($("span#totalHITCount").text() != $("span#countHIT").text() ) {
					
					
					start += lenHIT;
					displayHIT(start);
				}
	
			}
				
			if( $(window).scrollTop() == 0 ) {
				// 다시 처음부터 시작하도록 한다.
				
				$("div#displayHIT").empty();
				$("span#end").empty();
				$("span#countHIT").text("0");
				
				start = 1;
				displayHIT(start);
				
			}
      	
		});	
			
		
		
	}); // end of $(document).ready(function(){ } )

	
	
	
	// function Declaration
	
	
	
	
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
				
				//console.log("확인용 json => " + JSON.stringify(json));    // JSON 객체를 String 타입으로 변경시켜주는 것이다.
				//console.log("확인용 json => " + typeof JSON.stringify(json));
				
				// 확인용 : json => [{"pnum":36,"code":"100000","pname":"노트북30","pcompany":"삼성전자","saleprice":1000000,"discoutPercent":17,"point":60,"pinputdate":"2023-04-19","pimage1":"59.jpg","pqty":100,"pimage2":"60.jpg","pcontent":"30번 노트북","price":1200000,"sname":0},
								//{"pnum":35,"code":"100000","pname":"노트북29","pcompany":"레노버","saleprice":1000000,"discoutPercent":17,"point":60,"pinputdate":"2023-04-19","pimage1":"57.jpg","pqty":100,"pimage2":"58.jpg","pcontent":"29번 노트북","price":1200000,"sname":0},
								//{"pnum":34,"code":"100000","pname":"노트북28","pcompany":"아수스","saleprice":1000000,"discoutPercent":17,"point":60,"pinputdate":"2023-04-19","pimage1":"55.jpg","pqty":100,"pimage2":"56.jpg","pcontent":"28번 노트북","price":1200000,"sname":0},
								//{"pnum":33,"code":"100000","pname":"노트북27","pcompany":"애플","saleprice":1000000,"discoutPercent":17,"point":60,"pinputdate":"2023-04-19","pimage1":"53.jpg","pqty":100,"pimage2":"54.jpg","pcontent":"27번 노트북","price":1200000,"sname":0},
								//{"pnum":32,"code":"100000","pname":"노트북26","pcompany":"MSI","saleprice":1000000,"discoutPercent":17,"point":60,"pinputdate":"2023-04-19","pimage1":"51.jpg","pqty":100,"pimage2":"52.jpg","pcontent":"26번 노트북","price":1200000,"sname":0},
								//{"pnum":31,"code":"100000","pname":"노트북25","pcompany":"삼성전자","saleprice":1000000,"discoutPercent":17,"point":60,"pinputdate":"2023-04-19","pimage1":"49.jpg","pqty":100,"pimage2":"50.jpg","pcontent":"25번 노트북","price":1200000,"sname":0},
								//{"pnum":30,"code":"100000","pname":"노트북24","pcompany":"한성컴퓨터","saleprice":1000000,"discoutPercent":17,"point":60,"pinputdate":"2023-04-19","pimage1":"47.jpg","pqty":100,"pimage2":"48.jpg","pcontent":"24번 노트북","price":1200000,"sname":0},
								//{"pnum":29,"code":"100000","pname":"노트북23","pcompany":"DELL","saleprice":1000000,"discoutPercent":17,"point":60,"pinputdate":"2023-04-19","pimage1":"45.jpg","pqty":100,"pimage2":"46.jpg","pcontent":"23번 노트북","price":1200000,"sname":0}
								//]
							
				// 또는 
				//json => []
				
				
				let html = "";
				
				if(start == "1" && json.length == 0) {  // 배열이기 때문에 json == null 로 하면 절대로 안된다. 
					// 처음부터 데이터가 존재하지 않는 경우 
					// !!!! 주의 !!!! 
					// if(json == null) 이 절대 아니고 if(json.length == 0) 으로 해야함 !! 
					html += "현재 상품 준비중.....";
				
					// HIT 상품 결과물 출력하기 
					$("div#displayHIT").html(html);
				} 
				
				else if (json.length > 0) {
					// 데이터가 존재하는 경우 
					
					// 두가지의 방법이 있다.
					// 첫번째 자바스크립트를 사용하는 경우 
				<%--
					json.forEach(function(item, index, array) {
						
					});
				--%>
					
					// 두번째 jQuery 를 사용하는 경우
					$.each(json, function(index, item) {     //json 은 배열 
						
						html +=  "<div class='col-md-6 col-lg-3'>"+
                        "<div class='card mb-3'>"+
                            "<img src='/MyMVC/images/"+item.pimage1+"' class='card-img-top' style='width: 100%' />"+
                            "<div class='card-body' style='padding: 0; font-size: 11pt;'>"+
                              "<ul class='list-unstyled mt-3 pl-3'>"+
                                 "<li><label class='prodInfo'>제품명</label>"+item.pname+"</li>"+
                                 "<li><label class='prodInfo'>정가</label><span style=\"color: red; text-decoration: line-through;\">"+(item.price).toLocaleString('en')+" 원</span></li>"+
                                   "<li><label class='prodInfo'>판매가</label><span style=\"color: red; font-weight: bold;\">"+(item.saleprice).toLocaleString('en')+" 원</span></li>"+
                                   "<li><label class='prodInfo'>할인율</label><span style=\"color: blue; font-weight: bold;\">["+item.discoutPercent+"%] 할인</span></li>"+
                                   "<li><label class='prodInfo'>포인트</label><span style=\"color: orange;\">"+(item.point).toLocaleString('en')+" POINT</span></li>"+ 
                                   "<li class='text-center'><a href='/MyMVC/shop/prodView.up?pnum="+item.pnum+"' class='stretched-link btn btn-outline-dark btn-sm' role='button'>자세히보기</a></li>"+ 
                                   <%-- 카드 내부의 링크에 .stretched-link 클래스를 추가하면 전체 카드를 클릭할 수 있고 호버링할 수 있습니다(카드가 링크 역할을 함). --%>         
                              "</ul>"+
                            "</div>"+
                         "</div>"+ 
                      "</div>";
                      
					});   // end of $.each(json, function(index, item)  반복문을 뜻함 
						
							
					// HIT 상품 결과물 출력하기 (반복문을 빠져나온후 출력)
					$("div#displayHIT").append(html);		 // append 를 쓰면 기존거 + 새로운거 html 을 쓰면 기존꺼는 없애고 새로운것만 나타남
					
					
					// span#countHIT 에 지금가지 출력된 상품의 개수를 누럭해서 기록한다.
					$("span#countHIT").text( Number($("span#countHIT").text()) + json.length );    /// val() 대신 text 를 사용해야 한다.				
					
					
					// 만약 카운트 hit 의 값이 총 hit 상품 개수보다 크면 
					// 스크롤을 계속해서 countHIT 값과 totalHITCount 값이 일치하는 경우 
					if( Number($("span#totalHITCount").text()) <= Number($("span#countHIT").text()) )  {  // number 는 빼도 그만 안빼도 그만 
						
						$("span#end").html("더이상 조회할 제품이 없습니다.");		// id 가 end 인 span 태그에 추가하겠다.
					}

				} // end of else if 				
			
			},
			// 에러 일때 실행된다.
             error: function(request, status, error){
                 alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
             }
			
		});
		
		
		
			
		
	}// end of function displayHIT() 


</script>


	<%-- === HIT 상품을 모두 가져와서 디스플레이(스크롤 방식 방식으로 페이징 처리한 것) === --%>
   <div>
      <p class="h3 my-3 text-center">- HIT 상품 -</p>
      
       <div class="row" id="displayHIT"></div>
   
      <div>
         <p class="text-center">
            <span id="end" style="display:block; margin:20px; font-size: 14pt; font-weight: bold; color: red;"></span>        
            <span id="totalHITCount">${requestScope.totalHITCount}</span>
            <span id="countHIT">0</span>
         </p>
      </div>
   </div>

<jsp:include page="../footer2.jsp"/>