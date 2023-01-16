package application;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class StoneGroup implements PieceInterface
{
	private Circle circle;
	private Group stone = new Group();
	public StoneGroup(int xIn, int yIn, Status status) 
	{
		if(status == Status.Red)
			circle = new Circle((xIn + 1) * 160 - 80 * (yIn % 2) + 40, (yIn + 1) * 80 + 40, 30, Color.RED);
		else
			circle = new Circle((xIn + 1) * 160 - 80 * (yIn % 2) + 40, (yIn + 1) * 80 + 40, 30, Color.BLACK);
		stone.getChildren().add(circle);
	}
	public Group getPiece() {return stone;}
	public Circle getCircle() {return circle;}
	public void setCoordinatesWithInt(int x, int y) 
	{
		circle.setCenterX((x + 1) * 160 - 80 * (y % 2) + 40);
		circle.setCenterY((y + 1) * 80 + 40);
	}
	public void setCoordinatesWithDouble(double x, double y) 
	{
		circle.setCenterX(x);
		circle.setCenterY(y);
	}
}
