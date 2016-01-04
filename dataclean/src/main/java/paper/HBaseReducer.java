package paper;

import java.io.IOException;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.io.NullWritable;

public class HBaseReducer extends TableReducer<NullWritable, Paper, ImmutableBytesWritable> {
	// TODO: 这里的colomnFamily和colomn都是硬编码的，是否合适？
	// 文献属性
	public static final byte[] type = "type".getBytes();
	public static final byte[] brief = "brief".getBytes();
	public static final byte[] title = "title".getBytes();
	public static final byte[] author = "author".getBytes();
	public static final byte[] booktitle = "booktitle".getBytes();
	public static final byte[] year = "year".getBytes();
	public static final byte[] journal = "journal".getBytes();
	public static final byte[] volume = "volumn".getBytes();
	public static final byte[] page = "page".getBytes();

	@SuppressWarnings("unused")
	private static boolean test = false;

	@SuppressWarnings("deprecation")
	public void reduce(NullWritable nw, Iterable<Paper> papers, Context context)
			throws IOException, InterruptedException {
		for (Paper paper : papers) {
			byte[] rowKey = DigestUtils.md5(paper.title.replaceAll("[^a-zA-Z0-9]*", "").getBytes("UTF-8"));
			Put put = new Put(rowKey);
			byte[] columnFamily = paper.PAPER_TYPE.toString().getBytes();

			put.add(columnFamily, type, paper.type.toString().getBytes("UTF-8"));
			test = (paper.brief == "")
					? (put.add(columnFamily, brief, paper.brief.toString().getBytes("UTF-8"))) != null : false;
			test = (paper.title == "")
					? (put.add(columnFamily, title, paper.title.toString().getBytes("UTF-8"))) != null : false;
			test = (paper.author == "")
					? (put.add(columnFamily, author, paper.author.toString().getBytes("UTF-8"))) != null : false;
			test = (paper.bookTitle == "")
					? (put.add(columnFamily, booktitle, paper.bookTitle.toString().getBytes("UTF-8"))) != null : false;
			test = (paper.year == "") 
					? (put.add(columnFamily, year, paper.year.toString().getBytes("UTF-8"))) != null: false;
			test = (paper.journal == "")
					? (put.add(columnFamily, journal, paper.journal.toString().getBytes("UTF-8"))) != null : false;
			test = (paper.volume == "")
					? (put.add(columnFamily, volume, paper.volume.toString().getBytes("UTF-8"))) != null : false;
			test = (paper.page == "") 
					? (put.add(columnFamily, page, paper.page.toString().getBytes("UTF-8"))) != null: false;
			context.write(new ImmutableBytesWritable(rowKey), put);
		}
	}
}