package mla;

import paper.Paper;
import util.Parser;

public class MLAParser extends Parser{
	private static final String AUTHOR_REG = "(.+?\\.\\s)“";
	private static final String TITLE_REG = "(“.+?\\.”)";
	private static final String JOURNAL_REG = "(.+\\s)\\d+\\s\\(\\d+\\)";
	private static final String VOLUME_REG = "(\\d+\\s)\\(\\d+\\)";
	private static final String YEAR_REG = "(\\(\\d+\\))";
	private static final String PAGE_REG = "([-\\d]+\\.)";
	private static final String BOOK_TITLE_REG = "(.+)\\s\\(\\d+\\)";
	
	public MLAParser(String text) {
		super(text);
	}

	/**
	 * BibTeX的type只涉及article和inproceedings两种，对应的MLA格式分别是
	 * article: First author, Second author. et al. "Article Title." Journal Name Volume Number (Year Published): Page Numbers.
	 * inproceedings: First author, Second author. "Title of Book." Publisher Name (Year Published).
	 * article有属性:title, author, journal, year, volume, pages
	 * inproceedings有属性:title, author, booktitle, year
	 * 简单一些可以根据是否有journal来区分type具体为article还是inproceedings
	 */
	@Override
	public Paper parse() {
		String testContent = text.replace("\n", "").replace(" ", "").replace("\t", "");
		if(testContent.length() == 0) return null;
		
		Paper paper = new Paper();
		paper.PAPER_TYPE = "mla";
		paper.author = findField(AUTHOR_REG).map(this::trim).orElse("");
		paper.title = findField(TITLE_REG).map(this::trim).orElse("");
		paper.journal = findField(JOURNAL_REG).map(this::trim).orElse("");
		boolean isArticle = !paper.journal.equals("");
		paper.type = isArticle ? "article" : "inproceedings";
		paper.volume = isArticle ? "":findField(VOLUME_REG).map(this::trim).orElse("");
		paper.bookTitle = isArticle ? "" : findField(BOOK_TITLE_REG).map(this::trim).orElse("");
		paper.year = findField(YEAR_REG).map(this::trim).orElse("");
		paper.page = isArticle ? "" : findField(PAGE_REG).map(this::trim).orElse("");
		paper.brief = "";
		return paper;
	}
	
	public static void main(String args[]) {
		String articleText = "Rubner, Yossi et al. “The Earth Mover's Distance as a Metric for Image Retrieval.” International Journal of Computer Vision 40 (2000): 99-121.";
		System.out.println(new MLAParser(articleText).parse());

		String inproceedingsText = "Bradshaw, Ben. “Semantic based image retrieval: a probabilistic approach.” MM (2000).";
		System.out.println(new MLAParser(inproceedingsText).parse());

	}
}
