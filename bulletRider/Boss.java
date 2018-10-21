package bulletRider;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.net.URL;

public class Boss {
	
	private double x; //x and y position
	private double y;
	private int health = 50; //health for the boss
	private Image ufo = null;
	
	private AffineTransform tx = AffineTransform.getTranslateInstance(x, y);
	
	public Boss(int a, int b)
	{
		x = a;
		y = b;
	}
	
	public void update(double playerX, double playerY)
	{
		//all the logic that runs for the boss
		move(0.1, 0.1); //moves the boss by (x, y)
	}
	
	public void draw(Graphics g)
	{
		//draws the boss on the screen
		Graphics2D g2 = (Graphics2D)g;
		if(ufo == null)
			ufo = getImage("ufo.png");
		g2.drawImage(ufo, tx, null);
	}
	
	public Image getImage(String path)
	{
		Image tempImage = null;
		try
		{
			URL imageURL = Boss.class.getResource(path);
			tempImage = Toolkit.getDefaultToolkit().getImage(imageURL);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return tempImage;
	}
	
	public void move(double a, double b) //moves the boss by (a, b)
	{
		tx = AffineTransform.getTranslateInstance(x, y);
		x += a;
		y += b;
		tx.scale(0.2, 0.2);
		tx.translate(a, b);
	}
	
	

}
