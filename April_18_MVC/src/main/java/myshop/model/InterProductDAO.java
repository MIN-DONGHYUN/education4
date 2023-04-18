package myshop.model;

import java.sql.SQLException;
import java.util.List;

public interface InterProductDAO {
	
	// 메인 페이지에 보여지는 상품 이미지 파일명을 모두 조회(select)하는 매소드
	
	List<ImageVO>imageSelectAll() throws SQLException;   // 리턴 타입은 LIST

}
