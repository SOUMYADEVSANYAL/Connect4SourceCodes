package com.SoumyadevSanyal.connectfour;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Controller implements Initializable {

	//Rules of Connect4
	private static final int COLUMNS = 7;
	private static final int ROWS = 6;
	private static final int CIRCLE_DIAMETER = 80;

	private static final String DISC_COLOR1 = "#24303E";
	private static final String DISC_COLOR2 = "#4CAA88";

	public static String playerOneName = "Player One";
	public static String playerTwoName = "Player Two";

	private boolean isPlayerOneTurn = true;

	private Disc[][] insertedDiscsArray = new Disc[ROWS][COLUMNS]; // for structural use only: for the developers

	@FXML
	public GridPane rootGridPane;

	@FXML
	public Pane insertedDiscsPane, menuPane;

	@FXML
	public Label playerNameLabel;

	@FXML
	public TextField playerOneLabel, playerTwoLabel;

	@FXML
	public Button setNameButton;

	private boolean isAllowedToInsert = true;

	public void createPlayground() {

		Shape rectangleWithHoles = createGameStructureGrid();

		rootGridPane.add(rectangleWithHoles, 0, 1);

		List<Rectangle> rectangleList = createClickableRectangle();


		for (Rectangle rectangle :
				rectangleList) {
			rootGridPane.add(rectangle, 0, 1);
		}

	}

	private List<Rectangle> createClickableRectangle() {

		List<Rectangle> rectangleList = new ArrayList<>();

		for (int i = 0; i < COLUMNS; i++) {
			Rectangle rectangle = new Rectangle(CIRCLE_DIAMETER, (ROWS) * CIRCLE_DIAMETER);
			rectangle.setFill(Color.TRANSPARENT);
			rectangle.setTranslateX(CIRCLE_DIAMETER * i);

			//hover effect
			rectangle.setOnMouseEntered(event -> {
				rectangle.setFill(Color.valueOf("#eeeeee26"));
			});

			rectangle.setOnMouseExited(event -> {
				rectangle.setFill(Color.TRANSPARENT);
			});

			final int column = i;
			rectangle.setOnMouseClicked(event -> {
				if (isAllowedToInsert){
					isAllowedToInsert = false;
					insertDisc(new Disc(isPlayerOneTurn), column);
				}

			});

			rectangleList.add(rectangle);
		}

		return rectangleList;
	}

	private void insertDisc(Disc disc, int column) {

		int row = ROWS - 1;
		while (row >= 0) {
			if (getDiscIfPresent(row, column) == null)
				break;
			row--;
		}

		if (row < 0) // The column is full do nothing
			return;

		insertedDiscsArray[row][column] = disc; // for structural use only
		insertedDiscsPane.getChildren().add(disc);
//		disc.setCenterX(CIRCLE_DIAMETER * column + CIRCLE_DIAMETER/2);
		disc.setTranslateX(column * CIRCLE_DIAMETER);

		int current_row = row;
		TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.5), disc);

//		disc.setCenterY(5*CIRCLE_DIAMETER+ CIRCLE_DIAMETER/2);
		translateTransition.setToY(row * CIRCLE_DIAMETER);
		translateTransition.setOnFinished(event -> {

			isAllowedToInsert = true;
			if (gameEnded(current_row, column)) {
				gameOver();
			}
			isPlayerOneTurn = !isPlayerOneTurn;
			playerNameLabel.setText(isPlayerOneTurn ? playerOneName : playerTwoName);
		});
		translateTransition.play();

	}

	private void gameOver() {
		String winner = isPlayerOneTurn ? playerOneName : playerTwoName;
		System.out.println("The winner is " + winner);

		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Connect4");
		alert.setHeaderText("The winner is " + winner);
		alert.setContentText("Wanna play again?");

		ButtonType yesButton = new ButtonType("Sure");
		ButtonType noButton = new ButtonType("No, thanks!!");

		alert.getButtonTypes().setAll(yesButton, noButton);

		Platform.runLater(() -> {
			Optional<ButtonType> btnClicked = alert.showAndWait();
			if (btnClicked.isPresent() && btnClicked.get() == yesButton){
				resetGame();
			}else {
				Platform.exit();
				System.exit(0);
			}
		});

	}

	public void resetGame() {
		insertedDiscsPane.getChildren().clear();

		for (int row = 0; row < insertedDiscsArray.length; row++) {
			for (int col = 0; col < insertedDiscsArray[row].length; col++) {
				insertedDiscsArray[row][col] = null;
			}
		}
		isPlayerOneTurn = true;
		playerNameLabel.setText(playerOneName);

		createPlayground();

	}

	private boolean gameEnded(int current_row, int current_column) {
		List<Point2D> vertialPoints = IntStream.rangeClosed(current_row - 3, current_row + 3)
				.mapToObj(r -> new Point2D(r, current_column)) //making the Point2D objects with variable rows and fixed column
				.collect(Collectors.toList()); // collecting them in a list.

		List<Point2D> horizontalPoints = IntStream.rangeClosed(current_column - 3, current_column + 3)
				.mapToObj(cols -> new Point2D(current_row, cols))////making the Point2D objects with fixed row and variable column
				.collect(Collectors.toList());// collecting them in a list.

		Point2D startPoint1 = new Point2D(current_row - 3, current_column + 3);
		List<Point2D> diagonal1Points = IntStream.rangeClosed(0, 6)
				.mapToObj(i -> new Point2D(startPoint1.getX() + i, startPoint1.getY() - i))
				.collect(Collectors.toList());

		Point2D startPoint2 = new Point2D(current_row - 3, current_column - 3);
		List<Point2D> diagonal2Points = IntStream.rangeClosed(0, 6)
				.mapToObj(i -> startPoint2.add(i, i)) // new "Point2D(startPoint1.getX() + i, startPoint1.getY() - i)" by add method
				.collect(Collectors.toList());

		boolean isEnded = checkCombinations(vertialPoints) || checkCombinations(horizontalPoints)
				|| checkCombinations(diagonal1Points) || checkCombinations(diagonal2Points);

		return isEnded;
	}

	private boolean checkCombinations(List<Point2D> points) {

		int chain = 0;
		for (Point2D point :
				points) {
			int rowIndex = (int) point.getX();
			int columnIndex = (int) point.getY();

			Disc disc = getDiscIfPresent(rowIndex, columnIndex);

			if (disc != null && disc.isPlayerOneMove == isPlayerOneTurn) {
				chain++;
				if (chain == 4)
					return true;
			} else {
				chain = 0;
			}

		}
		return false;

	}

	private Disc getDiscIfPresent(int row, int column) {
		if (row >= ROWS || row < 0 || column >= COLUMNS || column < 0)
			return null;
		return insertedDiscsArray[row][column];

	}

	private static class Disc extends Circle {
		private final boolean isPlayerOneMove;

		public Disc(boolean isPlayerOneMove) {
			this.isPlayerOneMove = isPlayerOneMove;
			setRadius(CIRCLE_DIAMETER / 2);
			setFill(isPlayerOneMove ? Color.valueOf(DISC_COLOR1) : Color.valueOf(DISC_COLOR2));
			setCenterX(CIRCLE_DIAMETER / 2);
			setCenterY(CIRCLE_DIAMETER / 2);
			setSmooth(true);
		}
	}

	private Shape createGameStructureGrid() { // This method is creating the white rectangle with blue holes by
		//subtracting the cirlce from the rectangle
		Shape rectangleWithHoles = new Rectangle((COLUMNS) * CIRCLE_DIAMETER, (ROWS) * CIRCLE_DIAMETER);

		// Setting the center co-ordinates with out the setTranslate method
		int k = 1;
		int l = 1;

		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLUMNS; j++) {
				Circle circle = new Circle();
				circle.setRadius(CIRCLE_DIAMETER / 2);
				circle.setCenterX(CIRCLE_DIAMETER / 2 * k);
				circle.setCenterY(CIRCLE_DIAMETER / 2 * l);
				circle.setSmooth(true);

				// Setting co-ordinates of centers using setTranslate method
//				circle.setTranslateX(i*(CIRCLE_DIAMETER+5));
//				circle.setTranslateY(j*(CIRCLE_DIAMETER+5));

				rectangleWithHoles = Shape.subtract(rectangleWithHoles, circle);
				k = k + 2;
			}
			l = l + 2;
			k = 1;
		}

		rectangleWithHoles.setFill(Color.WHITE);

		return rectangleWithHoles;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		setNameButton.setOnAction(event -> {

			playerOneName = playerOneLabel.getText();
			playerTwoName = playerTwoLabel.getText();
			if(playerOneName != "" && playerTwoName != "")
				playerNameLabel.setText(playerOneName);
		});

	}
}
