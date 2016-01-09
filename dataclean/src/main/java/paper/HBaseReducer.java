package paper;

import java.io.IOException;
import java.util.HashMap;

import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.io.MD5Hash;
import org.apache.hadoop.io.Text;

public class HBaseReducer extends TableReducer<Text, Paper, MD5Hash> {
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

	HashMap<String, String> m = new HashMap<String,String>(); 

	public void reduce(Text citeType, Iterable<Paper> papers, Context context)
			throws IOException, InterruptedException {
		int i = 0;
		for (Paper paper : papers) {
			MD5Hash rowKey = MD5Hash.digest(paper.title);
			Put put = new Put(rowKey.getDigest());
			byte[] columnFamily = citeType.toString().getBytes();
						
			if(!paper.type.equals("")) 
				put.addColumn(columnFamily, type, paper.type.toString().getBytes("UTF-8"));
			if(!paper.brief.equals(""))
				put.addColumn(columnFamily, brief, paper.brief.toString().getBytes("UTF-8"));
			if(!paper.title.equals(""))
				put.addColumn(columnFamily, title, paper.title.toString().getBytes("UTF-8"));
			if(!paper.author.equals(""))
				put.addColumn(columnFamily, author, paper.author.toString().getBytes("UTF-8"));
			if(!paper.bookTitle.equals(""))
				put.addColumn(columnFamily, booktitle, paper.bookTitle.toString().getBytes("UTF-8"));
			if(!paper.year.equals("")) 
				put.addColumn(columnFamily, year, paper.year.toString().getBytes("UTF-8"));
			if(!paper.journal.equals(""))
				put.addColumn(columnFamily, journal, paper.journal.toString().getBytes("UTF-8"));
			if(!paper.volume.equals(""))
				put.addColumn(columnFamily, volume, paper.volume.toString().getBytes("UTF-8"));
			if(!paper.page.equals("")) 
				put.addColumn(columnFamily, page, paper.page.toString().getBytes("UTF-8"));
			context.write(rowKey, put);
			
//			if(paper.title.equals("")){
//				i++;
//				System.out.println(citeType + " of " + "author:"+paper.author);
//			}
		}
		System.out.println("blank==>" + i);
	}
}