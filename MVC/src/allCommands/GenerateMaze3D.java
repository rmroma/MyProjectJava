package allCommands;

import model.Model;


/**
 * <h1>Generate Maze3D</h1><p>
 * This Class define the command to show a Maze3D</br>
 * that generated by the name of the maze3D
 */
public class GenerateMaze3D extends ClientCommands {

	
	
	
	public GenerateMaze3D(Model m) {
		super(m);

	}

	@Override
	public void doCommmand(String[] param) {
	 String mazeName = param[1];
	 int z = Integer.parseInt(param[2]);
	 int x = Integer.parseInt(param[3]);
	 int y = Integer.parseInt(param[4]);
	 m.generateMaze3D(mazeName, z, x, y);
	

	}
	
	@Override
	public String toString() {
		return getClass().getName();
	}


}
