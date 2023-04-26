package myshop.model;

import java.sql.SQLException;
import java.util.HashMap;
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

	// 페이징 처리를 위한 특정카테고리의 제품개수에 대한 총 페이지 알아오기
	int getTotalPage(String cnum) throws SQLException;

	// cnum(카테고리번호)의 tbl_category 테이블에 존재하는지 존재하지 않는지 알아오기
	boolean isExist_cnum(String cnum) throws SQLException;

	// 목록을 조회하기 
	List<SpecVO> selectSpecList() throws SQLException;
	
	//제품번호 채번해오기 중요한 부분 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	int getPnumOfProduct() throws SQLException;

	// tbl_product 테이블에 insert 하기 
	int productInsert(ProductVO pvo) throws SQLException;

	// tbl_product_imagefile 테이블에 제품의 추가이미지 파일명 insert 해주기 
	int product_imagefile_Insert(int pnum, String attachFileName) throws SQLException;
	
	// 제품번호를 가지고서 해당 제품의 정보를 조회해오기
	ProductVO selectOneProductByPnum(String pnum)  throws SQLException;
	
	// 제품 번호를 가지고서 해당 제품의 추가된 이미지 정보를 조회해오기 
	List<String> getImagesByPnum(String pnum)   throws SQLException;

	// ***** 제품 번호를 가지고서 해당 제품의 제품설명서의 첨부파일명 및 오리지널 파일명 조회해오기 ***** //
	Map<String, String> getPrdmanualFileName(String pnum)    throws SQLException;

	// 장바구니 담기 
    // 장바구니 테이블(tbl_cart)에 해당 제품을 담아야 한다.
    // 장바구니 테이블에 해당 제품이 존재하지 않는 경우에는 tbl_cart 테이블에 insert 를 해야하고, 
    // 장바구니 테이블에 해당 제품이 존재하는 경우에는 또 그 제품을 추가해서 장바구니 담기를 한다라면 tbl_cart 테이블에 update 를 해야한다. 
	int addCart(Map<String, String> paraMap)  throws SQLException;

	// 로그인한 사용자의 장바구니 목록을 조회하기 
	List<CartVO> selectProductCart(String userid)  throws SQLException;

	// 로그인한 사용자의 장바구니에 담긴 주문총액 합계 및 총포인트합계 알아오기 
	HashMap<String, String> selectCartSumPricePoint(String userid) throws SQLException;

	// 장바구나 테이블에서 특정 제품을 장바구니 에서 비운다.
	int delCart(String cartno)  throws SQLException;

	// 장바구나 테이블에서 특정 제품을 장바구니 에서 비운다.
	int UpdateCart(String cartno, String oqty)   throws SQLException;
}
