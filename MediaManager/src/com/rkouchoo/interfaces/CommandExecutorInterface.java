package com.rkouchoo.interfaces;

import java.util.List;

public interface CommandExecutorInterface {
	
	public static enum CommandList {
		
	}
	
	public void register(List commandList);
	
	public void registerCommand(CommandList command);

	
	
}
