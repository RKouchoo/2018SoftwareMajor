package com.rkouchoo.mm.util;

import java.io.File;
import java.security.MessageDigest;

public  class FileHash {

	/**
	 * Returns a unique generated hash key.
	 * @param f file
	 * @return a unique byte hash.
	 * @throws Throwable
	 */
	public static String getFileHash(File f) throws Throwable {

		byte[] bytesOfMessage = f.toString().getBytes("UTF-8");

		MessageDigest md = MessageDigest.getInstance("MD5");
		byte[] thedigest = md.digest(bytesOfMessage);
				
		return new String(thedigest, "UTF-8");
	}
	
}
