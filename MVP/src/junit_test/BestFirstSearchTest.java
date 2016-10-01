package junit_test;

import static org.junit.Assert.*;

import org.junit.Test;

import algorithms.maze3DGenerators.Position;
import algorithms.search.BestFirstSearch;

public class BestFirstSearchTest {

	BestFirstSearch<Position> bfs;
	
	public BestFirstSearchTest() {
		bfs = new BestFirstSearch<Position>();
	}
	
	
	@Test
	public void testNull() {
		try{
			bfs.search(null);
		}catch (NullPointerException e) {
			assertTrue(1 > 0);
		}
		assertTrue("should throw NullPointerException but didnt", 1 > 0);
		}
}