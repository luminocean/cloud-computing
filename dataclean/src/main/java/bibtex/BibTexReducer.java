package bibtex;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import paper.Paper;

public class BibTexReducer extends Reducer<NullWritable, Paper, NullWritable, Paper>{
	@Override
	protected void reduce(NullWritable nw, Iterable<Paper> papers,
			Context context) throws IOException, InterruptedException {
		for(Paper paper: papers){
			System.out.println(paper);
			context.write(NullWritable.get(), paper);
		}	
	}
}
