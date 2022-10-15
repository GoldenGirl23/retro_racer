package sp;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Coins extends Rectangle {
	
	private static final long serialVersionUID = 1L;

	public Coins(int x, int y) {
		setBounds(x+10,y+10,6,6);
	}
	
	public void render(Graphics gr) {
		gr.setColor(Color.yellow);
		gr.fillRect(x, y, width, height);
	}

}
