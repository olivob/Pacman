package Pacman;

import javafx.scene.control.Label;
import javafx.scene.paint.Color;

/** This is my SideBar class that contains the score label and the lives label 
 *  This class primarily handles the graphical makeup of the two labels, and 
 *  has methods that return integers representing the score and number of lives.
 *  It also has methods to return both labels, as well as methods to update the score appropriately
 */
public class SideBar{
	private int _score; 
	private Label _scoreLabel;
	private Label _lives; 
	private int _lifeCount;

	public SideBar() {
		_scoreLabel = new Label("Score: " + _score);
		_lifeCount = 3;
		_scoreLabel.setTextFill(Color.WHITE);
		_scoreLabel.setScaleX(Constants.SCALE);
		_scoreLabel.setScaleY(Constants.SCALE);
		_lives = new Label("Lives: " + _lifeCount);
		_lives.setTextFill(Color.WHITE);
		_lives.setScaleX(Constants.SCALE);
		_lives.setScaleY(Constants.SCALE);
	}
	
	public Label getLabel() {
		return _scoreLabel;
	}
	
	public Label getLifeLabel() {
		return _lives;
	}
	
	public int getScore() {
		return _score;
	}
	
	public int getLife() {
		return _lifeCount;
	}
	
	// The two methods below are called in the game class when the appropriate collision occurs
	public void updateScore(int score) {
		_score += score;
		_scoreLabel.setText("Score: " + _score);
	}
	
	public void updateLife() {
		_lifeCount -= 1; 
		_lives.setText("Lives: " + _lifeCount);
	}
	
}