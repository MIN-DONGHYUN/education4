package member.model;

import java.util.Calendar;

public class MemberVO {
	
	// 아래 부분은 insert 부분이다. DB의 회원가입 insert 용 
	private String userid;             // 회원아이디
	private String pwd;                // 비밀번호 (SHA-256 암호화 대상)
	private String name;               // 회원명
	private String email;              //이메일 (AES-256 암호화/복호화 대상)
	private String mobile;             // 연락처 (AES-256 암호화/복호화 대상) 
	private String postcode;           // 우편번호
	private String address;            // 주소
	private String detailaddress;      // 상세주소
	private String extraaddress;       // 참고항목
	private String gender;             // 성별   남자:1  / 여자:2
	private String birthday;           // 생년월일   
	private int coin;                  // 코인액
	private int point;                 // 포인트 
	private String registerday;        // 가입일자 
	private String lastpwdchangedate;  // 마지막으로 암호를 변경한 날짜  
	private int status;                // 회원탈퇴유무   1: 사용가능(가입중) / 0:사용불능(탈퇴) 
	private int idle;                  // 휴면유무      0 : 활동중  /  1 : 휴면중 
									   // 마지막으로 로그인 한 날짜 시간이 현재시각으로 부터 1년이 지났으면 휴먼으로 지정
	
	
	////////////////////////////////////////////////////////////////////////////////////////
	
	
	// select 부분을 말한다.   로그인할때 불러오는 곳이다. 
	private boolean requirepwdChange = false;   // 페스워드 변경을 요구하는것은 기본값 false 로 준다.
	// 마지막으로 암호를 변경한 날짜가 현재시각으로 부터 3개월이 지났으면 false 대신 true 로 변경할 것이다. 
	// 마지막으로 암호를 변경한 날짜가 현재시각으로 부터 3개월이 지나지 않았으면 그대로 false 로 해줄 것이다. 
									
	
	////////////////////////////////////////////////////////////////////////////////////////
	
	// 기본 생성자(필수) 
	public MemberVO() {}
	
	
	public MemberVO(String userid, String pwd, String name, String email, String mobile, String postcode, 
			        String address, String detailaddress, String extraaddress, String birthday) {
		this.userid = userid;
		this.pwd = pwd;
		this.name = name;
		this.email = email;
		this.mobile = mobile;
		this.postcode = postcode;
		this.address = address;
		this.detailaddress = detailaddress;
		this.extraaddress = extraaddress;
		this.birthday = birthday;
	}
	
	// 생성자 생성 
	public MemberVO(String userid, String pwd, String name, String email, String mobile,
			String postcode, String address, String detailaddress, String extraaddress,
			String gender, String birthday) {
		
		this.userid = userid;
		this.pwd = pwd;
		this.name = name;
		this.email = email;
		this.mobile = mobile;
		this.postcode = postcode;
		this.address = address;
		this.detailaddress = detailaddress;
		this.extraaddress = extraaddress;
		this.gender = gender;
		this.birthday = birthday;
	}

	
	// GET/ SET
	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDetailaddress() {
		return detailaddress;
	}

	public void setDetailaddress(String detailaddress) {
		this.detailaddress = detailaddress;
	}

	public String getExtraaddress() {
		return extraaddress;
	}

	public void setExtraaddress(String extraaddress) {
		this.extraaddress = extraaddress;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public int getCoin() {
		return coin;
	}

	public void setCoin(int coin) {
		this.coin = coin;
	}

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}

	public String getRegisterday() {
		return registerday;
	}

	public void setRegisterday(String registerday) {
		this.registerday = registerday;
	}

	public String getLastpwdchangedate() {
		return lastpwdchangedate;
	}

	public void setLastpwdchangedate(String lastpwdchangedate) {
		this.lastpwdchangedate = lastpwdchangedate;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getIdle() {
		return idle;
	}

	public void setIdle(int idle) {
		this.idle = idle;
	}

	public boolean isRequirepwdChange() {
		return requirepwdChange;
	}

	public void setRequirepwdChange(boolean requirepwdChange) {
		this.requirepwdChange = requirepwdChange;
	}
	
	////////////////////////////////////////////////////////////////////////
	
	// 필요한 매소드 만들기 
	
	// 나이 
	public int getAge() {				// JSP 는 항상 get다음 대문자 로 시작해야 한다.
		
		int age = 0;
		
		// 현재 날짜와 시간을 얻어온다.
		Calendar currentDate =  Calendar.getInstance();
		
		// 현재년도를 알아온다.
		int currentYear = currentDate.get(Calendar.YEAR);
		
		age = currentYear = Integer.parseInt(birthday.substring(0, 4)) + 1;   // 나이 
		
		return age;
	}
	
	
	
}
