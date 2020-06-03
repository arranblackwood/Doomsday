package runngun.gameview;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;


class Background {
	public static final double SCROLL_SPEED = 0.05;
	
	private double scrollAmount;
	private BufferedImage image;
	private Dimension res;
	
	public void resetScroll() {
		scrollAmount = 0;
	}
	
	public void update(int fps) {
		scrollAmount += SCROLL_SPEED / fps * res.width;
	}
	
	public void draw(Graphics2D g2) {
		//Image width / 1080p width * actual width
		int width = (image.getWidth() / 1920) * res.width;
		g2.drawImage(image, (int) -scrollAmount, 0, width, res.height, null);
		//looping
		if(width - scrollAmount < res.width) {
			if(scrollAmount >= width)
				scrollAmount = 0;
			g2.drawImage(image, (int) (width - scrollAmount), 0, width, res.height, null);
		}
	}
	
	public Background(Dimension res) {
		this.res = res;
		
		image = ImageManager.getImage(ImageManager.BACKGROUND, getClass());
	}
}
