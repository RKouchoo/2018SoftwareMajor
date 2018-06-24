package com.rkouchoo.mm.actions;

import java.io.File;

import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import com.rkouchoo.mm.interfaces.ActionInterface;

public class RenameFileAction implements ActionInterface {
	
	ActionManager master;
	
	public RenameFileAction(ActionManager manager) {
		this.master = manager;
	}
	
	@Override
	public void run() {
		if (master.getMediaManager().currentFile == null) {
			master.getMessenger().showErrorMessage("No file selected to rename.", "Select File");
			return;
		}

		String renameTo = JOptionPane.showInputDialog(master.getMediaManager().uiPanel, "New Name");
		if (renameTo != null) {
			try {
				boolean directory = master.getMediaManager().currentFile.isDirectory();
				TreePath parentPath = master.getMediaManager().findTreePath(master.getMediaManager().currentFile.getParentFile());
				DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) parentPath.getLastPathComponent();

				boolean renamed = master.getMediaManager().currentFile.renameTo(new File(master.getMediaManager().currentFile.getParentFile(), renameTo));
				
				if (renamed) {
					if (directory) {
						TreePath currentPath = master.getMediaManager().findTreePath(master.getMediaManager().currentFile);
						System.out.println(currentPath);
						DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) currentPath.getLastPathComponent();
						master.getMediaManager().treeModel.removeNodeFromParent(currentNode);
					}

					master.getMediaManager().showChildren(parentNode);
				} else {
					String msg = "The file '" + master.getMediaManager().currentFile + "' could not be renamed.";
					master.getMessenger().showErrorMessage(msg, "Rename Failed");
				}
				
				
			} catch (Throwable t) {
				// For some reason when a folder is renamed it throws an exception but works anyway? I am not sure why this happens. please send help
				master.getMediaManager().uiPanel.repaint();
			}
		}
		master.getMediaManager().uiPanel.repaint();
	}

}
