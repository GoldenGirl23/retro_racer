package sp;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Block extends Rectangle {

	private static final long serialVersionUID = 1L;

	public Block(int x, int y){
		setBounds(x,y,40,40);
	}
	
	public void render(Graphics gr) {
		gr.setColor(new Color(0,38,225));
		gr.fillRect(x, y, width, height);
	}
	
}
