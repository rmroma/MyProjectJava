package view;

import algorithms.maze3DGenerators.Maze3D;
import algorithms.maze3DGenerators.Position;
import algorithms.search.Solution;

/**
 * <h1>The Interface View</h1><p>
 * This interface is the view part of the MVP</br> architecture, used for different
 * implementations of UI

 * @author Asi Belachow
 * @version 1.0
 * @since 2016-10-09
 *
 */
public interface View {


	public void close();
	
	public void run();
	
	
	/**
	 *<h1>Display Message</h1>
	 * <i><ul>void displayMessage(String msg)<i><p>
	 * Get a message from the {@link Presenter} and view it to the user
	 * @param msg - the message
 	 */
	void displayMessage(String msg);
	
	
	/**
	 *<h1>Notify Maze Is Ready</h1>
	 * <i><ul>void notifyMazeIsReady(String name)<i><p>
	 * Get an event that notify the {@link View} about the</br> process of generating maze in the {@link Model}
	 * @param name - the name of the Maze3D
 	 */
	public void notifyMazeIsReady(String name);
	
	public void notifySolutionIsReady(String name);
	
	
	public void showDirPath(String str);
	
	public void getCrossSection(int[][] section);
	
	public void displayMaze(Maze3D maze);
	

	public void displaySolution(Solution<Position> solution);
	
	
	
	
	
	
	
}