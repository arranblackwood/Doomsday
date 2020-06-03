package runngun.main;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;

import runngun.gameview.GameView;
import runngun.settings.Settings;
import runngun.settings.SettingsMenu;


public class RunNGunApp{
	private JFrame frame;
	private GameView gameView;
	private Settings settings;
	private SettingsMenu settingsMenu;

	public static void main(String[] args){
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new RunNGunApp();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	private void startGame() throws Exception {
		settings = new Settings();
		frame = new JFrame();
		
		Dimension res = settings.resolution;
		
		//TODO: Add this to settings class
		if(res.getWidth() / settings.aspectRatio.getWidth() != res.getHeight() / settings.aspectRatio.getHeight())
			throw new Exception("Window Size and Aspect Ratio do not match");
		
		if(settings.isFullscreen) {
			if(!Toolkit.getDefaultToolkit().getScreenSize().equals(res))
				throw new Exception("Fullscreen is enabled but Window Size and Screen Size do not match");
			frame.setUndecorated(true);
			frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		}
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.getContentPane().setPreferredSize(res);
		frame.pack();
		
		settingsMenu = new SettingsMenu(settings);
		settingsMenu.setBounds(0, 0, (int)res.getWidth(), (int)res.getHeight());
		
		JPanel glassPanel = (JPanel) frame.getGlassPane();
		glassPanel.setVisible(false);
		glassPanel.setLayout(null);
		glassPanel.add(settingsMenu);
		
		gameView = new GameView(settings, glassPanel);
		gameView.setBounds(0, 0, (int)res.getWidth(), (int)res.getHeight());
		frame.getContentPane().add(gameView);
		
		frame.setLocationRelativeTo(null);
		
		frame.setVisible(true);
	}
	
	public RunNGunApp() throws Exception {
		/*SecurityPrompt securityPrompt = new SecurityPrompt(s -> {
			try {
				startGame();
			} catch (Exception e) {
				e.printStackTrace();
			}
			gameView.setSignature(s);
		});
		securityPrompt.setVisible(true);*/
		startGame();
	}
}
