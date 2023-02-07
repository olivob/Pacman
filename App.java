package Pacman;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;

/**
  * Hello! Welcome to my Pacman final project! Each class is commented
  * thoroughly. Please take a look at my containment diagram and README
  * prior to looking through my code to get a better picture of my
  * thought process and decision making.
  */

public class App extends Application {

    // Below I instantiate my top level object, set the stage, and show
    // the scene
    @Override
    public void start(Stage stage) {
    	PaneOrganizer organizer = new PaneOrganizer();
    	Scene scene = new Scene(organizer.getRoot(), Constants.SCENE_WIDTH, Constants.SCENE_HEIGHT);
    	stage.setScene(scene);
    	stage.setTitle("Pacman");
    	stage.show();
    }

    /*
    * Here is the mainline! No need to change this.
    */
    public static void main(String[] argv) {
        // launch is a method inherited from Application
        launch(argv);
    }
}
