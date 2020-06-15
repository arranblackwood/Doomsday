package runngun.gameview;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;


class Enemy {
	private static final double
	TURNING_SPEED = 1,
	ANGLE_LOCK_TIME = 0.5,
	SPEED = 0.1,
	SHOOT_SPEED = 3,
	SHOOT_DELAY = 1e+5,
	MAX_HEALTH = 5;
	
	protected double startX, startY, height, width, speedX, speedY;
	
	private Dimension res;
	private Rectangle2D.Double boundary;
	private BufferedImage image;
	private Point2D.Double pos;
	private BulletManager bulletManager;
	private HealthBar healthBar;

	private double health = MAX_HEALTH, angle, angle2, shootDelay, lastFps;
	private int timeBeforeChangingAngle;
	private boolean lockAngle;
	private long enemyId;
	
	public Rectangle2D.Double getRect() {
		return new Rectangle2D.Double(pos.x, pos.y, width, height);
	}
	
	private boolean isInsideToBoundary() {
		return boundary.intersects(getRect());
	}
	
	public long getId() {
		return enemyId;
	}
	
	public void damage() {
		health--;
	}
	
	public boolean isAlive() {
		return health > 0;
	}
	
	public Point2D.Double getCenterPos() {
		return new Point2D.Double(pos.x + (width / 2), pos.y + (height / 2));
	}
	
	public void shootPlayer(int fps) {
		bulletManager.fire(getCenterPos(), fps);
	}
	
	public void update(int fps) {
		//really hacky way of trying to do random enemy movement
		//basically it just does random turns either left or right
		//until it hits a boundary then goes towards the center of the boundary until its back in the boundary
		timeBeforeChangingAngle++;
		if(timeBeforeChangingAngle > ANGLE_LOCK_TIME * fps && !lockAngle) {
			if(Math.random() > 0.5 )
				angle2 = TURNING_SPEED / fps;
			else 
				angle2 = -TURNING_SPEED / fps;
			timeBeforeChangingAngle = 0;
		}
		
		if(!lockAngle)
			angle += angle2;
		else
			angle = RNGUtils.getAngle(pos.x, pos.y, res.width * 0.75, res.height * 0.25, true);
		
		lockAngle = !isInsideToBoundary();
		
		Point2D.Double point = RNGUtils.getVector(angle, speedX / fps, speedY / fps);
		pos.setLocation(pos.x + point.x, pos.y + point.y);
		
		//SHOOTING
		double rand = Math.random();
		if(rand < SHOOT_SPEED / fps) {
			shootPlayer(fps);
			shootDelay = 0;
		}
		
		//HEALTH BAR
		healthBar.update(getCenterPos(), -res.height / 12, health / MAX_HEALTH);
	}
	
	public void draw(Graphics2D g2) {
		g2.drawImage(image, (int)pos.x, (int)pos.y, (int)width, (int)height, null);
		//g2.setColor(Color.RED);
		//g2.draw(new Line2D.Double(startX, startY, res.width / 2, res.height / 2));
		healthBar.draw(g2);
	}
	
	Enemy(Dimension res, BulletManager bulletManager, long enemyId) {
		super();
		
		this.res = res;
		this.bulletManager = bulletManager;
		this.enemyId = enemyId;
		
		boundary = new Rectangle2D.Double(res.width * 0.4, res.height * 0.05, res.width * 0.5, res.height * 0.4);
		
		height = res.height / 10;
		width = res.width / 10;
		startX = res.width / 2;
		startY = res.height / 2;
		
		//calculate spawn location by spawning on a circle circumference on the outside of the visible area
		Point2D.Double spawnOffset = RNGUtils.getVector(Math.random() * -Math.PI / 2, RNGUtils.getDistance(startX, startY, res.width, 0));
		
		startX += spawnOffset.x;
		startY += spawnOffset.y;
		
		speedX = SPEED * res.width;
		speedY = SPEED * res.height;
		
		pos = new Point2D.Double();
		pos.setLocation(startX, startY);
		
		shootDelay = SHOOT_DELAY;
		
		healthBar = new HealthBar(res);
		
		if(Math.random() > 0.5)
			image = ImageManager.getImage(ImageManager.ENEMY1, getClass());
		else 
			image = ImageManager.getImage(ImageManager.ENEMY2, getClass());
	}
}
