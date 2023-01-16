package application;

import javafx.scene.Group;
import javafx.scene.shape.Circle;

public interface PieceInterface 
{
	public Group getPiece();
	public Circle getCircle();
	public void setCoordinatesWithInt(int x, int y);
	public void setCoordinatesWithDouble(double x, double y);
}
