package paper;

import java.io.IOException;
import java.util.HashMap;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.io.NullWritable;

@SuppressWarnings(value={"deprecation","unused"})
public class HBaseReducer extends TableReducer<NullWritable, Paper, ImmutableBytesWritable> {
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

	private static boolean test = false;
	HashMap<String, String> m = new HashMap<String,String>(); 

	public void reduce(NullWritable nw, Iterable<Paper> papers, Context context)
			throws IOException, InterruptedException {
		int i = 0;
		for (Paper paper : papers) {
			byte[] rowKey = DigestUtils.md5((paper.title+paper.year).replaceAll("[^a-zA-Z0-9]*", "").getBytes("UTF-8"));
			Put put = new Put(rowKey);
			byte[] columnFamily = paper.PAPER_TYPE.toString().getBytes();
			
//			if(m.get(paper.title+paper.PAPER_TYPE)==null){
//				m.put(paper.title+paper.PAPER_TYPE, paper.title+paper.PAPER_TYPE);
//			}else{
//				System.out.println("==>"+paper.title);
//				i++;
//			}
//			if(paper.title.equals("")){
//				i++;
//				System.out.println(paper.PAPER_TYPE + " of " + "author:"+paper.author);
//			}
			test = (!paper.type.equals("")) 
					? (put.add(columnFamily, type, paper.type.toString().getBytes("UTF-8"))) != null: false;
			test = (!paper.brief.equals(""))
					? (put.add(columnFamily, brief, paper.brief.toString().getBytes("UTF-8"))) != null : false;
			test = (!paper.title.equals(""))
					? (put.add(columnFamily, title, paper.title.toString().getBytes("UTF-8"))) != null : false;
			test = (!paper.author.equals(""))
					? (put.add(columnFamily, author, paper.author.toString().getBytes("UTF-8"))) != null : false;
			test = (!paper.bookTitle.equals(""))
					? (put.add(columnFamily, booktitle, paper.bookTitle.toString().getBytes("UTF-8"))) != null : false;
			test = (!paper.year.equals("")) 
					? (put.add(columnFamily, year, paper.year.toString().getBytes("UTF-8"))) != null: false;
			test = (!paper.journal.equals(""))
					? (put.add(columnFamily, journal, paper.journal.toString().getBytes("UTF-8"))) != null : false;
			test = (!paper.volume.equals(""))
					? (put.add(columnFamily, volume, paper.volume.toString().getBytes("UTF-8"))) != null : false;
			test = (!paper.page.equals("")) 
					? (put.add(columnFamily, page, paper.page.toString().getBytes("UTF-8"))) != null: false;
			context.write(new ImmutableBytesWritable(rowKey), put);
		}
		System.out.println("blank==>" + i);
	}
}