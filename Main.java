package com.SoumyadevSanyal.connectfour;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import sun.plugin2.applet.FXAppletSecurityManager;

public class Main extends Application {

	private Controller controller;

	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("game.fxml"));
		GridPane rootGridPane = loader.load();

		controller = loader.getController();
		controller.createPlayground();

		MenuBar menuBar = createMenu();
		menuBar.prefWidthProperty().bind(primaryStage.widthProperty());// to make the menuBar take the width of the primaryStage

		Pane menuPane = (Pane) rootGridPane.getChildren().get(0);

		menuPane.getChildren().addAll(menuBar);

		Scene scene = new Scene(rootGridPane);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Connect 4");
		primaryStage.setResizable(false);
		primaryStage.show();
	}

	private MenuBar createMenu() {
		// File menu
		Menu fileMenu = new Menu("File");

		MenuItem newGame = new MenuItem("New Game");
		newGame.setOnAction(event -> resetGame());

		MenuItem resetGame = new MenuItem("Reset Game");
		resetGame.setOnAction(event -> controller.resetGame());

		SeparatorMenuItem separatorMenuItem = new SeparatorMenuItem();
		MenuItem exitGame = new MenuItem("Exit");
		exitGame.setOnAction(event -> {
			exitGame();
		});

		fileMenu.getItems().addAll(newGame, resetGame, separatorMenuItem, exitGame);

		// Help Menu
		Menu helpMenu = new Menu("Help");

		MenuItem aboutGame = new MenuItem("About Game");
		aboutGame.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				aboutGame();
			}
		});


		MenuItem aboutDeveloper = new MenuItem("About Developer");
		aboutDeveloper.setOnAction(event -> aboutDeveloper());


		SeparatorMenuItem separatorMenuItem1 = new SeparatorMenuItem();

		helpMenu.getItems().addAll(aboutGame, separatorMenuItem1, aboutDeveloper);

		MenuBar menuBar = new MenuBar();
		menuBar.getMenus().addAll(fileMenu, helpMenu);

		return menuBar;
	}

    private void aboutDeveloper() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About the Developer");
        alert.setHeaderText("Soumyadev Sanyal");
        alert.setContentText("Hi! My name is Soumyadev Sanyal." +
                " I am an engineer specialised in Computer Science Engineering." +
                " This game is my first project in JavaFX application." +
                " I made it when I was undergoing a core java training from Internshala." +
                " Thanks for reading.");
        alert.show();
    }

    private void aboutGame() {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("About the game");
		alert.setHeaderText("How to play");
		alert.setContentText("Connect Four is a two-player connection game in which the players first choose a color and then take turns dropping colored discs from the top into a seven-column, six-row vertically suspended grid. The pieces fall straight down, occupying the next available space within the column. The objective of the game is to be the first to form a horizontal, vertical, or diagonal line of four of one's own discs. Connect Four is a solved game. The first player can always win by playing the right moves.");
		alert.show();
	}

	private void exitGame() {
		Platform.exit();
		System.exit(0);
	}

	private void resetGame() {
		controller.playerOneLabel.clear();
		controller.playerTwoLabel.clear();

		controller.playerOneName = "Player One";
		controller.playerTwoName = "Player Two";
		controller.resetGame();
	}


	public static void main(String[] args) {
		launch(args);
	}
}
