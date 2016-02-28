package search;

import java.util.List;
import org.junit.Test;

import util.StringSplitter;

public class TestStringSplitter {
	@Test
	public void test() {
		List<String> splits = StringSplitter.split("@inproceedings{Sundaram2000ICMCS,  title={Video Scene Segmentation using Video and Audio Features},  author={Hari Sundaram and Shih-Fu Chang},  booktitle={ICMCS},  year={2000}}");
		for(String split: splits){
			System.out.println(split);
		}
	}
}
