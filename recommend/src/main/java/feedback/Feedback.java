package feedback;

import java.util.Collection;

import model.Paper;
import util.Constants;

public interface Feedback extends Constants{
	
	public void like(Collection<Paper> keys, Paper recommended);
	
	public void dislike(Collection<Paper> keys, Paper recommended);
	
	public float getLikeness(Collection<Paper> keys, Paper recommended);
}
