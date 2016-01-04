package paper;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.client.Mutation;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableOutputFormat;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import apa.APAMapper;
import bibtex.BibTexMapper;
import chicago.ChicagoMapper;

public class Driver extends Configured implements Tool {
	@Override
	public int run(String[] args) throws Exception {
		if (args.length != 2) {
			System.err.printf("Usage: %s [generic options] <input> <output>\n", getClass().getSimpleName());
			ToolRunner.printGenericCommandUsage(System.err);
			return -1;
		}
		
		// job配置
		Configuration config = getConf();
		String tableName = config.get("table");
		boolean outputToHBase = tableName != null?true:false;
		if(outputToHBase){
			config.set(TableOutputFormat.OUTPUT_TABLE, tableName);
		}
		config.set("textinputformat.record.delimiter", "\n\n"); // 设置隔行读取
		
		Job job = Job.getInstance(config, "Data Clean");
		job.setJarByClass(getClass());
		
		// Mapper配置
		MultipleInputs.addInputPath(job, // 配置多源输入路径
				new Path(args[0]), // 文件夹路径
				TextInputFormat.class, // 使用默认的TextInputFormat输入格式
				ChicagoMapper.class); // Mapper类
				
		job.setMapOutputKeyClass(NullWritable.class);
		job.setMapOutputValueClass(Paper.class);
		
		// Reducer配置，区分输出到文本文件还是hbase
		if(outputToHBase){
			job.setReducerClass(HBaseReducer.class);
			job.setOutputFormatClass(TableOutputFormat.class);
			job.setOutputKeyClass(ImmutableBytesWritable.class);
			job.setOutputValueClass(Mutation.class);
		}else{
			FileOutputFormat.setOutputPath(job, new Path(args[1])); // 配置输出路径
			job.setOutputKeyClass(NullWritable.class);
			job.setOutputValueClass(Paper.class);
		}
		
		return job.waitForCompletion(true) ? 0 : 1;
	}

	public static void main(String[] args) throws Exception {
		int exitCode = ToolRunner.run(new Driver(), args);
		System.exit(exitCode);
	}
}