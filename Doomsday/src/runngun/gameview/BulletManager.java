package runngun.gameview;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import runngun.settings.Settings;


class BulletManager {
	private Settings settings;
	private Player player;
	private Weapon weapon;
	private EnemyManager enemyManager;
	private Point mousePoint;
	
	private List<Bullet> bullets = new ArrayList<Bullet>();
	private boolean bulletMagnetEnabled;
	
	private Point2D.Double getWeaponMuzzlePoint() {
		Point2D.Double muzzlePos = RNGUtils.getVector(RNGUtils.getWeaponAngle(player, mousePoint), weapon.width - weapon.getRecoilOffset() - weapon.xOffset);
		return new Point2D.Double(player.startX + player.width + muzzlePos.getX(), player.getY() + (player.height / 3) + muzzlePos.getY());
	}
	
	private void updateBulletMagnet(BulletMagnet bullet, int i, int fps) {
		long enemyId = bullet.getEnemyId();
		Enemy enemy = enemyManager.getEnemy(enemyId);
		if(enemy != null)
			bullet.update(enemy, fps);
		else
			bullets.set(i, bullet.getBulletLinear());
	}
	
	public void toggleBulletMagnet() {
		bulletMagnetEnabled = !bulletMagnetEnabled;
	}
	
	public void reset() {
		bullets.clear();
	}
	
	public boolean hasHit(Rectangle2D.Double rect, int fireTeam) {
		for (int i = 0; i < bullets.size(); i++) {
			if(bullets.get(i).getTeam() == fireTeam && rect.intersects(bullets.get(i).getRect())) {
				bullets.remove(i);
				return true;
			}
		}
		return false;
	}
	
	//enemy
	public void fire(Point2D.Double startPos, int fps) {
		bullets.add(new BulletLinear(startPos, new Point((int)player.getCenterX(), (int)player.getCenterY()), BulletColor.ALIEN, Bullet.ENEMY, settings));
	}
	
	//player
	public void fire(int fps) {
		if(enemyManager.enemiesPresent() && bulletMagnetEnabled)
			bullets.add(new BulletMagnet(getWeaponMuzzlePoint(), mousePoint, enemyManager.getClosestToMouse(mousePoint), BulletColor.PLAYER, settings, fps));
		else
			bullets.add(new BulletLinear(getWeaponMuzzlePoint(), mousePoint, BulletColor.PLAYER, Bullet.PLAYER, settings));
		SoundEffectManager.playSound(SoundEffectManager.GUN_SHOT, getClass());
	}
	
	public void draw(Graphics2D g2) {
		for(Bullet bullet : bullets) {
			bullet.draw(g2);
			if((settings.isDebugModeEnabled) && bullet instanceof BulletMagnet)
				((BulletMagnet) bullet).drawDegug(g2);
		}
	}
	
	public void update(int fps) {
		for (int i = 0; i < bullets.size(); i++) {
			if(bullets.get(i) instanceof BulletLinear)
				((BulletLinear) bullets.get(i)).update(fps);
			else if(bullets.get(i) instanceof BulletMagnet)
				updateBulletMagnet(((BulletMagnet) bullets.get(i)), i, fps);
			
			//if bullets are out of view remove from render list
			if(!bullets.get(i).isActive())
				bullets.remove(i);
		}
	}
	
	//Must be set before update can be called
	public void setEnemyManager(EnemyManager enemyManager) {
		this.enemyManager = enemyManager;
	}
	
	public BulletManager(Settings settings, Player player, Weapon weapon, Point mousePoint) {
		this.settings = settings;
		this.player = player;
		this.weapon = weapon;
		this.mousePoint = mousePoint;
	}
}
