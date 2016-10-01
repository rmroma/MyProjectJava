package model;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPOutputStream;
import algorithms.demo.SearchableMaze3D;
import algorithms.maze3DGenerators.GrowingTreeGenerator;
import algorithms.maze3DGenerators.GrowingTreeRandom;
import algorithms.maze3DGenerators.Maze3D;
import algorithms.maze3DGenerators.Position;
import algorithms.search.BestFirstSearch;
import algorithms.search.DepthFirstSearch;
import algorithms.search.Solution;
import io.MyCompressorOutputStream;
import io.MyDecompressorInputStream;


public class MyModel extends AbstractModel {
	


	//-------------------------Functionality-------------------------//
	
	@Override
	public void getDirPath(String path) {
		//Checks if the file empty
		if(path.length()==0){
			setChanged();
			notifyObservers("dir_path Please enter a directory");
			return;
			//return "dir_path Please enter a directory";
		}
			
		//Open the file
		File dir = new File(path);
		//Checks if the path is directory if not, return a message to the presenter
		if(!dir.isDirectory()){
			setChanged();
			notifyObservers("dir_path Please enter a valid directory");
			return;
		}
			//return "Please enter a valid directory";
			
		//Get list of  all files and folders in the given path and  forward to the controller
		String[] temp = dir.list();
		StringBuilder sb = new StringBuilder();
		sb.append("\nThe files in "+ path + " are: \n");
		for (String s : temp){
			sb.append(s+"\n");
		}
		setChanged();
		notifyObservers("dir_path "+sb.toString()+".");
		
	}

	@Override
	public void generateMaze3D(String mazeName, int z, int x, int y) {
		
		pool.submit(new Callable<Maze3D>() {

			@Override
			public Maze3D call() throws Exception {
				Maze3D maze = (new GrowingTreeGenerator(new GrowingTreeRandom())).generate(z, x, y);
				mapMaze3D.put(mazeName, maze);
				saveMazes();
				setChanged();
				notifyObservers("maze_ready "+mazeName);
				return maze;
			}
		});		
	}

	@Override
	public int[][] CrossSectionByDimention(int index, String dimension,  String mazeName) {
		
		//Check is the maze in database
		if (mapMaze3D.containsKey(mazeName)){
			//If found get the maze
			Maze3D maze = mapMaze3D.get(mazeName);
			//if the index that the user entered invalid an IndexOutOfBoundsException will Thrown out
			try{
				//Find the dimension
				if(dimension.equals("z") || dimension.equals("Z"))
					return maze.getCrossSectionByZ(index);
				
				if(dimension.equals("x") || dimension.equals("X"))
					return maze.getCrossSectionByX(index);
				
				if(dimension.equals("y") || dimension.equals("Y")){
					return maze.getCrossSectionByY(index);
				}
			}catch (IndexOutOfBoundsException e){
				return null;
			}
		}
		//If the maze not found return a message to the CLI
		return null;
	}

	@Override
	public String saveMaze(String mazeName, String path) throws IOException  {
		
		//Check if the maze in database
		if(mapMaze3D.containsKey(mazeName)){
			//Trying to write the to a file, an Exception will Thrown out if is error while
			//trying to open the file
			try{
				System.out.println("data/saved_generated_maze/"+path+".maz");
				OutputStream out = new MyCompressorOutputStream(new FileOutputStream(path));
				out.write(mapMaze3D.get(mazeName).toByteArray());
				out.flush();
				out.close();
			}catch (Exception e) {
				return "Error while trying to save the maze, please try again";
			}
			return "The maze: \""+ mazeName+ "\" successfully saved";
			
		}else
			return "The maze: \"" + mazeName + "\" not exits";
	}

	@Override
	public Maze3D loadMaze(String file, String mazeName) throws IOException {
		Maze3D maze;
		//boolean flag = false;
		//Trying to open a file, IF the file not found FileNotFoundException will Thrown out
		try {
			File f = new File(file);
			System.out.println("ddasfsdfsdf"+file);
			@SuppressWarnings("resource")
			InputStream in=new MyDecompressorInputStream( new FileInputStream(f));
			byte b[] = new byte[(int) f.length()];
			in.read(b);
			maze = new Maze3D(b);//Load the maze
			mapMaze3D.put(mazeName, maze);//Saving the maze to the database
		} catch (FileNotFoundException e) {
			//flag=true;
			return null;
		}
		
		return maze;
		
	}

	@Override
	public void solveMaze(String mazeName, String alg) {
		
		pool.submit(new Callable<Solution<Position>>() {

			@Override
			public Solution<Position> call() throws Exception {
				boolean flag=false;
				Solution<Position> solution = null;
				
				
				if(!mapSolution.contains(mazeName)){
					Maze3D maze = mapMaze3D.get(mazeName);
					if(alg.equals("bfs") || alg.equals("BFS")){
						solution = new BestFirstSearch<Position>().search(new SearchableMaze3D<>(maze));
						flag =true;
					}
					else if(alg.equals("dfs") || alg.equals("DFS")){
						solution = new DepthFirstSearch<Position>().search(new SearchableMaze3D<>(maze));
						flag =true;
					}

					if(flag==false)
						return null;
					mapSolution.put(mazeName, solution);
					saveSolution();
					setChanged();
					notifyObservers("solution_ready "+ mazeName);
					return solution;
				}
			
				setChanged();
				notifyObservers("solution_ready "+ mazeName);
				return mapSolution.get(mazeName);
			}
			
		});

	}


	@Override
	public Maze3D displayMaze(String name) {
		
		return mapMaze3D.get(name);
	}
	
	
	@Override
	public Solution<Position> displaySolution(String mazeName) {
		/*//Check if the maze in database, if exists get the solution
		if(mapSolution.containsKey(mazeName))
			setCommandAndNotify("display solution", mapSolution.get(mazeName));
		//If not exits, return message to hte CLI
		else
			setCommandAndNotify("message", "The maze: \""+ mazeName+"\" not found");*/
		System.out.println("return nsolution");
		return mapSolution.get(mazeName);
	
	}
	
	@Override
	public String displayListOfAllMaze() {
		//Check if there is maze in database
		if(mapMaze3D.isEmpty())
			return "The database empty";
		else {
			//Get list of all maze's name in database
			StringBuilder sb = new StringBuilder();
			Iterator<String> iter = mapMaze3D.keySet().iterator();
			int i=1;
			sb.append("\nList of all maze:\n");
			while (iter.hasNext())
				sb.append(i+") "+iter.next()+"\n");
			return sb.toString();
		}
	}
	
	public String displayMenu(){
		
		StringBuilder sb = new StringBuilder();
		sb.append("\n*********************************************************************\n");
		sb.append("*                               CLI Menu                            *\n");
		sb.append("*********************************************************************\n");
		sb.append("1) To view file in directory enter: dir <path>\n");
		sb.append("2) To generate new Maze3D enter: generate_maze <name> <other params>\n");
		sb.append("3) To display a Maze3d enter: display <name>\n");
		sb.append("4) To dispaly cross section of the Maze3D enter: display_cross_section <index{xz,x,y} <name>\n");
		sb.append("5) To display a Maze3d enter: display <name>\n");
		sb.append("6) To save a Maze3D enter: save_maze <name> <file name>\n");
		sb.append("7) To load saved Maze3D enter: load_maze <file name> <name>\n");
		sb.append("8) To solve a Maze3D enter: solve <name> <algorithm(BFS,DFS)>\n");
		sb.append("9) To display the solution path of the maze enter: display_solution <name>\n");
		sb.append("10) To display the menu enter: menu\n");
		sb.append("11) To exit enter: exit\n");
		
		return sb.toString();
	}

	public void exit(){

		//presenter.setSolution("CLI closing...");
		pool.shutdown();
		try {
			pool.awaitTermination(10,TimeUnit.SECONDS );
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public void saveMazes(){
		ObjectOutputStream oos = null;
		
		try {
			oos= new ObjectOutputStream(new GZIPOutputStream(new FileOutputStream("data/database/mapMazes.dat")));
			oos.writeObject(mapMaze3D);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				oos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	public void saveSolution(){
		ObjectOutputStream oos = null;
		
		try {
			oos= new ObjectOutputStream(new GZIPOutputStream(new FileOutputStream("data/database/mapSolutions.dat")));
			oos.writeObject(mapSolution);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				oos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}


}