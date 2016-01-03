package apa;

import java.io.IOException;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import paper.Paper;

public class APAMapper extends Mapper<Object, Text, NullWritable, Paper>{
	@Override
	protected void map(Object key, Text value, Context context)
			throws IOException, InterruptedException {
//		System.out.print(value.toString().substring(0, Math.min(value.toString().length(), 80)));
		Paper paper = new APAParser(value.toString()).parse();
//		System.out.println(paper);
		if(paper == null) return;
		
		context.write(NullWritable.get(), paper);
	}
	
}
