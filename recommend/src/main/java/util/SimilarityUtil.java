package util;

import java.util.HashSet;

public class SimilarityUtil {
	private static final String[] stopWords = 
		{"inproceedings", "title", "journal", "year", "author", "booktitle", "volume", "pages", 
				"from", "and", "of", "for", "a", "an", "the", "by", "with"};
	private static HashSet<String> stopSet = new HashSet<>();
	static{
		// 初始化过滤字符集作为黑名单
		for(String word: stopWords){
			stopSet.add(word);
		}
	}
	
	/**
	 * 计算两个字符串之间的相似度
	 * @param s1
	 * @param s2
	 * @return
	 */
	public static int stringSimilarity(String s1, String s2){
		String[] splits1 = s1.split(" ");
		String[] splits2 = s2.split(" ");
		
		HashSet<String> set = new HashSet<>();
		for(String str: splits1){
			set.add(str);
		}
		
		int similarity = 0;
		for(String str: splits2){
			if( str.length() >=3 && set.contains(str) && !stopSet.contains(str)){
				similarity++;
			}
		}
		
		return similarity;
	}
}
