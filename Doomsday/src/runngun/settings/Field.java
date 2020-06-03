package runngun.settings;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;


class Field {
	private String name, field;
	private boolean spacer, selected;
	
	public void draw(Graphics2D g2, Dimension res, int pos) {
		if(spacer)
			return;
		
		Font font;
		
		if(selected) {
			g2.setColor(Color.WHITE);
			font = new Font("Segoe UI Light", Font.BOLD, (int) (res.getWidth() / 40));
		}
		else {
			g2.setColor(new Color(220, 220, 220));
			font = new Font("Segoe UI Light", Font.PLAIN, (int) (res.getWidth() / 40));
		}
		
		int marginSize = (int) (res.getWidth() / 10),
			spacing = (int) ((res.getHeight() / 18) * (pos + 1)),
			fieldX = (int) (res.getWidth() - g2.getFontMetrics(font).stringWidth(field));
		
		g2.setFont(font);
		g2.drawString(name, marginSize, marginSize + spacing);
		g2.drawString(field, fieldX  - marginSize, marginSize + spacing);
	}
	
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public Field(String name, String field) {
		this.name = name;
		this.field = field;
	}
	
	//Spacer
	public Field() {
		this.spacer = true;
	}
}
