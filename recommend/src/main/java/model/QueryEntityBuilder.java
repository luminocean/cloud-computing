package model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class QueryEntityBuilder {

	private static final double INCREASE_WEIGHT = 1.2;

	public QueryEntity build(Set<Paper> paperSet) {

		QueryEntity queryEntity = new QueryEntity();

		for (Paper p : paperSet) {
			if (p == null) {
				continue;
			}
			buildQueryEntityByQueryPaper(queryEntity, p);
		}

		return queryEntity;

	}

	private void buildQueryEntityByQueryPaper(QueryEntity queryEntity, Paper paper) {

		if (paper == null || queryEntity == null) {
			return;
		}

		Map<String, Integer> names = InfoExtractUtil.extract(paper.author);

		if (names != null) {

			for (Entry<String, Integer> entry : names.entrySet()) {
				String key = entry.getKey();

				// 如果已经存在，加大权值
				if (queryEntity.authers.containsKey(key)) {
					queryEntity.authers.put(key,
							(int) ((queryEntity.authers.get(key) + entry.getValue()) * INCREASE_WEIGHT));
				} else {
					queryEntity.authers.put(key, entry.getValue());
				}
			}
		}

		Map<String, Integer> titleKeywords = InfoExtractUtil.extract(paper.title);

		if (titleKeywords != null) {

			for (Entry<String, Integer> entry : titleKeywords.entrySet()) {
				String key = entry.getKey();

				// 如果已经存在，加大权值
				if (queryEntity.titleKeywords.containsKey(key)) {
					queryEntity.titleKeywords.put(key,
							(int) ((queryEntity.titleKeywords.get(key) + entry.getValue()) * INCREASE_WEIGHT));
				} else {
					queryEntity.titleKeywords.put(key, entry.getValue());
				}
			}
		}
	}

	private static class InfoExtractUtil {

		private static final String[] stopWords = { "inproceedings", "title", "journal", "year", "author", "booktitle",
				"volume", "pages", "from", "and", "of", "for", "a", "an", "the", "by", "with" };
		private static HashSet<String> stopSet = new HashSet<>();

		static {
			// 初始化过滤字符集作为黑名单
			for (String word : stopWords) {
				stopSet.add(word);
			}
		}

		/**
		 * 提取信息
		 * 
		 * @param s1
		 * @return
		 */
		public static Map<String, Integer> extract(String s) {

			if (s == null || "".equals(s)) {
				return null;
			}

			String[] splits = s.split(" ");

			if (splits == null) {
				return null;
			}

			Map<String, Integer> map = new HashMap<String, Integer>();

			for (String str : splits) {

				if (str == null || "".equals(str)) {
					continue;
				}

				str = str.toLowerCase();// 统一全小写
				if (str.length() >= 3 && !stopSet.contains(str)) {

					// 记频率
					if (map.get(str) == null) {
						map.put(str, 1);
					} else {
						map.put(str, map.get(str) + 1);
					}
				}
			}

			return map;
		}
	}
}
