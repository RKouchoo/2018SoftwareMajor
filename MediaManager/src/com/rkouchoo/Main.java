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
	
	public static enum currentThreadState {
		KILLED,
		ALIVE,
		RUNNING_COMMAND,
		AWAITING_COMMAND
	}
	
	public static void main(String[] args) throws IOException {
		
		// Enter data using a buffered reader for the console
		reader = new BufferedReader(new InputStreamReader(System.in));		
		
		createReaderThread();
		createCommandHandlerThread();
		
		readerThread.start();
		commandHandlerThread.start();
	}
	
	public static String read() throws IOException {
		 command = reader.readLine();
		 return command;
	}

	public static void updateCommandHandler(String command) throws IOException {
		
		if (COMMAND_LIST.contains(command)) {
			System.out.println("> true \n");
			System.out.println("\n");
		} else {
			 System.out.println("> Invalid command: " + command);
		}
		
		
	}
	
	public static currentThreadState pokeThreads() {
		if (!readerThread.isAlive()) {
			readerThread.start();
			return currentThreadState.KILLED;
		}
		
		if (!commandHandlerThread.isAlive()) {
			commandHandlerThread.start();
			return currentThreadState.KILLED;
		} else {
			return currentThreadState.AWAITING_COMMAND;
		}
	}
	
	private static void createReaderThread() {
		// Create a new thread to handle input reading
		readerThread = new Thread(new Runnable() {
	         @Override
	         public void run()  {
	        	 while(true) {
	        		 	try {
	        			 	command = read();
	        			 	lastCommand = command;
	        			 	updateCommandHandler(command);
	        		 	} catch (IOException ex) {
	        			 	System.out.println(ex);
	        		 	}	
	        	}
	         }
		});
		
	}
	
	private static void createCommandHandlerThread() {
		// Create a thread to handle commands when they are sent around the statemachine
		commandHandlerThread = new Thread(new Runnable() {
			@Override
        	public void run() {
       	 	try {
					updateCommandHandler(command);
				} catch (IOException ex) {
					ex.printStackTrace();
				}
        	}
		});
	
	}
	
	
	
	
}
