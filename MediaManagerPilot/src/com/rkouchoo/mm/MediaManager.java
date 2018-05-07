package com.rkouchoo.mm;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Container;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import javax.swing.filechooser.FileSystemView;

import java.io.*;

import com.rkouchoo.mm.actions.ActionManager;
import com.rkouchoo.mm.file.FileSystemRoot;
import com.rkouchoo.mm.file.FileTableModel;
import com.rkouchoo.mm.file.FileTreeCellRenderer;
import com.rkouchoo.mm.util.MessageUtil;

public class MediaManager {
	
	protected Desktop desktop;
	protected FileSystemView fileSystemView;
	protected JTree tree;
	protected JTable table;
	protected JProgressBar progressBar;
	protected FileTableModel fileTableModel;
	protected ListSelectionListener listSelectionListener;
	protected boolean cellSizesSet = false;
	protected JButton openFile;
	protected JButton printFile;
	protected JButton editFile;
	protected JButton deleteFile;
	protected JButton newFile;
	protected JButton renameFile;

	protected JLabel fileName;
	protected JTextField path;
	protected JLabel date;
	protected JLabel size;
	protected JCheckBox readable;
	protected JCheckBox writable;
	protected JCheckBox executable;
	protected JRadioButton isDirectory;
	protected JRadioButton isFile;
	protected JPanel detailView;
	protected JScrollPane tableScroll;
	protected Dimension windowDimension;
	protected DefaultMutableTreeNode rootTreeModel;
	protected JToolBar windowToolbar;
	
	public JPanel newFilePanel;
	public JRadioButton newTypeFile;
	public JTextField name;
	public File currentFile;
	public JPanel uiPanel;
	public DefaultTreeModel treeModel;
	
	public ActionManager actionManager;
	public ManagerBackend backend;

	public MessageUtil messenger;
	
	public MediaManager() {
		messenger = new MessageUtil(uiPanel);
		actionManager = new ActionManager(this, messenger);		
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

			FileSystemRoot.showFileSystemRoots(fileSystemView, rootTreeModel);

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
			
			createBackEndWhenReady();
			
			backend.setLablesDisabled(fileDetailsLabels);
			backend.doButtonHandling(); // make sure all the ui buttons are initialised and working.
		}
		
		return uiPanel;
	}
	
	private void createBackEndWhenReady() {
		backend = new ManagerBackend(
				desktop, 
				fileSystemView, 
				tree, 
				table, 
				progressBar, 
				fileTableModel, 
				listSelectionListener, 
				cellSizesSet, 
				openFile, 
				printFile,
				editFile, 
				deleteFile, 
				newFile, 
				renameFile, 
				fileName, 
				path, 
				date,
				size, 
				readable,
				writable,
				executable,
				isDirectory,
				isFile, 
				detailView,
				tableScroll, 
				windowDimension,
				rootTreeModel, 
				windowToolbar, 
				newFilePanel, 
				newTypeFile,
				name, 
				currentFile,
				uiPanel, 
				treeModel,
				actionManager, 
				backend,
				messenger
				);
	}
	
	
	/**
	 * A getter that allows you to get the manager.
	 * @return
	 */
	public MediaManager returnSelf() {
		return this;
	}
	
	public MessageUtil getMessenger() {
		return messenger;
	}
	
	public ActionManager getActionManager() {
		return actionManager;
	}
}