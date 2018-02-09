package com.rkouchoo.command;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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
	public String get(int timeout) {
		/*Scanner s = new Scanner(reader);
		ExecutorService executor = Executors.newCachedThreadPool();
		
		Callable<Object> task = new Callable<Object>() {
			public Object call() {
				try {
					return reader.readLine();
				} catch (IOException e) {
					// handle IO e
				} 
				return null;
			}
		};
		
		try {
			
			if (reader.ready()) {
			
				try {
					System.out.println(s.next());
					return reader.readLine();
				} catch (Exception e) {
					
				}
				
			} else {
				
				Future<Object> future = executor.submit(task);
				try {
					Object result = future.get(timeout, TimeUnit.MILLISECONDS);
				} catch (TimeoutException e) {
				} catch (InterruptedException e) {
				} catch (ExecutionException e) {
				} finally {
					future.cancel(true);
				}	
			}
		} catch (IOException e) {
			return null;
		} */
		
		String line;
		while(reader.ready() && (line = reader.readLine()) != null) {
		    System.out.println(line);
		}
	}
}
