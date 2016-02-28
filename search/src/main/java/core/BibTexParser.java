package core;

import util.RegexUtil;

public class BibTexParser {
	public static final String TYPE_REG = "@(\\w+?)\\{";
	public static final String BRIEF_REG = "@\\w+\\{(.+?),";
	public static final String TITLE_REG = "title=\\{(.+?)\\}";
	public static final String AUTHOR_REG = "author=\\{(.+?)\\}";
	public static final String BOOK_TITLE_REG = "booktitle=\\{(.+?)\\}";
	public static final String YEAR_REG = "year=\\{(.+?)\\}";
	public static final String JOURNAL_REG = "journal=\\{(.+?)\\}";
	public static final String VOLUME_REG = "volume=\\{(.+?)\\}";
	public static final String PAGE_REG = "pages=\\{(.+?)\\}";
	
	/**
	 * 将一个BibTex文献文本解析成一个键值对数据
	 * @param text
	 * @return
	 */
	public static Paper parse(String text){
		// 取出实际内容
		String testContent = text.replace("\n", "").replace(" ", "").replace("\t", "");
		if(testContent.length() == 0) return null;
		
		Paper paper = new Paper();
		
		paper.author = RegexUtil.findField(text, AUTHOR_REG).orElse("");
		paper.bookTitle = RegexUtil.findField(text, BOOK_TITLE_REG).orElse("");
		paper.brief = RegexUtil.findField(text, BRIEF_REG).orElse("");
		paper.journal = RegexUtil.findField(text, JOURNAL_REG).orElse("");
		paper.page = RegexUtil.findField(text, PAGE_REG).orElse("");
		paper.title = RegexUtil.findField(text, TITLE_REG).orElse("");
		paper.type = RegexUtil.findField(text, TYPE_REG).orElse("");
		paper.volume = RegexUtil.findField(text, VOLUME_REG).orElse("");
		paper.year = RegexUtil.findField(text, YEAR_REG).orElse("");
		paper.src = text;
		
		return paper;
	}
}
