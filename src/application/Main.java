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
	
	private Circle selectedCircle;
	private int index = 0;
	private double originX = 0;
	private double originY = 0;
	private Piece removedPiece = null;
	private ArrayList<Position> highlightedSquares = null;
	
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
			beginGame();});
		blackStarts.setOnAction(e -> {
			currentGame.turn = PositionStatus.Black;
			boardGroup.setRotate(180);
			secondaryStage.hide();});
		loadPieces();
	}
	private void loadPieces() 
	{
		for(int i = 0; i < currentGame.redPieces.size(); i++)
			boardGroup.getChildren().add(currentGame.redPieces.get(i).getCircle());
		for(int i  = 0; i < currentGame.blackPieces.size(); i++)
			boardGroup.getChildren().add(currentGame.blackPieces.get(i).getCircle());
	}
	private void beginGame() 
	{	
		final ArrayList<Pair<Piece, ArrayList<Position>>> moves = currentGame.scanForAllMoves();
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
			scene.setOnMousePressed(e -> 
			{	
				for(int i = 0; i < moves.size(); i++) 
				{
					if(e.getTarget() == moves.get(i).getKey().getCircle())
					{
						selectedCircle = moves.get(i).getKey().getCircle();
						originX = selectedCircle.getCenterX() + 0;
						originY = selectedCircle.getCenterY() + 0;
						index = i;
						break;
					}
				} 
				scene.setOnMouseDragged(dragEvent -> 
				{
					if(e.getTarget() == selectedCircle) 
					
							selectedCircle.setCenterX(dragEvent.getX());
							selectedCircle.setCenterY(dragEvent.getY());
							scene.setOnMouseReleased(releaseEvent -> 
							{
								for(int i = 0; i < moves.get(index).getValue().size(); i++) 
								{
									Rectangle selectedSquare = moves.get(index).getValue().get(i).getSquare();
									if(selectedSquare.contains(releaseEvent.getX(), releaseEvent.getY())) 
									{
										selectedCircle.setCenterX(selectedSquare.getX() + 40);
										selectedCircle.setCenterY(selectedSquare.getY() + 40);
										if(!currentGame.noJumps) 
										{
											removedPiece = currentGame.pieceFinder.get
											("" + ((originX + selectedCircle.getCenterX())) / 2 
													+ ((originY + selectedCircle.getCenterY()) / 2));
											if(currentGame.turn == PositionStatus.Black) 
												currentGame.redPieces.remove(removedPiece);
											else
												currentGame.blackPieces.remove(removedPiece);
											boardGroup.getChildren().remove(removedPiece.getCircle());
											for(int j = 0; j < highlightedSquares.size(); j++)
												highlightedSquares.get(j).getSquare().setFill(Color.BROWN);
										}
										if(currentGame.turn == PositionStatus.Black)
											currentGame.turn = PositionStatus.Red;
										else
											currentGame.turn = PositionStatus.Black;
										return;
									}
								}
								selectedCircle.setCenterX(originX);
								selectedCircle.setCenterY(originY);
							});
					
				});
				
			});	
	}
}
