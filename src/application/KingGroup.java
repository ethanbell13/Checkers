package application;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

public class KingGroup 
{
	private double x;
	private double y;
	private Circle pieceBase;
	private Rectangle crownBase;
	private Polygon firstProng;
	private Polygon secondProng;
	private Polygon thirdProng;
	private Circle firstProngCircle;
	private Circle secondProngCircle;
	private Circle thirdProngCircle;
	private Group king = new Group();
	public KingGroup(int xIn, int yIn, Status status) 
	{
		x = (xIn + 1) * 160 - 80 * (yIn % 2) + 40;
		y = (yIn + 1) * 80 + 40;
		if(status == Status.Red) 
			pieceBase = new Circle(x, y, 30, Color.RED);
		else 
			pieceBase = new Circle(x, y, 30, Color.BLACK);
		crownBase = new Rectangle(x - 15 * Math.sqrt(2), y - 10 + 15 * Math.sqrt(2), 30 * Math.sqrt(2), 10);
		crownBase.setFill(Color.ANTIQUEWHITE);
		firstProng = new Polygon(x - 15 * Math.sqrt(2), y - 10 + 15 * Math.sqrt(2), x - 10 * Math.sqrt(2), 
				y - 35 + 15 * Math.sqrt(2), x - 5 * Math.sqrt(2), y - 10 + 15 * Math.sqrt(2));
		firstProng.setFill(Color.ANTIQUEWHITE);
		secondProng = new Polygon(x - 5 * Math.sqrt(2), y - 10 + 15 * Math.sqrt(2), x, y - 35 + 15 * Math.sqrt(2),
				  x + 5 * Math.sqrt(2), y - 10 + 15 * Math.sqrt(2));
		secondProng.setFill(Color.ANTIQUEWHITE);
		thirdProng = new Polygon(x + 5 * Math.sqrt(2), y - 10 + 15 * Math.sqrt(2), x + 10 * Math.sqrt(2), 
				y - 35 + 15 * Math.sqrt(2), x + 15 * Math.sqrt(2), y - 10 + 15 * Math.sqrt(2));
		thirdProng.setFill(Color.ANTIQUEWHITE);
		firstProngCircle = new Circle(x - 10 * Math.sqrt(2), y - 40 + 15 * Math.sqrt(2), 5, Color.ANTIQUEWHITE);
		secondProngCircle = new Circle(x, y - 40 + 15 * Math.sqrt(2), 5, Color.ANTIQUEWHITE);
		thirdProngCircle = new Circle(x + 10 * Math.sqrt(2), y - 40 + 15 * Math.sqrt(2), 5, Color.ANTIQUEWHITE);
		king.getChildren().addAll(pieceBase, crownBase, firstProng, secondProng, thirdProng, firstProngCircle, secondProngCircle,
								  thirdProngCircle);
	}
	public Group getKing() {return king;}
}
