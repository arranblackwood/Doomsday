package runngun.settings;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class SettingsMenu extends JPanel{
	private Settings settings;
	private Dimension res;
	private int marginSize;
	
	private Field [] fields = new Field[] {
		new Field(),
		new Field("Resolution", ""),
		new Field("Fullscreen", "Enabled"),
		new Field(),
		new Field("Anti-Aliasing", "Disabled"),
		new Field(),
		new Field("FPS Counter", "Enabled"),
		new Field("FPS Limit", "Unlimited")
	};
	
	
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D) g;
		
		//Anti-Aliasing
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		g2.setColor(Color.WHITE);
		
		g2.setFont(new Font("Segoe UI Light", Font.BOLD, (int) (res.getWidth() / 20)));
		g2.drawString("Settings", marginSize, marginSize);
		
		fields[2].setSelected(true);
		
		for (int i = 0; i < fields.length; i++)
			fields[i].draw(g2, settings.resolution, i);
	}
	
	public SettingsMenu(Settings settings) {
		setBackground(new Color(0, 0, 0, 100));
		
		this.settings = settings;
		this.res = settings.resolution;
		
		marginSize = settings.resolution.width / 10;
	}
}
