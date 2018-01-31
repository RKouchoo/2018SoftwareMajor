package com.rkouchoo;

import java.util.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class Main {

	private static BufferedReader reader;
	private static String lastCommand;
	private static String command = "";
	
	private static Thread readerThread;
	private static Thread commandHandlerThread;
	
	public static final String[] COMMAND_ARRAY = new String[] {"echo", "ping", "hi"};
	public static final List<String> COMMAND_LIST = Arrays.asList(COMMAND_ARRAY);
	
	public static void main(String[] args) throws IOException {
		
		// Enter data using a buffered reader for the console
		reader = new BufferedReader(new InputStreamReader(System.in));
		
		// Create a new thread to handle input reading
		readerThread = new Thread(new Runnable() {
	         @Override
	         public void run()  {
	        	 while (command == lastCommand) {
	        		 try {
	        			 command = read();
	        			 lastCommand = command;
	        		 } catch (IOException ex) {
	        			 System.out.println(ex);
	        		 }	
	     		}	 
	         }
	});		
		
		commandHandlerThread = new Thread(new Runnable() {
	         @Override
	         public void run() {
	        	 try {
					updateCommandHandler(command);
				} catch (IOException e) {
					e.printStackTrace();
				}
	         }
		});
		
		readerThread.start();
		commandHandlerThread.start();
		
	}
	
	public static String read() throws IOException {
		 command = reader.readLine();
		 return command;
	}

	public static void updateCommandHandler(String command) throws IOException {
		
		if (COMMAND_LIST.contains(command)) {
			System.out.println("true");
		} else {
			 System.out.println("Invalid command: " + command);
		}
		
		
	}
	
	
}
