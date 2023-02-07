package Pacman;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/** 
 * I chose to have a wall class to have code that factors out the graphical components of a wall
 * These graphical components involve the size of a wall, the color, and the location
 */
public class Wall { 
	private Rectangle _wall;
	public Wall(Pane pane){
		_wall = new Rectangle();
		_wall.setHeight(Constants.SIDE_LENGTH);
		_wall.setWidth(Constants.SIDE_LENGTH);
		_wall.setFill(Color.BLUE);
		pane.getChildren().add(_wall);
	}
	
	public void setLocation(double x, double y) {
		_wall.setY(y);
		_wall.setX(x);
	}
}
