package com.rkouchoo.command;

import com.rkouchoo.interfaces.CommandExecutorInterface;
import com.rkouchoo.webserver.WebServer;

public class CommandExecutor implements CommandExecutorInterface {
	
	WebServer localWebServer;
	ConsoleReader localConsoleReader;
	
	public CommandExecutor(ConsoleReader consoleReader, WebServer server) {
		this.localWebServer = server;
		this.localConsoleReader = consoleReader;
	}

	@Override
	public CommandExecutorInterface setLocalStatus(localCommandStatus commandStatus) {
		
		return this;
	}

	@Override
	public void executeCommand(CommandList command) {
		
	}
}
