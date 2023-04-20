package myshop.model;

import java.sql.*;
import java.util.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class ProductDAO implements InterProductDAO {

	private DataSource ds;    // DataSource ds 는 아파치톰켓이 제공하는 DBCP(DB Connection Pool) 이다.
	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;
	
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
	public ProductDAO() {
		
		try {
			Context initContext = new InitialContext();
		    Context envContext  = (Context)initContext.lookup("java:/comp/env");
		    ds = (DataSource)envContext.lookup("jdbc/myoracle");      // lookup(이름) 이 중요한 것이다. web.xml 부분의 이름, context.xml 이름과 모두 같아야 한다. 이 것이 배치 서술자인 web.xml 로 가고 context.xml 로 간다. 
	
		} catch(NamingException e) {
			e.printStackTrace();
		}
	    
	}
	
	
	
	// interface 의 List 재정의 필요 
	// 메인 페이지에 보여지는 상품 이미지 파일명을 모두 조회(select)하는 매소드
	@Override
	public List<ImageVO> imageSelectAll() throws SQLException {
		
		List<ImageVO> imgList = new ArrayList<>();
		
		try {
			conn = ds.getConnection();     // return 타입 connection   이렇게 하면 자기 오라클 DB와 붙는다. 
			
			String sql = " select imgno, imgfilename " +
						 " from tbl_main_image " +
					     " order by imgno asc ";
			
			pstmt = conn.prepareStatement(sql);
			
			// 우편 배달부
			rs = pstmt.executeQuery();
			
			
			while(rs.next()) {
				
				ImageVO imgvo = new ImageVO();
				
				imgvo.setImgno(rs.getInt(1));
				imgvo.setImgfilename(rs.getString(2));
				
				imgList.add(imgvo);
				
			} // end of while(rs != null) {
			
			
		} finally {
			close();
		}
		
		return imgList;
	} // end of public List<ImageVO> imageSelectAll() throws SQLException {}


	// Ajax(JSON)을 사용하여 HIT 상품목록을 "더보기" 방식으로 페이징 처리 해주기 위해 스팩별로 제품의 전체개수 알아오기.//
	@Override
	public int totalPspecCount(String fk_snum) throws SQLException {
		
		int totalCount = 0;
		
		try {
			conn = ds.getConnection();     // return 타입 connection   이렇게 하면 자기 오라클 DB와 붙는다. 
			
			String sql = " select count(*) " +
						 " from tbl_product " +
					     " where fk_snum = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, fk_snum);
			
			
			// 우편 배달부
			rs = pstmt.executeQuery();
			
			
			rs.next(); // 다음이 있으면 실행 
			
			totalCount = rs.getInt(1);  // 값을 저장한다.
			
		} finally {
			close();
		}
		
		return totalCount;
	} // end of public int totalPspecCount(String fk_snum) throws SQLException {}


	// Ajax(JSON)를 이용한 더보기 방식(페이징처리)으로 상품정보를 8개씩 잘라서(start ~ end) 조회해오기
	@Override
	public List<ProductVO> selectBySpecName(Map<String, String> paraMap) throws SQLException {
		
		List<ProductVO> prodList = new ArrayList<>();
		
		try {
			conn = ds.getConnection();     // return 타입 connection   이렇게 하면 자기 오라클 DB와 붙는다. 
			
			String sql = " select pnum, pname, code, pcompany, pimage1, pimage2, pqty, price, saleprice, sname, pcontent, point, pinputdate "
					+ " from "
					+ " ( "
					+ "    select  row_number() over (order by pnum desc) AS RNO    "
					+ "          , pnum, pname, C.code, pcompany, pimage1, pimage2, pqty, price, saleprice, S.sname, pcontent, point "
					+ "          , to_char(pinputdate, 'yyyy-mm-dd') as pinputdate "
					+ "    from tbl_product P "
					+ "    JOIN tbl_category C "
					+ "    ON P.fk_cnum = C.cnum "
					+ "    JOIN tbl_spec S "
					+ "    ON P.fk_snum = S.snum "
					+ "    where S.sname = ? "
					+ " ) V "
					+ " where RNO between ? and ? ";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, paraMap.get("sname"));		//mailDisplayJSONAction 에서 확인하면 된다.
			pstmt.setString(2, paraMap.get("start"));
			pstmt.setString(3, paraMap.get("end"));
			
			
			// 우편 배달부
			rs = pstmt.executeQuery();
			
			
			while(rs.next()) {     // 다음이 있으면 실행 
				
				ProductVO pvo = new ProductVO();
				
				pvo.setPnum(rs.getInt(1));      // 제품 번호
				pvo.setPname(rs.getString(2));  // 제품 명
				
				CategoryVO categvo = new CategoryVO();   //join 때문에 이것을 만들어야 함
				
				categvo.setCode(rs.getString(3));  // 카테고리 코드 
				pvo.setCategvo(categvo);				
				
				pvo.setPcompany(rs.getString(4));  // 제조회사명
	            pvo.setPimage1(rs.getString(5));   // 제품이미지1   이미지파일명
	            pvo.setPimage2(rs.getString(6));   // 제품이미지2   이미지파일명
	            pvo.setPqty(rs.getInt(7));         // 제품 재고량
	            pvo.setPrice(rs.getInt(8));        // 제품 정가
				pvo.setSaleprice(rs.getInt(9)); // 제품 판매가(할인해서 할 것이므로)
				
				SpecVO spvo = new SpecVO();
				
				spvo.setSname(rs.getString(10));   // 스팩명
				pvo.setSpvo(spvo);;
				
				pvo.setPcontent(rs.getString(11));     // 제품설명 
	            pvo.setPoint(rs.getInt(12));         // 포인트 점수        
	            pvo.setPinputdate(rs.getString(13)); // 제품입고일자 
				
	            
	            prodList.add(pvo);		// 리스트에 담는다.
	            
			} 
			
			
		} finally {
			close();
		}
		
		return prodList;
	} // end of public List<ProductVO> selectBySpecName(Map<String, String> paraMap) throws SQLException

	// tbl_category 테이블에서 카테고리 대분류 번호(cnum), 카테고리코드(code), 카테고리명(cname)을 조회해오기 
	// VO 를 사용하지 않고 Map 으로 처리해보겠습니다
	@Override
	public List<Map<String, String>> getCategoryList() throws SQLException {
		
		 List<Map<String, String>> categoryList = new ArrayList<>();
		
		try {
			conn = ds.getConnection();     // return 타입 connection   이렇게 하면 자기 오라클 DB와 붙는다. 
			
			String sql = " select cnum, code, cname "
					+ "from tbl_category "
					+ "order by cnum asc ";
			
			pstmt = conn.prepareStatement(sql);
		
			// 우편 배달부
			rs = pstmt.executeQuery();
			
			
			while(rs.next()) {     // 다음이 있으면 실행 
				
				Map<String, String> map = new HashMap<>();
				
				map.put("cnum",  rs.getString(1));
				map.put("code",  rs.getString(2));
				map.put("cname",  rs.getString(3));
	           
				categoryList.add(map);
			} 
			
			
		} finally {
			close();
		}
		
		return categoryList;
		
		
	} // end of public List<Map<String, String>> getCategoryList() throws SQLException 


	// 특정 카테고리에 속하는 제품들을 페이지바를 이용한 페이징 처리하여 조회(select) 해오기 
	@Override
	public List<ProductVO> selectProductByCategory(Map<String, String> paraMap) throws SQLException {
		
		List<ProductVO> prodList = new ArrayList<>();
		
		try {
			conn = ds.getConnection();     // return 타입 connection   이렇게 하면 자기 오라클 DB와 붙는다. 
			
			String sql = "select cname, sname, pnum, pname, pcompany, pimage1, pimage2, pqty, price, saleprice, pcontent, point, pinputdate "+
					" from "+
					" ( "+
					"    select rownum AS RNO, cname, sname, pnum, pname, pcompany, pimage1, pimage2, pqty, price, saleprice, pcontent, point, pinputdate "+
					"    from "+
					"    ( "+
					"        select C.cname, S.sname, pnum, pname, pcompany, pimage1, pimage2, pqty, price, saleprice, pcontent, point, pinputdate "+
					"        from "+
					"        (select pnum, pname, pcompany, pimage1, pimage2, pqty, price, saleprice, pcontent, point "+
					"              , to_char(pinputdate, 'yyyy-mm-dd') as pinputdate, fk_cnum, fk_snum "+
					"         from tbl_product "+
					"         where fk_cnum = ? "+
					"         order by pnum desc "+
					"     ) P "+
					"     JOIN tbl_category C "+
					"     ON P.fk_cnum = C.cnum "+
					"     JOIN tbl_spec S "+
					"     ON P.fk_snum = S.snum "+
					"    ) V "+
					" ) T "+
					" where T.RNO between ? and ? ";
			
			pstmt = conn.prepareStatement(sql);
			
			paraMap.get("currentShowPageNo");
			
			int currentShowPageNo = Integer.parseInt(paraMap.get("currentShowPageNo"));		// 조회하고자 하는 페이지 번호 불러옴 memverlistAction 에서 
			int sizePerPage = 10;                 // 한페이지당 화면상에 보여줄 제품의 개수는 10으로 한다. 
			
			
			pstmt.setString(1, paraMap.get("cnum"));	
			
			// ==== 페이징 처리 공식 ====
			// where RNO between (조회하고자하는 페이지 번호 * 한페이지당 보여줄 행의 개수) - (한페이지당 보여줄 행의 개수 - 1) and (조회하고자하는 페이지 번호 * 한페이지당 보여줄 행의 개수) 
			
			pstmt.setInt(2, (currentShowPageNo * sizePerPage) - (sizePerPage - 1) );
			pstmt.setInt(3, (currentShowPageNo * sizePerPage) );
			
			
			// 우편 배달부
			rs = pstmt.executeQuery();
			
			
			while(rs.next()) {     // 다음이 있으면 실행 
				
				ProductVO pvo = new ProductVO();
	             
	             pvo.setPnum(rs.getInt("pnum"));      // 제품번호
	             pvo.setPname(rs.getString("pname")); // 제품명
	             
	             CategoryVO categvo = new CategoryVO(); 
	             categvo.setCname(rs.getString("cname"));  // 카테고리명  
	             pvo.setCategvo(categvo);                  // 카테고리 

	             pvo.setPcompany(rs.getString("pcompany")); // 제조회사명
	             pvo.setPimage1(rs.getString("pimage1"));   // 제품이미지1   이미지파일명
	             pvo.setPimage2(rs.getString("pimage2"));   // 제품이미지2   이미지파일명
	             pvo.setPqty(rs.getInt("pqty"));            // 제품 재고량
	             pvo.setPrice(rs.getInt("price"));          // 제품 정가
	             pvo.setSaleprice(rs.getInt("saleprice"));  // 제품 판매가(할인해서 팔 것이므로)
	               
	             SpecVO spvo = new SpecVO(); 
	             spvo.setSname(rs.getString("sname")); // 스펙이름 
	             pvo.setSpvo(spvo); // 스펙 
	               
	             pvo.setPcontent(rs.getString("pcontent"));       // 제품설명 
	             pvo.setPoint(rs.getInt("point"));              // 포인트 점수        
	             pvo.setPinputdate(rs.getString("pinputdate")); // 제품입고일자                                             
	             
	             prodList.add(pvo);
	            
			} 
			
			
		} finally {
			close();
		}
		
		return prodList;
	} // end of public List<ProductVO> selectProductByCategory(Map<String, String> paraMap) throws SQLException

}
