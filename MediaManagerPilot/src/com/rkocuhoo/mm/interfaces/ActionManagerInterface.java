package com.rkocuhoo.mm.interfaces;

import com.rkouchoo.mm.management.MediaManager;
import com.rkouchoo.mm.util.MessageUtil;

/**
 * Interface that defines the actions that will need to be run though the buttons on the GUI.
 *
 */
public interface ActionManagerInterface {

	public MediaManager getMediaManager();
	
	public MessageUtil getMessenger();
		
	public void renameFile();
	
	public void deleteFile();
	
	public void newFile();
	
	public void comment();
	
	public void viewComment();
}
