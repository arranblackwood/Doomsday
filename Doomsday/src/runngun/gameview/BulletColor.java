package runngun.gameview;

import java.awt.Color;

class BulletColor {
	public static final BulletColor
	PLAYER = new BulletColor(new Color(255, 220, 180), new Color(255, 150, 0)),
	ALIEN = new BulletColor(new Color(200, 150, 255), new Color(140, 0, 255));
	
	public Color bullet, trail;
	
	public BulletColor(Color bulletColor, Color trailColor) {
		this.bullet = bulletColor;
		this.trail = trailColor;
	}
}