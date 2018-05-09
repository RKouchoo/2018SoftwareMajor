package com.rkouchoo.mm.file;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.rkouchoo.mm.util.MessageUtil;

/**
 * A class that uses the methods of FileKeyIndexer to generate proper lists for user write out.
 * @author KOUC01
 *
 */
public class SelectedFileComment {
	
	private FileKeyIndexer fileIndexer;
	private MessageUtil messenger;
	
	private File workingDir;
	private List<String> workingDirKeys;
	private List<String> commentsList;
	
	public SelectedFileComment(MessageUtil msg) {
		this.messenger = msg;
		this.fileIndexer = new FileKeyIndexer(messenger);
	}
	
	public void doComment(File dir, File[] files, String fileKey, String comment) throws Throwable {

	}
	
	private void quickIndex(File dir, File[] files, String fileKey, String comment) throws Throwable {
		boolean continueIndex;
		
		if (dir.equals(this.workingDir)) {
			// no need to index anything, program has run in this directory before.
			// run the update method here!
			return;
		}
		
		// assign it if it doesnt actually exist
		if (this.workingDir == null || this.workingDir != dir) {
			cleanUp(); // remove all traces of other directories.
			this.workingDir = dir;
			continueIndex = true;
		} else {
			continueIndex = false;
			// or run the update method here.
			return;
		}
		
		if (continueIndex) {
			this.workingDirKeys = fileIndexer.indexFile(files);			
			this.commentsList = generateCommentList(this.workingDirKeys, fileKey, comment);
		}
		
	}
	
	private List<String> generateCommentList(List<String> fileKeys, String fileKey, String comment) {
		List<String> comments = new ArrayList<String>();
		
		if(fileKeys.contains(fileKey)) {
			int pos = fileKeys.indexOf(fileKey); // get the position of the key related to the comment so they are the same.
			// populate the list with nulls or the comment in the correct position.
			for (int i = 0; i < fileKeys.size(); i++) {
				if (i == pos) {
					comments.add(comment);
				} else {
					comments.add(null); // debatign weather this should be null or it should be "" 
				}
			}
			
		}
		return null;
	}

	/**
	 * cleans up the lists storage.
	 */
	private void cleanUp() {
		
	}
}

	