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
		System.out.println("hi");
		if (manager.getManager().currentFile == null) {
			manager.getMessenger().showErrorMessage("No file selected for deletion.", "Select File");
			return;
		}

		int result = JOptionPane.showConfirmDialog(manager.getManager().uiPanel, "Are you sure you want to delete this file?", "Delete File",
				JOptionPane.ERROR_MESSAGE);
		if (result == JOptionPane.OK_OPTION) {
			try {
				System.out.println("currentFile: " + manager.getManager().currentFile);
				TreePath parentPath = manager.getManager().backend.findTreePath(manager.getManager().currentFile.getParentFile());
				System.out.println("parentPath: " + parentPath);
				DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) parentPath.getLastPathComponent();
				System.out.println("parentNode: " + parentNode);

				boolean directory = manager.getManager().currentFile.isDirectory();
				boolean deleted = manager.getManager().currentFile.delete();
				if (deleted) {
					if (directory) {
						// delete the node.
						TreePath currentPath = manager.getBackend().findTreePath(manager.getManager().currentFile);
						System.out.println(currentPath);
						DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) currentPath.getLastPathComponent();

						manager.getManager().treeModel.removeNodeFromParent(currentNode);
					}

					manager.getBackend().showChildren(parentNode);
				} else {
					String msg = "The file '" + manager.getManager().currentFile + "' could not be deleted.";
					manager.getMessenger().showErrorMessage(msg, "Delete Failed");
				}
			} catch (Throwable t) {
				manager.getMessenger().showThrowable(t);
			}
		}
		manager.getManager().uiPanel.repaint();
	}
}
