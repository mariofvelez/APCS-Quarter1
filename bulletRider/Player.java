package bulletRider;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;

public class Player {
	
	private double cx = 300; //center x
	private double cy = 300; //center y
	private double v = 0; //velocity
	private double t = Math.PI/2; //angle player is facing
	private final double t2 = 0.6202494859828216; //angle of the sides of the player
	private final double h = Math.hypot(5, 7);
	private double j = 0; //distance from center of Field
	private int[] x = new int[3]; //vertices in x on screen
	private int[] y = new int[3]; //vertices in y
	private double[] x1 = new double[3]; //actual vertices
	private double[] y1 = new double[3];
	private int R = (int) (Math.random()*100) + 156;
	private int G = (int) (Math.random()*100) + 156;
	private int B = (int) (Math.random()*100) + 156;
	private Color c = new Color(R, G, B);
	
	Polygon p = new Polygon(x, y, 3);
	public Player(int a, int b) 
	{
		cx = a;
		cy = b;
		x1[0] = cx;
		x1[1] = cx - 7;
		x1[2] = cx + 7;
		y1[0] = cy - 15;
		y1[1] = cy + 5;
		y1[2] = cy + 5;
		for(byte i = 0; i < x1.length; i++)
		{
			x[i] = (int) x1[i];
			y[i] = (int) y1[i];
		}
		p = new Polygon(x, y, 3);
	}
	
	public void draw(Graphics g)
	{
		
		Graphics2D g2 = (Graphics2D)g;
		p = new Polygon(x, y, 3);
		g2.setColor(changeColor());
		g2.fillPolygon(p);
		
	}
	
	public void rotate(double rad)
	{
		//CCW
		t -= rad;
		
		x1[0] = cx - (Math.cos(t)*15);
		x1[1] = cx - (Math.cos(t + (Math.PI/2) + t2)*h);
		x1[2] = cx - (Math.cos(t + (3*Math.PI/2) - t2)*h);
		y1[0] = cy - (Math.sin(t)*15);
		y1[1] = cy - (Math.sin(t + (Math.PI/2) + t2)*h);
		y1[2] = cy - (Math.sin(t + (3*Math.PI/2) - t2)*h);
		
		for(byte i = 0; i < x.length; i++)
		{
			x[i] = (int) x1[i];
			y[i] = (int) y1[i];
		}
	}
	
	public void move()
	{
		//moves by the current velocity in the direction of t
		j = Math.hypot(300 - cx, 300 - cy);
		if(j < 250)
		{
			cx -= (Math.cos(t)*v);
			cy -= (Math.sin(t)*v);
			for(byte i = 0; i < x1.length; i++)
			{
				//translate x1 and y1
				x1[i] -= (Math.cos(t)*v);
				y1[i] -= (Math.sin(t)*v);
			}
			
			for(byte i = 0; i < x.length; i++) //sets the location of vertices
			{
				x[i] = (int) x1[i];
				y[i] = (int) y1[i];
			}
		}
	}
	
	public void incVel(double i)
	{
		v += i;
	}
	
	public void decVel(double i)
	{
		v -= i;
	}
	
	public void reset()
	{
		t = Math.PI/2;
		cx = 300;
		cy = 300;
		x1[0] = cx;
		x1[1] = cx - 7;
		x1[2] = cx + 7;
		y1[0] = cy - 15;
		y1[1] = cy + 5;
		y1[2] = cy + 5;
		for(byte i = 0; i < x1.length; i++)
		{
			x[i] = (int) x1[i];
			y[i] = (int) y1[i];
		}
	}
	
	public double vel()
	{
		return v;
	}
	
	public double t()
	{
		return t;
	}
	
	public double topx()
	{
		return x[0];
	}
	
	public double topy()
	{
		return y[0];
	}
	
	public double cx()
	{
		return cx;
	}
	
	public double cy()
	{
		return cy;
	}
	
	public double j()
	{
		return j;
	}
	
	public Color c()
	{
		return c;
	}
	
	public Color changeColor()
	{
		Color C = new Color(R, G, B);
		return C;
	}
	
	public int R()
	{
		return R;
	}
	
	public int G()
	{
		return G;
	}
	
	public int B()
	{
		return B;
	}
	
	public void setR(int a)
	{
		R = a;
	}
	
	public void setG(int a)
	{
		G = a;
	}
	
	public void setB(int a)
	{
		B = a;
	}
	
	public void setV(double a)
	{
		v = a;
	}

}
