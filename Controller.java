package com.SoumyadevSanyal.connectfour;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.net.URL;
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

	@FXML
	public GridPane rootGridPane;

	@FXML
	public Pane insertedDiscsPane, menuPane;

	@FXML
	public Label playerNameLabel;

	public void createPlayground() {

		Shape rectangleWithHoles = createGameStructureGrid();

		rootGridPane.add(rectangleWithHoles, 0, 1);
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
