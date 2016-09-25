package view.widgets;



import java.util.ArrayList;

import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import algorithms.maze3DGenerators.Position;
import view.MazeWinEventHandler;

public abstract class MazeDisplayer extends Canvas implements GameBoardDisplayAdapter {

	
	protected MazeWinEventHandler winEvent;
	
	protected ImageGameCharacter character;
	
	protected int numofSteps;
	
	
	
	// Just a stub..
	int[][] mazeData={
			{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
			{1,0,0,0,0,0,0,0,1,1,0,1,0,0,1},
			{0,0,1,1,1,1,1,0,0,1,0,1,0,1,1},
			{1,1,1,0,0,0,1,0,1,1,0,1,0,0,1},
			{1,0,1,0,1,1,1,0,0,0,0,1,1,0,1},
			{1,1,0,0,0,1,0,0,1,1,1,1,0,0,1},
			{1,0,0,1,0,0,1,0,0,0,0,1,0,1,1},
			{1,0,1,1,0,1,1,0,1,1,0,0,0,1,1},
			{1,0,0,0,0,0,0,0,0,1,0,1,0,0,1},
			{1,1,1,1,1,1,1,1,1,1,1,1,0,1,1},
			};
	
	
	//------------------------------Constructors-------------------------//
	
	public MazeDisplayer(Composite parent, int style) {
		super(parent, style);
		setNumofSteps(0);
	};

	//------------------------------Getters and Setters-------------------------//
	
	public int getNumofSteps() {
		return numofSteps;
	}

	public void setNumofSteps(int numofSteps) {
		this.numofSteps = numofSteps;
	}
	
	
	public MazeWinEventHandler getWinEvent() {
		return winEvent;
		
	}

	public ImageGameCharacter getCharacter() {
		return character;
	}

	public void setCharacter(ImageGameCharacter character) {
		this.character = character;
	}

	public void setWinEvent(MazeWinEventHandler winEvent) {
		this.winEvent = winEvent;
	}
	
	public int[][] getMazeData() {
		return mazeData;
	}


	public void setMazeData(int[][] mazeData) {
		this.mazeData = mazeData;
	}
	
	public void triggerWin() {
		if (winEvent != null)
			winEvent.winGame();
		else
			// if there is no win handler - use default syso
			System.out.println("Win!");
	}
	
	
	public abstract boolean moveUp();

	public abstract boolean moveDown();

	public abstract boolean moveLeft();

	public abstract boolean moveRight();

	public abstract boolean moveFront();

	public abstract boolean moveBack();
	
	public abstract void setCharacterPosition(Position p);
	
	public abstract boolean moveCharacter(Position p);
	
	
	public abstract void showSoution(ArrayList<Position> sol);
		
	

	
	
}
