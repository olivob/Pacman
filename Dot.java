package Pacman;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

/** This is my dot class, which contains primarily graphical components. Upon instantiating, the dot is set appropriate color and size and added 
 * to the game pane. A dot is a composite shape of sorts: a white circle with a black square background.
 *
 */
public class Dot implements Collidable {
	private Circle _dot;
	private Rectangle _back;
	private Pane _pane;
	public Dot(Pane pane) {
		_dot = new Circle();
		_back = new Rectangle();
		_dot.setFill(Color.WHITE);
		_dot.setRadius(Constants.DOT_RADIUS);
		_back.setFill(Color.BLACK);
		_back.setHeight(Constants.SIDE_LENGTH);
		_back.setWidth(Constants.SIDE_LENGTH);
		_pane = pane;
		_pane.getChildren().addAll(_back, _dot);
	}
	
	/** Sets x and y location in one method
	 */
	public void setLocation(double x, double y) {
		_back.setY(y);
		_dot.setCenterY(y + Constants.CIRCLE_CENTER_Y);
		_back.setX(x);
		_dot.setCenterX(x + Constants.CIRCLE_CENTER_X);
	}
	// Below are two getters for x and y location
	public double getX(){
		return _back.getX();
	}
	public double getY(){
		return _back.getY();
	}
	// Upon collision, dots should be removed graphically. They are removed logically from the arrayList of smartsquares in the game class
	@Override
	public int collide(Pacman pacman, Game game) {
		_pane.getChildren().remove(_dot);
		return 0;
	}
	// Dots should reward +10 points whenever eaten
	@Override
	public int updateScore(int score, Game game) {
		return score+= 10;
	}
	
	@Override
	public int updateGameOverCounter(int gameOverCounter) {
		return gameOverCounter+=1; //counter keeps track of dots/energizers eaten
	}
}
