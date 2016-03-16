package feedback;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

import model.Paper;

public class MemoryFeedback implements Feedback {
	private Map<String, Integer> likeMap;
	private Map<String, Integer> dislikeMap;
	
	public MemoryFeedback() {
		likeMap = new HashMap<>();
		dislikeMap = new HashMap<>();
	}
	
	/**
	 * 计算2 * numLiked / (numLiked + numDisliked)
	 * numLiked和numDisliked默认都是1
	 * @param key 作为索引的paper
	 * @param recommended 被推荐的paper
	 */
	public float getLikeness(Collection<Paper> keys, Paper recommended) {
		TreeSet<Paper> set = toSet(keys);
		
		String keyword = toString(set, recommended);
		int numLiked = likeMap.getOrDefault(keyword, 1);
		int numDisliked = dislikeMap.getOrDefault(keyword, 1);
		if(numLiked == 0 || numDisliked == 0) {
			return 1;
		}
		return 2f * numLiked / (numLiked + numDisliked);
	}
	
	/**
	 * up vote
	 */
	@Override
	public void like(Collection<Paper> keys, Paper recommended) {
		String keyword = toString(keys, recommended);
		if(!likeMap.containsKey(keyword)) {
			likeMap.put(keyword, 1);
		}
		likeMap.put(keyword, likeMap.get(keyword) + 1);
	}

	/**
	 * down vote
	 */
	@Override
	public void dislike(Collection<Paper> keys, Paper recommended) {
		String keyword = toString(keys, recommended);
		if(!dislikeMap.containsKey(keyword)) {
			dislikeMap.put(keyword, 1);
		}
		dislikeMap.put(keyword, dislikeMap.get(keyword) + 1);
	}
	
	private String toString(Collection<Paper> paperSet, Paper recommended) {
		StringBuilder builder = new StringBuilder();
		for(Paper paper : paperSet) {
			builder.append(paper.title);
			builder.append("&");
		}
		builder.append(recommended.title);
		return builder.toString();
	}

	private TreeSet<Paper> toSet(Collection<Paper> papers) {
		TreeSet<Paper> paperSet = new TreeSet<>(papers);
		return paperSet;
	}
}
