package core;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.io.NullWritable;

public class HBaseReducer extends TableReducer<NullWritable, Paper, ImmutableBytesWritable>{
	@Override
	protected void reduce(NullWritable key, Iterable<Paper> list, Context context)
					throws IOException, InterruptedException {

		Iterator<Paper> iter = list.iterator();
		while(iter.hasNext()){
			Paper paper = iter.next();
			if( paper.title == null || paper.title.equals("") )
				continue;
			
			// 将paper原始字符串写入cite表中
			Put put = new Put(paper.title.getBytes("utf-8"));
			put.addColumn("src".getBytes("utf-8"), "content".getBytes("utf-8"), paper.src.getBytes("utf-8"));
			context.write(new ImmutableBytesWritable("cite".getBytes("utf-8")), put);
			
			// 进行文本拆分，建立倒排索引
			
		}
	}
}
