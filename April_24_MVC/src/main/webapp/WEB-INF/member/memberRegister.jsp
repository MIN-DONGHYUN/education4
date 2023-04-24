<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
	String ctxPath = request.getContextPath();
	//        /MyMVC
%>

<jsp:include page="../header.jsp"></jsp:include>				<!-- header 불러오기-->

<style type="text/css">

	/* 테이블 전체 */ 
	table#tblMemberRegister {
		
		width: 93%;
		
		/* hidden 은 선을 숨기는 것이다 */
		border: hidden;
		
		margin: 10px;
	}
	
	/* 테이블 전체에서 아이디 th 부분 */ 
	table#tblMemberRegister #th {
		height: 40px;
		text-align: center;
		background-color: silver;
		font-size: 14pt;
	}
	
	/* 테이블 에 모든 td 부분 */
	table#tblMemberRegister td {
		
		/* hidden 은 선을 숨기는 것이다 */
		border: hidden;
		line-height: 30px;
		padding: 8px 0;
		
	}
	
	/* class star 부분 */
	span.star {
		
		color: red;
		font-weight: bold;
		font-size: 13pt;
		
	}

</style>

<!-- 다음에서 주소찾기를 가져오는 것  -->
<script src="https://t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>

<script type="text/javascript">

	// "우편번호찾기" 를 클릭했는지 클릭을 안했는지 여부를 알아오기 위한 용도
	let b_flag_zipcodeSearch_click = false;		// 우편번호 찾기 부분을 했는지 안했는지 알기 위해 boolean 값을 설정한다.
	
	// "아이디중복" 를 클릭했는지 클릭을 안했는지 여부를 알아오기 위한 용도
	let b_flag_idDuplicate_click = false;		// 아이디 중복 부분을 했는지 안했는지 알기 위해 boolean 값을 설정한다.
	
	// "이메일중복" 를 클릭했는지 클릭을 안했는지 여부를 알아오기 위한 용도
	let b_flag_emailDuplicate_click = false;		// 이메일 중복 부분을 했는지 안했는지 알기 위해 boolean 값을 설정한다.

	
	
	$(document).ready(function() {
		
		$("span.error").hide();	   // 숨기자 
		$("input#name").focus();   // 처음실행시 잡히는 곳 
		
	/*
		$("input#name").bind("blur", function(){				// blur 와 bind 같은 기능을 한다.
			alert("확인용 : 포커스를 가졌다가 포커스를 잃어버렸군요.")
		});
	*/
		// 또는
	/*
		$("input#name").blur(function(){
			alert("확인용2 : 포커스를 가졌다가 포커스를 잃어버렸군요. ㅠㅠㅠㅠ")
		});
	*/
	
		// 성명 부분
		$("input#name").blur((e)  => {     // 화살표 함수 
 			
			// 화살표 함수를 사용했을때 $(this) 를 사용하면 아무 결과도 받지 못한다.
			// 즉 ${e.target} 을 사용해야 하고 $(this)를 사용하려면 화살표 함수 대신 function() 을 사용해야 한다.
			//alert($(e.target).val());				// 입력한 값 알아오기    
			
			
			if($(e.target).val().trim() == "" ) {
				// 입력하지 않거나 공백만 입력했을 경우 if 문이 실행 
				
				// 이것은 모든 곳을 show 해준다.
				//$("span.error").show();			// error 일 경우 show 보여줘라 
				
				
				// 테이블 안에 있는 모든 input 태그에 대해서 prop 해서 모든 것을 막아버린다.   
				/*
					>>>> .prop() 와 .attr() 의 차이 <<<<            
		                 .prop() ==> form 태그내에 사용되어지는 엘리먼트의 disabled, selected, checked 의 속성값 확인 또는 변경하는 경우에 사용함. 
		                 .attr() ==> 그 나머지 엘리먼트의 속성값 확인 또는 변경하는 경우에 사용함.
	            */
				// prop 는 ==> radio,checkbox의 체크 등등임			
				
				$("table#tblMemberRegister :input").prop("disabled", true);   // 모두 못쓰게 한다. 기능 못하게 하는것을 true
				$(e.target).prop("disabled", false);      // 이벤트 일어난 곳만 살리자
				
				
				// 이벤트 발생되어진 다음 이벤트 만 보여라   성명일 경우 성명은 필수 입력 사항입니다 만 나오게 된다. 
				//$(e.target).next().show();
					// 또는 
				$(e.target).parent().find("span.error").show();
				
				// 이벤트가 발생되어진 곳에 다시 focus 를 보내자
				$(e.target).focus();
			}
			else {
				// 공백만이 아닌 글자를 입력했을 경우 
				$("table#tblMemberRegister :input").prop("disabled", false);    // 전부다 기능하도록 살리자
				
				// 이벤트 발생되어진 다음 이벤트 만 보여라   성명일 경우 성명은 필수 입력 사항입니다 만 나오게 된다. 
				//$(e.target).next().hide();
					// 또는 
				$(e.target).parent().find("span.error").hide();
					
			}
			
		});  // 아이디가 name 인 것은 포커스를 잃어버렸을 경우 (blur) 이벤트를 처리해주는 것이다.
	
	
		// 아이디 부분
		$("input#userid").blur((e)  => {     // 화살표 함수 
 			
			// 화살표 함수를 사용했을때 $(this) 를 사용하면 아무 결과도 받지 못한다.
			// 즉 ${e.target} 을 사용해야 하고 $(this)를 사용하려면 화살표 함수 대신 function() 을 사용해야 한다.
			//alert($(e.target).val());				// 입력한 값 알아오기    
			
			
			if($(e.target).val().trim() == "" ) {
				// 입력하지 않거나 공백만 입력했을 경우 if 문이 실행 
				
				// 이것은 모든 곳을 show 해준다.
				//$("span.error").show();			// error 일 경우 show 보여줘라 
				
				
				// 테이블 안에 있는 모든 input 태그에 대해서 prop 해서 모든 것을 막아버린다.   
				/*
					>>>> .prop() 와 .attr() 의 차이 <<<<            
		                 .prop() ==> form 태그내에 사용되어지는 엘리먼트의 disabled, selected, checked 의 속성값 확인 또는 변경하는 경우에 사용함. 
		                 .attr() ==> 그 나머지 엘리먼트의 속성값 확인 또는 변경하는 경우에 사용함.
	            */
				// prop 는 ==> radio,checkbox의 체크 등등임			
				
				$("table#tblMemberRegister :input").prop("disabled", true);   // 모두 못쓰게 한다. 기능 못하게 하는것을 true
				$(e.target).prop("disabled", false);      // 이벤트 일어난 곳만 살리자
				
				
				// 이벤트 발생되어진 다음 이벤트 만 보여라   성명일 경우 성명은 필수 입력 사항입니다 만 나오게 된다. 
				//$(e.target).next().show();
					// 또는 
				$(e.target).parent().find("span.error").show();
				
				// 이벤트가 발생되어진 곳에 다시 focus 를 보내자
				$(e.target).focus();
			}
			else {
				// 공백만이 아닌 글자를 입력했을 경우 
				$("table#tblMemberRegister :input").prop("disabled", false);    // 전부다 기능하도록 살리자
				
				// 이벤트 발생되어진 다음 이벤트 만 보여라   성명일 경우 성명은 필수 입력 사항입니다 만 나오게 된다. 
				//$(e.target).next().hide();
					// 또는 
				$(e.target).parent().find("span.error").hide();
					
			}
			
		});  // 아이디가 userid 인 것은 포커스를 잃어버렸을 경우 (blur) 이벤트를 처리해주는 것이다.
		
		
		
		// 암호 부분
		$("input#pwd").blur((e)  => {     // 화살표 함수 
 			
			// 정규표현식 비밀번호 부분 
			const regExp = /^.*(?=^.{8,15}$)(?=.*\d)(?=.*[a-zA-Z])(?=.*[^a-zA-Z0-9]).*$/g;
			
			const bool = regExp.test($(e.target).val());  // 값을 넣어준다. 정규표현식이면 true 아니면 false
		
			if(!bool) {
				// 암호가 정규표햔식에 위배된 경우 
				$("table#tblMemberRegister :input").prop("disabled", true);   // 모두 못쓰게 한다. 기능 못하게 하는것을 true
				$(e.target).prop("disabled", false);      // 이벤트 일어난 곳만 살리자
				
				
				// 이벤트 발생되어진 다음 이벤트 만 보여라   성명일 경우 성명은 필수 입력 사항입니다 만 나오게 된다. 
				//$(e.target).next().show();
					// 또는 
				$(e.target).parent().find("span.error").show();
				
				// 이벤트가 발생되어진 곳에 다시 focus 를 보내자
				$(e.target).focus();
			}
			else {
				// 암호가 정규표햔식에 맞는 경우 
				// 공백만이 아닌 글자를 입력했을 경우 
				$("table#tblMemberRegister :input").prop("disabled", false);    // 전부다 기능하도록 살리자
				
				// 이벤트 발생되어진 다음 이벤트 만 보여라   성명일 경우 성명은 필수 입력 사항입니다 만 나오게 된다. 
				//$(e.target).next().hide();
					// 또는 
				$(e.target).parent().find("span.error").hide();
			}
		
		});  // 아이디가 pwd 인 것은 포커스를 잃어버렸을 경우 (blur) 이벤트를 처리해주는 것이다.
		
		
		// 암호 확인 부분
		$("input#pwdcheck").blur((e)  => {     // 화살표 함수 
 			
			if($("input#pwd").val() != $(e.target).val()) {
				// 암호 확인 값이 다른 경우 
				// 암호가 정규표햔식에 위배된 경우 
				$("table#tblMemberRegister :input").prop("disabled", true);   // 모두 못쓰게 한다. 기능 못하게 하는것을 true
				$(e.target).prop("disabled", false);      // 이벤트 일어난 곳만 살리자
				$("input#pwd").prop("disabled", false);      // 이벤트 일어난 곳만 살리자
				
				
				// 이벤트 발생되어진 다음 이벤트 만 보여라   성명일 경우 성명은 필수 입력 사항입니다 만 나오게 된다. 
				//$(e.target).next().show();
					// 또는 
				$(e.target).parent().find("span.error").show();
				
				// 이벤트가 발생되어진 곳에 다시 focus 를 보내자
				$("input#pwd").focus();
				
			}
			else {
				// 암호 확인 값이 같은 경우 
				// 암호가 정규표햔식에 맞는 경우 
				// 공백만이 아닌 글자를 입력했을 경우 
				$("table#tblMemberRegister :input").prop("disabled", false);    // 전부다 기능하도록 살리자
				
				// 이벤트 발생되어진 다음 이벤트 만 보여라   성명일 경우 성명은 필수 입력 사항입니다 만 나오게 된다. 
				//$(e.target).next().hide();
					// 또는 
				$(e.target).parent().find("span.error").hide();
			}
		
		});  // 아이디가 pwdcheck 인 것은 포커스를 잃어버렸을 경우 (blur) 이벤트를 처리해주는 것이다.
		
		
		
		// 이메일 부분
		$("input#email").blur((e)  => {     // 화살표 함수 
 			
			// 정규표현식 이메일 부분 
			const regExp = /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i; 

			
			const bool = regExp.test($(e.target).val());  // 값을 넣어준다. 정규표현식이면 true 아니면 false
		
			if(!bool) {
				// 이메일이 정규표햔식에 위배된 경우 
				$("table#tblMemberRegister :input").prop("disabled", true);   // 모두 못쓰게 한다. 기능 못하게 하는것을 true
				$(e.target).prop("disabled", false);      // 이벤트 일어난 곳만 살리자
				
				
				// 이벤트 발생되어진 다음 이벤트 만 보여라   성명일 경우 성명은 필수 입력 사항입니다 만 나오게 된다. 
				//$(e.target).next().show();
					// 또는 
				$(e.target).parent().find("span.error").show();
				
				// 이벤트가 발생되어진 곳에 다시 focus 를 보내자
				$(e.target).focus();
			}
			else {
				// 이메일이 정규표햔식에 맞는 경우 
				// 공백만이 아닌 글자를 입력했을 경우 
				$("table#tblMemberRegister :input").prop("disabled", false);    // 전부다 기능하도록 살리자
				
				// 이벤트 발생되어진 다음 이벤트 만 보여라   성명일 경우 성명은 필수 입력 사항입니다 만 나오게 된다. 
				//$(e.target).next().hide();
					// 또는 
				$(e.target).parent().find("span.error").hide();
			}
		
		});  // 아이디가 email 인 것은 포커스를 잃어버렸을 경우 (blur) 이벤트를 처리해주는 것이다.
		
		
		// hp2 부분 (국번)
		$("input#hp2").blur((e)  => {     // 화살표 함수 
 			
			// 정규표현식 hp2 부분 
			const regExp = /^[1-9][0-9]{2,3}$/g;    // 2번 또는 3번 반복

			
			const bool = regExp.test($(e.target).val());  // 값을 넣어준다. 정규표현식이면 true 아니면 false
		
			if(!bool) {
				// 국번이 정규표햔식에 위배된 경우 
				$("table#tblMemberRegister :input").prop("disabled", true);   // 모두 못쓰게 한다. 기능 못하게 하는것을 true
				$(e.target).prop("disabled", false);      // 이벤트 일어난 곳만 살리자
				
				
				// 이벤트 발생되어진 다음 이벤트 만 보여라   성명일 경우 성명은 필수 입력 사항입니다 만 나오게 된다. 
				//$(e.target).next().show();
					// 또는 
				$(e.target).parent().find("span.error").show();
				
				// 이벤트가 발생되어진 곳에 다시 focus 를 보내자
				$(e.target).focus();
			}
			else {
				// 국번이 정규표햔식에 맞는 경우 
				// 공백만이 아닌 글자를 입력했을 경우 
				$("table#tblMemberRegister :input").prop("disabled", false);    // 전부다 기능하도록 살리자
				
				// 이벤트 발생되어진 다음 이벤트 만 보여라   성명일 경우 성명은 필수 입력 사항입니다 만 나오게 된다. 
				//$(e.target).next().hide();
					// 또는 
				$(e.target).parent().find("span.error").hide();
			}
		
		});  // 아이디가 hp2 인 것은 포커스를 잃어버렸을 경우 (blur) 이벤트를 처리해주는 것이다.
	
		
		
		// hp3 부분 (연락처 마지막 4자리)
		$("input#hp2").blur((e)  => {     // 화살표 함수 
 			
			// 정규표현식 hp3 부분 
			
			// const regExp = /^[0-9][4]$/g;    // 0-9까지가 4번 나온다.
				// 또는
			const regExp = 	/^\d{4}$/g;			// 숫자가 4번 반복 
			// 숫자 4자리만 들어오도록 검사해주는 정규표현식 객체 생성
			
			const bool = regExp.test($(e.target).val());  // 값을 넣어준다. 정규표현식이면 true 아니면 false
		
			if(!bool) {
				// 마지막 전화번호 4자리가 정규표햔식에 위배된 경우 
				$("table#tblMemberRegister :input").prop("disabled", true);   // 모두 못쓰게 한다. 기능 못하게 하는것을 true
				$(e.target).prop("disabled", false);      // 이벤트 일어난 곳만 살리자
				
				
				// 이벤트 발생되어진 다음 이벤트 만 보여라   성명일 경우 성명은 필수 입력 사항입니다 만 나오게 된다. 
				//$(e.target).next().show();
					// 또는 
				$(e.target).parent().find("span.error").show();
				
				// 이벤트가 발생되어진 곳에 다시 focus 를 보내자
				$(e.target).focus();
			}
			else {
				// 마지막 전화번호 4자리가 정규표햔식에 맞는 경우 
				// 공백만이 아닌 글자를 입력했을 경우 
				$("table#tblMemberRegister :input").prop("disabled", false);    // 전부다 기능하도록 살리자
				
				// 이벤트 발생되어진 다음 이벤트 만 보여라   성명일 경우 성명은 필수 입력 사항입니다 만 나오게 된다. 
				//$(e.target).next().hide();
					// 또는 
				$(e.target).parent().find("span.error").hide();
			}
		
		});  // 아이디가 hp3 인 것은 포커스를 잃어버렸을 경우 (blur) 이벤트를 처리해주는 것이다.
		
		
		// 우편 번호 
		$("img#zipcodeSearch").click(function() {
			
			new daum.Postcode({
	            oncomplete: function(data) {
	                // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

	                // 각 주소의 노출 규칙에 따라 주소를 조합한다.
	                // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
	                let addr = ''; // 주소 변수
	                let extraAddr = ''; // 참고항목 변수

	                //사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
	                if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
	                    addr = data.roadAddress;
	                } else { // 사용자가 지번 주소를 선택했을 경우(J)
	                    addr = data.jibunAddress;
	                }

	                // 사용자가 선택한 주소가 도로명 타입일때 참고항목을 조합한다.
	                if(data.userSelectedType === 'R'){
	                    // 법정동명이 있을 경우 추가한다. (법정리는 제외)
	                    // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
	                    if(data.bname !== '' && /[동|로|가]$/g.test(data.bname)){
	                        extraAddr += data.bname;
	                    }
	                    // 건물명이 있고, 공동주택일 경우 추가한다.
	                    if(data.buildingName !== '' && data.apartment === 'Y'){
	                        extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
	                    }
	                    // 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
	                    if(extraAddr !== ''){
	                        extraAddr = ' (' + extraAddr + ')';
	                    }
	                    // 조합된 참고항목을 해당 필드에 넣는다.
	                    document.getElementById("extraAddress").value = extraAddr;
	                
	                } else {
	                    document.getElementById("extraAddress").value = '';
	                }

	                // 우편번호와 주소 정보를 해당 필드에 넣는다.
	                document.getElementById('postcode').value = data.zonecode;
	                document.getElementById("address").value = addr;
	                // 커서를 상세주소 필드로 이동한다.
	                document.getElementById("detailAddress").focus();
	            }
	        }).open();
			
		});
		
		//////////////////////////////////////////////////////////////////////////////////////
		
		// 키를 눌렀다가 땠을때 이벤트 처리
		// 키를 눌렀다가 땟을때 실행한다.
		$("input#birthyyyy").keyup(function (e) {
			
			alert("생년월일을 키보드로 입력하지 마시고 마우스로 선택하세요!!!");	 // 키를 땔때 출력 한다. 
			$(e.target).val("1950");   // alert를 해주고 나서 무조건 1950을 넣어준다.
			
			
		});
		
		
		// 생년월일 중 월을 나타내자 HTML 로 나타내지 말고 자바로 나타내 보자 
		
		let = mm_html = "";     // 변수를 선언하고 
		for(let i = 1; i<= 12; i++ )		// 반복 회수 
		{
			if( i >= 10)					// 10 이상이면 0을 안붙여도 된다.
			{
				mm_html +=  "<option value ='" + i + "'>" + i + "</option>";	
			}
			else {							// 10 이하이면 앞에 0을 붙여야 한다.
				mm_html +=  "<option value = '0" + i +"'>0" + i + "</option>";	
			}
			
		}// end of for 
		
		//console.log("확인용 mm_html => " + mm_html);
		//memberRegister.up:527 확인용 mm_html => <option value = '01'>01</option><option value = '02'>02</option><option value = '03'>03</option><option value = '04'>04</option><option value = '05'>05</option><option value = '06'>06</option><option value = '07'>07</option><option value = '08'>08</option><option value = '09'>09</option><option value ='10'>10</option><option value ='11'>11</option><option value ='12'>12</option>
		
		$("select#birthmm").html(mm_html);  // 출력하는것을 잘 알아두자 
		
		
		
		// 생년월일 중 일을 나타내자 HTML 로 나타내지 말고 자바로 나타내 보자  (백틱으로 해보자)
		
		// 함수를 사용하여 하기
		//func_dd_html();  // 생년월일 중 일에 해당하는 값을 넣어주는 함수 호출하기		
		
		// 안에서 백틱 사용하기 
		let dd_html = ``;
	      for(let i=1; i<=31; i++) {
	         
	         if(i<10) {
	            dd_html += `<option>0\${i}</option>`;
	            <%--
		            // !!!! 중요 !!!!
		            // 확장자가 html 인 문서에서는 백틱 사용시 변수는 ${변수명} 으로 사용하면 된다.
		            // 그런데 확장자가 jsp 인 문서에서는 백틱 사용시 변수를 ${변수명} 으로 사용해 버리면
		            // request영역에 저장된 값을 불러들이는 ${requestScope.키값} 으로 인식해서 
		            // ${변수명}을 request 영역에 저장된 변수를 불러들이는 것으로 사용되어진다.
		            // 그래서 확장자가 jsp 인 문서에서는 백틱 사용시 $앞에 반드시 \를 붙여주어야 올바르게 동작한다.
	            --%>
	         }
	         else {
	            dd_html += `<option>\${i}</option>`;
	         }    
	         
	      }// end of for---------------------
		
	    //console.log("확인용 dd_html => " + dd_html);  
	      
	    $("select#birthdd").html(dd_html);  // 출력하는것을 잘 알아두자 
		
		
	    ////////////////////////////////////////////////////////////////////////////
	    
	    //// ==== JQuery UI 의 datepicker ==== //
	    $("input#datepicker").datepicker({			// 처음에는 영문으로 나타난다.
	    	
	    	//달력을 한글로 바꿔보자
	    	dateFormat: 'yy-mm-dd'  //Input Display Format 변경
                ,showOtherMonths: true   //빈 공간에 현재월의 앞뒤월의 날짜를 표시
                ,showMonthAfterYear:true //년도 먼저 나오고, 뒤에 월 표시
                ,changeYear: true        //콤보박스에서 년 선택 가능
                ,changeMonth: true       //콤보박스에서 월 선택 가능                
                ,showOn: "both"          //button:버튼을 표시하고,버튼을 눌러야만 달력 표시됨. both:버튼을 표시하고,버튼을 누르거나 input을 클릭하면 달력 표시됨.  
                ,buttonImage: "http://jqueryui.com/resources/demos/datepicker/images/calendar.gif" //버튼 이미지 경로
                ,buttonImageOnly: true   //기본 버튼의 회색 부분을 없애고, 이미지만 보이게 함
                ,buttonText: "선택"       //버튼에 마우스 갖다 댔을 때 표시되는 텍스트         /* 489 줄부터 이 줄까지 주석 처리한다면 버튼이 없어진다. */        
                ,yearSuffix: "년"         //달력의 년도 부분 뒤에 붙는 텍스트             
                ,monthNamesShort: ['1','2','3','4','5','6','7','8','9','10','11','12'] //달력의 월 부분 텍스트
                ,monthNames: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월'] //달력의 월 부분 Tooltip 텍스트
                ,dayNamesMin: ['일','월','화','수','목','금','토'] //달력의 요일 부분 텍스트
                ,dayNames: ['일요일','월요일','화요일','수요일','목요일','금요일','토요일'] //달력의 요일 부분 Tooltip 텍스트
              //,minDate: "-1M" //최소 선택일자(-1D:하루전, -1M:한달전, -1Y:일년전)
              //,maxDate: "+1M" //최대 선택일자(+1D:하루후, +1M:한달후, +1Y:일년후)  
	    
	    });     // 생년월일에 달력을 표시해줘서 날짜를 클릭할 수 있게 한다.
	    
	    //초기값을 오늘 날짜로 설정                            ↓ 아무것도 안적으면 defalut 가 오늘 날짜이다.
	    $('input#datepicker').datepicker('setDate',  'today'); //(-1D:하루전, -1M:한달전, -1Y:일년전), (+1D:하루후, +1M:한달후, +1Y:일년후) 
		 
	    
	    
	    /////////////////////////////////////////////////////////////////////////////////////////
	    
	    // ==== 전체 datepicker 옵션 일괄 설정하기 ==== //
	    // 한번의 설정으로 $("input#fromDate").$("input#toDate") 의 옵션을 모두 설정할 수 있다.
	    
	    
	    $(function() {
            //모든 datepicker에 대한 공통 옵션 설정
            $.datepicker.setDefaults({
                 dateFormat: 'yy-mm-dd' //Input Display Format 변경
                ,showOtherMonths: true //빈 공간에 현재월의 앞뒤월의 날짜를 표시
                ,showMonthAfterYear:true //년도 먼저 나오고, 뒤에 월 표시
                ,changeYear: true //콤보박스에서 년 선택 가능
                ,changeMonth: true //콤보박스에서 월 선택 가능                
             // ,showOn: "both" //button:버튼을 표시하고,버튼을 눌러야만 달력 표시됨. both:버튼을 표시하고,버튼을 누르거나 input을 클릭하면 달력 표시됨.  
             // ,buttonImage: "http://jqueryui.com/resources/demos/datepicker/images/calendar.gif" //버튼 이미지 경로
             // ,buttonImageOnly: true //기본 버튼의 회색 부분을 없애고, 이미지만 보이게 함
             // ,buttonText: "선택" //버튼에 마우스 갖다 댔을 때 표시되는 텍스트                
                ,yearSuffix: "년" //달력의 년도 부분 뒤에 붙는 텍스트
                ,monthNamesShort: ['1','2','3','4','5','6','7','8','9','10','11','12'] //달력의 월 부분 텍스트
                ,monthNames: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월'] //달력의 월 부분 Tooltip 텍스트
                ,dayNamesMin: ['일','월','화','수','목','금','토'] //달력의 요일 부분 텍스트
                ,dayNames: ['일요일','월요일','화요일','수요일','목요일','금요일','토요일'] //달력의 요일 부분 Tooltip 텍스트
             // ,minDate: "-1M" //최소 선택일자(-1D:하루전, -1M:한달전, -1Y:일년전)
             // ,maxDate: "+1M" //최대 선택일자(+1D:하루후, -1M:한달후, -1Y:일년후)                    
            });
 
            //input을 datepicker로 선언
            $("input#fromDate").datepicker();                    
            $("input#toDate").datepicker();
            
            //From의 초기값을 오늘 날짜로 설정
            $('input#fromDate').datepicker('setDate', 'today'); //(-1D:하루전, -1M:한달전, -1Y:일년전), (+1D:하루후, +1M:한달후, +1Y:일년후)
            
            //To의 초기값을 3일후로 설정
            $('input#toDate').datepicker('setDate', '+3D'); //(-1D:하루전, -1M:한달전, -1Y:일년전), (+1D:하루후, +1M:한달후, +1Y:일년후)
        });
	    
	    ///////////////////////////////////////////////////////////////////////////
	    
	    // "우편번호찾기" 을 클릭했을 때 이벤트 처리하기
	    $("img#zipcodeSearch").click(function(){
	    	 b_flag_zipcodeSearch_click = true;		// "우편번호찾기"를 클릭한 것을 나타내 준다.
	    });
	    
	    
	    // 우편번호 입력란에 키보드로 입력할 경우 이벤트 처리하기 
	    //$("input:text[id='postcode']").bind("keyup", function() {});    // 또는 $("input:postcode")
		// 또는
		$("input:text[id='postcode']").keyup(function() {
			
			alert(`우편번호 입력은 "우편번호찾기" 를 클릭으로만 됩니다.`);
			// 또는
			//alert("우편번호 입력은 \"우편번호찾기\" 를 클릭으로만 됩니다.");
			
			$(this).val("");    // 화살표 함수가 아니기 때문에 가능 this 는 $("input:text[id='postcode']") 를 말함 
				
		});
		
	    
	    
	    
	    // "아이디" 을 클릭했을 때 이벤트 처리하기 (아이디 중복 확인)   
	    $("img#idcheck").click(function() {
	    	b_flag_idDuplicate_click = true;		// " 아이디중복"을 클릭한 것을 나타내 준다.
	    // "아이디중복확인" 를 클릭했는지 클릭을 안했는지 여부를 알아오기 위한 용도
	    
	    
	    // 입력하고자 하는 아이디가 데이터베이스에 존재하는지, 존재하지 않는지 알아와야 한다.
	    /*
	        Ajax (Asynchronous JavaScript and XML)란?   
	        ==> 이름만 보면 알 수 있듯이 '비동기 방식의 자바스크립트와 XML' 로서
	          Asynchronous JavaScript + XML 인 것이다.
	          한마디로 말하면, Ajax 란? Client 와 Server 간에 XML 데이터를 JavaScript 를 사용하여 비동기 통신으로 주고 받는 기술이다.
	          하지만 요즘에는 데이터 전송을 위한 데이터 포맷방법으로 XML 을 사용하기 보다는 JSON(Javascript Standard Object Notation) 을 더 많이 사용한다. 
	          참고로 HTML은 데이터 표현을 위한 포맷방법이다.
	          그리고, 비동기식이란 어떤 하나의 웹페이지에서 여러가지 서로 다른 다양한 일처리가 개별적으로 발생한다는 뜻으로서, 
	          어떤 하나의 웹페이지에서 서버와 통신하는 그 일처리가 발생하는 동안 일처리가 마무리 되기전에 또 다른 작업을 할 수 있다는 의미이다.
      	*/ 
      	
      	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~암기하기 부분~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
      	// 첫번째 방법(jQuery 를 사용한 Ajax)
      	
	       <%-- 
	        $.ajax({						// 이것은  jQuery에서 ajax 가 함수이다.    /* key 값은 url은 이리로 보내주겠다. */
	        	url:"<%= ctxPath %>/member/idDuplicateCheck.up" ,  
	        	data:{"userid":$("input#userid").val()} ,  // 속성값  항상 객체로 넘어가야 하므로 {} 안에 쓰자    userid 는 getparametrer 해온것(request.getParameter("userid"); ) :input의 userid 의 값을 보내준다. 
	        											   //data 는 /MyMVC/member/idDuplicateCheck.up 로 전송해야할 데이터를 말한다.	
	        	type:"post" ,				        // method 를 쓰면 안된다.  method 대신 type을 사용	(!!!!!!!!!!!!!!!!조심하자!!!!!!!!!!!!!!!!!!!!!!)	
	        									    // type 를 생략하면 type:"get" 이다.
	        	dataType:"json",                    // 결과물 (dataType은 무조건 T는 대문자로 사용)
	        	
										        	// Javascript Standard Object Notation.  dataType은 /MyMVC/member/idDuplicateCheck.up 로 부터 실행되어진 결과물을 받아오는 데이터타입을 말한다.
									                // 만약에 dataType:"xml" 으로 해주면 /MyMVC/member/idDuplicateCheck.up 로 부터 받아오는 결과물은 xml 형식이어야 한다. 
									                // 만약에 dataType:"json" 으로 해주면 /MyMVC/member/idDuplicateCheck.up 로 부터 받아오는 결과물은 json 형식이어야 한다.
	        	
                async:true,      // async:true 는 비동기 방식을 말한다. // async 를 생략하면 기본값이 비동기 방식은 true 이다.
                				 // async:false 는 동기방식을 말한다.  동기방식이므로 순서가 있기 때문에 결과물(/member/idDuplicateCheck.up)을 받아올때까지 대기한다.   
                				 // 지도를 할때는 반드시 동기 방식인 async:false 를 사용해야만 지도가 올바르게 나온다.
                
                				 
                success:function(json) {	// 파라미터 json 에 {"isExists":true} 또는 {"isExists":false} 이 들어오게 된다. 		
                							// () 안에 아무거나 상관 없지만 일반적으로 json 사용
                	
                							
                	//if(json.isExists) 또는 if(json["isExists"])    //json 은 자바스크립트의 객체  (. 표기법, 대괄호 표기법으로 한다.)						
                		console.log(json);
                		console.log(json["isExists"]);
                	 console.log(json.isExists);
                	 if(json["isExists"]){
                		// 입력한 userid 가 이미 사용중 이라면 
                		$("span#idcheckResult").html($("input#userid").val() + "은 중복된 아이디 이므로 사용 불가합니다.").css("color", "red");    // $("input#userid").val() 는 작성해온 값
                		$("input#userid").val("");		// 입력창 초기화 시켜버린다.
                		
                	}
                	else{
                		// 입력한 userid 가 존재하지 않는 경우라면 
                		$("span#idcheckResult").html($("input#userid").val() + "은 사용 가능합니다.").css("color", "navy");    // $("input#userid").val() 는 작성해온 값
                		
                	}
          
                },
                
                // 에러 일때 실행된다.
                error: function(request, status, error){
                    alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
                }
                				 
	        });		
      	--%>
	        
	        
      	// 또는 
      	// 두번째 방법 (jQuery 를 사용한 Ajax)
      		
	        $.ajax({						// 이것은  jQuery에서 ajax 가 함수이다.    /* key 값은 url은 이리로 보내주겠다. */
	        	url:"<%= ctxPath %>/member/idDuplicateCheck.up" ,  
	        	data:{"userid":$("input#userid").val()} ,  // 속성값  항상 객체로 넘어가야 하므로 {} 안에 쓰자    userid 는 getparametrer 해온것(request.getParameter("userid"); ) :input의 userid 의 값을 보내준다. 
	        											   //data 는 /MyMVC/member/idDuplicateCheck.up 로 전송해야할 데이터를 말한다.	
	        	type:"post" ,				        // method 를 쓰면 안된다.  method 대신 type을 사용	(!!!!!!!!!!!!!!!!조심하자!!!!!!!!!!!!!!!!!!!!!!)	
	        									    // type 를 생략하면 type:"get" 이다.
	        	//dataType:"json",                    // 결과물 (dataType은 무조건 T대문자로 사용)
	        	
										        	// Javascript Standard Object Notation.  dataType은 /MyMVC/member/idDuplicateCheck.up 로 부터 실행되어진 결과물을 받아오는 데이터타입을 말한다.
									                // 만약에 dataType:"xml" 으로 해주면 /MyMVC/member/idDuplicateCheck.up 로 부터 받아오는 결과물은 xml 형식이어야 한다. 
									                // 만약에 dataType:"json" 으로 해주면 /MyMVC/member/idDuplicateCheck.up 로 부터 받아오는 결과물은 json 형식이어야 한다.
	        	
									                
                //async:true,      // async:true 는 비동기 방식을 말한다. // async 를 생략하면 기본값이 비동기 방식은 true 이다.
                				 // async:false 는 동기방식을 말한다.  동기방식이므로 순서가 있기 때문에 결과물(/member/idDuplicateCheck.up)을 받아올때까지 대기한다.   
                				 // 지도를 할때는 반드시 동기 방식인 async:false 를 사용해야만 지도가 올바르게 나온다.
                
                				 
                success:function(text) {	// 파라미터 json 에 {"isExists":true} 또는 {"isExists":false} 이 들어오게 된다. 		
                							// () 안에 아무거나 상관 없지만 일반적으로 json 사용
                	
                	//datatype:"json",  이것을 생략하면 
                	// 파라미터 text 에 "{"isExists":false}" 또는 "{"isExists":true}" 은 Object 타입으로 결과물이 들어오게 된다.
                	
                	//datatype:"json",  이것을 생략하지 않고 넣어주면 
                	// 파라미터 text 에 {"isExists":false} 또는 {"isExists":true} 의 객체 타입으로 결과물이 들어오게 된다.
                	
                	
                	const json = JSON.parse(text);
                	// JSON.parse(text); 은 JSON.parse("{"isExists":true}"); 또는 JSON.parse("{"isExists":false}"); 와 같은 것인데
                    // 그 결과물은 {"isExists":true} 또는 {"isExists":false} 와 같은 문자열을 자바스크립트 객체로 변환해주는 것이다. 
                    // 조심할 것은 text 는 반드시 JSON 형식으로 되어진 문자열이어야 한다. 
                	
                	
                	//if(json.isExists) 또는 if(json["isExists"])    //json 은 자바스크립트의 객체  (. 표기법, 대괄호 표기법으로 한다.)						
                		
                	 if(json["isExists"]){
                		// 입력한 userid 가 이미 사용중 이라면 
                		$("span#idcheckResult").html($("input#userid").val() + "은 중복된 아이디 이므로 사용 불가합니다.").css("color", "red");    // $("input#userid").val() 는 작성해온 값
                		$("input#userid").val("");		// 입력창 초기화 시켜버린다.
                		
                	}
                	else{
                		// 입력한 userid 가 존재하지 않는 경우라면 
                		$("span#idcheckResult").html($("input#userid").val() + "은 사용 가능합니다.").css("color", "navy");    // $("input#userid").val() 는 작성해온 값
                		
                	}
                	

                },
                
                // 에러 일때 실행된다.
                error: function(request, status, error){
                    alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
                }
                				 
	        });		
      	
      	
      	
	    });// end of $("img#idcheck").click(function() {
	    	
	    	
	    
	    	
	 	
	    	
	    // 아이디값이 변경되면 가입하기 버튼을 클릭시 "아이디중복확인" 을 클릭했는지 안했는지 알아보기 알아보기 위한 용도를 초기화 시키기
	    
	    // 개념	
	    //leess 클릭함 중복말나옴   ""
	    //eomjh 가입함 		 새롭게 "eomjh" 로 변경해야 하므로  
	    $("input#userid").bind("change", () => {     // change 이벤트   
	    	b_flag_idDuplicate_click = false;
	    });		
	    
	    
 		// 아메알값이 변경되면 가입하기 버튼을 클릭시 "이메일중복확인" 을 클릭했는지 안했는지 알아보기 알아보기 위한 용도를 초기화 시키기
	    
	    // 개념	
	    //leess@naver.com 클릭함 중복말나옴   ""
	    //eomjh@naver.com 가입함 		 새롭게 "eomjh@naver.com" 로 변경해야 하므로  
	    $("input#email").bind("change", () => {     // change 이벤트   
	    	b_flag_emailDuplicate_click = true;
	    });	
	    
	    
	 
        
	   });  // end of $(document).ready(function() {
	      
	   // "이메일 중복확인" 을 클릭했을때 이벤트 처리하기
	      function isExistEmailCheck() {
	         
	         let b_flag_emailDuplicate_click = true;
	         
	             
	         /*
	           Ajax (Asynchronous JavaScript and XML)란?
	           ==> 이름만 보면 알 수 있듯이 '비동기(순차적이지않은) 방식의 자바스크립트와 XML' 로서
	           Asynchronous JavaScript + XML 인 것이다.
	           한마디로 말하면, Ajax 란? Client 와 Server 간에 XML 데이터를 JavaScript 를 사용하여 비동기 통신으로 주고 받는 기술이다.
	           하지만 요즘에는 데이터 전송을 위한 데이터 포맷방법으로 XML 을 사용하기 보다는 JSON(Javascript Standard Object Notation) 을 더 많이 사용한다. 
	           참고로 HTML은 데이터 표현을 위한 포맷방법이다.
	           그리고, 비동기식이란 어떤 하나의 웹페이지에서 여러가지 서로 다른 다양한 일처리가 개별적으로 발생한다는 뜻으로서, 
	           어떤 하나의 웹페이지에서 서버와 통신하는 그 일처리가 발생하는 동안 일처리가 마무리 되기전에 또 다른 작업을 할 수 있다는 의미이다.
	           */ 
	             
	             
	         // 첫번째 방법 (jQuery를 이용한 Ajax)
	           $.ajax({                                              // 이것은  jQuery에서 ajax 가 함수이다.    /* key 값은 url은 이리로 보내주겠다. */
	            url:"<%= ctxPath%>/member/emailDuplicateCheck.up",             
	            // 전송해야할 데이터의 url을 뜻한다.
	            data:{"email":$("input#email").val()},
	            // 데이터는 key : value 로 구성되어 항상 객체로 구성된다.
	            
	            type:"post",
	            // type - 전송할 데이터 방식을 뜻한다. method와 혼동 주의 ! (생략시 get 방식)
	            
	            dataType:"json", // Javascript Standard Object Notation. (스크립트 표준 객체 표기법)
	            // dataType은 /MyMVC/member/idDuplicateCheck.up 로 부터 실행되어진 결과물을 받아오는 데이터타입을 말한다.
	            
	              async:true, // async:true 는 비동기 방식을 말한다. 생략시 true
	              // 지도를 할때는 동기방식인 false를 사용해야만 올바르게 나온다.
	              
	              success:function(json){ // 파라미터 json에 -> {"isExists":true} 또는 {"isExists":false} 들어오게 된다.
	                 // if (json[isExists])
	                   if (json.isExists){
	                    // 입력한 email이 이미 사용중이라면
	                    $("span#emailCheckResult").html($("input#email").val()+" 은 이미 사용중인 email입니다.").css("color","red");
	                    $("input#email").val("");
	                 }else if ( !json.isExists && $("input#email").val().trim() != "") {
	                    $("span#emailCheckResult").html($("input#email").val()+" 은 사용 가능한 email입니다.").css("color","blue");
	                 }
	              },
	              error: function(request, status, error){
	                  alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
	              }                
	         });
	                
	      } // end of function isExistEmailCheck()
	      
	   
	   
	
		
		
	/* 	생년월일 중 일 부분 함수로 사용한 부분이다.
	function func_dd_html() {
		let dd_html = ``;
	      for(let i=1; i<=31; i++) {
	         
	         if(i<10) {
	            dd_html += `<option>0\${i}</option>`;  */
	            /*
	            <%--
		            // !!!! 중요 !!!!
		            // 확장자가 html 인 문서에서는 백틱 사용시 변수는 ${변수명} 으로 사용하면 된다.
		            // 그런데 확장자가 jsp 인 문서에서는 백틱 사용시 변수를 ${변수명} 으로 사용해 버리면
		            // request영역에 저장된 값을 불러들이는 ${requestScope.키값} 으로 인식해서 
		            // ${변수명}을 request 영역에 저장된 변수를 불러들이는 것으로 사용되어진다.
		            // 그래서 확장자가 jsp 인 문서에서는 백틱 사용시 $앞에 반드시 \를 붙여주어야 올바르게 동작한다.
	            --%>
		            */
		            /*
	         }
	         else {
	            dd_html += `<option>\${i}</option>`;
	         }    
	         
	      }// end of for---------------------
		
	    console.log("확인용 dd_html => " + dd_html);  
	      
		document.querySelector("select#birthdd").innerHTML = dd_html;   // 출력해주기 
		
	}; // end of function func_dd_html() {
	*/
	
	// 가입하기 버튼 클릭시 호출되는 함수
	function goRegister() {
		
		// ***** 필수 입력사랑에 모두 입려이 되었는지 검사한다. ***** //
		
		let b_Flag_requiredInfo = false;  // 검사하기 위해 설정 
		
		
		$("input.requiredInfo").each( (index, elmt) => {
		
			if($(elmt).val().trim() == "") {  // 값을 가져온다.
				
				alert("표시된 필수 입력사항은 모두 입력하셔야 합니다.");
			
				b_Flag_requiredInfo = true;
			
				return false;  // each 에서 반복 중단하기 for 문의 break 와 같다.
			
			}	
		});
		
		if(b_Flag_requiredInfo) {
			return;   // 함수종료
		}
		
		
		
		// 성별 radio 선택을 했는지 검사하기 
		const radio_checked_length = $("input:radio[name='gender']:checked").length;   // checked 는 체크 되어졌는지 확인 
		
		if(radio_checked_length == 0) {
			alert("성별을 선택하셔야 합니다.");
			return;  //함수 종료
		}
		
		// 이용약관 동의 체크 박스에 체크를 했나 안했나 검사를 해보자 
		// 값을 넘기지 않아도 되기 때문에 name을 안써도 된다.
		const checkbox_checked_length = $("input:checkbox[id='agree']:checked").length;   // checked 는 체크 되어졌는지 확인 
		
		if(checkbox_checked_length == 0) {
			alert("이용약관에 동의를 하셔야 합니다.");
			return;  //함수 종료
		}
		
		
		
		////////////////////////////////////////////////////////////////////////
		
		
		// "아이디중복확인"을 클릭했는지 여부 알아오기
		if(!b_flag_idDuplicate_click) {
			// "아이디 중복확인"을 안한 경우
			alert("아이디 중복확인을 클릭하셔야 합니다.");
			
			return;   // 함수종료 
		
		}
		
		// "이메일중복확인"을 클릭했는지 여부 알아오기
		if(!b_flag_emailDuplicate_click) {
			// "이메일 중복확인"을 안한 경우
			alert("이메일 중복확인을 클릭하셔야 합니다.");
			
			return;   // 함수종료 
		
		}
		
		
		
		// "우편번호찾기" 을 클릭했는지 여부 알아오기
	    
		if(!b_flag_zipcodeSearch_click) {		// false 라면 
			//"우편번호찾기" 를 클릭 안 했을 경우
			alert("우편번호찾기를 클릭하셔서 우편번호를 입력하셔야 합니다.");
			return;  // 함수 종료
		}
		else {
			// "우편번호찾기" 를 클릭 했을 경우
			
			const regExp = /^\d{5}/g;    // 정규표현식 무조건 숫자 5글자 
			// 숫자 5자리만 들어오도록 검사해주는 정규표현식 객체 생성
			
			const bool = regExp.test($("input:text[id='postcode']").val());
			
			if(!bool)  {   // 숫자가 아니라면 
				alert("우편번호 형식에 맞지 않습니다.");
				$("input:text[id='postcode']").val("");   // 값을 비운다.
				
				b_flag_zipcodeSearch_click = false;     // "우편번호착기"를 클릭을 했다고 판단했던것을 다시 클릭 안했다고 해준다.
														// 잘못 입력이 되었기 때문이다.
				return; // 함수 종료   
			}
			
			// 주소 및 상세주소 입력에 대한 유효성 검사하기는 생략!!!
			
		}
		
		///////////////////////////////////////////////////////////////////////
		
		//submit 하기 위해 
		const frm = document.registerFrm;   
		frm.action = "memberRegister.up";     // 이것을 책임지는것은 member.controller.MemberRegisterAction 이다
		frm.method = "post";  // post 방식으로 가야 한다 이유는 개인 정보를 지키기 위해서 
		frm.submit();
		
		
	}; // end of function
	
</script>


<div class="row" id="divRegisterFrm">
  <div class="col-md-12" align="center">
   <form name="registerFrm">
   
   <table id="tblMemberRegister">
      <thead>
      <tr>
           <th colspan="2" id="th">::: 회원가입 (<span style="font-size: 10pt; font-style: italic;"><span class="star">*</span>표시는 필수입력사항</span>) :::</th>
      </tr>
      </thead>
      <tbody>
      <tr>
         <td style="width: 20%; font-weight: bold;">성명&nbsp;<span class="star">*</span></td>
         <td style="width: 80%; text-align: left;">
             <input type="text" name="name" id="name" class="requiredInfo" /> 
            <span class="error">성명은 필수입력 사항입니다.</span>
         </td>
      </tr>
      <tr>
         <td style="width: 20%; font-weight: bold;">아이디&nbsp;<span class="star">*</span></td>
         <td style="width: 80%; text-align: left;">
             <input type="text" name="userid" id="userid" class="requiredInfo" />&nbsp;&nbsp;
             <!-- 아이디중복체크 -->
             <img id="idcheck" src="../images/b_id_check.gif" style="vertical-align: middle;" />
             <span id="idcheckResult"></span>
             <span class="error">아이디는 필수입력 사항입니다.</span>
         </td> 
      </tr>
      <tr>
         <td style="width: 20%; font-weight: bold;">비밀번호&nbsp;<span class="star">*</span></td>
         <td style="width: 80%; text-align: left;"><input type="password" name="pwd" id="pwd" class="requiredInfo" />
            <span class="error">암호는 영문자,숫자,특수기호가 혼합된 8~15 글자로 입력하세요.</span>
         </td>
      </tr>
      <tr>
         <td style="width: 20%; font-weight: bold;">비밀번호확인&nbsp;<span class="star">*</span></td>
         <td style="width: 80%; text-align: left;"><input type="password" id="pwdcheck" class="requiredInfo" /> 
            <span class="error">암호가 일치하지 않습니다.</span>
         </td>
      </tr>
      <tr>
         <td style="width: 20%; font-weight: bold;">이메일&nbsp;<span class="star">*</span></td>
         <td style="width: 80%; text-align: left;"><input type="text" name="email" id="email" class="requiredInfo" placeholder="abc@def.com" /> 
             <span class="error">이메일 형식에 맞지 않습니다.</span>
             
             <%-- ==== 퀴즈 시작 ==== --%>
             <span style="display: inline-block; width: 80px; height: 30px; border: solid 1px gray; border-radius: 5px; font-size: 8pt; text-align: center; margin-left: 10px; cursor: pointer;" onclick="isExistEmailCheck();">이메일중복확인</span> 
             <span id="emailCheckResult"></span>
             <%-- ==== 퀴즈 끝 ==== --%>
         </td>
      </tr>
      <tr>
         <td style="width: 20%; font-weight: bold;">연락처</td>
         <td style="width: 80%; text-align: left;">
             <input type="text" id="hp1" name="hp1" size="6" maxlength="3" value="010" readonly />&nbsp;-&nbsp;   <!-- readonly 는 변경할 수 없게 한다. -->
             <input type="text" id="hp2" name="hp2" size="6" maxlength="4" />&nbsp;-&nbsp;
             <input type="text" id="hp3" name="hp3" size="6" maxlength="4" />
             <span class="error">휴대폰 형식이 아닙니다.</span>
         </td>
      </tr>
      <tr>
         <td style="width: 20%; font-weight: bold;">우편번호</td>
         <td style="width: 80%; text-align: left;">
            <input type="text" id="postcode" name="postcode" size="6" maxlength="5" />&nbsp;&nbsp;
            <%-- 우편번호 찾기 --%>
            <img id="zipcodeSearch" src="../images/b_zipcode.gif" style="vertical-align: middle;" />
            <span class="error">우편번호 형식이 아닙니다.</span>
         </td>
      </tr>
      <tr>
         <td style="width: 20%; font-weight: bold;">주소</td>
         <td style="width: 80%; text-align: left;">
            <input type="text" id="address" name="address" size="40" placeholder="주소" /><br/>
            <input type="text" id="detailAddress" name="detailAddress" size="40" placeholder="상세주소" />&nbsp;<input type="text" id="extraAddress" name="extraAddress" size="40" placeholder="참고항목" /> 
            <span class="error">주소를 입력하세요</span>
         </td>
      </tr>
      
      <tr>
         <td style="width: 20%; font-weight: bold;">성별</td>
         <td style="width: 80%; text-align: left;">
            <input type="radio" id="male" name="gender" value="1" /><label for="male" style="margin-left: 2%;">남자</label>
            <input type="radio" id="female" name="gender" value="2" style="margin-left: 10%;" /><label for="female" style="margin-left: 2%;">여자</label>
         </td>
      </tr>
      
      <tr>
         <td style="width: 20%; font-weight: bold;">생년월일</td>
         <td style="width: 80%; text-align: left;">
            <input type="number" id="birthyyyy" name="birthyyyy" min="1950" max="2050" step="1" value="1995" style="width: 80px;" required />
            
            <select id="birthmm" name="birthmm" style="margin-left: 2%; width: 60px; padding: 8px;">
            <%--    
               <option value ="01">01</option>
               <option value ="02">02</option>
               <option value ="03">03</option>
               <option value ="04">04</option>
               <option value ="05">05</option>
               <option value ="06">06</option>
               <option value ="07">07</option>
               <option value ="08">08</option>
               <option value ="09">09</option>
               <option value ="10">10</option>
               <option value ="11">11</option>
               <option value ="12">12</option>
             --%>   
            </select> 
            
            <select id="birthdd" name="birthdd" style="margin-left: 2%; width: 60px; padding: 8px;">
            <%--    
               <option value ="01">01</option>
               <option value ="02">02</option>
               <option value ="03">03</option>
               <option value ="04">04</option>
               <option value ="05">05</option>
               <option value ="06">06</option>
               <option value ="07">07</option>
               <option value ="08">08</option>
               <option value ="09">09</option>
               <option value ="10">10</option>
               <option value ="11">11</option>
               <option value ="12">12</option>
               <option value ="13">13</option>
               <option value ="14">14</option>
               <option value ="15">15</option>
               <option value ="16">16</option>
               <option value ="17">17</option>
               <option value ="18">18</option>
               <option value ="19">19</option>
               <option value ="20">20</option>
               <option value ="21">21</option>
               <option value ="22">22</option>
               <option value ="23">23</option>
               <option value ="24">24</option>
               <option value ="25">25</option>
               <option value ="26">26</option>
               <option value ="27">27</option>
               <option value ="28">28</option>
               <option value ="29">29</option>
               <option value ="30">30</option>
               <option value ="31">31</option>
             --%>   
            </select> 
         </td>
      </tr>
      
      <tr>
         <td style="width: 20%; font-weight: bold;">생년월일</td>
         <td style="width: 80%; text-align: left;">
            <input type="text" id="datepicker">
         </td>
      </tr>
      
      <tr>
         <td style="width: 20%; font-weight: bold;">재직기간</td>
         <td style="width: 80%; text-align: left;">
            From: <input type="text" id="fromDate">&nbsp;&nbsp; 
            To: <input type="text" id="toDate">
         </td>
      </tr>
         
      <tr>
         <td colspan="2">
            <label for="agree">이용약관에 동의합니다</label>&nbsp;&nbsp;<input type="checkbox" id="agree" />
         </td>
      </tr>
      <tr>
         <td colspan="2" style="text-align: center; vertical-align: middle;">
            <!-- 상대경로  <iframe src="../iframeAgree/agree.html" width="85%" height="150px" class="box" ></iframe> -->
            <!-- 절대경로 --><iframe src="<%= ctxPath %>/iframeAgree/agree.html" width="85%" height="150px" class="box" ></iframe>
         </td>
      </tr>
      <tr>
         <td colspan="2" style="line-height: 90px;" class="text-center">
            <%-- 
            <button type="button" id="btnRegister" style="background-image:url('<%= ctxPath%>/images/join.png'); border:none; width: 135px; height: 34px; margin-left: 30%;" onClick="goRegister();"></button> 
            --%>
            <input type="button" id="btnRegister" class="btn btn-dark btn-lg" onclick="goRegister()" value="가입하기"></input>
         </td>
      </tr>
      </tbody>
   </table>
   </form>
  </div>
</div>


<jsp:include page="../footer.jsp"></jsp:include>				=<!-- footer 불러오기-->


			