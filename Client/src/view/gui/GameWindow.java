package view.gui;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseWheelListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tray;
import org.eclipse.swt.widgets.TrayItem;
import maze.maze3d.*;
import position.position3d.*;
import algorithms.search.Solution;
import presenter.Properties;
import presenter.PropertiesHandler;
import view.dialogLoader.ClassGUIDialogLoader;
import view.gui.MyMessageBox;
import view.dialogLoader.Maze3DFormDetailes;
import view.gui.widgets.Maze3DDisplayer;
import view.gui.widgets.MazeDisplayer;

/**
 * <h1>GameWindow</h1><p>
 * A type of view in the MVP architecture using SWT GUI to get user input
 * @author Asi Belachow
 * @version 1.0
 * @since 2016-20-09
 */
public class GameWindow extends BasicWindow {

	//------------------------------Data Members-------------------------//

	private Properties properties = null;


	private Clip backGroundMusic = null , sound=null;

	//All item in the menu bar
	private MenuItem generateItem , propertiesItem, loadMazeItem,saveMazeItem, exitItem
	, helpItem, instructionItem, aboutItem, dfsSolve,bfsSolve,aStarMdItem,aStarAdItem,subMenuItem ,gameItem,stopMusicItem,playMusicItem
	,generateMazeTray,loadMazeTray,saveMazeTray,exitMazeTray,solveMazeTray,aboutTray,instructionsTray;

	//All the buttons in the windows
	private Button playMusicButton, stopMusicButton , solveButton, hintButton,
	buttonLeft, buttonRight, buttonBackward,buttonForward, buttonUp, buttonDown, generateButton,connectToServerButton;

	//Combo box for choosing backgrounds 
	private Combo backgroundCombo, characterCombo;

	//Group for the navigation panel
	private Group groupLeftRight,groupBackFor,groupUpDown;

	//For calculation of the its take to solve the maze
	private long startTime;

	//The name of the maze
	private String mazeName;

	//Handle the drawing part of the maze
	private MazeDisplayer mazeDisplayer;

	private MouseWheelListener mouseWheelListener;

	private KeyListener keyListener;
	
	protected MyMessageBox msgError;
	
	protected MyMessageBox msgInfo;
	
	private Tray tray;
	
	private Text consoleText;

	//Hold the listeners of the windows
	HashMap<String, Listener> listeners;



	//------------------------------Constructors-------------------------//


	/**
	 * <h1>GameWindow</h1><p>
	 * <i><ul>GameWindow(String title, int width, int height)<i><p>
	 * Instantiates a new game window.
	 * @param title - title of the window
	 * @param width - width of the window
	 * @param height - height of the window
	 */
	public GameWindow(String title, int width, int height) {
		super(title, width, height);
		listeners = new HashMap<String,Listener>();
		msgInfo= new MyMessageBox(getShell(), SWT.ICON_INFORMATION | SWT.OK);
		msgError= new MyMessageBox(getShell(), SWT.ICON_ERROR | SWT.OK);
	}

	/**
	 * <h1>GameWindow</h1><p>
	 * <i><ul>GameWindow(String title, int width, int height)<i><p>
	 * Instantiates a new game window based on details in the properties xml
	 * @param properties 
	 */
	public GameWindow(Properties properties) {
		super(properties.getTitle(), properties.getWidth(),properties.getHeight());
		setProperties(properties);
		listeners = new HashMap<String,Listener>();
		msgInfo= new MyMessageBox(getShell(), SWT.ICON_INFORMATION | SWT.OK);
		msgError= new MyMessageBox(getShell(), SWT.ICON_ERROR | SWT.OK);
	}

	//-----------------------------setters and getters-------------------//

	/**
	 * <h1>getProperties</h1><p>
	 * <i><ul>Properties getProperties()<i><p>
	 * Gets the properties
	 * @return the properties
	 */
	public Properties getProperties() {
		return properties;
	}

	/**
	 * <h1>setProperties</h1><p>
	 * <i><ul>Properties setProperties()<i><p>
	 * sets the properties
	 * @param properties the new properties
	 */
	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	/**
	 * <h1>getBackGroundMusic</h1><p>
	 * <i><ul>Clip getBackGroundMusic()<i><p>
	 * Gets get the background music.
	 * @return  background music
	 */
	public Clip getBackGroundMusic() {
		return backGroundMusic;
	}

	/**
	 * <h1>setBackGroundMusic</h1><p>
	 * <i><ul>setBackGroundMusic(Clip backGroundMusic)<i><p>
	 * Sets the background music.
	 * @param backGroundMusic - the clip
	 */
	public void setBackGroundMusic(Clip backGroundMusic) {
		this.backGroundMusic = backGroundMusic;
	}



	//-------------------------Functionality-------------------------//


	@Override
	public void start() {
		run();	
	}
	
	
	@Override
	protected void initWidgets() {

		initListeners();
		shell.setLayout(new GridLayout(13, false));
		mazeDisplayer = new Maze3DDisplayer(getShell(), SWT.BORDER | SWT.DOUBLE_BUFFERED);
		mazeDisplayer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 12, 13));
		mazeDisplayer.setBackgroundImage(new Image(getDisplay(), "resources/image/backgrounds/bg_image.png"));

	
		tray =  getDisplay().getSystemTray();
		if( tray != null){
			TrayItem item = new TrayItem(tray, SWT.NONE);
			item.setToolTipText("Maze Game");
			item.setImage(new Image(getDisplay(), "resources/image/tray/tray.png"));
			Menu menu = new Menu(getShell(), SWT.POP_UP);
			generateMazeTray = new MenuItem(menu, SWT.PUSH);
			generateMazeTray.setText("Start Game");
			generateMazeTray.addListener(SWT.Selection, listeners.get("generate_maze"));
			generateMazeTray.setImage(new Image(getDisplay(),"resources/image/button/start_game.png"));
			loadMazeTray = new MenuItem(menu, SWT.PUSH);
			loadMazeTray.setText("Load Maze");
			loadMazeTray.setImage(new Image(getDisplay(), "resources/image/button/load.png"));
			loadMazeTray.addListener(SWT.Selection, listeners.get("load_maze"));
			saveMazeTray = new MenuItem(menu, SWT.PUSH);
			saveMazeTray.setText("Save Maze");
			saveMazeTray.setImage(new Image(getDisplay(), "resources/image/button/save.png"));
			saveMazeTray.addListener(SWT.Selection, listeners.get("save_maze"));
			solveMazeTray = new MenuItem(menu, SWT.PUSH);
			solveMazeTray.setText("Solve Maze");
			solveMazeTray.setImage(new Image(getDisplay(), "resources/image/button/solve.png"));
			solveMazeTray.addListener(SWT.Selection, listeners.get("solve_maze"));
			instructionsTray = new MenuItem(menu, SWT.PUSH);
			instructionsTray.setText("Instructions");
			instructionsTray.setImage(new Image(getDisplay(),"resources/image/button/instructions.png"));
			instructionsTray.addListener(SWT.Selection, listeners.get("instructions"));
			aboutTray = new MenuItem(menu, SWT.PUSH);
			aboutTray.setText("about");
			aboutTray.addListener(SWT.Selection, listeners.get("about"));
			aboutTray.setImage(new Image(getDisplay(),"resources/image/button/about.png"));
			exitMazeTray = new MenuItem(menu, SWT.PUSH);
			exitMazeTray.setText("Exit Game");
			exitMazeTray.setImage(new Image(getDisplay(),"resources/image/button/exit.png"));
			exitMazeTray.addListener(SWT.Selection, listeners.get("exit"));

			item.addListener(SWT.MenuDetect, new Listener() {
				public void handleEvent(Event event) {
					menu.setVisible(true);
				}
			});
			item.setToolTipText("Maze Game");
			item.setImage(new Image(getDisplay(), "resources/image/tray/tray.png"));
			
			item.addListener(SWT.Selection,new Listener() {
				
				@Override
				public void handleEvent(Event arg0) {
					getShell().forceActive();
					
				}
			});
		}
			
		
		
		//**************************Menu Bar Items************************************
		//****************************************************************************

		Menu menuBar = new Menu(getShell(), SWT.BAR);
		shell.setMenuBar(menuBar);
		//****************************************************************************

		//**************************File Menu************************************
		//***********************************************************************


		/*----------Add file menu-------------*/
		MenuItem fileItem = new MenuItem(menuBar, SWT.CASCADE);
		fileItem.setText("&File"); //Set text for foe file menu
		//****************************************************************************

		//Drop down  for file menu
		Menu fileMenu = new Menu(getShell(), SWT.DROP_DOWN);
		fileItem.setMenu(fileMenu);
		//****************************************************************************

		/*----Add element properties to File menu----*/
		propertiesItem = new MenuItem(fileMenu, SWT.PUSH);
		propertiesItem.setText("Settings"); // Listener for load maze

		//****************************************************************************	

		/*----Add element Load maze to File menu----*/
		loadMazeItem = new MenuItem(fileMenu, SWT.PUSH);
		loadMazeItem.setText("Load maze");
		loadMazeItem.setImage(new Image(getDisplay(), "resources/image/button/load.png"));

		//****************************************************************************
		/*----Add element Save maze to File menu----*/
		saveMazeItem = new MenuItem(fileMenu, SWT.PUSH);
		saveMazeItem.setText("Save maze");
		saveMazeItem.setImage(new Image(getDisplay(), "resources/image/button/save.png"));

		//****************************************************************************
		/*----Add element Exit maze to File menu----*/
		exitItem = new MenuItem(fileMenu, SWT.PUSH);
		exitItem.setText("Exit");
		exitItem.setImage(new Image(getDisplay(),"resources/image/button/exit.png"));

		//**************************Game Menu Items************************************
		//****************************************************************************

		/*----Add Game menu to the menu bar----*/
		gameItem = new MenuItem(menuBar, SWT.CASCADE);
		gameItem .setText("&Game");
		//****************************************************************************

		//Drop down  for game menu
		Menu gameMenu = new Menu(getShell(), SWT.DROP_DOWN);
		gameItem.setMenu(gameMenu);

		//****************************************************************************

		/*----Add element generate to the game bar----*/
		generateItem = new MenuItem(gameMenu, SWT.PUSH);
		generateItem.setText("Start Game");
		generateItem.setEnabled(false);
		generateItem.setImage(new Image(getDisplay(),"resources/image/button/start_game.png"));
		//****************************************************************************

		/*----Add element cascade----*/
		subMenuItem = new MenuItem(gameMenu, SWT.CASCADE);
		subMenuItem.setText("Solve maze by..");
		subMenuItem.setImage(new Image(getDisplay(),"resources/image/button/solve.png"));

		//****************************************************************************
		/*----Add element drop down---*/
		Menu subMenu = new Menu(getShell(), SWT.DROP_DOWN);
		subMenuItem.setMenu(subMenu);

		//****************************************************************************
		/*----Add element Solve by BFS algorithm to solve sub menu ---*/
		bfsSolve = new MenuItem(subMenu, SWT.PUSH);
		bfsSolve.setText("Solve by BFS algorithm");
		bfsSolve.setEnabled(false);

		//****************************************************************************

		/*----Add element Solve by DFS algorithm to solve sub menu ---*/
		dfsSolve = new MenuItem(subMenu, SWT.PUSH);
		dfsSolve.setText("Solve by DFS algorithm");
		dfsSolve.setEnabled(false);

		//****************************************************************************	
		
		aStarAdItem = new MenuItem(subMenu, SWT.PUSH);
		aStarAdItem.setText("Solve by A*-AirDistance algorithm");
		aStarAdItem.setEnabled(false);
		
		//****************************************************************************	
		
		aStarMdItem = new MenuItem(subMenu, SWT.PUSH);
		aStarMdItem.setText("Solve by A*-ManhattanDistance algorithm");
		aStarMdItem.setEnabled(false);
		
		
		/*----Add element Play music to game menu ---*/
		playMusicItem = new MenuItem(gameMenu, SWT.PUSH);
		playMusicItem.setText("Play music");
		playMusicItem.setImage(new Image(getDisplay(), "resources/image/music/playmusic.png"));

		//****************************************************************************	
		/*----Add element Stop music to game menu ---*/
		stopMusicItem = new MenuItem(gameMenu, SWT.PUSH);
		stopMusicItem.setText("Stop music");
		stopMusicItem.setImage(new Image(display, "resources/image/music/stopmusic.png"));
		stopMusicItem.setEnabled(false);

		/*----Add Help menu to the menu bar----*/
		Menu helpMenu = new Menu(getShell(), SWT.DROP_DOWN);
		//****************************************************************************	

		helpItem = new MenuItem(menuBar, SWT.CASCADE);
		helpItem.setText("&Help");
		helpItem.setMenu(helpMenu);

		//****************************************************************************
		/*----Add element instruction to Help menu----*/
		instructionItem = new MenuItem(helpMenu, SWT.PUSH);
		instructionItem.setText("Instructions");
		instructionItem.setImage(new Image(getDisplay(), "resources/image/button/instructions.png"));

		//****************************************************************************	
		/*----Add element about to Help menu----*/
		aboutItem = new MenuItem(helpMenu, SWT.PUSH);
		aboutItem.setText("About");
		aboutItem.setImage(new Image(getDisplay(),"resources/image/button/about.png"));

		//****************************************************************************
		//****************************************************************************


		
		//**************************Buttons*******************************************
		//****************************************************************************	
		
		//----Connect/Disconnect from server to server Button----//
		connectToServerButton = new Button(getShell(), SWT.PUSH);
		connectToServerButton.setText("Connect to server");
		connectToServerButton.setBackground(new Color(getDisplay(), 210,105,30));
		connectToServerButton.setLayoutData(new GridData(SWT.FILL, SWT.NONE, false, false,1, 1));
		
		//----Generate maze Button----//
		generateButton = new Button(getShell(),SWT.PUSH);
		generateButton.setText("Start Game");
		generateButton.setBackground(new Color(getDisplay(), 210,105,30));
		generateButton.setLayoutData(new GridData(SWT.FILL, SWT.NONE, false, false,1, 1));
		generateButton.setImage(new Image(getDisplay(),"resources/image/button/start_game.png"));
		//****************************************************************************

		//----Background selection combo box----//
		backgroundCombo = new Combo(getShell(), SWT.DROP_DOWN | SWT.READ_ONLY | SWT.CENTER);
		backgroundCombo.setText("Choose Background");
		String items[] = { "Game Background","Sunset Background","Nature Background","Color Background",
				"Purple Background","Red Background","Yellow Background","Brown Background","White Background","Blue Background","Green Background","Black Background",};
		backgroundCombo.setItems(items);
		backgroundCombo.setLayoutData(new GridData(SWT.FILL, SWT.NONE, false, false,1, 1));
		backgroundCombo.select(0);
		//****************************************************************************
		
		//----Character selection combo box----//
		characterCombo = new Combo(getShell(), SWT.DROP_DOWN | SWT.READ_ONLY | SWT.CENTER);
		characterCombo.setText("Choose Character");
		String items1[] = { "Marco 1","Marco 2","Sonic","Mario","Son Goku 1","Son Goku 2","Hitman"};
		characterCombo.setItems(items1);
		characterCombo.setLayoutData(new GridData(SWT.FILL, SWT.NONE, false, false,1, 1));
		characterCombo.select(0);
		//****************************************************************************
		
		//----Play Music Button----//
		playMusicButton =  new Button(shell, SWT.PUSH);
		playMusicButton.setText("Play music");
		playMusicButton.setImage(new Image(display, "resources/image/music/playmusic.png"));
		playMusicButton.setBackground(new Color(getDisplay(), 210,105,30));
		playMusicButton.setLayoutData(new GridData(SWT.FILL, SWT.NONE, false, false,1, 1));
		//****************************************************************************

		//----Stop Music Button----//
		stopMusicButton =  new Button(shell, SWT.PUSH | SWT.LEFT);
		stopMusicButton.setText("Stop music");
		stopMusicButton.setBackground(new Color(getDisplay(), 210,105,30));
		stopMusicButton.setImage(new Image(display, "resources/image/music/stopmusic.png"));
		stopMusicButton.setLayoutData(new GridData(SWT.FILL, SWT.NONE, false, false,1, 1));
		stopMusicButton.setEnabled(false);
		//******************************************************************************


		//----Solve Music Button----//
		solveButton =  new Button(getShell(), SWT.PUSH);
		solveButton.setText("Solve maze");
		solveButton.setBackground(new Color(getDisplay(), 210,105,30));
		solveButton.setLayoutData(new GridData(SWT.FILL, SWT.NONE, false, false,1, 1));
		solveButton.setImage(new Image(getDisplay(), "resources/image/button/solve.png"));
		//******************************************************************************
		
		//----Hint Music Button----//
		hintButton = new Button(getShell(), SWT.PUSH);
		hintButton.setText("Show hint");
		hintButton.setBackground(new Color(getDisplay(), 210,105,30));
		hintButton.setLayoutData(new GridData(SWT.FILL, SWT.NONE, false, false,1, 1));
		//****************************************************************************
		
		//----Navigation Panel----//

		//----Add text----//
		Text text = new Text(getShell(),  SWT.MULTI | SWT.CENTER | SWT.READ_ONLY | SWT.BOLD  );
		text.setFont(new Font(getDisplay(), "Aharoni", 10, SWT.BOLD |SWT.COLOR_BLACK));
		text.setBounds(100, 100,50, 50);
		text.setText("\nArrows Navigation\nPanel");
		text.setToolTipText("Use the arrows below in order to\nmove the charcter");
		text.setBackground(new Color(getDisplay(), 0,102,204));
		text.setLayoutData(new GridData(SWT.CENTER, SWT.NONE, false, false,1, 1));
		text.pack();
		//****************************************************************************

		//----Add group for arrow left and right----//
		groupLeftRight = new Group(getShell(), SWT.SHADOW_IN);
		groupLeftRight.setSize(1, 1);
		groupLeftRight.setText("Left           Right");
		groupLeftRight.setLayoutData(new GridData(SWT.CENTER, SWT.NONE, false, false,1, 1));
		//****************************************************************************
		
		//----Add group for arrow backward and forward----//
		groupBackFor = new Group(getShell(), SWT.SHADOW_IN);
		groupBackFor.setSize(1, 1);
		groupBackFor.setText("Fward       Bward");
		groupBackFor.setLayoutData(new GridData(SWT.CENTER, SWT.NONE, false, false,1, 1));
		//****************************************************************************

		//----Add group for arrow up and down----//
		groupUpDown = new Group(getShell(), SWT.SHADOW_IN);
		groupUpDown.setSize(1, 1);
		groupUpDown.setText("Down      Up");
		groupUpDown.setLayoutData(new GridData(SWT.CENTER, SWT.NONE, false, false,1, 1));
		//****************************************************************************

		//----Add right arrow button----//
		buttonRight = new Button(groupLeftRight, SWT.RIGHT);
		buttonRight.setBounds(50, 20, 40, 35);
		buttonRight.setLayoutData(new GridData(SWT.RIGHT, SWT.NONE, false, false,1, 1));
		buttonRight.setImage(new Image(getDisplay(), "resources/image/navigations/arrow_right.png"));
		//****************************************************************************

		//----Add left arrow button----//
		buttonLeft = new Button(groupLeftRight, SWT.LEFT);
		buttonLeft.setBounds(0, 20, 40, 35);
		buttonLeft.setLayoutData(new GridData(SWT.LEFT, SWT.NONE, false, false,1, 1));
		buttonLeft.setImage(new Image(getDisplay(), "resources/image/navigations/arrow_left.png"));
		//****************************************************************************

		//----Add backward arrow button----//
		buttonBackward = new Button(groupBackFor,SWT.RIGHT );
		buttonBackward.setBounds(50, 27, 40, 35);
		buttonBackward.setLayoutData(new GridData(SWT.RIGHT, SWT.NONE, false, false,1, 1));
		buttonBackward.setImage(new Image(getDisplay(), "resources/image/navigations/arrow_back.png"));
		//****************************************************************************

		//----Add forward arrow button----//
		buttonForward = new Button(groupBackFor, SWT.LEFT);
		buttonForward.setBounds(0, 27, 40, 35);
		buttonForward.setLayoutData(new GridData(SWT.LEFT, SWT.NONE, false, false,1, 1));
		buttonForward.setImage(new Image(getDisplay(), "resources/image/navigations/arrow_for.png"));
		//****************************************************************************
		
		//----Add up arrow button----//
		buttonUp = new Button(groupUpDown, SWT.LEFT);
		buttonUp.setBounds(50, 27, 40, 35);
		buttonUp.setLayoutData(new GridData(SWT.LEFT, SWT.NONE, false, false,1, 1));	
		buttonUp.setImage(new Image(getDisplay(), "resources/image/navigations/arrow_up.png"));
		//****************************************************************************

		//----Add down arrow button----//
		buttonDown = new Button(groupUpDown, SWT.RIGHT);
		buttonDown.setBounds(0, 27, 40, 35);
		buttonDown.setLayoutData(new GridData(SWT.RIGHT, SWT.NONE, false, false,1, 1));
		buttonDown.setImage(new Image(getDisplay(), "resources/image/navigations/arrow_down.png"));
		//****************************************************************************

		groupLeftRight.pack();
		groupBackFor.pack();
		groupUpDown.pack();
		
		//**************************Text*******************************************
		//****************************************************************************
		
		//----Add group for the text----//
		Group consoleGroup = new Group(shell, SWT.SHADOW_ETCHED_IN);
		consoleGroup.setLayout(new GridLayout(1, true));
		consoleGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 13, 2));
		consoleGroup.setText("Console :");
		//****************************************************************************
		
		
		//----Add the console text----//
		consoleText = new Text(consoleGroup, SWT.READ_ONLY | SWT.MULTI | SWT.BORDER | SWT.V_SCROLL);
		consoleText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		consoleText.setToolTipText("Here you can see all the actions");
		consoleText.setBackground(new Color(display, 255, 255, 255));
		//****************************************************************************

		
		assignListenerToWidgets();
		initButtons();
		

	}



	/**
	 * <h1>Play Music InLoop</h1><p>
	 * <i><ul>void playMusicInLoop(File file) <i><p>
	 * Gets a music file and play it in loop.
	 * @param file - music file
	 * @throws LineUnavailableException the line unavailable exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws UnsupportedAudioFileException the unsupported audio file exception
	 */
	private void playBackgroundSoundInLoop(File file) throws LineUnavailableException,IOException,UnsupportedAudioFileException{
		try {
			backGroundMusic = AudioSystem.getClip();
			AudioInputStream inputStream = AudioSystem.getAudioInputStream(new BufferedInputStream(new FileInputStream(file)));
			backGroundMusic.open(inputStream);
			// loop infinitely
			backGroundMusic.setLoopPoints(0, -1);
			backGroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
		} catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {

		}

	}

	/**
	 * <h1>Play Move Music</h1><p>
	 * <i><ul>void playMoveMusic<i><p>
	 * Play a sound.
	 * @throws LineUnavailableException the line unavailable exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws UnsupportedAudioFileException the unsupported audio file exception
	 */
	private void playMoveSound() throws LineUnavailableException,IOException,UnsupportedAudioFileException{


		AudioInputStream audioInputStream;
		try {
			audioInputStream = AudioSystem.getAudioInputStream(new File("resources/music/move.wav").getAbsoluteFile());
			Clip clip;
			clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			clip.start();
		} catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	/**
	 * <h1>Play Music</h1><p>
	 * <i><ul>void playMusic(File file,Clip sound)<i><p>
	 * Attach music file to clip and play the music file.
	 * @param file - music file
	 * @param sound - the clip
	 * @throws LineUnavailableException the line unavailable exception
	 * @throws UnsupportedAudioFileException the unsupported audio file exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void playMusic(File file,Clip sound) throws LineUnavailableException, UnsupportedAudioFileException,IOException   {

		AudioInputStream inputStream;
		try {
			inputStream = AudioSystem.getAudioInputStream(new BufferedInputStream(new FileInputStream(file)));
			AudioFormat format = inputStream.getFormat();
			DataLine.Info info = new DataLine.Info(Clip.class, format);
			sound = (Clip) AudioSystem.getLine(info);
			sound.open(inputStream);
			sound.start();
		} catch (LineUnavailableException  | UnsupportedAudioFileException |IOException e) {
			e.printStackTrace();
		}

	}


	/**
	 * <h1>Stop Music</h1><p>
	 * <i><ul>void stopMusic()<i><p>
	 * Attach music file to clip and play the music file.
	 */
	private void stopMusic(){
		if(getBackGroundMusic()!=null){
			if(getBackGroundMusic().isRunning()){
				getBackGroundMusic().stop();
				getBackGroundMusic().close();
			}
		}


	}

	
	@Override
	public void displayMessage(String msg) {
		msgInfo.showMessage(getDisplay(), "message", msg);
	}

	
	@Override
	public void notifyMazeIsReady(String name) {
		System.out.println(name);
		String command = "display" + " " + name;
		setCommandAndNotify(cli.getCommandByInput(command), command.split(" "));
		

	}

	@Override
	public void notifySolutionIsReady(String name) {
		String command = "display solution " + name;
		setCommandAndNotify(cli.getCommandByInput(command), command.split(" "));
	}

	@Override
	public void close() {
		System.out.println("closing the gui gui");
		if(getDisplay()!=null && !getDisplay().isDisposed()){
			setCommandAndNotify(cli.getCommandByInput("exit"), null);
			closeWidget();
			//getShell().dispose();
		}
	
				
	}
	
	
	@Override
	public void getCrossSection(int[][] section) {
		// TODO Auto-generated method stub

	}

	@Override
	public void displayMaze(Maze3D maze) {
		mazeName = maze.getMazeName();
		msgInfo.showMessage(getDisplay(), "Maze ready", "Your maze is ready.\nYou can start play");
		display.syncExec(new Runnable() {

			@Override
			public void run() {
				//System.out.println(maze.toString());
				((Maze3DDisplayer) mazeDisplayer).setMaze(maze);
				mazeDisplayer.setCharacterPosition(maze.getStart());
				mazeDisplayer.setWinEvent(new MazeWinEventHandler() {
					@Override
					public void winGame() {
						try {
							playMusic(new File("resources/music/win_game.wav"), sound);
						} catch (Exception e) {
						}
						Shell win = new Shell(getShell());
						win.setLayout(new GridLayout(2, false));
						win.setText("Win!!!!");
						win.setSize(700, 500);
						win.setBackgroundImage(((Maze3DDisplayer) mazeDisplayer).getWinImage());
						Text t = new Text(win, SWT.MULTI| SWT.READ_ONLY  | SWT.BOLD |SWT.TRANSPARENT |SWT.BORDER);
						t.setBackground(new Color(getDisplay(), 27,68,120));
						t.setText("Game completed in " + ((System.currentTimeMillis() - startTime) / 1000) + " seconds\nIt took you "+ mazeDisplayer.getNumofSteps()+ " steps to help marco!!!");
						t.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, true, 2, 1));
						t.setFont(new Font(getDisplay(), "Ravie", 15 , SWT.BOLD |SWT.COLOR_BLACK));
						t.setEnabled(true);
						win.open();
						solveMazeButtonInit();
						//restart();
					}
				});
				shell.layout();
				startTime = System.currentTimeMillis();
				shell.addKeyListener(keyListener);
				displayMazeButtoninit();
				if(properties.getSound())
					try {
						playMusic(new File("resources/music/maze_generate.wav"),  sound);
					} catch (Exception e) {
					}
				getShell().forceFocus();

			}	
		});

	}

	@Override
	public void displaySolution(Solution<Position3D> solution) {
		writeToConsole("Solving the maze...");
		mazeDisplayer.showSoution(solution.getArraySolution());
	}

	
	/**
	 * <h1>restart</h1><p>
	 * <i><ul>void restart()<i><p>
	 * Reset the widget after solving the maze
	 *//*
	public void restart(){
	
	}*/


	/**
	 * <h1>Init Listeners</h1><p>
	 * <i><ul>void initListeners()<i><p>
	 * Initialize all the listeners
	 */
	public void initListeners(){

		//Add lister for playing background music
		listeners.put("play_music", new Listener() {

			@Override
			public void handleEvent(Event arg0) {
				try {
					playBackgroundSoundInLoop(new File("resources/music/m1.wav"));
					playMusicItem.setEnabled(false);
					playMusicButton.setEnabled(false);
					stopMusicButton.setEnabled(true);
					stopMusicItem.setEnabled(true);
				} catch (Exception e) {
					System.out.println("Error while trying play background music");
				}
			}
	
		});

		//Add lister for stop background music
		listeners.put("stop_music", new Listener() {

			@Override
			public void handleEvent(Event arg0) {
				stopMusic();
				stopMusicItem.setEnabled(false);
				stopMusicButton.setEnabled(false);
				playMusicItem.setEnabled(true);
				playMusicButton.setEnabled(true);
				
			}
		});

		//Add lister for solving the maze
		listeners.put("solve_maze", new Listener() {

			@Override
			public void handleEvent(Event e) {

				MessageBox messageBox = new MessageBox(shell, SWT.ICON_QUESTION | SWT.YES | SWT.NO );
				display.syncExec(new Runnable() {

					@Override
					public void run() {
						messageBox.setText("Solve maze");
						messageBox.setMessage("Dont give up!!!\nAre you sure you want to show the maze solution?\n ");
						int buttonID = messageBox.open();
						switch(buttonID) {
						case SWT.YES:
							String alg =null;
							if(e.widget == bfsSolve){
								System.out.println("User Choose to solve the maze by BFS algorithm");
								alg = "BFS";
							}
								
							else if (e.widget == dfsSolve){
								System.out.println("User Choose to solve the maze by DFS algorithm");
								alg ="DFS";
							}
							else if(e.widget == aStarAdItem){
								System.out.println("User Choose to solve the maze by A*-AirDistance algorithm");
								alg ="AstarAD";
							}
							else if(e.widget == aStarMdItem){
								System.out.println("User Choose to solve the maze by A*-ManhattanDistance algorithm");
								alg ="AstarMD";
							}
							else if (e.widget==solveButton || e.widget==solveMazeTray){
								if (properties!=null)
									alg = properties.getSearchAlg();
								else 
									alg = "AstarMD";
							}
							String command = "solve maze " + mazeName +" " +alg;
							setCommandAndNotify(cli.getCommandByInput(command), command.split(" "));
							solveButton.setEnabled(false);
							solveMazeTray.setEnabled(false);
							bfsSolve.setEnabled(false);
							dfsSolve.setEnabled(false);
							aStarAdItem.setEnabled(false);
							aStarMdItem.setEnabled(false);
							break;
						case SWT.NO:
							System.out.println("User cancel solve");
							solveButton.setEnabled(true);
							solveMazeTray.setEnabled(true);
							bfsSolve.setEnabled(true);
							dfsSolve.setEnabled(true);
							aStarAdItem.setEnabled(true);
							aStarMdItem.setEnabled(true);
							break;
						case SWT.CANCEL:
							// does nothing ...
						}
						System.out.println(buttonID);
					}
				});
			}
		});

		
		//Add lister for hint button
		listeners.put("hint", new Listener() {
			@Override
			public void handleEvent(Event e) {
				MessageBox messageBox=null;

				boolean flag = false;
				if(hintButton.getText().equals("Show hint")){
					messageBox = new MessageBox(shell, SWT.ICON_QUESTION | SWT.YES | SWT.NO );
					messageBox.setMessage("Try Harder!!!\nAre you sure you want to show the hint?");
					flag=true;
				}
				else{
					messageBox = new MessageBox(shell, SWT.ICON_INFORMATION | SWT.OK );
					messageBox.setMessage("Smartass!!! Now you can solve the maze easly");
					flag=false;
				}
				messageBox.setText("Hint...");
				int buttonID = messageBox.open();
				switch(buttonID) {
				case SWT.YES:
					writeToConsole("Showing the hint...");
					if(flag==true){
						((Maze3DDisplayer) mazeDisplayer).setHintFlag(true);
						hintButton.setText("Hide hint");
					}
					break;
				case SWT.OK:
					writeToConsole("Hiding the hint...");
					System.out.println("User clicked no");
					((Maze3DDisplayer) mazeDisplayer).setHintFlag(false);
					hintButton.setText("Show hint");
					break;
				case SWT.CANCEL:
					// does nothing ...
				}
				System.out.println(buttonID);
			}
		});

		
		//---------------------Add listeners for navigation arrows--------------------------//
		
		listeners.put("move_left", new Listener() {

			@Override
			public void handleEvent(Event e) {
				mazeDisplayer.moveLeft();
				//play
			}
		});

		listeners.put("move_right", new Listener() {

			@Override
			public void handleEvent(Event e) {
				mazeDisplayer.moveRight();
			}
		});

		listeners.put("move_forward", new Listener() {

			@Override
			public void handleEvent(Event e) {
				mazeDisplayer.moveFront();
			}
		});

		listeners.put("move_backward", new Listener() {

			@Override
			public void handleEvent(Event e) {
				mazeDisplayer.moveBack();
			}
		});

		listeners.put("move_up", new Listener() {

			@Override
			public void handleEvent(Event e) {
				mazeDisplayer.moveUp();
			}
		});

		listeners.put("move_down", new Listener() {

			@Override
			public void handleEvent(Event e) {
				mazeDisplayer.moveDown();
			}
		});

		//---------------------Add listeners foe exit--------------------------//
		listeners.put("exit", new Listener() {
			
			@Override
			public void handleEvent(Event e) {
				
				MessageBox messageBox = new MessageBox(shell, SWT.ICON_QUESTION | SWT.YES | SWT.NO );
				messageBox.setText("Exit");
				messageBox.setMessage("Are you sure you want to exit the game?");
				int buttonID = messageBox.open();
				switch (buttonID) {
				case SWT.YES:
					close();
					//closeWidget();
					e.doit =true;
					break;
				case SWT.NO:
					e.doit =false;
					break;
				default:
					break;
				}
			}
		});
		
		
		//---------------------Add listeners for generating maze--------------------------//
		listeners.put("generate_maze", new Listener() {

			@Override
			public void handleEvent(Event e) {
				ClassGUIDialogLoader dialog = new ClassGUIDialogLoader(getShell(), SWT.DIALOG_TRIM | SWT.SYSTEM_MODAL, Maze3DFormDetailes.class);
				dialog.setSubmitListener(new SelectionListener() {

					@Override
					public void widgetSelected(SelectionEvent e) {
						Maze3DFormDetailes mazeForm = (Maze3DFormDetailes) dialog.getInstance();

						// validates fields(not null and in range)
						if (mazeForm != null && mazeForm.getName() != null && !mazeForm.getName().isEmpty()
								&& mazeForm.getLevels() != null
								&& mazeForm.getColumns() != null 
								&& mazeForm.getRows() != null ) {

							if(!mazeForm.getName().matches("[A-Za-z0-9]+")){
								msgInfo.showMessage(getDisplay(), "Message", "Please enter a valid maze name ([A-Za-z0-9])");
								return;
							}
							String command = "generate maze 3D"+" "+mazeForm.getName()+" "+mazeForm.getLevels()+" "
									+mazeForm.getRows()+ " "+mazeForm.getColumns();
							setCommandAndNotify(cli.getCommandByInput(command), command.split(" "));
							mazeName = mazeForm.getName();
						}
					}

					@Override
					public void widgetDefaultSelected(SelectionEvent arg0) {	
					}

				});
				dialog.showDialog();
			}
		});
		
		//---------------------Add listeners for load maze--------------------------//
		listeners.put("load_maze", new Listener() {
			
			@Override
			public void handleEvent(Event arg0) {
				FileDialog fd = new FileDialog(getShell(),SWT.OPEN);
				fd.setFilterPath(" ");
				fd.setText("Load Maze File");
				fd.setFilterExtensions("*.maz".split(" "));
				String selectedMaze = fd.open();
				if (selectedMaze != null) {
					String mazeName = selectedMaze.substring(selectedMaze.lastIndexOf("\\") + 1,
							selectedMaze.lastIndexOf("."));
					System.out.println(mazeName);
					String command = "load maze"+" "+ selectedMaze;
					setCommandAndNotify(cli.getCommandByInput(command), command.split(" "));

				}
				
			}
		});
		
		//---------------------Add listeners for save maze--------------------------//
		listeners.put("save_maze", new Listener() {
			
			@Override
			public void handleEvent(Event arg0) {
				FileDialog fd = new FileDialog(getShell(),SWT.SAVE);
				fd.setFilterPath(" ");
				fd.setText("Save Maze File");
				fd.setFilterExtensions("*.maz *.xml *.java .*".split(" "));
				String selectedMaze = fd.open();
				if(selectedMaze != null){
					String command = "save maze " + selectedMaze;
					setCommandAndNotify(cli.getCommandByInput(command), command.split(" "));
				}
				
			}
		});

		// user mouse wheel listener (zoom in/out)
		mouseWheelListener = new MouseWheelListener() {

			@Override
			public void mouseScrolled(MouseEvent e) {
				// if both ctrl and wheel are being operated
				if ((e.stateMask & SWT.CTRL) != 0)
					mazeDisplayer.setSize(mazeDisplayer.getSize().x + e.count, mazeDisplayer.getSize().y + e.count);

			}
		};

		// user key press listener
		keyListener = new KeyListener() {

			@Override
			public void keyReleased(KeyEvent arg0) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.keyCode == SWT.ARROW_UP) {
					mazeDisplayer.moveBack();
					if(properties.getSound())
						try{playMoveSound();} catch (Exception e1){}
				}
				else if (e.keyCode == SWT.ARROW_DOWN) {
					mazeDisplayer.moveFront();
					if(properties.getSound())
						try{playMoveSound();} catch (Exception e1){}
				}

				else if (e.keyCode == SWT.ARROW_RIGHT){
					mazeDisplayer.moveRight();
					if(properties.getSound())
						try{playMoveSound();} catch (Exception e1){}
				}
				else if (e.keyCode == SWT.ARROW_LEFT){
					mazeDisplayer.moveLeft();
					if(properties.getSound())
						try{playMoveSound();} catch (Exception e1){}
				}
				else if (e.keyCode == SWT.PAGE_UP){
					mazeDisplayer.moveUp();
					if(properties.getSound())
						try{playMoveSound();} catch (Exception e1){}
				}
				else if (e.keyCode == SWT.PAGE_DOWN) {
					mazeDisplayer.moveDown();
					if(properties.getSound())
						try{playMoveSound();} catch (Exception e1){}
				}
			}
		};
		
		//---------------------Add listeners for instructions--------------------------//
		listeners.put("instructions", new Listener() {
			
			@Override
			public void handleEvent(Event arg0) {
				StringBuilder sb = new StringBuilder();
				try {
					BufferedReader br = new BufferedReader(new FileReader("resources/properties/instruction.txt")) ;
					String line = br.readLine();
					while (line != null) {
						sb.append(line);
						sb.append(System.lineSeparator());
						line = br.readLine();
					}
					msgInfo.showMessage(getDisplay(), "Instrucation", sb.toString());
				} catch (Exception e) {
					msgError.showMessage(getDisplay(), "Instrucation", "There was error while trying load instrucations");
				}	
			}
		});
		
		//---------------------Add listeners for about--------------------------//
		listeners.put("about", new Listener() {
			
			@Override
			public void handleEvent(Event arg0) {
				
				StringBuilder sb = new StringBuilder();
				try {
					BufferedReader br = new BufferedReader(new FileReader("resources/properties/about.txt")) ;
					String line = br.readLine();
					while (line != null) {
						sb.append(line);
						sb.append(System.lineSeparator());
						line = br.readLine();
					}
					
					msgInfo.showMessage(getDisplay(), "About", sb.toString());
				} catch (Exception e) {
					msgError.showMessage(getDisplay(), "About","There was error while trying load about details");
				}	
			}
		});

	}
	
	/**
	 * <h1>assignListenerToWidgets</h1><p>
	 * <i><ul>void assignListenerToWidgets()<i><p>
	 * This method assign listeners to all the widgets 
	 */
	public void assignListenerToWidgets(){
		
		//----Add listeners for all buttons and items----//
		generateButton.addListener(SWT.Selection, listeners.get("generate_maze"));
		generateItem.addListener(SWT.Selection, listeners.get("generate_maze"));
		playMusicItem.addListener(SWT.Selection,listeners.get("play_music") );
		playMusicButton.addListener(SWT.Selection , listeners.get("play_music"));
		stopMusicButton.addListener(SWT.Selection, listeners.get("stop_music"));
		stopMusicItem.addListener(SWT.Selection, listeners.get("stop_music"));
		exitItem.addListener(SWT.Selection, listeners.get("exit"));
		dfsSolve.addListener(SWT.Selection, listeners.get("solve_maze"));
		bfsSolve.addListener(SWT.Selection, listeners.get("solve_maze"));
		aStarAdItem.addListener(SWT.Selection, listeners.get("solve_maze"));
		aStarMdItem.addListener(SWT.Selection, listeners.get("solve_maze"));
		solveButton.addListener(SWT.Selection, listeners.get("solve_maze"));
		buttonRight.addListener(SWT.Selection, listeners.get("move_right"));
		buttonLeft.addListener(SWT.Selection, listeners.get("move_left"));
		buttonForward.addListener(SWT.Selection, listeners.get("move_backward"));
		buttonBackward.addListener(SWT.Selection, listeners.get("move_forward"));
		buttonUp.addListener(SWT.Selection, listeners.get("move_up"));
		buttonDown.addListener(SWT.Selection, listeners.get("move_down"));
		hintButton.addListener(SWT.Selection, listeners.get("hint"));
		loadMazeItem.addListener(SWT.Selection, listeners.get("load_maze"));
		instructionItem.addListener(SWT.Selection, listeners.get("instructions"));
		instructionsTray.addListener(SWT.Selection, listeners.get("instructions"));
		aboutItem.addListener(SWT.Selection, listeners.get("about"));
		getShell().addMouseWheelListener(mouseWheelListener);
		getShell().addListener(SWT.Close, listeners.get("exit"));
		
		//----Add listener for Connect To Server Button----//
		connectToServerButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if(connectToServerButton.getText().equals("Connect to server")){
					setCommandAndNotify(cli.getCommandByInput("connect to server"), null);
				

				}
				else if(connectToServerButton.getText().equals("Disconnect from server")){
					setCommandAndNotify(cli.getCommandByInput("disconnect from server"), null);
					connectToServerButton.setText("Connect to server");
				}

			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {


			}
		});


		//----Add listener for background selection----//
		backgroundCombo.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				String ch =backgroundCombo.getText();
				switch (ch) {
				case "Game Background":
					mazeDisplayer.setBackgroundImage(mazeDisplayer.getBackgrounds().get("game_background"));
					break;
				case "Red Background":
					mazeDisplayer.setBackgroundImage(mazeDisplayer.getBackgrounds().get("background_red"));
					break;
				case "Green Background":
					mazeDisplayer.setBackgroundImage(mazeDisplayer.getBackgrounds().get("background_green"));
					break;
				case "Blue Background":
					mazeDisplayer.setBackgroundImage(mazeDisplayer.getBackgrounds().get("background_blue"));
					break;
				case "Yellow Background":
					mazeDisplayer.setBackgroundImage(mazeDisplayer.getBackgrounds().get("background_yellow"));
					break;
				case "White Background":
					mazeDisplayer.setBackgroundImage(mazeDisplayer.getBackgrounds().get("background_white"));
					break;
				case "Brown Background":
					mazeDisplayer.setBackgroundImage(mazeDisplayer.getBackgrounds().get("background_brown"));
					break;
				case "Purple Background":
					mazeDisplayer.setBackgroundImage(mazeDisplayer.getBackgrounds().get("background_purple"));
					break;
				case "Sunset Background":
					mazeDisplayer.setBackgroundImage(mazeDisplayer.getBackgrounds().get("background_sunset"));
					break;
				case "Nature Background":
					mazeDisplayer.setBackgroundImage(mazeDisplayer.getBackgrounds().get("background_nature"));
					break;
				case "Color Background":
					mazeDisplayer.setBackgroundImage(mazeDisplayer.getBackgrounds().get("background_color"));
					break;
				case "Black Background":
					mazeDisplayer.setBackgroundImage(mazeDisplayer.getBackgrounds().get("background_black"));
					break;
				default:
					mazeDisplayer.setBackgroundImage(mazeDisplayer.getBackgrounds().get("game_background"));
					break;
					
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}
		});


		//----Add listener for character selection----//
		characterCombo.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				String ch = characterCombo.getText();
				switch (ch) {
				case "Marco 1":
					mazeDisplayer.setCharacter(mazeDisplayer.getCharacters().get("marco 1"));
					break;
				case "Marco 2":
					mazeDisplayer.setCharacter(mazeDisplayer.getCharacters().get("marco 2"));
					break;
				case "Sonic":
					mazeDisplayer.setCharacter(mazeDisplayer.getCharacters().get("sonic"));
					break;
				case "Mario":
					mazeDisplayer.setCharacter(mazeDisplayer.getCharacters().get("mario"));
					break;
				case "Son Goku 1":
					mazeDisplayer.setCharacter(mazeDisplayer.getCharacters().get("son goku 1"));
					break;
				case "Son Goku 2":
					mazeDisplayer.setCharacter(mazeDisplayer.getCharacters().get("son goku 2"));
					break;
				case "Hitman":
					mazeDisplayer.setCharacter(mazeDisplayer.getCharacters().get("hitman"));
					break;
				default:
					mazeDisplayer.setCharacter(mazeDisplayer.getCharacters().get("marco 1"));
					break;
				}
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {	
			}
		});

		//----Add listener for properties item selection----//
		propertiesItem.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {

				ClassGUIDialogLoader dialog = new ClassGUIDialogLoader(shell, SWT.DIALOG_TRIM | SWT.SYSTEM_MODAL,Properties.class, GameWindow.this.properties);

				dialog.setSubmitListener(new SelectionListener() {

					@Override
					public void widgetSelected(SelectionEvent arg0) {
						Properties newProperties = (Properties) dialog.getInstance();
						try{
							PropertiesHandler.writeProperties(newProperties, "resources/properties/properties.xml");
						}catch (Exception e) {
							msgError.showMessage(getDisplay(), "Error","Couldn't write new properties");
							//new MyMessageBox(getShell(), SWT.ERROR).showMessage(display, "Error", "Couldn't write new properties");
							return;
						}

						msgInfo.showMessage(getDisplay(), "Attention", "please restart the game for the applied changes to take effect");
						//new MyMessageBox(getShell(), SWT.ICON_INFORMATION).showMessage(getDisplay(), "attention", "please restart the game for the applied changes to take effect");
					}

					@Override
					public void widgetDefaultSelected(SelectionEvent arg0) {
						// TODO Auto-generated method stub

					}
				});
				dialog.showDialog();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub

			}
		});

		/*//----Add listener for load maze item----//
		loadMazeItem.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				FileDialog fd = new FileDialog(getShell(),SWT.OPEN);
				fd.setFilterPath(" ");
				fd.setText("Load Maze File");
				fd.setFilterExtensions("*.maz".split(" "));
				String selectedMaze = fd.open();
				if (selectedMaze != null) {
					String mazeName = selectedMaze.substring(selectedMaze.lastIndexOf("\\") + 1,
							selectedMaze.lastIndexOf("."));
					System.out.println(mazeName);
					String command = "load maze"+" "+ selectedMaze;
					setCommandAndNotify(cli.getCommandByInput(command), command.split(" "));

				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}
		});*/

		//Add listener for save maze item
		/*saveMazeItem.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				FileDialog fd = new FileDialog(getShell(),SWT.SAVE);
				fd.setFilterPath(" ");
				fd.setText("Save Maze File");
				fd.setFilterExtensions("*.maz *.xml *.java .*".split(" "));
				String selectedMaze = fd.open();
				if(selectedMaze != null){
					String command = "save maze " + selectedMaze;
					setCommandAndNotify(cli.getCommandByInput(command), command.split(" "));
				}

			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {	
			}
		});*/

	/*	//Add listener for instruction item
		instructionItem.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				StringBuilder sb = new StringBuilder();
				try {
					BufferedReader br = new BufferedReader(new FileReader("resources/properties/instruction.txt")) ;
					String line = br.readLine();
					while (line != null) {
						sb.append(line);
						sb.append(System.lineSeparator());
						line = br.readLine();
					}
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				new MyMessageBox(getShell(), SWT.OK).showMessage(getDisplay(), "Instrucation", sb.toString());

			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}
		});*/

		//Add listener for about item
	/*	aboutItem.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				StringBuilder sb = new StringBuilder();
				try {
					BufferedReader br = new BufferedReader(new FileReader("resources/properties/about.txt")) ;
					String line = br.readLine();
					while (line != null) {
						sb.append(line);
						sb.append(System.lineSeparator());
						line = br.readLine();
					}
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				new MyMessageBox(getShell(), SWT.OK).showMessage(getDisplay(), "About", sb.toString());
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {

			}
		});*/
		
		
	}
	
	/**
	 * <h1>Close Widget</h1><p>
	 * <i><ul>void closeWidget()<i><p>
	 * Dispose all the widget
	 */
	void closeWidget(){
	/*	System.out.println("Close all widget");
		if (playMusicButton!=null && !playMusicButton.isDisposed())
			playMusicButton.dispose();
		if (stopMusicButton!=null && !stopMusicButton.isDisposed())
			stopMusicButton.dispose();
		if (solveButton!=null && !solveButton.isDisposed())
			solveButton.dispose();
		if (hintButton!=null && !hintButton.isDisposed())
			hintButton.dispose();
		if (buttonLeft!=null && !buttonLeft.isDisposed())
			buttonLeft.dispose();
		if (buttonRight!=null && !buttonRight.isDisposed())
			buttonRight.dispose();
		if (buttonBackward!=null && !buttonBackward.isDisposed())
			buttonBackward.dispose();
		if (buttonForward!=null && !buttonForward.isDisposed())
			buttonForward.dispose();
		if (buttonUp!=null && !buttonUp.isDisposed())
			buttonUp.dispose();
		if (buttonDown!=null && !buttonDown.isDisposed())
			buttonDown.dispose();
		if (generateButton!=null && !generateButton.isDisposed())
			generateButton.dispose();
		if (generateItem!=null && !generateItem.isDisposed())
			generateItem.dispose();
		if (propertiesItem!=null && !propertiesItem.isDisposed())
			propertiesItem.dispose();
		if (loadMazeItem!=null && !loadMazeItem.isDisposed())
			loadMazeItem.dispose();
		if (saveMazeItem!=null && !saveMazeItem.isDisposed())
			saveMazeItem.dispose();
		if (exitItem!=null && !exitItem.isDisposed())
			exitItem.dispose();
		if (helpItem!=null && !helpItem.isDisposed())
			helpItem.dispose();
		if (instructionItem!=null && !instructionItem.isDisposed())
			instructionItem.dispose();
		if (aboutItem!=null && !aboutItem.isDisposed())
			aboutItem.dispose();
		if (dfsSolve!=null && !dfsSolve.isDisposed())
			dfsSolve.dispose();
		if (bfsSolve!=null && !bfsSolve.isDisposed())
			bfsSolve.dispose();
		if (aStarAdItem!=null && !aStarAdItem.isDisposed())
			aStarAdItem.dispose();
		if (aStarMdItem!=null && !aStarMdItem.isDisposed())
			aStarMdItem.dispose();
		if (subMenuItem!=null && !subMenuItem.isDisposed())
			subMenuItem.dispose();
		if (gameItem!=null && !gameItem.isDisposed())
			gameItem.dispose();
		if (stopMusicItem!=null && !stopMusicItem.isDisposed())
			stopMusicItem.dispose();
		if (playMusicItem!=null && !playMusicItem.isDisposed())
			playMusicItem.dispose();
		if (backGroundMusic!=null && !backGroundMusic.isOpen()){
			backGroundMusic.stop();
			backGroundMusic.close();
		}*/
		if( sound != null && sound.isOpen()){
			sound.stop();
			sound.close();
		}
		if (backgroundCombo!=null && !backgroundCombo.isDisposed()){
			backgroundCombo.dispose();
		}	
		
		if(keyListener!=null )
			getShell().removeKeyListener(keyListener);
		if(mouseWheelListener!=null )
			getShell().removeMouseWheelListener(mouseWheelListener);
		if(mazeDisplayer!=null && !mazeDisplayer.isDisposed())
			mazeDisplayer.dispose();
		
	}
	
	
	@Override
	public void writeToConsole(String msg) {
		Display.getDefault().syncExec(new Runnable() {
		    public void run() {
		    	consoleText.append(msg+"\n");
		    }
		});
		
	}
	
	@Override
	public void resetGameEvetHandler() {
		Display.getDefault().syncExec(new Runnable() {
			
			@Override
			public void run() {
				connectToServerButton.setText("Connect to server");
				getShell().removeKeyListener(keyListener);
				generateButton.setEnabled(false);
				generateItem.setEnabled(false);
				loadMazeItem.setEnabled(true);
				saveMazeItem.setEnabled(false);
				dfsSolve.setEnabled(false);
				bfsSolve.setEnabled(false);
				aStarAdItem.setEnabled(false);
				aStarMdItem.setEnabled(false);
				solveButton.setEnabled(false);
				hintButton.setEnabled(false);
				groupBackFor.setEnabled(false);
				groupLeftRight.setEnabled(false);
				groupUpDown.setEnabled(false);
				characterCombo.setEnabled(true);
				
				
			}
		});
		
		
	}
	
	
	@Override
	public void connectToServerButtonInit() {

		Display.getDefault().syncExec(new Runnable() {
			
			@Override
			public void run() {
				generateButton.setEnabled(true);
				generateItem.setEnabled(true);
				generateMazeTray.setEnabled(true);
				connectToServerButton.setText("Disconnect from server");
				
			}
		});
		

	}
	
	/**
	 * <h1>initButtons</h1><p>
	 * <i><ul>void initButtons()<i><p>
	 * Initialize the buttons an items
	 */
	public void initButtons(){
		
		connectToServerButton.setEnabled(true);
		characterCombo.setEnabled(false);
		hintButton.setEnabled(false);
		backgroundCombo.setEnabled(true);
		groupBackFor.setEnabled(false);
		groupLeftRight.setEnabled(false);
		groupUpDown.setEnabled(false);
		generateButton.setEnabled(false);
		generateMazeTray.setEnabled(false);
		generateItem.setEnabled(false);
		solveButton.setEnabled(false);
		solveMazeTray.setEnabled(false);
		dfsSolve.setEnabled(false);
		bfsSolve.setEnabled(false);
		aStarAdItem.setEnabled(false);
		aStarMdItem.setEnabled(false);
		instructionItem.setEnabled(true);
		aboutItem.setEnabled(true);
		propertiesItem.setEnabled(true);
		saveMazeItem.setEnabled(false);
		saveMazeTray.setEnabled(false);
		loadMazeItem.setEnabled(true);
		loadMazeTray.setEnabled(false);
		
		if(properties.getSound()){
			try {
				playBackgroundSoundInLoop(new File("resources/music/m1.wav"));
				
				playMusicButton.setEnabled(false);
				playMusicItem.setEnabled(false);
				stopMusicButton.setEnabled(true);
				stopMusicItem.setEnabled(true);
			} catch (Exception e) {} 	
		}
		else{
			playMusicButton.setEnabled(true);
			playMusicItem.setEnabled(true);
			stopMusicButton.setEnabled(false);
			stopMusicItem.setEnabled(false);
		}
	}
	
	/**
	 * <h1>solveMazeButtonInit</h1><p>
	 * <i><ul>void solveMazeButtonInit()<i><p>
	 * Reset the widget after solving the maze
	 */
	public void solveMazeButtonInit(){
		getShell().removeKeyListener(keyListener);
		getShell().removeMouseWheelListener(mouseWheelListener);
		generateButton.setEnabled(true);
		generateItem.setEnabled(true);
		generateMazeTray.setEnabled(true);
		loadMazeTray.setEnabled(true);
		loadMazeItem.setEnabled(true);
		saveMazeTray.setEnabled(true);
		saveMazeItem.setEnabled(true);
		solveButton.setEnabled(false);
		solveMazeTray.setEnabled(true);
		dfsSolve.setEnabled(true);
		bfsSolve.setEnabled(true);
		aStarAdItem.setEnabled(true);
		aStarMdItem.setEnabled(true);
		solveButton.setEnabled(true);
		hintButton.setEnabled(false);
		groupBackFor.setEnabled(false);
		groupLeftRight.setEnabled(false);
		groupUpDown.setEnabled(false);
		characterCombo.setEnabled(true);
	}
	
	
	/**
	 * <h1>sdisplayMazeButtoninit</h1><p>
	 * <i><ul>void displayMazeButtoninit()<i><p>
	 * Reset the widget after displaying the maze
	 */
	public void displayMazeButtoninit(){
		
		generateItem.setEnabled(false);
		generateButton.setEnabled(false);
		generateMazeTray.setEnabled(false);
		saveMazeItem.setEnabled(true);
		saveMazeTray.setEnabled(true);
		loadMazeItem.setEnabled(false);
		loadMazeTray.setEnabled(false);
		bfsSolve.setEnabled(true);
		dfsSolve.setEnabled(true);
		solveButton.setEnabled(true);
		solveMazeTray.setEnabled(true);
		groupBackFor.setEnabled(true);
		groupLeftRight.setEnabled(true);
		groupUpDown.setEnabled(true);
		hintButton.setEnabled(true);
		characterCombo.setEnabled(true);
		aStarAdItem.setEnabled(true);
		aStarMdItem.setEnabled(true);
	}


}
