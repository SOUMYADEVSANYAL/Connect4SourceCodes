package com.SoumyadevSanyal.connectfour;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
import java.util.ResourceBundle;

public class Controller implements Initializable {

	//Rules of Connect4
	private static final int COLUMNS = 7;
	private static final int ROWS = 6;
	private static final int CIRCLE_DIAMETER = 80;

	private static final String DISC_COLOR1 = "#24303E";
	private static final String DISC_COLOR2 = "#4CAA88";

	private static String playerOneName = "Player One";
	private static String playerTwoName = "Player Two";

	private boolean isPlayerOneTurn = true;

	private Disc[][] insertedDiscsArray = new Disc[ROWS][COLUMNS]; // for structural use only: for the developers

	@FXML
	public GridPane rootGridPane;

	@FXML
	public Pane insertedDiscsPane, menuPane;

	@FXML
	public Label playerNameLabel;

	public void createPlayground() {

		Shape rectangleWithHoles = createGameStructureGrid();

		rootGridPane.add(rectangleWithHoles, 0, 1);

		List<Rectangle> rectangleList = createClickableRectangle();


		for (Rectangle rectangle:
		     rectangleList) {
			rootGridPane.add(rectangle, 0, 1);
		}

	}

	private List<Rectangle> createClickableRectangle() {

		List<Rectangle> rectangleList = new ArrayList<>();

		for (int i = 0; i < COLUMNS; i++) {
			Rectangle rectangle = new Rectangle(CIRCLE_DIAMETER, (ROWS)*CIRCLE_DIAMETER );
			rectangle.setFill(Color.TRANSPARENT);
			rectangle.setTranslateX(CIRCLE_DIAMETER*i);

			//hover effect
			rectangle.setOnMouseEntered(event -> {
				rectangle.setFill(Color.valueOf("#eeeeee26"));
			});

			rectangle.setOnMouseExited(event -> {
				rectangle.setFill(Color.TRANSPARENT);
			});

			final int column = i;
			rectangle.setOnMouseClicked(event -> {
				insertDisc(new Disc(isPlayerOneTurn), column);
			});

			rectangleList.add(rectangle);
		}

		return rectangleList;
	}
	private void insertDisc(Disc disc, int column){

		int row = ROWS-1;
		while (row > 0){
			if(insertedDiscsArray[row][column] == null)
				break;
			row--;
		}

		if (row < 0) // The column is full do nothing
			return;

		insertedDiscsArray[row][column] = disc; // for structural use only
		insertedDiscsPane.getChildren().add(disc);
//		disc.setCenterX(CIRCLE_DIAMETER * column + CIRCLE_DIAMETER/2);
		disc.setTranslateX(column*CIRCLE_DIAMETER);

		int current_row = row;
		TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.5), disc);

//		disc.setCenterY(5*CIRCLE_DIAMETER+ CIRCLE_DIAMETER/2);
		translateTransition.setToY(row*CIRCLE_DIAMETER);
		translateTransition.setOnFinished(event -> {
			if (gameEnded(current_row, column)){

			}
			isPlayerOneTurn = !isPlayerOneTurn;
			playerNameLabel.setText(isPlayerOneTurn? playerOneName : playerTwoName);
		});
		translateTransition.play();

	}

	private boolean gameEnded(int current_row, int column) {
		return true;
	}

	private static class Disc extends Circle{
		private final boolean isPlayerOneMove;
		public Disc(boolean isPlayerOneMove){
			this.isPlayerOneMove = isPlayerOneMove;
			setRadius(CIRCLE_DIAMETER/2);
			setFill(isPlayerOneMove? Color.valueOf(DISC_COLOR1):Color.valueOf(DISC_COLOR2));
			setCenterX(CIRCLE_DIAMETER/2);
			setCenterY(CIRCLE_DIAMETER/2);
		}
	}

	private Shape createGameStructureGrid() { // This method is creating the white rectangle with blue holes by
		                                      //subtracting the cirlce from the rectangle
		Shape rectangleWithHoles = new Rectangle((COLUMNS)*CIRCLE_DIAMETER, (ROWS)*CIRCLE_DIAMETER);

		// Setting the center co-ordinates with out the setTranslate method
		int k = 1;
		int l = 1;

		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLUMNS; j++) {
				Circle circle = new Circle();
				circle.setRadius(CIRCLE_DIAMETER/2);
				circle.setCenterX(CIRCLE_DIAMETER/2*k);
				circle.setCenterY(CIRCLE_DIAMETER/2*l);

				// Setting co-ordinates of centers using setTranslate method
//				circle.setTranslateX(i*(CIRCLE_DIAMETER+5));
//				circle.setTranslateY(j*(CIRCLE_DIAMETER+5));

				rectangleWithHoles = Shape.subtract(rectangleWithHoles, circle);
				k = k+2;
			}
			l=l+2;
			k = 1;
		}

		rectangleWithHoles.setFill(Color.WHITE);

		return rectangleWithHoles;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}
}
