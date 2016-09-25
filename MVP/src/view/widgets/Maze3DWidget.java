package view.widgets;



import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import algorithms.maze3DGenerators.Maze3D;
import algorithms.maze3DGenerators.Position;


public class Maze3DWidget extends MazeDisplayer{

	final int numOfColors = 50;
	private Position position;
	private Clip s;
	private Image winImage;
	private Image goalImage;
	private Image hintImage;
	private Maze3D maze;
	private Color black;//,black2, green, red;
	private ArrayList<Color> floorColors;
	
	Timer timer;
	TimerTask timerTask;

	public Maze3DWidget(Composite parent, int style) {
		super(parent, style);
		this.maze = null;
		black = new Color(getDisplay(), 64, 64, 64);
		initImages();

	
	}
	
	private void initImages(){
		character = new ImageGameCharacter(this, SWT.NONE, new Image(getDisplay(), "data/image/character_image.png"));
		winImage = new Image(getDisplay(), "data/image/win_image.jpg");
		goalImage = new Image(getDisplay(), "data/image/goal_image.png");
		
		try {
			s = AudioSystem.getClip();
			AudioInputStream inputStream;
			inputStream = AudioSystem.getAudioInputStream(new BufferedInputStream(
			new FileInputStream(new File("data/music/move.wav"))));
			s.open(inputStream);
		}
			catch (Exception e  ) {
				e.printStackTrace();
			}
		//s.start();
	}

	public Maze3DWidget(Composite parent, int style,Maze3D maze) {
		super(parent, style);
		this.maze = maze;
		setBackgroundImage(new Image(getDisplay(), "data/image/sonicbg.png"));
		character = new ImageGameCharacter(this, SWT.NONE, new Image(getDisplay(), "data/image/character_image.png"));
		winImage = new Image(getDisplay(), "data/image/win_image.jpg");
		goalImage = new Image(getDisplay(), "data/image/goal_image.png");
		//hintImage = new Image(getDisplay(), arg1)
		position = new Position();
		black = new Color(getDisplay(), 64, 64, 64);
		floorColors = initDistinctFloorColors(maze.getzAxis());
		addPaintListener(new PaintListener() {

			@Override
			public void paintControl(PaintEvent e) {
				if(maze != null){
					e.gc.setAntialias(SWT.ON);
					int width = getSize().x;
					int height = getSize().y;

					int mx = width / 2;

					double w = (double) width / getMazeData().length ;
					double h = (double) height / getMazeData().length ;

					for (int i = 0; i <getMazeData().length ; i++) {
						
						double w0 = 0.5 * w + 0.4 * w * i / getMazeData().length;
						double w1 = 0.5 * w + 0.4 * w * (i + 1) / getMazeData().length;
						double start = mx - w0 *getMazeData()[i].length / 2;
						double start1 = mx - w1 * getMazeData()[i].length / 2;
						
						for (int j = 0; j < getMazeData()[i].length ; j++) {
							double[] dpoints = { start + j * w0, i * h, start + j * w0 + w0, i * h, start1 + j * w1 + w1,
									i * h + h, start1 + j * w1, i * h + h };
							double cheight = h / 2;

							if (getMazeData()[i][j] != 0)
								paintCube(dpoints, cheight, e);

							if (i == character.getX() && j== character.getY() )
								character.paint(e, (int) Math.round(dpoints[0]), (int) Math.round(dpoints[1] - cheight / 2),
										(int) Math.round((w0 + w1) / 2), (int) Math.round(h));

							if( i == maze.getEnd().getX() && j== maze.getEnd().getY() && maze.getEnd().getZ() == position.getZ())
								e.gc.drawImage(goalImage, 0, 0, 400, 400, (int) Math.round(dpoints[0]), (int) Math.round(dpoints[1] - cheight / 2), (int) Math.round((w0 + w1) / 2), (int) Math.round(h));

							if( position.getZ() == getMaze().getEnd().getZ() )
								e.gc.drawImage(goalImage, 0, 0, 593, 727, (int) Math.round(dpoints[0]), (int) Math.round(dpoints[1] - cheight / 2), (int) Math.round((w0 + w1) / 2), (int) Math.round(h));
						}
					}
				}
				e.gc.setForeground(new Color(null, 255, 0, 0));
				e.gc.drawString("Position: " + position, 20, 20);
			}
		});

		addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent arg0) {
				if(black !=null && !black.isDisposed())
					black.dispose();
				if(character !=null && !character.isDisposed())
					character.dispose();
				if(s !=null && !s.isOpen())
					s.close();
			}
		});	
	}
	

	public void initMaze3DWidget(){
		//setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,3, 4));
		setBackgroundImage(new Image(getDisplay(), "data/image/sonicbg.png"));
		character = new ImageGameCharacter(this, SWT.NONE, new Image(getDisplay(), "data/image/character_image.png"));
		winImage = new Image(getDisplay(), "data/image/win.gif");
		goalImage = new Image(getDisplay(), "data/image/goal_image.png");
		position = new Position();
		setCharacterPosition(maze.getStart());
		setBackground(new Color(null, 255, 255, 255));
		black = new Color(getDisplay(), 64, 64, 64);	
	}
	
	public Image getWinImage() {
		return winImage;
	}
	
	public void setWinImage(Image winImage) {
		this.winImage = winImage;
	}

	public Maze3D getMaze() {
		return maze;
	}

	public void setMaze(Maze3D maze) {
		this.maze = maze;
		floorColors = initDistinctFloorColors(maze.getzAxis());
		
		addPaintListener(new PaintListener() {

			@Override
			public void paintControl(PaintEvent e) {
				if(maze != null){
					e.gc.setAntialias(SWT.ON);
					int width = getSize().x;
					int height = getSize().y;

					int mx = width / 2;

					double w = (double) width / getMazeData().length ;
					double h = (double) height / getMazeData().length ;

					
					
					for (int i = 0; i <getMazeData().length ; i++) {


						double w0 = 0.5 * w + 0.4 * w * i / getMazeData().length;
						double w1 = 0.5 * w + 0.4 * w * (i + 1) / getMazeData().length;
						double start = mx - w0 *getMazeData()[i].length / 2;
						double start1 = mx - w1 * getMazeData()[i].length / 2;
						
						for (int j = 0; j < getMazeData()[i].length ; j++) {
							double[] dpoints = { start + j * w0, i * h, start + j * w0 + w0, i * h, start1 + j * w1 + w1,
									i * h + h, start1 + j * w1, i * h + h };
							double cheight = h / 2;

							if (getMazeData()[i][j] != 0)
								paintCube(dpoints, cheight, e);

							if (i == character.getX() && j== character.getY() )
								character.paint(e, (int) Math.round(dpoints[0]), (int) Math.round(dpoints[1] - cheight / 2),
										(int) Math.round((w0 + w1) / 2), (int) Math.round(h));

							if( i == maze.getEnd().getX() && j== maze.getEnd().getY() && maze.getEnd().getZ() == position.getZ())
								e.gc.drawImage(goalImage, 0, 0, 254, 380, (int) Math.round(dpoints[0]), (int) Math.round(dpoints[1] - cheight / 2), (int) Math.round((w0 + w1) / 2), (int) Math.round(h));
				
						}
					}
				}
				e.gc.setForeground(new Color(null, 255, 0, 0));
				e.gc.drawString("Position: " + position, 20, 20);

			}
		});

		addDisposeListener(new DisposeListener() {

			@Override
			public void widgetDisposed(DisposeEvent arg0) {
				black.dispose();
				character.dispose();
			}
		});
	}
	

	@Override
	public void setCharacterPosition(Position p) {

		character.setZ(p.getZ());
		character.setX(p.getX());
		character.setY(p.getY());
		position = new Position(p);
		setMazeData(getMaze().getCrossSectionByZ(position.getZ()));
		System.out.println(p);
		redraw();
		//moveCharacter(p);
	}

	@Override
	public boolean moveCharacter(Position p) {
		// if at end position
		if (getMaze().getEnd().equals(p)) {
			redraw();
			triggerWin();
			//dispose();
		}

		else if ( getMaze().checkPositionBoundsNoException(p) && getMaze().getValueByIndex(p) ==0){
			setCharacterPosition(p);
			setNumofSteps(getNumofSteps()+1);
		
			if(!s.isActive()){
				s.stop();
				s.start();
			}
			return true;
		}
		return false;

	
	}

	@Override
	public boolean moveUp() {
		Position newPos = new Position(position);
		newPos.setZ(newPos.getZ() + 2);

		return moveCharacter(newPos);
	}

	@Override
	public boolean moveDown() {
		Position newPos = new Position(position);
		newPos.setZ(newPos.getZ() - 2);

		return moveCharacter(newPos);
	}

	@Override
	public boolean moveLeft() {
		Position newPos = new Position(position);
		newPos.setY(newPos.getY() - 1);

		return moveCharacter(newPos);
	}

	@Override
	public boolean moveRight() {
		Position newPos = new Position(position);
		newPos.setY(newPos.getY() + 1);

		return moveCharacter(newPos);
	}

	@Override
	public boolean moveFront() {
		Position newPos = new Position(position);
		newPos.setX(newPos.getX() + 1);

		return moveCharacter(newPos);
	}

	@Override
	public boolean moveBack() {
		Position newPos = new Position(position);
		newPos.setX(newPos.getX() - 1);

		return moveCharacter(newPos);
	}

	private ArrayList<Color> initDistinctFloorColors(int numOfFloors) {
		ArrayList<Color> colors = new ArrayList<Color>();

		Random rnd = new Random();
		for (int i = 0; i < numOfFloors; i++)
			colors.add(new Color(getDisplay(), rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256)));

		return colors;
	}
	
	@Override
	public void showSoution(ArrayList<Position> sol) {
		
		getDisplay().syncExec(new Runnable() {
			
			@Override
			public void run() {
				timer = new Timer();
				
				Iterator<Position> iter = sol.iterator();
				
				timer.scheduleAtFixedRate(new TimerTask() {
					
					@Override
					public void run() {
						if(iter.hasNext()){
							getDisplay().syncExec(new Runnable() {
								
								@Override
								public void run() {
									
									moveCharacter(iter.next());
									
								}
							});
						}else{
							
							if(timer!=null)
								timer.cancel();
						}
					}
				}, 0, 250);
			}
		});
	}


	public void paintCube(double[] p, double h, PaintEvent e) {
		int[] f = new int[p.length];
		for (int k = 0; k < f.length; f[k] = (int) Math.round(p[k]), k++);

		int[] r = f.clone();
		for (int k = 1; k < r.length; r[k] = f[k] - (int) (h), k += 2);

		int[] fr = { r[6], r[7], r[4], r[5], f[4], f[5], f[6], f[7] };
		int[] right = { r[2], r[3], f[2], f[3], f[4], f[5], r[4], r[5] };
		int[] left = { r[0], r[1], f[0], f[1], f[6], f[7], r[6], r[7] };
		e.gc.setLineWidth(SWT.NONE);

			
		e.gc.setBackground(floorColors.get(position.getZ()));
		e.gc.drawPolygon(right);
		e.gc.drawPolygon(left);
		e.gc.drawPolygon(fr);
	
		e.gc.setBackground(black);
		e.gc.drawPolygon(right);
		e.gc.fillPolygon(right);
		e.gc.drawPolygon(fr);
		e.gc.fillPolygon(fr);
		e.gc.setBackground(floorColors.get(position.getZ()));
		e.gc.fillPolygon(r);
		e.gc.setBackground(floorColors.get(position.getZ()));
		e.gc.drawPolygon(r);

	}




}
