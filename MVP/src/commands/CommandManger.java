package commands;

import java.io.IOException;
import java.util.HashMap;

import algorithms.maze3DGenerators.Maze3D;
import algorithms.maze3DGenerators.Position;
import algorithms.search.Solution;
import model.Model;
import view.View;

public class CommandManger {
	
	Model model;
	View view;
	
	public CommandManger(Model model, View view) {
		this.model = model;
		this.view =view;
	}
	
	

	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}

	public View getView() {
		return view;
	}

	public void setView(View view) {
		this.view = view;
	}
	
	public HashMap<String,Command> getCommandMap(){
		
		HashMap<String, Command> commands = new HashMap<String, Command>();
		commands = new HashMap<String, Command>();
		commands.put("dir [^\n]+", new DirPathCommand());
		commands.put("generate_maze [A-Za-z0-9]+ [0-9]{1,2} [0-9]{1,2} [0-9]{1,2}", new GenerateMazeCommand());
		commands.put("display [A-Za-z0-9]+", new DisplayMazeCommand());
		commands.put("display_cross_section [0-9]{1,2} [A-Za-z0-9]+ [A-Za-z0-9]+", new DisplayCrossSectionCommand());
		commands.put("save_maze [A-Za-z0-9]+ [^\n]+", new SaveMazeCommand());
		commands.put("load_maze [^\n]+ [A-Za-z0-9]+", new LoadMazeCommand());
		commands.put("solve [A-Za-z0-9]+ [A-Za-z0-9]+", new SolveMazeCommand());
		commands.put("display_solution [A-Za-z0-9]+", new DisplaySolutionCommand());
		commands.put("display_menu", new DisplayMazeCommand());
		commands.put("list_maze", new ListNameOfMazeCommand());
		commands.put("menu", new DisplayMenuCommand());
		commands.put("maze_ready", new NotifyMazeReadyCommand());
		commands.put("solution_ready", new NotifySolutionReady());
		commands.put("dir_path", new DisplayDirFilesCommand());
		commands.put("exit", new ExitCommand());
		return commands;

	}
	
	public HashMap<String,String> getRegexCommand(){
		HashMap<String,String> regexCommands = new HashMap<String,String>();
		
		regexCommands.put("dir","dir [^\n]+");
		regexCommands.put("generate_maze", "generate_maze [A-Za-z0-9]+ [0-9]{1,2} [0-9]{1,2} [0-9]{1,2}");
		regexCommands.put("display", "display [A-Za-z0-9]+");
		regexCommands.put("display_cross_section", "display_cross_section [0-9]{1,2} [A-Za-z0-9]+ [A-Za-z0-9]+");
		regexCommands.put("save_maze", "save_maze [A-Za-z0-9]+ [^\n]+");
		regexCommands.put("load_maze", "load_maze [^\n]+ [A-Za-z0-9]+");
		regexCommands.put("solve", "solve [A-Za-z0-9]+ [A-Za-z0-9]+");
		regexCommands.put("display_solution", "display_solution [A-Za-z0-9]+");
		regexCommands.put("display_menu", "display_menu");
		regexCommands.put("list_maze","list_maze");
		regexCommands.put("menu", "menu");
		regexCommands.put("maze_ready", "maze_ready");
		regexCommands.put("solution_ready", "solution_ready");
		regexCommands.put("dir_path", "dir_path");
		regexCommands.put("exit", "exit");
 		return regexCommands;
		
	}
	
	class GenerateMazeCommand implements Command{
		
		@Override
		public void doCommmand(String[] param) throws IOException {
			model.generateMaze3D(param[1], Integer.parseInt(param[2]), Integer.parseInt(param[3]), Integer.parseInt(param[4]));
		}
		
		@Override
		public String toString() {
			return getClass().getSimpleName();
		}
	}
	
	
	class DirPathCommand implements Command{
		
		@Override
		public void doCommmand(String[] param) throws IOException {
			model.getDirPath(param[1]);
		}
		
		@Override
		public String toString() {
			return getClass().getSimpleName();
		}
	}
	
	class DisplayMazeCommand implements Command{
		
		@Override
		public void doCommmand(String[] param) throws IOException {
			Maze3D maze = model.displayMaze(param[1]);
			System.out.println("displayMazeCommand");
			if (maze==null)
				view.displayMessage("The maze not found");
			else
				view.displayMaze(maze);
			
		}
		
		@Override
		public String toString() {
			return getClass().getSimpleName();
		}
	}
	
	class DisplayCrossSectionCommand implements Command{
		
		@Override
		public void doCommmand(String[] param) throws IOException {
			int[][] cross = model.CrossSectionByDimention(Integer.parseInt(param[1]), param[2], param[3]);
			if(cross == null)
				view.displayMessage("There was error while trying to disply cros section, please try again");
			else
				view.getCrossSection(cross);
		}
		
		@Override
		public String toString() {
			return getClass().getSimpleName();
		}
	}
	
	
	class SaveMazeCommand implements Command{
		
		@Override
		public void doCommmand(String[] param) throws IOException {
			view.displayMessage(model.saveMaze(param[1], param[2]));
			
		}
		
		@Override
		public String toString() {
			return getClass().getSimpleName();
		}
	}
	
	class LoadMazeCommand implements Command{
		
		@Override
		public void doCommmand(String[] param) throws IOException {
			Maze3D maze = model.loadMaze(param[1], param[2]);
			if(maze==null)
				view.displayMessage("There was error while trying to load the maze, Please try agian");
			else
				view.displayMaze(maze);
		}
		
		@Override
		public String toString() {
			return getClass().getSimpleName();
		}
	}
	
	class SolveMazeCommand implements Command{

		@Override
		public void doCommmand(String[] param) throws IOException {
			model.solveMaze(param[1], param[2]);

		}

		@Override
		public String toString() {
			return getClass().getSimpleName();
		}
	}
		
	class DisplaySolutionCommand implements Command{
		
		@Override
		public void doCommmand(String[] param) throws IOException {
			Solution<Position> s =model.displaySolution(param[1]);
			System.out.println(s.toString());
			view.displaySolution(s);
			
		}
		
		@Override
		public String toString() {
			return getClass().getSimpleName();
		}
	}
	
	class ListNameOfMazeCommand implements Command{
		
		@Override
		public void doCommmand(String[] param) throws IOException {
			view.displayMessage(model.displayListOfAllMaze());
		}
		
		@Override
		public String toString() {
			return getClass().getSimpleName();
		}
	}
	
	class DisplayMenuCommand implements Command{
		
		@Override
		public void doCommmand(String[] param) throws IOException {
			view.displayMessage(model.displayMenu());
		}
		
		@Override
		public String toString() {
			return getClass().getSimpleName();
		}
	}
	
	class ExitCommand implements Command{
		
		@Override
		public void doCommmand(String[] param) throws IOException {
			model.exit();
			view.close();
		}
		
		@Override
		public String toString() {
			return getClass().getSimpleName();
		}
	}
	
	class NotifyMazeReadyCommand implements Command{
		
		@Override
		public void doCommmand(String[] param) throws IOException {
			String name = param[0];
			view.notifyMazeIsReady(name);
		}
		
		@Override
		public String toString() {
			return getClass().getSimpleName();
		}
	}
	
	class NotifySolutionReady implements Command{
		
		@Override
		public void doCommmand(String[] param) throws IOException {
			String name = param[0];
			//string msg = "Solution "
			view.notifySolutionIsReady(name);
		}
		
		@Override
		public String toString() {
			return getClass().getSimpleName();
		}
	}
	
	class DisplayDirFilesCommand implements Command{
		
		@Override
		public void doCommmand(String[] param) throws IOException {
			String str = param[0];
			view.displayMessage(str);
		}
		
		@Override
		public String toString() {
			return getClass().getSimpleName();
		}
		
	}
	
	

}
