package com.rkouchoo.mm.file;

import java.io.File;
import java.util.Date;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.AbstractTableModel;

import org.apache.commons.io.FilenameUtils;

import com.rkouchoo.mm.Constants;
import com.rkouchoo.mm.util.FileHash;
import com.rkouchoo.mm.util.MessageUtil;

public class FileTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L; // why java why !?
	
	private File[] files;
	private FileSystemView fileSystemView = FileSystemView.getFileSystemView();

	private MessageUtil messenger;
	private FileCommentReader commentReader;
	
	public FileTableModel(MessageUtil msg) {
		this(new File[0]);
		this.messenger = msg;
		this.commentReader = new FileCommentReader(messenger);
	}

	FileTableModel(File[] files) {
		this.files = files;
	}

	/**
	 * Returns the data which should be in a cell which was seen 
	 */
	public Object getValueAt(int row, int column) {
		File file = files[row];
		
		switch (column) {
			case 0:
				return fileSystemView.getSystemIcon(file);
			case 1:
				return fileSystemView.getSystemDisplayName(file);
			case 2:
				return file.getPath();
			case 3:
				if(file.isDirectory()) {
					return "dir";
				} else {
					return "." + FilenameUtils.getExtension(file.getAbsolutePath());
				}
			case 4:
				return file.lastModified();
			case 5:
				return file.canRead();
			case 6:
				return file.canWrite();
			case 7:
				return file.canExecute();
			case 8:
				return readCommentCache(file);
			case 9:
				if (file.isDirectory()) {
					return "-";
				} else {
					return file.length();
				}
			default:
				System.err.println("Logic Error in column: " + column);
		}
		
		// switch should of exited this method anyway, this should not impact anything..
		return null;
	}

	public int getColumnCount() {
		return Constants.TAB_NAMES.length;
	}

	/**
	 * Returns the data type that is being used in the columns,
	 * currently hard coded for every column that needs a type other than a string.
	 */
	public Class<?> getColumnClass(int column) {
		switch (column) {
		case 0:
			return ImageIcon.class;
		case 3:
			return String.class;
		case 4:
			return Date.class;
		case 5:
		case 6:
		case 7:
		case 8:
			return Boolean.class;
		case 9:
			return Long.class;
		}
		return String.class;
	}

	private boolean readCommentCache(File f) {
		List<String> comments;
		List<String> keys;
		
		try {
			comments = commentReader.runAutomated(f).getComments();
			keys = commentReader.runAutomated(f).getFileKeys();		
		} catch (Exception e) {
			return false;
		}
		
		String key = "";
		try {
			key = FileHash.getFileHash(f);
		} catch (Throwable e) {
			messenger.showThrowable(e);
			messenger.showErrorMessage("file key generation error", "cache error");
		}
		
		for (String k : keys) {
			System.out.println(k + "			" + key);
		}
		
		if (comments == null || keys == null) {
			System.out.println("No cache here, skipping");
			return false;
		} else {
			if (keys.contains(key)) {
				return true;
			} else {
				messenger.showErrorMessage("invalid cache file!", "cache error");
				return false;
			}
		}
	}
	
	public String getColumnName(int column) {
		return Constants.TAB_NAMES[column];
	}

	public int getRowCount() {
		return files.length;
	}

	public File getFile(int row) {
		return files[row];
	}

	public void setFiles(File[] files) {
		this.files = files;
		fireTableDataChanged();
	}
	
}