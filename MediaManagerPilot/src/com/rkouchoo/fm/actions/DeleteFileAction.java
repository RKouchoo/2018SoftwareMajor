package com.rkouchoo.fm.actions;

import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import com.rkocuhoo.fm.interfaces.ActionInterface;

public class DeleteFileAction implements ActionInterface{
	
	ActionManager manager;
	
	public DeleteFileAction(ActionManager man) {
		this.manager = man;
	}

	@Override
	public void run() {
		if (manager.getManager().currentFile == null) {
			manager.getManager().showErrorMessage("No file selected for deletion.", "Select File");
			return;
		}

		int result = JOptionPane.showConfirmDialog(manager.getManager().gui, "Are you sure you want to delete this file?", "Delete File",
				JOptionPane.ERROR_MESSAGE);
		if (result == JOptionPane.OK_OPTION) {
			try {
				System.out.println("currentFile: " + manager.getManager().currentFile);
				TreePath parentPath = manager.getManager().findTreePath(manager.getManager().currentFile.getParentFile());
				System.out.println("parentPath: " + parentPath);
				DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) parentPath.getLastPathComponent();
				System.out.println("parentNode: " + parentNode);

				boolean directory = manager.getManager().currentFile.isDirectory();
				boolean deleted = manager.getManager().currentFile.delete();
				if (deleted) {
					if (directory) {
						// delete the node..
						TreePath currentPath = manager.getManager().findTreePath(manager.getManager().currentFile);
						System.out.println(currentPath);
						DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) currentPath.getLastPathComponent();

						manager.getManager().treeModel.removeNodeFromParent(currentNode);
					}

					manager.getManager().showChildren(parentNode);
				} else {
					String msg = "The file '" + manager.getManager().currentFile + "' could not be deleted.";
					manager.getManager().showErrorMessage(msg, "Delete Failed");
				}
			} catch (Throwable t) {
				manager.getManager().showThrowable(t);
			}
		}
		manager.getManager().gui.repaint();
	}
}
