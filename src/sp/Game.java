package sp;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

public class Game extends Canvas implements Runnable, KeyListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private boolean isRunning = false;
	private static final int mainWidth = 800;
    private static final int mainHeight = 600;
    
    private Thread thread;
    
    public static Player player;
    public static Grid grid;
	public static Sprites sprSheet;
	public static int event=1;
	public boolean hard = false; 
	public static final int pause=0,play=1,gameover=2,win=3;
	public static int state=-1;
	
    public Game() {
    	Dimension dim = new Dimension(Game.mainWidth, Game.mainHeight);
    	setPreferredSize(dim);
    	
    	player = new Player(Game.mainWidth/2, Game.mainHeight/2);
    	grid = new Grid();
    	sprSheet = new Sprites("/spritesheet2.png");
    	addKeyListener(this);
    	
    	state=pause;
    }
 
    
	public synchronized void start() {
		if (isRunning) { return; }
		else {
			isRunning = true;
			thread = new Thread(this);
			thread.start();
		}
	}
	
	public synchronized void stop() {
		if (!isRunning) { return; }
		else {
			isRunning = false;
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
    

	
	private void tick() {
		//System.out.println("running");
		if(state==play) {
			player.tick();
			grid.tick();
		}

		
	}

	public void render() {
    	BufferStrategy bs = getBufferStrategy();
    	if (bs == null) {
    		createBufferStrategy(3);
    		return;
    	}
    	
    	Graphics gr = bs.getDrawGraphics();
    	gr.setColor(Color.black);
    	gr.fillRect(0, 0, mainWidth, mainHeight);
    	
	    player.render(gr);
	    grid.render(gr);

    	if(state==pause) {
    		//menu here
    		gr.setColor(Color.white);
    		gr.setFont(new Font(Font.SANS_SERIF,Font.BOLD,20));
    		gr.drawString("Press ARROW KEYS to start.",(mainWidth-250)/2,mainHeight-10);
    	}
    	
    	if(state==gameover) {
    		gr.setColor(Color.black);
    		gr.fillRect(0, 0, mainWidth, mainHeight);
    		gr.setColor(Color.white);
    		gr.setFont(new Font(Font.SANS_SERIF,Font.BOLD,40));
    		gr.drawString("GAME OVER!",(mainWidth-250)/2,(mainHeight-250)/2);
    	}
    	
    	if(state==win) {
    		gr.setColor(Color.black);
    		gr.fillRect(0, 0, mainWidth, mainHeight);
    		gr.setColor(Color.white);
    		gr.setFont(new Font(Font.SANS_SERIF,Font.BOLD,40));
    		gr.drawString("YOU WIN!",(mainWidth-250)/2,(mainHeight-250)/2);
    	}
		gr.setColor(Color.white);
		gr.setFont(new Font(Font.SANS_SERIF,Font.BOLD,20));
		gr.drawString("Lives Left: " + Integer.toString(Player.lives),mainWidth-200,mainHeight-10);
		
		gr.setColor(Color.white);
		gr.setFont(new Font(Font.SANS_SERIF,Font.BOLD,20));
		gr.drawString("Score: " + Integer.toString(Grid.noOfCoins),50,mainHeight-10);
		
    	gr.dispose();
    	bs.show();
    	
    }
	
	@Override
	public void run() {
		requestFocus();
		
		//if the current time is bigger that it should be, 
		//the delta value will compensate the tick and render if needed
		
		int fps = 0;
		double timer = System.currentTimeMillis();
		long lastTime = System.nanoTime();
		double targetTick = 60.0;
		double delta = 0;
		double ns = 1000000000/targetTick;
		
		while(isRunning) {
			long now = System.nanoTime();
			delta += (now - lastTime)/ns;
			lastTime = now;
			
			while(delta >= 1) {
				tick();
				render();
				fps++;
				delta--;
			}
			
			if(System.currentTimeMillis() - timer >= 1000) {
				System.out.println(fps);
				fps = 0;
				timer += 1000;
			}
		}
		stop();
	}

	public static void main(String[] args) {
		Game game = new Game();
		JFrame frame = new JFrame();
		
		frame.setTitle("Chase Master");
		frame.setSize(mainWidth, mainHeight);
		frame.add(game);
		frame.setResizable(false);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		
		frame.setVisible(true);
		
		game.start();
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {player.right = true;event = 1;state=play;}
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {player.left = true; event =2;state=play;}
		if (e.getKeyCode() == KeyEvent.VK_UP) {player.up = true; event =3;state=play;}
		if (e.getKeyCode() == KeyEvent.VK_DOWN) {player.down = true; event=4;state=play;}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {player.right = false;}
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {player.left = false;}
		if (e.getKeyCode() == KeyEvent.VK_UP) {player.up = false;}
		if (e.getKeyCode() == KeyEvent.VK_DOWN) {player.down = false;}
	}


	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

}
