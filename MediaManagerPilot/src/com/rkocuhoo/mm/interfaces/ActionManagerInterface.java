package com.rkocuhoo.mm.interfaces;

import com.rkouchoo.mm.ManagerBackend;
import com.rkouchoo.mm.MediaManager;
import com.rkouchoo.mm.util.MessageUtil;

/**
 * Interface that defines the actions that will need to be run though the buttons on the GUI.
 *
 */
public interface ActionManagerInterface {

	public MediaManager getManager();
	
	public MessageUtil getMessenger();
	
	public ManagerBackend getBackend();
	
	public void renameFile();
	
	public void deleteFile();
	
	public void newFile();
}
