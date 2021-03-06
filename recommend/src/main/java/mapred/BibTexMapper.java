package mapred;

import java.io.IOException;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import model.Paper;
import util.BibTexParser;


public class BibTexMapper extends Mapper<Object, Text, NullWritable, Paper>{
	@Override
	protected void map(Object key, Text value, Context context)
			throws IOException, InterruptedException {
		Paper paper = BibTexParser.parse(value.toString());
		if(paper == null) return;
		
		context.write(NullWritable.get(), paper);
	}
}
