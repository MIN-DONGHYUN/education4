package member.model;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.sql.*;
import java.util.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import util.security.AES256;
import util.security.SecretMyKey;
import util.security.Sha256;


public class MemberDAO implements InterMemberDAO {
	
	private DataSource ds;    // DataSource ds 는 아파치톰켓이 제공하는 DBCP(DB Connection Pool) 이다.
	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;
	
	// 필드에 넣기 
	private AES256 aes;
	
	// 사용한 자원을 반납하는 close() 매소드 생성하기
	private void close() {
		try {
			if(rs != null) { rs.close(); rs=null;}
			if(pstmt != null) { pstmt.close(); pstmt=null;}
			if(conn != null) { conn.close(); conn=null;}
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	// 생성자
	public MemberDAO() {
		
		try {
			Context initContext = new InitialContext();
		    Context envContext  = (Context)initContext.lookup("java:/comp/env");
		    ds = (DataSource)envContext.lookup("jdbc/myoracle");      // lookup(이름) 이 중요한 것이다. web.xml 부분의 이름, context.xml 이름과 모두 같아야 한다. 이 것이 배치 서술자인 web.xml 로 가고 context.xml 로 간다. 
	
		    aes = new AES256(SecretMyKey.KEY);  // 생성자 
		    // SecretMyKey.KEY 는 우리가 만든 암호화/ 복호화 키이다.
		    
		    
		} catch(NamingException e) {
			e.printStackTrace();
		} catch(UnsupportedEncodingException e) {
			e.printStackTrace();
		}
			
	    
	}
	
	
	// 회원 가입을 해주는 메소드 (tbl_member 테이블에 insert) 해준다.
	@Override
	public int registerMember(MemberVO member) throws SQLException {
		
		int result = 0;
		
		try {
			conn = ds.getConnection();   // date source 에서 가져옴
			
			String sql = " insert into tbl_member(userid, pwd, name, email, mobile, postcode, address, detailaddress, extraaddress, gender, birthday) "
					   + " values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
			
			// 우편배달부
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, member.getUserid());
			//pstmt.setString(2, member.getPwd());					//"qwer1234$"  // 암호화가 안된 상태로 들어간다. 그러므로 암호화를 해주는 작업을 해야 한다.
			pstmt.setString(2, Sha256.encrypt(member.getPwd()) );	// 파일 Sha256.java 로 암호화 해줌  (암호를 단방향을 통해 복호화 안함) (암호를 SHA256 알고리즘으로 단방향 암호화 시킨다.)
			pstmt.setString(3, member.getName());
			//pstmt.setString(4, member.getEmail());                // 암호화가 안되어진 email
			pstmt.setString(4, aes.encrypt(member.getEmail()) );    // 암호화가 되어진 email    (이메일을 양방향을 통해 복호화도 해준다.) (이메일을 AES256 알고리즘으로 양방향 암호화 시킨다.)
			pstmt.setString(5, aes.encrypt(member.getMobile()) ); 	// (휴대폰 번호를 AES256 알고리즘으로 양방향 암호화 시킨다.)
			pstmt.setString(6, member.getPostcode());
			pstmt.setString(7, member.getAddress());
			pstmt.setString(8, member.getDetailaddress());
			pstmt.setString(9, member.getExtraaddress());
			pstmt.setString(10, member.getGender());
			pstmt.setString(11, member.getBirthday());
			
			result = pstmt.executeUpdate();  // DML 문이므로 executeUpdate() 사용 정상이라면 1값이 나오게 된다.
			
	
		} catch ( GeneralSecurityException | UnsupportedEncodingException e) {			// 오류 두개를 같이 잡을때 | 는 or를 뜻한다.
			e.printStackTrace();
		}
		finally {
			close();
		}

		return result;
	}// end of public int registerMember(MemberVO member) throws SQLException {


	// ID 중복검사  tbl_member 테이블에서 userid 가 존재하면  , user id 가 존재하지 않으면 false 를 리턴한다.
	@Override
	public boolean idDuplicatecheck(String userid) throws SQLException {
		
		boolean isExists = false;
		
		// 존재하면 true 없으면 false
		
		
		try {
			
			conn = ds.getConnection();   // date source 에서 가져옴
			
			String sql = " select userid "
					   + " from tbl_member "
					   + " where userid = ? ";
			
			// 우편배달부
			pstmt = conn.prepareStatement(sql);
			
			// 위치 홀더
			pstmt.setString(1, userid);
			
			rs = pstmt.executeQuery();
			
			
			isExists = rs.next();				// 행이 있으면 true 가 나온다. 그니까 있는지 없는지 검사하는 것이다. 
												// 행이 있으면 (중복된 userid) true;
												// 행이 없으면 (사용가능 userid) false;
			
		} finally {
			close();
		}

		return isExists;
	}// end of public boolean idDuplicatecheck(String userid) throws SQLException {


	// email 중복검사  tbl_member 테이블에서 email 가 존재하면  , email가 존재하지 않으면 false 를 리턴한다.
	// 이메일은 암호화가 되었기 때문에 값을 비교할때 암호화를 또 해주거나 암호화된 원래 값을 복호화 해서 찾아준다.   (암호화가 더 쉽게 비교할 수 있음, 복호화는 복잡함)
	@Override
	public boolean emailDuplicatecheck(String email) throws SQLException {
		
		boolean isExists = false;
		
		// 존재하면 true 없으면 false
		
		
		try {
			
			conn = ds.getConnection();   // date source 에서 가져옴
			
			String sql = " select email "
					   + " from tbl_member "
					   + " where email = ? ";
			
			// 우편배달부
			pstmt = conn.prepareStatement(sql);
			
			// 위치 홀더
			pstmt.setString(1, aes.encrypt(email));			// 암호화를 해줘 값을 넘겨주어 암호화된 이메일을 찾는다.
			
			rs = pstmt.executeQuery();
			
			
			isExists = rs.next();				// 행이 있으면 true 가 나온다. 그니까 있는지 없는지 검사하는 것이다. 
												// 행이 있으면 (중복된 email) true;
												// 행이 없으면 (사용가능 email) false;
		} catch ( GeneralSecurityException | UnsupportedEncodingException e) {			// 오류 두개를 같이 잡을때 | 는 or를 뜻한다.
			e.printStackTrace();
		} finally {
			close();
		}

		return isExists;
	} // end of public boolean emailDuplicatecheck(String email) throws SQLException {

	
	
	// 입력받은 HashMap(paraMAp) 을 가지고 한명의 회원정보를 리턴시켜주는 매소드(로그인 처리)
	@Override
	public MemberVO selectOneMember(Map<String, String> paraMap) throws SQLException {

		MemberVO member = null;  // 있으면 null에서 변경될 것이다.
		
	try {
		
		conn = ds.getConnection();
		
		String sql = " select userid, name, email, mobile, postcode, address, detailaddress, extraaddress, gender, "
				+ "           birthyyyy , birthmm, birthdd, coin, point, registerday, "
				+ "           pwdchangegap ,  "
				+ "           NVL(lastlogingap, trunc(months_Between(sysdate, registerday)))  AS lastlogingap     "
				+ "    from  "
				+ " ( "
				+ "    select userid, name, email, mobile, postcode, address, detailaddress, extraaddress, gender "
				+ "          , substr(birthday, 1 , 4) AS birthyyyy , substr(birthday, 6 , 2) AS birthmm , substr(birthday, 9) AS birthdd "
				+ "          , coin, point, to_char(registerday, 'yyyy-mm-dd') AS registerday "
				+ "          , trunc(months_between(sysdate, lastpwdchangedate), 0) AS pwdchangegap  "
				+ "    from tbl_member "
				+ "    where status = 1 and userid = ? and pwd = ? "
				+ " ) M " 
				+ " CROSS JOIN "
				+ " ( "
				+ "    select trunc(months_Between(sysdate, max(logindate)),0) AS lastlogingap "
				+ "    from tbl_loginhistory "
				+ "    where fk_userid = ? "
				+ " ) H ";
		
		pstmt = conn.prepareStatement(sql);
		
		pstmt.setString(1, paraMap.get("userid"));
		pstmt.setString(2, Sha256.encrypt(paraMap.get("pwd")));
		pstmt.setString(3, paraMap.get("userid"));
		
		rs = pstmt.executeQuery();
		
		// select 되어진것이 있냐?
		if(rs.next()) {
			member = new MemberVO();
			
			member.setUserid(rs.getString(1));
			member.setName(rs.getString(2));
			member.setEmail( aes.decrypt(rs.getString(3)));   // 암호화 되어진 이메일 을 복호화 한다.
			member.setMobile( aes.decrypt(rs.getString(4)));  // 휴대폰 도 복호화 한다.
			member.setPostcode(rs.getString(5));
			member.setAddress(rs.getString(6));
			member.setDetailaddress(rs.getString(7));
			member.setExtraaddress(rs.getString(8));
			member.setGender(rs.getString(9));
			member.setBirthday(rs.getString(10) + rs.getString(11) + rs.getString(12));    // 생년월일을 합쳐서 보자 
			member.setCoin(rs.getInt(13));
			member.setPoint(rs.getInt(14));
			member.setRegisterday(rs.getString(15));
			
			
			// 암호 변경이 3개월이 지났다면 
			if(rs.getInt(16) >= 3) {   // 또는  rs.getInt("PWDCHANGEGAP")
				// 마지막으로 암호를 변경한 날짜가 현재시각으로 부터 3개월이 지났으면 false 대신 true 로 변경할 것이다. 
				// 마지막으로 암호를 변경한 날짜가 현재시각으로 부터 3개월이 지나지 않았으면 그대로 false 로 해줄 것이다. 
				
				member.setRequirepwdChange(true);   // false 였던 것을 true 로 바꿔줌  즉 로그인 시 암호를 변경해라는 alert 를 띄우도록 할 때 사용한다.
			}	
			
			// 전 로그인이 12개월 전 이상이라면 
			if(rs.getInt(17) >= 12) {   // 또는  rs.getInt("LASTLOGINGAP")
			
				// 마지막으로 로그인 한 날짜 시간이 현재시각으로 부터 1년이 지났으면 휴먼으로 지정
				member.setIdle(1);    // 1 값으로 변경한다.
				
				// === tbl_member 테이블의 idle 컬럼의 값을 1로 변경하기 update 해야함 === 
				sql = " update tbl_member set idle = 1 "
					+ " where userid = ? ";
				
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setString(1, paraMap.get("userid"));
				
				pstmt.executeUpdate();
				
			}
			
			// === tbl_loginhistory(로그인기록) 테이블에 insert 하기 ====
			// 휴면 회원이 아닐때만 로그인 기록을 주어야 한다.			
			if(member.getIdle() != 1) {
				
				sql = " insert into tbl_loginhistory(fk_userid, clientip) "
					+ " values(?, ?) "; 
				
				// 우편배달부
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setString(1, paraMap.get("userid"));
				pstmt.setString(2, paraMap.get("clientip"));
				
				pstmt.executeUpdate();
				
			}
			
			
			
		} // end of if(rs.next()) {
		
			
			
	} catch ( GeneralSecurityException | UnsupportedEncodingException e) {			// 오류 두개를 같이 잡을때 | 는 or를 뜻한다.
		e.printStackTrace();
	} finally { 
		close();
	}
		
		
		return member;
	} // end of public MemberVO selectOneMember(Map<String, String> paraMap) throws SQLException {


	
	// 아이디 찾기(성명, 이메일을 입력받아서 해당 사용자의 아이디를 알려준다.)
	@Override
	public String findUserid(Map<String, String> paraMap) throws SQLException {
		
		String userid = null;
		
		try {
			
			conn = ds.getConnection();   // date source 에서 가져옴
			
			String sql = " select userid "
					   + " from tbl_member "
					   + " where status = 1 and name = ? and email = ? ";
			
			
			// 우편배달부
			pstmt = conn.prepareStatement(sql);
			
			// 위치 홀더
			pstmt.setString(1, paraMap.get("name"));   //name 키값을써서 name 을 가져오자	
			pstmt.setString(2, aes.encrypt(paraMap.get("email")) );  // email 키 값을 써서 email 을 가져올껀데 암호화가 되어 있으므로 암호화 하여 비교한다.
			
			rs = pstmt.executeQuery();
			
			// selete 된 값이 있다면  
			if(rs.next()) {
				userid = rs.getString(1);    // 첫번째 컬럼을 userid 에 넣는다.
			}
			// selete 값이 없다면 null 이 넘어간다. 
			
					
		} catch ( GeneralSecurityException | UnsupportedEncodingException e) {			// 오류 두개를 같이 잡을때 | 는 or를 뜻한다.
			e.printStackTrace();
		} finally {
			close();
		}
		
		
		return userid;
	} // end of public String findUserid(Map<String, String> paraMap) throws SQLException {


	
	// 비밀번호 찾기(아이디, 이메일을 입력받아서 해당 사용자가 존재하는지 유뮤를 알려준다.)
	@Override
	public boolean isUserExist(Map<String, String> paraMap) throws SQLException {
		
		boolean isUserExist = false;
		
		try {
			
			conn = ds.getConnection();   // date source 에서 가져옴
			
			String sql = " select userid "
					   + " from tbl_member "
					   + " where status = 1 and userid = ? and email = ? ";
			
			
			// 우편배달부
			pstmt = conn.prepareStatement(sql);
			
			// 위치 홀더
			pstmt.setString(1, paraMap.get("userid"));   //name 키값을써서 name 을 가져오자	
			pstmt.setString(2, aes.encrypt(paraMap.get("email")) );  // email 키 값을 써서 email 을 가져올껀데 암호화가 되어 있으므로 암호화 하여 비교한다.
			
			rs = pstmt.executeQuery();
			
			
			isUserExist = rs.next();    // 다음 행이 있느냐 ? 있으면 true 없으면 false
			
					
		} catch ( GeneralSecurityException | UnsupportedEncodingException e) {			// 오류 두개를 같이 잡을때 | 는 or를 뜻한다.
			e.printStackTrace();
		} finally {
			close();
		}
		
		
		return isUserExist;
	} // end of public boolean isUserExist(Map<String, String> paraMap) throws SQLException {


	// 암호변경하기 
	@Override
	public int pwdUpdate(Map<String, String> paraMap) throws SQLException {
		
		int result = 0;
		
		try {
			conn = ds.getConnection();   // date source 에서 가져옴
			
			String sql = " update tbl_member set pwd = ? "
					   + "                      ,lastpwdchangedate = sysdate "
					   + " where userid = ? ";
			
			// 우편배달부
			pstmt = conn.prepareStatement(sql);
			
			// 위치 홀더
			pstmt.setString(1, Sha256.encrypt(paraMap.get("pwd")) );
			pstmt.setString(2, paraMap.get("userid") );
			
			result = pstmt.executeUpdate();  // DML 문이므로 executeUpdate() 사용 정상이라면 1값이 나오게 된다.
			
	
		} 
		finally {
			close();
		}

		return result;
		
	} // end of public int pwdUpdate(Map<String, String> paraMap) throws SQLException {


	// DB에 코인 및 포인트 증가하기
	@Override
	public int coinUpdate(Map<String, String> paraMap) throws SQLException {
		
		int result = 0;
		
		try {
			conn = ds.getConnection();   // date source 에서 가져옴
			
			// 코인과 포인트에 현재 있는거 더하기 값 들어오는걸 한다.
			String sql = " update tbl_member set coin = coin + ? "
					   + "                      , point = point + ? "
					   + " where userid = ? ";
			
			// 우편배달부
			pstmt = conn.prepareStatement(sql);
			
			// 위치 홀더
			pstmt.setInt(1, Integer.parseInt(paraMap.get("coinmoney")) );	       // 코인 충전
			pstmt.setInt(2, (int) (Integer.parseInt(paraMap.get("coinmoney")) * 0.01 ));   // 포인트 충전 
			pstmt.setString(3, paraMap.get("userid"));
			
			
			result = pstmt.executeUpdate();  // DML 문이므로 executeUpdate() 사용 정상이라면 1값이 나오게 된다.
			
	
		} 
		finally {
			close();
		}

		return result;

	}

	
	// 회원의 개인 정보 변경하기
	@Override
	public int updateMember(MemberVO member) throws SQLException {
		
			int result = 0;
		
		try {
			conn = ds.getConnection();   // date source 에서 가져옴
			
			// 코인과 포인트에 현재 있는거 더하기 값 들어오는걸 한다.
			String sql = " update tbl_member set  name = ?"
					   + "                      , pwd = ? "
					   + "                      , email = ? "
					   + "                      , mobile = ? "
					   + "                      , postcode = ? "
					   + "                      , address = ? "
					   + "                      , detailaddress = ? "
					   + "                      , extraaddress = ? "
					   + "                      , birthday = ? "
					   + "                      , lastpwdchangedate = sysdate "
					   + " where userid = ? ";
			
			// 우편배달부
			pstmt = conn.prepareStatement(sql);
			
			// 위치 홀더
			pstmt.setString(1, member.getName());					//"qwer1234$"  // 암호화가 안된 상태로 들어간다. 그러므로 암호화를 해주는 작업을 해야 한다.
			pstmt.setString(2, Sha256.encrypt(member.getPwd()) );	// 파일 Sha256.java 로 암호화 해줌  (암호를 단방향을 통해 복호화 안함) (암호를 SHA256 알고리즘으로 단방향 암호화 시킨다.)			
			pstmt.setString(3, aes.encrypt(member.getEmail()) );    // 암호화가 되어진 email    (이메일을 양방향을 통해 복호화도 해준다.) (이메일을 AES256 알고리즘으로 양방향 암호화 시킨다.)
			pstmt.setString(4, aes.encrypt(member.getMobile()) ); 	// (휴대폰 번호를 AES256 알고리즘으로 양방향 암호화 시킨다.)
			pstmt.setString(5, member.getPostcode());
			pstmt.setString(6, member.getAddress());
			pstmt.setString(7, member.getDetailaddress());
			pstmt.setString(8, member.getExtraaddress());
			pstmt.setString(9, member.getBirthday());
			pstmt.setString(10, member.getUserid());
			
			
			result = pstmt.executeUpdate();  // DML 문이므로 executeUpdate() 사용 정상이라면 1값이 나오게 된다.
			
	
		} catch ( GeneralSecurityException | UnsupportedEncodingException e) {			// 오류 두개를 같이 잡을때 | 는 or를 뜻한다.
			e.printStackTrace();
		}
		finally {
			close();
		}

		return result;
	}


	// 암호 변경시 현재 사용중인 암호인지 아닌지 알아오기
	@Override
	public int duplicatePwdCheck(Map<String, String> paraMap) throws SQLException {
		
		int n = 0;
		
		try {
			
			conn = ds.getConnection();   // date source 에서 가져옴
			
			String sql = " select count(*) "
					   + " from tbl_member "
					   + " where userid = ? and pwd = ? ";
			
			
			// 우편배달부
			pstmt = conn.prepareStatement(sql);
			
			// 위치 홀더
			pstmt.setString(1, paraMap.get("userid"));   //name 키값을써서 name 을 가져오자	
			pstmt.setString(2, Sha256.encrypt(paraMap.get("new_pwd")));  // 암호를 가져오자 
			
			rs = pstmt.executeQuery();
			
			rs.next();
			
			n = rs.getInt(1);    // 다음 행이 있느냐 ? 있으면 true 없으면 false
			
					
		} finally {
			close();
		}
		
		
		return n;
	} // end of public int duplicatePwdCheck(Map<String, String> paraMap) throws SQLException {


	// *** 페이징 처리를 안한 모든 회원 또는 검색한 회원 목록 보여주기 *** //
	@Override
	public List<MemberVO> selectMember(Map<String, String> paraMap) throws SQLException {

		List<MemberVO> memberList = new ArrayList<>();  
		
		try {
			
			conn = ds.getConnection();
			
			String sql = " select userid, name, email, gender "
					   + " from tbl_member"
					   + " where userid != 'admin' ";
			
			
			String colname = paraMap.get("searchType"); 		// Map 에 있는것을 꺼내본다. 검색 분류를 가져온다.
			String searchWord = paraMap.get("searchWord");      // 검색어를 가져온다.
			
			if("email".equals(colname)) {
				// 검색 대상이 이메일인 경우 
				// 암호화가 되어있으므로 이메일을 암호화 해서 위치홀더에 넣기 위해 한다. 
				searchWord = aes.encrypt(searchWord);
			}
			
			// 만약 Map 에 저장되어있는 값이 있다면 sql 문을 추가해줘야 한다. 
			if( searchWord != null && !searchWord.trim().isEmpty())   // searchWord 가 null 이 아니고 공백이 아니라면 
			{
				sql += " and " + colname + " like '%'|| ? || '%' ";    // ? 위치홀더에는 searchWord 가 올것이다.  위치홀더에는 무조건 데이터 값만 쓴다, 컬럼명 등을 쓰면 절대로 나오지 않는다.
				// 컬럼명과 테이블명은 위치홀더(?)로 사용하면 안된다.!!!!!
				// 위치홀더(?)로 들어오는 것은 오로지 컬럼명과 테이블명이 아닌 오로지 데이터 값만 들어온다.
			
			}
			
			sql += " order by registerday desc ";   // 검색에 있든 없든 최근에 가입한 회원부터 보여주기 위해 설정 
			
			
			
			pstmt = conn.prepareStatement(sql);
			
			if( searchWord != null && !searchWord.trim().isEmpty())   // searchWord 가 null 이 아니고 공백이 아니라면 그때만 위치 홀더가 있으니까 if문을 준다,
			{
				pstmt.setString(1, searchWord);
			}
			
			rs = pstmt.executeQuery();
			
			// while 로 반복 
			while(rs.next()) {
				MemberVO member = new MemberVO();
				
				member.setUserid(rs.getString(1));
				member.setName(rs.getString(2));
				member.setEmail( aes.decrypt(rs.getString(3)));   // 암호화 되어진 이메일 을 복호화 한다.				
				member.setGender(rs.getString(4));
				
				// memberList 에 저장하기 위해
				memberList.add(member);
				
			} // end of while(rs.next()) {
			
				
				
		} catch ( GeneralSecurityException | UnsupportedEncodingException e) {			// 오류 두개를 같이 잡을때 | 는 or를 뜻한다.
			e.printStackTrace();
		} finally { 
			close();
		}
			
			
		return memberList;
		
		
	} // end of 	public List<MemberVO> selectMember(Map<String, String> paraMap) throws SQLException {


	// *** 페이징 처리를 한 모든 회원 또는 검색한 회원 목록 보여주기 *** //
	@Override
	public List<MemberVO> selectPagingMember(Map<String, String> paraMap) throws SQLException {
		
		List<MemberVO> memberList = new ArrayList<>();  
		
		try {
			
			conn = ds.getConnection();
			
			String sql = " select userid, name, email, gender "
					   + " from "
					   + " ( "
					   + "    select rownum AS RNO, userid, name, email, gender "
			           + "    from "
				 	   + "    ( "
					   + "        select userid, name, email, gender "
					   + "		  from tbl_member "
					   + "		  where userid != 'admin' ";
			
			
			String colname = paraMap.get("searchType"); 		// Map 에 있는것을 꺼내본다. 검색 분류를 가져온다.
			String searchWord = paraMap.get("searchWord");      // 검색어를 가져온다.
			//System.out.println(searchWord);
			if("email".equals(colname)) {
				// 검색 대상이 이메일인 경우 
				// 암호화가 되어있으므로 이메일을 암호화 해서 위치홀더에 넣기 위해 한다. 
				searchWord = aes.encrypt(searchWord);
			}
			
			// 만약 Map 에 저장되어있는 값이 있다면 sql 문을 추가해줘야 한다. 
			if( searchWord != null && !searchWord.trim().isEmpty())   // searchWord 가 null 이 아니고 공백이 아니라면 
			{
				sql += " and " + colname + " like '%'|| ? || '%' ";    // ? 위치홀더에는 searchWord 가 올것이다.  위치홀더에는 무조건 데이터 값만 쓴다, 컬럼명 등을 쓰면 절대로 나오지 않는다.
				// 컬럼명과 테이블명은 위치홀더(?)로 사용하면 안된다.!!!!!
				// 위치홀더(?)로 들어오는 것은 오로지 컬럼명과 테이블명이 아닌 오로지 데이터 값만 들어온다.
			
			}
			
			sql += " order by registerday desc "
			     + "    ) V "
				 + " ) T "
				 + " where RNO between ? and ? ";   // 페이징 한 페이지쪽을 보여줄것이다.
			
			
			
			pstmt = conn.prepareStatement(sql);
			
			
			int currentShowPageNo = Integer.parseInt(paraMap.get("currentShowPageNo"));		// 조회하고자 하는 페이지 번호 불러옴 memverlistAction 에서 
			int sizePerPage = Integer.parseInt(paraMap.get("sizePerPage"));                 // 한페이지당 보여줄 행의 개수 
			
			
		// ==== 페이징 처리 공식 ====
		// where RNO between (조회하고자하는 페이지 번호 * 한페이지당 보여줄 행의 개수) - (한페이지당 보여줄 행의 개수 - 1) and (조회하고자하는 페이지 번호 * 한페이지당 보여줄 행의 개수) 
			
			if( searchWord != null && !searchWord.trim().isEmpty())   // searchWord 가 null 이 아니고 공백이 아니라면 그때만 위치 홀더가 있으니까 if문을 준다,
			{
				pstmt.setString(1, searchWord);
				pstmt.setInt(2, (currentShowPageNo * sizePerPage) - (sizePerPage - 1) );
				pstmt.setInt(3, (currentShowPageNo * sizePerPage) );
			}
			else {   // 검색어가 없으면
				
				pstmt.setInt(1, (currentShowPageNo * sizePerPage) - (sizePerPage - 1) );
				pstmt.setInt(2, (currentShowPageNo * sizePerPage) );
				
			}
			
			rs = pstmt.executeQuery();
			
			// while 로 반복 
			while(rs.next()) {
				MemberVO member = new MemberVO();
				
				member.setUserid(rs.getString(1));
				member.setName(rs.getString(2));
				member.setEmail( aes.decrypt(rs.getString(3)));   // 암호화 되어진 이메일 을 복호화 한다.				
				member.setGender(rs.getString(4));
				
				// memberList 에 저장하기 위해
				memberList.add(member);
				
			} // end of while(rs.next()) {
			
				
				
		} catch ( GeneralSecurityException | UnsupportedEncodingException e) {			// 오류 두개를 같이 잡을때 | 는 or를 뜻한다.
			e.printStackTrace();
		} finally { 
			close();
		}
			
			
		return memberList;
		
	} // end of public List<MemberVO> selectPagingMember(Map<String, String> paraMap) throws SQLException {

}
