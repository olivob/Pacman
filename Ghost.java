package Pacman;

import java.util.ArrayList;
import java.util.LinkedList;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/** This is my Ghost class, which is my second largest class because it contains
* my BFS algorithm. That method is shown and annotated below. Aside from BFS
* this class contains methods that move the ghosts around and keep track of
* their direction. The ghost class implements the Collidable interface,
* and appropriately Overrides those methods for the specific functionality
* of the ghosts.
*/
public class Ghost implements Collidable {
	private Rectangle _ghost;
	private Direction _direction;
	private Color _originalColor;
	private SmartSquare[][] _board;
	private BoardCoordinate _current;

/** I chose to pass in a color and pane so that upon instantiatiation,
* they would be added to the pane and set to the proper color to avoid
* redundant code. I also chose to pass in a Game so that I can get get the
* board into this class and use it for BFS.
*/
	public Ghost(Color color, Pane pane, Game game) {
		_ghost = new Rectangle();
		_originalColor = color;
		_ghost.setFill(_originalColor);
		_ghost.setHeight(Constants.SIDE_LENGTH);
		_ghost.setWidth(Constants.SIDE_LENGTH);
		pane.getChildren().add(_ghost);
		_board = game.getBoard();
		_direction = Direction.UP; // arbitrarily set the initial direction of the
		// ghosts - this was done to prevent null pointers. This direction is updated
		// appropriately and immediately due to BFS.
	}
	// Getter for the ghost node
	public Rectangle getGhost() {
		return _ghost;
	}

	// Setter to be used during frightned mode
	public void turnBlue() {
		_ghost.setFill(Color.LIGHTBLUE);
	}

	// Setter for when frightened mode is over
	public void revertColor() {
		_ghost.setFill(_originalColor);
	}

	// // Method that moves pacman one index to the right in the board and updates
	// current direction
	public void moveRight() {
		_ghost.setX(_ghost.getX() + Constants.SIDE_LENGTH);
		_direction = Direction.RIGHT;
	}
	//// Method that moves pacman one index to the left in the board and updates
	// current direction
	public void moveLeft() {
		_ghost.setX(_ghost.getX() - Constants.SIDE_LENGTH);
		_direction = Direction.LEFT;
	}
	// Method that moves pacman one index down in the board and updates
	// current direction
	public void moveDown() {
		_ghost.setY(_ghost.getY() + Constants.SIDE_LENGTH);
		_direction = Direction.DOWN;
	}
	// Method that moves pacman one index down in the board and updates
	// current direction
	public void moveUp() {
		_ghost.setY(_ghost.getY() - Constants.SIDE_LENGTH);
		_direction = Direction.UP;
	}
	// Returns the current direction of the ghost
	public Direction getDirection() {
		return _direction;
	}
	// This method is implemented to triple check that even after ghosts are
	// reset when they collide with pacman that they would have a direction and
	// bfs would not fail due to a null pointer.
	public void setStartingDirection() {
		_direction = Direction.UP;
	}

	/** The method below is used in my BFS as a helper method to gather
	*/

	/** The method below is my BFS algorithm. It begins by taking in the current
	* ghost row and location, as well as the target row and column (the same
	* algorithm is used in all modes of the game, with different targets). It
	* gathers the neighbors of the ghost, checks if they are walls or not, and
	* queues the neighbors to check the entire board. The algorithm essentially
	* maps out possible pathways to the target, and once the shortest
	* path has been mapped out, returns the initial direction that the ghost would
	* have taken to get there, constantly passing the direction up in the while
	* loop to the square being checked. The while loop ends once there are no
	* more elements to be queued, suggesting that all paths have been checked.
	* Also, I use the BoardCoordinate class instead of SmartSquare's due to the
	* error checking provided by the support cord.
	*/

	public Direction BFS(int ghostRow, int ghostCol, int targetRow, int targetCol) {
		LinkedList<BoardCoordinate> SquareList = new LinkedList<BoardCoordinate>();
		Direction[][] Directions = new Direction[23][23]; // Instantiating 2D array
		// that will be filled with directions based on validNeighbors. The final
		// direction that BFS returns is going to be stored in this array.
		_current = new BoardCoordinate(ghostRow, ghostCol, false); // updating the current pointer
		/**
		 * In the series of if statements below, I meticulously gather the neighbors of the ghost, 
		 * which will always be the four squares around the ghost. I check if they are valid by 
		 * seeing if they are not walls and the ghost would not be making a 180, and enqueue them to be 
		 * checked by BFS, as well as update the direction the ghost would have taken to get there
		 * in the 2D directions array.
		 */
		Directions[ghostRow][ghostCol] = Direction.RIGHT;
		if (_board[ghostRow - 1][ghostCol].isWall() == false && this.getDirection().getOpposite() != Direction.UP) {
			Directions[ghostRow - 1][ghostCol] = Direction.UP;
			SquareList.addLast(new BoardCoordinate(ghostRow - 1, ghostCol, false));
		}
		if (_board[ghostRow + 1][ghostCol].isWall() == false && this.getDirection().getOpposite() != Direction.DOWN) {
			Directions[ghostRow + 1][ghostCol] = Direction.DOWN;
			SquareList.addLast(new BoardCoordinate(ghostRow + 1, ghostCol, false));
		}
		if (ghostRow == 11 && ghostCol == 22) { // Accounting for the tunnel edge case
			Directions[11][0] = Direction.RIGHT;
			SquareList.addLast(new BoardCoordinate(11, 0, false));
		} else if (_board[ghostRow][ghostCol + 1].isWall() == false
				&& this.getDirection().getOpposite() != Direction.RIGHT) {
			Directions[ghostRow][ghostCol + 1] = Direction.RIGHT;
			SquareList.addLast(new BoardCoordinate(ghostRow, ghostCol + 1, false));
		}
		if (ghostRow == 11 && ghostCol == 0) { // Accounting for the tunnel edge case 
			Directions[11][22] = Direction.LEFT;
			SquareList.addLast(new BoardCoordinate(11, 22, false));
		} else if (_board[ghostRow][ghostCol - 1].isWall() == false
				&& this.getDirection().getOpposite() != Direction.LEFT) {
			Directions[ghostRow][ghostCol - 1] = Direction.LEFT;
			SquareList.addLast(new BoardCoordinate(ghostRow, ghostCol - 1, false));
		}

		double minDistance = Constants.LIMIT; // storing huge distance
		Direction closestDirection = Directions[ghostRow][ghostCol]; // initializing the variable that will keep track of direction at closest square 
		while (SquareList.isEmpty() != true && minDistance != 0) {
			_current = SquareList.removeFirst(); // dequeue
			int newRow = _current.getRow();
			int newCol = _current.getColumn();
			double distance = this.calculateDistance(newRow, newCol, targetRow, targetCol); // calculates shortest distance using pythagorean theorem 
			ArrayList<BoardCoordinate> validNeighbors = this.enqueueNeighbors(newRow, newCol, closestDirection, Directions); // storing extended valid neighbors in ArrayList
			// See enqueue neighbors method for more specifics 
			if (distance < minDistance) {
				minDistance = distance;
				closestDirection = Directions[newRow][newCol]; // these two statements update the closest distance and the direction that will
				// take us to the target fastest 
			}
			for (int i = 0; i < validNeighbors.size(); i++) {
				SquareList.addLast(validNeighbors.get(i)); // enqueue valid neighbors 
			}
			_direction = closestDirection; // storing the direction as an instance variable to be returned outside the while loop 
		}
		return _direction;
	}

		// This method models the pythagorean theorem
	private double calculateDistance(int row, int col, int targetRow, int targetCol) {
		double yDistance = Math.abs(targetRow - row);
		double xDistance = Math.abs(targetCol - col);
		double ySquared = Math.pow(yDistance, Constants.SQUARED);
		double xSquared = Math.pow(xDistance, Constants.SQUARED);
		double distance = Math.sqrt(ySquared + xSquared);
		return distance;
	}

		/** This is a helper method factoring out the process of enqueueing valid neighbors. It follows similar process to the 
		 * way in which I added the ghost's primary neighbors. This method, however, takes in the current direction that the previous
		 * steps of BFS has determined and passes up that direction continuously throughout the entire board. This is essential to ensure
		 * that all squares are assigned absolutely and not relative to the direction of the neighbors' neighbor. This method 
		 * returns an array list of valid neighbors that are added to the LinkedList of squares back in BFS. 
		 */
	private ArrayList<BoardCoordinate> enqueueNeighbors(int newRow, int newCol, Direction closestDirection, Direction[][] Directions) {
		ArrayList<BoardCoordinate> validNeighbors = new ArrayList<BoardCoordinate>();
		switch (closestDirection) {
		case UP:
			if (_board[newRow - 1][newCol].isWall() == false && Directions[newRow - 1][newCol] != null) {
				validNeighbors.add(new BoardCoordinate(newRow - 1, newCol, false));
				Directions[newRow - 1][newCol] = Directions[newRow][newCol];
			}
			break;
		case DOWN:
			if (_board[newRow + 1][newCol].isWall() == false && Directions[newRow + 1][newCol] != null) {
				validNeighbors.add(new BoardCoordinate(newRow + 1, newCol, false));
				Directions[newRow + 1][newCol] = Directions[newRow][newCol];
			}
			break;
		case RIGHT:
			if (newRow == 11 && newCol == 22 && Directions[11][22] != null) { // tunnel edge case 
				validNeighbors.add(new BoardCoordinate(11, 0, false));
				Directions[11][0] = Direction.RIGHT;
				Directions[11][21] = Direction.RIGHT; // to prevent the ghost from going back and forth between tunnel entrances forever
			} else if (_board[newRow][newCol + 1].isWall() == false && Directions[newRow][newCol + 1] != null) {
				validNeighbors.add(new BoardCoordinate(newRow, newCol + 1, false));
				Directions[newRow][newCol + 1] = Directions[newRow][newCol];
			}
			break;
		case LEFT:
			if (newRow == 11 && newCol == 0 && Directions[11][0] != null) {
				validNeighbors.add(new BoardCoordinate(11, 22, false));
				Directions[11][22] = Direction.LEFT;
				Directions[11][1] = Direction.LEFT; // // to prevent the ghost from going back and forth between tunnel entrances forever
			} else if (_board[newRow][newCol - 1].isWall() == false & Directions[newRow][newCol - 1] != null) {
				validNeighbors.add(new BoardCoordinate(newRow, newCol - 1, false));
				Directions[newRow][newCol - 1] = Directions[newRow][newCol];
			}
			break;
		}
		return validNeighbors;
	}

	// Setter for x location
	public void setX(double x) {
		_ghost.setX(x);
	}
	// Setter for y location 
	public void setY(double y) {
		_ghost.setY(y);
	}
	// Setter for both locations
	public void setLocation(double x, double y) {
		_ghost.setY(y);
		_ghost.setX(x);
	}
	// Getter for x location
	public double getX() {
		return _ghost.getX();
	}
	// Getter for y location 
	public double getY() {
		return _ghost.getY();
	}
	// The ghosts should collide with pacman in one of two ways. If they are not in frightened mode, they should set Pacman to his starting location
	// The location of the ghosts are also reset, but they are reset to unique locations depending on the instance of the ghost and thus I chose to 
	// handle that logical and graphical reset in the Game class. However, if they are in frightened mode and collided(eaten by pacman), they return to the ghost
	// pen
	@Override
	public int collide(Pacman pacman, Game game) {
		if (game.isFrightened() == false) {
			pacman.setY(Constants.PACMAN_STARTING_Y + Constants.CIRCLE_CENTER_Y);
			pacman.setX(Constants.PACMAN_STARTING_X + Constants.CIRCLE_CENTER_X);
		}
		else {
			_ghost.setY(Constants.GHOST_ROW * Constants.SIDE_LENGTH);
			_ghost.setX(Constants.GHOST_COL * Constants.SIDE_LENGTH);
		}
		return 1;
	}

	@Override
	public int updateScore(int score, Game game) {
		if (game.isFrightened() == true) {
			return score += 200;// the score that ghosts should give when eaten
		}
		return score += 0; // if not in frightened, should not increase score
	}

	@Override
	public int updateGameOverCounter(int gameOverCounter) {
		return gameOverCounter += 0; // gameOverCounter checks for energizers and dots, and ghosts can be eaten infinitely many times 
	}
}
