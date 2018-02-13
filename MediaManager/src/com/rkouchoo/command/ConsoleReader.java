package com.rkouchoo.command;

import java.io.BufferedReader;
import java.io.IOException;

import com.rkouchoo.Constants;
import com.rkouchoo.interfaces.ConsoleReaderInterface;

public class ConsoleReader implements ConsoleReaderInterface {

	 private BufferedReader reader;
	 
	 private WantedStringSupplier lastInputStringSupplier;
	 private WantedStringSupplier currentInputStringSupplier;
	
	/**
	 * A class that reads the console to forward back end commands around the program for the web server. 
	 * @param reader
	 */
	public ConsoleReader(BufferedReader reader) {
		this.reader = reader;
		lastInputStringSupplier = new WantedStringSupplier();
		currentInputStringSupplier = new WantedStringSupplier();
	}
	
	/**
	 * Returns the latest user input from the program
	 * @return string input
	 */
	@Override
	public WantedStringSupplier getSupplier() {	
		String line = null;
		
		try {
			while(reader.ready() && (line = reader.readLine()) != null) {
				if (line.split(Constants.COMMAND_SPLIT_TERM).length > 1) {
					currentInputStringSupplier
						.setCommand(line.split(Constants.COMMAND_SPLIT_TERM)[0])
						.setArgs(line.split(Constants.COMMAND_SPLIT_TERM)[1])
						.setRaw(line)
						.setCommandOnly(false);		
				} else {
					currentInputStringSupplier
						.setCommand(line)
						.setRaw(line)
						.setCommandOnly(true);
				}
				
			}
		} catch (IOException e) {
			System.out.println("[FATAL] Unable to read the console: " + e);
		}
		
		this.lastInputStringSupplier = currentInputStringSupplier;
		
		return currentInputStringSupplier;
	}
	
	public WantedStringSupplier getLastSupplier() {
		return this.lastInputStringSupplier;
	}
	
}
