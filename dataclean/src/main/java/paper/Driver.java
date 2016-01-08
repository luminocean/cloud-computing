package paper;

import mla.MLAMapper;

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
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import apa.APAMapper;
import bibtex.BibTexMapper;

public class Driver extends Configured implements Tool {
	@Override
	public int run(String[] args) throws Exception {
		if(!argsValids(args)) return -1;
		
		// job配置
		Configuration config = getJobConfig();
		
		// job创建
		Job job = Job.getInstance(config, "Data Clean");
		job.setJarByClass(getClass());
		
		// Mapper配置
		MultipleInputs.addInputPath(job, // 配置多源输入路径
				new Path(args[0]), // 文件夹路径
				TextInputFormat.class, // 使用默认的TextInputFormat输入格式
				BibTexMapper.class); // Mapper类
		
		MultipleInputs.addInputPath(job, // 配置多源输入路径
				new Path(args[1]), // 文件夹路径
				TextInputFormat.class, // 使用默认的TextInputFormat输入格式
				APAMapper.class); // Mapper类
		
		MultipleInputs.addInputPath(job, // 配置多源输入路径
				new Path(args[2]), // 文件夹路径
				TextInputFormat.class, // 使用默认的TextInputFormat输入格式
				MLAMapper.class); // Mapper类
				
		job.setMapOutputKeyClass(NullWritable.class);
		job.setMapOutputValueClass(Paper.class);
		
		// Reducer配置
		if("true".equals(config.get("output.toHbase"))){ // 输出到hbase
			job.setReducerClass(HBaseReducer.class);
			job.setOutputFormatClass(TableOutputFormat.class);
			job.setOutputKeyClass(ImmutableBytesWritable.class);
			job.setOutputValueClass(Mutation.class);
		}else{ // 输出到文本文件
			String[] formats = {"bibtex","apa","mla"}; // 各种文献格式
			for(String format: formats){
				// 为每一种文献格式定义一个输出
				// 在PaperReducer中使用
				MultipleOutputs.addNamedOutput(job, 
						format, 
						TextOutputFormat.class, 
						NullWritable.class, 
						Paper.class);
			}
			job.setReducerClass(PaperReducer.class);
			job.setOutputKeyClass(NullWritable.class);
			job.setOutputValueClass(Paper.class);
			
			// 配置输出根路径，它是输入参数中最后一个
			// 即使使用了MultipleOutputs，还是要靠这一行代码来设置所有输出的基础路径
			FileOutputFormat.setOutputPath(job, new Path(args[args.length-1])); 
		}
		
		return job.waitForCompletion(true) ? 0 : 1;
	}

	/**
	 * 获取本项目需要的job配置对象
	 * 对默认的config对象做一些必要的配置
	 * @return
	 */
	private Configuration getJobConfig() {
		Configuration config = getConf();
		
		// 如果指定了table入参，表示要将程序写入hbase
		// 而不是默认的文本文件
		if(config.get("table") != null){
			config.set("output.toHbase", "true");
			config.set(TableOutputFormat.OUTPUT_TABLE, config.get("table"));
		}
		config.set("textinputformat.record.delimiter", "\n\n"); // 设置隔行读取
		
		return config;
	}

	/**
	 * 检查命令行参数
	 * 至少要保证有一个输入目录
	 * @param args
	 * @return
	 */
	private boolean argsValids(String[] args) {
		if (args.length < 1) {
			System.err.printf("Usage: %s [generic options] <input> <output>\n", getClass().getSimpleName());
			ToolRunner.printGenericCommandUsage(System.err);
			return false;
		}
		return true;
	}

	public static void main(String[] args) throws Exception {
		int exitCode = ToolRunner.run(new Driver(), args);
		System.exit(exitCode);
	}
}