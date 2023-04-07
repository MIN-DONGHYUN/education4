package member.model;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.sql.*;
import java.util.Map;

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

}
