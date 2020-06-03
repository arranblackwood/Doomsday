package runngun.gameview;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

class HealthBar {
	private Point2D.Double pos;
	private double width, height, border, health;
	
	public void update(Point2D.Double anchorPoint, double yOffset, double health) {
		pos = new Point2D.Double(anchorPoint.x, anchorPoint.y + yOffset);
		this.health = health;
	}
	
	public void draw(Graphics2D g2) {
		g2.setColor(Color.WHITE);
		g2.fill(new Rectangle2D.Double(pos.x - (width / 2) - border, pos.y - border, width + (border * 2), height + (border * 2)));
		g2.setColor(Color.RED);
		g2.fill(new Rectangle2D.Double(pos.x - (width / 2), pos.y, width, height));
		g2.setColor(Color.GREEN);
		g2.fill(new Rectangle2D.Double(pos.x - (width / 2), pos.y, width * health, height));
	}
	
	public HealthBar(Dimension res) {
		width = res.width / 20;
		height = res.height / 100;
		border = 2;
	}
}
