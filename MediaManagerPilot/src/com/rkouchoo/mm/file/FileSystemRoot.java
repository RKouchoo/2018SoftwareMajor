package com.rkouchoo.mm.file;

import java.io.File;

import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultMutableTreeNode;

public class FileSystemRoot {
	/**
	 * Shows the roots of the file system to allow
	 * @param view
	 * @param root
	 */
	public static void showFileSystemRoots(FileSystemView view, DefaultMutableTreeNode root) {
		// show the file system roots.
		File[] roots = view.getRoots();
		for (File fileSystemRoot : roots) {
			DefaultMutableTreeNode node = new DefaultMutableTreeNode(fileSystemRoot);
			root.add(node);
			File[] files = view.getFiles(fileSystemRoot, true);
			for (File file : files) {
				if (file.isDirectory()) {
					node.add(new DefaultMutableTreeNode(file));
				}
			}
		}
	}
}
