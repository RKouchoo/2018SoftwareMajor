package com.rkouchoo.fm;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Container;
import java.awt.Image;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.tree.*;

import org.apache.commons.io.FilenameUtils;

import javax.swing.table.*;
import javax.swing.filechooser.FileSystemView;
import javax.imageio.ImageIO;

import com.rkouchoo.fm.actions.ActionManager;
import com.rkouchoo.fm.file.FileTableModel;
import com.rkouchoo.fm.file.FileTreeCellRenderer;
import com.rkouchoo.util.ImageLoader;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import java.io.*;
import java.nio.channels.FileChannel;

import java.net.URL;

public class MediaManager {
	
	private Desktop desktop;
	private FileSystemView fileSystemView;
	public File currentFile;
	public JPanel gui;
	private JTree tree;
	public DefaultTreeModel treeModel;
	private JTable table;
	private JProgressBar progressBar;
	private FileTableModel fileTableModel;
	private ListSelectionListener listSelectionListener;
	private boolean cellSizesSet = false;
	private int rowIconPadding = 6;

	private JButton openFile;
	private JButton printFile;
	private JButton editFile;
	private JButton deleteFile;
	private JButton newFile;
	private JButton copyFile;

	private JLabel fileName;
	private JTextField path;
	private JLabel date;
	private JLabel size;
	private JCheckBox readable;
	private JCheckBox writable;
	private JCheckBox executable;
	private JRadioButton isDirectory;
	private JRadioButton isFile;

	public JPanel newFilePanel;
	public JRadioButton newTypeFile;
	public JTextField name;
	
	private ActionManager actionManager;
	
	public MediaManager() {
		actionManager = new ActionManager(this);
	}

	public Container getGui() {
		
		if (gui == null) {
			gui = new JPanel(new BorderLayout(3, 3));
			gui.setBorder(new EmptyBorder(5, 5, 5, 5));

			fileSystemView = FileSystemView.getFileSystemView();
			desktop = Desktop.getDesktop();

			JPanel detailView = new JPanel(new BorderLayout(3, 3));

			table = new JTable();
			table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			table.setAutoCreateRowSorter(true);
			table.setShowVerticalLines(false);

			listSelectionListener = new ListSelectionListener() {
				@Override
				public void valueChanged(ListSelectionEvent lse) {
					int row = table.getSelectionModel().getLeadSelectionIndex();
					setFileDetails(((FileTableModel) table.getModel()).getFile(row));
				}
			};
			table.getSelectionModel().addListSelectionListener(listSelectionListener);
			
			JScrollPane tableScroll = new JScrollPane(table);
			
			Dimension windowDimension = new Dimension(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
			tableScroll.setPreferredSize(windowDimension);
			detailView.add(tableScroll, BorderLayout.CENTER);

			// the File tree
			DefaultMutableTreeNode root = new DefaultMutableTreeNode();
			treeModel = new DefaultTreeModel(root);

			TreeSelectionListener treeSelectionListener = new TreeSelectionListener() {
				public void valueChanged(TreeSelectionEvent tse) {
					DefaultMutableTreeNode node = (DefaultMutableTreeNode) tse.getPath().getLastPathComponent();
					showChildren(node);
					setFileDetails((File) node.getUserObject());
				}
			};

			// show the file system roots.
			File[] roots = fileSystemView.getRoots();
			for (File fileSystemRoot : roots) {
				DefaultMutableTreeNode node = new DefaultMutableTreeNode(fileSystemRoot);
				root.add(node);
				File[] files = fileSystemView.getFiles(fileSystemRoot, true);
				for (File file : files) {
					if (file.isDirectory()) {
						node.add(new DefaultMutableTreeNode(file));
					}
				}
			}

			tree = new JTree(treeModel);
			tree.setRootVisible(false);
			tree.addTreeSelectionListener(treeSelectionListener);
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

			int count = fileDetailsLabels.getComponentCount();
			
			for (int ii = 0; ii < count; ii++) {
				fileDetailsLabels.getComponent(ii).setEnabled(false);
			}

			JToolBar toolBar = new JToolBar();
			toolBar.setFloatable(false);

			openFile = new JButton("Open");
			editFile = new JButton("Edit");
			printFile = new JButton("Print");
			
			openFile.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					try {
						desktop.open(currentFile);
					} catch (Throwable t) {
						showThrowable(t);
					}
					gui.repaint();
				}
			});

			editFile.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					try {
						desktop.edit(currentFile);
					} catch (Throwable t) {
						showThrowable(t);
					}
				}
			});

			printFile.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					try {
						desktop.print(currentFile);
					} catch (Throwable t) {
						showThrowable(t);
					}
				}
			});

			// Check the actions are supported on the current platform. Should be as it is run in windows for testing.
			openFile.setEnabled(desktop.isSupported(Desktop.Action.OPEN));
			editFile.setEnabled(desktop.isSupported(Desktop.Action.EDIT));
			printFile.setEnabled(desktop.isSupported(Desktop.Action.PRINT));

			newFile = new JButton("New");
			JButton renameFile = new JButton("Rename");
			deleteFile = new JButton("Delete");
			
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
			
			readable = new JCheckBox("Read  ");
			writable = new JCheckBox("Write  ");
			executable = new JCheckBox("Execute");
			
			// add the elements to the toolbar
			toolBar.add(openFile);
			toolBar.add(editFile);
			toolBar.add(printFile);
			toolBar.addSeparator();
			toolBar.add(newFile);
			toolBar.add(renameFile);
			toolBar.add(deleteFile);
			toolBar.addSeparator();
			toolBar.add(readable);
			toolBar.add(writable);
			toolBar.add(executable);

			JPanel fileView = new JPanel(new BorderLayout(3, 3));

			fileView.add(toolBar, BorderLayout.NORTH);
			fileView.add(fileMainDetails, BorderLayout.CENTER);

			detailView.add(fileView, BorderLayout.SOUTH);

			JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, treeScroll, detailView);
			gui.add(splitPane, BorderLayout.CENTER);

			JPanel simpleOutput = new JPanel(new BorderLayout(3, 3));
			progressBar = new JProgressBar();
			simpleOutput.add(progressBar, BorderLayout.EAST);
			progressBar.setVisible(false);

			gui.add(simpleOutput, BorderLayout.SOUTH);
			
			// Set up mnemonic binds, seems useless, could be removed at any point
			executable.setMnemonic('x');
			readable.setMnemonic('a');
			deleteFile.setMnemonic('d');
			newFile.setMnemonic('n');
			printFile.setMnemonic('p');
			editFile.setMnemonic('e');
			openFile.setMnemonic('o');
			writable.setMnemonic('w');
			renameFile.setMnemonic('r');
		}
		return gui;
	}

	public void showRootFile() {
		// ensure the main files are displayed
		tree.setSelectionInterval(0, 0);
	}

	public TreePath findTreePath(File find) {
		for (int ii = 0; ii < tree.getRowCount(); ii++) {
			TreePath treePath = tree.getPathForRow(ii);
			Object object = treePath.getLastPathComponent();
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) object;
			File nodeFile = (File) node.getUserObject();

			if (nodeFile == find) {
				return treePath;
			}
		}
		return null;
	}

	public void showErrorMessage(String errorMessage, String errorTitle) {
		JOptionPane.showMessageDialog(gui, errorMessage, errorTitle, JOptionPane.ERROR_MESSAGE);
	}

	public void showThrowable(Throwable t) {
		t.printStackTrace();
		JOptionPane.showMessageDialog(gui, t.toString(), t.getMessage(), JOptionPane.ERROR_MESSAGE);
		gui.repaint();
	}

	/** Update the table on the EDT */
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
				if (!cellSizesSet) {
					Icon icon = fileSystemView.getSystemIcon(files[0]);

					// size adjustment to better account for icons
					table.setRowHeight(icon.getIconHeight() + rowIconPadding);

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
			// use the preferred width of the header..
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
		progressBar.setVisible(true);
		progressBar.setIndeterminate(true);

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
	}

	/**
	 * Get the file details that will be placed into cell tree
	 * @param file
	 */
	private void setFileDetails(File file) {
		currentFile = file;
		Icon icon = fileSystemView.getSystemIcon(file);
		fileName.setText(fileSystemView.getSystemDisplayName(file));
		path.setText(file.getPath());
		date.setText(new Date(file.lastModified()).toString());
		size.setText(file.length() + " bytes");
		readable.setSelected(file.canRead());
		writable.setSelected(file.canWrite());
		executable.setSelected(file.canExecute());
		isDirectory.setSelected(file.isDirectory());

		isFile.setSelected(file.isFile());

		JFrame f = (JFrame) gui.getTopLevelAncestor();
		
		if (f != null) {
			f.setTitle(Constants.WINDOW_TITLE + " : " + fileSystemView.getSystemDisplayName(file));
		} 
		gui.repaint();
	}
	

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					// Significantly improves the look of the output in terms of the file names returned by FileSystemView!
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				} catch (Exception e) {
					// erm.. couldn't find your operating system?! kk will just use the default java theme.
				}
				
				MediaManager fileManager = new MediaManager();
				JFrame frame = new JFrame(Constants.WINDOW_TITLE);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);			
				frame.setContentPane(fileManager.getGui());

				try {
					frame.setIconImages(ImageLoader.loadIconImages(fileManager, Constants.WINDOW_ICON_PROJECT_PATH));
				} catch (Exception e) {
					System.err.println("Failed to load ICON images for the application");
				}

				frame.pack();
				frame.setLocationByPlatform(Constants.WINDOW_NATIVE_LOCATION);
				frame.setMinimumSize(frame.getSize());
				frame.setVisible(true);
				fileManager.showRootFile();
			}
		});
	}
}