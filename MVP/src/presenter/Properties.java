package presenter;

import java.io.Serializable;



/**
 * The Class Properties.
 * 
 * @author Asi Belachow
 * @version 1.0
 * @since 2016-09-17
 */
public class Properties implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3958798948682138687L;

	
	private String title;
	private Integer width;
	private Integer height;
	private Integer numOfThreads;
	private String searchAlg;
	private Boolean sound;
	private String serverIp;
	private Integer serverPort;
	private Boolean gui;
	
	
	
	public Properties() {
		
		this.sound = true;
		this.title = "Enter windows name";
		this.width = 600;
		this.height = 800;
		this.numOfThreads = 20;
		this.searchAlg = "BFS";
		this.serverPort = 5555;
		this.serverIp = "127.0.0.1";
		this.gui = true;
		
	}
	
	public Properties(String title,Integer width,Integer height,Integer numOfThreads,String searchAlg,Boolean sound,String serverIp,Integer serverPort,Boolean gui) {
	
		this.setTitle(title);
		this.setWidth(width);
		this.setHeight(height);
		this.setNumOfThreads(numOfThreads);
		this.setSearchAlg(searchAlg);
		this.setSound(sound);
		this.setServerIp(serverIp);
		this.setServerPort(serverPort);
		this.setGui(gui);
		
		
	}
	public String getSearchAlg() {
		return searchAlg;
	}

	public void setSearchAlg(String searchAlg) {
		this.searchAlg = searchAlg;
	}

	public Integer getNumOfThreads() {
		return numOfThreads;
	}

	public void setNumOfThreads(Integer numOfThreads) {
		this.numOfThreads = numOfThreads;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public Boolean getSound() {
		return sound;
	}

	public void setSound(Boolean sound) {
		this.sound = sound;
	}

	public String getServerIp() {
		return serverIp;
	}

	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}

	public Integer getServerPort() {
		return serverPort;
	}

	public void setServerPort(Integer serverPort) {
		this.serverPort = serverPort;
	}

	public Boolean getGui() {
		return gui;
	}

	public void setGui(Boolean gui) {
		this.gui = gui;
	}

	
	
	
	
	
	

}
