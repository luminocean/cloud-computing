package action;

import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

import model.Paper;
import recommend.Recommend;
import util.Pair;

public class RecommendAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Recommend rec;
	private List<Pair<Integer, Paper>> list;

	public String recommend() throws Exception {
		rec = Recommend.getInstance();
		ActionContext context = ActionContext.getContext();
		HttpServletRequest request = (HttpServletRequest) context.get(ServletActionContext.HTTP_REQUEST);
		HttpSession session = request.getSession();

		String query = request.getParameter("literature");
		Collection<Paper> queryPapers = Recommend.parseQuery(query);
		list = rec.recommend(query, 20);
		session.setAttribute("queryPapers", queryPapers);
		session.setAttribute("list", list);

		return "success";
	}

	public List<Pair<Integer, Paper>> getList() {
		return list;
	}

	public void setList(List<Pair<Integer, Paper>> list) {
		this.list = list;
	}

}
