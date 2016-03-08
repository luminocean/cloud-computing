package mapred;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.mapreduce.MultiTableOutputFormat;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import model.Paper;

public class Driver extends Configured implements Tool{

	public int run(String[] args) throws Exception {
		if(!argsValid(args)) return -1;
		
		// job配置
		Configuration config = getConf();
		// 设置隔行读取
		config.set("textinputformat.record.delimiter", "\n\n");
		
		// job创建
		Job job = Job.getInstance(config, "Recommendation");
		job.setJarByClass(getClass());
		
		// 设置输入文件路径与对应mapper
		FileInputFormat.addInputPath(job, new Path(args[0]));
		job.setMapperClass(BibTexMapper.class);
		job.setMapOutputKeyClass(NullWritable.class);
		job.setMapOutputValueClass(Paper.class);
		
		// 输出设置
		job.setOutputFormatClass(MultiTableOutputFormat.class);
		job.setReducerClass(HBaseReducer.class);

		TableMapReduceUtil.addDependencyJars(job);
		TableMapReduceUtil.addDependencyJars(job.getConfiguration());

	    // 任务开始
	    return job.waitForCompletion(true) ? 0 : 1;
	}
	
	 /* 检查命令行参数
	 * 至少要保证有一个输入目录
	 * @param args
	 * @return
	 */
	private boolean argsValid(String[] args) {
		if (args.length < 1) {
			System.err.printf("Usage: %s [generic options] <input>\n", getClass().getSimpleName());
			ToolRunner.printGenericCommandUsage(System.err);
			return false;
		}
		return true;
	}
	
	public static void main(String[] args) throws Exception {
		System.exit(ToolRunner.run(new Driver(), args)); 
	}
	
}