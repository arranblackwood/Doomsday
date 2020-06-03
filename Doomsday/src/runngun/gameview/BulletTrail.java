package runngun.gameview;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

class BulletTrail {
	private double length, height;
	private int lengthFps;
	private List<Point2D.Double> points = new ArrayList<Point2D.Double>();

	private Point2D.Double getLengthAtAngle(Point2D.Double p1, Point2D.Double p2, double angleOffset, double length) {
		double angle = RNGUtils.getAngle(p1.x, p1.y, p2.x, p2.y, true);
		angle += angleOffset;
		Point2D.Double p = RNGUtils.getVector(angle, height / length);
		return new Point2D.Double(p.x + p1.x, p.y + p1.y);
	}
	
	public void draw(Graphics2D g2) { 
		if(points.size() < lengthFps+1)
			return;
		
		GeneralPath path = new GeneralPath();
		
		Point2D.Double p1 = getLengthAtAngle(points.get(0), points.get(lengthFps / 2), Math.PI / 2, 1),
			   	   	   p2 = getLengthAtAngle(points.get(lengthFps / 2), points.get(lengthFps), Math.PI / 2, 6),
			   	   	   p3 = getLengthAtAngle(points.get(lengthFps / 2), points.get(lengthFps), -Math.PI / 2, 6),
			   	   	   p4 = getLengthAtAngle(points.get(0), points.get(lengthFps / 2), -Math.PI / 2, 1);
		
		path.moveTo(p1.x, p1.y);
		path.curveTo(p1.x, p1.y, p2.x, p2.y, points.get(lengthFps).x, points.get(lengthFps).y);
		path.curveTo(points.get(lengthFps).x, points.get(lengthFps).y, p3.x, p3.y, p4.x, p4.y);
		path.lineTo(p1.x, p1.y);
		
		g2.fill(path);
	}

	public void update(int fps) {
		lengthFps = (int) (length * fps);
		lengthFps = (lengthFps < 2) ? 2 : lengthFps;
	}
	
	public void addPoint(Point2D.Double point) {
		points.add(0, point);
	}
	
	public BulletTrail(Dimension res, double height) {
		this.height = height / 2;
		length = 3e-5 * res.width;
	}
}
