package bibtex;

import paper.Paper;
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
		String field = "";
		
		paper.author = (field=RegexUtil.findField(text, AUTHOR_REG)) == null ? "" : field;
		paper.bookTitle = (field=RegexUtil.findField(text, BOOK_TITLE_REG)) == null ? "" : field;
		paper.brief = (field=RegexUtil.findField(text, BRIEF_REG)) == null ? "" : field;
		paper.journal = (field=RegexUtil.findField(text, JOURNAL_REG)) == null ? "" : field;
		paper.page = (field=RegexUtil.findField(text, PAGE_REG)) == null ? "" : field;
		paper.title = (field=RegexUtil.findField(text, TITLE_REG)) == null ? "" : field;
		paper.type = (field=RegexUtil.findField(text, TYPE_REG)) == null ? "" : field;
		paper.volume = (field=RegexUtil.findField(text, VOLUME_REG)) == null ? "" : field;
		paper.year = (field=RegexUtil.findField(text, YEAR_REG)) == null ? "" : field;
		
		return paper;
	}
}
