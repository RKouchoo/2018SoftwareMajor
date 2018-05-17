package com.rkouchoo.mm.actions;

import com.rkocuhoo.mm.interfaces.ActionManagerInterface;
import com.rkouchoo.mm.management.ManagerBackend;
import com.rkouchoo.mm.util.MessageUtil;

public class ActionManager implements ActionManagerInterface {

	private ManagerBackend manager;	
	private RenameFileAction rename;
	private DeleteFileAction delete;
	private NewFileAction newFile;
	private CommentAction comment;
	
	private MessageUtil messenger;
	
	public ActionManager(ManagerBackend man, MessageUtil messenger) {
		this.manager = man;
		this.messenger = messenger;
		
		rename = new RenameFileAction(this);
		delete = new DeleteFileAction(this);
		newFile = new NewFileAction(this);
		comment = new CommentAction(this);
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
	public void comment() {
		comment.run();
	}
	
	@Override
	public ManagerBackend getMediaManager() {
		return manager;
	}

	@Override
	public MessageUtil getMessenger() {
		return messenger;
	}

}
