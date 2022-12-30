package application;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Pair;

public class Game 
{
	ArrayList<Piece> redPieces = new ArrayList<>();
	ArrayList<Piece> blackPieces = new ArrayList<>();
	ArrayList<Piece> allPieces = new ArrayList<>();
	Position[] positions = new Position[32];
	HashMap<String, Integer> positionFinder =  new HashMap<>();
	HashMap<String, Piece> pieceFinder = new HashMap<>();
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
//		pieceFinder.put("" + 360.0 + 440.0, blackPieces.get(0));
		pieceFinder.put("" + 200.0 + 120.0, blackPieces.get(0));
		redPieces.add(new Piece(new Circle(280, 520, 30, Color.RED), PieceStatus.Stone));
		pieceFinder.put("" + 280.0 + 520.0, redPieces.get(0));
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
	public ArrayList<Pair<Piece, ArrayList<Position>>> scanForAllMoves()
	{
		ArrayList<Piece> pieces = new ArrayList<Piece>();
		ArrayList<Pair<Piece, ArrayList<Position>>> moves = new ArrayList<>();
		if(turn == PositionStatus.Red)
			pieces = redPieces;
		else
			pieces = blackPieces;
		for(int i = 0; i < pieces.size(); i++) 
			moves.add(new Pair<>(pieces.get(i), scanPieceForMoves(pieces.get(i))));
		return moves;
	}
	public ArrayList<Position> scanPieceForMoves(Piece piece) 
	{
		ArrayList<Position> moves = new ArrayList<Position>();
		ArrayList<Position> jumps = new ArrayList<Position>();
		double x = piece.getCircle().getCenterX();
		double y = piece.getCircle().getCenterY();
		Position move = null;
		Position jump = null;
		if(x != 120 && y != 120 && (piece.getStatus() == PieceStatus.King || turn == PositionStatus.Red)) 
		{
			
			move = positions[positionFinder.get("" + (x - 120) + (y - 120))];
			jump = positions[positionFinder.get("" + (x - 200) + (y - 200))];
			if(noJumps && move.getStatus() == PositionStatus.Empty) 
				moves.add(move);
			else if(x != 200 | y != 200 && move.getStatus() != turn && 
					jump.getStatus() == PositionStatus.Empty)
			{
				jumps.add(jump);
				noJumps = false;
			}
		}
		
		if(x != 680 && y != 120 && (piece.getStatus() == PieceStatus.King || turn == PositionStatus.Red)) 
		{
			move = positions[positionFinder.get("" + (x + 40) + (y - 120))];
			jump = positions[positionFinder.get("" + (x + 120) + (y - 200))];
			if(noJumps && move.getStatus() == PositionStatus.Empty) 
				moves.add(move);
			else if(x != 600 | y != 200 && move.getStatus() != turn &&
					jump.getStatus() == PositionStatus.Empty)
			{
				jumps.add(jump);
				noJumps = false;
			}
		}
		
		if(x != 120 && y != 680 && (piece.getStatus() == PieceStatus.King || turn == PositionStatus.Black)) 
		{
			move = positions[positionFinder.get("" + (x - 120) + (y + 40))];
			jump = positions[positionFinder.get("" + (x - 200) + (y + 120))];
			if(noJumps && move.getStatus() == PositionStatus.Empty) 
				moves.add(move);
			else if(x != 200 | y != 600 && move.getStatus() != turn &&
					positions[positionFinder.get("" + (x - 200) + (y + 120))].getStatus() == PositionStatus.Empty)
			{
				jumps.add(jump);
				noJumps = false;
			}
		}
		
		if(x != 680 && y != 680 && (piece.getStatus() == PieceStatus.King || turn == PositionStatus.Black)) 
		{
			move = positions[positionFinder.get("" + (x + 40) + (y + 40))];
			jump = positions[positionFinder.get("" + (x + 120) + (y + 120))];
			if(jumps.size() == 0 && move.getStatus() == PositionStatus.Empty) 
				moves.add(move);
			else if(x != 600 | y != 600 && move.getStatus() != turn && 
					jump.getStatus() == PositionStatus.Empty)
			{
				jumps.add(jump);
				noJumps = false;
			}
		}
		if(noJumps)
			return moves;
		return jumps;
	}
}