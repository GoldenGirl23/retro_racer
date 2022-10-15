package sp;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;

public class Police extends Rectangle{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int random=0,smart=1;
	private int state =random;
	
	private int r=0,l=1,u=2,d=3;
	
	private int dir =-1;
	
	public Random rand;
	
	private int time=0;
	private int target=60*5;
	
	private static int speed =2;
	
	private int last = -1;
	private int stopTime; //use this to time the Stopwatch power to disable police
	
	public Police(int x, int y) {
		rand = new Random();
		setBounds(x,y,32,32);

	}
	
	public void tick() {
		
		for(int i = 0; i < Game.grid.st.size(); i++) {
			if(Game.player.intersects(Game.grid.st.get(i))) {
				Game.grid.st.remove(i);
				stopTime = 3*60;
				speed = 0;
				break;
			}
		}
	
		stopTime--;
		if(stopTime==0) {
			speed = 2;
		}
		
		//this works fine without smart state
		if(state==random) {
			if (dir == r) {
				if(canMove(x+speed,y)) {
					x+=speed;
				} else {
					dir = rand.nextInt(4);
				}
			}
			
			else if (dir ==l) {
				if(canMove(x-speed,y)) {
					x-=speed;
				} else {
					dir = rand.nextInt(4);
				}
			}
			
			else if (dir ==u) {
				if(canMove(x,y-speed)) {
					y-=speed;
				} else {
					dir = rand.nextInt(4);
				}
			}
			
			else if (dir ==d) {
				if(canMove(x,y+speed)) {
					y+=speed;
				} else {
					dir = rand.nextInt(4);
				}
			}
			
			time++;
			
			if(time==target) {
				state=smart;
				time=0;
			}
		}

		else if(state == smart) {
			//move RIGHT to player
			if(x < Game.player.x) {
				if(canMove(x+speed,y)) {
					x+=speed;
					last =r;
				}
			}
			//move LEFT to player
			if(x > Game.player.x) {
				if(canMove(x-speed,y)) {
					x-=speed;
					last =l;
				}
			}
			//move DOWN to player
			if(y < Game.player.y) {
				if(canMove(x,y+speed)) {
					y+=speed;
					last=d;
				}
			}
			// move UP to player
			if(y > Game.player.y) {
				if(canMove(x,y-speed)) {
					y-=speed;
					last=u;
				}
			}

			time++;
			if(time==target/2) {
				state = random;
				time=0;
			}
		}
	}

	private boolean canMove(int nx, int ny) {
		Rectangle bounds = new Rectangle(nx, ny, width, height);
		Grid grid = Game.grid;
		
		for(int xx = 0; xx < grid.blocks.length; xx++) {
			for(int yy = 0; yy < grid.blocks[0].length; yy++) {
				if(grid.blocks[xx][yy] != null) {
					if(bounds.intersects(grid.blocks[xx][yy])) {
						return false;
					}
				}
			}
		}
		
		return true;
	}
	
	public void render(Graphics gr) {
		Sprites s = Game.sprSheet;
		if(dir==-1) {
			gr.drawImage(s.getSprite(0, 16), x, y, 32, 32, null);
			dir=d;
		}
		else {
			if(dir==r) {gr.drawImage(s.getSprite(16, 16),x,y,30,30,null);}
			if(dir==l) {gr.drawImage(s.getSprite(32, 16),x,y,30,30,null);}
			if(dir==u) {gr.drawImage(s.getSprite(48, 16),x,y,30,30,null);}
			if(dir==d) {gr.drawImage(s.getSprite(0, 16),x,y,30,30,null);}
		}
		}

}
