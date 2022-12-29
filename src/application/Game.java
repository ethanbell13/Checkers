package application;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Pair;

public class Game 
{
	ArrayList<Piece> redPieces = new ArrayList<Piece>();
	ArrayList<Piece> blackPieces = new ArrayList<Piece>();
	ArrayList<Piece> allPieces = new ArrayList<Piece>();
	Position[] positions = new Position[32];
	HashMap<String, Integer> positionFinder =  new HashMap<String, Integer>();
	PositionStatus turn = PositionStatus.Empty;
	boolean noJumps = true;
	public Game() 
	{
		double x = 160;
		double y = 80;
		//Forms all playable positions as brown squares on the board.
		//Starts at top left and then moves one row down 
		//after every 4 squares, 
		for(int i = 0; i < 32; i++) 
		{
			positions[i] = new Position(new Rectangle(x, y, 79, 79), PositionStatus.Empty);
			positions[i].getSquare().setFill(Color.BROWN);
			positions[i].getSquare().setStyle("-fx-stroke: yellow; -fx-stroke-width: 2;");
			positionFinder.put("" + x + y, i);
			x += 160;
			if((i + 1) % 8 == 0)
			{
				y += 80;
				x = 160;
			}
			else if((i + 1) % 4 == 0)
			{
				y += 80;
				x = 80;
			}
		}
	}
	public void NewGame() 
	{
		//restore code after testing available moves
//		int x = 0;
//		int y = 120;
//		for(int i = 0; i < 4; i++) 
//		{
//			for(int j = 0; j < 8; j++) 
//			{
//				if(j % 2 == 1)
//					x = 120;
//				else
//					x = 200;
//				if(j < 3) 
//					blackPieces.add(new Piece(new Circle(i * 160 + x,
//							j * 80 + y, 30, Color.BLACK), PieceStatus.Stone));
//				else if(j > 4)
//					redPieces.add(new Piece(new Circle(i * 160 + x, 
//							j * 80 + y, 30, Color.RED), PieceStatus.Stone));
//			}
//		}
		blackPieces.add(new Piece(new Circle(200, 120, 30, Color.BLACK), PieceStatus.Stone));
//		blackPieces.add(new Piece(new Circle(360, 440, 30, Color.BLACK), PieceStatus.Stone));
		redPieces.add(new Piece(new Circle(280, 520, 30, Color.RED), PieceStatus.Stone));
		allPieces.addAll(redPieces);
		allPieces.addAll(blackPieces);
		updateBoard();
	}
	public void updateBoard() 
	{
		for(int i = 0; i < redPieces.size(); i++)
			positions[positionFinder.get(redPieces.get(i).getPositionDictionaryKey())].setStatus(PositionStatus.Red);
		for(int i = 0; i < blackPieces.size(); i++)
			positions[positionFinder.get(blackPieces.get(i).getPositionDictionaryKey())].setStatus(PositionStatus.Black);
	}
	public ArrayList<Pair<Position, Integer>> availableMoves(ArrayList<Piece> pieces, PositionStatus team) 
	{
		ArrayList<Pair<Position, Integer>> jumps = new ArrayList<Pair<Position, Integer>>();		
		ArrayList<Pair<Position,  Integer>> moves = new ArrayList<Pair<Position, Integer>>();
		Position curPosition = null;
		//code written to check moves for red player. Will need to edit to include black player.
		for(int i = 0; i < pieces.size(); i++) 
		{
			double x = pieces.get(i).getCircle().getCenterX();
			double y = pieces.get(i).getCircle().getCenterY();
			if(x != 120 && y != 120 && (pieces.get(i).getStatus() == PieceStatus.King || team == PositionStatus.Red)) 
			{
				curPosition = positions[positionFinder.get("" + (x - 120) + (y - 120))];
				if(jumps.size() == 0 && curPosition.getStatus() == PositionStatus.Empty) 
					moves.add(new Pair<>(curPosition, i));
				else if(x != 200 | y != 200 && curPosition.getStatus() != team && 
						positions[positionFinder.get("" + (x - 200) + (y - 200))].getStatus() == PositionStatus.Empty)
					jumps.add(new Pair<>(positions[positionFinder.get("" + (x - 200) + (y - 200))], i));
			}
			
			if(x != 680 && y != 120 && (pieces.get(i).getStatus() == PieceStatus.King || team == PositionStatus.Red)) 
			{
				curPosition = positions[positionFinder.get("" + (x + 40) + (y - 120))];
				if(jumps.size() == 0 && curPosition.getStatus() == PositionStatus.Empty) 
					moves.add(new Pair<>(curPosition, i));
				else if(x != 600 | y != 200 && curPosition.getStatus() != team &&
						positions[positionFinder.get("" + (x + 120) + (y - 200))].getStatus() == PositionStatus.Empty)
					jumps.add(new Pair<>(positions[positionFinder.get("" + (x + 120) + (y - 200))], i));
			}
			
			if(x != 120 && y != 680 && (pieces.get(i).getStatus() == PieceStatus.King || team == PositionStatus.Black)) 
			{
				curPosition = positions[positionFinder.get("" + (x - 120) + (y + 40))];
				if(jumps.size() == 0 && curPosition.getStatus() == PositionStatus.Empty) 
					moves.add(new Pair<>(curPosition, i));
				else if(x != 200 | y != 600 && curPosition.getStatus() != team &&
						positions[positionFinder.get("" + (x - 200) + (y + 120))].getStatus() == PositionStatus.Empty)
					jumps.add(new Pair<>(positions[positionFinder.get("" + (x - 200) + (y + 120))], i));
			}
			
			if(x != 680 && y != 680 && (pieces.get(i).getStatus() == PieceStatus.King || team == PositionStatus.Black)) 
			{
				curPosition = positions[positionFinder.get("" + (x + 40) + (y + 40))];
				if(jumps.size() == 0 && curPosition.getStatus() == PositionStatus.Empty) 
					moves.add(new Pair<>(curPosition, i));
				else if(x != 600 | y != 600 && curPosition.getStatus() != team && 
						positions[positionFinder.get("" + (x + 120) + (y + 120))].getStatus() == PositionStatus.Empty)
					jumps.add(new Pair<>(positions[positionFinder.get("" + (x + 120) + (y + 120))], i));
			}
		}
		if(jumps.size() == 0)
			return moves;
		noJumps = false;
		return jumps;
	}
}