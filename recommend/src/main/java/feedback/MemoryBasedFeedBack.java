package feedback;

import java.util.HashMap;
import java.util.Map;

import model.Paper;

public class MemoryBasedFeedBack implements FeedBack{
	private Map<String, Integer> likeMap;
	private Map<String, Integer> dislikeMap;
	
	public MemoryBasedFeedBack() {
		likeMap = new HashMap<>();
		dislikeMap = new HashMap<>();
	}
	
	private String keyword(Paper p, Paper q) {
		return p.title + "&" + q.title;
	}

	/**
	 * 计算numLiked / (numLiked + numDisliked)
	 * numLiked和numDisliked较小的情况，做特殊处理，如果numLiked和numDisliked至少一个为0，函数返回1
	 * @param key 作为索引的paper
	 * @param recommended 被推荐的paper
	 */
	@Override
	public float getLikeness(Paper key, Paper recommended) {
		String keyword = keyword(key, recommended);
		int numLiked = likeMap.getOrDefault(keyword, 0);
		int numDisliked = dislikeMap.getOrDefault(keyword, 0);
		if(numLiked == 0 || numDisliked == 0) {
			return 1;
		}
		return numLiked / (float)(numLiked + numDisliked);
	}

	/**
	 * up vote
	 */
	@Override
	public void like(Paper key, Paper recommended) {
		String keyword = keyword(key, recommended);
		if(!likeMap.containsKey(keyword)) {
			likeMap.put(keyword, 0);
		}
		likeMap.put(keyword, likeMap.get(keyword) + 1);
	}

	/**
	 * down vote
	 */
	@Override
	public void dislike(Paper key, Paper recommended) {
		String keyword = keyword(key, recommended);
		if(!dislikeMap.containsKey(keyword)) {
			dislikeMap.put(keyword, 0);
		}
		dislikeMap.put(keyword, dislikeMap.get(keyword) + 1);
	}
}
