package application;

public class Piece 
{
//	private PieceStatus status; 
//	private Circle circle;
//	Piece(Circle circleIn, PieceStatus statusIn)
//	{
//		status = statusIn;
//		circle = circleIn;
//	}
//	public PieceStatus getStatus() {return status;}
//	public Circle getCircle() {return circle;}
//	public String getPositionDictionaryKey() 
//	{return "" + (circle.getCenterX() - 40) + (circle.getCenterY() -  40);}
//	public String getPieceFinderKey() {return "" + circle.getCenterX() + circle.getCenterY();}
	private int y;
	private int x;
	private int arrayPos;
	private PieceStatus pieceStatus;
	private Team team;
	
	Piece(int xIn, int yIn, int arrayPosIn, PieceStatus pieceStatusIn, Team teamIn)
	{
		x = xIn;
		y = yIn;
		arrayPos = arrayPosIn;
		pieceStatus = pieceStatusIn;
		team = teamIn;
	}
	
	public void setX(int xIn) {x = xIn;}
	public void setY(int yIn) {y = yIn;}
	public void setPieceStatus(PieceStatus pieceStatusIn) {pieceStatus = pieceStatusIn;}
	public void setTeam(Team teamIn) {team = teamIn;}
	public int getX() {return x;}
	public int getY() {return y;}
	public int getArrayPos() {return arrayPos;}
	public PieceStatus getPieceStatus() {return pieceStatus;}
	public Team getTeam() {return team;}
	public String getCoordinates() {return "" + x + y;}
}
