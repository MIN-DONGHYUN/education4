<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<% 
	// 암기 하기
	// 컨텍스트 패스명(context path name)을 알아오고자 한다.
	String ctxPath = request.getContextPath();

	System.out.println("ctxPath => " + ctxPath);
	
	
	
%>    

    
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>개인성향 수정된 데이터를 오라클 DB로 전송하기</title>

<style type="text/css">

	ul {
		list-style-type: none;
	}
	li {
		line-height: 200%;
	}
	

</style>

<script type="text/javascript" src="<%= ctxPath %>/js/jquery-3.6.4.min.js"></script>
<script type="text/javascript">


	$(document).ready(function(){
		
		
	// 1. 성명을 입력해주기
	$("input:text[name='name']").val("${requestScope.psdto.name}"); // 선택자 잡기 	
	
	// 2. 학력 입력해주기
	$("select[name='school']").val("${requestScope.psdto.school}"); // 선택자 잡기 
	
	// 3. 색상 입력해주기
	<%--
	
		$("input:radio[name='color']").each(function(index, elmt) {     // for문 처럼 반복 한다.   for each 는 배열을 기준으로 한다.
			
			const user_color = "${requestScope.psdto.color}"	 // 사용자의 컬러
			// "red", "blue", "green" , "yellow" 중 하나가 나올 것이다.
			
			
			/*
				>>>> .prop() 와 .attr() 의 차이 <<<<            
		             .prop() ==> form 태그내에 사용되어지는 엘리먼트의 disabled, selected, checked 의 속성값 확인 또는 변경하는 경우에 사용함. 
		             .attr() ==> 그 나머지 엘리먼트의 속성값 확인 또는 변경하는 경우에 사용함.
			 */
			
			if($(elmt).attr("value") == user_color)   {		// val() 은 input 태그이나 select 태그일때 사용할 수 있다. 즉 여기에서는 attr("value") 를 사용해야 한다.      
			
				$(elmt).prop("checked", true);      // prop() 는 form 태그내에 사용되어지는 엘리먼트의 disabled, selected, checked 의 속성값 확인 또는 변경하는 경우에 사용함.
												   // ("checked", true)  를 사용하면 체크를 해주는 것이다.
				return false;                      // 반복문을 탈출하기 위해서 for 문의 반복문 break; 와 같은 역할을 한다.
			}
		});   
	
	--%>
	
	// 색상 입력해주기 또 다른 방법
	
	$("input:radio[id='${requestScope.psdto.color}']").prop("checked", true);   // 아이디 값으로 해보기   아이디값과 value 값이 같기 때문에 가능한 것     
	
	
	// 4. 음식 입력해주기 
	const user_foodes = "${requestScope.psdto.strFood}";
	
	//console.log(user_foodes);  // "짬뽕,탕수육"  또는  "없음" 
	
	<%--
	if(user_foodes != "없음") {
		
		const arr_food = user_foodes.split(",");   // 배열을 하나하나 쪼개서 보기 위해 
		// arr_food ==> ["짜장면","탕수육"]	
		
		
		// 2중 for 문이라고 생각하면 된다.
		arr_food.forEach(function(item,index,array) {   // 배열을 사용할 때에는 for each 를 사용한다.
			
			$("input:checkbox[name='food']").each(function(i, chkbox) {   // 복수개이니까 each 를 사용한다.  ( 5번 반복)
				
				 if($(chkbox).attr("value") == item) {    //val() 은 input & select 태그에만 사용 
				
					$(chkbox).prop("checked", true); 
				 
					return false; 						  // for 문의 break; 와 같은 것이다.
					 
				 }
			}); // end of $("input:checkbox[name='food']").each(function(i, chkbox) {  

		}); // end of arr_food.foreach(function(item, index, array) { 
		
	}
	--%>
	
	// 또 다른 방법
	
	if(user_foodes != "없음") {
		
		const arr_food = user_foodes.split(",");   // 배열을 하나하나 쪼개서 보기 위해 
		// arr_food ==> ["짜장면","탕수육"]
		
		arr_food.forEach(function(item, index, array) {
			
			//$("input:checkbox[value='짜장면']").prop("checked", true);   // 짜장면만 체크박스만 해준다. 
			$("input:checkbox[value='"+ item + "']").prop("checked", true);   // 각 아이템만 체크를 해준다. 
			
		}); // end of arr_food.forEach(function(item, index, array) {
	}
	
		
	
	
	
	
	
	
		
	
		
		
		// === 유효성 검사 === //
		$("form[name='updateFrm']").submit(function(){
			
			// 1. 성명은 필수 입력 검사
			if($("input:text[name='name']").val().trim() == "" ) {     // :text 는 타입, [name] 은 이름 
				alert("성명은 필수항목 이오니 성명을 입력하세요!!");
				return false;   // return false; 이 submit(전송)을 하지 말라는 뜻이다.
			}
			
			// 2. 학력도 필수 입력 검사
			if($("select[name='school']").val() == "선택하세요" ) {    
				alert("학력은 필수항목 이오니 입력하세요!!");
				return false;   // return false; 이 submit(전송)을 하지 말라는 뜻이다.
			}
			
			// 3. 색상도 필수 입력 검사
			const color_checked_length = $("input:radio[name='color']:checked").length;    
			if(color_checked_length == 0) {
				alert("색상을 선택하세요!!");
				return false;   // return false; 이 submit(전송)을 하지 말라는 뜻이다.
			}
			
			//4. 음식은 좋아하는 음식이 없다라면선택을 안하더라도 허용하겠다.
		      <%--
		         const food_checked_length = $("input:checkbox[name='food']:checked").length;
		         if(food_checked_length == 0){
		            alert("선호하는 음식을 최소한 1개 이상 입력하세요!!");
		            return false; // return false; 이 서브밋(전송)을 하지말라는 뜻이다.
		         }
		      --%>   
			
			
		});// end of $("form[name='updateFrm']").submit(function(){
		
		
	}); // end of $(document).ready(function(){


</script>



</head>
<body>
	
	<div style="width: 60%; margin: 50px auto;">
		<form name="updateFrm" action="<%= ctxPath %>/personUpdateEnd.do" method="post">   <!-- method 에 get을 쓰면 전송되어질 값이 보이고 post 를 쓰면 전송되어질 값이 안보인다. (method 생략시 같다.) -->
		
			<fieldset>
		      <legend> ${requestScope.psdto.name} 님 성향 정보 수정 하기</legend>   <%-- ${requestScope.psdto.name} 이거는 request 로 받아왔고 psdto 의 값을 넘겨줬기 때문에 이렇게 써서 이름을 받아온다. --%>
		      <ul>
			         <li>
			         	<input type="hidden" name="seq" value="${requestScope.psdto.seq}" />  <!-- 어떤 태그에 직접 줄 수도 있다.  -->
			            <label for="name">성명</label>
			            <input type="text" name="name" id="name" placeholder="성명입력"/> 
			         </li>
			         <li>
			            <label for="school">학력</label>
			            <select name="school" id="school"> 
			               <option >선택하세요</option>  
			               <option >고졸</option>  <!-- value 값이 주소로 넘어감 안쓰면 쓴 내용이 value 값으로 넘어감 지금은 고졸 아래줄은 초대줄 -->
			               <option >초대졸</option>
			               <option >대졸</option>
			               <option value="대학원졸">대학원졸</option>
			            </select>
			         </li>
			         <li>
			            <label for="">좋아하는 색상</label>
			            <div>
			               <label for="red">빨강</label>
			               <input type="radio" name="color" id="red" value="red" />
			               
			               <label for="blue">파랑</label>
			               <input type="radio" name="color" id="blue" value="blue" />
			               
			               <label for="green">초록</label>
			               <input type="radio" name="color" id="green" value="green" />
			               
			               <label for="yellow">노랑</label>
			               <input type="radio" name="color" id="yellow" value="yellow" />
			            </div>
			         </li>
			         <li>
			            <label for="">좋아하는 음식(다중선택)</label>
			            <div>
			                <label for="food1">짜장면</label>
			               <input type="checkbox" name="food" id="food1" value="짜장면" />
			               &nbsp;
			               
			               <label for="food2">짬뽕</label>
			               <input type="checkbox" name="food" id="food2" value="짬뽕" />
			               &nbsp;
			               
			               <label for="food3">탕수육</label>
			               <input type="checkbox" name="food" id="food3" value="탕수육" />
			               &nbsp;
			               
			               <label for="food4">양장피</label>
			               <input type="checkbox" name="food" id="food4" value="양장피" />
			               &nbsp;
			               
			               <label for="food5">팔보채</label>
			               <input type="checkbox" name="food" id="food5" value="팔보채" />
			            </div>
			         </li>
			         <li>
			            <input type="submit" value="수정완료" />
			            <input type="reset" value="취소" />
			         </li>
			      </ul>
		   </fieldset>
		
		
		</form>
	</div>
	
	
	
</body>
</html>