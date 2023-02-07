package Pacman;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/** This is my Pacman class, which models the protagonist of the game, Pacman.
* This class does not have too much going on, as most of the logic of the actual
* mechanics of Pacman are contained in the game class. This class does, however
* keep active track of the direction in which pacman is moving by updating
* the value of the direction enum appropriately.
*/
public class Pacman {
	private Circle _pacman;
	private Direction _direction;
	public Pacman() {
		_pacman = new Circle();
		_pacman.setFill(Color.YELLOW);
		_pacman.setCenterY(Constants.PACMAN_STARTING_Y + Constants.CIRCLE_CENTER_X);
		_pacman.setCenterX(Constants.PACMAN_STARTING_X + Constants.CIRCLE_CENTER_Y);
		_pacman.setRadius(Constants.PACMAN_RADIUS);
	}

	// Setter for pacman's y location
	public void setY(double y) {
		_pacman.setCenterY(y);
	}
	// Setter for pacman's x location
	public void setX(double x) {
		_pacman.setCenterX(x);
	}
	// Getter for pacman's y location
	public double getY() {
		return _pacman.getCenterY();
	}
	// Getter for pacman's x location
	public double getX() {
		return _pacman.getCenterX();
	}

	// returns the instance of pacman. Used in game class to set node to front
	// and avoid java layering issues
	public Circle getPacman() {
		return _pacman;
	}
	// Method that moves pacman one index to the right in the board and updates
	// current direction
	public void moveRight() {
		_pacman.setCenterX(_pacman.getCenterX() + Constants.SIDE_LENGTH);
		_direction = Direction.RIGHT;
	}
	// Method that moves pacman one index to the left in the board and updates
	// current direction
	public void moveLeft() {
		_pacman.setCenterX(_pacman.getCenterX() - Constants.SIDE_LENGTH);
		_direction = Direction.LEFT;
	}
	// Method that moves pacman one index down in the board and updates
	// current direction
	public void moveDown() {
		_pacman.setCenterY(_pacman.getCenterY() + Constants.SIDE_LENGTH);
		_direction = Direction.DOWN;
	}
	// Method that moves pacman one index up in the board and updates
	// current direction
	public void moveUp() {
		_pacman.setCenterY(_pacman.getCenterY() - Constants.SIDE_LENGTH);
		_direction = Direction.UP;
	}
	// getter for current direcion
	public Direction getDirection() {
		return _direction;
	}
}
