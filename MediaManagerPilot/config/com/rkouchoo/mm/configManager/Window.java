package com.rkouchoo.mm.configManager;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.Toolkit;
import java.awt.Color;

public class Window extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
	 * Create the frame.
	 */
	public Window() {
		setType(Type.UTILITY);
		setBackground(Color.CYAN);
		setForeground(Color.CYAN);
		setIconImage(Toolkit.getDefaultToolkit().getImage(Window.class.getResource("/res/exp_icon.png")));
		setResizable(false);
		setTitle("MM CONFIG");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 279, 269);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JCheckBox chckbxIsCahce = new JCheckBox("hidden cache");
		chckbxIsCahce.setBounds(11, 49, 139, 29);
		contentPane.add(chckbxIsCahce);

		JCheckBox checkBoxSafeMode = new JCheckBox("safe mode");
		checkBoxSafeMode.setBounds(11, 12, 139, 29);
		contentPane.add(checkBoxSafeMode);

		JButton btnExit = new JButton("Exit");
		btnExit.setBounds(141, 180, 115, 29);
		contentPane.add(btnExit);

		JButton btnOpenManager = new JButton("Open mm");

		btnOpenManager.setBounds(11, 180, 115, 29);
		contentPane.add(btnOpenManager);

		JCheckBox chckbxShowLog = new JCheckBox("show log");
		chckbxShowLog.setBounds(11, 87, 139, 29);
		contentPane.add(chckbxShowLog);

		JCheckBox chckbxSafeExplorer = new JCheckBox("safe explorer");
		chckbxSafeExplorer.setBounds(11, 124, 139, 29);
		contentPane.add(chckbxSafeExplorer);

		contentPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
				"Cancel"); //$NON-NLS-1$
		contentPane.getActionMap().put("Cancel", new AbstractAction() { //$NON-NLS-1$

			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				System.out.println("Exiting window.");
				System.exit(1); // dont really need to exit gracefully.
				// should add a check to see if we are waiting on json's to write out.
			}
		});

		btnOpenManager.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// do main method here
				System.exit(2);
			}
		});

		btnExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(1);
			}
		});

		
		
		
	}
}
