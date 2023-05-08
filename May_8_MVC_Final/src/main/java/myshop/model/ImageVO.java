package myshop.model;

// DTO 는 DATA Transfer Object == VO 는 VALUE Object 이다.  둘다 같은 말이다. 

public class ImageVO {

	private int imgno;
	private String imgfilename;
	
	
	public int getImgno() {
		return imgno;
	}
	public void setImgno(int imgno) {
		this.imgno = imgno;
	}
	public String getImgfilename() {
		return imgfilename;
	}
	public void setImgfilename(String imgfilename) {
		this.imgfilename = imgfilename;
	}
	
	
	
	
}
