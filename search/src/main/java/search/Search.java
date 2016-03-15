package search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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

public class Search implements Constants{	
	private Connection connection;
	private Table indexTable;
	
	public Search() throws IOException {
		Configuration conf = HBaseConfiguration.create();
		connection = ConnectionFactory.createConnection(conf);
		indexTable = connection.getTable(TableName.valueOf("index"));
	}
	
	/**
	 * 本来觉得用优先队列太麻烦了，结果不用优先队列代码写起来也很麻烦QAQ
	 * @param keywords 关键词
	 * @param limit 期望的搜索结果数量
	 * @return 搜索结果
	 * @throws IOException
	 */
	public Iterator<String> search(String keywords, int limit) throws IOException {
		Map<String, Integer> paperToOccurance = new TreeMap<>();
		List<String> words = StringSplitter.split(keywords);
		for(String word : words) {
			searchKeyword(word, paperToOccurance);
		}
		
		//找出允许加入结果集合的paper的最小出现次数
		List<Integer> numOccurance = new ArrayList<>(paperToOccurance.values());
		Collections.sort(numOccurance);
		int minOccurance = numOccurance.size() < limit ? numOccurance.get(0) : numOccurance.get(numOccurance.size() - limit); 

		//比如minOccurance为3，出现次数大于3的paper直接放进结果集合中，然后再遍历paperToOccurance一遍，将出现次数等于3的paper放进结果集合
		List<String> ret = new ArrayList<>();
		for(Map.Entry<String, Integer> entry : paperToOccurance.entrySet()) {
			if(entry.getValue() > minOccurance) {
				ret.add(entry.getKey());
			}
		}
		for(Map.Entry<String, Integer> entry : paperToOccurance.entrySet()) {
			if(ret.size() < limit && entry.getValue() == minOccurance) {
				ret.add(entry.getKey());
			}
		}
		
		return ret.iterator();
	}
	
	public void close() {
		try {
			indexTable.close();
			connection.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void searchKeyword(String keyword, Map<String, Integer> paperToOccurance) throws IOException {
		Get get = new Get(keyword.toLowerCase().getBytes(CHARSET));
		get.addFamily("title".getBytes(CHARSET));
		Result getResult = indexTable.get(get);
		if(getResult.isEmpty()) {
			return;
		}
		
		Collection<byte[]> paperTitles = getResult.getFamilyMap("title".getBytes(CHARSET)).keySet();
		for(byte[] bytes : paperTitles) {
			String paper = new String(bytes, CHARSET);
			if(paperToOccurance.containsKey(paper)) {
				paperToOccurance.put(paper, paperToOccurance.get(paper) + 1);
			}
			else {
				paperToOccurance.put(paper, 1);
			}
		}
	}
}
