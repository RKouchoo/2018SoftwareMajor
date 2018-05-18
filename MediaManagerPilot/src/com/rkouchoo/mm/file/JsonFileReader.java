package com.rkouchoo.mm.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rkouchoo.mm.Constants;
import com.rkouchoo.mm.util.FileInformationSupplier;

public class JsonFileReader {
	
	Gson gson;
	
	public JsonFileReader() {
		gson = new Gson();
	}
	
	/**
	 * returns a string with json meta
	 * @param path file to read
	 * @return string of jsom meta
	 * @throws Throwable
	 */
	public String read(File path) throws Throwable {
		 BufferedReader br = new BufferedReader(new FileReader(path.getParentFile() + Constants.HIDDEN_FILE_NAME));
		    try {
		        StringBuilder sb = new StringBuilder();
		        String line = br.readLine();

		        while (line != null) {
		            sb.append(line);
		            sb.append("\n");
		            line = br.readLine();
		        }
		        return sb.toString();
		    } finally {
		        br.close();
		    }
	}
	
	/**
	 * Converts a json string to a list of FileInformationSuppliers,
	 * @param jsonString
	 * @return list of FileInformationSuppliers
	 */
	public List<FileInformationSupplier> readFromJson(String jsonString) {
		return gson.fromJson(jsonString, new TypeToken<List<FileInformationSupplier>>(){}.getType());
	}
}
