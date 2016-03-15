package feedback;

import model.Paper;
import util.Constants;

public interface FeedBack extends Constants{
	
	public void like(Paper key, Paper recommended);
	
	public void dislike(Paper key, Paper recommended);
	
	public float getLikeness(Paper key, Paper recommended);
}
