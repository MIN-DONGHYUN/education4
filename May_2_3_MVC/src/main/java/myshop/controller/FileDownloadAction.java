package myshop.controller;

import java.io.File;
import java.io.FileInputStream;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.controller.AbstractController;
import myshop.model.*;

public class FileDownloadAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String pnum = request.getParameter("pnum");
		
		try {
			// 다운로드 할 파일의 경로를 구하고 File 객체를 생성한다.
			HttpSession session =  request.getSession();   // 세션 값을 가져 온다.
		
			ServletContext svlCtx = session.getServletContext();   // 서블릿 환경을 알아온다.
			String downloadFileDir = svlCtx.getRealPath("/images");   // 진짜 경로 를 뜻함 
	
			//System.out.println("=== 파일이 다운로드 되어지는 절대 경로 downloadFileDir =>" + downloadFileDir);
			// 결과 : === 첨부되어지는 이미지 파일이 올라가는 절대 경로 downloadFileDir =>C:\NCS\workspace(JSP)\.metadata\.plugins\org.eclipse.wst.server.core\tmp0\wtpwebapps\MyMVC\images
			
			
			
			// ***** 시스템에 업로드 되어진 파일 설명서 첨부파일명 및 오리지널 파일명 알아오기 ***** //
			InterProductDAO pdao = new ProductDAO();
			Map<String, String> map = pdao.getPrdmanualFileName(pnum);    // 메소드 생성 pnum 을 같이 넘겨줌 
			// 컬럼이 두개이므로 Map을 사용해야 한다.   // 한행 전체를 보려면 VO 또는 Map 을 사용한다.
			
			
			// File 객체 생성하기 
			String filePath = downloadFileDir + "\\"+map.get("prdmanual_systemFileName");
			// map.get("prdmanual_systemFileName"); 은 파일서버에 업로드 되어진 제품설명서 파일명임 
			
			File file = new File(filePath);    // filePath 는 String 타입
			
			// MIME TYPE 설정하기 
	        // (구글에서 검색어로 MIME TYPE 을 해보면 MIME TYPE에 따른 문서종류가 쭉 나온다)
	        String mimeType = svlCtx.getMimeType(filePath);
	        
		    //   System.out.println("~~~~ 확인용 mimeType => " + mimeType);
		    //  ~~~~ 확인용 mimeType => application/pdf  .pdf 파일임
		    //   ~~~~ 확인용 mimeType => image/jpeg       .jpg 파일임
		    //  ~~~~ 확인용 mimeType => application/vnd.openxmlformats-officedocument.spreadsheetml.sheet    엑셀파일임.
				
			if(mimeType == null) {
				mimeType = "application/octet-stream";   // "application/octet-stream" 는 모든 타입의 이진 타입 
				// "application/octet-stream" 은 일반적으로 잘 알려지지 않은 모든 종류의 이진 데이터를 뜻하는 것임.
			}
	        response.setContentType(mimeType);
	        // response.setContentType(mimeType)은 서버에서 클라이언트에게 응답할 때, 응답 본문의 MIME 타입을 설정하는 메서드입니다.
	        // setContentType() 메서드는 response 객체에 포함된 응답 헤더의 Content-Type 속성을 설정합니다. 이를 통해 웹 브라우저는 서버가 전송한 데이터가 어떤 형식인지를 알고, 적절한 방식으로 데이터를 처리하게 됩니다.
	        
	        
	        // 다운로드 되어질 파일명 알아와서 설정해주기 
	        String prdmanual_orginFileName = map.get("prdmanual_orginFileName");
	        // map.get("prdmanual_orginFileName"); 는 웹클라이언트의 웹브라우저에서 파일을 업로드 할때 올린 제품설명서 파일명임.
	        
	        
	        
	        // prdmanual_orginFileName(다운로드 되어지는 파일명)이 한글일때  
	        // 한글 파일명이 깨지지 않도록 하기위한 웹브라우저 별로 encoding 하기 및  다운로드 파일명 설정해주기
	        String downloadFileName = "";
	        String header = request.getHeader("User-Agent");
	        
	        if (header.contains("Edge")){
	           downloadFileName = URLEncoder.encode(prdmanual_orginFileName, "UTF-8").replaceAll("\\+", "%20");
	            response.setHeader("Content-Disposition", "attachment;filename=" + downloadFileName);
	         } else if (header.contains("MSIE") || header.contains("Trident")) { // IE 11버전부터는 Trident로 변경됨.
	            downloadFileName = URLEncoder.encode(prdmanual_orginFileName, "UTF-8").replaceAll("\\+", "%20");
	            response.setHeader("Content-Disposition", "attachment;filename=" + downloadFileName);
	        } else if (header.contains("Chrome")) {
	           downloadFileName = new String(prdmanual_orginFileName.getBytes("UTF-8"), "ISO-8859-1");
	            response.setHeader("Content-Disposition", "attachment; filename=" + downloadFileName);
	        } else if (header.contains("Opera")) {
	           downloadFileName = new String(prdmanual_orginFileName.getBytes("UTF-8"), "ISO-8859-1");
	            response.setHeader("Content-Disposition", "attachment; filename=" + downloadFileName);
	        } else if (header.contains("Firefox")) {
	           downloadFileName = new String(prdmanual_orginFileName.getBytes("UTF-8"), "ISO-8859-1");
	            response.setHeader("Content-Disposition", "attachment; filename=" + downloadFileName);
	        }
	        
	        
	        
	        // *** 다운로드 할 요청 파일을 읽어서 클라이언트로 파일을 전송하기 *** //
	        FileInputStream finStream = new FileInputStream(file);   // (file) 은 파일 객체를 뜻함 우리는 위에 파일객체를 설정해줬다. 
	        // 1 BYTE 기반 파일 입력 노드 스트림 생성 
	        
	        
	        ServletOutputStream srvOutStream = response.getOutputStream();
	        // 1 byte 기반 파일 출력 노드스트림 생성
	        // ServletOutputStream 은 바이너리 데이터를 웹 브라우저로 전송할 때 사용함 
	        
	        byte arrb[] = new byte[4096];    // 뒤에 [4096] 은 마음대로 설정 가능 지금은 4kbyte
	        int data = 0;
	        while( (data = finStream.read(arrb, 0, arrb.length)) != -1 ) {    // 0 은 시작값 arrb.length 은 그 배열 크기만큼 읽어들인다. 
	        	srvOutStream.write(arrb, 0, data);   // 값을 쌓는다.
	        } // end of while
	        
	        srvOutStream.flush();   // 쌓인것을 출력해준다.
	        
	        srvOutStream.close();
	        finStream.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
        
	}

}
