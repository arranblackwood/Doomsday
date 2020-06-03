package runngun.gameview;

import java.awt.Point;
import java.awt.geom.Point2D;

class RNGUtils {
	static double getAngle(double x1, double y1, double x2, double y2, boolean is360) {
		return Math.atan((y2 - y1) / (x2 - x1)) + ((is360 && x2 < x1) ? Math.PI : 0);
	} 
	
	static Point2D.Double getVector(double angle, double lengthx, double lengthy){
		return new Point2D.Double(lengthx * Math.cos(angle), lengthy * Math.sin(angle));
	}
	
	static Point2D.Double getVector(double angle, double length){
		return getVector(angle, length, length);
	}
	
	static int getJumpParabola(double x, double maxJumpHeight) {
		return (int) ((1 - Math.pow((x - 1), 2)) * maxJumpHeight);
	}
	
	static double getDistance(double x1, double y1, double x2, double y2) {
		return Math.hypot(x1-x2, y1-y2);
	}
	
	static double getWeaponAngle(Player player, Point mousePoint) {
		return getAngle(player.startX + player.width, player.getY() + (player.height / 3), mousePoint.getX(), mousePoint.getY(), false);
	}
}
