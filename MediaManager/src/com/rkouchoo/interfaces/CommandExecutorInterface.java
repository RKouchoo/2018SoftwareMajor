package com.rkouchoo.interfaces;

public interface CommandExecutorInterface {
	
	public static enum CommandList {
		START,
		RELOAD,
		STOP,
		ADD,
		ECHO // as a test command to see if the server is responding!
	}
	
	public static enum localCommandStatus {
		STARTING,
		IDLE,
		RUNNING,
		FAILED
	}

	public static enum localFailedCommandCarrier {
		CANNOT_STOP,
		STARTED,
		IO_ERROR
	}
	
	CommandExecutorInterface setLocalStatus(localCommandStatus commandStatus);

	public void executeCommand(CommandList command);
		
}
