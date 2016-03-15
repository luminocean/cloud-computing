package recommend;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;

import feedback.Feedback;
import feedback.MemoryFeedback;
import model.Paper;
import model.QueryEntity;
import model.QueryEntityBuilder;
import util.BibTexParser;
import util.Pair;

public class Recommend {
	private Table table;
	private Connection connection;
	private Feedback feedBack;
	private List<Paper> papers = new ArrayList<>(2000);
	private static final String DELIMITER = "@";

	public Recommend() throws IOException {
		feedBack = new MemoryFeedback();
		Configuration conf = HBaseConfiguration.create();
		connection = ConnectionFactory.createConnection(conf);
		table = connection.getTable(TableName.valueOf("cite"));
		load();
		// 加载后直接断开数据库连接
		table.close();
		connection.close();
	}

	// 加载数据库里的所有paper数据，缓存在内存中
	private void load() throws IOException {
		Scan scan = new Scan();
		scan.setMaxVersions(1);
		ResultScanner scanner = table.getScanner(scan);
		Iterator<Result> iter = scanner.iterator();

		while (iter.hasNext()) {
			Result result = iter.next();
			Paper paper = new Paper(result);
			papers.add(paper);
		}
	}

	/**
	 * 根据请求进行推荐
	 * @param queryText BibTex格式的请求字符串
	 * @return 推荐的<相似度,文献>键值对列表
	 */
	public List<Pair<Integer, Paper>> recommend(String queryText, int size) {
		Set<Paper> paperSet = parseQuery(queryText);
		QueryEntity queryEntity = new QueryEntityBuilder().build(paperSet);

		Comparator<Pair<Integer, Paper>> descendingComparator = new Comparator<Pair<Integer, Paper>>() {
			@Override
			public int compare(Pair<Integer, Paper> e1, Pair<Integer, Paper> e2) {
				if (e1.getKey() != e2.getKey()) // 相似度不同时，按相似度由高到低排序
					return e2.getKey() - e1.getKey();
				else 							// 相似度相同时，按照发表年份倒序排列
					return e2.getValue().year.compareTo(e1.getValue().year);
			}
		};
		
		TreeSet<Pair<Integer, Paper>> recommendedSet = new TreeSet<>(descendingComparator);
		for (Paper paper : papers) {
			if (paperSet.contains(paper))
				continue;
			int similarity = paper.similarity(queryEntity);
			similarity *= feedBack.getLikeness(paperSet, paper);
			recommendedSet.add(new Pair<>(similarity, paper));
		}
		
		List<Pair<Integer, Paper>> recommendedList = new ArrayList<>();
		recommendedSet.stream()
			.limit(size + 1)
			.forEach(p -> {recommendedList.add(p);});
		return recommendedList;
	}

	private Set<Paper> parseQuery(String query) {
		Set<Paper> paperSet = new HashSet<>();
		String[] querys = query.split(DELIMITER);
		Paper paper = null;

		for (int i = 0; i < querys.length; i++) {
			if (querys[i] != null && !"".equals(querys[i].trim())) {
				paper = BibTexParser.parse(DELIMITER + querys[i]);
				paperSet.add(paper);
			}
		}

		return paperSet;
	}
}
