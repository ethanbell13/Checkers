package application;

import java.util.ArrayList;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.paint.Color;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Circle;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.TextInputDialog;

public class Main extends Application
{	
	private Game currentGame = new Game();
	private ArrayList<Pair<Position, Integer>> moves = new ArrayList<Pair<Position, Integer>>();
	
	private Button newGameButton = new Button("New Game");
	private Button loadGameButton = new Button("Load Game");
	private Button saveGameButton = new Button("Save Game");
	private Scene scene;
	private Group boardGroup = new Group();
	private Pane root = new Pane();
	private Button redStarts = new Button("Red Starts");
	private Button blackStarts = new Button("Black Starts");
	private Stage secondaryStage = new Stage();
	
	@Override
	public void start(Stage stage) 
	{
		Rectangle board = new Rectangle(80, 80, 640, 640);
		board.setFill(Color.DARKKHAKI);
		board.setStyle("-fx-stroke: yellow; -fx-stroke-width: 2;");
		boardGroup.getChildren().addAll(board);
		int x = 160;
		int y = 80;
		for(int i = 0; i < 32; i++) 
			boardGroup.getChildren().add(currentGame.positions[i].getSquare());
		newGameButton.setOnAction(e -> newGameHandler());
		HBox buttons = new HBox(10, newGameButton, loadGameButton);
		buttons.setLayoutX(320);
		buttons.setLayoutY(0);
		root.getChildren().addAll(boardGroup, buttons);
		scene = new Scene(root, 800, 800);
		VBox box = new VBox();
		box.getChildren().addAll(redStarts, blackStarts);
		Scene secondaryScene = new Scene(box, 100, 50);
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
			secondaryStage.hide();
			beginGame();});
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
		int indexModifier = 0;
		Position piecePos = null;
		Position destinationPos = null;
		ArrayList<Circle> circles = new ArrayList<Circle>();
		boolean pieceSelected = false;
//		while(currentGame.redPieces.size() != 0 && currentGame.blackPieces.size() != 0) 
//		{
			if(currentGame.turn == PositionStatus.Red)
				moves = currentGame.availableMoves(currentGame.redPieces, PositionStatus.Red);
			else
				{
				moves = currentGame.availableMoves(currentGame.blackPieces, PositionStatus.Black);
				indexModifier += currentGame.redPieces.size();
				}
			if(!currentGame.noJumps)
			{
				for(int i = 0; i < moves.size(); i++) 
				{   
					//returns position of piece that can jump
					piecePos = currentGame.positions[
					currentGame.positionFinder.get
					(currentGame.allPieces.get(moves.get(i).getValue() + indexModifier)
							.getPositionDictionaryKey())];
					piecePos.getSquare().setFill(Color.GREEN);
					destinationPos = moves.get(i).getKey();
					destinationPos.getSquare().setFill(Color.GREEN);
				}
			}
			for(int i = 0; i < moves.size(); i++)
				circles.add(currentGame.allPieces.get(indexModifier + i).getCircle());
			scene.setOnMousePressed(e -> 
			{
				scene.setOnMouseDragged(dragEvent -> 
				{
					for(int i = 0; i < circles.size(); i++) 
					{
						Circle currentCircle = circles.get(i);
						final double originX = currentCircle.getCenterX();
						final double originY = currentCircle.getCenterY() + 0;
						if(e.getTarget() == currentCircle) 
						{
							currentCircle.setCenterX(dragEvent.getX());
							currentCircle.setCenterY(dragEvent.getY());
						}
						//originX and Y are changing to unexpected values on mouse relase
						//the problem could be using a for loop within these events or 
						//creating events within events
						scene.setOnMouseReleased(releaseEvent -> 
						{
							for(int j = 0; j < moves.size(); j++) 
							{
								if(releaseEvent.getTarget() == moves.get(j).getKey())
								{
									currentCircle.setCenterX(moves.get(j).getKey().getSquare().getX() + 40);
									currentCircle.setCenterX(moves.get(j).getKey().getSquare().getY() + 40);
								}
								else 
								{
									currentCircle.setCenterX(originX);
									currentCircle.setCenterY(originY);
								}
							}
						});
					}
				});
				
			});
			
		//}
	}
}
