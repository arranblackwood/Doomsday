package runngun.gameview;

import java.awt.Dimension;
import java.awt.geom.Rectangle2D;

@SuppressWarnings("serial")
class ScrollingObject extends Rectangle2D.Double {
	public double width, height, startX, startY, endX, speed;
	
	private Dimension res;
	
	private double position = 0;
	private boolean isActive = true;
	
	public boolean isActive() {
		return isActive;
	}
	
	//move object along screen
	public void increment(int fps) {
		position += (Math.pow(speed, -1) / fps) * res.getWidth();	
		isActive = startX - position > endX;
		setRect(startX - position, startY, width, height);
	}
	
	public ScrollingObject(Dimension res, double width, double height, double heightFromFloor, double floor) {
		super();
		
		this.res = res;
		this.width = width;
		this.height = height;
		startX = res.getWidth();
		startY = floor - height - heightFromFloor;
		endX = -width;
		speed = 0.8;
		
		setRect(startX, startY, width, height);
	}
}
