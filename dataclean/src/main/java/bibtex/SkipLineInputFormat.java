package bibtex;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.CompressionCodecFactory;
import org.apache.hadoop.io.compress.SplittableCompressionCodec;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.LineRecordReader;

import com.google.common.base.Charsets;

/**
 * 隔行读取的输入格式
 * @author luminocean
 *
 */
public class SkipLineInputFormat extends FileInputFormat<LongWritable, Text> {

	@Override
	public RecordReader<LongWritable, Text> createRecordReader(InputSplit split, TaskAttemptContext context) {
		String delimiter = "\n\n"; // 这里改为隔行读取
		byte[] recordDelimiterBytes = null;
		if (null != delimiter)
			recordDelimiterBytes = delimiter.getBytes(Charsets.UTF_8);
		return new LineRecordReader(recordDelimiterBytes);
	}

	@Override
	protected boolean isSplitable(JobContext context, Path file) {
		final CompressionCodec codec = new CompressionCodecFactory(context.getConfiguration()).getCodec(file);
		if (null == codec) {
			return true;
		}
		return codec instanceof SplittableCompressionCodec;
	}

}
