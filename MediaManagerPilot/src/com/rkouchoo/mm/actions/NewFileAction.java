package com.rkouchoo.mm.actions;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.File;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import com.rkouchoo.mm.interfaces.ActionInterface;

/**
 * Messy logic that needs to be cleaned up at some time.
 * @author KOUC01
 *
 */
public class NewFileAction implements ActionInterface {

	ActionManager manager;
	
	public NewFileAction(ActionManager man) {
		this.manager = man;
	}
	
	@Override
	public void run() {
		if (manager.getMediaManager().currentFile == null) {
			manager.getMessenger().showErrorMessage("No location selected for new file.", "Select Location");
			return;
		}

		if (manager.getMediaManager().newFilePanel == null) {
			
			/**
			 * Create the popup window.
			 */
			manager.getMediaManager().newFilePanel = new JPanel(new BorderLayout(3, 3));
			JPanel southRadio = new JPanel(new GridLayout(1, 0, 2, 2));
			manager.getMediaManager().newTypeFile = new JRadioButton("File", true);
			JRadioButton newTypeDirectory = new JRadioButton("Directory");
			ButtonGroup bg = new ButtonGroup();
			bg.add(manager.getMediaManager().newTypeFile);
			bg.add(newTypeDirectory);
			southRadio.add(manager.getMediaManager().newTypeFile);
			southRadio.add(newTypeDirectory);

			manager.getMediaManager().name = new JTextField(15);

			manager.getMediaManager().newFilePanel.add(new JLabel("Name"), BorderLayout.WEST);
			manager.getMediaManager().newFilePanel.add(manager.getMediaManager().name);
			manager.getMediaManager().newFilePanel.add(southRadio, BorderLayout.SOUTH);
		}

		int result = JOptionPane.showConfirmDialog(manager.getMediaManager().uiPanel, manager.getMediaManager().newFilePanel, "Create File", JOptionPane.OK_CANCEL_OPTION);
		
		if (result == JOptionPane.OK_OPTION) {
			try {
				boolean created;
				File parentFile = manager.getMediaManager().currentFile;
				if (!parentFile.isDirectory()) {
					parentFile = parentFile.getParentFile();
				}
				File file = new File(parentFile, manager.getMediaManager().name.getText());
				if (manager.getMediaManager().newTypeFile.isSelected()) {
					created = file.createNewFile();
				} else {
					created = file.mkdir();
				}
				if (created) {

					TreePath parentPath = manager.getMediaManager().findTreePath(parentFile);
					DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) parentPath.getLastPathComponent();

					if (file.isDirectory()) {
						// add the new node..
						DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(file);
						manager.getMediaManager().treeModel.insertNodeInto(newNode, parentNode, parentNode.getChildCount());
					}

					manager.getMediaManager().showChildren(parentNode);
				} else {
					String msg = "The file '" + file + "' could not be created.";
					manager.getMessenger().showErrorMessage(msg, "Create Failed");
				}
			} catch (Throwable t) {
				manager.getMessenger().showThrowable(t);
			}
		}
		manager.getMediaManager().uiPanel.repaint();
	}
}
