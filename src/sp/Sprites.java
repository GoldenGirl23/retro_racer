package sp;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Sprites {

	private BufferedImage sheet;
	
	public Sprites(String path) {
		try {
			sheet= ImageIO.read(getClass().getResource(path));
		} catch(IOException e) {
			System.out.println("failed load img");
		}
	}
	
	public BufferedImage getSprite(int xx, int yy) {
		return sheet.getSubimage(xx, yy, 16, 16);
	}
}
