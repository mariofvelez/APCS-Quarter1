package bulletRider;

import java.awt.geom.Ellipse2D;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.net.URL;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.FileReader;

public class Field extends Canvas implements KeyListener, MouseMotionListener, MouseListener, Runnable
{
	//private Object[][] grid = new Object[50][50];
	private Graphics bufferGraphics; // graphics for backbuffer
	private BufferStrategy bufferStrategy;
	
	File HIGHSCORE = new File("C:/Users/Mario Velez/Desktop/bulletRider.txt");
	
	private boolean alive = false;
	private int lives = 0;
	private double fuel = 100;
	private int ammo = 5;
	
	private int score = 0;
	private int highscore = 0;
	
	private int R = 0;
	private int G = 0;
	private int B = 0;
	
	private int eSpawn = (int) (Math.random()*4);
	
	private boolean opt = false;
	
	int mousex = 0; //mouse values
	int mousey = 0;
	
	double theta = 0;
	double termV = 4; //terminal velocity of the ship
	
	ArrayList<Double> px = new ArrayList<Double>(); //particle locations
	ArrayList<Double> py = new ArrayList<Double>();
	ArrayList<Double> pvx = new ArrayList<Double>();
	ArrayList<Double> pvy = new ArrayList<Double>();
	ArrayList<Double> pd = new ArrayList<Double>(); //diameter of the particle
	
	Font font = new Font("Arial", 0, 15); //font
	Font font1 = new Font("Airstrike Regular", 0, 80);
	Font font2 = new Font("Airstrike Regular", 0, 60);
	Font font3 = new Font("Airstrike Regular", 0, 30);
	Font font4 = new Font("Airstrike Regular", 0, 15);
	Color c = new Color(50, 50, 50);
	Color c1 = new Color(200, 200, 200);
	
	private int runTime = 0;
	private int runTime1 = 0;
	
	private ArrayList<Integer> keysDown; //holds all the keys being held down
	
	private Thread thread;
	
	private boolean running;
	
	Ellipse2D Circle = new Ellipse2D.Double(0, 0, 50, 50); //round objects
	
	Player p = new Player(300, 300);
	Player p1 = new Player(200, 300);
	ArrayList<Bullet> b = new ArrayList<Bullet>();
	ArrayList<Enemy> E = new ArrayList<Enemy>();
	
	Boss boss = new Boss(0, 0);
	
	
	public Field(Dimension size) throws Exception
	{
		this.setPreferredSize(size);
		this.addKeyListener(this);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		
		this.thread = new Thread(this);
		running = true;
		
		
		
		
		keysDown = new ArrayList<Integer>();
	}

	public void paint(Graphics g)
	{
		
		Graphics2D g2 = (Graphics2D)g;
		
		if (bufferStrategy == null)
		{
			this.createBufferStrategy(2);
			bufferStrategy = this.getBufferStrategy();
			bufferGraphics = bufferStrategy.getDrawGraphics();
			
			this.thread.start();
		}
	}
	
	@Override
	public void run()
	{	
		//what runs when editor is running
		while (running)
		{
			if(alive)
			{
				DoLogic();
				Draw();
			}
			else
			{
				if(opt)
				{
					Draw2();
					DoLogic2();
				}
				else
				{
					Draw1();
					DoLogic1();
				}
			}
			DrawBackbufferToScreen();
			
			Thread.currentThread();
			try
			{
				Thread.sleep(10);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			
			
		}
	}
	
	public void DrawBackbufferToScreen()
	{
		bufferStrategy.show();
		
		Toolkit.getDefaultToolkit().sync();
	}
	
	public void DoLogic()
	{
		runTime ++;
		
		theta = p.t();
		if (keysDown.contains(new Integer(KeyEvent.VK_W)))
		{
			if(fuel > 0)
			{
				addThrust();
				fuel -= 0.5;
				p.incVel(0.02);
				score++;
			}
		}
		else
		{
			if(fuel < 100)
				fuel += 0.3;
			if(p.vel() > 0)
				p.decVel(0.005);
		}
		p.move();
		
		if(p.j() > 250) //player dies
		{
			explode(p.cx(), p.cy());
			p.reset();
			lives --;
		}
		
		for(int i = 0; i < E.size(); i++)
		{
			//player hits an enemy
			if(Math.hypot(p.cx() - E.get(i).cx(), p.cy() - E.get(i).cy()) < 20)
			{
				explode(p.cx(), p.cy());
				p.reset();
				lives --;
			}
		}
		
		if (keysDown.contains(new Integer(KeyEvent.VK_A)))
			p.rotate(0.06);
		if (keysDown.contains(new Integer(KeyEvent.VK_D)))
			p.rotate(-0.06);
		for(int i = 0; i < E.size(); i++)
		{
			E.get(i).rotate(0.1);
			E.get(i).move(E.get(i).vx(), E.get(i).vy());
			
			
			if(E.get(i).cx() > 600 || E.get(i).cx() < 0)
			{
				E.get(i).changeX();
				//System.out.println("detected");
			}
			
			if(E.get(i).cy() > 600 || E.get(i).cy() < 0)
			{
				E.get(i).changeY();
			}
		}

		
		for(int i = 0; i < b.size(); i++) //bullet
		{
			b.get(i).move();
		
			for(int j = 0; j < E.size(); j++)
			{
				if(i >= 0 && Math.hypot(E.get(j).cx() - b.get(i).bx(), E.get(j).cy() - b.get(i).by()) < 15)
				{
					explodeEnemy(E.get(j).cx(), E.get(j).cy());
					E.remove(j);
					j--;
					b.remove(i);
					i--;
					score += 500;
					
				}
				
				
			}
			if(i >= 0 && (b.get(i).bx() > 600 || b.get(i).bx() < 0 || b.get(i).by() > 600 || b.get(i).by() < 0))
			{
				b.remove(i);
				i--;
			}
		}
		
		for(int i = 0; i < px.size(); i++) //particle
		{
			pd.set(i, pd.get(i) - 0.1);
			px.set(i, px.get(i) + pvx.get(i));
			py.set(i, py.get(i) + pvy.get(i));
			if(py.get(i) < 0 || py.get(i) > 600)
				pvy.set(i, pvy.get(i)*-1);
			if(px.get(i) < 0 || px.get(i) > 600)
				pvx.set(i, pvx.get(i)*-1);
			if(pd.get(i) <= 0)
			{
				removeP(i);
				i--;
			}
			
		}
		
		if(runTime%5000 == 999)
			score += 5000;
		
		//commented out so you can work on the boss without enemies getting in the way
		
//		if(runTime%100 == 1)
//		{
//			//add another enemy
//			eSpawn = (int) (Math.random()*4);
//			if(eSpawn == 0)
//				E.add(new Enemy(0, (int) (Math.random()*600)));
//			if(eSpawn == 1)
//				E.add(new Enemy(600, (int) (Math.random()*600)));
//			if(eSpawn == 2)
//				E.add(new Enemy((int) (Math.random()*600), 0));
//			if(eSpawn == 3)
//				E.add(new Enemy((int) (Math.random()*600), 600));
//			
//		}
		
		if(lives <= 0)
		{
			alive = false;
			E.removeAll(E);
			score = 0;
			runTime = 0;
			p.setV(0);
			fuel = 100;
		}
		if(score > highscore)
			highscore = score;
		
		boss.update(p.cx(), p.cy());
		
	}
	
	public void DoLogic1()
	{
		runTime1 ++;
		if(runTime1%75 == 0)
			explodeEnemy(Math.random()*600, Math.random()*600);
		
		for(int i = 0; i < px.size(); i++) //particle
		{
			pd.set(i, pd.get(i) - 0.1);
			px.set(i, px.get(i) + pvx.get(i));
			py.set(i, py.get(i) + pvy.get(i));
			if(py.get(i) < 0 || py.get(i) > 600)
				pvy.set(i, pvy.get(i)*-1);
			if(px.get(i) < 0 || px.get(i) > 600)
				pvx.set(i, pvx.get(i)*-1);
			if(pd.get(i) <= 0)
			{
				removeP(i);
				i--;
			}
			
		}
		
		
		
		
	}
	
	public void DoLogic2()
	{
		p.changeColor();
		p1.changeColor();
	}
	
	public void Draw()
	{
		//clears the backbuffer
		bufferGraphics = bufferStrategy.getDrawGraphics();
		try
		{
			bufferGraphics.clearRect(0, 0, this.getSize().width, this.getSize().height);
			//where everything will be drawn to the backbuffer
			Graphics2D g2 = (Graphics2D)bufferGraphics;
			g2.setFont(font);
			g2.setColor(c1);
			g2.fillRect(0, 0, 600, 600);
			g2.setColor(c);
			g2.fillOval(50, 50, 500, 500);
			g2.setColor(Color.green);
			g2.fillRect(20, 20, (int) fuel, 20);
			g2.setColor(Color.white);
			g2.setFont(font4);
			g2.drawString("Fuel", 130, 35);
			g2.drawRect(18, 18, 103, 23);
			
			g2.drawString("Health", 130, 65);
			g2.drawRect(18, 48, 103, 23);
			g2.setColor(Color.red);
			g2.fillRect(20, 50, lives*33 + 1, 20);
			g2.setFont(font4);
			
			g2.setColor(Color.white);
			g2.drawString("SCORE: " + score, 20, 90);
			
			g2.setColor(Color.orange);
			for(int i = 0; i < px.size(); i++)
			{
				if(pd.get(i) > 2)
					g2.setColor(Color.yellow);
				if(pd.get(i) > 4.5)
					g2.setColor(Color.WHITE);
				if(pd.get(i) > 5)
					g2.setColor(p.changeColor());
				Ellipse2D particle = new Ellipse2D.Double(px.get(i) - (pd.get(i)/2), py.get(i) - (pd.get(i)/2), pd.get(i), pd.get(i));
				g2.fill(particle);
			}
			for(int i = 0; i < E.size(); i++)
			{
				E.get(i).draw(g2);
			}
			if(alive)
				p.draw(g2);
			for(int i = 0; i < b.size(); i++)
			{
				b.get(i).draw(g2);
			}
			g2.setColor(Color.WHITE);
			boss.draw(g2);
			
			
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally 
		{
			bufferGraphics.dispose();
		}
	}
	
	public void Draw1()
	{
		//clears the backbuffer
		bufferGraphics = bufferStrategy.getDrawGraphics();
		try
		{
			bufferGraphics.clearRect(0, 0, this.getSize().width, this.getSize().height);
			//where everything will be drawn to the backbuffer
			Graphics2D g2 = (Graphics2D)bufferGraphics;
			g2.setColor(Color.BLACK);
			g2.fillRect(0, 0, 600, 600);
			g2.setColor(Color.orange);
			for(int i = 0; i < px.size(); i++)
			{
				if(pd.get(i) > 2)
					g2.setColor(Color.yellow);
				if(pd.get(i) > 4.5)
					g2.setColor(Color.WHITE);
				if(pd.get(i) > 5)
					g2.setColor(p.changeColor());
				Ellipse2D particle = new Ellipse2D.Double(px.get(i) - (pd.get(i)/2), py.get(i) - (pd.get(i)/2), pd.get(i), pd.get(i));
				g2.fill(particle);
			}
			g2.setColor(Color.WHITE);
			g2.setFont(font1);
			g2.drawString("BULLET", 90, 100);
			g2.setFont(font2);
			g2.drawString("RIDER", 220, 160);
			if(mousex > 200 && mousex < 400 && mousey > 400 && mousey < 450)
				g2.setColor(Color.GREEN);
			else
				g2.setColor(Color.BLUE);
			g2.fillRoundRect(200, 400, 200, 50, 15, 15);
			if(mousex > 200 && mousex < 400 && mousey > 475 && mousey < 525)
				g2.setColor(Color.GREEN);
			else
				g2.setColor(Color.BLUE);
			g2.fillRoundRect(200, 475, 200, 50, 15, 15);
			g2.setFont(font3);
			g2.setColor(Color.WHITE);
			g2.drawString("Highscore: " + highscore, 150, 200);
			g2.setColor(Color.BLACK);
			g2.drawString("START", 250, 430);
			g2.drawString("Options", 230, 505);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			bufferGraphics.dispose();
		}
	}
	
	public void Draw2()
	{
		//clears the backbuffer
		bufferGraphics = bufferStrategy.getDrawGraphics();
		try
		{
			bufferGraphics.clearRect(0, 0, this.getSize().width, this.getSize().height);
			//where everything will be drawn to the backbuffer
			Graphics2D g2 = (Graphics2D)bufferGraphics;
			g2.fillRect(0,  0,  600,  600);
			p1.draw(g2);
			if(mousex > 25 && mousex < 75 && mousey > 25 && mousey < 50)
				g2.setColor(Color.green);
			else
				g2.setColor(Color.blue);
			g2.fillRoundRect(25,  25, 50, 25, 15, 15);
			g2.setFont(font4);
			g2.setColor(Color.black);
			g2.drawString("BACK", 30, 42);
			g2.setColor(Color.red);
			g2.fillRoundRect(400, 200, 127, 25, 15, 15);
			g2.setColor(Color.GREEN);
			g2.fillRoundRect(400, 250, 127, 25, 15, 15);
			g2.setColor(Color.blue);
			g2.fillRoundRect(400, 300, 127, 25, 15, 15);
			g2.setColor(Color.white);
			g2.drawLine(400 + (p.R()/2), 200, 400 + (p.R()/2), 225);
			g2.drawLine(400 + (p.G()/2), 250, 400 + (p.G()/2), 275);
			g2.drawLine(400 + (p.B()/2), 300, 400 + (p.B()/2), 325);
			
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			bufferGraphics.dispose();
		}
	}
	
	@Override
	public void keyPressed(KeyEvent e)
	{
		if (!keysDown.contains(e.getKeyCode()) && e.getKeyCode() != 86)
			keysDown.add(new Integer(e.getKeyCode()));
		
	}
	
	@Override
	public void keyReleased(KeyEvent e)
	{
		keysDown.remove(new Integer(e.getKeyCode()));
	}
	
	@Override
	public void keyTyped(KeyEvent e)
	{
//		if(new Integer(e.getKeyCode()) == 0)
//		{
//			b.add(new Bullet((int) p.topx(), (int) p.topy(), theta));
//			System.out.println("detected");
//		}
//		else
//		{
//			
//		}
	}
	
	
	
	public void Sleep(int time)
	{
		try
		{
			thread.sleep(time);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	public void addThrust()
	{
		px.add(p.cx());
		py.add(p.cy());
		pd.add(5.0);
		pvx.add((Math.cos(p.t())*termV/2) + (Math.random() - 0.5));
		pvy.add((Math.sin(p.t())*termV/2) + (Math.random() - 0.5));
	}
	
	public void removeP(int i)
	{
		px.remove(i);
		py.remove(i);
		pd.remove(i);
		pvx.remove(i);
		pvy.remove(i);
	}
	
	public void explode(Double x, Double y)
	{
		for(int i = 0; i < p.vel()*Math.sqrt(p.vel())*5; i++)
		{
			px.add(x);
			py.add(y);
			pd.add(8.0);
			pvx.add(Math.random()*15 - 7.5);
			pvy.add(Math.random()*15 - 7.5);
		}
	}
	public void explodeEnemy(Double x, Double y)
	{
		for(int i = 0; i < 10; i++)
		{
			px.add(x);
			py.add(y);
			pd.add(8.0);
			pvx.add(Math.random()*7 - 3.5);
			pvy.add(Math.random()*7 - 3.5);
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		b.add(new Bullet((int) p.topx(), (int) p.topy(), theta));
		ammo --;
		if(mousex > 200 && mousex < 400 && mousey > 400 && mousey < 450 && !alive && !opt)
		{
			lives = 3;
			alive = true;
		}
		if(mousex > 200 && mousex < 400 && mousey > 475 && mousey < 525 && !alive)
		{
			opt = true;
		}
		if(mousex > 25 && mousex < 75 && mousey > 25 && mousey < 50 && !alive && opt)
		{
			opt = false;
			alive = false;
		}
		
		if(!alive && opt)
		{
			if(mousex > 400 && mousex < 527 && mousey > 200 && mousey < 225)
			{
				p.setR((mousex - 400)*2);
				p1.setR((mousex - 400)*2);
			}
			if(mousex > 400 && mousex < 527 && mousey > 250 && mousey < 275)
			{
				p.setG((mousex - 400)*2);
				p1.setG((mousex - 400)*2);
			}
			if(mousex > 400 && mousex < 527 && mousey > 300 && mousey < 325)
			{
				p.setB((mousex - 400)*2);
				p1.setB((mousex - 400)*2);
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		//System.out.println("Dragged");
		mousex = e.getX();
		mousey = e.getY();
		if(!alive && opt)
		{
			if(mousex > 400 && mousex < 527 && mousey > 200 && mousey < 225)
			{
				p.setR((mousex - 400)*2);
				p1.setR((mousex - 400)*2);
			}
			if(mousex > 400 && mousex < 527 && mousey > 250 && mousey < 275)
			{
				p.setG((mousex - 400)*2);
				p1.setG((mousex - 400)*2);
			}
			if(mousex > 400 && mousex < 527 && mousey > 300 && mousey < 325)
			{
				p.setB((mousex - 400)*2);
				p1.setB((mousex - 400)*2);
			}
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		
			mousex = e.getX();
			mousey = e.getY();
	}

}
