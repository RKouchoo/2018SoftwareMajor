package com.rkouchoo.command;

import com.rkouchoo.Constants;
import com.rkouchoo.interfaces.CommandExecutorInterface;
import com.rkouchoo.webserver.WebServer;

public class CommandExecutor implements CommandExecutorInterface {
	
	WebServer localWebServer;
	ConsoleReader localConsoleReader;
	
	private CommandList setCommand;
	
	private String consoleOutput = "";
		
	public CommandExecutor(ConsoleReader consoleReader, WebServer server) {
		this.localWebServer = server;
		this.localConsoleReader = consoleReader;
	}

	@Override
	public void query() {
		if (localConsoleReader.getSupplier().getRaw() != null) {
			for (CommandList commandLocal : CommandList.values()) {
				if (localConsoleReader.getSupplier().getCommand().equalsIgnoreCase(commandLocal.getNativeCommand())) {
					setCommand = commandLocal;
					executeCommand(setCommand);
					localConsoleReader.getSupplier().clearAll();
					System.out.println(Constants.CONSOLE_UI_PRINT + consoleOutput);
					return;
				}
			}
			consoleOutput = Constants.CONSOLE_ERROR_MESSAGE + localConsoleReader.getSupplier().getRaw() + Constants.COMMAND_HELP_MESSAGE;	
			localConsoleReader.getSupplier().clearAll();
			System.out.println(Constants.CONSOLE_UI_PRINT + consoleOutput);
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
				this.consoleOutput = "add --!";		
			break;
			
		case ECHO:
			this.consoleOutput = "echo --!";
			break;
			
		case RELOAD:
			this.consoleOutput = "reload --!";
			break;
			
		case START:
			this.consoleOutput = "start --!";
			break;
			
		case STOP:
			this.consoleOutput = "stop --!";
			break;
			
		case EXIT:
			this.consoleOutput = "exit --!";
			break;
			
		default:
			break;
		}
			
	}

}

