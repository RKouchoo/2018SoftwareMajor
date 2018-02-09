package com.rkouchoo.command;

import com.rkouchoo.interfaces.CommandExecutorInterface;
import com.rkouchoo.webserver.WebServer;

public class CommandExecutor implements CommandExecutorInterface {
	
	WebServer localWebServer;
	ConsoleReader localConsoleReader;
	
	private String wantedString;
	
	public CommandExecutor(ConsoleReader consoleReader, WebServer server) {
		this.localWebServer = server;
		this.localConsoleReader = consoleReader;
	}

	@Override
	public void query() {
		wantedString = localConsoleReader.get(10);
		System.out.println(CommandList.ECHO.getNativeCommand());
	}

	@Override
	public boolean getProgramStatus() {
		return true;
	}
	
	@Override
	public CommandExecutorInterface setLocalStatus(localCommandStatus commandStatus) {
		
		return this;
	}

	@Override
	public void executeCommand(CommandList command) {
		
	}

}

