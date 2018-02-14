package com.rkouchoo.command;

import com.rkouchoo.Constants;
import com.rkouchoo.command.doCommand.DoAdd;
import com.rkouchoo.command.doCommand.DoEcho;
import com.rkouchoo.command.doCommand.DoExit;
import com.rkouchoo.command.doCommand.DoReload;
import com.rkouchoo.command.doCommand.DoStart;
import com.rkouchoo.command.doCommand.DoStop;

import com.rkouchoo.interfaces.CommandExecutorInterface;
import com.rkouchoo.webserver.WebServer;

public class CommandExecutor implements CommandExecutorInterface {
	
	WebServer localWebServer;
	ConsoleReader localConsoleReader;
	
	private String consoleOutput = "";
	
	private DoAdd doAdd;
	private DoEcho doEcho;
	private DoReload doReload;
	private DoStart doStart;
	private DoStop doStop;
	private DoExit doExit;
		
	private boolean exitStatus = true;
	
	public CommandExecutor(ConsoleReader consoleReader, WebServer server) {
		this.localWebServer = server;
		this.localConsoleReader = consoleReader;
		
		doAdd = new DoAdd();
		doEcho = new DoEcho();
		doReload = new DoReload();
		doStart = new DoStart();
		doStop = new DoStop();
		doExit = new DoExit();
	}

	@Override
	public void query() {
		if (localConsoleReader.getSupplier().getRaw() != null) {
			
			for (CommandList commandLocal : CommandList.values()) {
				if (localConsoleReader.getSupplier().getCommand().equalsIgnoreCase(commandLocal.getNativeCommand())) {
					
					executeCommand(
							commandLocal, 
							localConsoleReader.getSupplier().getArgs(), 
							localConsoleReader.getSupplier().isCommandOnly()
							);
				
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
		return exitStatus;
	}
	
	@Override
	public CommandExecutorInterface setLocalStatus(LocalCommandStatus commandStatus) {
		
		return this;
	}

	private boolean check(String args) {
		if (args == null) {
			return true;
		}
		return false;
	}
	
	@Override
	public void executeCommand(CommandList command, String args, boolean isCommandOnly) {
		switch (command) {
		case ADD:
			if (check(args)) {
				this.consoleOutput = Constants.CONSOLE_NULL_ARGUMENT + Constants.COMMAND_HELP_MESSAGE;
				break;
			}
			
			this.consoleOutput = "add --!" + doAdd.run(args, this);		
			break;
			
		case ECHO:
			if (check(args)) {
				this.consoleOutput = Constants.CONSOLE_NULL_ARGUMENT + Constants.COMMAND_HELP_MESSAGE;
				break;
			}
			
			this.consoleOutput = doEcho.run(args, this);
			break;
			
		case RELOAD:
			this.consoleOutput = "reload --!" + doReload.run(args, this);
			break;
			
		case START:
			this.consoleOutput = "start --!" + doStart.run(args, this);
			break;
			
		case STOP:
			this.consoleOutput = "stop --!" + doStop.run(args, this);
			break;
			
		case EXIT:
			this.consoleOutput = "exit --!" + doExit.run(args, this);
			break;
			
		default:
			break;
		}
			
	}
	
	// Set the boolean that Main.java queries when it runs the main thread.
	public void stopSystemLoop() {
		this.exitStatus = false;
	}
	
}

