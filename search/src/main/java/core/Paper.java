package core;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.io.Writable;

public class Paper implements Writable{
	public String CHARSET = "utf-8";
	public String author;
	public String bookTitle;
	public String brief;
	public String journal;
	public String page;
	public String title;
	public String type;
	public String volume;
	public String year;
	public String src;
	
	@Override
	public void write(DataOutput out) throws IOException {
		out.writeUTF(author);
		out.writeUTF(bookTitle);
		out.writeUTF(brief);
		out.writeUTF(journal);
		out.writeUTF(page);
		out.writeUTF(title);
		out.writeUTF(type);
		out.writeUTF(volume);
		out.writeUTF(year);
		out.writeUTF(src);
	}
	
	@Override
	public void readFields(DataInput in) throws IOException {
		author = in.readUTF();
		bookTitle = in.readUTF();
		brief = in.readUTF();
		journal = in.readUTF();
		page = in.readUTF();
		title = in.readUTF();
		type = in.readUTF();
		volume = in.readUTF();
		year = in.readUTF();
		src = in.readUTF();
	}
	
	public void fillIn(Put put) {
		try {
			// 填充paper部分
			put.addColumn("paper".getBytes(CHARSET), "src".getBytes(CHARSET), src.getBytes(CHARSET));
			put.addColumn("paper".getBytes(CHARSET), "author".getBytes(CHARSET), author.getBytes(CHARSET));
			put.addColumn("paper".getBytes(CHARSET), "brief".getBytes(CHARSET), brief.getBytes(CHARSET));
			put.addColumn("paper".getBytes(CHARSET), "title".getBytes(CHARSET), title.getBytes(CHARSET));
			put.addColumn("paper".getBytes(CHARSET), "year".getBytes(CHARSET), year.getBytes(CHARSET));

			// 填充article部分
			put.addColumn("paper".getBytes(CHARSET), "journal".getBytes(CHARSET), journal.getBytes(CHARSET));
			put.addColumn("paper".getBytes(CHARSET), "volume".getBytes(CHARSET), volume.getBytes(CHARSET));
			put.addColumn("paper".getBytes(CHARSET), "pages".getBytes(CHARSET), page.getBytes(CHARSET));

			// 填充inproceeding部分
			put.addColumn("paper".getBytes(CHARSET), "booktitle".getBytes(CHARSET), bookTitle.getBytes(CHARSET));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String toString() {
		return "[" + "author=" + author + 
				", title=" + title +
				", type=" + type +
				", bookTitle=" + bookTitle + 
				", journal=" + journal + 
				", brief=" + brief + 
				", volume=" + volume +
				", page=" + page + 
				", year=" + year + "]";
	}
}
