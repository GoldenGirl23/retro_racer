package sp;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;


public class Grid {

	public int height;
	public int width;
	
	public  Block[][] blocks;
	public List<Coins> coins;
	public List<Fuel> f;
	public List<Stopwatch> st;
	public List <Police> pol;
	
	public static int noOfCoins = 0;
	
	public Grid() {
		coins = new ArrayList<>();
		f = new ArrayList<>();
		st = new ArrayList<>();
		pol = new ArrayList<>();
		
		try {
			BufferedImage grid = ImageIO.read(getClass().getResource("/maze6.png"));
		
		this.width = grid.getWidth();
		this.height = grid.getHeight();
		
		int[] pixels = new int[width*height];
		blocks = new Block[width][height];
		grid.getRGB(0, 0, width, height, pixels, 0, width);
		
		for(int xx=0; xx<width; xx++) {
			for(int yy=0; yy<height; yy++) {
				int n = pixels[xx + (yy*width)];
				
				//wall block
				if(n == 0xFF0026FF) {
					blocks[xx][yy] = new Block(xx*40,yy*40);
					//System.out.println(blocks[xx][yy]);
				}
				//player starting block
				else if(n == 0xFFFF0000) {
					Game.player.x = xx*40;
					Game.player.y = yy*40;
				}
				//fuel
				else if(n == 0xFFFF59F9){
					f.add(new Fuel(xx*40,yy*40));
				}	
				//stopwatch
				else if(n == 0xFF4CFF00){
					st.add(new Stopwatch(xx*40,yy*40));
				}
				
				//police
				else if(n== 0xFFFFD800) {
					pol.add(new Police(xx*40,yy*40));
				}
				
				//coins
				else {
						coins.add(new Coins(xx*40,yy*40));
						//noOfCoins++;
				}
					
			}
		}
		
		//System.out.println("Coins: " + noOfCoins);
		
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	}

	public void tick() {
		for(int i=0;i < pol.size();i++) {
			pol.get(i).tick();
		}
	}
	public void render(Graphics gr) {
		for(int x=0; x<width; x++) {
			for(int y=0; y<height; y++) {
				if (blocks[x][y] != null) {blocks[x][y].render(gr);}
				
			}
		}
		
		for(int i=0; i<coins.size(); i++) {
			coins.get(i).render(gr);
		}
		
		for(int i=0; i<f.size(); i++) {
			f.get(i).render(gr);
		}
		
		for(int i=0; i<st.size(); i++) {
			st.get(i).render(gr);
		}
		
		for(int i=0; i<pol.size();i++) {
			pol.get(i).render(gr);
		}

	}

}