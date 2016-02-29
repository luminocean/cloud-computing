package search;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Table;

import util.Constants;
import util.StringSplitter;

public class Search {	
	private Connection connection;
	private Table indexTable;
	
	public Search() throws IOException {
		Configuration conf = HBaseConfiguration.create();
		connection = ConnectionFactory.createConnection(conf);
		indexTable = connection.getTable(TableName.valueOf("index"));
	}
	
	/**
	 * @param keywords 关键词
	 * @param limit 期望的搜索结果数量
	 * @return 搜索结果
	 * @throws IOException
	 */
	public Iterator<String> search(String keywords, int limit) throws IOException {
		TreeSet<String> candidates = new TreeSet<>();
		List<String> words = StringSplitter.split(keywords);
		for(String word : words) {
			candidates = searchKeyword(word, candidates);
			if(candidates.size() <= limit) {
				break;
			}
		}
		
		while(candidates.size() > limit) {
			candidates.remove(candidates.last());
		}
		return candidates.iterator();		
	}
	
	public void close() {
		try {
			indexTable.close();
			connection.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private TreeSet<String> searchKeyword(String keyword, TreeSet<String> candidates) throws IOException {
		Get get = new Get(keyword.toLowerCase().getBytes(Constants.CHARSET));
		get.addFamily("title".getBytes(Constants.CHARSET));
		Result getResult = indexTable.get(get);
		//关键词没有匹配的结果，跳过该关键词
		if(getResult.isEmpty()) {
			return candidates;
		}

		Collection<byte[]> paperTitles = getResult.getFamilyMap("title".getBytes(Constants.CHARSET)).keySet();
		TreeSet<String> ret = new TreeSet<>();
		for(byte[] bytes : paperTitles) {
			String paperTitle = new String(bytes, Constants.CHARSET);
			if(candidates.isEmpty() || candidates.contains(paperTitle)) {
				ret.add(paperTitle);
			}
		}
		return ret;
	}
}
