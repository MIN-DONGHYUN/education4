package myshop.model;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface InterProductDAO {
	
	// 메인 페이지에 보여지는 상품 이미지 파일명을 모두 조회(select)하는 매소드
	
	List<ImageVO>imageSelectAll() throws SQLException;   // 리턴 타입은 LIST

	// Ajax(JSON)을 사용하여 HIT 상품목록을 "더보기" 방식으로 페이징 처리 해주기 위해 스팩별로 제품의 전체개수 알아오기.//
	int totalPspecCount(String fk_snum)  throws SQLException;

	// Ajax(JSON)를 이용한 더보기 방식(페이징처리)으로 상품정보를 8개씩 잘라서(start ~ end) 조회해오기 
	List<ProductVO> selectBySpecName(Map<String, String> paraMap) throws SQLException;

	// tbl_category 테이블에서 카테고리 대분류 번호(cnum), 카테고리코드(code), 카테고리명(cname)을 조회해오기 
	// VO 를 사용하지 않고 Map 으로 처리해보겠습니다.
	List<Map<String, String>> getCategoryList() throws SQLException;

	
	// 특정 카테고리에 속하는 제품들을 페이지바를 이용한 페이징 처리하여 조회(select) 해오기 
	List<ProductVO> selectProductByCategory(Map<String, String> paraMap) throws SQLException; 

}
