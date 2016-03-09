package recommend;

import java.io.IOException;
import java.util.List;
import java.util.Map.Entry;

import model.Paper;
import util.Pair;

public class Driver {

	public static void main(String[] args) throws IOException {
		Recommend rec = new Recommend();
		List<Pair<Integer,Paper>> papers = rec.recommend("@article{"
				+ "Arbelaez2011IEEETransPatternAnalMachIntell,  "
				+ "title={Contour Detection and Hierarchical Image Segmentation},  "
				+ "author={Pablo Arbelaez and Michael Maire and Charless C. Fowlkes and Jitendra Malik},  "
				+ "journal={IEEE Trans. Pattern Anal. Mach. Intell.},  "
				+ "year={2011},  volume={33},  pages={898-916}}", 20);
		
		// 输出查询结果
		for(Pair<Integer,Paper> pair: papers){
			System.out.println(pair.getKey());
			System.out.println(pair.getValue());
		}
		
		/// 多输入文献处理的重点在于要捕捉这多个文献重合的部分
		/// 比如叠加
	}
}
