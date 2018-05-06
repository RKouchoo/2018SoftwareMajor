package com.rkouchoo.interfaces;

public interface CommandExecutorInterface {
	
	// The literal implementations of what the user will input.
	String start = "start";
	String stop = "stop";
	String reload = "reload";
	String add = "add";
	String echo = "echo";
	String exit = "exit";
	String help = "help";
	String qMark = "?";
	
	// enum for passing around commands. you can get the literal by calling getNativeCommand()
	public static enum CommandList {
		START (start),
		RELOAD (reload),
		STOP (stop),
		ADD (add),
		ECHO (echo), // as a test command to see if the console is responding!
		EXIT (exit),
		HELP (help),
		QMARK (qMark); 
		
		private final String command;
		
		private CommandList(String commandLocal){
			this.command = commandLocal;
		}
		
		public String getNativeCommand() {
			return command;
		 }
	}
	
	public static enum LocalCommandStatus {
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
	
	CommandExecutorInterface setLocalStatus(LocalCommandStatus commandStatus);

	public void executeCommand(CommandList command, String args, boolean isCommandOnly);
		
	public void query();
	
	public boolean getProgramStatus();
}
