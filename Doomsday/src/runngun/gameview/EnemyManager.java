package runngun.gameview;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.Timer;

import runngun.settings.Settings;

class EnemyManager {
	private static final int
	ENEMY_INITIAL_DELAY = 2000, 
	ENEMY_MIN_DELAY = 1000,
	ENEMY_MAX_DELAY = 3000;
	
	private Settings settings;
	private BulletManager bulletManager;
	private PointCounter cashCounter;
	
	private HashMap<Long, Enemy> enemies = new HashMap<Long, Enemy>(); //Uses time of spawn an identifier
	
	private double getDistanceFromMouse(long enemyId, Point mousePoint) {
		return RNGUtils.getDistance(enemies.get(enemyId).getCenterPos().x, enemies.get(enemyId).getCenterPos().y, mousePoint.getX(), mousePoint.getY());
	}
	
	public void reset() {
		enemies.clear();
	}
	
	public Enemy getClosestToMouse(Point mousePoint) {
		long closestEnemy = 0;
		double closestDistance = Double.MAX_VALUE;
		for (long i : enemies.keySet()) {
			double distance = getDistanceFromMouse(i, mousePoint);
			if(distance < closestDistance) {
				closestEnemy = i;
				closestDistance = distance;
			}
		}
		return enemies.get(closestEnemy);
	}
	
	public boolean enemiesPresent() {
		return enemies.size() > 0;
	}
	
	public Enemy getEnemy(long enemyId) {
		return enemies.get(enemyId);
	}
	
	public void spawn() {
		long enemyId = System.currentTimeMillis();
		enemies.put(enemyId, new Enemy(settings.resolution, bulletManager, enemyId));
	}
	
	public void draw(Graphics2D g2) {
		for (long i : enemies.keySet())
			enemies.get(i).draw(g2);
	}
	
	public void update(int fps) {
		Iterator<Long> enemyIterator = enemies.keySet().iterator();
		while (enemyIterator.hasNext()) {
			long enemyId;
			try {
				enemyId = enemyIterator.next();
			} catch (ConcurrentModificationException e) {
				enemyIterator = enemies.keySet().iterator();
				enemyId = enemyIterator.next();
			}
			
			Enemy enemy = enemies.get(enemyId);
			
			if(bulletManager.hasHit(enemy.getRect(), Bullet.PLAYER))
				enemy.damage();
			
			enemy.update(fps);
			
			//if health is less than 0 remove
			if(!enemy.isAlive()) {
				enemies.remove(enemyId);
				//update iterator
				enemyIterator = enemies.keySet().iterator();
				cashCounter.addEnemyKillPoints();
			}
		}
	}
	
	public Timer enemyAutoSpawner = new Timer(ENEMY_INITIAL_DELAY, e -> {
		new Thread(() -> spawn()).start();
		((Timer)e.getSource()).setDelay(ENEMY_MIN_DELAY + (int)(Math.random() * (ENEMY_MAX_DELAY - ENEMY_MIN_DELAY)));
	});
	
	//Must be set before update can be called
	public void setBulletManager(BulletManager bulletManager) {
		this.bulletManager = bulletManager;
	}
	
	EnemyManager(Settings settings, PointCounter cashCounter) {
		this.settings = settings;
		this.cashCounter = cashCounter;
		
		enemyAutoSpawner.start();
	}
}
