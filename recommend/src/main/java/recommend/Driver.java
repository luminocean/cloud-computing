package recommend;

import java.io.IOException;
import java.util.List;
import java.util.Map.Entry;

import model.Paper;

public class Driver {

	public static void main(String[] args) throws IOException {
		Recommend rec = new Recommend();
		List<Entry<Integer,Paper>> papers = rec.recommend("@article{"
				+ "Arbelaez2011IEEETransPatternAnalMachIntell,  "
				+ "title={Contour Detection and Hierarchical Image Segmentation},  "
				+ "author={Pablo Arbelaez and Michael Maire and Charless C. Fowlkes and Jitendra Malik},  "
				+ "journal={IEEE Trans. Pattern Anal. Mach. Intell.},  "
				+ "year={2011},  volume={33},  pages={898-916}}", 10);
		
		for(Entry<Integer,Paper> entry: papers){
			System.out.println(entry.getKey());
			System.out.println(entry.getValue());
		}
	}
}
