package application;

import javafx.scene.shape.Circle;

public class Piece 
{
	private PieceStatus status; 
	private Circle circle;
	Piece(Circle circleIn, PieceStatus statusIn)
	{
		status = statusIn;
		circle = circleIn;
	}
	public PieceStatus getStatus() {return status;}
	public Circle getCircle() {return circle;}
	public String getPositionDictionaryKey() 
	{return "" + (circle.getCenterX() - 40) + (circle.getCenterY() -  40);}
	public String getPieceFinderKey() {return "" + circle.getCenterX() + circle.getCenterY();}
}
