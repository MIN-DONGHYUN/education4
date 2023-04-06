package member.model;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.sql.*;

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

}
