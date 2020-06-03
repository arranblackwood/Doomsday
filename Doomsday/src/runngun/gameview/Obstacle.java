package runngun.gameview;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

@SuppressWarnings("serial")
class Obstacle extends ScrollingObject {
	private BufferedImage image;
	
	//me being lazy getting x and y from rectangle superclass
	//added car image last minute
	public void draw(Graphics2D g2) {
		g2.drawImage(image, (int)x, (int)y, (int)width, (int)height, null);
	}
	
	public Obstacle(Dimension res, double floor) {
		super(res, res.getWidth() / 8, res.getHeight() / 5, 0, floor);
		
		image = ImageManager.getImage(ImageManager.HONDA, getClass());
	}
}
