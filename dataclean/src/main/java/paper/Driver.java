package paper;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import bibtex.BibTexMapper;

public class Driver extends Configured implements Tool {
	@Override
	public int run(String[] args) throws Exception {
		if (args.length != 2) {
			System.err.printf("Usage: %s [generic options] <input> <output>\n", getClass().getSimpleName());
			ToolRunner.printGenericCommandUsage(System.err);
			return -1;
		}
		
		JobConf conf = new JobConf(getConf());
		// 设置隔行读取
		conf.set("textinputformat.record.delimiter", "\n\n");
		Job job = Job.getInstance(conf, "Paper Clean");
		job.setJarByClass(getClass());
		
		// 配置多源输入路径
		MultipleInputs.addInputPath(job, 
				new Path(args[0]), // 文件夹路径
				TextInputFormat.class, // 使用默认的TextInputFormat输入格式
				BibTexMapper.class); // Mapper类
		
		job.setReducerClass(PaperReducer.class);
		job.setOutputKeyClass(NullWritable.class);
		job.setOutputValueClass(Paper.class);
		
		// 配置输出路径
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		return job.waitForCompletion(true) ? 0 : 1;
	}

	public static void main(String[] args) throws Exception {
		int exitCode = ToolRunner.run(new Driver(), args);
		System.exit(exitCode);
	}
}
