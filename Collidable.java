package Pacman;

/** This is my collidable interface, which contained methods used throughout my other classes that implement collidable
 *  All collidable objects collide with pacman uniquely, update the score uniquely, and if it is a dot or energizer, updates
 *  the game over counter. 
 */
public interface Collidable {
	public int collide(Pacman pacman, Game game); // Pass in the game class and Pacman so that ghosts can automatically update pacman's location and 
	// check if the game is in frightened mode. 
	public int updateScore (int score, Game game);
	public int updateGameOverCounter(int gameOverCounter);
}
