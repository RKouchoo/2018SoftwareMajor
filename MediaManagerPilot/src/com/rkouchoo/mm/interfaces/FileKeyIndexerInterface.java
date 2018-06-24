package com.rkouchoo.mm.interfaces;

import java.io.File;

public interface FileKeyIndexerInterface {

	public void indexFile(File file);
	
	public String getFileKey(File file);
	
	public File getFileFromKey(String key);

}
