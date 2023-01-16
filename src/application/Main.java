package application;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.application.Application;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Circle;
import javafx.scene.control.Button;
import javafx.scene.shape.Polygon;

public class Main extends Application
{	
	private Game currentGame = new Game();
	
	private Button newGameButton = new Button("New Game");
	private Button loadGameButton = new Button("Load Game");
	//private Button saveGameButton = new Button("Save Game");
	private Scene scene;
	private Group boardGroup = new Group();
	private Pane root = new Pane();
	private Button redStarts = new Button("Red Starts");
	private Button blackStarts = new Button("Black Starts");
	private Stage secondaryStage = new Stage();
	
	private Circle selectedCircle = null;
	private HashMap<String, Rectangle> rectangleFinder = new HashMap<>();
	private HashMap<String, Circle> circleFinder = new HashMap<>();
	private Rectangle[] rectangles = new Rectangle[32];
	private Circle[] circles;
	private double anchorX;
	private double anchorY;
	private ArrayList<Rectangle> highlightedSquares = null;
	private ArrayList<ArrayList<Integer>> moves = new ArrayList<>();
	
	@Override
	public void start(Stage stage) 
	{
		Rectangle board = new Rectangle(80, 80, 640, 640);
		board.setFill(Color.DARKKHAKI);
		board.setStyle("-fx-stroke: yellow; -fx-stroke-width: 2;");
		boardGroup.getChildren().addAll(board);
		int x = 160;
		for(int i = 0; i < 8; i++) 
		{
			if(i % 2 == 1)
				x = 80;
			else
				x = 160;
			for(int j = 0; j < 4; j++) 
			{
				rectangles[i * 4 + j] = new Rectangle(x + j * 160, 80 * (i + 1), 79, 79);
				rectangles[i * 4 + j].setFill(Color.BROWN);
				rectangles[i * 4 + j].setStyle("-fx-stroke: yellow; -fx-stroke-width: 2;");
				rectangleFinder.put("" + j + i, rectangles[i * 4 + j]);
				boardGroup.getChildren().add(rectangles[i * 4 + j]);
			}
		}
		HBox buttons = new HBox(10, newGameButton, loadGameButton);
		buttons.setLayoutX(320);
		buttons.setLayoutY(0);
		root.getChildren().addAll(boardGroup, buttons);
		scene = new Scene(root, 800, 800);
		VBox box = new VBox();
		box.getChildren().addAll(redStarts, blackStarts);
		Scene secondaryScene = new Scene(box, 100, 50);
		newGameButton.setOnAction(e -> {
			newGameHandler();
			buttons.getChildren().remove(newGameButton);});
		scene.setOnMousePressed(e -> 
		{
			for(int i = 0; i < moves.size(); i++) 
			{
				selectedCircle = circleFinder.get("" + moves.get(i).get(0) + moves.get(i).get(1));
				if(selectedCircle.contains(e.getX(), e.getY()))
				{
					anchorX = 0 + selectedCircle.getCenterX();
					anchorY = 0 + selectedCircle.getCenterY();
					movePiece(moves.get(i).get(0), moves.get(i).get(1));
					break;
				}
				selectedCircle = new Circle();
				if(i + 1 == moves.size())
					e.consume();
			}
		});
		
		secondaryStage.setScene(secondaryScene);
		secondaryStage.initModality(Modality.APPLICATION_MODAL);
		stage.setScene(scene);
		stage.show();
	} 
	public static void main(String[] args) 
	{
		launch(args);
	}
	private void newGameHandler() 
	{
		currentGame.NewGame();
		secondaryStage.show();
		redStarts.setOnAction(e -> {
			currentGame.setTurn(Team.Red);
			boardGroup.setRotate(0);
			secondaryStage.hide();
			moves = currentGame.scanForMoves();
			highlightJumps();});
		blackStarts.setOnAction(e -> {
			currentGame.setTurn(Team.Black);
			boardGroup.setRotate(180);
			secondaryStage.hide();
			moves = currentGame.scanForMoves();
			highlightJumps();});
		loadPieces();
	}
	private void loadPieces() 
	{
		circles = new Circle[currentGame.getRedCount() + currentGame.getBlackCount()];
		int circleCount = 0;
		for(int x = 0; x < 4; x++)
		{
			for(int y = 0; y < 8; y++)
			{
				if(currentGame.getPositions()[x][y] == Status.Red || currentGame.getPositions()[x][y] == Status.RedKing)
				{
					circles[circleCount] = new Circle((x + 1) * 160 - 80 * (y % 2) + 40, (y + 1) * 80 + 40, 30, Color.RED);
					circleFinder.put("" + x + y, circles[circleCount]);
					boardGroup.getChildren().add(circles[circleCount]);
					circleCount++;
				}
				else if(currentGame.getPositions()[x][y] == Status.Black ||currentGame.getPositions()[x][y] == Status.BlackKing) 
				{
					circles[circleCount] = new Circle((x + 1) * 160 - 80 * (y % 2) + 40, (y + 1) * 80 + 40, 30, Color.BLACK);
					circleFinder.put("" + x + y, circles[circleCount]);
					boardGroup.getChildren().add(circles[circleCount]);
					circleCount++;
				}
			}
		}
	}
	private void highlightJumps() 
	{
		highlightedSquares = new ArrayList<>();
		if(moves.get(0).size() == 6)
		{
			for(int i = 0; i < moves.size(); i++) 
			{   
				highlightedSquares.add(rectangleFinder.get("" + moves.get(i).get(0) + moves.get(i).get(1)));
				highlightedSquares.add(rectangleFinder.get("" + moves.get(i).get(4) + moves.get(i).get(5)));
			}
			for(int i = 0; i < highlightedSquares.size(); i ++) 
				highlightedSquares.get(i).setFill(Color.GREEN);
		}
	}
	private void movePiece(int x, int y) 
	{	
		ArrayList<ArrayList<Integer>> movesForPiece = new ArrayList<>();
		for(int i = 0; i < moves.size(); i++)
		{
			if(x == moves.get(i).get(0) && y == moves.get(i).get(1))
				movesForPiece.add(moves.get(i));
		}
		selectedCircle.setOnMouseDragged(dragEvent -> 
		{
			selectedCircle.setCenterX(dragEvent.getX());
			selectedCircle.setCenterY(dragEvent.getY());
		});
		selectedCircle.setOnMouseReleased(releaseEvent -> 
		{
			for(int i = 0; i < movesForPiece.size(); i++) 
			{
				Rectangle destRectangle = rectangleFinder.get("" + 
						movesForPiece.get(i).get(movesForPiece.get(i).size() - 2) + 
						movesForPiece.get(i).get(movesForPiece.get(i).size() - 1));
				if(destRectangle.contains(releaseEvent.getX(), releaseEvent.getY())) 
				{
					int destX = movesForPiece.get(i).get(movesForPiece.get(i).size() - 2);
					int destY = movesForPiece.get(i).get(movesForPiece.get(i).size() - 1);
					currentGame.movePiece(x, y, destX, destY, currentGame.getPositions()[x][y]);
					circleFinder.remove("" + x + y);
					circleFinder.put("" + destX + destY, selectedCircle);
					selectedCircle.setCenterX(destRectangle.getX() + 40);
					selectedCircle.setCenterY(destRectangle.getY() + 40);
					if(movesForPiece.get(i).size() == 6)
					{
						int removedPieceX = movesForPiece.get(i).get(movesForPiece.get(i).size() - 4);
						int removedPieceY = movesForPiece.get(i).get(movesForPiece.get(i).size() - 3);
						currentGame.removePiece(removedPieceX, removedPieceY, currentGame.getPositions()[removedPieceX][removedPieceY]);
						boardGroup.getChildren().remove(circleFinder.get
								("" + removedPieceX + removedPieceY));
						circleFinder.remove("" + removedPieceX + removedPieceY);
						moves = currentGame.scanPieceForJumps(destX, destY);
						for(int j = 0; j < highlightedSquares.size(); j++)
							highlightedSquares.get(j).setFill(Color.BROWN);
						highlightJumps();
						if(moves.get(0).size() == 6)
							break;
					}
					if(currentGame.getTurn() == Team.Black)
						currentGame.setTurn(Team.Red);
					else
						currentGame.setTurn(Team.Black);
					moves = currentGame.scanForMoves();
					highlightJumps();
					break;
				}
				else if(i + 1 == movesForPiece.size()) 
				{
					selectedCircle.setCenterX(anchorX + 0);
					selectedCircle.setCenterY(anchorY + 0);
				}
			}
			selectedCircle = new Circle();
		});	
	}
}
