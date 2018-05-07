package com.rkouchoo.mm;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Container;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import javax.swing.table.*;
import javax.swing.filechooser.FileSystemView;

import java.util.Date;
import java.util.List;
import java.io.*;

import com.rkouchoo.mm.actions.ActionManager;
import com.rkouchoo.mm.file.FileTableModel;
import com.rkouchoo.mm.file.FileTreeCellRenderer;
import com.rkouchoo.mm.util.ImageLoader;
import com.rkouchoo.mm.util.MessageUtil;

public class MediaManager {
	
	private Desktop desktop;
	private FileSystemView fileSystemView;
	private JTree tree;
	private JTable table;
	private JProgressBar progressBar;
	private FileTableModel fileTableModel;
	private ListSelectionListener listSelectionListener;
	private boolean cellSizesSet = false;
	private JButton openFile;
	private JButton printFile;
	private JButton editFile;
	private JButton deleteFile;
	private JButton newFile;
	private JButton renameFile;
	private JLabel fileName;
	private JTextField path;
	private JLabel date;
	private JLabel size;
	private JCheckBox readable;
	private JCheckBox writable;
	private JCheckBox executable;
	private JRadioButton isDirectory;
	private JRadioButton isFile;
	private JPanel detailView;
	private JScrollPane tableScroll;
	private Dimension windowDimension;
	private DefaultMutableTreeNode rootTreeModel;
	private JToolBar windowToolbar;
	
	public JPanel newFilePanel;
	public JRadioButton newTypeFile;
	public JTextField name;
	public File currentFile;
	public JPanel uiPanel;
	public DefaultTreeModel treeModel;
	
	private ActionManager actionManager;

	private MessageUtil messenger;
	
	public MediaManager() {
		messenger = new MessageUtil(uiPanel);
		actionManager = new ActionManager(this, messenger);
	}

	public Container getUIPanel() {
		
		if (uiPanel == null) {
			
			BorderLayout panelBorderLayout = new BorderLayout(3, 3);
			BorderLayout detailBorderLayout = new BorderLayout(3, 3);
			EmptyBorder uiEmptyBorder = new EmptyBorder(5, 5, 5, 5);
			
			uiPanel = new JPanel(panelBorderLayout);
			uiPanel.setBorder(uiEmptyBorder);
			fileSystemView = FileSystemView.getFileSystemView();
			desktop = Desktop.getDesktop();
			detailView = new JPanel(detailBorderLayout);
			table = new JTable();
			table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			table.setAutoCreateRowSorter(true);
			table.setShowVerticalLines(false);
			tableScroll = new JScrollPane(table);
		    windowDimension = new Dimension(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
			tableScroll.setPreferredSize(windowDimension);
			detailView.add(tableScroll, BorderLayout.CENTER);

			// the File tree
			rootTreeModel = new DefaultMutableTreeNode();
			treeModel = new DefaultTreeModel(rootTreeModel);

			showFileSystemRoots(fileSystemView, rootTreeModel);

			tree = new JTree(treeModel);
			tree.setRootVisible(false);
			tree.setCellRenderer(new FileTreeCellRenderer());
			tree.expandRow(0);
			tree.setVisibleRowCount(Constants.VISIBLE_ROW_COUNT);

			JScrollPane treeScroll = new JScrollPane(tree);

			Dimension preferredSize = treeScroll.getPreferredSize();
			Dimension widePreferred = new Dimension(200, (int) preferredSize.getHeight());
			
			treeScroll.setPreferredSize(widePreferred);

			// details for a File
			JPanel fileMainDetails = new JPanel(new BorderLayout(4, 2));
			fileMainDetails.setBorder(new EmptyBorder(0, 6, 0, 6));

			JPanel fileDetailsLabels = new JPanel(new GridLayout(0, 1, 2, 2));
			fileMainDetails.add(fileDetailsLabels, BorderLayout.WEST);

			JPanel fileDetailsValues = new JPanel(new GridLayout(0, 1, 2, 2));
			fileMainDetails.add(fileDetailsValues, BorderLayout.CENTER);

			fileDetailsLabels.add(new JLabel("File", JLabel.TRAILING));

			fileName = new JLabel();
			fileDetailsValues.add(fileName);
			fileDetailsLabels.add(new JLabel("Path/name", JLabel.TRAILING));

			path = new JTextField(5);
			path.setEditable(false);
			fileDetailsValues.add(path);
			fileDetailsLabels.add(new JLabel("Last Modified", JLabel.TRAILING));

			date = new JLabel();
			fileDetailsValues.add(date);
			fileDetailsLabels.add(new JLabel("File size", JLabel.TRAILING));

			size = new JLabel();
			fileDetailsValues.add(size);
			fileDetailsLabels.add(new JLabel("Type", JLabel.TRAILING));

			JPanel flags = new JPanel(new FlowLayout(FlowLayout.LEADING, 4, 0));
			isDirectory = new JRadioButton("Directory");
			isDirectory.setEnabled(false);
			flags.add(isDirectory);

			isFile = new JRadioButton("File");
			isFile.setEnabled(false);
			flags.add(isFile);
			fileDetailsValues.add(flags);

			setLablesDisabled(fileDetailsLabels);

			windowToolbar = new JToolBar();
			windowToolbar.setFloatable(false);

			openFile = new JButton("Open");
			editFile = new JButton("Edit");
			printFile = new JButton("Print");

			// Check the actions are supported on the current platform. Should be as it is
			// run in windows for testing.
			openFile.setEnabled(desktop.isSupported(Desktop.Action.OPEN));
			editFile.setEnabled(desktop.isSupported(Desktop.Action.EDIT));
			printFile.setEnabled(desktop.isSupported(Desktop.Action.PRINT));

			newFile = new JButton("New");
			renameFile = new JButton("Rename");
			deleteFile = new JButton("Delete");
			
			doButtonHandling(); // make sure all the ui buttons are initialised and working.
			
			/**
			 * Add all the items to the panel and then return it.
			 */		
			readable = new JCheckBox("Read  ");
			writable = new JCheckBox("Write  ");
			executable = new JCheckBox("Execute");
			
			// add the elements to the toolbar
			windowToolbar.add(openFile);
			windowToolbar.add(editFile);
			windowToolbar.add(printFile);
			windowToolbar.addSeparator();
			windowToolbar.add(newFile);
			windowToolbar.add(renameFile);
			windowToolbar.add(deleteFile);
			windowToolbar.addSeparator();
			windowToolbar.add(readable);
			windowToolbar.add(writable);
			windowToolbar.add(executable);

			JPanel fileView = new JPanel(new BorderLayout(3, 3));

			fileView.add(windowToolbar, BorderLayout.NORTH);
			fileView.add(fileMainDetails, BorderLayout.CENTER);

			detailView.add(fileView, BorderLayout.SOUTH);
			JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, treeScroll, detailView);
			uiPanel.add(splitPane, BorderLayout.CENTER);
			JPanel simpleOutput = new JPanel(new BorderLayout(3, 3));
			progressBar = new JProgressBar();
			simpleOutput.add(progressBar, BorderLayout.CENTER);
			progressBar.setVisible(false);

			uiPanel.add(simpleOutput, BorderLayout.SOUTH);	
		}
		
		return uiPanel;
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
		progressBar.setVisible(false); // TODO: this causes a visual bug, should be disabled if I cannot fix it, possibly just stretch the frame.
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
	 * Sets the look and feel based on the operating system. 
	 * If it doesnt recognise your OS it will use the default java theme.
	 */
	public void setSystemLookAndFeel() {
		try {
			// Significantly improves the look of the output in terms of the file names returned by FileSystemView!
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			// erm.. couldn't find your operating system?! kk will just use the default java theme.
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

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {			
				MediaManager mediaManager = new MediaManager();
				mediaManager.setSystemLookAndFeel();
				
				JFrame frame = new JFrame(Constants.WINDOW_TITLE);
				
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);			
				frame.setContentPane(mediaManager.getUIPanel());
				
				mediaManager.setWindowIconImage(frame, mediaManager, Constants.WINDOW_ICON_PROJECT_PATH);
		
				frame.pack();
				frame.setLocationByPlatform(Constants.WINDOW_NATIVE_LOCATION);
				frame.setMinimumSize(frame.getSize());
				frame.setVisible(true);
				
				mediaManager.showRootFile();
			}
		});
	}
}