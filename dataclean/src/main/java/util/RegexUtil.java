package util;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtil {
	/**
	 * 在给定文本中寻找匹配正则的字符串
	 * @param text
	 * @param regexStr
	 * @return
	 */
	public static Optional<String> findField(String text, String regexStr){
		Pattern p = Pattern.compile(regexStr);
		Matcher m = p.matcher(text);
		if(m.find()){
			String result = m.group(1);
			return Optional.of(result);
		}
		return Optional.empty();
	}
}
