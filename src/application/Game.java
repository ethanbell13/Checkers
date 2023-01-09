package application;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import javafx.util.Pair;

public class Game implements Serializable
{
	//Time is being spent finding bugs in code. Create unit tests that identify where the code is not working.
	private PositionStatus[][] positions = new PositionStatus[4][8];
	private ArrayList<Piece> redPieces = new ArrayList<>();
	private ArrayList<Piece> blackPieces = new ArrayList<>();
//	private HashMap<String, Integer> positionFinder =  new HashMap<>();
//	private HashMap<String, Piece> pieceFinder = new HashMap<>();
	private Team turn;
	private boolean noJumps = true;
	public Game() 
	{
		for(int i = 0; i < 4; i++) 
		{
			for(int j = 0; j < 8; j++) 
			{
				positions[i][j] = PositionStatus.Empty; 
			}
		}
	}
	public void setTurn(Team teamIn) {turn = teamIn;}
	public Team getTeam() {return turn;}
	public ArrayList<Piece> getRedPieces(){return redPieces;}
	public ArrayList<Piece> getBlackPieces(){return blackPieces;}
	public boolean checkForJumps() {return noJumps;}
	public void NewGame() 
	{
		int blackPos = 0;
		int redPos = 0;
		for(int i = 0; i < 4; i++) 
		{
			for(int j = 0; j < 8; j++) 
			{
				if(j < 3)
				{
					blackPieces.add(new Piece(i, j, blackPos, PieceStatus.Stone, Team.Black));
					blackPos++;
					positions[i][j] = PositionStatus.Black;
				}
				else if(j > 4)
				{
					redPieces.add(new Piece(i, j, redPos, PieceStatus.Stone, Team.Red));
					redPos++;
					positions[i][j] = PositionStatus.Red;
				}
			}
		}
	}
	public void movePiece(Piece piece, int x, int y, Team team) 
	{
		positions[piece.getX()][piece.getY()] = PositionStatus.Empty;
		if(team == Team.Red) 
		{
			positions[x][y] = PositionStatus.Red;
			redPieces.get(redPieces.indexOf(piece)).setX(x);
			redPieces.get(redPieces.indexOf(piece)).setY(y);
		}
		else 
		{
			positions[x][y] = PositionStatus.Black;
			blackPieces.get(blackPieces.indexOf(piece)).setX(x);
			blackPieces.get(blackPieces.indexOf(piece)).setY(y);
		}
	}
	public void removePiece(Piece piece, Team team) 
	{
		if(team == Team.Red)
			redPieces.remove(piece);
		else
			blackPieces.remove(piece);
	}
	public ArrayList<Pair<Piece, ArrayList<Integer>>> scanForMoves() 
	{
		PositionStatus otherTeam;
		ArrayList<Piece> pieces = new ArrayList<>();
		ArrayList<Pair<Piece, ArrayList<Integer>>> moves = new ArrayList<>();
		ArrayList<Pair<Piece, ArrayList<Integer>>> jumps = new ArrayList<>();
		if(turn == Team.Red)
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
			PieceStatus pieceStatus = pieces.get(i).getPieceStatus();
			int x = pieces.get(i).getX();
			int y = pieces.get(i).getY();
			if((x - (y % 2)) != -1 && y > 0 && (turn == Team.Red || pieceStatus == PieceStatus.King))
			{
				PositionStatus test = positions[x - (y % 2)][y - 1];
				if(noJumps == true && positions[x - (y % 2)][y - 1] == PositionStatus.Empty)
					moves.add(new Pair<>(pieces.get(i), new ArrayList<>(Arrays.asList(x, y, x - (y % 2), y - 1))));
				else if(y > 1 && x > 0 && positions[x - (y % 2)][y - 1] == otherTeam 
						&& positions[x - 1][y - 2] == PositionStatus.Empty)
				{
					noJumps = false;
					jumps.add(new Pair<>(pieces.get(i), 
							new ArrayList<>(Arrays.asList(x, y, x - (y % 2), y - 1, x - 1, y - 2))));
				}
			}
			if((x - y % 2) != 3 && y > 0 && (turn == Team.Red || pieceStatus == PieceStatus.King))
			{
				if(noJumps == true && positions[x - (y % 2) + 1][y - 1] == PositionStatus.Empty)
					moves.add(new Pair<>(pieces.get(i), new ArrayList<>(Arrays.asList(x, y, x - (y % 2) + 1, y - 1))));
				else if(y > 1 && positions[x - (y % 2) + 1][y - 1] == otherTeam 
						&& positions[x + 1][y - 2] == PositionStatus.Empty)
				{
					noJumps = false;
					jumps.add(new Pair<>(pieces.get(i), 
							new ArrayList<>(Arrays.asList(x, y, x - (y % 2) + 1, y - 1, x + 1, y - 2 ))));
				}
			}
			if((x - (y % 2)) != -1 && y < 7 && (turn == Team.Black || pieceStatus == PieceStatus.King))
			{
				if(noJumps == true && positions[x - (y % 2)][y + 1] == PositionStatus.Empty)
					moves.add(new Pair<>(pieces.get(i), new ArrayList<>(Arrays.asList(x, y, x - (y % 2), y + 1))));
				else if(y < 6 && x > 0 && positions[x - (y % 2)][y + 1] == otherTeam 
						&& positions[x - 1][y + 2] == PositionStatus.Empty)
				{
					noJumps = false;
					jumps.add(new Pair<>(pieces.get(i), 
							new ArrayList<>(Arrays.asList(x, y, x - (y % 2), y + 1, x - 1, y + 2))));
				}
			}
			if((x - y % 2) != 3 && y < 7 && (turn == Team.Black || pieceStatus == PieceStatus.King))
			{
				if(noJumps == true && positions[x - (y % 2) + 1][y + 1] == PositionStatus.Empty)
					moves.add(new Pair<>(pieces.get(i), new ArrayList<>(Arrays.asList(x, y, x - (y % 2) + 1, y + 1))));
				else if(x != 3 && y < 6 && positions[x - (y % 2) + 1][y + 1] == otherTeam 
						&& positions[x + 1][y + 2] == PositionStatus.Empty)
				{
					noJumps = false;
					jumps.add(new Pair<>(pieces.get(i), 
							new ArrayList<>(Arrays.asList(x, y, x - (y % 2) + 1, y + 1, x + 1, y + 2 ))));
				}
			}
		}
		if(!noJumps) 
		{
			noJumps = true;
			return jumps;
		}
		return moves;
	}
	public void setPosition(int x, int y, PositionStatus status) 
	{
		positions[x][y] = status;
	}
	public void addPiece(Piece piece) 
	{
		if(piece.getTeam() == Team.Red) 
		{
			redPieces.add(piece);
			positions[piece.getX()][piece.getY()] = PositionStatus.Red;
		}
		else
		{
			blackPieces.add(piece);
			positions[piece.getX()][piece.getY()] = PositionStatus.Black;
		}
	}
}