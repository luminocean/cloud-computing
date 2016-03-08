package util;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class StringSplitter {
	private static final String[] escapeWords = 
		{"inproceedings", "title", "journal", "year", "author", "booktitle", "volume", "pages", 
				"from", "and", "of", "for", "a", "an", "the", "by", "with"};
	private static HashSet<String> escapeSet = new HashSet<>();
	
	static{
		// 初始化过滤字符集作为黑名单
		for(String word: escapeWords){
			escapeSet.add(word);
		}
	}
	
	// 将输入字符串拆分成只有纯西文单词的数组
	public static List<String> split(String str){
		String[] splits = str.split("[^a-zA-Z]");
		
		// 拆分后的字符串列表
		List<String> list = new LinkedList<>();
		for(String sp: splits){
			if( !sp.trim().equals("") ){
				list.add(sp.trim());
			}
		}
		
		// 过滤后返回
		return escape(list);
	}
	
	// 过滤要忽略的字符串
	private static List<String> escape(List<String> words){
		Iterator<String> iter = words.iterator();
		while(iter.hasNext()){
			String word = iter.next();
			// 按照黑名单和长度过滤
			if( escapeSet.contains(word) || word.length() < 3 ){
				iter.remove();
			}
		}
		return words;
	}
}
