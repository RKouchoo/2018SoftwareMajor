package com.rkouchoo.mm.actions;

import com.rkocuhoo.mm.interfaces.ActionManagerInterface;
import com.rkouchoo.mm.MediaManager;
import com.rkouchoo.mm.util.MessageUtil;

public class ActionManager implements ActionManagerInterface {

	private MediaManager manager;
	private boolean hasConstructed;
	
	private RenameFileAction rename;
	private DeleteFileAction delete;
	private NewFileAction newFile;
	
	private MessageUtil messenger;
	
	public ActionManager(MediaManager man, MessageUtil messenger) {
		this.manager = man;
		this.messenger = messenger;
		
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

	@Override
	public MessageUtil getMessenger() {
		return messenger;
	}

}
