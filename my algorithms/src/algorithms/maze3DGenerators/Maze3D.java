package algorithms.maze3DGenerators;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;



/**
 * The Class Maze3d defines the representation of three-dimensional maze
 * Hold zAxis, xAxis, yAxis dimensions</br>
 * zAxis - represent the floors</br>
 * xAxis - represent the rows</br>
 * yAxis - represent the columns</br>
 * The 3D maze represent by three dimensional array of integer: int[zAxis][xAxis][yAxis]
 * 
 * @author Asi Belachow
 * @version 1.0
 * @since 2016-30-07
 */
public class Maze3D {
	
	
	/* The Constant WALL - define a wall in the maze*/
	public static final int WALL = 1;
	
	/* The Constant PASS - define a pass in the maze */
	public static final int PASS = 0;
	
	/*The Constant CASING - define the casing of the maze*/
	public static final int CASING = 2;
	

	/*The z axis - represent the floors*/
	private int zAxis;   
	
	/*The x axis - represent the rows*/
	private int xAxis;
	
	/*The y axis - represent the columns*/
	private int yAxis;
	
	/*Three dimensional array of integer - represent the 3d maze*/
	private int [][][] array;
	
	/*The maze entrance {@link Position} */
	private Position start;
	
	/*The maze exit. {@link Position} */
	private Position end;
	
	
	Random r;
	
	
	
	//------------------------------Constructors-------------------------//
	
	/**
	 *<h1>Maze3D</h1><p>
	 * <i><ul>Maze3D(int z , int x , int y)<i><p>
	 * Initialize a new 3D maze
	 *
	 * @param z - the zAxis damnation
	 * @param x - the xAxis damnation
	 * @param y - the yAxis damnation
	 */
	public Maze3D(int z , int x , int y) {
		setzAxis(z*2+1);
		setxAxis(x*2+1);
		setyAxis(y*2+1);
		array = new int[getzAxis()][getxAxis()][getyAxis()];
		start = null;
		end = null;
	}
	
	/**
	 *<h1>Maze3D</h1><p>
	 * <i> <ul>Maze3D(Maze3D maze)<i><p>
	 * Copy Constructor of 3d maze
	 */
	public Maze3D(Maze3D maze){
		setzAxis(maze.getzAxis());
		setxAxis(maze.getxAxis());
		setyAxis(maze.getyAxis());
		setStart(maze.getStart());
		setEnd(maze.getEnd());
		setArray(maze.getArray());
	
	}
	
	
	public Maze3D(byte[] array) throws IOException {
		
		ByteArrayInputStream bIn= new ByteArrayInputStream(array);
		DataInputStream data = new DataInputStream(bIn);
		this.setzAxis(data.readInt());
		this.setxAxis(data.readInt());
		this.setyAxis(data.readInt());
		this.setStart(new Position(data.readInt(), data.readInt(), data.readInt()));
		this.setEnd(new Position(data.readInt(), data.readInt(), data.readInt()));
		System.out.println(getzAxis());
		System.out.println(getStart());
		System.out.println(getEnd());
		this.array = new int[getzAxis()][getxAxis()][getyAxis()];
		
		for (int i=0;i<getzAxis();i++)
			for(int j=0;j<getxAxis();j++)
				for(int k=0;k<getyAxis();k++)
					this.array[i][j][k]=data.readByte();
	}
	
	
	//-----------------------------setters and getters-------------------//
	
	public int getzAxis() {
		return zAxis;
	}

	public void setzAxis(int zAxis) {
		this.zAxis = zAxis;
	}

	public int getxAxis() {
		return xAxis;
	}

	public void setxAxis(int xAxis) {
		this.xAxis = xAxis;
	}

	public int getyAxis() {
		return yAxis;
	}

	public void setyAxis(int yAxis) {
		this.yAxis = yAxis;
	}

	public int[][][] getArray() {
		return array;
	}

	public void setArray(int[][][] array) {
		int z = array.length;
		int x = array[0].length;
		int y = array[0][0].length;
		this.array = new int[z][x][y];
		for (int i=0;i<z;i++)
			for(int j=0;j<x;j++)
				for(int k=0;k<y;k++)
					this.array[i][j][k]=array[i][j][k];
	}

	public Position getStart() {
		return start;
	}

	public void setStart(Position start) {
		this.start = start;
	}
	
	public Position getEnd() {
		return end;
	}

	public void setEnd(Position end) {
		this.end = end;
	}
	
	
	//-------------------------Functionality-------------------------//

	
	
	/**
	 *<h1>To Byte Array</h1><p>
	 * <i><ul>byte[] toByteArray()<i><p>
	 *
	 *This method convert the Maze3D to a ByteArray
	 *@see algorithms.maze3DGenerators.Maze3D @link #Maze3D(Maze3D)
	 * @return ByteArray - the converted Maze3D
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public byte[] toByteArray() throws IOException{
		
		ByteArrayOutputStream array = new ByteArrayOutputStream();
		DataOutputStream data = new DataOutputStream(array);
		
		data.writeInt(getzAxis());
		data.writeInt(getxAxis());
		data.writeInt(getyAxis());
		data.writeInt(getStart().getZ());
		data.writeInt(getStart().getX());
		data.writeInt(getStart().getY());
		data.writeInt(getEnd().getZ());
		data.writeInt(getEnd().getX());
		data.writeInt(getEnd().getY());
		
		for (int i = 0; i < this.getzAxis(); i++) 
			for (int j = 0; j < this.getxAxis(); j++) 
				for (int k = 0; k < this.getyAxis(); k++) 
					data.writeByte(this.array[i][j][k]);

		
		return array.toByteArray();
	}
	
	
	
	
	
	/**
	 *<h1>Get value by index</h1><p>
	 * <i> <ul>getValueByIndex(int z, int x, int y)<i><p>
	 * Gets the value by given index.
	 * @param z - the zAxis damnation
	 * @param x - the xAxis damnation
	 * @param y - the yAxis damnation
	 * @return the value by index
	 * @throws IndexOutOfBoundsException the index out of bounds exception
	 */
	public int getValueByIndex(int z, int x, int y) throws IndexOutOfBoundsException{
		if (checkPositionBounds(z, x, y))
			return array[z][x][y];
		else
			throw new IndexOutOfBoundsException("{"+z+","+x+","+y+"} out of bound" );
	}
	
	
	/**
	 * <h1>Get value by position</h1><p>
	 * <i> <ul>getValueByIndex(Position p)<i><p>
	 * Gets the value by given position.
	 *
	 * @param position p
	 * @return the value by index
	 * @throws IndexOutOfBoundsException the index out of bounds exception
	 */
	public int getValueByIndex(Position p) throws IndexOutOfBoundsException{
		if (checkPositionBounds(p))
			return array[p.getZ()][p.getX()][p.getY()];
		else
			throw new IndexOutOfBoundsException(" Position out of bound "+p.toString());
	}
	
	
	
	/**
	 * <h1>Set wall</h1><p>
	 * <i> <ul>setWall(int z, int x ,int y)<i><p>
	 * Set a wall in the maze in given index
	 * @param z - the zAxis damnation
	 * @param x - the xAxis damnation
	 * @param y - the yAxis damnation
	 * @throws IndexOutOfBoundsException the index out of bounds exception
	 */
	public void setWall(int z, int x ,int y)throws IndexOutOfBoundsException{ 
		if(checkPositionBounds(z, x, y))
			array[z][x][y] = WALL;
		else
			throw new IndexOutOfBoundsException(" Position out of bound "+new Position(z, x, y));
			
	}
	
	
	/**
	 * <h1>Set pass</h1><p>
	 * <i> <ul>setPass(int z, int x ,int y)<i><p>
	 * Set a passage in the maze in given index
	 * @param z - the zAxis damnation
	 * @param x - the xAxis damnation
	 * @param y - the yAxis damnation
	 * @throws IndexOutOfBoundsException the index out of bounds exception
	 */
	public void setPass(int z, int x ,int y)throws IndexOutOfBoundsException{
		if(checkPositionBounds(z, x, y))
			array[z][x][y] = PASS;
		else
			throw new IndexOutOfBoundsException(" Position out of bound "+new Position(z, x, y));
	}
	
	@Override
	public boolean equals(Object other) {
		Maze3D m = (Maze3D) other;
		if (getzAxis() == m.getzAxis() && getxAxis() == m.getxAxis()
				&& getyAxis() == m.getyAxis()) {
			if (start.equals(m.getStart())
					&& end.equals(m.getEnd())) {
				for (int i = 0; i < this.array.length; i++) {
					for (int j = 0; j < this.array[0].length; j++) {
						for (int j2 = 0; j2 < this.array[0][0].length; j2++) {
							if (this.array[i][j][j2] != m.getValueByIndex(i, j, j2)) {
								return false;
							}
						}
					}
				}
			} else {
				return false;
			}
		} else {
			return false;
		}
		return true;
	}
	


	/**
	 * <h1>Set pass</h1><p>
	 * <i> <ul>setPass(Position p)<i><p>
	 * Set a passage in the maze in given position
	 * @param Positon  - the index
	 * @throws IndexOutOfBoundsException the index out of bounds exception
	 */
	public void setPass(Position p)throws IndexOutOfBoundsException{
		if(checkPositionBounds(p))
			array[p.getZ()][p.getX()][p.getY()] = PASS;
		else
			throw new IndexOutOfBoundsException(" Position out of bound "+p);
			
	}
	
	
	/**
	 * <h1>Set casing</h1><p>
	 * <i><ul>setCasing(int z, int x ,int y)<i><p>
	 * Set a casing in the maze in given index
	 * @param z - the zAxis damnation
	 * @param x - the xAxis damnation
	 * @param y - the yAxis damnation
	 * @throws IndexOutOfBoundsException the index out of bounds exception
	 */
	public void setCasing(int z, int x ,int y)throws IndexOutOfBoundsException{
		if(checkPositionBounds(z, x, y))
			array[z][x][y] = CASING;
		else
			throw new IndexOutOfBoundsException(" Position out of bound "+new Position(z,x,y));
			
	}
	
	
	@Override
	public String toString() {
		StringBuilder  printMaze= new StringBuilder ();
		
		for(int z=0; z < zAxis; z++){
			for(int x=0; x < xAxis; x++){
				for(int y=0; y < yAxis; y++){
					printMaze.append(array[z][x][y]+ "   ");
				}
				printMaze.append("\n");
			}
			printMaze.append("\n");
		}
		return printMaze.toString();
		
		
	}
	
	
	/**
	 * <h1>Set value by index</h1><p>
	 * <i><ul>setCasing(int z, int x ,int y)<i><p>
	 * Sets the value by index.
	 *
	 * @param z - the zAxis damnation
	 * @param x - the xAxis damnation
	 * @param y - the yAxis damnation
	 * @param value 
	 * @throws IndexOutOfBoundsException the index out of bounds exception
	 */
	public void setValueByIndex(int z,int x,int y, int value)  throws IndexOutOfBoundsException{
		if(checkPositionBounds(z, x, y))
			array[z][x][y] = value;
		else 
			throw new IndexOutOfBoundsException(" Position out of bound "+new Position(z,x,y));
	}
	
	
	/**
	 * <h1>Set value by position</h1><p>
	 * <i><ul>setValueByIndex(Position p,int  value)<i><p>
	 * Sets the value by index.
	 * @param Position
	 * @param int value 
	 * @throws IndexOutOfBoundsException the index out of bounds exception
	 */
	public void setValueByIndex(Position p,int  value)throws IndexOutOfBoundsException{
		if(checkPositionBounds(p)) 
			array[p.getZ()][p.getX()][p.getY()] = value;
		else 
			throw new IndexOutOfBoundsException(" Position out of bound "+p);
		
	}
	
	
	/**
	 * <h1>Get all possible moves</h1><p>
	 * <i><ul>getPossibleMoves(Position pos)<i><p>
	 * Gets the possible moves of given position
	 *
	 * @param Position pos
	 * @return String[] all moves
	 */
	public String [] getPossibleMovesAsString(Position p){
		
		ArrayList<Position> pMoves = getPossibleMoves(p);
		String[] possibleMovesArray = new String[pMoves.size()];
		// copy from list to array
		for (int i = 0; i < possibleMovesArray.length; i++) {
			possibleMovesArray[i] = pMoves.get(i).toString();
		}
		
		return possibleMovesArray;	
	}
	
/*	public ArrayList<Position> getPossibleMoves(Position p){
		
		ArrayList<Position> pMoves = getAllMoves(p);
		Iterator<Position> iter = pMoves.iterator();
		while ( iter.hasNext()){
			Position temp = iter.next();
			if( !(checkPositionBoundsNoException(temp))){
				iter.remove();
				continue;
			}
			
			if(temp.equals(obj))
			
			
			
			
			if(getValueByIndex(temp) != 0)
				iter.remove();
		}
		return pMoves;
	}*/
	
	/**
	 * <h1>Get all possible moves</h1><p>
	 * <i><ul>ArrayList<Position> getPossibleMoves(Position pos)<i><p>
	 * Get all optional moves of given position by
	 * using {@link Position#MergePos(Position, Position)} method
	 * @param Position - pos 
	 * @return ArrayList<Position> - list of all optional moves
	 */
	public ArrayList<Position> getPossibleMoves(Position pos){
		ArrayList<Position> pMoves = new ArrayList<Position>();		
		Position temp = Position.MergePos(pos, Position.UP);
		if(checkPositionBoundsNoException(temp)){
			if( getValueByIndex(temp.getZ()-1, temp.getX(), temp.getY())==0 && getValueByIndex(temp)==0){
				pMoves.add(temp);
			}
		}
		temp = Position.MergePos(pos, Position.DOWN);
		if(checkPositionBoundsNoException(temp)){
			if( getValueByIndex(temp.getZ()+1, temp.getX(), temp.getY())==0 && getValueByIndex(temp)==0){
				pMoves.add(temp);
			}
		}
		temp = Position.MergePos(pos, Position.RIGHT);
		if(checkPositionBoundsNoException(temp)){
			if( getValueByIndex(temp.getZ(), temp.getX(), temp.getY()-1)==0 && getValueByIndex(temp)==0){
				pMoves.add(temp);
			}
		}
		temp = Position.MergePos(pos, Position.LEFT);
		if(checkPositionBoundsNoException(temp)){
			if( getValueByIndex(temp.getZ(), temp.getX(), temp.getY()+1)==0 && getValueByIndex(temp)==0){
				pMoves.add(temp);
			}
		}
		temp = Position.MergePos(pos, Position.BACKWARD);
		if(checkPositionBoundsNoException(temp)){
			if( getValueByIndex(temp.getZ(), temp.getX()-1, temp.getY())==0 && getValueByIndex(temp)==0){
				pMoves.add(temp);
			}
		}
		temp = Position.MergePos(pos, Position.FORWARD);
		if(checkPositionBoundsNoException(temp)){
			if( getValueByIndex(temp.getZ(), temp.getX()+1, temp.getY())==0 && getValueByIndex(temp)==0){
				pMoves.add(temp);
			}
		}

		return pMoves;
	}
	
	
	
	
	/**
	 * <h1>Get all moves</h1><p>
	 * <i><ul>ArrayList<Position> getAllMoves(Position pos)<i><p>
	 *	Calculate all the moves of given position
	 *	using {@link Position#MergePos(Position, Position)} method
	 * @param pos the pos
	 * @return ArrayList - All moves
	 */
	public ArrayList<Position> getAllMoves(Position pos){
		ArrayList<Position> aMoves = new ArrayList<Position>();
		
		aMoves.add(Position.MergePos(pos, Position.UP));
		aMoves.add(Position.MergePos(pos, Position.DOWN));
		aMoves.add(Position.MergePos(pos, Position.RIGHT));
		aMoves.add(Position.MergePos(pos, Position.LEFT));
		aMoves.add(Position.MergePos(pos, Position.BACKWARD));
		aMoves.add(Position.MergePos(pos, Position.FORWARD));
		return aMoves;
		
	}


	/**
	 * <h1>Check position bounds</h1><p>
	 * <i><ul>checkPositionBounds(int z, int x, int y)<i><p>
	 * Check if the given index valid
	 *
	 * @param z - the zAxis damnation
	 * @param x - the xAxis damnation
	 * @param y - the yAxis damnation
	 * @return true, if valid, else  false
	 */
	public boolean checkPositionBounds(int z, int x, int y) {
		return ((z >= 0 && z < getzAxis()) && (x >= 0 && x < getxAxis()) && (y >= 0 && y < getyAxis()));
	}
	
	/**
	 * <h1>Check position bounds</h1><p>
	 * <i><ul>checkPositionBounds(Position p)<i><p>
	 * Check if the given �������� valid
	 *
	 * @param Position p
	 * @return true, if valid, else  false
	 */
	public boolean checkPositionBounds(Position p) {
		return checkPositionBounds(p.getZ(), p.getX(), p.getY());
	}
	
	public boolean checkPositionBoundsNoException(Position p) {
		return ((p.getZ()>=0 && p.getZ()<getzAxis())&&(p.getX() >= 0 &&p.getX()<getxAxis())&&(p.getY()>=0&&p.getY()<getyAxis()));

	}

	
	
	/**
	 * <h1>Gets the cross section by Y.</h1><p>
	 * <i><ul>getCrossSectionByY(int y)<i><p>
	 *
	 * @param y - the yAxis damnation
	 * @return imt[][] the cross section by Y
	 * @throws IndexOutOfBoundsException the section is out of bounds exception
	 */
	public int[][] getCrossSectionByY(int y) throws IndexOutOfBoundsException{
		if(y>=0 && y < getyAxis()){
			int [][] crossByY = new int[getzAxis()][getyAxis()];
			for ( int i=0; i<getzAxis(); i++){
				for( int j=0; j<getyAxis(); j++){
					crossByY[i][j]=array[i][j][y];
				}
			}
			return crossByY;
		}else
			throw new IndexOutOfBoundsException("invalid section "+ y);
	}
	
	/**
	 * <h1>Gets the cross section by Z.</h1><p>
	 * <i><ul>getCrossSectionByZ(int z)<i><p>
	 * @param z - the zAxis damnation
	 * @return imt[][] the cross section by Z
	 * @throws IndexOutOfBoundsException the section is out of bounds exception
	 */
	public int[][] getCrossSectionByZ(int z)throws IndexOutOfBoundsException{
		if(z>=0 && z < this.getzAxis()){
			int [][] crossByZ = new int[getxAxis()][getyAxis()];
			
			for ( int i=0; i<getxAxis(); i++){
				for( int j=0; j<getyAxis(); j++){
					crossByZ[i][j]=array[z][i][j];
				}
			}
			return crossByZ;
		}
		else
		{
			throw new IndexOutOfBoundsException("invalid section "+ z);
		}
	}
	
	/**
	 * <h1>Gets the cross section by X.</h1><p>
	 * <i><ul>getCrossSectionByZ(int x)<i><p>
	 * @param x - the xAxis damnation
	 * @return imt[][] the cross section by X
	 * @throws IndexOutOfBoundsException the section is out of bounds exception
	 */
	public int[][] getCrossSectionByX(int x)throws IndexOutOfBoundsException  {
		if(x>=0 && x < this.getxAxis())
		{
			int [][] crossByX = new int[this.getzAxis()][this.getxAxis()];
			for ( int i=0; i<this.getzAxis(); i++)
			{
				for( int j=0; j<this.getyAxis(); j++)
					crossByX[i][j]=array[i][x][j];	
			}
			return crossByX;
		}
		else
			throw new IndexOutOfBoundsException("invalid section "+ x);
	}
	
	
	
	/**
	 * <h1>Print cross by Axis</h1><p>
	 * <i><ul>printCrossByAxis(int [][] arr)<i><p>
	 * Prints the cross section
	 *
	 * @param arr the section
	 */
	public void printCrossByAxis(int [][] arr){

		String str = new String();
		for (int i=0;i<arr.length;i++){
			for( int j=0; j<arr[i].length;j++){
				str += arr[i][j]+"   ";
			}
			str+="\n";	
		}
		System.out.println(str);
	}
	
	
	/**
	 * <h1>Creates  random position.</h1><p>
	 * <i><ul>Position createRandomPosition()<i><p>
	 *
	 * @return Position - random position
	 */
	public Position createRandomPosition(){

		r = new Random();
		int wall = r.nextInt(6);
		int z=0,x=0,y=0;

		switch (wall) {
		case 5:// if at ceiling
			z = getzAxis()-2;
			while(x%2==0)
				x = r.nextInt(getxAxis()-2)+1;
			while (y%2==0)
				y = r.nextInt(getyAxis()-2)+1;
			break;
		case 0: // if at floor
			z=1;
			while(x%2==0)
				x = r.nextInt(getxAxis()-2)+1;
			while (y%2==0)
				y = r.nextInt(getyAxis()-2)+1;
			break;
		case 1://if at left edge
			while (z%2 ==0)
				z = r.nextInt(getzAxis()-2)+1;
			while(x%2==0)
				x = r.nextInt(getxAxis()-2)+1;
			//y = 1;
				y = 1;
			break;
		case 2: //if at right edge
			while (z%2 ==0)
				z = r.nextInt(getzAxis()-2)+1;
			while(x%2==0)
				x = r.nextInt(getxAxis()-2)+1;
			// y = myMaze.getyAxis()-2;
			y = getyAxis()-2;
			break;
		case 3: //if at backward edge
			while (z%2 ==0)
				z = r.nextInt(getzAxis()-2)+1;
			x = 1;
			//x=1;
			while (y%2==0)
				y = r.nextInt(getyAxis()-2)+1;
			break;
		case 4: //if at forward edge
			while (z%2 ==0)
				z = r.nextInt(getzAxis()-2)+1;
			x = getxAxis()-2;
			//x=myMaze.getxAxis()-2;
			while (y%2==0)
				y = r.nextInt(getyAxis()-2)+1;
			break;
		}
		
		return new Position(z,x,y);
	}
	
	
	public Position carvePosition(Position p){
		Position temp = new Position(p);
		if(temp.getZ()==0){  //Check if start position at the floor
			temp.setZ(1);
			setPass(temp);
		}else if(temp.getZ()==getzAxis()-1){//Check if start position at the ceiling
			temp.setZ(getzAxis()-2);
			setPass(temp);
		}


		if(temp.getX()!=0 && (temp.getY()==0 )){  //Check if start position at the floor
			temp.setY(1);
			setPass(temp);
		}else if(temp.getY()==getyAxis()-1){//Check if start position at the ceiling
			temp.setY(getyAxis()-2);
			setPass(temp);
		}	

		if(temp.getX()==0){  //Check if start position at the floor
			temp.setX(1);
			setPass(temp);
		}else if(temp.getX()==getxAxis()-1){//Check if start position at the ceiling
			temp.setX(getyAxis()-2);
			setPass(temp);
		}
		
		return temp;
	}
	


}