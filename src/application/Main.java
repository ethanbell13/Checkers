package application;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.application.Application;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Circle;
import javafx.scene.control.Button;

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
	
	private Piece selectedPiece = null;
	private Circle selectedCircle = null;
	private HashMap<String, Rectangle> rectangleFinder = new HashMap<>();
	private HashMap<String, Circle> circleFinder = new HashMap<>();
	private Rectangle[] rectangles = new Rectangle[32];
	private Circle[] circles;
	private int index = 0;
	private double anchorX;
	private double anchorY;
	private Piece removedPiece = null;
	private ArrayList<Rectangle> highlightedSquares = null;
	private boolean pieceSelected = false;
	private ArrayList<Pair<Piece, ArrayList<Integer>>> moves = new ArrayList<>();
	
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
				selectedPiece = moves.get(i).getKey();
				selectedCircle = circleFinder.get(selectedPiece.getCoordinates());
				if(selectedCircle.contains(e.getX(), e.getY()))
				{
					anchorX = 0 + selectedCircle.getCenterX();
					anchorY = 0 + selectedCircle.getCenterY();
					movePiece(selectedPiece);
					break;
				}
				else if(i + 1 == moves.size())
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
		circles = new Circle[currentGame.getRedPieces().size() + currentGame.getBlackPieces().size()];
		Piece piece;
		for(int i = 0; i < currentGame.getRedPieces().size(); i++)
		{
			piece = currentGame.getRedPieces().get(i);
			circles[i] = new Circle(
					(piece.getX() + 1) * 160 - 80 * (piece.getY() % 2) + 40, (piece.getY() + 1) * 80 + 40, 30, Color.RED);
			circleFinder.put("" + piece.getX() + piece.getY(), circles[i]);
			boardGroup.getChildren().add(circles[i]);
		}
		for(int i  = 0; i < currentGame.getBlackPieces().size(); i++)
		{
			piece = currentGame.getBlackPieces().get(i);
			circles[i + currentGame.getRedPieces().size()] = new Circle(
					(piece.getX() + 1) * 160 - 80 * (piece.getY() % 2) + 40, (piece.getY() + 1) * 80 + 40, 30, Color.BLACK);
			circleFinder.put("" + piece.getX() + piece.getY(), circles[i + currentGame.getRedPieces().size()]);
			boardGroup.getChildren().add(circles[i + currentGame.getRedPieces().size()]);
		}
	}
	private void highlightJumps() 
	{
		highlightedSquares = new ArrayList<>();
		if(moves.get(0).getValue().size() == 6)
		{
			for(int i = 0; i < moves.size(); i++) 
			{   
				highlightedSquares.add(rectangleFinder.get(moves.get(i).getKey().getCoordinates()));
				highlightedSquares.add(rectangleFinder.get("" + moves.get(i).getValue().get(4) + moves.get(i).getValue().get(5)));
			}
			for(int i = 0; i < highlightedSquares.size(); i ++) 
				highlightedSquares.get(i).setFill(Color.GREEN);
		}
	}
	private void movePiece(Piece piece) 
	{	
		ArrayList<Pair<Piece, ArrayList<Integer>>> movesForPiece = new ArrayList<>();
		for(int i = 0; i < moves.size(); i++)
		{
			if(piece == moves.get(i).getKey())
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
				Rectangle destRectangle = rectangleFinder.get("" + movesForPiece.get(i).getValue().get(-2) + 
						movesForPiece.get(i).getValue().get(-1));
				if(destRectangle.contains(releaseEvent.getX(), releaseEvent.getY())) 
				{
					currentGame.movePiece(piece, movesForPiece.get(i).getValue().get(-2), movesForPiece.get(i).getValue().get(-1), 
							currentGame.getTeam());
					if(movesForPiece.get(i).getValue().size() == 6)
					{
						removedPiece = currentGame.pieceFinder.get
						("" + ((anchorX + selectedCircle.getCenterX())) / 2 + 
								((anchorY + selectedCircle.getCenterY()) / 2));
						boardGroup.getChildren().remove(removedPiece.getCircle());
						if(currentGame.turn == PositionStatus.Black) 
							currentGame.removePiece(removedPiece, anchorX, anchorY, PositionStatus.Red);
						else
							currentGame.removePiece(removedPiece, anchorX, anchorY, PositionStatus.Black);
						//boardGroup.getChildren().remove(removedPiece.getCircle());
						for(int j = 0; j < highlightedSquares.size(); j++)
							highlightedSquares.get(j).getSquare().setFill(Color.BROWN);
					}
					if(currentGame.turn == PositionStatus.Black)
						currentGame.turn = PositionStatus.Red;
					else
						currentGame.turn = PositionStatus.Black;
					currentGame.noJumps = true;
					moves = currentGame.scanForMoves();
					highlightJumps();
					return;
				}
				else if(i + 1 == move.getValue().size()) 
				{
					selectedCircle.setCenterX(anchorX + 0);
					selectedCircle.setCenterY(anchorY + 0);
				}
			}
		});	
	}
}
