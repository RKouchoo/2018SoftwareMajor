package com.rkouchoo.mm.util;

import java.io.File;
import java.security.MessageDigest;

import com.rkouchoo.mm.Constants;

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
				
		return byteArray2Hex(thedigest);
	}
	
	public static String byteArray2Hex(byte[] bytes) {
	    StringBuffer sb = new StringBuffer(bytes.length * 2);
	    for(final byte b : bytes) {
	        sb.append(Constants.HEX_DICT[(b & 0xF0) >> 4]);
	        sb.append(Constants.HEX_DICT[b & 0x0F]);
	    }
	    return sb.toString();
	}

	
}
