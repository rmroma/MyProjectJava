package view;


import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import algorithms.maze3DGenerators.Maze3D;
import algorithms.maze3DGenerators.Position;
import algorithms.search.Solution;

public abstract class AbstractObservableView extends Observable implements View, Observer {

	//------------------------------Data Members-------------------------//
	
	protected CLI cli;
	
	HashMap<String, Object> data;
	
	//------------------------------Constructors-------------------------//
	
	public AbstractObservableView() {
		data = new HashMap<String, Object>();

	}
	
	//-----------------------------setters and getters-------------------//

	public CLI getCli() {
		return cli;
	}

	public void setCli(CLI cli) {
		this.cli = cli;
	}
	
	//-------------------------Functionality-------------------------//
	

	protected void setCommandAndNotify(String command, Object args)
	{
		if (data!=null)
			data.put(command, args);
		setChanged();
		notifyObservers(command);
		
		
	}
	

	@Override
	public abstract void close();
	
	public abstract void getCrossSection(int[][] section);
	
	public abstract void showDirPath(String str);
	
	@Override
	public abstract void displayMaze(Maze3D maze);
	
	@Override
	public abstract void displaySolution(Solution<Position> solution);
	@Override
	public abstract void notifySolutionIsReady(String name);
	

	
	
	
	

}
