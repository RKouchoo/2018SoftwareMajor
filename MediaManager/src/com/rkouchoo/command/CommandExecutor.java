package com.rkouchoo.command;

import com.rkouchoo.Constants;
import com.rkouchoo.interfaces.CommandExecutorInterface;
import com.rkouchoo.webserver.WebServer;

public class CommandExecutor implements CommandExecutorInterface {
	
	WebServer localWebServer;
	ConsoleReader localConsoleReader;
	
	private CommandList setCommand;
	
	private String cOutput = "";
	
	public CommandExecutor(ConsoleReader consoleReader, WebServer server) {
		this.localWebServer = server;
		this.localConsoleReader = consoleReader;
	}

	@Override
	public void query() {
		if (localConsoleReader.getSupplier().getRaw() != null) {
			System.out.println(cOutput + Constants.CONSOLE_UI_PRINT);
			for (CommandList commandLocal : CommandList.values()) {
				if (localConsoleReader.getSupplier().getCommand().equalsIgnoreCase(commandLocal.getNativeCommand())) {
					setCommand = commandLocal;
					executeCommand(setCommand);
				}
			}
		}
	}

	@Override
	public boolean getProgramStatus() {
		return true;
	}
	
	@Override
	public CommandExecutorInterface setLocalStatus(LocalCommandStatus commandStatus) {
		
		return this;
	}

	@Override
	public void executeCommand(CommandList command) {
		switch (command) {
		case ADD:
						
			break;
			
		case ECHO:
			
			break;
			
		case RELOAD:
			
			break;
			
		case START:
			
			break;
			
		case STOP:
			
			break;
			
		default:
			break;
		}
			
	}

}

