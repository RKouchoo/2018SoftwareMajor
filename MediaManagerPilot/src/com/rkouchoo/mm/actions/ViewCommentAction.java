package com.rkouchoo.mm.actions;

import java.io.File;
import java.util.List;

import javax.swing.JOptionPane;
import com.rkocuhoo.mm.interfaces.ActionInterface;
import com.rkouchoo.mm.file.FileCommentReader;
import com.rkouchoo.mm.util.FileHash;

public class ViewCommentAction implements ActionInterface {

	private ActionManager manager;
	private FileCommentReader reader;
	
	private List<String> comments;
	private List<String> keys;
	private String comment;
	
	/**
	 * Class that grabs and displays a comment to the user based on the file that they select.
	 * @param man
	 */
	public ViewCommentAction(ActionManager man) {
		this.manager = man;
		this.reader = new FileCommentReader(manager.getMessenger());
	}
	
	@Override
	public void run() {
		int position = 0;
		String key = null;
		
		File dir = manager.getMediaManager().currentFile;	
		
		try {
			comments = reader.runAutomated(dir).getComments();
			keys = reader.runAutomated(dir).getFileKeys();	
		} catch (Throwable t) {
			JOptionPane.showMessageDialog(manager.getMediaManager().uiPanel, "Please select a file to view a comment on!", "Comment view", JOptionPane.ERROR_MESSAGE);
		}
		
		try {
			key = FileHash.getFileHash(dir); // convert the current file to a key
		} catch (Throwable e) {
			e.printStackTrace(); // for whatever reason this fails, print the stack. (I have no Idea why it would fail)...
		}
		
		if (keys.contains(key) && key != null) { // check if the converted file exists in the cache
			position = keys.indexOf(key); // get the position if it exists
		}
		
		comment = comments.get(position); // set the comment
		
		if (!comment.equals("")) {
			// print debug message and info to the user
			System.out.println("Got comment '" + comment + "' from file: '" + dir.getAbsolutePath() + "'");
			JOptionPane.showMessageDialog(manager.getMediaManager().uiPanel, comment, "Comment of: " + dir.getName(), JOptionPane.DEFAULT_OPTION);			
		} else {
			JOptionPane.showMessageDialog(manager.getMediaManager().uiPanel, "No comment exists for this file!", "Comment view", JOptionPane.ERROR_MESSAGE);
		}
	}
}
