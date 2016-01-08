package chicago;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import paper.Paper;

public class ChicagoMapper extends Mapper<Object, Text, Text, Paper>{
	@Override
	protected void map(Object key, Text value, Context context)
			throws IOException, InterruptedException {
		Paper paper = new ChicagoParser(value.toString()).parse();
		if(paper == null) return;
		context.write(new Text("chicago"), paper);
	}
}