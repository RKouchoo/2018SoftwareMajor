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
			localConsoleReader.getSupplier().clearAll();
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
				System.out.println("add --!");		
			break;
			
		case ECHO:
			System.out.println("echp --!");
			break;
			
		case RELOAD:
			System.out.println("reload --!");
			break;
			
		case START:
			System.out.println("start --!");
			break;
			
		case STOP:
			System.out.println("stop --!");
			break;
			
		case EXIT:
			System.out.println("exit --!");
			break;
			
		default:
			break;
		}
			
	}

}

