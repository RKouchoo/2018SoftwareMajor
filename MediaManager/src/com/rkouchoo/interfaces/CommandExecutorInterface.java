package com.rkouchoo.interfaces;

@SuppressWarnings("unused")
public interface CommandExecutorInterface {
	
	// The literal implementations of what the user will input.
	String start = "start";
	String stop = "stop";
	String reload = "reload";
	String add = "add";
	String echo = "echo";
	
	// binding those inputs to an enum. you can get the literal by calling getNativeCommand()
	public static enum CommandList {
		START (start),
		RELOAD (stop),
		STOP (reload),
		ADD (add),
		ECHO (echo); // as a test command to see if the server is responding!
		
		private final String command;
		
		private CommandList(String commandLocal){
			this.command = commandLocal;
		}
		
		public String getNativeCommand() {
			return command;
		 }
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
		
	public void query();
	
	public boolean getProgramStatus();
}
