package runngun.gameview;

import java.awt.Dimension;

@SuppressWarnings("serial")
class Wall extends ScrollingObject {
	public Wall(Dimension res, double floor, double heightFromFloor) {
		super(res, res.getWidth() / 3, res.getHeight() / 7, heightFromFloor, floor);
	}
}
