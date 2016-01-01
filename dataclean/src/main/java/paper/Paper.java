package paper;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;

public class Paper implements Writable{
	public String author;
	public String bookTitle;
	public String brief;
	public String journal;
	public String page;
	public String title;
	public String type;
	public String volume;
	public String year;
	
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
	}

	@Override
	public String toString() {
		return "Paper [author=" + author + ", bookTitle=" + bookTitle + ", brief=" + brief + ", journal=" + journal
				+ ", page=" + page + ", title=" + title + ", type=" + type + ", volume=" + volume + ", year=" + year
				+ "]";
	}
}
