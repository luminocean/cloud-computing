package core;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Reducer;

public class BibTexReducer extends Reducer<NullWritable, Paper, NullWritable, Paper>{

	@Override
	protected void reduce(NullWritable key, Iterable<Paper> list, Context context) 
			throws IOException, InterruptedException {
		Iterator<Paper> iter = list.iterator();
		while(iter.hasNext()){
			Paper paper = iter.next();
			context.write(NullWritable.get(), paper);
		}
	}
}
