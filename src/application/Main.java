package application;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class Main extends Application
{	
	private Game currentGame;
	
	private Button newGameButton = new Button("New Game");
	private Button loadGameButton = new Button("Load Game");
	private Button saveGameButton = new Button("Save Game");
	private Scene scene;
	private Group boardGroup = new Group();
	private Pane root = new Pane();
	
	private Button redStarts = new Button("Red Starts");
	private Button blackStarts = new Button("Black Starts");
	private Stage secondaryStage = new Stage();
	
	private Button yes = new Button("Yes");
	private Button no = new Button("No");
	private Label newGameLabel = new Label("Do you want to start a new game? Any unsaved progress will be lost.");
	private Stage thirdStage = new Stage();
	
	private PieceInterface selectedPiece = null;
	private HashMap<String, Rectangle> rectangleFinder = new HashMap<>();
	private HashMap<String, PieceInterface> pieceFinder = new HashMap<>();
	private Rectangle[] rectangles = new Rectangle[32];
	private ArrayList<PieceInterface> pieces = new ArrayList<>();
	private double anchorX;
	private double anchorY;
	private ArrayList<Rectangle> highlightedSquares = null;
	private ArrayList<ArrayList<Integer>> moves = new ArrayList<>();
	private ArrayList<ArrayList<Integer>> movesForPiece;
	
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
		HBox buttons = new HBox(10, newGameButton, saveGameButton, loadGameButton);
		buttons.setLayoutX(280);
		buttons.setLayoutY(0);
		root.getChildren().addAll(boardGroup, buttons);
		scene = new Scene(root, 800, 800);
		stage.setScene(scene);
		
		VBox box = new VBox();
		box.getChildren().addAll(redStarts, blackStarts);
		Scene secondaryScene = new Scene(box, 100, 50);
		secondaryStage.setScene(secondaryScene);
		secondaryStage.initModality(Modality.APPLICATION_MODAL);
		
		VBox newGameBox = new VBox();
		newGameBox.getChildren().addAll(newGameLabel, yes, no);
		Scene thirdScene = new Scene(newGameBox, 375, 75);
		thirdStage.setScene(thirdScene);
		thirdStage.initModality(Modality.APPLICATION_MODAL);
		
		newGameButton.setOnAction(e -> {
			if(currentGame != null) 
				thirdStage.show();
			else
				newGameHandler();});
		yes.setOnAction(e -> {
			thirdStage.hide();
			clearBoard();
			newGameHandler();});
		no.setOnAction(e -> {
			thirdStage.hide();});
		saveGameButton.setOnAction(e -> {saveGameHandler();});
		loadGameButton.setOnAction(e -> {loadGameHandler();});
		scene.setOnMousePressed(e -> 
		{
			for(int i = 0; i < moves.size(); i++) 
			{
				selectedPiece = pieceFinder.get("" + moves.get(i).get(0) + moves.get(i).get(1));
				anchorX = selectedPiece.getCircle().getCenterX();
				anchorY = selectedPiece.getCircle().getCenterY();
				if(selectedPiece.getCircle().contains(e.getX(), e.getY()))
				{
					movePiece(moves.get(i).get(0), moves.get(i).get(1));
					break;
				}
				selectedPiece = null;
				if(i + 1 == moves.size())
					e.consume();
			}
		});
		stage.show();
	} 
	public static void main(String[] args) 
	{
		launch(args);
	}
	private void newGameHandler() 
	{
		currentGame = new Game();
		currentGame.NewGame();
		secondaryStage.show();
		redStarts.setOnAction(e -> {
			currentGame.setTurn(Team.Red);
			secondaryStage.hide();
			moves = currentGame.scanForMoves();
			highlightJumps();});
		blackStarts.setOnAction(e -> {
			currentGame.setTurn(Team.Black);
			secondaryStage.hide();
			moves = currentGame.scanForMoves();
			highlightJumps();});
		loadPieces();
	}
	private void loadPieces() 
	{
		for(int x = 0; x < 4; x++)
		{
			for(int y = 0; y < 8; y++)
			{
				if(currentGame.getPositions()[x][y] == Status.Red || currentGame.getPositions()[x][y] == Status.Black)
				{
					pieces.add(new StoneGroup(x, y, currentGame.getPositions()[x][y]));
					pieceFinder.put("" + x + y, pieces.get(pieces.size() - 1));
					boardGroup.getChildren().add(pieces.get(pieces.size() - 1).getPiece());
				}
				else if(currentGame.getPositions()[x][y] == Status.BlackKing || currentGame.getPositions()[x][y] == Status.RedKing) 
				{
					pieces.add(new KingGroup(x, y, currentGame.getPositions()[x][y]));
					pieceFinder.put("" + x + y, pieces.get(pieces.size() - 1));
					boardGroup.getChildren().add(pieces.get(pieces.size() - 1).getPiece());
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
		movesForPiece = new ArrayList<>();
		for(int i = 0; i < moves.size(); i++)
		{
			if(x == moves.get(i).get(0) && y == moves.get(i).get(1))
				movesForPiece.add(moves.get(i));
		}
		selectedPiece.getPiece().setOnMouseDragged(dragEvent -> 
		{
			if(selectedPiece != null)
				selectedPiece.setCoordinatesWithDouble(dragEvent.getX(), dragEvent.getY());
		});
		selectedPiece.getPiece().setOnMouseReleased(releaseEvent -> 
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
					pieceFinder.remove("" + x + y);
					selectedPiece.setCoordinatesWithDouble(destRectangle.getX() + 40, destRectangle.getY() + 40);
					if(destY == 0 && currentGame.getPositions()[x][y] == Status.Red) 
					{
						pieces.remove(selectedPiece);
						boardGroup.getChildren().remove(selectedPiece.getPiece());
						pieces.add(new KingGroup(destX, destY, Status.RedKing));
						selectedPiece = pieces.get(pieces.size() - 1);
						boardGroup.getChildren().add(selectedPiece.getPiece());
					}
					else if(destY == 7 && currentGame.getPositions()[x][y] == Status.Black) 
					{
						pieces.remove(selectedPiece);
						boardGroup.getChildren().remove(selectedPiece.getPiece());
						pieces.add(new KingGroup(destX, destY, Status.BlackKing));
						selectedPiece = pieces.get(pieces.size() - 1);
						boardGroup.getChildren().add(selectedPiece.getPiece());
					}
					currentGame.movePiece(x, y, destX, destY, currentGame.getPositions()[x][y]);
					pieceFinder.put("" + destX + destY, selectedPiece);
					if(movesForPiece.get(i).size() == 6)
					{
						int removedPieceX = movesForPiece.get(i).get(movesForPiece.get(i).size() - 4);
						int removedPieceY = movesForPiece.get(i).get(movesForPiece.get(i).size() - 3);
						currentGame.removePiece(removedPieceX, removedPieceY, currentGame.getPositions()[removedPieceX][removedPieceY]);
						boardGroup.getChildren().remove(pieceFinder.get
								("" + removedPieceX + removedPieceY).getPiece());
						pieceFinder.remove("" + removedPieceX + removedPieceY);
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
					selectedPiece.setCoordinatesWithDouble(anchorX + 0, anchorY + 0);
			}
			selectedPiece = null;
			movesForPiece = new ArrayList<>();
		});	
	}
	private void saveGameHandler() 
	{
		try(
				FileOutputStream gameFile = new FileOutputStream("./game.dat");
				ObjectOutputStream gameStream = new ObjectOutputStream(gameFile);)
		{
			gameStream.writeObject(currentGame);
			
		}
		catch(IOException e){System.out.println("There was a problem writing the file");}
	}
	private void loadGameHandler() 
	{
		try(
				FileInputStream gameFile = new FileInputStream("./game.dat");
				ObjectInputStream gameStream = new ObjectInputStream(gameFile);
			)
		{
			currentGame = (Game) gameStream.readObject();
			clearBoard();
			loadPieces();
			moves = currentGame.scanForMoves();
			highlightJumps();
		}
		catch(FileNotFoundException e)
		{
			System.out.println("\nNo file was read");
		}
		catch(ClassNotFoundException e) 
		{
			System.out.println("\nUnreadable file format");
		}
		
		catch(IOException e)
		{
			System.out.println("\nThere was a problem reading the file");
		}
	}
	private void clearBoard() 
	{
		for(int i = 0; i < pieces.size(); i++)
			boardGroup.getChildren().remove(pieces.get(i).getPiece());
		pieces = new ArrayList<>();
		pieceFinder = new HashMap<>();
		for(int j = 0; j < highlightedSquares.size(); j++)
			highlightedSquares.get(j).setFill(Color.BROWN);
	}
}
