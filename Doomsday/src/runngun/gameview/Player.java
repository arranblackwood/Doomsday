package runngun.gameview;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.Timer;


@SuppressWarnings("serial")
class Player extends Rectangle2D.Double {	
	private static final int MAX_JUMPS = 2, MAX_HEALTH = 100, ANIM_DELAY = 150, ANIM_FRAMES = 2;
	private static final double WR_JUMP_SPEED = 1.5, JUMP_SPEED = 4;
	
	public double maxJumpHeight, jumpSpeed, width, height, startX, startY, floor;
	
	private double jumpHeight, jumpHeightLinear, firstJumpHeight;
	private int jumpState, health, animFrame;
	private BufferedImage[] images;
	private ScrollingObjectManager scrollingObjectManager;
	private BulletManager bulletManager;
	private HealthBar healthBar;
	
	
	//Jump States
	//0 - No jump
	//1 - Jump off of floor
	//2 - Jump off of wall
	
	private void setJumpHeight(double jumpHeight) {
		this.jumpHeight = RNGUtils.getJumpParabola(jumpHeight, maxJumpHeight);
		super.setRect(startX, startY - this.jumpHeight - firstJumpHeight, width, height);
	}
	
	private void incrementJumpHeight(int fps) {
		jumpHeightLinear += jumpSpeed / fps;
		
		setJumpHeight(jumpHeightLinear);
		
		if(((y + height) >= floor) && (jumpHeightLinear > 0.1))
			resetJumpHeight();
	}
	
	private void resetJumpHeight() {
		setRect(startX, startY, width, height);
		jumpHeightLinear = 0;
		jumpState = 0;
		firstJumpHeight = 0;
	}
	
	private Timer animTimer = new Timer(ANIM_DELAY, e -> {
		if(animFrame < ANIM_FRAMES)
			animFrame++;
		else
			animFrame = 0;
	});
	
	public boolean isDead() {
		return health <= 0;
		//return false;
	}
	
	public void damage() {
		health--;
	}
	
	public void reset() {
		resetJumpHeight();
		health = MAX_HEALTH;
	}
	
	public void setWallRunning(boolean wallRunning) {
		if(wallRunning && jumpState != 2)
			jumpSpeed = WR_JUMP_SPEED;
		else
			jumpSpeed = JUMP_SPEED;
	}
	
	public void jump() {
		if(jumpState == 0 || (scrollingObjectManager.isPlayerTouching(ScrollingObjectManager.WALL) && jumpState == 1)) {
			this.jumpState++;
			
			if(jumpState == 2)
				firstJumpHeight = jumpHeight;
			else
				firstJumpHeight = 0;
			jumpHeight = 0;
			jumpHeightLinear = 0;
		}
	}
	
	public void update(int fps) {
		if(jumpState > 0)
			incrementJumpHeight(fps);
		
		if(bulletManager.hasHit(this, Bullet.ENEMY)) {
			damage();
			System.out.println(health);
		}
		
		healthBar.update(new Point2D.Double(x + (width / 2), y), -height * 0.2, (double)health / MAX_HEALTH);
	}
	
	public void draw(Graphics2D g2) {
		g2.drawImage(images[animFrame], (int)x, (int)y, (int)width, (int)height, null);
		//g2.setColor(Color.BLUE);
		//g2.fill(this);
		//TODO: temporary, implement health bar on hud
		healthBar.draw(g2);
	}
	
	//Must be set before update can be called
	public void setScrollingObjectManager(ScrollingObjectManager scrollingObjectManager) {
		this.scrollingObjectManager = scrollingObjectManager;
	}
	
	public void setBulletManager(BulletManager bulletManager) {
		this.bulletManager = bulletManager;
	}
	
	public Player(Dimension res, double floor) {
		super();
		
		this.maxJumpHeight = res.getHeight() / 3;
		this.width = res.getWidth() / 20;
		this.height = res.getHeight() / 6;
		this.startX = (res.getWidth() / 6) - (width / 2);
		this.startY = floor - height;
		this.floor = floor;
		
		jumpSpeed = JUMP_SPEED;
		health = MAX_HEALTH;
		
		healthBar = new HealthBar(res);
		
		setRect(startX, startY, width, height);
		
		images = new BufferedImage[]{
			ImageManager.getImage(ImageManager.PLAYER1, getClass()),
			ImageManager.getImage(ImageManager.PLAYER2, getClass()),
			ImageManager.getImage(ImageManager.PLAYER3, getClass())
		};
		
		animTimer.start();
	}
}
