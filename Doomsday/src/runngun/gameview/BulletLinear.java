package runngun.gameview;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import runngun.settings.Settings;


class BulletLinear extends Bullet {
	private static final double
	ENEMY_INACCURACY = 0.3;
	
	protected double speed, speedX, speedY;
	
	private Point2D.Double vector;
	
	private void setSpeed(Settings settings) {
		speed = 20d * globalSpeed;
		speedX = speed / settings.aspectRatio.width;
		speedY = speed / settings.aspectRatio.height;
	}
	
	public void update(int fps) {
		pos.x += (vector.x / fps);
		pos.y += (vector.y / fps);
		
		if(!new Rectangle2D.Double(0, 0, res.getWidth(), res.getHeight()).contains(pos))
			isActive = false;
		
		updateTrail(fps);
	}
	
	BulletLinear(Point2D.Double startPosition, double angle, BulletColor bulletColor, Settings settings) {
		super(startPosition, bulletColor, Bullet.PLAYER, settings.resolution);
		pos = startPosition;
		
		setSpeed(settings);
		
		super.angle = angle;
		this.vector = RNGUtils.getVector(angle, speedX * res.getWidth(), speedY * res.getHeight());
	}
	
	//start double, end int because mouse event is only accurate to int
	BulletLinear(Point2D.Double startPosition, Point endPoint, BulletColor bulletColor, int team, Settings settings) {
		super(startPosition, bulletColor, team, settings.resolution);
		pos = startPosition;		
		
		setSpeed(settings);
		
		super.angle = RNGUtils.getAngle(startPosition.getX(), startPosition.getY() - (height / 2), endPoint.getX(), endPoint.getY() - (height / 2), true);
		if(team == Bullet.ENEMY)
			super.angle += (Math.random() * ENEMY_INACCURACY) - (ENEMY_INACCURACY / 2);
		
		this.vector = RNGUtils.getVector(angle, speedX * res.getWidth(), speedY * res.getHeight());
	}
}
