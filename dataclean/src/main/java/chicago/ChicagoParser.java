package chicago;

import paper.Paper;
import util.Parser;

public class ChicagoParser extends Parser{
	private static final String AUTHOR_REG = "(.+?\\.\\s)“";
	private static final String TITLE_REG = "(“.+?\\.”)";
	private static final String JOURNAL_REG = "(.+\\s)\\d+\\s\\(\\d+\\)";
	private static final String VOLUME_REG = "(\\d+\\s)\\(\\d+\\)";
	private static final String YEAR_REG = "(\\(\\d+\\))";
	private static final String PAGE_REG = "([-\\d]+\\.)";
	private static final String BOOK_TITLE_REG = "(.+)\\s\\(\\d+\\)";
	
	public ChicagoParser(String text) {
		super(text);
	}

	/**
	 * BibTeX的type只涉及article和inproceedings两种，对应的Chicago格式分别是
	 * article: Last Name, First Name. "Article Title." Journal Name Volume Number (Year Published): Page Numbers.
	 * inproceedings: Last Name, First Name. "Title of Book." Publisher Name (Year Published).
	 */
	@Override
	public Paper parse() {
		String testContent = text.replace("\n", "").replace(" ", "").replace("\t", "");
		if(testContent.length() == 0) return null;
		
		Paper paper = new Paper();
		paper.author = findField(AUTHOR_REG).map(this::trim).orElse("");		
		paper.title = findField(TITLE_REG).map(this::trim).orElse("");
		paper.journal = findField(JOURNAL_REG).map(this::trim).orElse("");
		paper.type = paper.journal.equals("") ? "inproceedings" : "article";
		paper.volume = findField(VOLUME_REG).map(this::trim).orElse("");
		paper.bookTitle = findField(BOOK_TITLE_REG).map(this::trim).orElse("");
		paper.year = findField(YEAR_REG).map(this::trim).orElse("");
		paper.page = findField(PAGE_REG).map(this::trim).orElse("");
		
		paper.brief = "";
		return paper;
	}
	
	public static void main(String args[]) {
		String text1 = "Leggetter, C. J. and Philip C. Woodland. “Maximum likelihood linear regression for speaker adaptation of continuous density hidden Markov models.” Computer Speech & Language 9 (1995): 171-185.";
		System.out.println(new ChicagoParser(text1).parse());
		String text2 = "Joachims, Thorsten. “Training linear SVMs in linear time.” KDD (2006).";
		System.out.println(new ChicagoParser(text2).parse());
	}
}
