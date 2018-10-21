package bulletRider;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;

public class Enemy {
	
	private double cx;
	private double cy;
	private int[] x = new int[4]; //screen positions
	private int[] y = new int[4];
	private int[] x1 = new int[4];
	private int[] y1 = new int[4];
	
	private double[] X = new double[4]; //actual positions
	private double[] Y = new double[4];
	private double[] X1 = new double[4];
	private double[] Y1 = new double[4];
	
	private double vx;
	private double vy;
	
	private final int size = 15;
	
	private double t = 0;
	
	private Color c = new Color(200, 100, 100);
	private Color c2 = new Color(200, 50, 50);
	
	private Polygon E = new Polygon(x, y, 4);
	private Polygon E1 = new Polygon(x1, y1, 4);
	
	public Enemy(int a, int b) {
		
		cx = a;
		cy = b;
		X[0] = cx + size;
		X[1] = cx - size;
		X[2] = cx - size;
		X[3] = cx + size;
		Y[0] = cy - size;
		Y[1] = cy - size;
		Y[2] = cy + size;
		Y[3] = cy + size;
		
		vx = Math.random()*2 - 1;
		vy = Math.random()*2 - 1;
		
		for(byte i = 0; i < X.length; i++)
		{
			X1[i] = X[i];
			Y1[i] = Y[i];
			x[i] = (int) X[i];
			y[i] = (int) Y[i];
			x1[i] = (int) X1[i];
			y1[i] = (int) Y1[i];
		}
		
		Polygon E = new Polygon(x, y, 4);
		Polygon E1 = new Polygon(x1, y1, 4);
	}
	
	public void draw(Graphics g)
	{
		Graphics2D g2 = (Graphics2D)g;
		Polygon E = new Polygon(x, y, 4);
		Polygon E1 = new Polygon(x1, y1, 4);
		g2.setColor(c);
		g2.fill(E);
		g2.setColor(c2);
		g2.fill(E1);
	}
	
	public void rotate(double rad)
	{
		t -= rad;
		X[0] = cx + (Math.cos(t)*size);
		X[1] = cx + (Math.cos(t + (Math.PI/2))*size);
		X[2] = cx + (Math.cos(t + Math.PI)*size);
		X[3] = cx + (Math.cos(t + (3*Math.PI/2))*size);
		
		X1[0] = cx + (Math.cos(-(t + (Math.PI/4)))*size);
		X1[1] = cx + (Math.cos(-(t + (Math.PI/2) + (Math.PI/4)))*size);
		X1[2] = cx + (Math.cos(-(t + Math.PI + (Math.PI/4)))*size);
		X1[3] = cx + (Math.cos(-(t + (3*Math.PI/2) + (Math.PI/4)))*size);
		
		Y[0] = cy + (Math.sin(t)*size);
		Y[1] = cy + (Math.sin(t + (Math.PI/2))*size);
		Y[2] = cy + (Math.sin(t + Math.PI)*size);
		Y[3] = cy + (Math.sin(t + (3*Math.PI/2))*size);
		
		Y1[0] = cy + (Math.sin(-(t + (Math.PI/4)))*size);
		Y1[1] = cy + (Math.sin(-(t + (Math.PI/2) + (Math.PI/4)))*size);
		Y1[2] = cy + (Math.sin(-(t + Math.PI + (Math.PI/4)))*size);
		Y1[3] = cy + (Math.sin(-(t + (3*Math.PI/2) + (Math.PI/4)))*size);
		
		for(byte i = 0; i < x.length; i++)
		{
			x[i] = (int) X[i];
			y[i] = (int) Y[i];
			x1[i] = (int) X1[i];
			y1[i] = (int) Y1[i];
		}
		
	}
	

	public void move(double a, double b)
	{
		cx += a;
		cy += b;
		for(byte i = 0; i < x.length; i++)
		{
			X[i] += a;
			X1[i] += a;
			Y[i] += b;
			Y1[i] += b;
		}
		
		for(byte i = 0; i < x.length; i++)
		{
			x[i] = (int) X[i];
			x1[i] = (int) X1[i];
			y[i] = (int) Y[i];
			y1[i] = (int) Y1[i];
		}
	}
	
	public Polygon area()
	{
		return E1;
	}
	
	public double cx()
	{
		return cx;
	}
	
	public double cy()
	{
		return cy;
	}
	
	public double vx()
	{
		return vx;
	}
	
	public double vy()
	{
		return vy;
	}
	
	public void changeX()
	{
		vx *= -1;
	}
	
	public void changeY()
	{
		vy *= -1;
	}

}
