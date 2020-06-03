package runngun.main;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class IanDRM {
	public static void verifyNotIan(JPanel parent) {
		if(System.getProperty("user.name").contentEquals("5w63362")) {//563362 //563501
			JOptionPane.showMessageDialog(parent, "IanDRM Triggered. Closing Application");
			System.exit(0);
		}
	}
}
