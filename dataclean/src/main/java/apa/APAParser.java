package apa;

import paper.Paper;
import util.Parser;

public class APAParser extends Parser{
	private static final String AUTHOR_REG = "(.+?\\.\\s)";
	private static final String YEAR_REG = "(\\(\\d+?\\)\\.\\s)";
	private static final String TITLE_REG = "(.+?\\.)";
	private static final String JOURNAL_REG = "(.+?\\,\\s)\\d+,";
	private static final String VOLUME_REG = "(\\d+?,\\s)";
	private static final String PAGE_REG = "([-\\d]+?\\.)";
	private static final String BOOK_TITLE_REG = "(.+?\\.)";
	
	public APAParser(String text) {
		super(text);
	}

	/**
	 * BibTeX的type只有article和inproceedings两种，对应的APA格式是
	 * article: Author, A.A.. (Year, month of Publication). Article title. Magazine Title, Volume, pp.-pp.
	 * inproceedings: Author, A.A.. (Year of Publication). Title of work. Publisher.
	 * 
	 * Paper的bookTitle就是Publisher
	 */
	@Override
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
}
