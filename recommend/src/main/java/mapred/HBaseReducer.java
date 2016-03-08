package mapred;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.io.NullWritable;

import model.Paper;
import util.Constants;
import util.StringSplitter;

public class HBaseReducer extends TableReducer<NullWritable, Paper, ImmutableBytesWritable> implements Constants{
	@Override
	protected void reduce(NullWritable key, Iterable<Paper> list, Context context)
					throws IOException, InterruptedException {

		Iterator<Paper> iter = list.iterator();
		while(iter.hasNext()){
			Paper paper = iter.next();
			if( paper.title.equals("") ) continue;
			
			// 以文献标题为row key
			Put put = new Put(paper.title.getBytes(CHARSET));
			// 填充put数据
			paper.fillIn(put);
			// 写入cite表
			context.write(new ImmutableBytesWritable("cite".getBytes(CHARSET)), put);
		}
	}
}
