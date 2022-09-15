package runngun.gameview;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.event.MouseInputListener;

import runngun.settings.Settings;
import runngun.statistics.FpsGraph;

 
@SuppressWarnings("serial")
public class GameView extends JPanel implements KeyListener, MouseInputListener {
	public static final int
	MAIN_LOOP_DELAY = 0,
	SHOOT_DELAY = 150,
	MG_FIRE_RATE = 50;
	
	public static final boolean
	DEBUG_VISUALS = false;

	//Settings Callback
	private JPanel glassPanel;
	//Global Settings
	private Settings settings;
	
	//Game Objects
	private Background background;
	private PointCounter pointCounter;
	private Player player;
	private Weapon weapon;
	private BulletManager bulletManager;
	private EnemyManager enemyManager;
	private ScrollingObjectManager scrollingObjectManager;

	//Misc
	private int fps;
	private long oNano;
	private boolean canShoot = true, isMachineGunEnabled;
	private double floor, weaponXLimit;
	private Point mousePoint;
	private String signature;
	private FpsGraph fpsGraph;
	
	private void resetGame() {
		scrollingObjectManager.reset();
		bulletManager.reset();
		player.reset();
		pointCounter.reset();
		pointCounter.autoIncrementer.restart();
		background.resetScroll();
		enemyManager.reset();
	}
	
	private void setPaused(boolean paused) {
		if(paused) {
			mainLoop.stop();
			machineGun.stop();
			scrollingObjectManager.obstacleAutoSpawner.stop();
			pointCounter.autoIncrementer.stop();
		}
		else {
			mainLoop.start();
			scrollingObjectManager.obstacleAutoSpawner.start();
			pointCounter.autoIncrementer.start();
			oNano = System.nanoTime();
		}
	}
	
	private void updateFPS() {
		if(oNano != 0)
			fps = (int) (1e+9 / (System.nanoTime() - oNano));
		oNano = System.nanoTime();
	}
	
	private void drawFPSCounter(Graphics2D g2) {
		if(settings.isFpsCounterEnabled) {
			g2.setColor(Color.BLACK);
			g2.setFont(new Font("Arial", Font.PLAIN, 10));
			g2.drawString(String.valueOf(fps) + " FPS", 0, 10);
		}
	}
	
	public void drawWatermark(Graphics2D g2) {
		int width = settings.resolution.width;
		int fontSize = (int) (0.03 * width);
		Font font1 = new Font("Arial", Font.BOLD, fontSize);
		Font font2 = new Font("Arial", Font.BOLD, fontSize / 3);
		//String string1 = "DEVELOPMENT BUILD";
		//String string2 = "For development purposes only (i.e. not Ian)";
		String string1 = "TESTING BUILD";
		String string2 = "Licenced to " + signature;
		g2.setColor(new Color(0, 0, 0, 50));
		g2.setFont(font1);
		g2.drawString(string1, (width - getFontMetrics(font1).stringWidth(string1)) / 2, fontSize);
		g2.setFont(font2);
		g2.drawString(string2, (width - getFontMetrics(font2).stringWidth(string2)) / 2, (int) (fontSize * 1.5));
	}
	
	public void setSignature(String signature) {
		this.signature = signature;
	}
	
	private void checkForResetConditions() {
		if(scrollingObjectManager.isPlayerTouching(ScrollingObjectManager.OBSTACLE) || player.isDead())
			resetGame();
	}
	
	private Timer 
	mainLoop = new Timer(MAIN_LOOP_DELAY, new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			background.update(fps);
			player.update(fps);
			weapon.update(fps);
			enemyManager.update(fps);
			bulletManager.update(fps);
			scrollingObjectManager.update(fps);
			
			checkForResetConditions();
			updateFPS();
			repaint();
		}
	}),
	
	shootDelay = new Timer(SHOOT_DELAY, new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			canShoot = true;
			((Timer)e.getSource()).stop();
		}
	}),
	
	machineGun = new Timer(0, new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			bulletManager.fire(fps);
			weapon.enableRecoilAnimation();
			((Timer)e.getSource()).setDelay(MG_FIRE_RATE);
		}
	});	
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D)g;
		
		//Anti-Aliasing
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		background.draw(g2);
		bulletManager.draw(g2);
		enemyManager.draw(g2);
		scrollingObjectManager.draw(g2);
		pointCounter.draw(g2);
		player.draw(g2);
		weapon.draw(g2);
		
		//drawWatermark(g2);
		drawFPSCounter(g2);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode()) {
			case KeyEvent.VK_SPACE:
				player.jump();
				break;
			case KeyEvent.VK_1:
				isMachineGunEnabled = !isMachineGunEnabled;
				break;
			case KeyEvent.VK_2:
				bulletManager.toggleBulletMagnet();
				break;
			case KeyEvent.VK_3:
				//scrollingObjectManager.spawn(ScrollingObjectManager.WALL);
				break;
			case KeyEvent.VK_ESCAPE:
				glassPanel.setVisible(!glassPanel.isVisible());
				setPaused(glassPanel.isVisible());
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(e.getPoint().getX() >= weaponXLimit)
			mousePoint.setLocation(e.getPoint());
		
		if(isMachineGunEnabled)
			machineGun.start();
		else if(canShoot) {
			bulletManager.fire(fps);
			weapon.enableRecoilAnimation();
			canShoot = false;
			shootDelay.start();
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		machineGun.stop();
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		if(e.getPoint().getX() >= weaponXLimit)
			mousePoint.setLocation(e.getPoint());
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		if(e.getPoint().getX() >= weaponXLimit)
			mousePoint.setLocation(e.getPoint());
	}
	
	public GameView(Settings settings, JPanel glassPanel) {
		this.setFocusable(true);
		this.addKeyListener(this);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		
		this.settings = settings;
		this.glassPanel = glassPanel;
		
		floor = (settings.resolution.getHeight() / 6) * 5;
		
		mousePoint = new Point();
		pointCounter = new PointCounter(settings.resolution);
		background = new Background(settings.resolution);
		player = new Player(settings.resolution, floor);
		weapon = new Weapon(settings.resolution, player, mousePoint);
		bulletManager = new BulletManager(settings, player, weapon, mousePoint);
		enemyManager = new EnemyManager(settings, pointCounter);
		scrollingObjectManager = new ScrollingObjectManager(settings, player, floor);
		
		bulletManager.setEnemyManager(enemyManager);
		enemyManager.setBulletManager(bulletManager);
		player.setScrollingObjectManager(scrollingObjectManager);
		player.setBulletManager(bulletManager);
		
		weaponXLimit = player.startX + player.width + 1;
		
		mainLoop.start();
		
		fpsGraph = new FpsGraph(new Point2D.Double(settings.resolution.width - FpsGraph.WIDTH - 10, settings.resolution.height - FpsGraph.HEIGHT - 10));
	}
	
	
	@Override
	public void keyTyped(KeyEvent e) {}
	@Override
	public void keyReleased(KeyEvent e) {}
	@Override
	public void mouseClicked(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
}