package Pacman;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

/** This class models an energizer class, which contains primarily graphical components to the class. 
 *  An energizer is a composite shape, with a larger circle on a black background. Upon instantiation, 
 *  I pass in a pane so that energizers can be immediately added graphically. 
 */
public class Energizer implements Collidable {
	private Circle _energizer;
	private Rectangle _back;
	private Pane _pane;

	public Energizer(Pane pane) {
		_energizer = new Circle();
		_back = new Rectangle();
		_energizer.setFill(Color.WHITE);
		_energizer.setRadius(Constants.ENERGIZER_RADIUS);
		_energizer.setCenterX(Constants.CIRCLE_CENTER_X);
		_energizer.setCenterY(Constants.CIRCLE_CENTER_Y);
		_back.setFill(Color.BLACK);
		_back.setHeight(Constants.SIDE_LENGTH);
		_back.setWidth(Constants.SIDE_LENGTH);
		pane.getChildren().addAll(_back, _energizer);
		_pane = pane;
	}

	// Sets both x and y location 
	public void setLocation(double x, double y) {
		_back.setY(y);
		_energizer.setCenterY(y + Constants.CIRCLE_CENTER_Y);
		_back.setX(x);
		_energizer.setCenterX(x + Constants.CIRCLE_CENTER_X);
	}
	// Below are two getters for the x and y locations
	public double getX() {
		return _back.getX();
	}

	public double getY() {
		return _back.getY();
	}
	// When an energizer collides with pacman, it is removed graphically here. 
	// It is logically removed using smartSquare capabilities in the SmartSquare class. This logic
	// is handled in the game class. 
	@Override
	public int collide(Pacman pacman, Game game) {
		_pane.getChildren().remove(_energizer);
		return 0;
	}
	// An energizer adds +100 to the score;
	@Override
	public int updateScore(int score, Game game) {
		return score += 100;
	}
	// An energizer being eaten contributes to the gameOverCoutner, which calls game over when all dots and energizers have been eaten
	@Override
	public int updateGameOverCounter(int gameOverCounter) {
		return gameOverCounter += 1;
	}
}
