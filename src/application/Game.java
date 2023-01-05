package application;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Pair;

public class Game 
{
	//Time is being spent finding bugs in code. Create unit tests that identify where the code is not working.
	Position[] positions = new Position[32];
	ArrayList<Piece> redPieces = new ArrayList<>();
	ArrayList<Piece> blackPieces = new ArrayList<>();
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
		int x = 0;
		int y = 120;
		for(int i = 0; i < 4; i++) 
		{
			for(int j = 0; j < 8; j++) 
			{
				if(j % 2 == 1)
					x = 120;
				else
					x = 200;
				if(j < 3) 
				{
					blackPieces.add(new Piece(new Circle(i * 160 + x, 
							j * 80 + y, 30, Color.BLACK), PieceStatus.Stone));
				}
							
				else if(j > 4)
				{
					redPieces.add(new Piece(new Circle(i * 160 + x, 
							j * 80 + y, 30, Color.RED), PieceStatus.Stone));
				}
							
			}
		}
		for(int i = 0; i < 12; i++)
		{
			pieceFinder.put(blackPieces.get(i).getPieceFinderKey(), null);
			pieceFinder.put(redPieces.get(i).getPieceFinderKey(), null);
		}
		updateBoard();
	}
	public void updateBoard() 
	{
		for(int i = 0; i < redPieces.size(); i++)
			positions[positionFinder.get(redPieces.get(i).getPositionDictionaryKey())].setStatus(PositionStatus.Red);
		for(int i = 0; i < blackPieces.size(); i++)
			positions[positionFinder.get(blackPieces.get(i).getPositionDictionaryKey())].setStatus(PositionStatus.Black);
	}
	public void movePiece(Piece piece, double anchorX, double anchorY, PositionStatus team, Position position) 
	{
		positions[positionFinder.get("" + (anchorX - 40) + (anchorY - 40))].setStatus(PositionStatus.Empty);
		position.setStatus(team);
		pieceFinder.remove("" + anchorX + anchorY);
		piece.getCircle().setCenterX(position.getSquare().getX() + 40);
		piece.getCircle().setCenterY(position.getSquare().getY() + 40);
		pieceFinder.put(piece.getPieceFinderKey(), piece);
	}
	public void removePiece(Piece piece, double anchorX, double anchorY, PositionStatus team) 
	{
		if(team == PositionStatus.Red)
			redPieces.remove(piece);
		else
			blackPieces.remove(piece);
		positions[positionFinder.get("" + (anchorX - 40) + (anchorY - 40))].setStatus(PositionStatus.Empty);
		pieceFinder.remove(piece.getPieceFinderKey());
	}
	public ArrayList<Pair<Piece, ArrayList<Position>>> scanForMoves() 
	{
		PositionStatus otherTeam;
		ArrayList<Piece> pieces = new ArrayList<>();
		ArrayList<Pair<Piece, ArrayList<Position>>> allMoves = new ArrayList<>();
		ArrayList<Pair<Piece, ArrayList<Position>>> allJumps = new ArrayList<>();
		if(turn == PositionStatus.Red)
		{
			pieces = redPieces;
			otherTeam = PositionStatus.Black;
		}
		else
		{
			pieces = blackPieces;
			otherTeam = PositionStatus.Red;
		}
		for(int i = 0; i < pieces.size(); i++) 
		{
			ArrayList<Position> moves = new ArrayList<>();
			ArrayList<Position> jumps = new ArrayList<>();
			double x = pieces.get(i).getCircle().getCenterX();
			double y = pieces.get(i).getCircle().getCenterY();
			Position move = null;
			if(x != 120 && y != 120 && (pieces.get(i).getStatus() == PieceStatus.King || turn == PositionStatus.Red)) 
			{
				move = positions[positionFinder.get("" + (x - 120) + (y - 120))];
				if(noJumps && move.getStatus() == PositionStatus.Empty) 
					moves.add(move);
				else if(x != 200 && y != 200 && move.getStatus() == otherTeam && 
						positions[positionFinder.get("" + (x - 200) + (y - 200))].getStatus() == PositionStatus.Empty)
				{
					jumps.add(positions[positionFinder.get("" + (x - 200) + (y - 200))]);
					noJumps = false;
				}
			}
			
			if(x != 680 && y != 120 && (pieces.get(i).getStatus() == PieceStatus.King || turn == PositionStatus.Red)) 
			{
				move = positions[positionFinder.get("" + (x + 40) + (y - 120))];
				if(noJumps && move.getStatus() == PositionStatus.Empty) 
					moves.add(move);
				else if(x != 600 && y != 200 && move.getStatus() == otherTeam &&
						positions[positionFinder.get("" + (x + 120) + (y - 200))].getStatus() == PositionStatus.Empty)
				{
					jumps.add(positions[positionFinder.get("" + (x + 120) + (y - 200))]);
					noJumps = false;
				}
			}
			
			if(x != 120 && y != 680 && (pieces.get(i).getStatus() == PieceStatus.King || turn == PositionStatus.Black)) 
			{
				move = positions[positionFinder.get("" + (x - 120) + (y + 40))];
				if(noJumps && move.getStatus() == PositionStatus.Empty) 
					moves.add(move);
				else if(x != 200 && y != 600 && move.getStatus() == otherTeam &&
						positions[positionFinder.get("" + (x - 200) + (y + 120))].getStatus() == PositionStatus.Empty)
				{
					jumps.add(positions[positionFinder.get("" + (x - 200) + (y + 120))]);
					noJumps = false;
				}
			}
			
			if(x != 680 && y != 680 && (pieces.get(i).getStatus() == PieceStatus.King || turn == PositionStatus.Black)) 
			{
				move = positions[positionFinder.get("" + (x + 40) + (y + 40))];
				if(jumps.size() == 0 && move.getStatus() == PositionStatus.Empty) 
					moves.add(move);
				else if(x != 600 && y != 600 && move.getStatus() == otherTeam && 
						positions[positionFinder.get("" + (x + 120) + (y + 120))].getStatus() == PositionStatus.Empty)
				{
					jumps.add(positions[positionFinder.get("" + (x + 120) + (y + 120))]);
					noJumps = false;
				}
			}
			if(!noJumps && jumps.size() > 0)
				allJumps.add(new Pair<>(pieces.get(i), jumps));
			else if(noJumps && moves.size() > 0)
				allMoves.add(new Pair<>(pieces.get(i), moves));
		}
		if(noJumps)
			return allMoves;
		return allJumps;
	}
}