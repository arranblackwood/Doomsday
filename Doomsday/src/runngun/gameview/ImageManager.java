package runngun.gameview;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class ImageManager {
	public static final int
	ENEMY1 = 0,
	ENEMY2 = 1,
	BACKGROUND = 2,
	WEAPON = 3,
	PLAYER1 = 4,
	PLAYER2 = 5,
	PLAYER3 = 6,
	HONDA = 7;
	
	public static <T> BufferedImage getImage(int image, Class<T> class1) {
		String imageName = "";
		try {
			switch(image) {
				case ENEMY1:
					imageName = "/enemy.png";
					break;
				case ENEMY2:
					imageName = "/enemy2.png";
					break;
				case BACKGROUND:
					imageName = "/background.png";
					break;
				case WEAPON:
					imageName = "/weapon.png";
					break;
				case PLAYER1:
					imageName = "/player1.png";
					break;
				case PLAYER2:
					imageName = "/player2.png";
					break;
				case PLAYER3:
					imageName = "/player3.png";
					break;
				case HONDA:
					imageName = "/honda.png";
					break;
				default:
					throw new IllegalArgumentException("Unknown Image");
			}
		
			return ImageIO.read(class1.getResourceAsStream(imageName));
		} catch (IOException e) {
			System.out.println("Error getting image " + imageName);
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		return null;
	}
}
