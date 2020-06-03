package runngun.gameview;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Timer;

import runngun.settings.Settings;

class ScrollingObjectManager {
	public static final int OBSTACLE = 0, WALL = 1;

	private static final int OBSTACLE_INITIAL_DELAY = 2000, OBSTACLE_MIN_DELAY = 500, OBSTACLE_MAX_DELAY = 2000;

	private Settings settings;
	private Player player;
	private double floor;

	private ArrayList<ScrollingObject> scrollingObjects = new ArrayList<ScrollingObject>();

	private ScrollingObject getTouchingPlayer() {
		for (ScrollingObject scrollingObject : scrollingObjects)
			if (scrollingObject.intersects(player))
				return scrollingObject;
		return null;
	}

	// Pass scrolling object constant
	public boolean isPlayerTouching(int scrollingObject) {
		ScrollingObject touchingObject = getTouchingPlayer();
		if (touchingObject instanceof Obstacle && scrollingObject == OBSTACLE)
			return true;
		else if (touchingObject instanceof Wall && scrollingObject == WALL)
			return true;
		return false;
	}

	public void reset() {
		scrollingObjects.clear();
	}

	public void spawn(int scrollingObject) {
		if (scrollingObject == WALL)
			scrollingObjects.add(new Wall(settings.resolution, floor, player.maxJumpHeight - (player.height / 2)));
		else if (scrollingObject == OBSTACLE)
			scrollingObjects.add(new Obstacle(settings.resolution, floor));
	}

	public void draw(Graphics2D g2) {
		g2.setColor(Color.GREEN);
		for (ScrollingObject scrollingObject : scrollingObjects)
			if (scrollingObject instanceof Wall)
				g2.fill(scrollingObject);
			else if (scrollingObject instanceof Obstacle)
				((Obstacle) scrollingObject).draw(g2);
	}

	public void update(int fps) {
		// obstacles
		for (int i = 0; i < scrollingObjects.size(); i++) {
			scrollingObjects.get(i).increment(fps);
			// if obstacles are out of view remove from render list
			if (!scrollingObjects.get(i).isActive())
				scrollingObjects.remove(i);
		}
	}

	public Timer obstacleAutoSpawner = new Timer(OBSTACLE_INITIAL_DELAY, new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			spawn(ScrollingObjectManager.OBSTACLE);
			((Timer) e.getSource())
					.setDelay(OBSTACLE_MIN_DELAY + (int) (Math.random() * (OBSTACLE_MAX_DELAY - OBSTACLE_MIN_DELAY)));
		}
	});

	public ScrollingObjectManager(Settings settings, Player player, double floor) {
		this.settings = settings;
		this.player = player;
		this.floor = floor;

		obstacleAutoSpawner.start();
	}
}
