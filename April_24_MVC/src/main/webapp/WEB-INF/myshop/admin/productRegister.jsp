<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>



<jsp:include page="../../header2.jsp" />   

<style type="text/css">

	table#tblProdInput {border: solid gray 1px; 
                       border-collapse: collapse; }
                       
    table#tblProdInput td {border: solid gray 1px; 
                          padding-left: 10px;
                          height: 50px; }
                          
    .prodInputName {background-color: #e6fff2; 
                    font-weight: bold; }                                                 
   
   .error {color: red; font-weight: bold; font-size: 9pt;}

</style>


<script type="text/javascript">

	$(document).ready(function(){
		
		$("span.error").hide();
		
		// 제품수량에 스피너 달아주기  (jquery ui 사이트에서 가져온것 //https://jqueryui.com/spinner/#content)
	    $("input#spinnerPqty").spinner({
	       spin:function(event,ui){
	          if(ui.value > 100) {						// max 값이 100
	             $(this).spinner("value", 100);
	             return false;
	          }
 	          else if(ui.value < 1) {					// min 값이 1  없으면 무한대이다.
     	         $(this).spinner("value", 1);
	             return false;
	          }
	       }
	    });// end of $("#spinnerPqty").spinner()--------
	    
	    
	    
	    // 추가이미지에 스피너 달아주기  (jquery ui 사이트에서 가져온것 //https://jqueryui.com/spinner/#content)
	    $("input#spinnerImgQty").spinner({
	       spin:function(event,ui){
	          if(ui.value > 10) {						// max 값이 10
	             $(this).spinner("value", 10);
	             return false;
	          }
 	          else if(ui.value < 0) {					// min 값이 0  없으면 무한대이다.
     	         $(this).spinner("value", 0);
	             return false;
	          }
	       }
	    });// end of $("#spinnerPqty").spinner()--------
	    
	    
	    
	 	// #### 스피너의 이벤트는 click 도 아니고 change 도 아니고 "spinstop" 이다. #### //
	    $("input#spinnerImgQty").bind("spinstop", function() {
	    	
	    	let html =``;   // 백틱 사용
	    	let cnt = $(this).val();  //this 는  $("input#spinnerImgQty") 을 뜻함  
	    	
	    	//console.log(" == 확인용 cnt => " + cnt);
	    	//console.log(" == 확인용 typeof cnt => " + typeof cnt);   // map 은 항상 String 이 타입이다.
	    	// == 확인용 typeof cnt => string
	    	
	    	for(let i=0; i<Number(cnt); i++ ) {
	    		
	    		html += `<br><input type="file" name="attach\${i}" class="btn btn-default img_file" accept='image/*' />`;	    	
	    	}// end of for
	    	
	    	$("div#divfileattach").html(html);  // html 을 뿌려라 
	    	$("input#attachCount").val(cnt);    // 몇개를 넣었다.
	    	
	    	
	    }); // end of $("input#spinnerImgQty").bind("spinstop", function()
	    
	    
	    		
	    // 이벤트 하나 추가 함
	    // === 제품이미지 또는 추가이미지 파일을 선택하면 화면에 이미지를 미리 보여주기 시작 //
	    
	    
	    //~~~~~~ **** !!!! 중요 !!!! **** ~~~~~~~//
        /*
	          선택자를 잡을때 선택자가 <body>태그에 직접 기술한 것이라면 선택자를 제대로 잡을수가 있으나
	          스크립트내에서 기술한 것이라면 선택자를 못 잡아올수도 있다.
	          이러한 경우는 아래와 해야만 된다.
	         $(document).on("이벤트종류", "선택자", function(){});  으로 한다.
       */
	    
       /*
       // 아래와 같이 해야만 작동한다 (사진 위에 마우스 올리면 alert 나옴)
			$(document).on("click", "input.img_file", function(){
		    	alert("확인용입니다.");   // 여기는 추가적으로 html 을 가져오는 부분은 실행되지 않았다. 
		    });
		*/ 
       

		// 파일 이름을 변경해야만 이벤트가 일어나도록 해야 하므로 change 이벤트를 사용해야 한다.
		$(document).on("change", "input.img_file", function(e){
			
			const input_file = $(e.target).get(0);
			// jQuery선택자.get(0) 은 jQuery 선택자인 jQuery Object 를 DOM(Document Object Model) element 로 바꿔주는 것이다. 
	        // DOM element 로 바꿔주어야 순수한 javascript 문법과 명령어를 사용할 수 있게 된다. 
			
	        console.log(input_file.files);
	        /*
	         FileList {0: File, length: 1}
	            0:    File {name: 'berkelekle심플라운드01.jpg', lastModified: 1605506138000, lastModifiedDate: Mon Nov 16 2020 14:55:38 GMT+0900 (한국 표준시), webkitRelativePath: '', size: 71317, …}
	            length: 1
	            [[Prototype]]: FileList
	         */
	         // 파일이름을 선택한 후, file input 엘리먼트의 files 프로퍼티를 출력해보면, 위와 같이 FileList 라는 객체가 출력되는 것을 볼 수 있다. 
	         // FileList 객체 프로퍼티(키)는 0,1 … 형태의 숫자로, 그리고 값에는 File 객체가 들어있다. 
	         // file input 엘리먼트의 files 프로퍼티의 값이 FileList 로 되어있으므로 File 객체에 접근하려면 input_file.files[i] 이런 식으로 사용하여 i 번째의 File 객체에 접근을 한다.
	        
	         
	         console.log(input_file.files[0]);   // 대괄호 표기법  0 은 key 값을 나타낸다.
	         /*
	            File {name: 'berkelekle심플라운드01.jpg', lastModified: 1605506138000, lastModifiedDate: Mon Nov 16 2020 14:55:38 GMT+0900 (한국 표준시), webkitRelativePath: '', size: 71317, …}
	         
	             >>설명<<
	             name : 단순 파일의 이름 string타입으로 반환 (경로는 포함하지 않는다.)
	             lastModified : 마지막 수정 날짜 number타입으로 반환 (없을 경우, 현재 시간)
	             lastModifiedDate: 마지막 수정 날짜 Date객체타입으로 반환
	             size : 64비트 정수의 바이트 단위 파일의 크기 number타입으로 반환
	             type : 문자열인 파일의 MIME 타입 string타입으로 반환 
	                    MIME 타입의 형태는 type/subtype 의 구조를 가지며, 다음과 같은 형태로 쓰인다. 
	                   text/plain
	                   text/html
	                   image/jpeg
	                   image/png
	                   audio/mpeg
	                   video/mp4
	                   ...
	         */
	         
	         // 첨부한 파일 이름을 알아온다.
	         console.log(input_file.files[0].name);   // .표기법사용  파일명을 얻어온다.
	         // berkelekle심플라운드01.jpg
	         
	         
	         // File 객체의 실제 데이터(내용물)에 접근하기 위해 FileReader 객체를 생성하여 사용한다.
	         const fileReader = new FileReader();
	         
	         fileReader.readAsDataURL(input_file.files[0]); // FileReader.readAsDataURL() --> 파일을 읽고, result속성에 파일을 나타내는 URL을 저장 시켜준다.
	      
	          fileReader.onload = function() { // FileReader.onload --> 파일 읽기 완료 성공시에만 작동하도록 하는 것임. 
	             console.log(fileReader.result);
	          /*
	              data:image/jpeg;base64,/9j/4AAQSkZJRgABAQEAeAB4AAD/2wBDAAIBAQIBAQICAgICAgICAwUDAwMDAwYEBAMFBwYHBwcGBwcICQsJCAgKCAcHCg0KCgsMDAwMBwkODw0MDgsMDAz/2wBDAQICAg 
	              이러한 형태로 출력되며, img.src 의 값으로 넣어서 사용한다.
	          */
	             document.getElementById("previewImg").src = fileReader.result;
	         };
		}); // end of $(document).on("change", "input.img_file", function(e)
		
	    // === 제품이미지 또는 추가이미지 파일을 선택하면 화면에 이미지를 미리 보여주기 끝 //
	    
	    
	    // 제품 등록하기 
	    $("input#btnRegister").click(function(){
	    	
	    	let flag = false;   // 필수 입력사항을 구분하기 위해 사용한다.
	    	
	    	$(".infoData").each(function(index, elmt){     //이건 j쿼리
	    		
	    		const val = $(elmt).val().trim();      //각각 elmt를 검사할 것이다.
	    		if(val == "") {
	    			// 아무것도 없다면 에러 메세지 보내자
	    			$(elmt).next().show();    // next() 는 형제 다음을 보이자 (에러 메세지) 
	    			flag = true;			  // 깃발 상태를 true 로 변경 
	    			return false;			  // 반복문을 멈춘다.
	    		}
	    	});    
	    	
	    	// flag 가 반복문을 돌아도 false 라면
	    	if(!flag) {
	    		const frm = document.prodInputFrm;
	    		frm.submit();
	    	}
	    		
	    		
	    });
	    
	    
	    
	    // 취소를 누르면 모든 입력값 초기화 하기 
	    $("input[type='reset']").click(function(){
	    	
	    	$("span.error").hide();
	    	$("div#divfileattach").empty();  // 싹 비우자
	    	$("img#previewImg").hide();      // 미리보기를 감춘다.
	    	
	    });
		
		
		
	    
	   
		
		
		
		
	});// end of $(document).ready(function()



</script>
	
	
	<div align="center" style="margin-bottom: 20px;">
	
	<div style="border: solid green 2px; width: 250px; margin-top: 20px; padding-top: 10px; padding-bottom: 10px; border-left: hidden; border-right: hidden;">       
	   <span style="font-size: 15pt; font-weight: bold;">제품등록&nbsp;[관리자전용]</span>   
	</div>
	<br/>
	
	<%-- !!!!! ==== 중요 ==== !!!!! --%>
	<%-- 폼에서 파일을 업로드 하려면 반드시 method 는 POST 이어야 하고 
	     enctype="multipart/form-data" 으로 지정해주어야 한다.!! --%>
	<form name="prodInputFrm"
	      action="<%= request.getContextPath()%>/shop/admin/productRegister.up"
	      method="POST"                         
	      enctype="multipart/form-data">     <%-- 꼭 기억하기 --%>
	      
	<table id="tblProdInput" style="width: 80%;">
	<tbody>
	   <tr>
	      <td width="25%" class="prodInputName" style="padding-top: 10px;">카테고리</td>
	      <td width="75%" align="left" style="padding-top: 10px;" >
	         <select name="fk_cnum" class="infoData">
	            <option value="">:::선택하세요:::</option>
	            <%-- 
	               <option value="1">전자제품</option>
	               <option value="2">의  류</option>
	               <option value="3">도  서</option>
	            --%> 
	            <c:forEach var = "map" items="${requestScope.categotyList}">				<%--DB에서 받아오기 위해 위의 주석대신 사용한다. --%>
	            	<option value="${map.cnum}">${map.cname}</option>    <%-- 카테고리 에서 map 단위로 불러와서 이름만 꺼내자  map이니까 key 값만 써도 된다. --%>
	            </c:forEach>
	            
	            
	         </select>
	         <span class="error">필수입력</span>
	      </td>   
	   </tr>
	   <tr>
	      <td width="25%" class="prodInputName">제품명</td>
	      <td width="75%" align="left" style="border-top: hidden; border-bottom: hidden;" >
	         <input type="text" style="width: 300px;" name="pname" class="box infoData" />
	         <span class="error">필수입력</span>
	      </td>
	   </tr>
	   <tr>
	      <td width="25%" class="prodInputName">제조사</td>
	      <td width="75%" align="left" style="border-top: hidden; border-bottom: hidden;">
	         <input type="text" style="width: 300px;" name="pcompany" class="box infoData" />
	         <span class="error">필수입력</span>
	      </td>
	   </tr>
	   <tr>
	      <td width="25%" class="prodInputName">제품이미지</td>
	      <td width="75%" align="left" style="border-top: hidden; border-bottom: hidden;">
	         <input type="file" name="pimage1" class="infoData img_file" accept='image/*' /><span class="error">필수입력</span>
	         <input type="file" name="pimage2" class="infoData img_file" accept='image/*' /><span class="error">필수입력</span>
	      </td>
	   </tr>
	   <tr>
	      <td width="25%" class="prodInputName">제품설명서 파일첨부(선택)</td>
	      <td width="75%" align="left" style="border-top: hidden; border-bottom: hidden;">
	         <input type="file" name="prdmanualFile" />
	      </td>
	   </tr>
	   <tr>
	      <td width="25%" class="prodInputName">제품수량</td>
	      <td width="75%" align="left" style="border-top: hidden; border-bottom: hidden;">
	              <input id="spinnerPqty" name="pqty" value="1" style="width: 30px; height: 20px;"> 개
	         <span class="error">필수입력</span>
	      </td>
	   </tr>
	   <tr>
	      <td width="25%" class="prodInputName">제품정가</td>
	      <td width="75%" align="left" style="border-top: hidden; border-bottom: hidden;">
	         <input type="text" style="width: 100px;" name="price" class="box infoData" /> 원
	         <span class="error">필수입력</span>
	      </td>
	   </tr>
	   <tr>
	      <td width="25%" class="prodInputName">제품판매가</td>
	      <td width="75%" align="left" style="border-top: hidden; border-bottom: hidden;">
	         <input type="text" style="width: 100px;" name="saleprice" class="box infoData" /> 원
	         <span class="error">필수입력</span>
	      </td>
	   </tr>
	   <tr>
	      <td width="25%" class="prodInputName">제품스펙</td>
	      <td width="75%" align="left" style="border-top: hidden; border-bottom: hidden;">
	         <select name="fk_snum" class="infoData">
	            <option value="">:::선택하세요:::</option>
	            <%-- 
	               <option value="1">HIT</option>
	               <option value="2">NEW</option>
	               <option value="3">BEST</option> 
	            --%>
	            
	            
	            <c:forEach var = "spvo" items="${requestScope.specList}">				<%--DB에서 받아오기 위해 위의 주석대신 사용한다. --%>
	            	<option value="${spvo.snum}">${spvo.sname}</option>
	            </c:forEach>
	            
	            
	         </select>
	         <span class="error">필수입력</span>
	      </td>
	   </tr>
	   <tr>
	      <td width="25%" class="prodInputName">제품설명</td>
	      <td width="75%" align="left" style="border-top: hidden; border-bottom: hidden;">
	         <textarea name="pcontent" rows="5" cols="60"></textarea>
	      </td>
	   </tr>
	   <tr>
	      <td width="25%" class="prodInputName" style="padding-bottom: 10px;">제품포인트</td>
	      <td width="75%" align="left" style="border-top: hidden; border-bottom: hidden; padding-bottom: 10px;">
	         <input type="text" style="width: 100px;" name="point" class="box infoData" /> POINT
	         <span class="error">필수입력</span>
	      </td>
	   </tr>
	   
	   <%-- ==== 첨부파일 타입 추가하기 ==== --%>
	    <tr>
	          <td width="25%" class="prodInputName" style="padding-bottom: 10px;">추가이미지파일(선택)</td>
	          <td>
	             <label for="spinnerImgQty">파일갯수 : </label>
	          <input id="spinnerImgQty" value="0" style="width: 30px; height: 20px;">
	             <div id="divfileattach"></div>
	              
	             <input type="text" name="attachCount" id="attachCount" />
	              
	          </td>
	    </tr>
	    
	    <%-- ==== 이미지파일 미리보여주기 ==== --%>
	    <tr>
	          <td width="25%" class="prodInputName" style="padding-bottom: 10px;">이미지파일 미리보기</td>
	          <td>
	             <img id="previewImg" width="300" />
	          </td>
	    </tr>
	   
	   <tr style="height: 70px;">
	      <td colspan="2" align="center" style="border-left: hidden; border-bottom: hidden; border-right: hidden;">
	          <input type="button" value="제품등록" id="btnRegister" style="width: 80px;" /> 
	          &nbsp;
	          <input type="reset" value="취소"  style="width: 80px;" />   
	      </td>
	   </tr>
	</tbody>
	</table>
	</form>
	</div>
	



 
<jsp:include page="../../footer2.jsp" /> 