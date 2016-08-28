package algorithms.maze3DGenerators;

import java.util.ArrayList;
import java.util.Random;



/**
 * The Class GrowingTreeRandom.
 * 
 * return  a random element given ArrayList
 * @author Asi Belachow
 * @version 1.0
 * @since 2016-30-07
 */
public class GrowingTreeRandom implements GrowingTree {

	/* (non-Javadoc)
	 * @see algorithms.maze3DGenerators.GrowingTree#choosePosition(java.util.ArrayList)
	 */
	@Override
	public Position choosePosition(ArrayList<Position> list) {
	
		Random r = new Random();
		return list.get(r.nextInt(list.size()));
	}

}