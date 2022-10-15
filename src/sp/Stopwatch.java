package sp;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Stopwatch extends Rectangle {
	
	private static final long serialVersionUID = 1L;
	Boolean exist;
	
	public Stopwatch(int x, int y) {
		setBounds(x+10,y+10,16,16);
		exist = true;
	}
	
	public void render(Graphics gr) {
		Sprites s = Game.sprSheet;
		gr.drawImage(s.getSprite(16, 32),x,y,32,32,null);
	}

}
