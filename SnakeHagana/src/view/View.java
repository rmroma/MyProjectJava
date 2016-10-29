package view;

import java.util.ArrayList;

import position.position2d.Position2D;

public interface View {

	public void displaySolution(ArrayList<Position2D> solution);
	
	public void moveRight();
	
	public void moveForward();
	
	public void moveBackward();
	
	public void moveLeft();
	
}