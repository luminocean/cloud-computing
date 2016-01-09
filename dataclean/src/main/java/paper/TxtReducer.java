package paper;

import java.io.IOException;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;

public class TxtReducer extends Reducer<Text, Paper, NullWritable, Paper>{
	private MultipleOutputs<NullWritable, Paper> mout;
	
	@Override
	protected void setup(Reducer<Text, Paper, NullWritable, Paper>.Context context)
			throws IOException, InterruptedException {
		super.setup(context);
		mout = new MultipleOutputs<NullWritable, Paper>(context);
	}
	
	@Override
	protected void cleanup(Reducer<Text, Paper, NullWritable, Paper>.Context context)
			throws IOException, InterruptedException {
		super.cleanup(context);
		mout.close();
	}

	@Override
	protected void reduce(Text citeType, Iterable<Paper> papers,
			Context context) throws IOException, InterruptedException {
		for(Paper paper: papers){
			mout.write(NullWritable.get(), paper, citeType.toString());
		}	
	}
}
