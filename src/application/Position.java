package application;

import javafx.scene.shape.Rectangle;

public class Position 
{
	private PositionStatus status;
	private Rectangle square;
	
	public Position(Rectangle squareIn, PositionStatus statusIn) 
	{
		status = statusIn;
		square = squareIn;
	}
	public Position(Rectangle squareIn) 
	{
		square = squareIn;
	}
	public PositionStatus getStatus() {return status;}
	public Rectangle getSquare() {return square;}
	public void setStatus(PositionStatus statusIn) {status = statusIn;}
}
