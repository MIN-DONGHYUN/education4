package chap05;

public class PersonDTO_02 {

	private int seq;
	private String   name;    
	private String   school;     
	private String   color;     
	private String[] food;       
	private String   registerday;
	
	
	
	public int getSeq() {
		return seq;
	}
	public void setSeq(int seq) {
		this.seq = seq;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSchool() {
		return school;
	}
	public void setSchool(String school) {
		this.school = school;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String[] getFood() {
		return food;
	}
	public void setFood(String[] food) {
		this.food = food;
	}
	public String getRegisterday() {
		return registerday;
	}
	public void setRegisterday(String registerday) {
		this.registerday = registerday;
	}
	
	////////////////////////////////////////////////////////////////////////
	
	public String getStrFood() {
		
		
		if(food != null) {
			return String.join(",", food);    // , 로 연결해서 문자열로 만들어서 리턴함 
		}
		else {						// null 일 경우에는 없음을 표시한다.
			return "없음";
		}
		
	}// end of public String getStrFood() {

	
	
	public String getStrFood_imgFileName() {
	      
	    String result = null;

		// 음식이 존재한다면 
		if(food != null) {
			StringBuilder sb = new StringBuilder();
			
			
			
			// 음식 수만큼 돈다.
			 for(int i=0; i<food.length; i++) {
				
		            switch (food[i]) {
		               case "짜장면":  // 밸류값이 짜장면입니까....
		                  sb.append("jjm.png");
		                  break;
		                  
		               case "짬뽕":  
		                  sb.append("jjbong.png");
		                  break;
		                  
		               case "탕수육":  
		                  sb.append("tangsy.png");
		                  break;
		                  
		               case "양장피":  
		                  sb.append("yang.png");
		                  break;
		                  
		               case "팔보채":  
		                  sb.append("palbc.png");
		                  break;   
		            }// end of switch()------------------------------------------
		            

				
				if(i< food.length-1) {
					sb.append(",");
				}
				
			}// end of for  
			result = sb.toString();
		}  // end of if

		
		return result;
		
	} // end of public String getStrFood_imgFileName() {
	
	
	
}
