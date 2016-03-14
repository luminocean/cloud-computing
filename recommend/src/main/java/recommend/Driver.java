package recommend;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import model.Paper;
import util.Pair;

/**
 * 推荐查询驱动类
 */
public class Driver {

	public static void main(String[] args) throws IOException {

		String query = readFileByLines("query.txt");

		Recommend rec = new Recommend();
		// // 单文献推荐
		// List<Pair<Integer, Paper>> papers = rec.recommend("@article{" +
		// "Arbelaez2011IEEETransPatternAnalMachIntell, "
		// + "title={Contour Detection and Hierarchical Image Segmentation}, "
		// + "author={Pablo Arbelaez and Michael Maire and Charless C. Fowlkes
		// and Jitendra Malik}, "
		// + "journal={IEEE Trans. Pattern Anal. Mach. Intell.}, "
		// + "year={2011}, volume={33}, pages={898-916}}", 20);

		List<Pair<Integer, Paper>> papers = rec.recommend(query, 20);

		// 输出查询结果
		for (Pair<Integer, Paper> pair : papers)

		{
			System.out.println(pair.getKey());
			System.out.println(pair.getValue());
		}
	}

	public static String readFileByLines(String fileName) {

		File file = new File(fileName);
		BufferedReader reader = null;
		StringBuilder sb = new StringBuilder();

		try {
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			while ((tempString = reader.readLine()) != null) {
				sb.append(tempString).append("\n");
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}

		return sb.toString();
	}
}
