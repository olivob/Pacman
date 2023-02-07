package Pacman;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

/**
 * My top level graphical object, responsible for instantiating the instance of
 * game that the user plays. I stylistically set the background color of the
 * BorderPane to be black, modeled after the demo and real Pacman. This class
 * primarily sets up the label for lives, score, and the quit button 
 */
public class PaneOrganizer {
	private BorderPane _root;
	private SideBar _sideBar;
	private Label _sideLabel;
	private Label _lives;

	public PaneOrganizer() {
		_root = new BorderPane();
		_root.setStyle("-fx-background-color: black;");
		_sideBar = new SideBar();
		Game myGame = new Game(_sideBar); // pass in a sideBar since it is technically a higher level class than my Game 
		_root.setCenter(myGame.getGamePane());
		this.setupBottom();
	}

	// Getter method that returns root pane, used in App
	public BorderPane getRoot() {
		return _root;
	}

	// Sets up the bottom of the root pane, which is an HBox that contains
	// the labels keeping track of the lives and the score, as well as the
	// button that quits the game.
	
	private void setupBottom() {
		_sideLabel = _sideBar.getLabel();
		_lives = _sideBar.getLifeLabel();
		Button quitBtn = new Button("Quit");
		quitBtn.setFocusTraversable(false);
		HBox labelPane = new HBox();
		labelPane.getChildren().addAll(_sideLabel, _lives, quitBtn);
		_root.setBottom(labelPane);
		labelPane.setAlignment(Pos.CENTER);
		labelPane.setSpacing(Constants.SPACING);
		quitBtn.setOnAction(new QuitHandler());
	}
	// Quits the game
	private class QuitHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			System.exit(0);
		}
	}
}
