package util;

import java.util.Map;

public class SimilarityUtil {

	/**
	 * 计算两个字符串之间的相似度
	 * 
	 * @param s1
	 * @param s2
	 * @return
	 */
	public static int stringSimilarity(String s1, Map<String, Integer> map) {
		int similarity = 0;

		if (s1 == null || "".equals(s1)) {
			return similarity;
		}

		String[] splits1 = s1.split(" ");

		if (splits1 == null) {
			return similarity;
		}

		for (String str : splits1) {
			if (str == null) {
				continue;
			}

			str = str.toLowerCase();
			if (str.length() >= 3 && map.containsKey(str)) {
				similarity += map.get(str);
			}
		}

		return similarity;
	}
}
