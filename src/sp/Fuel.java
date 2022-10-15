package sp;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Fuel extends Rectangle {
	
	private static final long serialVersionUID = 1L;
	Boolean exist;
	
	public Fuel(int x, int y) {
		setBounds(x+10,y+10,16,16);
		//exist = true;
	}
	
	public void render(Graphics gr) {
		Sprites s = Game.sprSheet;
		gr.drawImage(s.getSprite(0, 32),x,y,32,32,null);
	}

}
