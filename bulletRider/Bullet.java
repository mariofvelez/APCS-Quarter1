package bulletRider;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Ellipse2D;

public class Bullet {
	private int bx;
	private int by;
	private int speed;
	private double t;
	private Color c = new Color((int) (Math.random()*100) + 156, (int) (Math.random()*100) + 156, (int) (Math.random()*100) + 156);
	Ellipse2D b = new Ellipse2D.Double();
	public Bullet(int x, int y, double theta) {
		bx = x;
		by = y;
		speed = 10;
		t = theta;
		b = new Ellipse2D.Double(x - 2.5, y - 2.5, 5, 5);
	}
	
	public void move()
	{
		bx -= (Math.cos(t)*speed);
		by -= (Math.sin(t)*speed);
	}
	
	public void draw(Graphics g)
	{
		Graphics2D g2 = (Graphics2D)g;
		b = new Ellipse2D.Double(bx - 2.5, by - 2.5, 5, 5);
		g2.setColor(c);
		g2.fill(b);
	}
	
	public int bx()
	{
		return bx;
	}
	
	public int by()
	{
		return by;
	}

}
