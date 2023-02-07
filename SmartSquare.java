package Pacman;

import java.util.ArrayList;

public class SmartSquare {
	private ArrayList<Collidable> _squareType;
	private boolean _isWall;
	private boolean _isEnergizer;
	private boolean _isDot;
	
	/** This is my SmartSquare class, which contains an arrayList of collidable items so that the square always knows what kind of object it has inside of it.
	 *  The constructor takes in booleans for walls, dots, and energizers and are set to true/false depending on the type it is meant to represent. 
	 *  This is implemented in the game class, and each type of object has a method that returns a boolean indicating if they are or are not a wall/dot/energizer.
	 */
	public SmartSquare(boolean isWall, boolean isEnergizer, boolean isDot) {
		_squareType = new ArrayList<Collidable>();
		_isWall = isWall;
		_isEnergizer = isEnergizer;
		_isDot = isDot;
	}
	
	public boolean isWall() {
		return _isWall;
	}
	
	public boolean isEnergizer() {
		return _isEnergizer;
	}
	
	public boolean isDot() {
		return _isDot;
	}
	
	
	public void clearList() {
		_squareType.clear();
		_isWall = false;
		_isEnergizer = false;
	}

//	public void makeWall() {
//		Rectangle rectangle = new Rectangle();
//		rectangle.setFill(Color.BLUE);
//	}
	
	// Populates the arrayList. This is done whenever objects are created//whenever ghosts move into a new SmartSquare
	public void fill(Collidable collidable) {
		_squareType.add(collidable);
	}
	// removes items from arrayList logically
	public void remove(Collidable collidable) {
		_squareType.remove(collidable);
	}
	// Returns the arrayList
	public ArrayList<Collidable> getArrayList(){
		return _squareType;
	}

}
