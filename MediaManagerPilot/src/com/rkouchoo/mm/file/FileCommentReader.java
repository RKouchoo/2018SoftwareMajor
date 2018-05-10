package com.rkouchoo.mm.file;

import java.io.File;
import java.util.List;

import com.rkouchoo.mm.util.FileInformationSupplier;

public class FileCommentReader {

	private List<String> fileComments;
	private List<String> fileKeys;
	private List<FileInformationSupplier> informationSuppliers;
	private String jsonString;
	
	private JsonFileReader reader;
	
	public FileCommentReader() {
		reader = new JsonFileReader();
	}
	
	public FileCommentReader runAutomated(File dir) {
		cleanUp(); // run the cleanup method.
		this.jsonString = reader.read(dir);
		this.informationSuppliers = reader.readFromJson(this.jsonString);
		this.fileKeys = splitSuppliersToKeys(informationSuppliers);
		this.fileComments = splitSuppliersToComments(informationSuppliers);
		
		return this;
	}
	

	private List<String> splitSuppliersToKeys(List<FileInformationSupplier> suppliers) {
		
		return null;
	}
		
	private List<String> splitSuppliersToComments(List<FileInformationSupplier> suppliers) {
		
		return null;
	}
	
	public List<String> getComments() {
		return fileComments;
	}
	
	public List<String> getFileKeys() {
		return fileKeys;
	}
	
	private void cleanUp() { 
		this.jsonString = null;
		this.informationSuppliers = null;
		this.fileKeys = null;
		this.fileComments = null;
	}
}
