package application;

import java.util.ArrayList;

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
	
//	private Circle selectedCircle;
	private int index = 0;
	private double anchorX;
	private double anchorY;
	private Piece removedPiece = null;
	private ArrayList<Position> highlightedSquares = null;
	private boolean pieceSelected = false;
	private ArrayList<Pair<Piece, ArrayList<Position>>> moves = new ArrayList<>();
	
	@Override
	public void start(Stage stage) 
	{
		Rectangle board = new Rectangle(80, 80, 640, 640);
		board.setFill(Color.DARKKHAKI);
		board.setStyle("-fx-stroke: yellow; -fx-stroke-width: 2;");
		boardGroup.getChildren().addAll(board);
		for(int i = 0; i < 32; i++) 
			boardGroup.getChildren().add(currentGame.positions[i].getSquare());
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
				if(moves.get(i).getKey().getCircle().contains(e.getX(), e.getY()))
				{
					movePiece(moves.get(i));
					anchorX = 0 + moves.get(i).getKey().getCircle().getCenterX();
					anchorY = 0 + moves.get(i).getKey().getCircle().getCenterY();
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
			currentGame.turn = PositionStatus.Red;
			boardGroup.setRotate(0);
			secondaryStage.hide();
			moves = currentGame.scanForMoves();});
		blackStarts.setOnAction(e -> {
			currentGame.turn = PositionStatus.Black;
			boardGroup.setRotate(180);
			secondaryStage.hide();
			moves = currentGame.scanForMoves();});
		highlightJumps();
		loadPieces();
	}
	private void loadPieces() 
	{
		for(int i = 0; i < currentGame.redPieces.size(); i++)
			boardGroup.getChildren().add(currentGame.redPieces.get(i).getCircle());
		for(int i  = 0; i < currentGame.blackPieces.size(); i++)
			boardGroup.getChildren().add(currentGame.blackPieces.get(i).getCircle());
	}
	private void highlightJumps() 
	{
		if(!currentGame.noJumps)
		{
			for(int i = 0; i < moves.size(); i++) 
			{   
				currentGame.positions[currentGame.positionFinder.get(moves.get
						(i).getKey().getPositionDictionaryKey())].getSquare().setFill(Color.GREEN);
				highlightedSquares = moves.get(i).getValue();
				highlightedSquares.add(currentGame.positions[currentGame.positionFinder.get(moves.get
						(i).getKey().getPositionDictionaryKey())]);
				for(int j = 0; j < highlightedSquares.size(); j++)
					highlightedSquares.get(j).getSquare().setFill(Color.GREEN);
			}
		}
	}
	private void movePiece(Pair<Piece, ArrayList<Position>> move) 
	{	
		move.getKey().getCircle().setOnMouseDragged(dragEvent -> 
		{
			Circle selectedCircle = move.getKey().getCircle();
			selectedCircle.setCenterX(dragEvent.getX());
			selectedCircle.setCenterY(dragEvent.getY());
		});
		move.getKey().getCircle().setOnMouseReleased(releaseEvent -> 
		{
			Circle selectedCircle = move.getKey().getCircle();
			for(int i = 0; i < move.getValue().size(); i++) 
			{
				if(move.getValue().get(i).getSquare().contains(releaseEvent.getX(), releaseEvent.getY())) 
				{
					currentGame.movePiece(move.getKey(), anchorX, anchorY, 
							currentGame.turn, move.getValue().get(i));
					if(!currentGame.noJumps) 
					{
						//RemovedPiece is being evaluated at null. Must resolve.
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
