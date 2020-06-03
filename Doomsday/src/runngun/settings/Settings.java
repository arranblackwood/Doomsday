package runngun.settings;

import java.awt.Dimension;

public class Settings {
	public Dimension
	resolution = new Dimension(1280, 720),//(800, 450)(1920, 1080)(1600, 900)(1440, 900)(1280, 720)
	aspectRatio = new Dimension(16, 9);
	
	public boolean
	isFullscreen = false,
	isFpsCounterEnabled = true,
	isAntiAliasingEnabled = true,
	isDebugModeEnabled = false;
}