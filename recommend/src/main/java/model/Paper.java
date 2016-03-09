package model;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.List;

import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.io.Writable;

import util.Constants;
import util.SimilarityUtil;

public class Paper implements Writable{
	public static final String CHARSET = Constants.CHARSET;
	
	// paper部分
	public String src; // 文献原文
	public String type; // 类型
	public String author;
	public String brief;
	public String title;
	public String year;
	
	// article部分
	public String journal;
	public String volume;
	public String pages;
	
	// inproceeding部分
	public String bookTitle;
	
	/**
	 * 创建一个空的paper对象
	 */
	public Paper() {}
	
	/**
	 * 使用HBase的查询结果创建Paper实例
	 * @param result
	 * @throws UnsupportedEncodingException
	 */
	public Paper(Result result) throws UnsupportedEncodingException {
		src = getValueFromResult(result, "src");
		type = getValueFromResult(result, "type");
		author = getValueFromResult(result, "author");
		brief = getValueFromResult(result, "brief");
		title = getValueFromResult(result, "title");
		year = getValueFromResult(result, "year");
		journal = getValueFromResult(result, "journal");
		volume = getValueFromResult(result, "volume");
		pages = getValueFromResult(result, "pages");
		bookTitle = getValueFromResult(result, "bookTitle");
	}

	/**
	 * 计算两个paper对象之间的相似度
	 * @param paper
	 * @return
	 */
	public int similarity(Paper paper){
		int similarity = 0;
		
		// type, brief, volume, pages不参与相似度计算
		similarity += SimilarityUtil.stringSimilarity(this.author, paper.author) * 3;
		similarity += SimilarityUtil.stringSimilarity(this.title, paper.title) * 2;
		similarity += SimilarityUtil.stringSimilarity(this.year, paper.year);
		similarity += SimilarityUtil.stringSimilarity(this.journal, paper.journal);
		similarity += SimilarityUtil.stringSimilarity(this.bookTitle, paper.bookTitle);
		
		// TODO: year字段目前是直接匹配，是否可以根据相近程度计算不同的相似度？
		
		return similarity;
	}
	
	/**
	 * 使用各数据域填充指定的put对象
	 * @param put
	 */
	public void fillIn(Put put){
		try {
			// 填充paper部分
			put.addColumn("paper".getBytes(CHARSET),
					"src".getBytes(CHARSET), src.getBytes(CHARSET));
			put.addColumn("paper".getBytes(CHARSET),
					"author".getBytes(CHARSET), author.getBytes(CHARSET));
			put.addColumn("paper".getBytes(CHARSET),
					"brief".getBytes(CHARSET), brief.getBytes(CHARSET));
			put.addColumn("paper".getBytes(CHARSET),
					"title".getBytes(CHARSET), title.getBytes(CHARSET));
			put.addColumn("paper".getBytes(CHARSET),
					"year".getBytes(CHARSET), year.getBytes(CHARSET));
			
			// 填充article部分
			put.addColumn("paper".getBytes(CHARSET),
					"journal".getBytes(CHARSET), journal.getBytes(CHARSET));
			put.addColumn("paper".getBytes(CHARSET),
					"volume".getBytes(CHARSET), volume.getBytes(CHARSET));
			put.addColumn("paper".getBytes(CHARSET),
					"pages".getBytes(CHARSET), pages.getBytes(CHARSET));

			// 填充inproceeding部分
			put.addColumn("paper".getBytes(CHARSET),
					"booktitle".getBytes(CHARSET), bookTitle.getBytes(CHARSET));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void write(DataOutput out) throws IOException {
		out.writeUTF(src);
		out.writeUTF(type);
		out.writeUTF(author);
		out.writeUTF(brief);
		out.writeUTF(title);
		out.writeUTF(year);
		out.writeUTF(journal);
		out.writeUTF(volume);
		out.writeUTF(pages);
		out.writeUTF(bookTitle);
	}
	
	@Override
	public void readFields(DataInput in) throws IOException {
		src = in.readUTF();
		type = in.readUTF();
		author = in.readUTF();
		brief = in.readUTF();
		title = in.readUTF();
		year = in.readUTF();
		journal = in.readUTF();
		volume = in.readUTF();
		pages = in.readUTF();
		bookTitle = in.readUTF();
	}

	// 从Result中取出String值
	private String getValueFromResult(Result result, String field) throws UnsupportedEncodingException {
		List<Cell> cells = result.getColumnCells("paper".getBytes(CHARSET), field.getBytes(CHARSET));
		if(cells.size() == 0 ) return "";
		
		String value = new String(CellUtil.cloneValue(cells.get(0)));
		return value;
	}
	
	@Override
	public String toString() {
		return "Paper [src=" + src + ", type=" + type + ", author=" + author + ", brief=" + brief + ", title=" + title
				+ ", year=" + year + ", journal=" + journal + ", volume=" + volume + ", pages=" + pages + ", booktitle="
				+ bookTitle + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if( !(obj instanceof Paper) || this.title == null )
			return false;
		
		return this.title.equals(((Paper)obj).title);
	}
}
