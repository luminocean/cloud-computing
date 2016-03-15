package action;

import com.opensymphony.xwork2.ActionSupport;

public class RecommendAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public String recommend() throws Exception{
		System.out.println("hello");
		return "success";
		
	}

}
