package runngun.statistics;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

public class FpsGraph {
	public static final double
	WIDTH = 400,
	HEIGHT = 300;
	
	//nanoseconds
	private static final long GRAPH_LENGTH = (long) 2e+10;
	
	private Point2D.Double pos;
	private GeneralPath path;
	private List<Integer> fpsList = new ArrayList<Integer>();
	private List<Long> timesList = new ArrayList<Long>();
	private int maxFps, minFps;
	
	
	private double getX(long time) {
		return (((double)(time - (System.nanoTime() - GRAPH_LENGTH)) / GRAPH_LENGTH) * WIDTH) + pos.x;
	}
	
	private double getY(int fps) {
		return (((double)(fps - minFps) / (maxFps - minFps)) * HEIGHT) + pos.y;
	}
	
	//The path is calculated during the update cycle for optimisation, since the update cycle is on a
	//separate thread from the game loop
	private void updatePath() {
		if(fpsList.size() < 2)
			return;
		
		path = new GeneralPath();
		path.moveTo(getX(timesList.get(0)), getY(fpsList.get(0)));
		
		for(int i = 1; i < fpsList.size(); i++)
			path.lineTo(getX(timesList.get(i)), getY(fpsList.get(i)));
	}
	
	public void addFps(int fps, long time) {
		this.fpsList.add(fps);
		this.timesList.add(time);
		
		maxFps = 0;
		minFps = Integer.MAX_VALUE;
		
		for (int i = 0; i < timesList.size(); i++) {
			if(timesList.get(i) < System.nanoTime() - GRAPH_LENGTH) {
				timesList.remove(i);	
				fpsList.remove(i);
			}
			
			if(fpsList.get(i) > maxFps)
				maxFps = fpsList.get(i);
			else if(fpsList.get(i) < minFps)
				minFps = fpsList.get(i);
		}
		
		updatePath();
	}
	    
	public void draw(Graphics2D g2) {
		g2.setColor(new Color(0, 0, 0, 120));
		g2.fill(new Rectangle2D.Double(pos.x, pos.y, WIDTH, HEIGHT));
		g2.setColor(Color.WHITE);
		g2.draw(path);
	}
	
	public FpsGraph(Point2D.Double pos) {
		this.pos = pos;
	}
}