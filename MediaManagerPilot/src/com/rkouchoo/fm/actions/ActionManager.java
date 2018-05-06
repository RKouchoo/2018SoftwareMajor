package com.rkouchoo.fm.actions;

import com.rkocuhoo.fm.interfaces.ActionManagerInterface;
import com.rkouchoo.fm.MediaManager;

public class ActionManager implements ActionManagerInterface {

	private MediaManager manager;
	private boolean hasConstructed;
	
	private RenameFileAction rename;
	private DeleteFileAction delete;
	private NewFileAction newFile;
	
	public ActionManager(MediaManager man) {
		this.manager = man;

		rename = new RenameFileAction(this);
		delete = new DeleteFileAction(this);
		newFile = new NewFileAction(this);
		
		hasConstructed = true;
	}
	
	@Override
	public void renameFile() {
		rename.run();
	}

	@Override
	public void deleteFile() {
		delete.run();
	}

	@Override
	public void newFile() {
		newFile.run();
	}

	@Override
	public MediaManager getManager() {
		if (hasConstructed) {
			return manager;
		} else {
			return null;
			// Should return mock manager! which needs to be created.
		}
	}

}
