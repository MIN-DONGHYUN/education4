<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="../header2.jsp" />
   
<style type="text/css" >
   table#tblCartList {width: 90%;
                      border: solid gray 1px;
                      margin-top: 20px;
                      margin-left: 10px;
                      margin-bottom: 20px;}
                      
   table#tblCartList th {border: solid gray 1px;}
   table#tblCartList td {border: dotted gray 1px;} 
     
     
   /* -- CSS 로딩화면 구현 시작(bootstrap 에서 가져옴) -- */    
   div.loader {
     border: 16px solid #f3f3f3;
     border-radius: 50%;
     border-top: 12px dotted blue;
     border-right: 12px dotted green; 
     border-bottom: 12px dotted red; 
     border-left: 12px dotted pink; 
     width: 120px;
     height: 120px;
     -webkit-animation: spin 2s linear infinite;
     animation: spin 2s linear infinite;
   }
   
   @-webkit-keyframes spin {
     0% { -webkit-transform: rotate(0deg); }
     100% { -webkit-transform: rotate(360deg); }
   }
   
   @keyframes spin {
     0% { transform: rotate(0deg); }
     100% { transform: rotate(360deg); }
   }
/* -- CSS 로딩화면 구현 끝(bootstrap 에서 가져옴) -- */  
     
     
</style>

<script type="text/javascript">

	$(document).ready(function() {
      
	  $("div.loader").hide();   // css 로딩화면 감추기  
	   
	  $("p#order_error_msg").css('display' , 'none');   // 코인 잔액 부족시 주문이 안된다는 표시를 해주는 곳.
	   
      $(".spinner").spinner({
         spin: function(event, ui) {
            if(ui.value > 100) {
               $(this).spinner("value", 100);
               return false;
            }
            else if(ui.value < 0) {
               $(this).spinner("value", 0);
               return false;
            }
         }
      });// end of $(".spinner").spinner({});-----------------
      
            
      // 제품번호의 모든 체크박스가 체크가 되었다가 그 중 하나만 이라도 체크를 해제하면 전체선택 체크박스에도 체크를 해제하도록 한다.
      $(".chkboxpnum").click(function(){
         
         var bFlag = false;
         $(".chkboxpnum").each(function(){
            var bChecked = $(this).prop("checked");
            if(!bChecked) {
               $("#allCheckOrNone").prop("checked",false);
               bFlag = true;
               return false;
            }
         });
         
         if(!bFlag) {
            $("#allCheckOrNone").prop("checked",true);
         }
         
      });
      
   }); // end of $(document).ready()--------------------------
   
   
   // Function declaration
   
   function allCheckBox() {
   
      var bool = $("#allCheckOrNone").is(":checked");
      /*
         $("#allCheckOrNone").is(":checked"); 은
           선택자 $("#allCheckOrNone") 이 체크되어지면 true를 나타내고,
           선택자 $("#allCheckOrNone") 이 체크가 해제되어지면 false를 나타내어주는 것이다.
      */
      
      $(".chkboxpnum").prop("checked", bool);
   }// end of function allCheckBox()-------------------------
   
   
   // === 장바구니 현재주문수량 수정하기 === // 
   function goOqtyEdit(btn_obj) {
	   
	   const index = $("button.updateBtn").index(btn_obj);   // index 값을 알아와서 저장 한다.
	   
	   //alert(index);   // 0,  1,   2 밑으로 내려갈수록 순서대로 나온다.
	
	   const cartno = $("input.cartno").eq(index).val();   // 여러개중에 한개만꺼내오는건 eq   이것은 장바구니의 번호를 알아온 것이다.
	   	   
	   const oqty = $("input.oqty").eq(index).val();   // 주문 개수를 알아오개 한다. 
	   
	   const reqExp =  /^[0-9]+$/g;      // 숫자만 체크하는 정규표현식
	   const bool = reqExp.test(oqty);   // boolean 값으로 나오게 된다.
	   
	   if(!bool) {   //  정규표현식 검사 
		   alert("수정하시려는 수량은 0개 이상이여야 합니다.");
		   location.href="javascript:history.go(0)";		// 자기페이지를 새로고침 한다.
		   return;   // 종료
	   }
	   
	   // alert("장바구니번호 " + cartno + "을 " + oqty + "개로 수정합니다.");
	   
	   // 제품 수량 을 0개로 되었다면 
	   if(oqty == 0) {
		   goDel(cartno);
	   }
	   else {
		// 데이터 베이스에서 다시 새로운 .up 을 불러와야 하므로 ajax를 사용해야 한다. 
         $.ajax({								/* cartDel.up 으로 이동 */
            url:"<%= request.getContextPath()%>/shop/cartEdit.up",
            type:"POST",
            data:{"cartno":cartno,
            	  "oqty":oqty},
            dataType:"JSON",
            success:function(json){     // json 이 확실히 1이라면 
               
               // 장바구니 보기페이지 가야한다.               
               if(json.n == 1) { 
            	   alert("주문수량이 변경되었습니다.");
                  location.href = "cartList.up"; // 장바구니 보기는 페이징처리를 안함.  (업데이트 되어진 정보를 다시 보기 )
               }
            },
            error: function(request, status, error){
               alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
            }
         });
	   }
	   
	   
	   
	   
   <%--   
      var index = $("button.updateBtn").index(obj);
   //   alert(index);
      
      var cartno = $("input.cartno").eq(index).val();
    //   alert(cartno);   
      
      var oqty = $("input.oqty").eq(index).val();
   //   alert(oqty);
      
      var regExp = /^[0-9]+$/g; // 숫자만 체크하는 정규표현식
      var bool = regExp.test(oqty);
   
      if(!bool) {
         alert("수정하시려는 수량은 0개 이상이어야 합니다.");
         location.href="javascript:history.go(0);";
         return;
      }
            
   //  alert("장바구니번호 : " + cartno + "\n주문량 : " + oqty);
      
      if(oqty == "0") {
         goDel(cartno);
      }
      else {
         $.ajax({
               url:"<%= request.getContextPath()%>/shop/cartEdit.up",
               type:"POST",
               data:{"cartno":cartno,
                    "oqty":oqty},
               dataType:"JSON",
               success:function(json){
                  if(json.n == 1) {
                     alert("주문수량이 변경되었습니다.");   
                     location.href = "cartList.up"; 
                  }
               },
               error: function(request, status, error){
                  alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
               }
            });
      } --%>
   
   }// end of function goOqtyEdit(obj)-----------------
   
   
   // === 장바구니에서 특정 제품을 비우기 === //  
   function goDel(cartno) {
      
	   // 제품명 알아오기 
	  const pname = $(event.target).parent().parent().find("span.cart_pname").text();  // 여기서는 삭제 버튼을 말한다/
	  
      const bool = confirm(pname + "을(를) 장바구니에서 제거하시는 것이 맞습니까?");    // 확인창을 띄운다.
      
      if(bool) {
          
    	// 데이터 베이스에서 다시 새로운 .up 을 불러와야 하므로 ajax를 사용해야 한다. 
         $.ajax({								/* cartDel.up 으로 이동 */
            url:"<%= request.getContextPath()%>/shop/cartDel.up",
            type:"POST",
            data:{"cartno":cartno},
            dataType:"JSON",
            success:function(json){     // json 이 확실히 1이라면 
               
               //console.log("확인용입니다 하하하하하하 하기싫다 => "+ JSON.stringify(json));  // JSON.stringify(json) 는 문자열로 만드는 것이다.
               
               // 장바구니 보기페이지 가야한다.               
               if(json.n == 1) { 
                  location.href = "cartList.up"; // 장바구니 보기는 페이징처리를 안함.
               }
            },
            error: function(request, status, error){
               alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
            }
         });
      }
      else {
         alert("장바구니에서 "+pname+" 제품 삭제를 취소하셨습니다.");
      }
      
   }// end of function goDel(cartno)---------------------------
   
   
   // === 장바구니에서 제품 주문하기 === // 
   function goOrder() {
       
	   /// === 체크박스의 체크된 갯수(checked 속성 이용) == //// 
	   const checked_cnt = $("input:checkbox[name='pnum']:checked").length;    // 테크되어진 개수가 몇개인지 알아오는 것 
	   
	   // 체크되어진것이 아무것도 없다면 
	   if(checked_cnt < 1) {
		   alert("주문하실 제품을 선택하세요!!");
		   return;   // 종료
	   } 
	   else {   // 1개라도 체크되었다면 
		   
		   // 데이터 베이스를 건들기 위해 ajax 를 사용하자
		   
		   // === 체크박스에서 체크된 value 값 (checked 속성 이용) === //
		   // === 체크가 된 것만 값을 읽어와서 배열에 넣어준다. === //
		   
		   const all_cnt = $("input:checkbox[name='pnum']").length;   // 이것은 모든 체크박스를 말한다.
		   
		   const pnum_arr = new Array();  // 또는 const pnum_arr = [];  // 제품 번호만 담는 것을 설정
		   const oqty_arr = new Array();  // 또는 const oqty_arr = []; 
		   const cartno_arr = new Array();  // 또는 const cartno_arr = []; 
		   const totalPrice_arr = new Array();  // 또는 const totalPrice_arr = []; 
		   const totalPoint_arr = new Array();  // 또는 const totalPoint_arr = []; 
		   
		   // forEach 를 사용하면 더 쉽다.
		   for(let i = 0; i<all_cnt; i++ ) {
			   
			   if($("input:checkbox[name='pnum']").eq(i).prop("checked") ) { // 체크가 되어있으면 true, 아니면 false 가 되어진다.  
			   		//alert("제품번호 : " + $("input:checkbox[name='pnum']").eq(i).val() );
			   		//alert("주문량 : " + $("input.oqty").eq(i).val() );     // 주문량의 각각의 값을 알아온다.
			   		//alert("삭제해야할 장바구니 번호 : " + $("input.cartno").eq(i).val() );  // 삭제해야할 장바구니 번호
			   		//alert("주문한 제품의 개수에 따른 가격 합계 : " + $("input.totalPrice").eq(i).val() );
			   		//alert("주문한 제품의 개수에 따른 포인트 합계 : " + $("input.totalPoint").eq(i).val());
			   		
			   		// 배열에 넣는다.
			   		pnum_arr.push(  $("input:checkbox[name='pnum']").eq(i).val() );
			   		oqty_arr.push(  $("input.oqty").eq(i).val()  );
			   		cartno_arr.push(  $("input.cartno").eq(i).val() );
			   		totalPrice_arr.push(  $("input.totalPrice").eq(i).val() );
			   		totalPoint_arr.push(  $("input.totalPoint").eq(i).val() );
			   		
			   }
		   }// end of for 
		   
		   // join 사용하기 배열속에 다 담아온것을 합치는 것이다. 
		   const pnum_join = pnum_arr.join();
		   const oqty_join = oqty_arr.join();
		   const cartno_join = cartno_arr.join();
		   const totalPrice_join = totalPrice_arr.join();
		   const totalPoint_join = totalPoint_arr.join();
		   
		   
		   let sum_totalPrice = 0;
		   
		   for(let i =0; i < totalPrice_arr.length; i++) {
			   sum_totalPrice += Number(totalPrice_arr[i]);
		   }
		   
		   
		   let sum_totalPoint = 0;
		   
		   for(let i =0; i < totalPoint_arr.length; i++) {
			   sum_totalPoint += Number(totalPoint_arr[i]);
		   }
		   
		   <%--
		   console.log("!!! 확인용 pnum_join  " + pnum_join);    
		   console.log("!!! 확인용 oqty_join  " + oqty_join);
		   console.log("!!! 확인용 cartno_join  " + cartno_join);
		   console.log("!!! 확인용 totalPrice_join  " + totalPrice_join);
		   console.log("!!! 확인용 totalPoint_join  " + totalPoint_join);
		   console.log("!!! 확인용 sum_totalPrice  " + sum_totalPrice);
		   console.log("!!! 확인용 sum_totalPoint  " + sum_totalPoint);
		   
			   	!!! 확인용 pnum_join  6,5
				!!! 확인용 oqty_join  2,10
				!!! 확인용 cartno_join  8,2
				!!! 확인용 totalPrice_join  240000,330000
				!!! 확인용 totalPoint_join  120,200
				!!! 확인용 sum_totalPrice  0240000330000
				!!! 확인용 sum_totalPoint  0120200
		   --%>
		   
		   const current_coin = ${sessionScope.loginuser.coin};  // 현재 사람의 코인 
		   
		   if( sum_totalPrice > current_coin) {
			   
			   $("p#order_error_msg").html("코인잔액이 부족하므로 주문이 불가합니다 <br>주문 총액 : " + sum_totalPrice.toLocaleString('en') + " 원 / 코인잔액 : " + current_coin.toLocaleString('en') + "원").css('display' , '');
		   	   return;
		   }
		   else {
			   
			   $("p#order_error_msg").css('display' , 'none');
			   
			   if( confirm("총주문액 : "+ sum_totalPrice.toLocaleString('en') + "원 결제하시겠습니까?")) {
					   $("div.loader").show();   // css 로딩화면 보여주기 
					   
					    
					   // ajax  데이터 베이스 insert 할것이다.
					  $.ajax({
						   url:"<%= request.getContextPath()%>/shop/orderAdd.up",
						   type:"post",
						   data:{"sum_totalPrice":sum_totalPrice, 
							     "sum_totalPoint":sum_totalPoint,
							     "pnum_join":pnum_join,
							     "oqty_join":oqty_join,
							     "totalPrice_join":totalPrice_join,
							     "cartno_join":cartno_join
							     },
						   dataType:"json",
						   success:function(json){     // json 이 확실히 1이라면 
							   
							   // {"isSuccess":1} 또는 {"isSuccess":0}  앞에꺼는 성공 뒤에는 실패 
							   
							   if(json.isSuccess == 1) {
								   location.href = "<%= request.getContextPath()%>/shop/orderList.up";
							   }
							   else {
								   location.href = "<%= request.getContextPath()%>/shop/orderError.up";
							   }
							   
				               
				            },
				            error: function(request, status, error){
		                        alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
		                    }
					   }); 
					   
					   
					   
			   }
		   }
		   
		   
		   
		   
	   }
	   
	   
	   
	   
	   
	   
	   
      <%-- ///// == 체크박스의 체크된 갯수(checked 속성이용) == /////
       var checkCnt = $("input:checkbox[name=pnum]:checked").length;
       
      if(checkCnt < 1) {
          alert("주문하실 제품을 선택하세요!!");
          return; // 종료 
       }
       
       else {
            //// == 체크박스에서 체크된 value값(checked 속성이용) == ////
            ///  == 체크가 된 것만 값을 읽어와서 배열에 넣어준다. /// 
              var allCnt = $("input:checkbox[name=pnum]").length;
            
               var pnumArr = new Array();
            var oqtyArr = new Array();
            var cartnoArr = new Array();
            var totalPriceArr = new Array();
            var totalPointArr = new Array();
             
             for(var i=0; i<allCnt; i++) {
                if( $("input:checkbox[name=pnum]").eq(i).is(":checked") ) {
                  pnumArr.push( $("input:checkbox[name=pnum]").eq(i).val() );
                  oqtyArr.push( $(".oqty").eq(i).val() );
                  cartnoArr.push( $(".cartno").eq(i).val() );
                  totalPriceArr.push( $(".totalPrice").eq(i).val() );
                  totalPointArr.push( $(".totalPoint").eq(i).val() );
               }
            }// end of for---------------------------
               
            for(var i=0; i<checkCnt; i++) {
               console.log("확인용 제품번호: " + pnumArr[i] + ", 주문량: " + oqtyArr[i] + ", 장바구니번호 : " + cartnoArr[i] + ", 주문금액: " + totalPriceArr[i] + ", 포인트: " + totalPointArr[i]);
            /*
                    확인용 제품번호: 3, 주문량: 3, 장바구니번호 : 14, 주문금액: 30000, 포인트: 15
                    확인용 제품번호: 56, 주문량: 2, 장바구니번호: 13, 주문금액: 2000000, 포인트 : 120 
                    확인용 제품번호: 59, 주문량: 3, 장바구니번호: 11, 주문금액: 30000, 포인트 : 300    
            */
            }// end of for---------------------------
            
            var pnumjoin = pnumArr.join();
            var oqtyjoin = oqtyArr.join();
            var cartnojoin = cartnoArr.join();
            var totalPricejoin = totalPriceArr.join();

            var sumtotalPrice = 0;
            for(var i=0; i<totalPriceArr.length; i++) {
               sumtotalPrice += parseInt(totalPriceArr[i]);
            }

            var sumtotalPoint = 0;
            for(var i=0; i<totalPointArr.length; i++) {
               sumtotalPoint += parseInt(totalPointArr[i]);
            }
            
            console.log("확인용 pnumjoin : " + pnumjoin);             // 확인용 pnumjoin : 3,56,59
            console.log("확인용 oqtyjoin : " + oqtyjoin);             // 확인용 oqtyjoin : 3,2,3
            console.log("확인용 cartnojoin : " + cartnojoin);         // 확인용 cartnojoin : 14,13,11
            console.log("확인용 totalPricejoin : " + totalPricejoin); // 확인용 totalPricejoin : 30000,2000000,30000
            console.log("확인용 sumtotalPrice : " + sumtotalPrice);   // 확인용 sumtotalPrice : 2060000
            console.log("확인용 sumtotalPoint : " + sumtotalPoint);   // 확인용 sumtotalPoint : 435
            
                var currentcoin = ${sessionScope.loginuser.coin};
                
                var str_sumtotalPrice = sumtotalPrice.toLocaleString('en'); // 자바스크립트에서 숫자 3자리마다 콤마 찍어주기  
             var str_currentcoin = currentcoin.toLocaleString('en');     // 자바스크립트에서 숫자 3자리마다 콤마 찍어주기 
             
            if( sumtotalPrice > currentcoin ) {
                $("p#order_error_msg").html("코인잔액이 부족하므로 주문이 불가합니다.<br>주문총액 : "+str_sumtotalPrice+"원 / 코인잔액 : "+str_currentcoin+"원"); 
                return;
            }
            
            else {
               
               var bool = confirm("총주문액 : "+str_sumtotalPrice+"원 결제하시겠습니까?");
               
               if(bool) {
               
                  $.ajax({
                     url:"<%= request.getContextPath()%>/shop/orderAdd.up",
                     type:"POST",
                     data:{"pnumjoin":pnumjoin,
                          "oqtyjoin":oqtyjoin, 
                          "cartnojoin":cartnojoin,
                          "totalPricejoin":totalPricejoin,
                          "sumtotalPrice":sumtotalPrice,
                          "sumtotalPoint":sumtotalPoint},
                     dataType:"JSON",     
                     success:function(json){
                        if(json.isSuccess == 1) {
                           location.href="<%= request.getContextPath()%>/shop/orderList.up";
                        }
                        else {
                           location.href="<%= request.getContextPath()%>/shop/orderError.up";
                        }
                     },
                     error: function(request, status, error){
                        alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
                     }
                  });
               }
            }   
       
       } 
       --%>
      
   }// end of function goOrder()----------------------
   
</script>

<div class="container-fluid" style="border: solid 0px red">
   <div class="my-3">
      <p class="h4 text-center">&raquo;&nbsp;&nbsp;${(sessionScope.loginuser).name} [${(sessionScope.loginuser).userid}]님 장바구니 목록&nbsp;&nbsp;&laquo;</p>
   </div>
   <div>
       <table id="tblCartList" class="mx-auto" style="width: 90%">
         <thead>
            <tr>
             <th style="border-right-style: none;">
                 <input type="checkbox" id="allCheckOrNone" onClick="allCheckBox();" />
                 <span style="font-size: 10pt;"><label for="allCheckOrNone">전체선택</label></span>
             </th>
             <th colspan="5" style="border-left-style: none; font-size: 12pt; text-align: center;">
                 <marquee>주문을 하시려면 먼저 제품번호를 선택하신 후 [주문하기] 를 클릭하세요</marquee>
             </th>
            </tr>
         
            <tr style="background-color: #cfcfcf;">
              <th style="width:10%; text-align: center; height: 30px;">제품번호</th>
              <th style="width:23%; text-align: center;">제품명</th>
                 <th style="width:17%; text-align: center;">현재주문수량</th>
                 <th style="width:20%; text-align: center;">판매가/포인트(개당)</th>
                 <th style="width:20%; text-align: center;">주문총액/총포인트</th>
                 <th style="width:10%; text-align: center;">비우기</th>
            </tr>   
         </thead>
       
         <tbody>
            <c:if test="${empty requestScope.cartList}">
               <tr>
                    <td colspan="6" align="center">
                      <span style="color: red; font-weight: bold;">
                         장바구니에 담긴 상품이 없습니다.
                      </span>
                    </td>   
               </tr>
            </c:if>   
            
            <c:if test="${not empty requestScope.cartList}">
               <c:forEach var="cartvo" items="${requestScope.cartList}" varStatus="status"> 
                   <tr>
                        <td> <%-- 체크박스 및 제품번호 --%>
                             <%-- c:forEach 에서 선택자 id를 고유하게 사용하는 방법  
                                  varStatus="status" 을 이용하면 된다.
                                  status.index 은 0 부터 시작하고,
                                  status.count 는 1 부터 시작한다. 
                             --%> 
                             <input type="checkbox" name="pnum" class="chkboxpnum" id="pnum${status.index}" value="${cartvo.pnum}" /> &nbsp;<label for="pnum${status.index}">${cartvo.pnum}</label>   
                        </td>
                        <td align="center"> <%-- 제품이미지1 및 제품명 --%> 
                           <a href="/MyMVC/shop/prodView.up?pnum=${cartvo.pnum}">
                              <img src="/MyMVC/images/${cartvo.prod.pimage1}" class="img-thumbnail" width="130px" height="100px" />
                           </a> 
                           <br/><span class="cart_pname">${cartvo.prod.pname}</span> 
                        </td>
                        <td align="center"> 
                            <%-- 현재주문수량 --%>
                              <input class="spinner oqty" name="oqty" value="${cartvo.oqty}" style="width: 30px; height: 20px;">개
                              <button type ="button" class="btn btn-outline-secondary btn-sm updateBtn" type="button" onclick="goOqtyEdit(this)">수정</button>
                              
                              <%-- 장바구니 테이블에서 특정제품의 현재주문수량을 변경하여 적용하려면 먼저 장바구니번호(시퀀스)를 알아야 한다 --%>
                              <input type="hidden" class="cartno" value="${cartvo.cartno}" /> 
                        </td>
                        <td align="right"> <%-- 판매가/포인트(개당) --%> 
                            <fmt:formatNumber value="${cartvo.prod.saleprice}" pattern="###,###" /> 원<br>
                            <fmt:formatNumber value="${cartvo.prod.point}" pattern="###,###" /> POINT
                        </td>
                        <td align="right"> <%-- 주문총액/총포인트 --%> 
                            <fmt:formatNumber value="${cartvo.prod.totalPrice}" pattern="###,###" /> 원<br>
                            <fmt:formatNumber value="${cartvo.prod.totalPoint}" pattern="###,###" /> POINT
                            <input type="hidden" class="totalPrice" value="${cartvo.prod.totalPrice}" />
                            <input type="hidden" class="totalPoint" value="${cartvo.prod.totalPoint}" />
                        </td>
                        <td align="center"> <%-- 장바구니에서 해당 특정 제품 비우기 --%> 
                            <button type ="button" class="btn btn-outline-danger btn-sm" onclick="goDel('${cartvo.cartno}')">삭제</button>  
                        </td>
                    </tr>
                 </c:forEach>
            </c:if>   
            
            <tr>
                 <td colspan="3" align="right">
                     <span style="font-weight: bold;">장바구니 총액 :</span>
                  <span style="color: red; font-weight: bold;"><fmt:formatNumber value="${requestScope.sumMap.SUMTOTALPRICE}" pattern="###,###" /></span> 원  
                     <br>
                     <span style="font-weight: bold;">총 포인트 :</span> 
                  <span style="color: red; font-weight: bold;"><fmt:formatNumber value="${requestScope.sumMap.SUMTOTALPOINT}" pattern="###,###" /></span> POINT 
                 </td>
                 <td colspan="3" align="center">
                  <button type ="button" class="btn btn-outline-dark btn-sm mr-3" onclick="goOrder()">주문하기</button>
                    <a class="btn btn-outline-dark btn-sm" href="<%= request.getContextPath() %>/shop/mallHome1.up" role="button">계속쇼핑</a>
                 </td>
            </tr>
            
         </tbody>
      </table> 
   </div>
   
   
    <div>
        <p id="order_error_msg" class="text-center text-danger font-weight-bold h4"></p>
    </div>
   
   <%-- CSS 로딩화면 구현한것--%>
    <div style="display: flex">
      <div class="loader" style="margin: auto"></div>
   </div>
   
  
   
    
    
 </div>
    
<jsp:include page="../footer2.jsp" />  
  