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


	// 페이징 처리를 위한 특정카테고리의 제품개수에 대한 총 페이지 알아오기
	@Override
	public int getTotalPage(String cnum) throws SQLException {
		
		int totalPage = 0;
		
		try {
			
			conn = ds.getConnection();
			
			String sql = " select ceil(count(*)/10) "    // 10 이 size이다.
					   + " from tbl_product "
					   + " where fk_cnum = ? ";
			
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1,  cnum);
			
			rs = pstmt.executeQuery();
			
			
			rs.next();   // 이건 무조건 필요한 것이다.
			
			
			totalPage =  rs.getInt(1);
			
				
		} finally { 
			close();
		}
			
			
		return totalPage;
		
		
		
	} // end of public int getTotalPage(String cnum) throws SQLException 

	// cnum(카테고리번호)의 tbl_category 테이블에 존재하는지 존재하지 않는지 알아오기
	@Override
	public boolean isExist_cnum(String cnum) throws SQLException {
		
		boolean isExists = false;
		
		// 존재하면 true 없으면 false
		
		
		try {
			
			conn = ds.getConnection();   // date source 에서 가져옴
			
			String sql = " select * "
					   + " from tbl_category "
					   + " where cnum = ? ";
			
			// 우편배달부
			pstmt = conn.prepareStatement(sql);
			
			// 위치 홀더
			pstmt.setString(1, cnum);
			
			rs = pstmt.executeQuery();
			
			
			isExists = rs.next();				// 행이 있으면 true 가 나온다. 그니까 있는지 없는지 검사하는 것이다. 
												// 행이 있으면 (중복된 userid) true;
												// 행이 없으면 (사용가능 userid) false;
			
		} finally {
			close();
		}

		return isExists;
	} // end of public boolean isExist_cnum(String cnum) throws SQLException

	// 목록을 조회하기 
	@Override
	public List<SpecVO> selectSpecList() throws SQLException {
		
		
		List<SpecVO> specList = new ArrayList<>();
		
		try {
			conn = ds.getConnection();     // return 타입 connection   이렇게 하면 자기 오라클 DB와 붙는다. 
			
			String sql = " select snum, sname "
					+ " from tbl_spec "
					+ " order by snum asc ";
			
			pstmt = conn.prepareStatement(sql);
			
			// 우편 배달부
			rs = pstmt.executeQuery();
			
			
			while(rs.next()) {     // 다음이 있으면 실행 
				
				SpecVO spvo = new SpecVO();
				spvo.setSnum(rs.getInt(1));
				spvo.setSname(rs.getString(2));
	            
				specList.add(spvo);
			}  // end of while(rs.next())
			
			
		} finally {
			close();
		}
		
		return specList;
		
		
	}
	
	
	//제품번호 채번해오기 중요한 부분 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 @Override
	   public int getPnumOfProduct() throws SQLException {
	      
	      int pnum = 0;
	      
	      try {
	         conn = ds.getConnection();
	         
	         String sql = " select seq_tbl_product_pnum.nextval AS PNUM "
	                  + " from dual ";
	         
	         pstmt = conn.prepareStatement(sql);
	         rs = pstmt.executeQuery();
	         
	         rs.next();
	         pnum = rs.getInt(1);
	         
	      } finally {
	         close();
	      }
	      
	      return pnum;
	   }// end of public int getPnumOfProduct() throws 

	
	
	// tbl_product 테이블에 insert 하기 
	@Override
	public int productInsert(ProductVO pvo) throws SQLException {
		
		  int result = 0;
	      
	      try {
	         conn = ds.getConnection();
	         
	         String sql = " insert into tbl_product(pnum, pname, fk_cnum, pcompany, pimage1, pimage2, prdmanual_systemFileName, prdmanual_orginFileName, pqty, price, saleprice, fk_snum, pcontent, point) " +  
	                    " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	         
	         pstmt = conn.prepareStatement(sql);
	         
	         pstmt.setInt(1, pvo.getPnum());
	         pstmt.setString(2, pvo.getPname());
	         pstmt.setInt(3, pvo.getFk_cnum());    
	         pstmt.setString(4, pvo.getPcompany()); 
	         pstmt.setString(5, pvo.getPimage1());    
	         pstmt.setString(6, pvo.getPimage2()); 
	         pstmt.setString(7, pvo.getPrdmanual_systemFileName());
	         pstmt.setString(8, pvo.getPrdmanual_orginFileName());
	         pstmt.setInt(9, pvo.getPqty()); 
	         pstmt.setInt(10, pvo.getPrice());
	         pstmt.setInt(11, pvo.getSaleprice());
	         pstmt.setInt(12, pvo.getFk_snum());
	         pstmt.setString(13, pvo.getPcontent());
	         pstmt.setInt(14, pvo.getPoint());
	            
	         result = pstmt.executeUpdate();
	         
	      } finally {
	         close();
	      }
	      
	      return result;

	}  // end of public int productInsert(ProductVO pvo) throws SQLException

	
	// tbl_product_imagefile 테이블에 제품의 추가이미지 파일명 insert 해주기 
	@Override
	public int product_imagefile_Insert(int pnum, String attachFileName) throws SQLException {
		
		  int result = 0;
	      
	      try {
	         conn = ds.getConnection();
	         
	         String sql = " insert into tbl_product_imagefile(imgfileno, fk_pnum, imgfilename) "+ 
	                    " values(seqImgfileno.nextval, ?, ?) ";
	         
	         pstmt = conn.prepareStatement(sql);
	         
	         pstmt.setInt(1, pnum);
	         pstmt.setString(2, attachFileName);
	         
	         result = pstmt.executeUpdate();
	         
	      } finally {
	         close();
	      }
	      
	      return result;
		
		
		
	}  // end of public int product_imagefile_Insert(int pnum, String attachFileName) throws SQLException


	// 제품번호를 가지고서 해당 제품의 정보를 조회해오기
	@Override
	public ProductVO selectOneProductByPnum(String pnum) throws SQLException {
	     
		  ProductVO pvo = null;
	      
	      try {
	          conn = ds.getConnection(); 
	          
	          String sql = " select S.sname, pnum, pname, pcompany, price, saleprice, point, pqty, pcontent, pimage1, pimage2, prdmanual_systemFileName, nvl(prdmanual_orginFileName,'없음') AS prdmanual_orginFileName "+  
	                     " from "+
	                     " ( "+
	                     "  select fk_snum, pnum, pname, pcompany, price, saleprice, point, pqty, pcontent, pimage1, pimage2, prdmanual_systemFileName, prdmanual_orginFileName "+
	                     "  from tbl_product "+
	                     "  where pnum = ? "+
	                     " ) P JOIN tbl_spec S "+
	                     " ON P.fk_snum = S.snum ";
	          
	          pstmt = conn.prepareStatement(sql);
	          pstmt.setString(1, pnum);
	          
	          rs = pstmt.executeQuery();
	          
	          if(rs.next()) {
	             
	             String sname = rs.getString(1);     // "HIT", "NEW", "BEST" 값을 가짐 
	             int    npnum = rs.getInt(2);        // 제품번호
	             String pname = rs.getString(3);     // 제품명
	             String pcompany = rs.getString(4);  // 제조회사명
	             int    price = rs.getInt(5);        // 제품 정가
	             int    saleprice = rs.getInt(6);    // 제품 판매가
	             int    point = rs.getInt(7);        // 포인트 점수
	             int    pqty = rs.getInt(8);         // 제품 재고량
	             String pcontent = rs.getString(9);  // 제품설명
	             String pimage1 = rs.getString(10);  // 제품이미지1
	             String pimage2 = rs.getString(11);  // 제품이미지2
	             String prdmanual_systemFileName = rs.getString(12); // 파일서버에 업로드되어지는 실제 제품설명서 파일명
	             String prdmanual_orginFileName = rs.getString(13);  // 웹클라이언트의 웹브라우저에서 파일을 업로드 할때 올리는 제품설명서 파일명 
	             
	             pvo = new ProductVO(); 
	             
	             SpecVO spvo = new SpecVO();
	             spvo.setSname(sname);
	             
	             pvo.setSpvo(spvo);
	             pvo.setPnum(npnum);
	             pvo.setPname(pname);
	             pvo.setPcompany(pcompany);
	             pvo.setPrice(price);
	             pvo.setSaleprice(saleprice);
	             pvo.setPoint(point);
	             pvo.setPqty(pqty);
	             pvo.setPcontent(pcontent);
	             pvo.setPimage1(pimage1);
	             pvo.setPimage2(pimage2);
	             pvo.setPrdmanual_systemFileName(prdmanual_systemFileName);
	             pvo.setPrdmanual_orginFileName(prdmanual_orginFileName);
	             
	          }// end of if-----------------------------
	          
	      } finally {
	         close();
	      }
	      
	      return pvo;      
		
	} // end of public ProductVO selectOneProductByPnum(String pnum)

	
	// 제품 번호를 가지고서 해당 제품의 추가된 이미지 정보를 조회해오기 
	@Override
	public List<String> getImagesByPnum(String pnum) throws SQLException {
		
		List<String> imgList = new ArrayList<>();
	      
	      try {
	         conn = ds.getConnection();
	         
	         String sql = " select imgfilename "+
	                     " from tbl_product_imagefile "+
	                     " where fk_pnum = ? ";
	         
	         pstmt = conn.prepareStatement(sql);
	         pstmt.setString(1, pnum);
	         
	         rs = pstmt.executeQuery();
	         
	         while(rs.next()) {
	            String imgfilename = rs.getString(1); // 이미지파일명 
	            imgList.add(imgfilename); 
	         }
	         
	      } finally {
	         close();
	      }
	      
	      return imgList;
		
	} // end of public List<String> getImagesByPnum(String pnum) throws SQLException


	// ***** 제품 번호를 가지고서 해당 제품의 제품설명서의 첨부파일명 및 오리지널 파일명 조회해오기 ***** //
	@Override
	public Map<String, String> getPrdmanualFileName(String pnum) throws SQLException {

		Map<String, String> map = new HashMap<>();
	      
	      try {
	         conn = ds.getConnection();
	         
	         String sql = " select prdmanual_systemFileName, prdmanual_orginFileName "
	         		+ "from tbl_product "
	         		+ "where pnum = ? ";
	         
	         pstmt = conn.prepareStatement(sql);
	         pstmt.setString(1, pnum);    // 위치홀더 
	         
	         rs = pstmt.executeQuery();
	         
	         if(rs.next()) {
	        	 map.put("prdmanual_systemFileName", rs.getString(1));
	        	 // 파일 서버에 업로드 되어진 실제 제품설명서 파일명 
	        	 
	        	 map.put("prdmanual_orginFileName", rs.getString(2));
	        	 // 웹클라이언트의 웹브라우저에서 파일을 업로드 할때 올린 제품설명서 파일명 
	         }
	         
	      } finally {
	         close();
	      }
	      
	      return map;
	      
	} // end of public Map<String, String> getPrdmanualFileName(String pnum) throws SQLException


	
	// 장바구니 담기 
    // 장바구니 테이블(tbl_cart)에 해당 제품을 담아야 한다.
    // 장바구니 테이블에 해당 제품이 존재하지 않는 경우에는 tbl_cart 테이블에 insert 를 해야하고, 
    // 장바구니 테이블에 해당 제품이 존재하는 경우에는 또 그 제품을 추가해서 장바구니 담기를 한다라면 tbl_cart 테이블에 update 를 해야한다. 
	@Override
	public int addCart(Map<String, String> paraMap) throws SQLException {
		
		int n = 0;
		
		try {
			
			conn = ds.getConnection();
			
			/*
	            먼저 장바구니 테이블(tbl_cart)에 어떤 회원이 새로운 제품을 넣는 것인지,
	            아니면 또 다시 제품을 추가로 더 구매하는 것인지를 알아야 한다.
	            이것을 알기위해서 어떤 회원이 어떤 제품을  장바구니 테이블(tbl_cart) 넣을때
	            그 제품이 이미 존재하는지 select 를 통해서 알아와야 한다.
	            
	          -------------------------------------------
	           cartno   fk_userid     fk_pnum   oqty  
	          -------------------------------------------
	             1      mindh          7         2      
	             2      mindh          6         3     
	             3      leess          7         5     
	         */
			
			String sql = " select cartno "
					   + " from tbl_cart "
					   + " where fk_userid = ? and fk_pnum = ? ";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, paraMap.get("fk_userid"));
			pstmt.setString(2, paraMap.get("fk_pnum"));
			
			rs = pstmt.executeQuery();
			
			
			if(rs.next()) {
				// 어떤 제품을 추가로 장바구니에 넣고자 하는 경우
				
				int cartno = rs.getInt("CARTNO");   // CARTNO 대신 1을 써도 됨  소문자로 써도 됨
				
				sql = " update tbl_cart set oqty = oqty + ? " 
				    + " where cartno = ? ";
				
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, Integer.parseInt(paraMap.get("oqty")));
				pstmt.setInt(2, cartno);
				
				
				n = pstmt.executeUpdate();
			}
			else {
				// 장바구니에 존재하지 않는 새로운 제품을 넣고자 하는 경우 
				
				sql = " insert into tbl_cart(cartno, fk_userid, fk_pnum, oqty, registerday) "
					+ " values(seq_tbl_cart_cartno.nextval, ?, ?, ?, default) ";
				
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, paraMap.get("fk_userid"));
				pstmt.setInt(2, Integer.parseInt(paraMap.get("fk_pnum")));
				pstmt.setInt(3, Integer.parseInt(paraMap.get("oqty")));
			
				n = pstmt.executeUpdate();
				
			}
			
			
			
			
			
		} finally {
			
			close();
			
		}
		
		
		return n;
		
	} // end of public int addCart(Map<String, String> paraMap) throws SQLException


	// 로그인한 사용자의 장바구니 목록을 조회하기 
	@Override
	public List<CartVO> selectProductCart(String userid) throws SQLException {
		
		List<CartVO> cartList = null;
	      
	      try {
	         conn = ds.getConnection();
	         
	         String sql = " select A.cartno, A.fk_userid, A.fk_pnum, "+
	                    "          B.pname, B.pimage1, B.price, B.saleprice, B.point, A.oqty "+
	                    " from tbl_cart A join tbl_product B "+
	                    " on A.fk_pnum = B.pnum "+
	                    " where A.fk_userid = ? "+
	                    " order by A.cartno desc ";
	         
	         pstmt = conn.prepareStatement(sql);
	         pstmt.setString(1, userid);
	         
	         rs = pstmt.executeQuery();
	         
	         int cnt = 0;
	         while(rs.next()) {
	            cnt++;
	            
	            if(cnt==1) {
	               cartList = new ArrayList<>();
	            }
	            
	            int cartno = rs.getInt("cartno");
	            String fk_userid = rs.getString("fk_userid");
	            int fk_pnum = rs.getInt("fk_pnum");
	            String pname = rs.getString("pname");
	            String pimage1 = rs.getString("pimage1");
	            int price = rs.getInt("price");
	            int saleprice = rs.getInt("saleprice");
	            int point = rs.getInt("point");
	            int oqty = rs.getInt("oqty");  // 주문량 
	                        
	            ProductVO prodvo = new ProductVO();
	            prodvo.setPnum(fk_pnum);
	            prodvo.setPname(pname);
	            prodvo.setPimage1(pimage1);
	            prodvo.setPrice(price);
	            prodvo.setSaleprice(saleprice);
	            prodvo.setPoint(point);
	            
	            // **** !!!! 중요함 !!!! **** //
	            prodvo.setTotalPriceTotalPoint(oqty);
	            // **** !!!! 중요함 !!!! **** //
	            
	            CartVO cvo = new CartVO();
	            cvo.setCartno(cartno);
	            cvo.setUserid(fk_userid);
	            cvo.setPnum(fk_pnum);
	            cvo.setOqty(oqty);
	            cvo.setProd(prodvo);
	            
	            cartList.add(cvo);
	         }// end of while---------------------------------
	                  
	      } finally {
	         close();
	      }
	      
	      return cartList;
		
	} // end of public List<CartVO> selectProductCart(String userid) throws SQLException


	// 로그인한 사용자의 장바구니에 담긴 주문총액 합계 및 총포인트합계 알아오기 
	@Override
	public HashMap<String, String> selectCartSumPricePoint(String userid) throws SQLException {
	
		HashMap<String, String> sumMap = new HashMap<>();
		
		try {
	        conn = ds.getConnection();
		
			String sql = " select NVL(sum(B.saleprice * A.oqty), 0) AS SUMTOTALPRICE , "
					   + "       NVL(sum(B.point * A.oqty),0) AS SUMTOTALPOINT "
					   + " from tbl_cart A join tbl_product B "
					   + " on A.fk_pnum = B.pnum "
					   + " where A.fk_userid = ? ";
		     
		     pstmt = conn.prepareStatement(sql);
		     pstmt.setString(1, userid);
		     
		     rs = pstmt.executeQuery();
		     
		     rs.next();
	        
		     sumMap.put("SUMTOTALPRICE", rs.getString("SUMTOTALPRICE"));
		     sumMap.put("SUMTOTALPOINT", rs.getString("SUMTOTALPOINT"));
		     
		     
	  } finally {
	     close();
	  }
	  
	  return sumMap;

	} // end of public HashMap<String, String> selectCartSumPricePoint(String userid) throws SQLException


	// 장바구나 테이블에서 특정 제품을 장바구니 에서 비운다.
	@Override
	public int delCart(String cartno) throws SQLException {
		
		int n = 0;
		
		try {
	        conn = ds.getConnection();
		
			String sql = " delete from tbl_cart "
				       + " where cartno = ? ";
		     
		     pstmt = conn.prepareStatement(sql);
		     pstmt.setString(1, cartno);
		     
		     n = pstmt.executeUpdate();
		     		     
	  } finally {
	     close();
	  }
	  
	  return n;

	} // end of public int delCart(String cartno) throws SQLException

	
	
	// 장바구나 테이블에서 특정 제품을 장바구니 에서 비운다.
	@Override
	public int UpdateCart(String cartno, String oqty) throws SQLException {
		
		int n = 0;
		
		try {
	        conn = ds.getConnection();
		
			String sql = " update tbl_cart set oqty = ? "
				       + " where cartno = ? ";
		     
		     pstmt = conn.prepareStatement(sql);
		     pstmt.setString(1, oqty);
		     pstmt.setString(2, cartno);
		     
		     n = pstmt.executeUpdate();
		     		     
	  } finally {
	     close();
	  }
	  
	  return n;
		
		
	} // end of public int UpdateCart(String cartno, String oqty) throws SQLException

	
	// 주문번호(시퀀스 seq_tbl_order 값)을 채번해오는 것 
	@Override
	public int getSeq_tbl_order() throws SQLException {

		int seq = 0;
		
		try {
	        conn = ds.getConnection();
		
			String sql = " select seq_tbl_order.nextval AS seq "
					   + " from dual ";
		     
		     pstmt = conn.prepareStatement(sql);
		     
		     rs = pstmt.executeQuery();
		     
		     rs.next();
	        
		     seq = rs.getInt("seq");
		     
		     
	  } finally {
	     close();
	  }
	  
	  return seq;
	}//end of public int getSeq_tbl_order() throws SQLException


	
	// ===== Transaction 처리하기 ===== // 
    // 2. 주문 테이블에 채번해온 주문전표, 로그인한 사용자, 현재시각을 insert 하기(수동커밋처리)
    // 3. 주문상세 테이블에 채번해온 주문전표, 제품번호, 주문량, 주문금액을 insert 하기(수동커밋처리)
    // 4. 제품 테이블에서 제품번호에 해당하는 잔고량을 주문량 만큼 감하기(수동커밋처리) 
        
    // 5. 장바구니 테이블에서 cartnojoin 값에 해당하는 행들을 삭제(delete)하기(수동커밋처리)
    // >> 장바구니에서 주문을 한 것이 아니라 특정제품을 바로주문하기를 한 경우에는 장바구니 테이블에서 행들을 삭제할 작업은 없다. << 
        
    // 6. 회원 테이블에서 로그인한 사용자의 coin 액을 sumtotalPrice 만큼 감하고, point 를 sumtotalPoint 만큼 더하기(update)(수동커밋처리) 
    // 7. **** 모든처리가 성공되었을시 commit 하기(commit) **** 
    // 8. **** SQL 장애 발생시 rollback 하기(rollback) **** 
	@Override
	public int orderAdd(HashMap<String, Object> paraMap) throws SQLException {
		
		
		int isSuccess = 0;
		int n1 = 0, n2 = 0, n3 = 0, n4 = 0, n5 = 0;
		try {
			
			conn = ds.getConnection();
			
			// 수동 커밋으로 하기 
			conn.setAutoCommit(false);  // 수동 커밋 하기 
			
			// 2. 주문 테이블에 채번해온 주문전표, 로그인한 사용자, 현재시각을 insert 하기(수동커밋처리)
			String sql = " insert into tbl_order(odrcode, fk_userid, odrtotalPrice, odrtotalPoint, odrdate) "
                       + " values(?, ?, ?, ?, default) ";
			
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, (String)paraMap.get("odrcode"));
			pstmt.setString(2, (String)paraMap.get("userid"));
			pstmt.setInt(3, Integer.parseInt((String)paraMap.get("sum_totalPrice")));
			pstmt.setInt(4, Integer.parseInt((String)paraMap.get("sum_totalPoint")));
			
			n1 = pstmt.executeUpdate();
			
			System.out.println(" 확인용~~~~~ n1 : " + n1);
			
			
			// 3. 주문상세 테이블에 채번해온 주문전표, 제품번호, 주문량, 주문금액을 insert 하기(수동커밋처리)
			if(n1==1) {
				String[] pnum_arr = (String[]) paraMap.get("pnum_arr");    // 제품 번호
				String[] oqty_arr = (String[]) paraMap.get("oqty_arr");    // 주문량
				String[] totalPrice_arr = (String[]) paraMap.get("totalPrice_arr");    // 주문 금액
				
				int cnt = 0;    // 반복문이 성공될때마다 cnt 를 증가시키기 위해 
				
				for(int i = 0; i<pnum_arr.length; i++) {
					
					sql = " insert into tbl_orderdetail(odrseqnum, fk_odrcode, fk_pnum, oqty, odrprice, deliverStatus) "  
			            + " values(seq_tbl_orderdetail.nextval, ?, to_number(?), to_number(?), to_number(?), default) ";
					
					pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, (String)paraMap.get("odrcode"));
					pstmt.setString(2, pnum_arr[i]);
					pstmt.setString(3, oqty_arr[i]);
					pstmt.setString(4, totalPrice_arr[i]);
					
					pstmt.executeUpdate();
					cnt++;
				}// end of for ~~~~~~~~~~~~~~~~~~~
				
				if(pnum_arr.length == cnt) {
					n2 = 1;   // 모든 것이 성공일시 1값으로 변경 
				}
				
				System.out.println("확인용 n2 : " + n2);
				
				
			} // end of if 
			
			
			
		// 4. 제품 테이블에서 제품번호에 해당하는 잔고량을 주문량 만큼 감하기(수동커밋처리) 	
		if(n2 == 1) {
			
			String[] pnum_arr = (String[]) paraMap.get("pnum_arr");    // 제품 번호
			String[] oqty_arr = (String[]) paraMap.get("oqty_arr");    // 주문량
			
			
			//update 할 것이다. 
			int cnt = 0;    // 반복문이 성공될때마다 cnt 를 증가시키기 위해 
			
			for(int i = 0; i<pnum_arr.length; i++) {
				sql = " update tbl_product set pqty = pqty - to_number(?) "  
				    + " where pnum = ? ";   // 주문량과 제품 번호를 받아와야 한다. to_number(?) 로 해도되고 1044 줄에 serInt로 변경해도 됨 대신 Integer.parseInt로 변경
			
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setString(1, oqty_arr[i]);
				pstmt.setString(2, pnum_arr[i]);
				
				n3 = pstmt.executeUpdate();
				cnt++;

			} // end of for
			
			if(pnum_arr.length == cnt) {
				n3 = 1;   // 모든 것이 성공일시 1값으로 변경 
			}
			
			System.out.println("확인용 n3 : " + n3);
			
		} // end of if
		
		
		// 5. 장바구니 테이블에서 cartnojoin 값에 해당하는 행들을 삭제(delete)하기(수동커밋처리)
	    // >> 장바구니에서 주문을 한 것이 아니라 특정제품을 바로주문하기를 한 경우에는 장바구니 테이블에서 행들을 삭제할 작업은 없다. << 
	        	
		if(paraMap.get("cartno_arr") != null && n3 == 1) {   //orderAddAction 에서 가져옴 
			
			/*
				sql = " delete from tbl_cart "
				    + " where cartno in (?) ";
				// !!!!!! 중요 in 절을 위와 같이 위치홀더 ? 를 사용하면 안됨. !!!!!!
			*/
			
			String cartno_join =  String.join("','",  (String[]) paraMap.get("cartno_arr"));   // {"9","6","5"}  배열이므로 String 에 담도록 변경  paraMap.get("cartno_arr") 는 object
			// (String[]) paraMap.get("cartno_arr") 은 {"9","6","5"} 와 같이 배열형태이다.
			// cartno_join 은 "9','6','5" 와 같이 된다.  
			
			// !!!!!!!!!!!!!!!!!!!!!!!!!!!!★ * 2000000000  !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
			
			cartno_join = "'"+cartno_join+"'";  // 최종적으로 cartno_join 은 "'9','6','5'" 와 같이 된다.
			
			
			// 중요 !!!! in 절은 위와 같이 직접 변수로 처리해야함 !!!! //
			sql = " delete from tbl_cart "
				+ " where cartno in ("+ cartno_join +") ";
				// where cartno in('9','6','5') 와 같이 되어져야 한다.
			
			
			
			pstmt = conn.prepareStatement(sql);
			
			n4 = pstmt.executeUpdate();
			
			System.out.println("확인용 n4 : " + n4);
			
		} // end of if~~
			
		
		if(paraMap.get("cartno_arr") == null && n3 == 1) {
			// 이 밀은 "제품 상세 정보" 페이지에서 "바로 주문하기" 를 한 경우 
			// 장바구니 번호인 paraMap.get("cartno_arr") 이것이 없는 것이다.
			n4 = 1;    
			
			System.out.println("바로 주문하기인 경우 확인용 n4 : " + n4);
			// 바로 주문하기인 경우 확인용 n4 : 
		} // end of if
		
		
	    
	    // 6. 회원 테이블에서 로그인한 사용자의 coin 액을 sumtotalPrice 만큼 감하고, point 를 sumtotalPoint 만큼 더하기(update)(수동커밋처리) 
		if(n4 > 0) { // 앞에 모든 과정 통과
		
			sql = " update tbl_member set coin = coin - ? "
				+ "                      ,point = point + ? "
				+ " where userid = ?  ";    // 세미 프로젝트에서는 코인 개념이 아니기 때문에 point 만 하면 된다.
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, Integer.parseInt((String) paraMap.get("sum_totalPrice")));
			pstmt.setInt(2, Integer.parseInt((String) paraMap.get("sum_totalPoint")));
			pstmt.setString(3, (String) paraMap.get("userid") );
		
			n5 = pstmt.executeUpdate();
		
			System.out.println("확인용 n5 : " + n5);
		
		
		} // end of if
		
		
		// 7. **** 모든처리가 성공되었을시 commit 하기(commit) **** 
		if( n1*n2*n3*n4*n5 > 0 ) {
			
			conn.commit();  // 커밋 하기 (수동으로 커밋한것)
			
			conn.setAutoCommit(true);   // 모두 정상적으로 들어갔으므로 다시 자동커밋으로 전환 한다.
			
			System.out.println("확인용 n1*n2*n3*n4*n5 : " + (n1*n2*n3*n4*n5));
			
			isSuccess = 1;  // 성공 되었으므로 1값을 넘겨주기 위해 설정 
		}// end of if 
		
		
		
		
		
		} catch(SQLException e) {
			
			// 8. **** SQL 장애 발생시 rollback 하기(rollback) **** 
			conn.rollback();  // 롤백하기 
			
			conn.setAutoCommit(true);  //auto 커밋으로 하기 위해 
			
			isSuccess = 0;  // 0을 넘긴다.
			
			
		} finally {
			close();
		}
		
		
		
		return isSuccess;
	} // end of public int orderAdd(HashMap<String, Object> paraMap) throws SQLException


	// 주문한 제품에 대해 email 보내기시 email 내용에 넣을 주문한 제품번호에 대한 제품정보를 얻어오는 것 .
	@Override
	public List<ProductVO> getJumunProductList(String pnumes) throws SQLException {
		
		List<ProductVO> prodList = new ArrayList<>();
		
		try {
			conn = ds.getConnection();     // return 타입 connection   이렇게 하면 자기 오라클 DB와 붙는다. 
			
			String sql = " select pnum, pname, pcompany, pimage1, pimage2, price, saleprice, pcontent, point "
					   + "       , to_char(pinputdate, 'yyyy-mm-dd') as pinputdate "
					   + " from tbl_product "
					   + " where pnum in ("+pnumes+") ";
			
			pstmt = conn.prepareStatement(sql);
			
			// 우편 배달부
			rs = pstmt.executeQuery();
			
			
			while(rs.next()) {     // 다음이 있으면 실행 
				
				ProductVO pvo = new ProductVO();
				
				pvo.setPnum(rs.getInt("pnum"));      // 제품 번호
				pvo.setPname(rs.getString("pname"));  // 제품 명
				
				
				pvo.setPcompany(rs.getString("pcompany"));  // 제조회사명
	            pvo.setPimage1(rs.getString("pimage1"));   // 제품이미지1   이미지파일명
	            pvo.setPimage2(rs.getString("pimage2"));   // 제품이미지2   이미지파일명
	            pvo.setPrice(rs.getInt("price"));         // 정가
	            pvo.setSaleprice(rs.getInt("saleprice"));     // 판매 가격 
				pvo.setPcontent(rs.getString("pcontent")); // 제품 설명
				pvo.setPoint(rs.getInt("point")); // 제품 설명
				pvo.setPinputdate(rs.getString("pinputdate")); // 제품 설명
				
				
	            prodList.add(pvo);		// 리스트에 담는다.
	            
			} 
			
			
		} finally {
			close();
		}
		
		return prodList;
	} // end of public List<ProductVO> getJumunProductList(String pnumes) throws SQLException


	



}
