package com.rkouchoo.mm;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.rkouchoo.mm.management.ManagerBackend;
import com.rkouchoo.mm.management.ManagerRunner;

public class Main {

	/**
	 * Main method that runs mediaManager.getUIPanel() which kickstarts everything.
	 * @param args
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {			
				
				ManagerBackend mediaManager = new ManagerRunner();
				
				JFrame frame = new JFrame(Constants.WINDOW_TITLE);
				mediaManager.setSystemLookAndFeel();
				mediaManager.setWindowIconImage(frame, mediaManager, Constants.WINDOW_ICON_PATH);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);			
				frame.setContentPane(((ManagerRunner) mediaManager).getUIPanel());
				
				mediaManager.showRootFile();
				
				frame.pack();
				frame.setLocationByPlatform(Constants.WINDOW_NATIVE_LOCATION);
				frame.setMinimumSize(frame.getSize());
				frame.setVisible(true);
			}
		});
	}
}
