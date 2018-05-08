package com.rkouchoo.mm.management;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Date;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.TableColumn;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import com.rkouchoo.mm.Constants;
import com.rkouchoo.mm.actions.ActionManager;
import com.rkouchoo.mm.file.FileTableModel;
import com.rkouchoo.mm.util.ImageLoader;
import com.rkouchoo.mm.util.MessageUtil;

public class ManagerBackend extends MediaManager {
	
	public ManagerBackend() {
		super();
		messenger = new MessageUtil(uiPanel);
		actionManager = new ActionManager(this, messenger);		
	}
	
	/**
	 * Make sure the files are displayed from the root node
	 */
	public void showRootFile() {
		tree.setSelectionInterval(0, 0);
	}

	/**
	 * Finds the location of a file in a tree.
	 * @param find
	 * @return
	 */
	public TreePath findTreePath(File find) {
		for (int i = 0; i < tree.getRowCount(); i++) {
			TreePath treePath = tree.getPathForRow(i);
			Object object = treePath.getLastPathComponent();
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) object;
			File nodeFile = (File) node.getUserObject();

			if (nodeFile == find) {
				return treePath;
			}
		}
		return null;
	}

	/**
	 * Updates the table from the new array of Files[]
	 * @param files
	 */
	private void setTableData(final File[] files) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {			
				if (fileTableModel == null) {
					fileTableModel = new FileTableModel();
					table.setModel(fileTableModel);
				}

				table.getSelectionModel().removeListSelectionListener(listSelectionListener);
				fileTableModel.setFiles(files);
				table.getSelectionModel().addListSelectionListener(listSelectionListener);
				
				// If the cells have not been sized properly, do it now.
				if (!cellSizesSet) {
					Icon icon = fileSystemView.getSystemIcon(files[0]);

					// size adjustment to better account for icons
					table.setRowHeight(icon.getIconHeight() + Constants.ROW_ICON_PADDING);

					// -1 for most of them so they automatically allocate thier space.
					setColumnWidth(0, -1);
					setColumnWidth(3, 120);
					table.getColumnModel().getColumn(3).setMaxWidth(120);
					setColumnWidth(4, -1);
					setColumnWidth(5, -1);
					setColumnWidth(6, -1);
					setColumnWidth(7, -1);
					setColumnWidth(8, 180);
					
					cellSizesSet = true;
				}
				
			}
		});
	}

	/**
	 * Sets the width of the column.
	 * If the with is < 0 it will automatically try and set the width of the object.
	 * @param column
	 * @param width
	 */
	private void setColumnWidth(int column, int width) {
		TableColumn tableColumn = table.getColumnModel().getColumn(column);
		
		if (width < 0) {
			// use the preferred width of the header.
			JLabel label = new JLabel((String) tableColumn.getHeaderValue());
			Dimension preferred = label.getPreferredSize();
			width = (int) preferred.getWidth() + 14;
		}
		
		tableColumn.setPreferredWidth(width);
		tableColumn.setMaxWidth(width);
		tableColumn.setMinWidth(width);
	}

	/**
	 * Add the files that are contained within the directory of this node.
	 */
	public void showChildren(final DefaultMutableTreeNode node) {
		tree.setEnabled(false);
		progressBar.setVisible(false); // TODO: this causes a visual bug, is disabled at the moment as i have not fixed it.
		progressBar.setIndeterminate(true);

		// Create a new swing worker that runs the tree and the table
		SwingWorker<Void, File> worker = new SwingWorker<Void, File>() {
		
			@Override
			public Void doInBackground() {
				File file = (File) node.getUserObject();
				if (file.isDirectory()) {
					File[] files = fileSystemView.getFiles(file, true); // !!
					if (node.isLeaf()) {
						for (File child : files) {
							if (child.isDirectory()) {
								publish(child);
							}
						}
					}
					setTableData(files);
				}
				return null;
			}

			@Override
			protected void process(List<File> chunks) {
				for (File child : chunks) {
					node.add(new DefaultMutableTreeNode(child));
				}
			}

			@Override
			protected void done() {
				progressBar.setIndeterminate(false);
				progressBar.setVisible(false);
				tree.setEnabled(true);
			}
		};
		
		worker.execute();
		
		if (!worker.isDone()) {
			worker.execute();
		}
		
	}

	/**
	 * Get the file details that will be placed into cell tree
	 * @param file
	 */
	private void setFileDetails(File file) {
		this.currentFile = file;
		fileName.setText(fileSystemView.getSystemDisplayName(file));
		path.setText(file.getPath());
		date.setText(new Date(file.lastModified()).toString());
		size.setText(file.length() + " bytes");
		readable.setSelected(file.canRead());
		writable.setSelected(file.canWrite());
		executable.setSelected(file.canExecute());
		isDirectory.setSelected(file.isDirectory());
		isFile.setSelected(file.isFile());
		updateWindowTitle(file);
		uiPanel.repaint();
	}
	
	/**
	 * Updates the window title based on which folder/file is selected. 
	 * @param file
	 */
	private void updateWindowTitle(File file) {
		JFrame f = (JFrame) uiPanel.getTopLevelAncestor();
		if (f != null) {
			f.setTitle(Constants.WINDOW_TITLE + " : " + fileSystemView.getSystemDisplayName(file));
		} 		
	}
	
	/**
	 * TODO: be moved to a utils class!
	 * Sets all of the labes in a panel to disbaled.
	 * @param fileDetailsLabels
	 */
	public void setLablesDisabled(JPanel fileDetailsLabels) {
		int count = fileDetailsLabels.getComponentCount();
		for (int i = 0; i < count; i++) {
			fileDetailsLabels.getComponent(i).setEnabled(false);
		}
	}

	
	/**
	 * Sets the look and feel based on the operating system. 
	 * If it doesnt recognise your OS it will use the default java theme.
	 */
	public void setSystemLookAndFeel() {
		try {
			// Significantly improves the look of the output in terms of the file names returned by FileSystemView!
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			System.out.println("visual class error!");
		}
	}
	
	/**
	 * Sets the window icon image, in the top left of the window and the icon on the desktop window manager.
	 * @param frame
	 * @param manager
	 * @param path
	 */
	public void setWindowIconImage(JFrame frame, MediaManager manager, String path) {
		try {
			frame.setIconImages(ImageLoader.loadIconImages(manager, path));
		} catch (Exception e) {
			System.err.println("Failed to load ICON images for the application");
		}

	}
	
	/**
	 * Shows the roots of the file system to allow
	 * @param view
	 * @param root
	 */
	public void showFileSystemRoots(FileSystemView view, DefaultMutableTreeNode root) {
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

	/**
	 * Create all of the button press listeners and make sure that they work.
	 */
	public void doButtonHandling() {
		TreeSelectionListener treeSelectionListener = new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent tse) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) tse.getPath().getLastPathComponent();
				showChildren(node);
				setFileDetails((File) node.getUserObject());
			}
		};
		tree.addTreeSelectionListener(treeSelectionListener);

		listSelectionListener = new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent lse) {
				int row = table.getSelectionModel().getLeadSelectionIndex();
				setFileDetails(((FileTableModel) table.getModel()).getFile(row));
			}
		};
		table.getSelectionModel().addListSelectionListener(listSelectionListener);

		openFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				try {
					desktop.open(currentFile);
				} catch (Throwable t) {
					messenger.showThrowable(t);
				}
				uiPanel.repaint();
			}
		});

		editFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				try {
					desktop.edit(currentFile);
				} catch (Throwable t) {
					messenger.showThrowable(t);
				}
			}
		});

		printFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				try {
					desktop.print(currentFile);
				} catch (Throwable t) {
					messenger.showThrowable(t);
				}
			}
		});

		newFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				actionManager.newFile();
			}
		});

		renameFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				actionManager.renameFile();
			}
		});

		deleteFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				actionManager.deleteFile();
			}
		});
	}
	
}
