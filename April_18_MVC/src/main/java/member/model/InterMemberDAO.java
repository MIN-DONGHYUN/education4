package member.model;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface InterMemberDAO {

	// 회원 가입을 해주는 메소드 (tbl_member 테이블에 insert) 해준다.
	// insert 는 return 타입이 int 여야 한다.   select 는 return 타입이 DTO도 될수 있고 List도 될 수 있고 매핑을 한다면 MAP도 될수 있다.
	// INSERT를 생각 해보면, 조회해서 데이터들을 가져올 필요가 없죠?
	//그래서 반환 타입이 int형 입니다.
	
	
	int registerMember(MemberVO member) throws SQLException;
	
	
	// ID 중복검사  tbl_member 테이블에서 userid 가 존재하면  , user id 가 존재하지 않으면 false 를 리턴한다.
	boolean idDuplicatecheck(String userid) throws SQLException; 
	
	
	// email 중복검사  tbl_member 테이블에서 email 가 존재하면  , email가 존재하지 않으면 false 를 리턴한다.
	boolean emailDuplicatecheck(String email) throws SQLException;

	// 입력받은 HashMap(paraMAp) 을 가지고 한명의 회원정보를 리턴시켜주는 매소드(로그인 처리)
	MemberVO selectOneMember(Map<String, String> paraMap) throws SQLException;

	// 아이디 찾기(성명, 이메일을 입력받아서 해당 사용자의 아이디를 알려준다.)
	String findUserid(Map<String, String> paraMap) throws SQLException;

	// 비밀번호 찾기(아이디, 이메일을 입력받아서 해당 사용자가 존재하는지 유뮤를 알려준다.)
	boolean isUserExist(Map<String, String> paraMap) throws SQLException;

	// 암호변경하기 
	int pwdUpdate(Map<String, String> paraMap) throws SQLException;

	// DB에 코인 및 포인트 증가하기
	int coinUpdate(Map<String, String> paraMap) throws SQLException;

	// 회원의 개인 정보 변경하기
	int updateMember(MemberVO member) throws SQLException;

	// 암호 변경시 현재 사용중인 암호인지 아닌지 알아오기
	int duplicatePwdCheck(Map<String, String> paraMap) throws SQLException;

	// *** 페이징 처리를 안한 모든 회원 또는 검색한 회원 목록 보여주기 *** //
	List<MemberVO> selectMember(Map<String, String> paraMap) throws SQLException;

	// *** 페이징 처리를 한 모든 회원 또는 검색한 회원 목록 보여주기 *** //
	List<MemberVO> selectPagingMember(Map<String, String> paraMap) throws SQLException;

	// 페이징 처리를 위한 검색이 있는 또는 검색이 없는 전체 회원 에 대한 총 페이지 알아오기
	int getTotalPage( Map<String, String> paraMap ) throws SQLException;

	// userid 값을 입력받아서 회원 1명에 대한 상세정보 알아오기
	MemberVO MemberOneDetail(String userid) throws SQLException;
		
	
	

}
