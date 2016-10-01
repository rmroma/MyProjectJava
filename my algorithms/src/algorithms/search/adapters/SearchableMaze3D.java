package algorithms.demo;

import java.util.ArrayList;
import algorithms.maze3DGenerators.Maze3D;
import algorithms.maze3DGenerators.Position;
import algorithms.search.CommonSearchable;
import algorithms.search.State;



/**
 * <h1>State<T></h1><p>
 * This class extends the CommonSearchable class and its define a searchable Maze3D
 * @param <T> the generic type
 * @author Asi Belachow
 * @version 1.0
 * @since 2016-28-08

 */
public class SearchableMaze3D<T> extends CommonSearchable<Position> {
	
	//------------------------------Constructors-------------------------//
	
	
	/**
	 * <h1>SearchableMaze3D</h1><p>
	 * <i><ul>SearchableMaze3D(Maze3D myMaze)<i><p>
	 * Initialization a Searchable Maze3D
	 * @param - aMaze3D
	 */
	public SearchableMaze3D(Maze3D myMaze) {
		super(myMaze);
	}
	
	public SearchableMaze3D(Maze3D myMaze,double cost) {
		super(myMaze,cost);
		
	}
	

	//-------------------------------Functionality------------------------------//
	@Override
	public State<Position> getInitialState() {
		State<Position> start = new State<Position>(myMaze.getStart());
		start.setCost(0);
		start.setCameFrom(null);
		return start;
	}

	@Override
	public State<Position> getGoalState() {
		State<Position> end = new State<Position>(myMaze.getEnd());
		return end;
	}

	@Override
	public ArrayList<State<Position>> getAllPossibleStates(State<Position> s) {
		ArrayList<Position> pMoves = myMaze.getPossibleMoves(s.getState()); 
		
		ArrayList<State<Position>> stateList = new ArrayList<State<Position>>();

		for (int i=0;i < pMoves.size(); i++){
			stateList.add(new State<Position>(pMoves.get(i)));
			stateList.get(i).setCameFrom(s);
			stateList.get(i).setCost(getCostValue());
		}
		return stateList;
	}




	
}