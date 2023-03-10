package application;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

@SuppressWarnings("serial")
public class Game implements Serializable
{
	//Tracks if playable positions are empty and what type of piece they have if not empty.
	private Status[][] positions = new Status[4][8];
	private int redCount = 0;
	private int blackCount = 0;
	private Team turn;
	private boolean canJump = false;
	public Game() 
	{
		for(int i = 0; i < 4; i++) 
		{
			for(int j = 0; j < 8; j++) 
			{
				positions[i][j] = Status.Empty; 
			}
		}
	}
	public void setTurn(Team teamIn) {turn = teamIn;}
	public Status[][] getPositions(){return positions;}
	public Team getTurn() {return turn;}
	public boolean checkForJumps() {return canJump;}
	public void setRedCount(int redCountIn) {redCount = redCountIn;}
	public void setBlackCount(int blackCountIn) {blackCount = blackCountIn;}
	public int getRedCount() {return redCount;}
	public int getBlackCount() {return blackCount;}
	public void setPosition(int x, int y, Status status) {positions[x][y] = status;}
	public void NewGame() 
	{
		for(int x = 0; x < 4; x++) 
		{
			for(int y = 0; y < 8; y++) 
			{
				if(y < 3)
				{	
					positions[x][y] = Status.Black;
					blackCount++;
				}
				else if(y > 4) 
				{
					positions[x][y] = Status.Red;
					redCount++;
				}
			}
		}
	}
	public void movePiece(int originX, int originY, int destX, int destY) 
	{
		
		if(destY == 0 && positions[originX][originY] == Status.Red)
			positions[destX][destY] = Status.RedKing;
		else if(destY == 7 && positions[originX][originY] == Status.Black)
			positions[destX][destY] = Status.BlackKing;
		else
			positions[destX][destY] = positions[originX][originY];
		positions[originX][originY] = Status.Empty;
	}
	public void removePiece(int x, int y) 
	{
		if(positions[x][y] == Status.Black || positions[x][y] == Status.BlackKing)
			blackCount --;
		else
			redCount --;
		positions[x][y] = Status.Empty;
	}
	//Scans all pieces for moves.
	public ArrayList<ArrayList<Integer>> scanForMoves() 
	{
		ArrayList<Status> otherTeam;
		ArrayList<ArrayList<Integer>> moves = new ArrayList<>();
		ArrayList<ArrayList<Integer>> jumps = new ArrayList<>();
		if(turn == Team.Red)
			otherTeam = new ArrayList<Status>(Arrays.asList(Status.Black, Status.BlackKing));
		else
			otherTeam = new ArrayList<Status>(Arrays.asList(Status.Red, Status.RedKing));
		for(int x = 0; x < 4; x++) 
		{
			for(int y = 0; y < 8; y++) 
			{
					if(otherTeam.contains(positions[x][y]) || positions[x][y] == Status.Empty)
						continue;
					if((x - (y % 2)) != -1 && y > 0 && (turn == Team.Red || positions[x][y] == Status.BlackKing))
					{
						if(canJump == false && positions[x - (y % 2)][y - 1] == Status.Empty)
							moves.add(new ArrayList<>(Arrays.asList(x, y, x - (y % 2), y - 1)));
						else if(y > 1 && x > 0 && otherTeam.contains(positions[x - (y % 2)][y - 1]) && positions[x - 1][y - 2] == Status.Empty)
						{
							canJump = true;
							jumps.add(new ArrayList<>(Arrays.asList(x, y, x - (y % 2), y - 1, x - 1, y - 2)));
						}
					}
					if((x - y % 2) != 3 && y > 0 && (turn == Team.Red || positions[x][y] == Status.BlackKing))
					{
						if(canJump == false && positions[x - (y % 2) + 1][y - 1] == Status.Empty)
							moves.add(new ArrayList<>(Arrays.asList(x, y, x - (y % 2) + 1, y - 1)));
						else if(y > 1 && x != 3 && otherTeam.contains(positions[x - (y % 2) + 1][y - 1]) && positions[x + 1][y - 2] == Status.Empty)
						{
							canJump = true;
							jumps.add(new ArrayList<>(Arrays.asList(x, y, x - (y % 2) + 1, y - 1, x + 1, y - 2 )));
						}
					}
					if((x - (y % 2)) != -1 && y < 7 && (turn == Team.Black || positions[x][y] == Status.RedKing))
					{
						if(canJump == false && positions[x - (y % 2)][y + 1] == Status.Empty)
							moves.add(new ArrayList<>(Arrays.asList(x, y, x - (y % 2), y + 1)));
						else if(y < 6 && x > 0 && otherTeam.contains(positions[x - (y % 2)][y + 1]) && positions[x - 1][y + 2] == Status.Empty)
						{
							canJump = true;
							jumps.add(new ArrayList<>(Arrays.asList(x, y, x - (y % 2), y + 1, x - 1, y + 2)));
						}
					}
					if((x - y % 2) != 3 && y < 7 && (turn == Team.Black || positions[x][y] == Status.RedKing))
					{
						if(canJump == false && positions[x - (y % 2) + 1][y + 1] == Status.Empty)
							moves.add(new ArrayList<>(Arrays.asList(x, y, x - (y % 2) + 1, y + 1)));
						else if(x != 3 && y < 6 && otherTeam.contains(positions[x - (y % 2) + 1][y + 1]) 
								&& positions[x + 1][y + 2] == Status.Empty)
						{
							canJump = true;
							jumps.add(new ArrayList<>(Arrays.asList(x, y, x - (y % 2) + 1, y + 1, x + 1, y + 2 )));
						}
					}
			}
		}
		if(canJump) 
		{
			canJump = false;
			return jumps;
		}
		return moves;
	}
	//Called after piece has jumped to check if more jumps are available.
	public ArrayList<ArrayList<Integer>> scanPieceForJumps(int x, int y) 
	{
		ArrayList<Status> otherTeam;
		ArrayList<ArrayList<Integer>> jumps = new ArrayList<>();
		if(turn == Team.Red)
			otherTeam = new ArrayList<Status>(Arrays.asList(Status.Black, Status.BlackKing));
		else
			otherTeam = new ArrayList<Status>(Arrays.asList(Status.Red, Status.RedKing));
		if(y > 1 && x > 0 && otherTeam.contains(positions[x - (y % 2)][y - 1]) && positions[x - 1][y - 2] == Status.Empty
				&& (turn == Team.Red || positions[x][y] == Status.BlackKing))
			jumps.add(new ArrayList<>(Arrays.asList(x, y, x - (y % 2), y - 1, x - 1, y - 2)));
		if(y > 1 && x != 3 && otherTeam.contains(positions[x - (y % 2) + 1][y - 1]) && positions[x + 1][y - 2] == Status.Empty
				&& (turn == Team.Red || positions[x][y] == Status.BlackKing))
			jumps.add(new ArrayList<>(Arrays.asList(x, y, x - (y % 2) + 1, y - 1, x + 1, y - 2 )));
		if(y < 6 && x > 0 && otherTeam.contains(positions[x - (y % 2)][y + 1]) && positions[x - 1][y + 2] == Status.Empty
				&& (turn == Team.Black || positions[x][y] == Status.RedKing))
			jumps.add(new ArrayList<>(Arrays.asList(x, y, x - (y % 2), y + 1, x - 1, y + 2)));
		if(x != 3 && y < 6 && otherTeam.contains(positions[x - (y % 2) + 1][y + 1]) && positions[x + 1][y + 2] == Status.Empty
			&& (turn == Team.Black || positions[x][y] == Status.RedKing))
			jumps.add(new ArrayList<>(Arrays.asList(x, y, x - (y % 2) + 1, y + 1, x + 1, y + 2 )));
		if(jumps.size() == 0)
			jumps.add(new ArrayList<>(Arrays.asList(0)));
		return jumps;
	}
	@Override
	public boolean equals(Object objectIn) 
	{
		Game gameIn = (Game)objectIn;
		for(int x = 0; x < 4; x++) 
		{
			for(int y = 0; y < 8; y++)
			if(gameIn.getPositions()[x][y] != positions[x][y])
				return false;
		}
		if(redCount != gameIn.getRedCount() || blackCount != gameIn.getBlackCount())
			return false;
		return true;
	}
	
}