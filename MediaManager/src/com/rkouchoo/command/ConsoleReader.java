package com.rkouchoo.command;

import java.io.BufferedReader;
import java.io.IOException;

import com.rkouchoo.Constants;
import com.rkouchoo.interfaces.ConsoleReaderInterface;

public class ConsoleReader implements ConsoleReaderInterface {

	 private BufferedReader reader;
	 private StringBuilder builder;
	 
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
		builder = new StringBuilder();
	}
	
	/**
	 * Returns the latest user input from the program and stores it to a string supplier.
	 * @return string input
	 */
	@Override
	public WantedStringSupplier getSupplier() {	// Misleading title, does multiple functions
		String line = null;
		
		try {
			while(reader.ready() && (line = reader.readLine()) != null) {
				if (line.split(Constants.COMMAND_SPLIT_TERM).length > 1) {									
								
					// Make sure we capture all of the arguments before we throw them around.
					int i = 0;
					for(String s : line.split(Constants.COMMAND_SPLIT_TERM)) {
						if (i >= 1) { // filter out the command.
							builder.append(s + " ");
						}
						i ++;
					}
					// Store all the data in the supplier.
					currentInputStringSupplier
					.setCommand(line.split(Constants.COMMAND_SPLIT_TERM)[0]) // we only want the command, so the first array position
					.setArgs(builder.toString())
					.setRaw(line)
					.setCommandOnly(false);		
					
				} else {
					currentInputStringSupplier
						.setCommand(line)
						.setRaw(line)
						.setCommandOnly(true);
				}
				
				builder.delete(0, builder.length()); // Reset the string builder so cached strings are not spat out in another iteratlion.
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
