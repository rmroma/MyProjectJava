package presenter;


import java.io.IOException;
import java.util.Observable;
import commands.Command;
import model.Model;
import view.View;



public class MyPresenter extends AbstractPresenter {

	

	public MyPresenter(Model model, View view) {
		super(model, view);
		
	}

	


	@Override
	public void update(Observable o, Object args) {
		String commandLine = (String) args;
		System.out.println(commandLine);
		String arr[] = commandLine.split(" ");
		String command = arr[0];
	
		if( o== view){
			if(!regexCommands.containsKey(command))
			{
				String msg = "You entered unrecognized command (\"" + command+"\") for help enter \"menu\"";
				view.displayMessage(msg);
			}
			else{

				if(!checkRegexCommand(command,commandLine)){
					String msg = "You entered in valid parameters (\"" + commandLine+"\") for help enter \"menu\"";
					view.displayMessage(msg);
					return;
				}
				else{
					String c = regexCommands.get(command);
					Command cmd = commands.get(c);
					System.out.println(cmd.toString());
					try {
						cmd.doCommmand(arr);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

			}
		}
		if (o==model){
			if(regexCommands.containsKey(command))
			{
				String[] arg = null;
				if (arr.length > 1) {
					String commandArgs = commandLine.substring(commandLine.indexOf(" ") + 1);
					arg = commandArgs.split(" ");
					arg[0]=commandArgs;
				}
				
				
				String c = regexCommands.get(command);
				Command cmd = commands.get(c);
				System.out.println(cmd.toString());
				try {
					cmd.doCommmand(arg);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else{
				
			}
			
		}
	
	}
			

		
	/*	if( o == view ){
			
			Command command = getCommandByInput(temp);
			
			if(command != null){
				try {
					command.doCommmand(temp.split(" "));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}else{
				String msg = "You entered unrecognized command (\"" + temp+"\") for help enter \"menu\"";
				view.displayMessage(msg);
			}
		
		}
		else if(o == model){
			
			
			
			
			
			if(temp.equals("dir"))
				view.displayMessage((String)model.getCommand(temp));
			
			else if(temp.equals("message"))
				view.displayMessage((String)model.getCommand(temp));
			
			else if(temp.equals("cross section"))
				view.getCrossSection((int[][])model.getCommand(temp));
			else if(temp.equals("display maze"))
				view.displayMaze((Maze3D)model.getCommand(temp));
			else if(temp.equals("display solution"))
				view.displaySolution((Solution<Position>)model.getCommand(temp));
			else if(temp.equals("generate maze")){
				System.out.println((String)model.getCommand(temp));
				view.notifyMazeIsReady((String)model.getCommand(temp));
				
			}
		}*/
		
	
	
	private boolean checkRegexCommand(String command,String commandLine) {
	
		// matching all regular expressions with the given user command
		String regex = regexCommands.get(command);
		return commandLine.matches(regex);
		
		
		
		
	/*	Iterator<String> iter = regexCommands.keySet().iterator();
		while (iter.hasNext() && !commandOk) {
			command = iter.next();
			String s = regexCommands.get(command);
			commandOk = commandLine.matches(s);
		}
		return commandOk*/
	}
	

}