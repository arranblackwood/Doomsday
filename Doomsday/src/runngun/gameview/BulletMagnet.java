package runngun.gameview;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.geom.QuadCurve2D;

import runngun.settings.Settings;


class BulletMagnet extends Bullet {
	private Settings settings;
	private QuadCurve2D.Double trajectory;
	private int pos = 0;
	private double numPoints, numPointsFps, lastNumPointsFps;
	private long enemyId;
	
	//legit spent about 3-4 hours trying to get this to work
	//legend https://forum.unity.com/threads/constant-speed-along-bezier-curve.416781/
	
	private Point2D.Double getBezierPoint(double t){
		t /= numPointsFps;
    	double x = (((1 - t) * (1 - t)) * trajectory.getX1()) + (2 * t * (1 - t) * trajectory.getCtrlX()) + ((t * t) * trajectory.getX2());
    	double y = (((1 - t) * (1 - t)) * trajectory.getY1()) + (2 * t * (1 - t) * trajectory.getCtrlY()) + ((t * t) * trajectory.getY2());
    	return new Point2D.Double(x, y);
	}
	
	private double getDistance() {
		/*return (RNGUtils.getDistance(trajectory.getX1(), trajectory.getY1(), trajectory.getCtrlX(), trajectory.getCtrlY())
			  + RNGUtils.getDistance(trajectory.getCtrlX(), trajectory.getCtrlY(), trajectory.getX2(), trajectory.getY2())
			  + RNGUtils.getDistance(trajectory.getX1(), trajectory.getY1(), trajectory.getX2(), trajectory.getY2())) / 2;*/
		return RNGUtils.getDistance(trajectory.getX1(), trajectory.getY1(), trajectory.getX2(), trajectory.getY2());
	}
	
	public long getEnemyId() {
		return enemyId;
	}
	
	public BulletLinear getBulletLinear() {
		return new BulletLinear(getBezierPoint(pos), angle, bulletColor, settings);
	}
	
	//drawing bezier lines is really taxing on game loop
	public void drawDegug(Graphics2D g) {
		g.setColor(Color.RED);
		g.draw(trajectory);
	}
	
	public void update(Enemy enemy, int fps) {
		numPointsFps = numPoints * fps;
		System.out.println(numPointsFps);
		
		//update trajectory to enemy
		trajectory.x2 = enemy.getCenterPos().x;
		trajectory.y2 = enemy.getCenterPos().y;
		
		//try to sync pos from last fps
		pos = (int) ((pos / lastNumPointsFps) * numPointsFps);
		
		pos++;
		
		//TODO: once path ended continue at angle
		if(pos > numPointsFps) {
			isActive = false;
			return;
		}
		
		Point2D.Double p1 = getBezierPoint(pos), p2 = getBezierPoint(pos + 1);
		super.pos.setLocation(p1.x, p1.y);
		
		angle = RNGUtils.getAngle(p1.x, p1.y, p2.x, p2.y, true);
		
		updateTrail(fps);
		
		lastNumPointsFps = numPointsFps;
	}
	
	BulletMagnet(Point2D.Double startPosition, Point mousePoint, Enemy enemy, BulletColor bulletColor, Settings settings, int fps) {
		super(startPosition, bulletColor, Bullet.PLAYER, settings.resolution);
		
		this.settings = settings;
		this.enemyId = enemy.getId();
		
		trajectory = new QuadCurve2D.Double(startPosition.getX(), startPosition.getY(), mousePoint.getX(), mousePoint.getY(), enemy.getCenterPos().x, enemy.getCenterPos().y);
		Point2D.Double p1 = getBezierPoint(pos), p2 = getBezierPoint(pos + 1);
		angle = RNGUtils.getAngle(p1.x, p1.y, p2.x, p2.y, true);
		 
		//lower = faster
		this.numPoints = 0.45 * Math.pow(globalSpeed, -1) * getDistance() / res.getWidth() ;
	}
}
