package view.gui.widgets;

import java.util.ArrayList;
import java.util.HashMap;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import position.position3d.*;
import view.gui.MazeWinEventHandler;


/**
 * <h1>MazeDisplayer</h1><p>
 * This class extends Canvas and having parameters which define a maze game
 */
public abstract class MazeDisplayer extends Canvas implements GameBoardDisplayAdapter {

	
	//------------------------------Data Members-------------------------//
	
	protected MazeWinEventHandler winEvent;
	protected Position3D position;
	protected Image winImage;
	protected Image goalImage;
	protected HashMap<String, Image> backgrounds;
	protected HashMap<String, ImageGameCharacter> characters;
	protected ImageGameCharacter character;
	protected int numofSteps;
	
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
	
	
	/**
	 * <h1>MazeDisplayer</h1><p>
	 * <i>MazeDisplayer(Composite parent, int style)<i><p>
	 * Instantiates a new MazeDisplayer.
	 * @param parent - Composite
	 * @param style - style
	 */
	public MazeDisplayer(Composite parent, int style) {
		super(parent, style);
		setNumofSteps(0);
		character = new ImageGameCharacter(this, SWT.NONE, new Image(getDisplay(), "resources/image/characters/character_image1.png"));
		initImages();
		initCharacters();
		initBackgrounds();
		setBackgroundImage(new Image(getDisplay(), "resources/image/backgrounds/bg_image.png"));
		
		addPaintListener(new PaintListener() {

			@Override
			public void paintControl(PaintEvent e) {
				e.gc.setAntialias(SWT.ON);
				int width = getSize().x;
				int height = getSize().y;
				
				e.gc.drawImage(getBackgroundImage(), 0, 0,getBackgroundImage().getBounds().width,getBackgroundImage().getBounds().height,0,0,width,height);
			}
		});
		redraw();
	};

	//------------------------------Setters and Getters-------------------------//
	
	/**
	 * <h1>getBackgrounds</h1><p>
	 * <i>HashMap<String, Image> getBackgrounds()<i><p>
	 * Gets the HashMap of the background images
	 * @return  background - HashMap of background
	 */
	public HashMap<String, Image> getBackgrounds() {
		return backgrounds;
	}

	/**
	 * <h1>setBackgrounds</h1><p>
	 * <i>setBackgrounds(HashMap<String, Image> backGrounds)<i><p>
	 * Gets the HashMap of the background images.
	 * @param backGrounds - HashMap of background images
	 */
	public void setBackgrounds(HashMap<String, Image> backGrounds) {
		this.backgrounds = backGrounds;
	}
	
	/**
	 * <h1>getNumofSteps</h1><p>
	 * <i>int getNumofSteps()<i><p>
	 * Gets number of steps that made
	 * @return int - number of steps that made
	 */
	public int getNumofSteps() {
		return numofSteps;
	}

	/**
	 * <h1>setNumofSteps</h1><p>
	 * <i>void setNumofSteps(int numofSteps)<i><p>
	 * Gets number of steps that made.
	 * @param numofSteps - new number of steps
	 */
	public void setNumofSteps(int numofSteps) {
		this.numofSteps = numofSteps;
	}
	
	/**
	 * <h1>getWinEvent</h1><p>
	 * <i>MazeWinEventHandler getWinEvent()<i><p>
	 * Gets the win event handler.
	 * @return MazeWinEventHandler
	 */
	public MazeWinEventHandler getWinEvent() {
		return winEvent;
	}

	/**
	 * <h1>setWinEvent</h1><p>
	 * <i>void setWinEvent(MazeWinEventHandler winEvent)<i><p>
	 * Sets the win event handler
	 * @param winEvent - MazeWinEventHandler
	 */
	public void setWinEvent(MazeWinEventHandler winEvent) {
		this.winEvent = winEvent;
	}
	
	/**
	 * <h1>getMazeData</h1><p>
	 * <i>int[][] getMazeData()<i><p>
	 * Gets the maze data
	 * @return int[][] data
	 */
	public int[][] getMazeData(){
		return mazeData;
	}

	/**
	 * <h1>setMazeData</h1><p>
	 * <i>void setMazeData(int[][] mazeData)<i><p>
	 * * Sets the maze data
	 * @param mazeData - int[][] data
	 */
	public void setMazeData(int[][] mazeData) {
		this.mazeData = mazeData;
	}
	
	/**
	 * <h1>getPosition</h1><p>
	 * <i>Position3D getPosition()<i><p>
	 * Gets current position
	 * @return position3D
	 */
	public Position3D getPosition() {
		return position;
	}

	/**
	 * <h1>setPosition</h1><p>
	 * <i>void setPosition(Position3D position)<i><p>
	 * Sets the current position
	 * @param position - position3D 
	 */
	public void setPosition(Position3D position) {
		this.position = position;
	}

	/**
	 * <h1>getWinImage</h1><p>
	 * <i>Image getWinImage()<i><p>
	 * Gets the winImage handler
	 * @return Image - the win image
	 */
	public Image getWinImage() {
		return winImage;
	}

	/**
	 * <h1>setWinImage</h1><p>
	 * <i>void setWinImage(Image winImage)<i><p>
	 * Sets the winImage handler
	 * @param winImage - Image 
	 */
	public void setWinImage(Image winImage) {
		this.winImage = winImage;
	}

	/**
	 * <h1>getGoalImage()</h1><p>
	 * <i>Image getGoalImage()<i><p>
	 * Gets the goalImage
	 * @return Image - the goal image
	 */
	public Image getGoalImage() {
		return goalImage;
	}

	/**
	 * <h1>setGoalImage</h1><p>
	 * <i>void setGoalImage(Image goalImage)<i><p>
	 * Sets the goalImage
	 * @param goalImage - Image
	 */
	public void setGoalImage(Image goalImage) {
		this.goalImage = goalImage;
	}


	/**
	 * <h1>getCharacter</h1><p>
	 * <i>ImageGameCharacter getCharacter()<i><p>
	 * Gets the ImageGameCharacter
	 * @return character - ImageGameCharacter
	 */
	public ImageGameCharacter getCharacter() {
		return character;
	}

	/**
	 * <h1>setCharacter</h1><p>
	 * <i>void setCharacter(ImageGameCharacter character)<i><p>
	 * Sets the ImageGameCharacter.
	 * @param character - ImageGameCharacter
	 */
	public void setCharacter(ImageGameCharacter character) {
		this.character = character;
		setCharacterPosition(getPosition());
		redraw();
	}


	/**
	 * <h1>getCharacters()</h1><p>
	 * <i>HashMap<String, ImageGameCharacter> getCharacters()<i><p>
	 * Gets the HashMap that hold all the characters images
	 * @return  HashMaps of characters images
	 */
	public HashMap<String, ImageGameCharacter> getCharacters() {
		return characters;
	}

	/**
	 * <h1>setCharacters</h1><p>
	 * <i>void setCharacters(HashMap<String, ImageGameCharacter> characters)<i><p>
	 * Sets the HashMap that hold all the characters images.
	 * @param characters - HashMap of characters images
	 */
	public void setCharacters(HashMap<String, ImageGameCharacter> characters) {
		this.characters = characters;
	}

	
	
	//-------------------------Functionality-------------------------//
	
	/**
	 * <h1>triggerWin</h1><p>
	 * <i>void triggerWin()<i><p>
	 * This method activate the win event handler {@link MazeWinEventHandler#winGame()}
	 */
	public void triggerWin() {
		if (winEvent != null)
			winEvent.winGame();
		else
			// if there is no win handler - use default syso
			System.out.println("Win!");
	}
	
	/**
	 * <h1>initImages()</h1><p>
	 * <i>void initImages()<i><p>
	 * Initialize the win image and goal image
	 */
	public void initImages(){
		winImage = new Image(getDisplay(), "resources/image/wins/win_image.jpg");
		goalImage = new Image(getDisplay(), "resources/image/characters/goal_image.png");
	}
	
	/**
	 * <h1>initCharacters</h1><p>
	 * <i>void initCharacters()<i><p>
	 * Initialize the hashMap of the character images
	 */
	public void initCharacters(){
		
		characters = new HashMap<String, ImageGameCharacter>();
		characters.put("marco 1", new ImageGameCharacter(this, SWT.NONE, new Image(getDisplay(), "resources/image/characters/character_image1.png")));
		characters.put("marco 2", new ImageGameCharacter(this, SWT.NONE, new Image(getDisplay(), "resources/image/characters/character_image2.png")));
		characters.put("sonic", new ImageGameCharacter(this, SWT.NONE, new Image(getDisplay(), "resources/image/characters/character_image3.png")));
		characters.put("mario", new ImageGameCharacter(this, SWT.NONE, new Image(getDisplay(), "resources/image/characters/character_image4.png")));
		characters.put("son goku 1", new ImageGameCharacter(this, SWT.NONE, new Image(getDisplay(), "resources/image/characters/character_image5.png")));
		characters.put("son goku 2", new ImageGameCharacter(this, SWT.NONE, new Image(getDisplay(), "resources/image/characters/character_image6.png")));
		characters.put("hitman", new ImageGameCharacter(this, SWT.NONE, new Image(getDisplay(), "resources/image/characters/character_image7.png")));
		characters.put("spiderman",new ImageGameCharacter(this, SWT.NONE, new Image(getDisplay(), "resources/image/characters/character_image9.png")));
	}

	/**
	 * <h1>initBackgrounds()</h1><p>
	 * <i>void initBackgrounds()<i><p>
	 * Initialize the hashMap of the background images
	 */
	public void initBackgrounds(){
		backgrounds = new HashMap<String, Image>();
		backgrounds.put("game_background",new Image(getDisplay(), "resources/image/backgrounds/bg_image.png"));
		backgrounds.put("background_red", new Image(getDisplay(), "resources/image/backgrounds/background_red.png"));
		backgrounds.put("background_green", new Image(getDisplay(), "resources/image/backgrounds/background_green.png"));
		backgrounds.put("background_blue", new Image(getDisplay(), "resources/image/backgrounds/background_blue.png"));
		backgrounds.put("background_yellow", new Image(getDisplay(), "resources/image/backgrounds/background_yellow.png"));
		backgrounds.put("background_white", new Image(getDisplay(), "resources/image/backgrounds/background_white.png"));
		backgrounds.put("background_brown", new Image(getDisplay(), "resources/image/backgrounds/background_brown.png"));
		backgrounds.put("background_purple", new Image(getDisplay(), "resources/image/backgrounds/background_purple.png"));
		backgrounds.put("background_sunset", new Image(getDisplay(), "resources/image/backgrounds/background_sunset.png"));
		backgrounds.put("background_nature", new Image(getDisplay(), "resources/image/backgrounds/background_nature.png"));
		backgrounds.put("background_color", new Image(getDisplay(), "resources/image/backgrounds/background_color.png"));
		backgrounds.put("background_black", new Image(getDisplay(), "resources/image/backgrounds/background_black.png"));
	}
	
	
	/**
	 * <h1>moveUp()</h1><p>
	 * <i>void moveUp()<i><p>
	 * Move the character one floor up
	 */
	public abstract void moveUp();

	/**
	 * <h1>moveDown</h1><p>
	 * <i>void moveDown()<i><p>
	 * Move the character one floor down
	 */
	public abstract void moveDown();

	/**
	 * <h1>moveLeft</h1><p>
	 * <i>void moveLeft()<i><p>
	 * Move the character one step left
	 */
	public abstract void moveLeft();

	/**
	 * <h1>moveRight</h1><p>
	 * <i>void moveRight()<i><p>
	 * Move the character one step right
	 */
	public abstract void moveRight();

	/**
	 * <h1>moveFront</h1><p>
	 * <i>void moveFront()<i><p>
	 * Move the character one step front
	 */
	public abstract void moveFront();

	/**
	 * <h1>moveBack</h1><p>
	 * <i>void moveBack()<i><p>
	 * Move the character one step back
	 */
	public abstract void moveBack();
	
	/**
	 * <h1>setCharacterPosition</h1><p>
	 * <i>void setCharacterPosition(Position3D p)<i><p>
	 * Set the character position in the given position
	 */
	public abstract void setCharacterPosition(Position3D p);
	
	/**
	 * <h1>moveCharacter</h1><p>
	 * <i>void moveCharacter(Position3D p)<i><p>
	 * Move the character to the given position
	 */
	public abstract void moveCharacter(Position3D p);
	
	/**
	 * <h1>showSoution</h1><p>
	 * <i>void showSoution(ArrayList<Position3D> sol)<i><p>
	 * Show the solution of the maze.
	 * @param sol - the solution
	 */
	public abstract void showSoution(ArrayList<Position3D> sol);
	

		
	

	
	
}
