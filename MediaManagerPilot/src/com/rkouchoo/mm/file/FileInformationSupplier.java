package com.rkouchoo.mm.file;

/**
 * A class that holds information about files, will be used with gson.
 * @author KOUC01
 *
 */
public class FileInformationSupplier {
	
	private String uniqueFileID;
	private String comment;
	
	public FileInformationSupplier(String id, String comment) {
		this.uniqueFileID = id;
		this.comment = comment;
	}
	
	public String getUniqueFileID() {
		return uniqueFileID;
	}
	
	public String getComment() {
		return comment;
	}
	
	// there is no reason why I set the comments, but it works anyway
	// part of eclipses auto generate getter and setter tool.
	
	public void setUniqueFileID(String uniqueFileID) {
		this.uniqueFileID = uniqueFileID;
	}
	
	public void setComment(String comment) {
		this.comment = comment;
	}
}
