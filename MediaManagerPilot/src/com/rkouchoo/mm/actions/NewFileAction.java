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

import com.rkocuhoo.mm.interfaces.ActionInterface;

public class NewFileAction implements ActionInterface {

	ActionManager manager;
	
	public NewFileAction(ActionManager man) {
		this.manager = man;
	}
	
	@Override
	public void run() {
		if (manager.getManager().currentFile == null) {
			manager.getMessenger().showErrorMessage("No location selected for new file.", "Select Location");
			return;
		}

		if (manager.getManager().newFilePanel == null) {
			
			manager.getManager().newFilePanel = new JPanel(new BorderLayout(3, 3));
			JPanel southRadio = new JPanel(new GridLayout(1, 0, 2, 2));
			manager.getManager().newTypeFile = new JRadioButton("File", true);
			JRadioButton newTypeDirectory = new JRadioButton("Directory");
			ButtonGroup bg = new ButtonGroup();
			bg.add(manager.getManager().newTypeFile);
			bg.add(newTypeDirectory);
			southRadio.add(manager.getManager().newTypeFile);
			southRadio.add(newTypeDirectory);

			manager.getManager().name = new JTextField(15);

			manager.getManager().newFilePanel.add(new JLabel("Name"), BorderLayout.WEST);
			manager.getManager().newFilePanel.add(manager.getManager().name);
			manager.getManager().newFilePanel.add(southRadio, BorderLayout.SOUTH);
		}

		int result = JOptionPane.showConfirmDialog(manager.getManager().uiPanel, manager.getManager().newFilePanel, "Create File", JOptionPane.OK_CANCEL_OPTION);
		
		if (result == JOptionPane.OK_OPTION) {
			try {
				boolean created;
				File parentFile = manager.getManager().currentFile;
				if (!parentFile.isDirectory()) {
					parentFile = parentFile.getParentFile();
				}
				File file = new File(parentFile, manager.getManager().name.getText());
				if (manager.getManager().newTypeFile.isSelected()) {
					created = file.createNewFile();
				} else {
					created = file.mkdir();
				}
				if (created) {

					TreePath parentPath = manager.getManager().findTreePath(parentFile);
					DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) parentPath.getLastPathComponent();

					if (file.isDirectory()) {
						// add the new node..
						DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(file);
						manager.getManager().treeModel.insertNodeInto(newNode, parentNode, parentNode.getChildCount());
					}

					manager.getManager().showChildren(parentNode);
				} else {
					String msg = "The file '" + file + "' could not be created.";
					manager.getMessenger().showErrorMessage(msg, "Create Failed");
				}
			} catch (Throwable t) {
				manager.getMessenger().showThrowable(t);
			}
		}
		manager.getManager().uiPanel.repaint();
	}
}
