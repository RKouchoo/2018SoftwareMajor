package com.rkouchoo.interfaces;

import com.rkouchoo.command.CommandExecutor;

public interface DoCommandInterface {
	
	public String run(String args, CommandExecutor exec);

}
