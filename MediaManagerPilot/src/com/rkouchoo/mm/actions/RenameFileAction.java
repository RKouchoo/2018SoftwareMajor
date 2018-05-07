package com.rkouchoo.mm.actions;

import java.io.File;

import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import com.rkocuhoo.mm.interfaces.ActionInterface;

public class RenameFileAction implements ActionInterface {
	
	ActionManager master;
	
	public RenameFileAction(ActionManager manager) {
		this.master = manager;
	}
	
	@Override
	public void run() {
		if (master.getManager().currentFile == null) {
			master.getMessenger().showErrorMessage("No file selected to rename.", "Select File");
			return;
		}

		String renameTo = JOptionPane.showInputDialog(master.getManager().uiPanel, "New Name");
		if (renameTo != null) {
			try {
				boolean directory = master.getManager().currentFile.isDirectory();
				TreePath parentPath = master.getBackend().findTreePath(master.getManager().currentFile.getParentFile());
				DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) parentPath.getLastPathComponent();

				boolean renamed = master.getManager().currentFile.renameTo(new File(master.getManager().currentFile.getParentFile(), renameTo));
				
				if (renamed) {
					if (directory) {
						TreePath currentPath = master.getBackend().findTreePath(master.getManager().currentFile);
						System.out.println(currentPath);
						DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) currentPath.getLastPathComponent();
						master.getManager().treeModel.removeNodeFromParent(currentNode);
					}

					master.getBackend().showChildren(parentNode);
				} else {
					String msg = "The file '" + master.getManager().currentFile + "' could not be renamed.";
					master.getMessenger().showErrorMessage(msg, "Rename Failed");
				}
				
				
			} catch (Throwable t) {
				// For some reason when a folder is renamed it throws an exception but works anyway? I am not sure why this happens. please send help
				master.getManager().uiPanel.repaint();
			}
		}
		master.getManager().uiPanel.repaint();
	}

}
