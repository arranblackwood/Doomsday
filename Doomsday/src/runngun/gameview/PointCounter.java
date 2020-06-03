package runngun.gameview;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

class PointCounter {
	private static final int
	SCORE_INCREMENT_RATE = 100;
	
	private Dimension res;
	private int score;
	
	public void draw(Graphics2D g2) {
		g2.setColor(new Color(0, 180, 0));

		String string = "$" + score;
		
		g2.setFont(new Font("Segoe UI Light", Font.PLAIN, (int) (res.getWidth() / 25)));
		g2.drawString(string, (int) (res.getWidth() / 100),	(int) (g2.getFontMetrics().getHeight() * 0.8));
	}
	
	public void reset() {
		score = 0;
	}
	 
	public void addDistancePoints() {
		score++;
	}
	
	public void	addEnemyKillPoints() {
		score += 100;
	}
	
	public Timer autoIncrementer = new Timer(SCORE_INCREMENT_RATE, new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			addDistancePoints();
		}
	});
	
	PointCounter(Dimension res) {
		this.res = res;
		autoIncrementer.start();
	}
}
