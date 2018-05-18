package com.rkouchoo.mm.file;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.rkouchoo.mm.Constants;
import com.rkouchoo.mm.util.FileHash;
import com.rkouchoo.mm.util.FileInformationSupplier;
import com.rkouchoo.mm.util.MessageUtil;

public class FileKeyIndexer {
	
	private Gson gson;
	private List<String> fileKeys;
	private MessageUtil messenger;
	
	/**
	 * WARN: this class must be created as a static.
	 * Methods from this class should not be run the first time that the app has been opened, 
	 * or every directory will be indexed
	 * They should only be called when actual data about the file has been created.
	 * @param messenger
	 */
	public FileKeyIndexer(MessageUtil messenger) {
		this.gson = new GsonBuilder().setPrettyPrinting().create();
		this.fileKeys = new ArrayList<String>();
		this.messenger = messenger;
	}
	
	/**
	 * indexes the array of files that is generated when showFileRootTree is run.
	 * @param files
	 * @throws Throwable
	 * @returns unqiue file key list
	 */
	public List<String> indexFile(File[] files) throws Throwable {
		for (int i = 0; files.length > i; i ++) {
			String fileUniqueKey = FileHash.getFileHash(files[i]);
			fileKeys.add(fileUniqueKey); // add the file key to the list.
		}
		System.gc(); // hint at the compiler that i made a mess of objects and they kinda need to go
		
		return fileKeys;
	}
	
	/**
	 * Puts a list of file keys and comments together into multiple FileCommentSuppliers which goes into 
	 */
	public String generateJSONString(List<String> fileIDList, List<String> comments) {
		List<FileInformationSupplier> jsonList = new ArrayList<FileInformationSupplier>();

		for (int i = 1; i < fileIDList.size(); i++) {
			String id = fileIDList.get(i);
			String comment = comments.get(i);
			jsonList.add(new FileInformationSupplier(id, comment));
		}
		
		// convert the list to a big json string.
		String jsonString = gson.toJson(jsonList);
		
		return jsonString;
	}
	
	/**
	 * Writes out a hidden file in the current directory containing the file keys and comments.
	 * @param path
	 * @param jsonString
	 */
	public void writeOutJson(String path, String jsonString, boolean hidden) {
		System.out.println("Writing file to cache: " + path + Constants.HIDDEN_FILE_NAME);
		try (Writer writer = new FileWriter(path + Constants.HIDDEN_FILE_NAME)) {
		    Gson gson = new GsonBuilder().setPrettyPrinting().create(); // create the gson object with pretty prining
		    JsonElement jsonElement = new JsonParser().parse(jsonString);
		    gson.toJson(jsonElement, writer);
		    
		    if (hidden) { // if the file is to be hidden make it.
		    	makeHidden(path + Constants.HIDDEN_FILE_NAME);
		    }
		} catch (Throwable t) {
			messenger.showThrowable(t);
			messenger.showErrorMessage("Failed to write out comment!", "Comment failure");
		}
	}
	
	/**
	 * Makes a file hidden with a native windows call.
	 * need to be able to hand calls from other operating systems e.g. It should work on my linux environment
	 * @param path
	 */
	private void makeHidden(String path) {
		try {
			Runtime.getRuntime().exec("attrib +H " + path);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
