package feedback;

import java.io.IOException;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Table;

import model.Paper;

/**
 * 暂时不实现基于HBase的FeedBack
 */
@Deprecated
public class HbaseBasedFeedBack implements FeedBack{
	private static final String TIMES_COLUMN = "times";
	private Connection connection;
	private Table feedbackTable;

	public HbaseBasedFeedBack() throws IOException {
		Configuration conf = HBaseConfiguration.create();
		connection = ConnectionFactory.createConnection(conf);
		feedbackTable = connection.getTable(TableName.valueOf("feedback"));
	}
		
	@Override
	public void like(Paper p, Paper q) {	
	}
	
	@Override
	public void dislike(Paper p, Paper q) {
	}
	
	@Override
	public float getLikeness(Paper p, Paper q) {
		int numLike = 0, numDislike = 0;
		try {
			numLike = getNumLiked(p, q);
			numDislike = getNumDisliked(p, q);		
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		if(numLike == 0 || numDislike == 0) {
			return 1;
		}
		return numLike / (float)(numLike + numDislike);
	}
	
	private int getNumLiked(Paper p, Paper q) throws IOException {
		String keyword = keyword(p, q);
		Get get = new Get(keyword.toLowerCase().getBytes(CHARSET));
		Result getResult = feedbackTable.get(get);
		
		int numLike = 0;
		List<Cell> ret = getResult.getColumnCells("like".getBytes(CHARSET), TIMES_COLUMN.getBytes(CHARSET));
		if(!ret.isEmpty()) {
			numLike = Integer.valueOf(new String(ret.get(0).getValueArray()));
		}
		return numLike;
	}
	
	private int getNumDisliked(Paper p, Paper q) throws IOException {
		String keyword = keyword(p, q);
		Get get = new Get(keyword.toLowerCase().getBytes(CHARSET));
		Result getResult = feedbackTable.get(get);
		
		int numDislike = 0;		
		List<Cell> ret = getResult.getColumnCells("dislike".getBytes(CHARSET), TIMES_COLUMN.getBytes(CHARSET));
		if(!ret.isEmpty()) {
			numDislike = Integer.valueOf(new String(ret.get(0).getValueArray()));
		}
		return numDislike;
	}
	
	private String keyword(Paper p, Paper q) {
		return p.title + "&" + q.title;
	}
}
