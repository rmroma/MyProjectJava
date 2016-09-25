package presenter;



public interface Presenter {
	


	
	/**
	 * <h1>Forward command.</h1><p>
	 * <i><ul>void forwardCommand(String command,String[] param)<i><p>
	 * Forward the command and parameters to the {@link Model}</br>
	 * to do all the calculation
	 * @param command - the command to forward
	 * @param param - the parameters to forward
	 * @throws IOException Signals that an I/O exception has occurred.
	 *//*
	public void forwardCommand(String command,String[] param) throws IOException;*/
	
	
	
	
	

	
	public void initCommands();
	
	public void initRegexCommand();
	
}
