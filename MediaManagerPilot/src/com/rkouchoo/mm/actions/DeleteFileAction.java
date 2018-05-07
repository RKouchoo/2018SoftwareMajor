package com.rkouchoo.mm.actions;

import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import com.rkocuhoo.mm.interfaces.ActionInterface;

public class DeleteFileAction implements ActionInterface{
	
	ActionManager manager;
	
	public DeleteFileAction(ActionManager man) {
		this.manager = man;
	}

	@Override
	public void run() {
		if (manager.getMediaManager().currentFile == null) {
			manager.getMessenger().showErrorMessage("No file selected for deletion.", "Select File");
			return;
		}

		int result = JOptionPane.showConfirmDialog(manager.getMediaManager().uiPanel, "Are you sure you want to delete this file?", "Delete File",
				JOptionPane.ERROR_MESSAGE);
		if (result == JOptionPane.OK_OPTION) {
			try {
				System.out.println("currentFile: " + manager.getMediaManager().currentFile);
				TreePath parentPath = manager.getMediaManager().findTreePath(manager.getMediaManager().currentFile.getParentFile());
				System.out.println("parentPath: " + parentPath);
				DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) parentPath.getLastPathComponent();
				System.out.println("parentNode: " + parentNode);

				boolean directory = manager.getMediaManager().currentFile.isDirectory();
				boolean deleted = manager.getMediaManager().currentFile.delete();
				if (deleted) {
					if (directory) {
						// delete the node.
						TreePath currentPath = manager.getMediaManager().findTreePath(manager.getMediaManager().currentFile);
						System.out.println(currentPath);
						DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) currentPath.getLastPathComponent();

						manager.getMediaManager().treeModel.removeNodeFromParent(currentNode);
					}

					manager.getMediaManager().showChildren(parentNode);
				} else {
					String msg = "The file '" + manager.getMediaManager().currentFile + "' could not be deleted.";
					manager.getMessenger().showErrorMessage(msg, "Delete Failed");
				}
			} catch (Throwable t) {
				manager.getMessenger().showThrowable(t);
			}
		}
		manager.getMediaManager().uiPanel.repaint();
	}
}
