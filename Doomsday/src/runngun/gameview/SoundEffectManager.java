package runngun.gameview;

import java.io.BufferedInputStream;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundEffectManager {
	public static final int
	GUN_SHOT = 0;
	
	private static void logError(String soundName, Exception e) {
		System.out.println("Error playing sound " + soundName);
		e.printStackTrace();
	}
	
	public static <T> void playSound(int sound, Class<T> class1) {
		String soundName = "";
		AudioInputStream audioInputStream;
		try {
			switch(sound) {
				case GUN_SHOT:
					soundName = "/bang.wav";
					break;
				default:
					throw new IllegalArgumentException("Unknown Sound");
			}
		
			audioInputStream = AudioSystem.getAudioInputStream(new BufferedInputStream(class1.getResourceAsStream(soundName)));	
			
			Clip clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			clip.start();
		} catch (UnsupportedAudioFileException e) {
			logError(soundName, e);
		} catch (IOException e) {
			logError(soundName, e);
		} catch (LineUnavailableException e) {
			logError(soundName, e);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
	}
}
