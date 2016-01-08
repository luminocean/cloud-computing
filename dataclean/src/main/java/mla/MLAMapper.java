package mla;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import paper.Paper;

public class MLAMapper extends Mapper<Object, Text, Text, Paper> {
	@Override
	protected void map(Object key, Text value, Context context)
			throws IOException, InterruptedException {
		Paper paper = new MLAParser(value.toString()).parse();
		if (paper == null)
			return;
		context.write(new Text("mla"), paper);
	}
}