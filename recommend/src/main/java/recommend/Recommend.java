package recommend;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;

import model.Paper;
import util.BibTexParser;

public class Recommend {	
	private Connection connection;
	private Table table;
	private List<Paper> papers = new ArrayList<>(2000);
	
	public Recommend() throws IOException {
		Configuration conf = HBaseConfiguration.create();
		connection = ConnectionFactory.createConnection(conf);
		table = connection.getTable(TableName.valueOf("cite"));
		load();
		table.close();
		connection.close();
	}
	
	// 加载数据库里的所有paper数据，构建集合
	private void load() throws IOException {
		Scan scan = new Scan();
		scan.setMaxVersions(1);
		ResultScanner scanner = table.getScanner(scan);
		Iterator<Result> iter = scanner.iterator();
		while( iter.hasNext() ){
			Result result = iter.next();
			Paper paper = new Paper(result);
			papers.add(paper);
		}
	}
	
	/**
	 * 根据请求进行推荐
	 * @param queryText BibTex格式的请求字符串
	 * @return 
	 */
	public List<Entry<Integer,Paper>> recommend(String queryText, int size){
		Paper query = BibTexParser.parse(queryText);
		
		// 使用TreeMap排序
		TreeMap<Integer, Paper> tree = new TreeMap<>((n1,n2) -> n2-n1);
		for(Paper paper: papers){
			int distance = paper.distance(query);
			tree.put(distance, paper);
		}
		
		List<Entry<Integer, Paper>> list = new ArrayList<>();
		list.addAll(tree.entrySet());
		list = list.subList(0, size+1);
		
		// 去掉查询文献自身
		Iterator<Entry<Integer, Paper>> iter = list.iterator();
		while( iter.hasNext() ){
			Paper paper = iter.next().getValue();
			if( paper.equals(query) ){
				iter.remove();
			}
		}
		
		return list;
	}
}
