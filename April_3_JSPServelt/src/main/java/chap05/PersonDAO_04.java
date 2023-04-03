package chap05;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PersonDAO_04 implements InterPersonDAO_03 {

	
	private Connection conn;
	private PreparedStatement pstmt;  // 위치홀더를 사용하기 위해
	private ResultSet rs;
	
	private void close() {// 자원 반납
		
		try {
			if(rs != null) {
				rs.close();
				rs=null;
			}
			if(pstmt != null) {
				pstmt.close();
				pstmt = null;
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		
	} // end of private void close()
	
	
	// 개인성향을 입력(insert) 해주는 메소드 구현하기
	@Override
	public int personRegister(PersonDTO_02 psdto) throws SQLException {
		
		int n = 0;
		
		try {
			
			conn = MyDBConnection_05.getConn();
			
			// 위치 홀더
			String sql = "insert into tbl_person_interest(seq, name, school, color, food)"
					   + "values(person_seq.nextval, ?, ?, ?, ?)";
		
			// 우편 배달부
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, psdto.getName());
			pstmt.setString(2, psdto.getSchool());
			pstmt.setString(3, psdto.getColor());
			
			if( psdto.getFood() != null) {
				pstmt.setString(4, String.join(",", psdto.getFood()) );   // 배열을 join으로 문자열로 바꿈
			}
			else {
				pstmt.setString(4, null);
			}
			
			n = pstmt.executeUpdate();   //DML 문  리턴은 int 가 나오게 됨  
		} 
		finally {
			close();
		}
		
		
		return n;
	} // end of public int personRegister(PersonDTO_02 psdto) {
	
	
	// tbl_person_interset 테이블에 저장되어진 모든 행(데이터)을 읽어다가select 해주는 추상 메소드 구현하기
		@Override
		public List<PersonDTO_02> selectAll() throws SQLException {
			
			List<PersonDTO_02> personList = new ArrayList<>();
			
			
			try {
				
				conn = MyDBConnection_05.getConn();
				
				String sql = " select seq, name, school, color, food, "
						   + "       to_char(registerday, 'yyyy-mm-dd hh24:mi:ss') as registerday "
						   + " from tbl_person_interest "
						   + " order by seq desc ";
				
				pstmt = conn.prepareStatement(sql);
				
				rs = pstmt.executeQuery();
				
				while(rs.next()) {
					
					PersonDTO_02 psdto = new PersonDTO_02();
					psdto.setSeq(rs.getInt(1));  // 또는 psdto.setSeq(rs.getInt("SEQ");
					psdto.setName(rs.getString(2));  // 또는 psdto.setName(re.getString("NAME");
					psdto.setSchool(rs.getString(3));
					psdto.setColor(rs.getString(4));
					
					
					String foodes = rs.getString(5);  //rs.gerString("FOOD");
					
					if(foodes != null)
					{
						psdto.setFood(foodes.split("\\,"));   // ("짜장면", " 탕수육", "양장피" )
					}
					else {
						psdto.setFood(null);
					}
					
					psdto.setRegisterday(rs.getString(6));
					
					personList.add(psdto);
					
				}// end of while
				
			} finally {
				close();
			}
			
			
			return personList;
		}// end of public List<PersonDTO_02> selectAll() throws SQLException {

		// tbl_person_interest 테이블에 저장되어진 특정 1개 행만을select 해주는 메소드 구현하기
		@Override
		public PersonDTO_02 selectOne(String seq) throws SQLException {
			
			PersonDTO_02 psdto = null;    // 데이터 베이스에서 없으면 초기 값은 null;
			
			try {
				
				conn = MyDBConnection_05.getConn();
				
				String sql = " select seq, name, school, color, food, "
						   + "       to_char(registerday, 'yyyy-mm-dd hh24:mi:ss') as registerday "
						   + " from tbl_person_interest "
						   + " where seq = ? ";
				
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, seq); 			// 위치 홀더에 넣는 것은 seq
				
				
				rs = pstmt.executeQuery();
				
				if(rs.next()) {
					
					psdto = new PersonDTO_02();     // DB에서 select 되어진것이 있다면 new 를 해줘야 한다.
					psdto.setSeq(rs.getInt(1));  // 또는 psdto.setSeq(rs.getInt("SEQ");
					psdto.setName(rs.getString(2));  // 또는 psdto.setName(re.getString("NAME");
					psdto.setSchool(rs.getString(3));
					psdto.setColor(rs.getString(4));
					
					
					String foodes = rs.getString(5);  //rs.gerString("FOOD");
					
					if(foodes != null)
					{
						psdto.setFood(foodes.split("\\,"));   // ("짜장면", " 탕수육", "양장피" )
					}
					else {
						psdto.setFood(null);
					}
					
					psdto.setRegisterday(rs.getString(6));
					
					
					
				}// end of if
				
			} finally {
				close();
			}
			
			return psdto;
		}// end of public PersonDTO_02 selectOne(String seq) throws SQLException {


		// tbl_person_interest 테이블에 저장되어진 특정 1개 행만을 delete 해주는 메소드	
		@Override
		public int deletePerson(String seq) throws SQLException {
			
			int n = 0;
			
			try {
				conn = MyDBConnection_05.getConn();
				
				String sql = " delete from tbl_person_interest "
						  + " where seq = ?";
				
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setString(1, seq);
				
				n = pstmt.executeUpdate();
				// 정상적이면 1값이 나온다.
				
				
			} finally {
				close();
			}
			
			return n;
		}// end of public int deletePerson(String seq) throws SQLException {

		
		
		// tbl_person_interest 테이블에 저장되어진 특정 1개행(데이터)만 update 해주는 메소드 구현하기
		@Override
		public int updatePerson(PersonDTO_02 psdto) throws SQLException {
			
			int n = 0;
			
			try {
				
				conn = MyDBConnection_05.getConn();
				
				// 위치 홀더
				String sql = "update tbl_person_interest set name = ?, school = ?, color = ?, food = ?\n"+
						     "where seq = ?";
			
				// 우편 배달부
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setString(1, psdto.getName());
				pstmt.setString(2, psdto.getSchool());
				pstmt.setString(3, psdto.getColor());
				
				
				if(psdto.getFood() != null)
				{
					pstmt.setString(4, psdto.getStrFood());    // 배열이기 때문에 StrFood
				}
				else {
					pstmt.setString(4, null);				   // 값이 없을때는 null 을 넣어주기 위해 
				}
				
				pstmt.setInt(5, psdto.getSeq());
				
				
				
				n = pstmt.executeUpdate();   //DML 문  리턴은 int 가 나오게 됨  
			} 
			finally {
				close();
			}
			
			
			return n;
		}  // end of public int updatePerson(PersonDTO_02 psdto) throws SQLException {


}
