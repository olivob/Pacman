package Pacman;

import java.util.LinkedList;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

/**
 * This is my GhostPen class, which handles the logic of the ghostPen. The ghostPen uses a linked list that acts like a queue
 * which first adds the ghosts in the pen at the initial state of the game and periodically releases them. This is done in the 
 * game class. I set the Duration to one second and incremented a counter to handle the delay between ghosts being released.
 */
public class GhostPen {
	private Timeline _timeline;
	private LinkedList<Ghost> _ghostQ;
	private Ghost _next;
	private SmartSquare[][] _board;
	private int _counter; 
	private Game _game;
	public GhostPen(SmartSquare[][] board, Game game) { // Chose to pass in a board to preserve the actual board used in the game class 
		_game = game;
		_board = board;
		_ghostQ = new LinkedList<Ghost>();
		_counter = 0;
		this.setupTimeline(); // GhostPen operates on its own, slower timeline
	}
	
	public void addToQueue(Ghost ghost) {
		_ghostQ.addLast(ghost); // enqueue
	}
	
	public void clear() {
		_ghostQ.clear();
	}
	
	// The method below simply shifts the pointer to be the next thing to be released from the pen, which is also 
	// equivalent to the first thing waiting in line (aka the the object that is dequeued). It graphically and logically 
	// moves the ghost
	public void updatePen() {
		if (_ghostQ.size() != 0) { // error checking null case
			_next = _ghostQ.removeFirst();
			_next.setX(Constants.GHOST_COL * Constants.SIDE_LENGTH);
			_next.setY(Constants.PEN_ROW * Constants.SIDE_LENGTH);
			_board[Constants.GHOST_ROW][Constants.GHOST_COL].fill(_next);
		}
	}
	// Sets up the timeline and keyframe
	private void setupTimeline() {
		KeyFrame kf = new KeyFrame(Duration.seconds(Constants.PEN_DURATION), new TimeHandler());
		_timeline = new Timeline(kf);
		_timeline.setCycleCount(Animation.INDEFINITE);
		_timeline.play();
	}
	
	private class TimeHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {
			_counter++;
			if (_counter %5 == 0 && _game.checkGameOver() == false) { // updates ghost pen every four seconds
				GhostPen.this.updatePen();
			}
		}
	}
}