package com.rkouchoo.mm.file;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;

public class FileKeyIndexer {

	public FileKeyIndexer() {
	
	}
	
	public static void indexFile(File file) throws Throwable {
		Path f = Paths.get(file.getAbsolutePath());

		BasicFileAttributes attrs = Files.readAttributes(f, BasicFileAttributes.class);
		String thisFileCreationDate = attrs.fileKey().toString();
		
		// I dont need attrs anymore! trying to optimise to make the program faster when making comments
		System.gc();
	}
}
