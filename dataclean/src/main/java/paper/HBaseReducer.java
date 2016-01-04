package paper;

import java.io.IOException;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;

public class HBaseReducer extends TableReducer<NullWritable, Paper, ImmutableBytesWritable> {
	// TODO: 这里的colomnFamily和colomn都是硬编码的，是否合适？
	public static final byte[] CF = "paper".getBytes(); 
	public static final byte[] COL = "cite".getBytes();

	public void reduce(NullWritable nw, Iterable<Paper> papers, Context context)
			throws IOException, InterruptedException {
		for(Paper paper: papers){
			byte[] rowKey = DigestUtils.md5(paper.toString().getBytes("UTF-8"));
			Put put = new Put(rowKey);
			put.add(CF, COL, paper.toString().getBytes("UTF-8"));
			context.write(new ImmutableBytesWritable(rowKey), put);
		}
	}
}