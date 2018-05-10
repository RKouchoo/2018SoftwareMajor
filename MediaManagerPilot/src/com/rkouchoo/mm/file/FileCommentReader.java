package com.rkouchoo.mm.file;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.rkouchoo.mm.util.FileInformationSupplier;
import com.rkouchoo.mm.util.JsonFileReader;
import com.rkouchoo.mm.util.MessageUtil;

public class FileCommentReader {

	private List<String> fileComments;
	private List<String> fileKeys;
	private List<FileInformationSupplier> informationSuppliers;
	private String jsonString;
	
	private MessageUtil messenger;
	private JsonFileReader reader;
	
	public FileCommentReader(MessageUtil msg) {
		this.messenger = msg;
		this.reader = new JsonFileReader();
	}
	
	/**
	 * Runs through loads the file to lists
	 * @param dir  path to hidden file
	 * @return
	 */
	public FileCommentReader runAutomated(File dir) {
		cleanUp(); // run the cleanup method, dont need to persistently store info.
		
		try {
			this.jsonString = reader.read(dir);
		} catch (Throwable t) {
			messenger.showErrorMessage("failed to read json file", "json error");
			messenger.showThrowable(t);
		}
		
		this.informationSuppliers = reader.readFromJson(this.jsonString);
		this.fileKeys = splitSuppliersToKeys(this.informationSuppliers);
		this.fileComments = splitSuppliersToComments(this.informationSuppliers);
		
		return this;
	}
	
	/**
	 * Splits the supplier to a list of file keys
	 * @param suppliers
	 * @return
	 */
	private List<String> splitSuppliersToKeys(List<FileInformationSupplier> suppliers) {
		List<String> keys = new ArrayList<String>();
	
		for (FileInformationSupplier supplier : suppliers) {
			keys.add(supplier.getUniqueFileID());
		}
		return keys;
	}
	
	/**
	 * Splits the supplier into a list of comments
	 * @param suppliers
	 * @return
	 */
	private List<String> splitSuppliersToComments(List<FileInformationSupplier> suppliers) {
		List<String> comments = new ArrayList<String>();
		
		for (FileInformationSupplier supplier : suppliers) {
			comments.add(supplier.getComment());
		}	
		return comments;
	}
	
	/**
	 * List<String> comments
	 * @return list of comments for the specified directory
	 */
	public List<String> getComments() {
		return fileComments;
	}
	
	/**
	 * List<String> keys
	 * @return list of file keys for the specified directory
	 */
	public List<String> getFileKeys() {
		return fileKeys;
	}
	
	/**
	 * reset the feilds in the object.
	 */
	private void cleanUp() { 
		this.jsonString = null;
		this.informationSuppliers = null;
		this.fileKeys = null;
		this.fileComments = null;
	}
}
