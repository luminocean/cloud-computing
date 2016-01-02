package bibtex;

import java.io.IOException;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import paper.Paper;

public class BibTexMapper extends Mapper<Object, Text, NullWritable, Paper>{
	@Override
	protected void map(Object key, Text value, Context context)
			throws IOException, InterruptedException {
		Paper paper = BibTexParser.parse(value.toString());
		if(paper == null) return;
		
		System.out.println(paper.author);
		context.write(NullWritable.get(), paper);
	}
}
