package search;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Iterator;

import org.junit.Test;

public class TestSearchResult {

	@Test
	public void test() throws IOException {
		Search s = new Search();

		int[] returnNums = new int[]{1, 3, 10};

		FileInputStream fi = new FileInputStream("src/test/java/search/test.txt");
		BufferedReader br = new BufferedReader(new InputStreamReader(fi));

		FileOutputStream fo = new FileOutputStream("src/test/java/search/result.txt");
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fo));

		String queryStr = null;
		while ((queryStr = br.readLine()) != null) {

			if (queryStr.trim().length() != 0) {
				bw.write("查询条件为：" + queryStr + "\n\n");
				for (int i = 0; i < returnNums.length; i++) {
					testAndSave(queryStr, returnNums[i], s, bw);
				}

				bw.write("-------------------------------------------------------------\n\n");
			}
		}

		bw.close();
		br.close();
		s.close();
	}
	private static void testAndSave(String queryStr, int returnNum, Search s, BufferedWriter bw) {
		try {
			bw.write("要求返回数量为" + returnNum + "时的结果:\n");
			Iterator<String> result = s.search(queryStr, returnNum);
			while (result.hasNext()) {
				String next = result.next();
				bw.write(next + "\n");
			}
			bw.write("\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
