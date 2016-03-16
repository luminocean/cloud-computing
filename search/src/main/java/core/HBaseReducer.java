package core;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.io.NullWritable;

import util.Constants;
import util.StringSplitter;

public class HBaseReducer extends TableReducer<NullWritable, Paper, ImmutableBytesWritable> implements Constants{
	@Override
	protected void reduce(NullWritable key, Iterable<Paper> list, Context context)
					throws IOException, InterruptedException {

		Iterator<Paper> iter = list.iterator();
		while(iter.hasNext()){
			Paper paper = iter.next();
			if( paper.title == null || paper.title.equals("") )
				continue;
			
			// 将paper原始字符串写入cite表中
			// MD5Hash rowKey = MD5Hash.digest(paper.title);
			Put put = new Put(paper.title.getBytes(CHARSET));
//			put.addColumn("src".getBytes(CHARSET), 
//					"content".getBytes(CHARSET), 
//					paper.src.getBytes(CHARSET));
			paper.fillIn(put);
			context.write(new ImmutableBytesWritable("cite".getBytes(CHARSET)), put);
			
			// 拆分源文本
			List<String> words = StringSplitter.split(paper.src);
			// 为每个单词建立倒排索引
			for(String word: words){
				put = new Put(word.toLowerCase().getBytes(CHARSET));
				put.addColumn("title".getBytes(CHARSET), 
						paper.title.getBytes(CHARSET), 
						"".getBytes(CHARSET));
				// 写入倒排索引表
				context.write(new ImmutableBytesWritable("index".getBytes(CHARSET)), put);
			}
		}
	}
}
