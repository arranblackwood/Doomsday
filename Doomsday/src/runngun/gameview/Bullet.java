package runngun.gameview;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;


class Bullet {
	public static final int
	PLAYER = 0,
	ENEMY = 1;
	
	protected double height, width, globalSpeed;
	
	protected boolean isActive = true;
	protected double angle;
	protected Dimension res;
	protected Point2D.Double pos;
	protected BulletColor bulletColor;
	private BulletTrail bulletTrail;
	private int team;
	
	protected void updateTrail(int fps) {
		bulletTrail.addPoint(new Point2D.Double(pos.x, pos.y));
		bulletTrail.update(fps);
	}
	
	public Rectangle2D.Double getRect() {
		return new Rectangle2D.Double(pos.x, pos.y - (height / 2), width, height);
	}
	
	public double getAngle() {
		return angle;
	}
	
	public boolean isActive() {
		return isActive;
	}
	
	public int getTeam() {
		return team;
	}
	
	public void draw(Graphics2D g2) {
		AffineTransform oldTrans = g2.getTransform();
		
		//Draw Bullet
		g2.setColor(bulletColor.bullet);
		g2.rotate(angle, pos.x, pos.y);
		g2.fill(getRect());
		g2.setTransform(oldTrans);
		
		//Draw Trail
		g2.setColor(bulletColor.trail);
		bulletTrail.draw(g2);
	}
	
	Bullet(Point2D.Double startPosition, BulletColor bulletColor, int team, Dimension res) {
		super();		
		
		this.res = res;
		this.team = team;
		this.bulletColor = bulletColor;
		
		height = res.getHeight() / 100;
		width = res.getWidth() / 50;
		globalSpeed = 1d;
		
		bulletTrail = new BulletTrail(res, height);
		pos = new Point2D.Double();
	}
}
