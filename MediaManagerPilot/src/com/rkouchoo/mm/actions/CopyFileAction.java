package com.rkouchoo.mm.actions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import com.rkocuhoo.mm.interfaces.ActionInterface;

@Deprecated
public class CopyFileAction implements ActionInterface {

	ActionManager manager;
	boolean status;

	File toFile;
	File fromFile;

	public CopyFileAction(ActionManager man, File from, File to) {
		this.fromFile = from;
		this.toFile = to;
		this.manager = man;
	}

	@SuppressWarnings("resource")
	@Override @Deprecated
	public void run() {
		boolean created = false;
		try {
			created = toFile.createNewFile();
		} catch (IOException e3) {
			e3.printStackTrace();
		}
		try {
		if (created) {
			FileChannel fromChannel = null;
			FileChannel toChannel = null;
			try {
				try {
					fromChannel = new FileInputStream(fromFile).getChannel();
				} catch (FileNotFoundException e2) {
					e2.printStackTrace();
				}
				try {
					toChannel = new FileOutputStream(toFile).getChannel();
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
				try {
					toChannel.transferFrom(fromChannel, 0, fromChannel.size());
				} catch (IOException e) {
					e.printStackTrace();
				}

				// set the flags of the to the same as the from
				toFile.setReadable(fromFile.canRead());
				toFile.setWritable(fromFile.canWrite());
				toFile.setExecutable(fromFile.canExecute());
			} finally {
				if (fromChannel != null) {
					try {
						fromChannel.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (toChannel != null) {
					try {
						toChannel.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}	
		}
		} finally {
			this.status = created;	
		}		
	}

	public boolean getBoolean() {
		return status;
	}

}
