package com.rkouchoo.mm;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Main {

	/**
	 * Main method that runs mediaManager.getUIPanel() which kickstarts everything.
	 * @param args
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {			
				MediaManager mediaManager = new MediaManager();
				
				JFrame frame = new JFrame(Constants.WINDOW_TITLE);
							
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);			
				frame.setContentPane(mediaManager.getUIPanel());
				mediaManager.backend.setSystemLookAndFeel();
				mediaManager.backend.setWindowIconImage(frame, mediaManager, Constants.WINDOW_ICON_PROJECT_PATH);
		
				frame.pack();
				frame.setLocationByPlatform(Constants.WINDOW_NATIVE_LOCATION);
				frame.setMinimumSize(frame.getSize());
				frame.setVisible(true);
				
				mediaManager.backend.showRootFile();
			}
		});
	}
}
