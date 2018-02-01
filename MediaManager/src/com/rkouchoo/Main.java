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
	private static String lastCommand;
	private static String command = "";

	private static Thread readerThread;
	private static Thread commandHandlerThread;
	private static Thread webServerThread;

	public static final String[] COMMAND_ARRAY = new String[] { "start", "reload", "stop", "edit", "addurl" };
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

		// Create the threads before we run them
		createReaderThread();
		createCommandHandlerThread();
		createWebServerThread();

		readerThread.start();
		commandHandlerThread.start();
		webServerThread.start();

	}

	public static String read() throws IOException {
		command = reader.readLine();
		return command;
	}

	public static void updateCommandHandler(String command) throws IOException {

		if (COMMAND_LIST.contains(command)) {
			System.out.println("> true");
		} else {
			if (command == "") {
				return;
			} else {
				System.out.println("> Invalid command: " + command + "\n> ");
				System.out.println(command);
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
				while (true) { // Main command poller, ugly and thread locks but it whould work
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

	public static void createWebServerThread() {
		// Create a thread to run and take care of the web server
		webServerThread = new Thread(new Runnable() {
			@Override
			public void run() {
				// start the web server
				try {
					WebServer server = new WebServer("", 80);
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

}
