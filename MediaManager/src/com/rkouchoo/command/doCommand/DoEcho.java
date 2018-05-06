package com.rkouchoo.command.doCommand;

import com.rkouchoo.command.CommandExecutor;
import com.rkouchoo.interfaces.DoCommandInterface;

public class DoEcho implements DoCommandInterface {
	
	public DoEcho() {
		
	}

	@Override
	public String run(String args, CommandExecutor exec) {
		return args;
	}
	
}