package com.rkouchoo.mm.management;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import com.rkouchoo.mm.Constants;
import com.rkouchoo.mm.file.FileTreeCellRenderer;

public class ManagerRunner extends ManagerBackend {
	
	public ManagerRunner() {
		super();
	}

	/**
	 * TODO, most of this should be done in the backend anyway....
	 * @return
	 */
	public Container getUIPanel() {
		
		if (uiPanel == null) {		
			/**
			 * create the objects which contain all of the measurements.
			 */
			BorderLayout panelBorderLayout = new BorderLayout(3, 3);
			BorderLayout detailBorderLayout = new BorderLayout(3, 3);
			EmptyBorder uiEmptyBorder = new EmptyBorder(5, 5, 5, 5);
			
			/**
			 * Initialize and create all of the elements from the manager.
			 */
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
			
			/**
			 * Add all the items to the panel and then return it.
			 */		
			readable = new JCheckBox("Read  ");
			writable = new JCheckBox("Write  ");
			executable = new JCheckBox("Execute");
			refreshButton = new JButton("      Refresh      ");
			makeCommentButton = new JButton("Make a comment ");
			removeCommentButton = new JButton("Remove Comment ");
			clearAllCommentButton = new JButton("Remove all comments ");
			
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
			windowToolbar.addSeparator();
			windowToolbar.add(makeCommentButton);
			windowToolbar.add(removeCommentButton);
			windowToolbar.add(clearAllCommentButton);
			windowToolbar.addSeparator();
			windowToolbar.add(refreshButton);

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
			
			super.setLablesDisabled(fileDetailsLabels);
			super.doButtonHandling(); // make sure all the ui buttons are initialised and working.
		}
		
		return uiPanel;
	}
	
}
