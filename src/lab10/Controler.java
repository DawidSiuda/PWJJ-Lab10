package lab10;

import java.io.File;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;

public class Controler {
	@FXML
	public Pane mainPane;

	@FXML
	public CheckBox checkBoxPlayComputer;

	@FXML
	public ChoiceBox<String> choiceBoxSelectStrategy;

	private final int sizeX = 5;
	private final int sizeY = 5;
	private final int signNumberToWin = 4;
	private final int rectangleSize = 80;
	private final int BOARD_POSSITION_X = 110;
	private final int BOARD_POSSITION_Y = 130;

	private enum Player {
		PLAYER_X, PLAYER_O
	}

	private RectangleWrapper[][] gameBoard;
	private AnchorPane anchorPaneGameBoard;
	private Player currentPlayer;

	List<JavaScriptStrategy> ListJavaScriptStrategy = new ArrayList<>();

	public Controler() {
		System.out.println("FUNCTION: constructor");
		System.out.println("Working Directory = " + System.getProperty("user.dir"));
	}

	/**
	 * Initialize FX controllers.
	 */
	public void initialize() {
		System.out.println("FUNCTION: initialize");

		//
		// Load JavaScript.
		//

		File directoryPath = new File("./Scripts");
		for(String file : directoryPath.list()) {
			ListJavaScriptStrategy.add(new JavaScriptStrategy("./Scripts/" + file));
		}

		//
		// Set controls.
		//
		checkBoxPlayComputer.setSelected(true);

		List<String> strategiesAsString = new ArrayList<>();

		strategiesAsString.add("Java random");
		strategiesAsString.add("C++ strategy 1");
		strategiesAsString.add("C++ strategy 2");

		for(JavaScriptStrategy jss : ListJavaScriptStrategy) {
			strategiesAsString.add(jss.toString());
		}

		ObservableList<String> strategies = FXCollections.observableArrayList(strategiesAsString);

		choiceBoxSelectStrategy.setItems(strategies);

		choiceBoxSelectStrategy.getSelectionModel().selectFirst();

		//
		// Generate game board.
		//
		currentPlayer = Player.PLAYER_X;

		anchorPaneGameBoard = new AnchorPane();

		gameBoard = new RectangleWrapper[sizeX][sizeY];

		for (int y = 0; y < sizeY; y++) {
			for (int x = 0; x < sizeX; x++) {
				RectangleWrapper rectangle = new RectangleWrapper(x * rectangleSize, y * rectangleSize, rectangleSize,
						rectangleSize);

				rectangle.setFill(Color.TRANSPARENT);
				rectangle.setStroke(Color.BLACK);
				rectangle.setOnMouseClicked(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent t) {
						Boolean endGame = false;
						if (rectangle.getCurrentSign() == Sign.NONE) {
							if (currentPlayer == Player.PLAYER_O) {
								Image image = new Image("./lab10/o.png");
								ImagePattern imagePattern = new ImagePattern(image);
								rectangle.setFill(imagePattern);
								rectangle.setCurrentSign(Sign.O);
								endGame = checkWinner(Sign.O);
							} else {
								Image image = new Image("./lab10/x.jpg");
								ImagePattern imagePattern = new ImagePattern(image);
								rectangle.setFill(imagePattern);
								rectangle.setCurrentSign(Sign.X);
								endGame = checkWinner(Sign.X);
							}

							if (endGame == true) {
								MyMessage.show("Win");
								cleanGameBoard();
								initialize();
								return;
							}
							changePlayer();
							callTheNextPlayerMove();
						}
					}
				});

				gameBoard[x][y] = rectangle;

				anchorPaneGameBoard.getChildren().add(gameBoard[x][y]);
			}
		}

		anchorPaneGameBoard.setLayoutX(BOARD_POSSITION_X);
		anchorPaneGameBoard.setLayoutY(BOARD_POSSITION_Y);
		mainPane.getChildren().add(anchorPaneGameBoard);
	}

	public void buttonReloadClicked() {
		ListJavaScriptStrategy.clear();

		File directoryPath = new File("./Scripts");
		for(String file : directoryPath.list()) {
			ListJavaScriptStrategy.add(new JavaScriptStrategy("./Scripts/" + file));
		}

		List<String> strategiesAsString = new ArrayList<>();

		strategiesAsString.add("Java random");
		strategiesAsString.add("C++ strategy 1");
		strategiesAsString.add("C++ strategy 2");

		for(JavaScriptStrategy jss : ListJavaScriptStrategy) {
			strategiesAsString.add(jss.toString());
		}

		ObservableList<String> strategies = FXCollections.observableArrayList(strategiesAsString);

		choiceBoxSelectStrategy.setItems(strategies);

		choiceBoxSelectStrategy.getSelectionModel().selectFirst();


	}
	private Boolean checkWinner(Sign sign) {
		//
		// Vertical.
		//

		for (int x = 0; x < sizeX; x++) {
			int counter = 0;
			for (int y = 0; y < sizeY; y++) {
				if (gameBoard[x][y].getCurrentSign() == sign)
					counter++;
				else
					counter = 0;
				if (counter == signNumberToWin)
					return true;
			}
		}

		//
		// Horizontal.
		//

		for (int y = 0; y < sizeY; y++) {
			int counter = 0;
			for (int x = 0; x < sizeX; x++) {
				if (gameBoard[x][y].getCurrentSign() == sign)
					counter++;
				else
					counter = 0;

				if (counter == signNumberToWin)
					return true;
			}
		}

		//
		// Across top left - down right.
		//
		for (int startY = 0; startY < sizeY; startY++) {
			for (int startX = 0; startX < sizeX; startX++) {
				int counter = 0;
				if (gameBoard[startX][startY].getCurrentSign() == sign)
					counter++;
				else
					continue;

				int y = startY + 1;
				int x = startX + 1;

				for (; y < sizeY && x < sizeX; x++, y++) {
					if (gameBoard[x][y].getCurrentSign() == sign)
						counter++;
					else
						counter = 0;
					if (counter == signNumberToWin)
						return true;
				}
			}
		}

		//
		// Across top right - down left.
		//
		for (int startY = sizeY - 1; startY >= 0; startY--) {
			for (int startX = 0; startX < sizeX; startX++) {
				int counter = 0;
				if (gameBoard[startX][startY].getCurrentSign() == sign)
					counter++;
				else
					continue;

				int y = startY - 1;
				int x = startX + 1;

				for (; y >= 0 && x < sizeX; y--, x++) {
					if (gameBoard[x][y].getCurrentSign() == sign)
						counter++;
					else
						counter = 0;
					if (counter == signNumberToWin)
						return true;
				}
			}
		}
		return false;
	}

	private void makeMove(int x, int y) {
		RectangleWrapper rectangle = gameBoard[x][y];
		Boolean endGame = false;
		if (rectangle.getCurrentSign() == Sign.NONE) {
			if (currentPlayer == Player.PLAYER_O) {
				Image image = new Image("./lab10/o.png");
				ImagePattern imagePattern = new ImagePattern(image);
				rectangle.setFill(imagePattern);
				rectangle.setCurrentSign(Sign.O);
				endGame = checkWinner(Sign.O);
			} else {
				Image image = new Image("./lab10/x.jpg");
				ImagePattern imagePattern = new ImagePattern(image);
				rectangle.setFill(imagePattern);
				rectangle.setCurrentSign(Sign.X);
				endGame = checkWinner(Sign.X);
			}

			if (endGame == true) {
				MyMessage.show("Win");
				cleanGameBoard();
				initialize();
				return;
			}
			changePlayer();
		}
		return;
	}

	private void doNextComputerMove() {
		String str = choiceBoxSelectStrategy.getSelectionModel().getSelectedItem();

		if (str.equals("Java random")) {
			Random rand = new Random();
			while (true) {
				int x = rand.nextInt(sizeX);
				int y = rand.nextInt(sizeY);
				if (gameBoard[x][y].getCurrentSign() == Sign.NONE) {
					makeMove(x, y);
					break;
				}
			}
			return;
		}

		if (str.equals("C++ strategy 1")) {
			Coordinates cooridnates = Java10Strategy1.getMove(gameBoard, sizeX, sizeY, 1);
			makeMove(cooridnates.x, cooridnates.y);
			return;
		}

		if (str.equals("C++ strategy 2")) {
			Coordinates cooridnates = Java10Strategy2.getMove(gameBoard, sizeX, sizeY, 1);
			makeMove(cooridnates.x, cooridnates.y);
			return;
		}

		for(JavaScriptStrategy jss : ListJavaScriptStrategy) {
			if (str.equals(jss.toString())) {
				Coordinates cooridnates = jss.getMove(gameBoard, sizeX, sizeY, 1);
				makeMove(cooridnates.x, cooridnates.y);
				return;
			}
		}

	}

	private void callTheNextPlayerMove() {

		//
		// Check if any pool is still not set.
		//
		if (CheckIfUnsignPoolExist() == false) {
			MyMessage.show("End game.");
			cleanGameBoard();
			initialize();
			return;
		}

		//
		// Generate move if play with computer.
		//
		if (currentPlayer != Player.PLAYER_O) {
			return;
		}

		if (checkBoxPlayComputer.isSelected() == true) {
			doNextComputerMove();
		}
	}

	private Boolean CheckIfUnsignPoolExist() {
		for (int x = 0; x < sizeX; x++) {
			for (int y = 0; y < sizeY; y++) {
				if (gameBoard[x][y].getCurrentSign() == Sign.NONE)
					return true;
			}
		}

		return false;
	}

	private void cleanGameBoard() {
		mainPane.getChildren().remove(anchorPaneGameBoard);
	}

	void changePlayer() {
		if (currentPlayer == Player.PLAYER_O)
			currentPlayer = Player.PLAYER_X;
		else
			currentPlayer = Player.PLAYER_O;
	}

}