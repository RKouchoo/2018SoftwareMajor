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
	
	@SuppressWarnings("unused")
	private void createBackEndWhenReady() {
		backend = new ManagerBackend();
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