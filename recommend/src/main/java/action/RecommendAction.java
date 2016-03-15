package action;


import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class RecommendAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<String> list;
	
	public String recommend() throws Exception{
		ActionContext context=ActionContext.getContext(); 
		HttpServletRequest request = (HttpServletRequest)context.get(ServletActionContext.HTTP_REQUEST);     
        String bookName=request.getParameter("literature");    
		System.out.println(bookName);
		return "success";
	}

	public List<String> getList() {
		return list;
	}

	public void setList(List<String> list) {
		this.list = list;
	}

}
