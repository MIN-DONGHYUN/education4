package member.model;

import java.sql.SQLException;

public interface InterMemberDAO {

	// 회원 가입을 해주는 메소드 (tbl_member 테이블에 insert) 해준다.
	// insert 는 return 타입이 int 여야 한다.   select 는 return 타입이 DTO도 될수 있고 List도 될 수 있고 매핑을 한다면 MAP도 될수 있다.
	// INSERT를 생각 해보면, 조회해서 데이터들을 가져올 필요가 없죠?
	//그래서 반환 타입이 int형 입니다.
	
	
	int registerMember(MemberVO member) throws SQLException;
	
	
	// ID 중복검사  tbl_member 테이블에서 userid 가 존재하면  , user id 가 존재하지 않으면 false 를 리턴한다.
	boolean idDuplicatecheck(String userid) throws SQLException; 
	
	

}
