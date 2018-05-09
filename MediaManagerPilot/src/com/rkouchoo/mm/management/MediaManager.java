package com.rkouchoo.mm.management;

import java.awt.Desktop;
import java.awt.Dimension;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import javax.swing.filechooser.FileSystemView;

import java.io.*;

import com.rkouchoo.mm.actions.ActionManager;
import com.rkouchoo.mm.file.FileTableModel;
import com.rkouchoo.mm.util.MessageUtil;

/**
 * Class to hold all of the variables, overkill but was my only solution to make the program look clean
 * May or may not combine main and this class but could cause a stack overflow again.
 * @author KOUC01
 *
 */
public class MediaManager {
	
	/*
	 * 'universal' componenets.
	 */
	protected Desktop desktop;
	protected FileSystemView fileSystemView;
	protected FileTableModel fileTableModel;
	protected ListSelectionListener listSelectionListener;
	protected Dimension windowDimension;
	protected DefaultMutableTreeNode rootTreeModel;
	public File currentFile;
	public DefaultTreeModel treeModel;
	
	/*
	 * window components
	 */
	protected JTree tree;
	protected JTable table;
	protected JProgressBar progressBar;
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
	protected JToolBar windowToolbar;
	public JPanel newFilePanel;
	public JRadioButton newTypeFile;
	public JTextField name;
	public JPanel uiPanel;
	
	/*
	 * my classes 
	 */
	protected ActionManager actionManager;
	protected MessageUtil messenger;
	
	protected boolean haveCellSizesBeenSet = false;
	
	protected MediaManager() {
		/**
		 * Nothing to construct here..
		 */
	}
}