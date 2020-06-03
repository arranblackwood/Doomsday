package runngun.gameview;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

@SuppressWarnings("serial")
class Weapon extends Rectangle2D.Double {
	protected double startX, startY, height, width, playerHeight, recoilAmount, recoilSpeed, muzzleOffset, xOffset;
	
	private BufferedImage image;
	
	private Point2D.Double pos;
	private double recoilOffset;
	private int recoilPhase;
	private Player player;
	private Point mousePoint; 
	
	private void updatePosition(double playerY) {
		pos = new Point2D.Double(startX, playerY + (playerHeight / 3));
		setRect(pos.getX() - recoilOffset, pos.getY() - (height / 2) + muzzleOffset, width, height);
	}
	
	private void updateRecoil(double fps) {
		if(recoilPhase > 0) {
			if(recoilOffset < recoilAmount && recoilPhase == 1)
				recoilOffset += (10 / fps) * recoilSpeed;
			else if(recoilOffset >= recoilAmount || recoilPhase == 2) {
				recoilPhase = 2;
				recoilOffset -= (1 / fps) * recoilSpeed;
				if(recoilOffset <= 0) {
					recoilOffset = 0;
					recoilPhase = 0;
				}
			}
		}
		if(recoilOffset > recoilAmount)
			recoilOffset = recoilAmount;
	}
	
	public void enableRecoilAnimation() {
		recoilPhase = 1;
	}
	
	public double getRecoilOffset() {
		return recoilOffset;
	}
	
	public void update(double fps) {
		updateRecoil(fps);
		updatePosition(player.y);
	}
	
	public void draw(Graphics2D g2) {
		if(pos != null) {
			AffineTransform oldTrans = g2.getTransform();
			g2.rotate(RNGUtils.getWeaponAngle(player, mousePoint), player.startX + player.width, player.getY() + (player.height / 3));
			g2.drawImage(image, (int)(pos.x - recoilOffset - xOffset), (int)(pos.y - (height / 2) + muzzleOffset), (int)width, (int)height, null);
			g2.setTransform(oldTrans);
		}
	}

	public Weapon(Dimension res, Player player, Point mousePoint) {
		super();
		
		this.player = player;
		this.mousePoint = mousePoint;
		
		startX = player.startX + ((player.width) * 0.7);
		height = res.getHeight() / 25;
		width = res.getWidth() / 15;
		playerHeight = player.height;
		
		recoilAmount = (1 * Math.pow(10, -2)) * res.getWidth();
		recoilSpeed = (1 * Math.pow(10, -1)) * res.getWidth();
		
		muzzleOffset = height / 4;
		xOffset = player.width / 3;
		
		setRect(startX, player.startY + (player.height / 5), width, height);
		
		image = ImageManager.getImage(ImageManager.WEAPON, getClass());
	}
}
