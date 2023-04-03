<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<% 
	// 암기 하기
	// 컨텍스트 패스명(context path name)을 알아오고자 한다.
	String ctxPath = request.getContextPath();

	//System.out.println("ctxPath => " + ctxPath);
	
	
	
%>    

    
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>개인성향 데이터를 오라클 DB로 전송하기</title>

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
		
		// === 유효성 검사 === //
		$("form[name='registerFrm']").submit(function(){
			
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
			
			
		});// end of $("form[name='registerFrm']").submit(function(){
		
		
	}); // end of $(document).ready(function(){


</script>



</head>
<body>
	
	<div style="width: 60%; margin: 50px auto;">
		<form name="registerFrm" action="<%= ctxPath %>/personRegister.do" method="post">   <!-- method 에 get을 쓰면 전송되어질 값이 보이고 post 를 쓰면 전송되어질 값이 안보인다. (method 생략시 같다.) -->
		
			<fieldset>
		      <legend>개인성향 테스트(POST method)</legend>
		      <ul>
			         <li>
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
			            <input type="submit" value="전송" />
			            <input type="reset" value="취소" />
			         </li>
			      </ul>
		   </fieldset>
		
		
		</form>
	</div>
	
</body>
</html>