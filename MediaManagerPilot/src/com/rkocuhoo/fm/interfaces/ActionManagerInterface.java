package com.rkocuhoo.fm.interfaces;

import com.rkouchoo.fm.MediaManager;

/**
 * Interface that defines the actions that will need to be run though the buttons on the GUI.
 *
 */
public interface ActionManagerInterface {

	public MediaManager getManager();
	
	public void renameFile();
	
	public void deleteFile();
	
	public void newFile();
}
