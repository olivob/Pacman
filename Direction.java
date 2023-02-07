package Pacman;

// This is my enum class representing various directions that ghost and pacman can move. 
public enum Direction {
	UP, LEFT, RIGHT, DOWN;

	public Direction getOpposite() {
		switch (this) {
		case UP:
			return DOWN;
		case DOWN:
			return UP;
		case LEFT:
			return RIGHT;
		case RIGHT:
			return LEFT;
		default:
			return null;
		}
	}
}