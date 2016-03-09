package search;

import java.io.IOException;
import java.util.Iterator;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestSearch {
	@Test
	public void test() {
		try {
			String keywords = "A hidden Markov model";
			String expectedResult = "A hidden Markov model framework for video segmentation using audio and image features";
			Search s = new Search();
			Iterator<String> result = s.search(keywords, 10);
			boolean found = false;
			while(result.hasNext()) {
				String next = result.next();
				if(next.toLowerCase().equals(expectedResult.toLowerCase())) {
					found = true;
				}
			}
			s.close();
			assertTrue(found);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

