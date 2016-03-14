package recommend;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

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
import model.PaperRecommendQueryEntity;
import model.QueryEntityBuilder;
import util.BibTexParser;
import util.Pair;

public class Recommend {
	private Connection connection;
	private Table table;
	private List<Paper> papers = new ArrayList<>(2000);

	private static final String DELIMITER = "@";

	public Recommend() throws IOException {
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
	 * 
	 * @param queryText
	 *            BibTex格式的请求字符串
	 * @return 推荐的<相似度,文献>键值对列表
	 */
	public List<Pair<Integer, Paper>> recommend(String queryText, int size) {

		Set<Paper> paperSet = parseQuery(queryText);
		PaperRecommendQueryEntity queryEntity = new QueryEntityBuilder().build(paperSet);

		// 将所有的paper数据使用TreeMap按照相似度排序
		// 相同相似度的paper放在同一个list里面
		// 之后由flatten方法来将这个TreeMap平铺为一整个list
		TreeMap<Integer, List<Paper>> tree = new TreeMap<>((n1, n2) -> n2 - n1);
		for (Paper paper : papers) {

			if (paperSet.contains(paper))
				continue;

			int similarity = paper.similarity(queryEntity);

			List<Paper> list = tree.get(similarity);
			if (list == null) {
				list = new ArrayList<Paper>();
				tree.put(similarity, list);
			}
			list.add(paper);
		}

		return

		flatten(tree.entrySet(), size);
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

	/**
	 * Set<Entry<Integer, List<Paper>>> 平铺为List<Pair<Integer, Paper>>
	 * 
	 * 之所以用Pair是因为用户代码无法创建Entry因此使用Pair来代替
	 * 
	 * @param set
	 *            要平铺的Entry<Integer, List<Paper>>集合，特点是一个key对应了一个list
	 * @param size
	 *            最后返回的list大小，超出的会被截去
	 * @return 平铺后的<Integer,Paper>键值对列表
	 */
	private List<Pair<Integer, Paper>> flatten(Set<Entry<Integer, List<Paper>>> set, int size) {
		// 先将set转为一个list
		List<Entry<Integer, List<Paper>>> treeList = new ArrayList<>();
		treeList.addAll(set);

		// 要返回的Paper列表，和其对应的相似度捆绑在一起
		List<Pair<Integer, Paper>> list = treeList.stream()
				// 将Entry<Integer,List<Paper>>展开为List<Pair<Integer,Paper>>
				.flatMap((entry) -> {
					Integer key = entry.getKey();
					List<Paper> pl = entry.getValue();
					List<Pair<Integer, Paper>> el = new ArrayList<>(pl.size());
					for (Paper p : pl) {
						el.add(new Pair<Integer, Paper>(key, p));
					}
					return el.stream();
				}).limit(size + 1) // 只获取前size+1个（其中应该包括查询请求自身，下面要将它去掉）
				.collect(Collectors.toList());

		// 重排序
		list.sort((e1, e2) -> {
			if (e1.getKey() != e2.getKey()) // 相似度不同时，按相似度由高到低排序
				return e2.getKey() - e1.getKey();
			else // 相似度相同时，按照发表年份倒序排列
				return e2.getValue().year.compareTo(e1.getValue().year);
		});

		return list;
	}
}
