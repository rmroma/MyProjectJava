package controller;

import java.io.IOException;

import allCommands.Command;

public  class MyController extends MyCommonController {

	@Override
	public void forwardCommand(String command, String[] param) throws IOException  {
		Command comm = commands.get(command);
		comm.doCommmand(param);
		
	}
	
	@Override
	public void setSolution(String solution) {
		v.displaySolution(solution);
		
	}
		
}
