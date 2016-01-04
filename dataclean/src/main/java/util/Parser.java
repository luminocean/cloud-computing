package util;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import paper.Paper;

/**
 * 与BibTeX不同，APA解析需要记录上一个field结束的位置，因此parse方法不是static的。
 */
public abstract class Parser {
	protected int pos;
	protected String text;

	public Parser(String text) {
		this.text = text;
	}
	
	/**
	 * 在给定文本中寻找匹配正则的字符串
	 * a non static version.
	 * @param text
	 * @param regexStr
	 * @return
	 */
	protected Optional<String> findField(String regexStr) {
		Pattern p = Pattern.compile(regexStr);
		Matcher m = p.matcher(text.substring(pos));
		if(m.find()){
			String result = m.group(1);
			pos += m.end(1);
//			System.out.println(result + "@" + pos);
			return Optional.of(result);
		}
		return Optional.empty();
	}
		
	/**
	 * 去除结尾的空格，逗号，句号，以及两边的括号，全角双引号
	 * @param rawStr
	 * @return
	 */
	protected String trim(String rawStr) {
		String ret = rawStr.trim().replaceAll("[,\\.]$", "");
		if(ret.matches("^“.*”$")) {
			ret = ret.replaceAll("^“", "").replaceAll("”$", "");
		}
		if(ret.matches("^\\(.*\\)$")) {
			ret = ret.replaceAll("^\\(", "").replaceAll("\\)$", "");
		}
		return ret;
	}

	public abstract Paper parse();
}