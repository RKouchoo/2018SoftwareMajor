package com.rkouchoo.mm.file;

import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rkouchoo.mm.Constants;
import com.rkouchoo.mm.util.MessageUtil;

public class FileKeyIndexer {
	
	private static Gson gson;
	private static List<String> fileKeys;
	private static MessageUtil messenger;
	
	public FileKeyIndexer(Gson gsonObject, MessageUtil messenger) {
		FileKeyIndexer.gson = gsonObject; // TODO: do I need to pass this in or should it be created here.
		fileKeys = new ArrayList<String>();
		FileKeyIndexer.messenger = messenger;
	}
	
	public static void indexFile(File[] files) throws Throwable {
		
		for (int i = 0; files.length > i; i ++) {
			Path f = Paths.get(files[i].getAbsolutePath());
			BasicFileAttributes attrs = Files.readAttributes(f, BasicFileAttributes.class);
			String fileUniqueKey = attrs.fileKey().toString();
			
			fileKeys.add(fileUniqueKey); // add the file key to the list.
		}
		
		System.gc(); // hint at the compiler that i made a mess of objects and they kinda need to go
	}
	
	/**
	 * Puts a list of file keys and comments together into multiple FileCommentSuppliers which goes into 
	 */
	public static String generateProperList(List<String> fileIDList, List<String> comments) {
		List<FileCommentSupplier> jsonList = new ArrayList<FileCommentSupplier>();
		
		for (String id : fileIDList) {
			jsonList.add(new FileCommentSupplier(id, comments.iterator().next()));
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
	public static void WriteOutJson(String path, String jsonString) {
		try (Writer writer = new FileWriter(path + Constants.HIDDEN_FILE_NAME)) {
		    Gson gson = new GsonBuilder().create();
		    gson.toJson(jsonString, writer);
		} catch (Throwable t) {
			messenger.showThrowable(t);
			messenger.showErrorMessage("Failed to write out comment!", "Comment failure");
		}
	}
	
}
