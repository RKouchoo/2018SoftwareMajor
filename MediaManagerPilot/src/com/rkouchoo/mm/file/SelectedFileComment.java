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

	/**
	 * Main commenting method, handles all usecases
	 * @param dir = current directory
	 * @param files = array of files in the directory
	 * @param fileKey = the selected files key
	 * @param comment = the comment the user has given
	 * @throws Throwable :P shot myself in the ass putting this here.
	 */
	public void doComment(File dir, File[] files, String fileKey, String comment) throws Throwable {
		quickIndex(dir, files, fileKey, comment);
	}
	
	/**
	 * private helper method.
	 * @param dir = current directory
	 * @param files = array of files in the directory
	 * @param fileKey = the selected files key
	 * @param comment = the comment the user has given
	 * @throws Throwable
	 */
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
	
	/**
	 * Generates a list of comments, 
	 * with one string embedded in the right place.
	 * @param fileKeys
	 * @param fileKey
	 * @param comment
	 * @return
	 */
	private List<String> generateCommentList(List<String> fileKeys, String fileKey, String comment) {
		List<String> comments = new ArrayList<String>();
		if(fileKeys.contains(fileKey)) {
			int pos = fileKeys.indexOf(fileKey); // get the position of the key related to the comment so they are the same.
			// populate the list with nulls or the comment in the correct position.
			for (int i = 0; i < fileKeys.size(); i++) {
				if (i == pos) {
					comments.add(comment); // put the comment in the corresponding place.
				} else {
					comments.add(null); // debating weather this should be null or it should be "" 
				}
			}
		}
		return comments;
	}

	/**
	 * Updates pre existing lists then writes it out to a file.
	 * @param keys = ArrayList of keys
	 * @param comments = ArrayList of comments
	 * @param key = the current unique file that is being commented on.
	 * @param comment = the comment to be added
	 * @param dir = the current working directory
	 */
	private void updateLists(List<String> keys, List<String> comments, String key, String comment, File dir) {
		// check if using this method is valid.
		if (keys.contains(key) && !comments.contains(comment)) {
			int pos = keys.indexOf(key);
			comments.add(pos, comment); // get the pos and then update the pos.
			
			// generate the new json string and then write it out into the directory.
			this.writeableString = fileIndexer.generateJSONString(keys, comments);
			fileIndexer.writeOutJson(dir.getAbsolutePath(), this.writeableString, Constants.MAKE_HIDDEN_FILES);	
		} else {
			messenger.showErrorMessage("Cannot save comment, already contains comment!", "Commenting error");
		}
	}
	
	/**
	 * cleans up the lists storage.
	 */
	private void cleanUp() {
		this.workingDir = null; // I can do all of the for loops but requires extra effort and less efficient. 
		this.commentsList = null;
		this.workingDirKeys = null;
		this.writeableString = null;
	}
}

	