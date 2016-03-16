package action;

import java.util.ArrayList;
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

public class LikeOrNotAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Recommend rec;

	@SuppressWarnings("unchecked")
	public String likeOrNot() throws Exception {
		rec = Recommend.getInstance();
		ActionContext context = ActionContext.getContext();
		HttpServletRequest request = (HttpServletRequest) context.get(ServletActionContext.HTTP_REQUEST);
		HttpSession session = request.getSession();

		String radio = request.getParameter("str");
		List<Integer> likeList = new ArrayList<>();
		List<Integer> dislikeList = new ArrayList<>();
		List<Pair<Integer, Paper>> list = (List<Pair<Integer, Paper>>) session.getAttribute("list");
		Collection<Paper> queryPapers = (Collection<Paper>) session.getAttribute("queryPapers");
		for (int i = 0; i < radio.length(); i++) {
			if (radio.charAt(i) == '4') {
				likeList.add(i);
			} else if (radio.charAt(i) == '9') {
				dislikeList.add(i);
			}
		}
		for (Integer pos : likeList) {
			rec.getFeedBack().like(queryPapers, list.get(pos).getValue());
		}

		for (Integer pos : dislikeList) {
			rec.getFeedBack().dislike(queryPapers, list.get(pos).getValue());
		}

		return "success";
	}

}
