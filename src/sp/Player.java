package sp;
import java.awt.Graphics;
import java.awt.Rectangle;


public class Player extends Rectangle{

	private static final long serialVersionUID = 1L;
	
	public boolean right, left, up, down;
	private static int speed = 3;
	private static int fuelTime = 0;
	public static int lives =3;
	private int startX=375, startY=525;
	
	public Player(int x, int y) {
		setBounds(x,y,32,32);
	}
	
	public void tick() {
		if(right && canMove(x+speed, y)) {x += speed;}
		if(left && canMove(x-speed, y)) {x -= speed;}
		if(up && canMove(x, y-speed)) {y -= speed;}
		if(down && canMove(x, y+speed)) {y += speed;}
		
		for(int i = 0; i < Game.grid.coins.size(); i++) {
			
			//coins (Total = 135)
			if(this.intersects(Game.grid.coins.get(i))) {
				Game.grid.coins.remove(i);
				Grid.noOfCoins++;
				System.out.println("Score: " + Grid.noOfCoins); //display this on screen
				break;
			}
			
		}
			
			//all coins collected
			if(Game.grid.coins.size()==0) {
				Game.state= Game.win;
				//go back to main menu from here
			}
			
			//fuel cells
			for(int i = 0; i < Game.grid.f.size(); i++) {
				if(this.intersects(Game.grid.f.get(i))) {
					Game.grid.f.remove(i);
					fuelTime = 400;
					Player.speed = 5;
					break;
				}
			}
		
			fuelTime--;
			if(fuelTime==0) {
				Player.speed = 3;
			}
			
			for(int i=0;i<Game.grid.pol.size();i++) {
				Police p = Game.grid.pol.get(i);
				
				if(p.intersects(this)) {
					lives--;
					System.out.println("Lives left: " + lives);
					x=startX;
					y=startY;
				}
				
				if(lives<=0) {
					System.out.println("Game over");
					Game.state=Game.gameover;
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
		if(Game.event==1) {gr.drawImage(s.getSprite(16, 0),x,y,32,32,null);}
		if(Game.event==2) {gr.drawImage(s.getSprite(32, 0),x,y,32,32,null);}
		if(Game.event==3) {gr.drawImage(s.getSprite(48, 0),x,y,32,32,null);}
		if(Game.event==4) {gr.drawImage(s.getSprite(0 , 0),x,y,32,32,null);}
	}
}
