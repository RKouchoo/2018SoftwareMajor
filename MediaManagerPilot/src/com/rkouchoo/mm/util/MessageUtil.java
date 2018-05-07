package com.rkouchoo.mm.util;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class MessageUtil {
	
	JPanel uiPanel;
	
	public MessageUtil(JPanel uiPanel) {
		this.uiPanel = uiPanel;
	}

	/**
	 * Sends out an error message
	 * @param errorMessage
	 * @param errorTitle
	 */
	public void showErrorMessage(String errorMessage, String errorTitle) {
		if (checkPanel(uiPanel)) {
			JOptionPane.showMessageDialog(uiPanel, errorMessage, errorTitle, JOptionPane.ERROR_MESSAGE);	
		}
	}

	/**
	 * Shows a throwable error log.
	 * @param throwableError
	 */
	public void showThrowable(Throwable throwableError) {
		if (checkPanel(uiPanel)) {
			JOptionPane.showMessageDialog(uiPanel, throwableError.toString(), throwableError.getMessage(), JOptionPane.ERROR_MESSAGE);
			uiPanel.repaint();		
		}
		// still want to print the error anyway
		throwableError.printStackTrace();
	}
	
	private boolean checkPanel(JPanel panel) {
		if (panel == null) {
			return false;
		}
		return true;
	}
}
