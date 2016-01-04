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
		Paper paper = new APAParser(value.toString()).parse();
		if(paper == null) return;
		context.write(NullWritable.get(), paper);
	}
}