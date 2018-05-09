package com.rkouchoo.mm.file;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.rkouchoo.mm.Constants;
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
	
	private String writeableString;
	
	public SelectedFileComment(MessageUtil msg) {
		this.messenger = msg;
		this.fileIndexer = new FileKeyIndexer(messenger);
	}
	
	public void doComment(File dir, File[] files, String fileKey, String comment) throws Throwable {
		quickIndex(dir, files, fileKey, comment);
	}
	
	private void quickIndex(File dir, File[] files, String fileKey, String comment) throws Throwable {
		boolean continueIndex;
		
		if (dir.equals(this.workingDir)) {
			// no need to index anything, program has run in this directory before.
			// update the already made lists with a new comment.
			updateLists(this.workingDirKeys, this.commentsList, fileKey, comment, dir);
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
			this.writeableString = fileIndexer.generateJSONString(workingDirKeys, commentsList); // convert those lists to FileInformationSuppliers, then into a json string.
			
			fileIndexer.writeOutJson(dir.getAbsolutePath(), this.writeableString, Constants.MAKE_HIDDEN_FILES); // write out the json strings in the current directory
		}
		
	}
	
	private List<String> generateCommentList(List<String> fileKeys, String fileKey, String comment) {
		List<String> comments = new ArrayList<String>();
		
		if(fileKeys.contains(fileKey)) {
			int pos = fileKeys.indexOf(fileKey); // get the position of the key related to the comment so they are the same.
			// populate the list with nulls or the comment in the correct position.
			for (int i = 0; i < fileKeys.size(); i++) {
				if (i == pos) {
					comments.add(comment); // put the comment in the corresponding place.
				} else {
					comments.add(null); // debatign weather this should be null or it should be "" 
				}
			}
			
		}
		return comments;
	}

	/**
	 * Should only be run if something is being updated.
	 */
	private void updateLists(List<String> keys, List<String> comments, String key, String comment, File dir) {
		// check if using this method is valid.
		if (keys.contains(key) && !comments.contains(comment)) {
			int pos = keys.indexOf(key);
			comments.add(pos, comment); // get the pos and then update the pos.
			
			// generate the new json string and then write it out into the directory.
			this.writeableString = fileIndexer.generateJSONString(keys, comments);
			fileIndexer.writeOutJson(dir.getAbsolutePath(), this.writeableString, Constants.MAKE_HIDDEN_FILES);
			
		}
		
	}
	
	/**
	 * cleans up the lists storage.
	 */
	private void cleanUp() {
		
	}
}

	