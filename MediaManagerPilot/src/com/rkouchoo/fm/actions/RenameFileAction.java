package com.rkouchoo.fm.actions;

import java.io.File;

import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import com.rkocuhoo.fm.interfaces.ActionInterface;

public class RenameFileAction implements ActionInterface {
	
	ActionManager master;
	
	public RenameFileAction(ActionManager manager) {
		this.master = manager;
	}
	
	@Override
	public void run() {
		if (master.getManager().currentFile == null) {
			master.getManager().showErrorMessage("No file selected to rename.", "Select File");
			return;
		}

		String renameTo = JOptionPane.showInputDialog(master.getManager().gui, "New Name");
		if (renameTo != null) {
			try {
				boolean directory = master.getManager().currentFile.isDirectory();
				TreePath parentPath = master.getManager().findTreePath(master.getManager().currentFile.getParentFile());
				DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) parentPath.getLastPathComponent();

				boolean renamed = master.getManager().currentFile.renameTo(new File(master.getManager().currentFile.getParentFile(), renameTo));
				
				if (renamed) {
					if (directory) {
						TreePath currentPath = master.getManager().findTreePath(master.getManager().currentFile);
						System.out.println(currentPath);
						DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) currentPath.getLastPathComponent();
						master.getManager().treeModel.removeNodeFromParent(currentNode);
					}

					master.getManager().showChildren(parentNode);
				} else {
					String msg = "The file '" + master.getManager().currentFile + "' could not be renamed.";
					master.getManager().showErrorMessage(msg, "Rename Failed");
				}
				
				
			} catch (Throwable t) {
				// For some reason when a folder is renamed it throws an exception but works anyway? I am not sure why this happens. please send help
				master.getManager().gui.repaint();
			}
		}
		master.getManager().gui.repaint();
	}

}
