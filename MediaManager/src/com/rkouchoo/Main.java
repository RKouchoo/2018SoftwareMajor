package com.rkouchoo;

import java.util.List;

import com.rkouchoo.webserver.WebServer;
import com.rkouchoo.webserver.WebServerException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class Main {

	private static BufferedReader reader;
	private static Thread readerThread;
	private static Thread commandHandlerThread;
	private static Thread webServerThread;
	
	private static WebServer server;

	public static final String[] COMMAND_ARRAY = new String[] { "start", "reload", "stop", "edit", "addurl" };
	public static final List<String> COMMAND_LIST = Arrays.asList(COMMAND_ARRAY);
	
	public static String currentCommand = "";
	public static Boolean killed = true;

	public static enum currentThreadState {
		KILLED, 
		ALIVE, 
		RUNNING_COMMAND, 
		AWAITING_COMMAND
	}

	public static void main(String[] args) throws IOException {

		// Enter data using a buffered reader for the console
		reader = new BufferedReader(new InputStreamReader(System.in));

		// Create the threads before we run them
		createReaderThread();
		createCommandHandlerThread();
		createWebServerThread();

		//readerThread.start();
		commandHandlerThread.start();
		
		//webServerThread.start();
	}

	public static String read() {
		try {
			return reader.readLine();	
		} catch (IOException e) {
			System.out.println("Could not read the console!");
		}
		return null;
	}

	public static void updateCommandHandler(String command) { 
		// Convert the input string to a list so we can be able to compare it easier.
		String[] tempArray = {command};
		List<String> tempCommand = Arrays.asList(tempArray);
		if (COMMAND_LIST.contains(command)) {
			for (int i = 0; i < COMMAND_LIST.size(); i++) {
				if (tempCommand.contains(COMMAND_LIST.get(i))) {	
					if (i == 0) {
						startWebServer();		
						return;	
					} else if (i == 1) {
						System.out.println("Reloading program!");
					} else if (i == 2) {
						System.out.println("Cannot stop web server due to a new java depreciation. Please kill the process instead by closing the console!");
					}
				}				
			}
		}
	}

	@Deprecated // When I do this the thread will lock and not work properly.
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
			public void run() {
				while (true) {
					currentCommand = read();
				}
			
			}
		});

	}

	private static void createCommandHandlerThread() {
		// Create a thread to handle commands when they are sent around the statemachine
		commandHandlerThread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					System.out.println(read());
					updateCommandHandler(read());		
				}
			}
		});

	}

	public static void createWebServerThread() {
		// Create a thread to run and take care of the web server
		webServerThread = new Thread(new Runnable() {
			@Override
			public void run() {
				// start the web server
				try {
					server = new WebServer("", 80);
					server.activate();
				} catch (WebServerException e) {
					System.out.println(e.toString());
				}
			}
		});
	}

	public void printConsolePin() {
		System.out.print("\n >");
	}
	
	public static void startWebServer() {
			webServerThread.start();
			System.out.println("Started web server!");
			System.out.println("Cannot start the webserver multiple times!");
	}
}
