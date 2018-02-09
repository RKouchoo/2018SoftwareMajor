package com.rkouchoo;

import com.rkouchoo.command.CommandExecutor;
import com.rkouchoo.command.ConsoleReader;
import com.rkouchoo.webserver.WebServer;
import com.rkouchoo.webserver.WebServerException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@SuppressWarnings("unused")
public class Main {
	
	private static Thread commandThread;
	private static BufferedReader reader;
	
	private static WebServer server;
	private static CommandExecutor commandExecutor;
	private static ConsoleReader consoleReader;
	
	public static void main(String[] args) throws IOException, WebServerException {
		
		/**
		 * Create the modules and objects needed for the program to run.
		 */
		reader = new BufferedReader(new InputStreamReader(System.in));
		
		server = new WebServer(Constants.WEB_SERVER_LOCATION, Constants.WEB_SERVER_PORT);
		consoleReader = new ConsoleReader(reader);
		commandExecutor = new CommandExecutor(consoleReader, server);
		
		/**
		 * Start running the program after everything has been created.
		 */
		
	}
	
	public void createCommandExecutorThread() {
		
	}
}
