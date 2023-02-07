package Pacman;

import cs015.fnl.PacmanSupport.SupportMap;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.ArrayList;


import cs015.fnl.PacmanSupport.SquareType;

/**
 * This is my game class, responsible for much of the logic of Pacman. This class is the heart of my entire code
 * and contains a lot of information! The rules of the game Pacman are all set here, and it would be best to 
 * look around and take a look at the many methods this class contains individually.
 * Since this class is so enormous, I do not comment everything to the degree of specificity as the other classes, 
 * but, in my opinion, reading the README and other classes first will help understand my game class as best as possible! 
 */

public class Game {
	private Pane _gamePane;
	private SmartSquare[][] _board;
	private Timeline _timeline;
	private KeyHandler _keyHandler;
	private Pacman _pacman;
	private Ghost _blinky;
	private Ghost _inky;
	private Ghost _pinky;
	private Ghost _clyde;
	private SmartSquare _blinkySquare;
	private SmartSquare _inkySquare;
	private SmartSquare _pinkySquare;
	private SmartSquare _clydeSquare;
	private int _pacmanRow;
	private int _pacmanCol;
	private int _ghostRow;
	private int _ghostCol;
	private GhostPen _ghostPen;
	private Direction _pacmanDirection;
	private Mode _mode;
	private int _modeCounter;
	private SideBar _sideBar;
	private int _score;
	private int _gameOverCounter;
	private int _frightenedCounter;

	public Game(SideBar sideBar) { // Pass the sideBar from the PaneOrganizer so that it updates properly 
		_gamePane = new Pane();
		_board = new SmartSquare[23][23];
		_keyHandler = new KeyHandler();
		_gamePane.setOnKeyPressed(_keyHandler);
		_gamePane.setFocusTraversable(true);
		_pacmanDirection = Direction.UP; // arbitrarily setting Pacman's initial direction to error check the null case
		_mode = Mode.CHASE; // set the initial game state to be in chase mode
		_modeCounter = 0;
		_frightenedCounter = 0;
		_score = 0;
		_gameOverCounter = 0;
		_sideBar = sideBar;
		_ghostPen = new GhostPen(_board, this);
		this.generateMap(); 
		this.setupTimeline();
		this.populateGhostPen();
	}

	public Pane getGamePane() {
		return _gamePane;
	}

	//Enqueue the ghosts in the order described by the handout that are in the pen
	public void populateGhostPen() {
		_ghostPen.addToQueue(_pinky);
		_ghostPen.addToQueue(_inky);
		_ghostPen.addToQueue(_clyde);
	}

	public Ghost getPinky() {
		return _pinky;
	}

	public Ghost getInky() {
		return _inky;
	}

	public Ghost getClyde() {
		return _clyde;
	}

	/**
	 *  Here I use the support map to generate my own version of the pacman map. I check at each square type what kind of object belongs there, and then 
	 *  use my helper classes to properly fill the 2D array of SmartSquares. Each smart square takes in three booleans, as shown in the SmartSquare class
	 *  which keep track of what kind of object the SmartSquare contains. Then, I instantiate the appropriate collidable object, and set its location graphically
	 */
	private void generateMap() {
		SquareType[][] supportMap = SupportMap.getSupportMap();
		for (int i = 0; i < supportMap.length; i++) {
			for (int j = 0; j < supportMap[0].length; j++) {
				if (supportMap[i][j].equals(SquareType.DOT)) {
					SmartSquare square = new SmartSquare(false, false, true);
					_board[i][j] = square;
					Dot dot = new Dot(_gamePane);
					square.fill(dot);
					dot.setLocation(Constants.SIDE_LENGTH * j, Constants.SIDE_LENGTH * i);
				}
				if (supportMap[i][j].equals(SquareType.ENERGIZER)) {
					SmartSquare square = new SmartSquare(false, true, false);
					_board[i][j] = square;
					Energizer energizer = new Energizer(_gamePane);
					square.fill(energizer);
					energizer.setLocation(Constants.SIDE_LENGTH * j, Constants.SIDE_LENGTH * i);

				}
				if (supportMap[i][j].equals(SquareType.WALL)) {
					SmartSquare square = new SmartSquare(true, false, false);
					_board[i][j] = square;
					Wall wall = new Wall(_gamePane);
					wall.setLocation(Constants.SIDE_LENGTH * j, Constants.SIDE_LENGTH * i);
				}
				if (supportMap[i][j].equals(SquareType.PACMAN_START_LOCATION)) {
					SmartSquare square = new SmartSquare(false, false, false);
					_board[i][j] = square;
					_pacman = new Pacman();
				}
				if (supportMap[i][j].equals(SquareType.FREE)) {
					SmartSquare square = new SmartSquare(false, false, false);
					_board[i][j] = square;
				}
				/**
				 * The SmartSquare class only has one ghost starting locations, and thus the four ghosts are instantiated relative to that position
				 * with inky populating the actual spot in the 2D array and all other ghosts offset from that using constants. 
				 */
				if (supportMap[i][j].equals(SquareType.GHOST_START_LOCATION)) {
					_blinkySquare = new SmartSquare(false, false, false);
					_board[i - Constants.BLINKY_OFFSET_INDEX][j] = _blinkySquare;
					_blinky = new Ghost(Color.RED, _gamePane, this);
					_blinkySquare.fill(_blinky);
					_blinky.setLocation(Constants.SIDE_LENGTH * j, Constants.SIDE_LENGTH * i - Constants.BLINKY_OFFSET);
					_pinkySquare = new SmartSquare(false, false, false);
					_board[i - 1][j] = _pinkySquare;
					_pinky = new Ghost(Color.PINK, _gamePane, this);
					_pinkySquare.fill(_pinky);
					_pinky.setLocation(Constants.SIDE_LENGTH * j - Constants.PINKY_OFFSET, Constants.SIDE_LENGTH * i);
					_inkySquare = new SmartSquare(false, false, false);
					_board[i][j] = _inkySquare;
					_inky = new Ghost(Color.CYAN, _gamePane, this);
					_inkySquare.fill(_inky);
					_inky.setLocation(Constants.SIDE_LENGTH * j, Constants.SIDE_LENGTH * i);
					_clydeSquare = new SmartSquare(false, false, false);
					_board[i + 1][j] = _clydeSquare;
					_clyde = new Ghost(Color.GOLDENROD, _gamePane, this);
					_clydeSquare.fill(_clyde);
					_clyde.setLocation(Constants.SIDE_LENGTH * j + Constants.CLYDE_OFFSET, Constants.SIDE_LENGTH * i);
				}
			}
		}
		/**
		 * THE PURPOSE OF THE NEXT FOUR LINES: With the help of one of my TA's I discovered that even after looping through the support code, 
		 * my loop was not adding specifically row: 9, column: 11. This was causing my ghosts to escape the pen prematurely. As such, 
		 * I simply hard code that specific spot to be a wall. Both my TA and I were not sure what was causing this.
		 */
		SmartSquare square = new SmartSquare(true, false, false);
		_board[9][11] = square;
		Wall wall = new Wall(_gamePane);
		wall.setLocation(11 * Constants.SIDE_LENGTH, 9 * Constants.SIDE_LENGTH);
		// Below I am setting the nodes of each ghost to the front and adding pacman graphically because they were often disappearing randomly without doing so 
		Circle node = _pacman.getPacman();
		_gamePane.getChildren().add(node);
		Rectangle blinkyNode = _blinky.getGhost();
		blinkyNode.toFront();
		Rectangle pinkyNode = _pinky.getGhost();
		pinkyNode.toFront();
		Rectangle inkyNode = _inky.getGhost();
		inkyNode.toFront();
		Rectangle clydeNode = _clyde.getGhost();
		clydeNode.toFront();
	}

	/**
	 * The method below checks for collision with Pacman, since all objects collide with only Pacman. This method is constantly being checked in the 
	 * timehandler. Also, if there is a collision, both the score, life counter, and game over counter are updated according to whatever type of 
	 * collidable object is contained. I passed in a game to the collide and update score method because the ghost collision for both of these 
	 * work different in frightened mode vs. the other modes. 
	 */
	private void checkCollision(int row, int column, Ghost ghost) {
		SmartSquare square = _board[row][column];
		ArrayList<Collidable> list = square.getArrayList();
		for (int i = 0; i < list.size(); i++) {
			int type = list.get(i).collide(_pacman, this);
			_gameOverCounter = list.get(i).updateGameOverCounter(_gameOverCounter);
			_sideBar.updateScore(list.get(i).updateScore(_score, this));
			if (type ==1 ) {
				this.eatGhost(ghost);
				if (_mode != Mode.FRIGHTENED) {
					_sideBar.updateLife();
				}
				if (checkGameOver()) { // Done here to end the game as soon as the collision ends instead of one time step afterwards
					this.gameOver();
				}
			}
		}
		square.clearList(); // logical removal 
	}
	
	// This method graphically resets the ghost after being eaten
	private void eatGhost(Ghost ghost) {
		if (this.isFrightened() != true) {
			_ghostCol = (int) (ghost.getX() / Constants.SIDE_LENGTH);
			_ghostRow = (int) (ghost.getY() / Constants.SIDE_LENGTH);
			_blinky.setLocation(Constants.BLINKY_START_X, Constants.BLINKY_START_Y);
			_pinky.setLocation(Constants.PINKY_START_X, Constants.PINKY_START_Y);
			_inky.setLocation(Constants.INKY_START_X, Constants.INKY_START_Y);
			_clyde.setLocation(Constants.CLYDE_START_X, Constants.CLYDE_START_Y);
			_blinky.setStartingDirection();
			this.resetLogically(_ghostRow, _ghostCol, ghost);
			if (checkGameOver() == false) {
				_ghostPen.clear();
				this.populateGhostPen();
			}
		}
			else {
				this.resetFrightenedLogically(_ghostRow, _ghostCol, ghost);
				_ghostPen.addToQueue(ghost);
			}
	}
	// This method logically sends ghosts to starting position, done via hard coding to avoid a confusing amount of constants
	private void resetLogically(int ghostRow, int ghostCol, Ghost ghost) {
		_board[8][11].fill(_blinky);
		_board[10][10].fill(_pinky);
		_board[10][11].fill(_inky);
		_board[10][12].fill(_clyde);
		_board[ghostRow][ghostCol].remove(ghost);
	}

	// This method logically sends ghosts back to the pen
	private void resetFrightenedLogically(int ghostRow, int ghostCol, Ghost ghost) {
		_board[Constants.GHOST_ROW][Constants.GHOST_COL].fill(ghost);
		_board[ghostRow][ghostCol].remove(ghost);
	}

	public int getScore() {
		return _score;
	}

	public SmartSquare[][] getBoard() {
		return _board;
	}

	// This method toggles Frightened mode, and sets the ghost to be a blue color
	// This is called in the timeHandler when Pacman collides with an energizer
	private void toggleFrightenedMode() {
		_pacmanCol = (int) (_pacman.getPacman().getCenterX() / Constants.SIDE_LENGTH);
		_pacmanRow = (int) (_pacman.getPacman().getCenterY() / Constants.SIDE_LENGTH);
		if (_board[_pacmanRow][_pacmanCol].isEnergizer()) {
			_blinky.turnBlue();
			_pinky.turnBlue();
			_inky.turnBlue();
			_clyde.turnBlue();
		}
		_mode = Mode.FRIGHTENED;

	}
	// Checks to see if the game is in frightened mode
	public boolean isFrightened() {
		if (_mode == Mode.FRIGHTENED) {
			return true;
		}
		return false;
	}
	// Called after frightened mode ends
	private void revertColor() {
		_blinky.revertColor();
		_inky.revertColor();
		_pinky.revertColor();
		_clyde.revertColor();
	}

	/** This method handles the ghost movement entirely. The method takes in a target row and column, as well as a ghost, and immediately 
	 * calls BFS to determine the direction in which the ghosts need to turn to get to the target. Based on this direction, I use a switch 
	 * statement to determine movement. In order to update logically, I first remove the ghost from its current tile, move the ghost graphically, 
	 * and then logically add it to the square it is moving toward.
	 */
	private void moveGhost(Ghost ghost, int targetRow, int targetCol) {
		_ghostCol = (int) (ghost.getX() / Constants.SIDE_LENGTH);
		_ghostRow = (int) (ghost.getY() / Constants.SIDE_LENGTH);
		Direction direction = ghost.BFS(_ghostRow, _ghostCol, targetRow, targetCol);
		switch (direction) {
		case UP:
			if (_board[_ghostRow - 1][_ghostCol].isWall() == false) {
				_board[_ghostRow][_ghostCol].remove(ghost);
				ghost.moveUp();
				_board[_ghostRow - 1][_ghostCol].fill(ghost);
			}
			break;
		case DOWN:
			if (_board[_ghostRow + 1][_ghostCol].isWall() == false) {
				_board[_ghostRow][_ghostCol].remove(ghost);
				ghost.moveDown();
				_board[_ghostRow + 1][_ghostCol].fill(ghost);
			}
			break;
		case LEFT:
			if (_ghostCol - 1 < 0) { // tunnel edge case
				ghost.setX(Constants.SCENE_WIDTH - Constants.SIDE_LENGTH);
			} else if (_board[_ghostRow][_ghostCol - 1].isWall() == false) {
				_board[_ghostRow][_ghostCol].remove(ghost);
				ghost.moveLeft();
				_board[_ghostRow][_ghostCol - 1].fill(ghost);
			}
			break;
		case RIGHT:
			if (_ghostCol == 22) { // tunnel edge case
				ghost.setX(0);
			} else if (_board[_ghostRow][_ghostCol + 1].isWall() == false) {
				_board[_ghostRow][_ghostCol].remove(ghost);
				ghost.moveRight();
				_board[_ghostRow][_ghostCol + 1].fill(ghost);
			}
			break;
		default:
			break;
		}
	}

	private void atRightTunnel() {
		_pacman.setX(Constants.CIRCLE_CENTER_X);
	}

	private void atLeftTunnel() {
		_pacman.setX(Constants.SCENE_WIDTH - Constants.SIDE_LENGTH + Constants.CIRCLE_CENTER_X);
	}

	private void setupTimeline() {
		KeyFrame kf = new KeyFrame(Duration.seconds(Constants.DURATION), new TimeHandler());
		_timeline = new Timeline(kf);
		_timeline.setCycleCount(Animation.INDEFINITE);
		_timeline.play();
	}

	/** This method takes in pacman's current direction at any given movement and allows him to continuously 
	 *  move in that direction! The direction is updated in the keyHandler
	 */
	private void movePacman(Direction direction) {
		_pacmanCol = (int) (_pacman.getPacman().getCenterX() / Constants.SIDE_LENGTH);
		_pacmanRow = (int) (_pacman.getPacman().getCenterY() / Constants.SIDE_LENGTH);
		switch (direction) {
		case UP:
			if (_board[_pacmanRow - 1][_pacmanCol].isWall() == false) {
				_pacman.moveUp();
			}
			break;
		case DOWN:
			if (_board[_pacmanRow + 1][_pacmanCol].isWall() == false) {
				_pacman.moveDown();
			}
			break;
		case LEFT:
			if (_pacmanCol - 1 < 0 && _pacmanDirection != Direction.RIGHT) {
				this.atLeftTunnel(); // tunnel edge case
			} else if (_board[_pacmanRow][_pacmanCol - 1].isWall() == false) {
				_pacman.moveLeft();
			}
			break;
		case RIGHT:
			if (_pacmanCol == Constants.SCENE_WIDTH / Constants.SIDE_LENGTH - 1 && _pacmanDirection != Direction.LEFT) {
				this.atRightTunnel(); // tunnel edge case
			} else if (_board[_pacmanRow][_pacmanCol + 1].isWall() == false) {
				_pacman.moveRight();
			}
			break;
		default:
			break;
		}
	}

	// This method is called to efficiently keep track of scatter and chase mode in timeHandler
	private void resetCounter() {
		_modeCounter = 0;
	}

	public boolean checkGameOver() {
		if (_sideBar.getLife() == 0 || _score > 2500 || _gameOverCounter == Constants.NO_MORE_ENERGIZERS_OR_DOTS) { // conditions to end the game (0 lives, or all energizers/dots being eaten
			return true;
		}
		return false;
	}

	// Ends the game
	private void gameOver() {
		_timeline.stop();
		_gamePane.setOnKeyPressed(null);
		Label gameOver = new Label("Thanks for Playing!");
		gameOver.setLayoutX(Constants.GAMEOVER_LAYOUT_X);
		gameOver.setLayoutY(Constants.GAMEOVER_LAYOUT_Y);
		gameOver.setScaleX(Constants.GAMEOVER_SCALE);
		gameOver.setScaleX(Constants.GAMEOVER_SCALE);
		gameOver.setTextFill(Color.RED);
		_gamePane.getChildren().add(gameOver);
		System.out.println("Game Over!");
	}

	// Helper method that factors out repetetive code. Generates a random row and column for each ghost to head toward during 
	// frightened mode
	private void handleFrightened() {
		_pacmanCol = (int) (_pacman.getPacman().getCenterX() / Constants.SIDE_LENGTH);
		_pacmanRow = (int) (_pacman.getPacman().getCenterY() / Constants.SIDE_LENGTH);
		int blinkyFrightenedRow = (int) (Math.random() * 24);
		int blnkyFrightenedCol = (int) (Math.random() * 24);
		int pinkyFrightenedRow = (int) (Math.random() * 24);
		int pinkyFrightenedCol = (int) (Math.random() * 24);
		int inkyFrightenedRow = (int) (Math.random() * 24);
		int inkyFrightenedCol = (int) (Math.random() * 24);
		int clydeFrightenedRow = (int) (Math.random() * 24);
		int clydeFrightenedCol = (int) (Math.random() * 24);
		this.movePacman(_pacmanDirection);
		this.checkCollision(_pacmanRow, _pacmanCol, _blinky);
		this.checkCollision(_pacmanRow, _pacmanCol, _pinky);
		this.checkCollision(_pacmanRow, _pacmanCol, _inky);
		this.checkCollision(_pacmanRow, _pacmanCol, _clyde);
		this.moveGhost(_blinky, blinkyFrightenedRow, blnkyFrightenedCol);
		this.checkCollision(_pacmanRow, _pacmanCol, _blinky);
		this.moveGhost(_pinky, pinkyFrightenedRow, pinkyFrightenedCol);
		this.checkCollision(_pacmanRow, _pacmanCol, _pinky);
		this.moveGhost(_inky, inkyFrightenedRow, inkyFrightenedCol);
		this.checkCollision(_pacmanRow, _pacmanCol, _inky);
		this.moveGhost(_clyde, clydeFrightenedRow, clydeFrightenedCol);
		this.checkCollision(_pacmanRow, _pacmanCol, _clyde);
//		this.checkCollision(_pacmanRow, _pacmanCol, _blinky);
//		this.checkCollision(_pacmanRow, _pacmanCol, _pinky);
//		this.checkCollision(_pacmanRow, _pacmanCol, _inky);
//		this.checkCollision(_pacmanRow, _pacmanCol, _clyde);
	}
	
	// Helper method that takes the current mode of the game and moves pacman and the ghosts appropriately 
	// Chase mode: all the ghosts either target pacman directly or the recommended spaces around pacman
	// Scatter: they head to the four corners of the map and circle around 
	// In Frightened: they chase a random target. 
	
	private void handleGhostBehavior(Mode mode) {
		_pacmanCol = (int) (_pacman.getPacman().getCenterX() / Constants.SIDE_LENGTH);
		_pacmanRow = (int) (_pacman.getPacman().getCenterY() / Constants.SIDE_LENGTH);
		int pinkyTarget = _pacmanRow + 2;
		int inkyTarget = _pacmanCol + 4;
		int clydeTargetRow = _pacmanRow + 1;
		int clydeTargetCol = _pacmanCol - 3;
		switch (mode) {
		case CHASE: 
			this.movePacman(_pacmanDirection);
			this.checkCollision(_pacmanRow, _pacmanCol, _blinky);
			this.checkCollision(_pacmanRow, _pacmanCol, _pinky);
			this.checkCollision(_pacmanRow, _pacmanCol, _inky);
			this.checkCollision(_pacmanRow, _pacmanCol, _clyde);
			this.moveGhost(_blinky, _pacmanRow, _pacmanCol);
			this.checkCollision(_pacmanRow, _pacmanCol, _blinky);
			this.moveGhost(_pinky, pinkyTarget, _pacmanCol);
			this.checkCollision(_pacmanRow, _pacmanCol, _pinky);
			this.moveGhost(_inky, inkyTarget, _pacmanCol);
			this.checkCollision(_pacmanRow, _pacmanCol, _inky);
			this.moveGhost(_clyde, clydeTargetRow, clydeTargetCol);
			this.checkCollision(_pacmanRow, _pacmanCol, _clyde);
			break;
		case SCATTER: 
			this.movePacman(_pacmanDirection);
			this.checkCollision(_pacmanRow, _pacmanCol, _blinky);
			this.checkCollision(_pacmanRow, _pacmanCol, _pinky);
			this.checkCollision(_pacmanRow, _pacmanCol, _inky);
			this.checkCollision(_pacmanRow, _pacmanCol, _clyde);
			this.moveGhost(_blinky, Constants.BLINKY_SCATTER_ROW, Constants.BLINKY_SCATTER_COL);
			this.checkCollision(_pacmanRow, _pacmanCol, _blinky);
			this.moveGhost(_pinky, Constants.PINKY_SCATTER_ROW, Constants.PINKY_SCATTER_COL);
			this.checkCollision(_pacmanRow, _pacmanCol, _pinky);
			this.moveGhost(_inky, Constants.INKY_SCATTER_ROW, Constants.INKY_SCATTER_COL);
			this.checkCollision(_pacmanRow, _pacmanCol, _inky);
			this.moveGhost(_clyde, Constants.CLYDE_SCATTER_ROW, Constants.CLYDE_SCATTER_COL);
			this.checkCollision(_pacmanRow, _pacmanCol, _clyde);
			break; 
		case FRIGHTENED: 
			this.handleFrightened(); // frightened was especially repetitive and has its own helper method
			break;
		}
			
	}

	/**
	 * My KeyHandler is primarily responsible for changing the direction of pacman appropriately. This direction 
	 * is used in the move method.
	 */
	private class KeyHandler implements EventHandler<KeyEvent> {

		@Override
		public void handle(KeyEvent e) {
			_pacmanCol = (int) (_pacman.getPacman().getCenterX() / Constants.SIDE_LENGTH);
			_pacmanRow = (int) (_pacman.getPacman().getCenterY() / Constants.SIDE_LENGTH);
			KeyCode keyCode = e.getCode();
			switch (keyCode) {
			case UP:
				if (_board[_pacmanRow - 1][_pacmanCol].isWall() == false) {
					_pacmanDirection = Direction.UP;
				}
				break;
			case DOWN:
				if (_board[_pacmanRow + 1][_pacmanCol].isWall() == false) {
					_pacmanDirection = Direction.DOWN;
				}
				break;
			case LEFT:
				if (_pacmanRow == 11 && _pacmanCol == 0) { // tunnel edge case
					_pacmanDirection = Direction.LEFT;
				} else if (_board[_pacmanRow][_pacmanCol - 1].isWall() == false) {
					_pacmanDirection = Direction.LEFT;
				}
				break;
			case RIGHT:
				if (_pacmanRow == 11 && _pacmanCol == 22) { // tunnel edge case
					_pacmanDirection = Direction.RIGHT;
				} else if (_board[_pacmanRow][_pacmanCol + 1].isWall() == false) {
					_pacmanDirection = Direction.RIGHT;
				}
				break;
			default:
				break;
			}
		}
	}

	/**
	 * My TimeHandler essentailly is constantly updating the state of the game to be CHASE, SCATTER, or FRIGHTENED depending on if
	 * the appropriate conditions have been met. Once the mode has been set, I call the helper method that handles all ghost and
	 * pacman behavior, which within that method contains movement methods and behavioral methods.
	 *
	 */
	private class TimeHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {
			if (checkGameOver()) {
				Game.this.gameOver();
			}
			_pacmanCol = (int) (_pacman.getPacman().getCenterX() / Constants.SIDE_LENGTH);
			_pacmanRow = (int) (_pacman.getPacman().getCenterY() / Constants.SIDE_LENGTH);
			if (_board[_pacmanRow][_pacmanCol].isEnergizer()) {
				Game.this.toggleFrightenedMode();
				_frightenedCounter = 0;
			}
			_modeCounter++;
			if (_mode == Mode.FRIGHTENED) {
				_modeCounter = 0;
				_frightenedCounter++;
				if (_frightenedCounter > 20) {
					_mode = Mode.CHASE;
					Game.this.revertColor();
				}
			} else if (_modeCounter > Constants.TWENTY_SECONDS) { 
				_mode = Mode.SCATTER;
			} else if (_mode != Mode.FRIGHTENED) {
				_mode = Mode.CHASE;
			}
			
			Game.this.handleGhostBehavior(_mode);
			if (_modeCounter > Constants.RESET_MODES) { // Consistently ensuring chase is 20 seconds and Scatter is 7 seconds
				Game.this.resetCounter();
			}
		}
	}

}
