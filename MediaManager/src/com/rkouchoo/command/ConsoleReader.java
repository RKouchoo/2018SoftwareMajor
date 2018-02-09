package com.rkouchoo.command;

import java.io.BufferedReader;
import java.io.IOException;

public class ConsoleReader {

	 BufferedReader reader;
	
	/**
	 * A class that reads the console to forward backend commands around the program for the web server. 
	 * @param reader
	 */
	public ConsoleReader(BufferedReader reader) {
		this.reader = reader;
	}
	
	/**
	 * Returns the latest user input from the program
	 * @return string input
	 */
	public String get() {
		try {
			return reader.readLine();	
		} catch (IOException e) {
			System.out.println("Could not read the console!");
		}
		return null;
	}
}
