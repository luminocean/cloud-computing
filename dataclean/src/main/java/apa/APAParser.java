package apa;

import paper.Paper;
import util.Parser;

public class APAParser extends Parser{
	private static final String AUTHOR_REG = "(.+?\\.\\s)";
	private static final String YEAR_REG = "(\\(\\d+?\\)\\.\\s)";
	private static final String TITLE_REG = "(.+?\\.\\s)";
	private static final String JOURNAL_REG = "(.+?\\,\\s)";
	private static final String VOLUME_REG = "(\\d+?,\\s)";
	private static final String PAGE_REG = "([-\\d]+?\\.)";
	private static final String BOOK_TITLE_REG = "(.+?\\.)";
	
	public APAParser(String text) {
		super(text);
	}

	public Paper parse() {
		String testContent = text.replace("\n", "").replace(" ", "").replace("\t", "");
		if(testContent.length() == 0) return null;
		
		Paper paper = new Paper();
		paper.author = findField(AUTHOR_REG).map(this::trim).orElse("");		
		paper.year = findField(YEAR_REG).map(this::trim).orElse("");
		paper.title = findField(TITLE_REG).map(this::trim).orElse("");
		paper.journal = findField(JOURNAL_REG).map(this::trim).orElse("");
		paper.volume = findField(VOLUME_REG).map(this::trim).orElse("");
		paper.page = findField(PAGE_REG).map(this::trim).orElse("");
		paper.bookTitle = findField(BOOK_TITLE_REG).map(this::trim).orElse("");
		paper.type = paper.bookTitle.equals("") ? "article" : "inproceedings";
		paper.brief = "";
		return paper;
	}

//	public static void main(String args[]) {
//		String text1 = "Brin, S., & Page, L.. (1998). Reprint of: The anatomy of a large-scale hypertextual web search engine. Computer Networks, 56, 3825-3833.";
//		System.out.println(new APAParser(text1).parse());
//		String text2 = "Chen, D.M., Chandrasekhar, V., Girod, B., Singh, J.P., Tsai, S.S., & Takacs, G.. (2009). Location coding for mobile image retrieval. MOBIMEDIA.";
//		System.out.println(new APAParser(text2).parse());
//	}
}
