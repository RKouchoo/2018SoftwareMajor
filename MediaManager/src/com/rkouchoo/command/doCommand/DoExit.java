package com.rkouchoo.command.doCommand;

import com.rkouchoo.command.CommandExecutor;
import com.rkouchoo.interfaces.DoCommandInterface;

public class DoExit implements DoCommandInterface {

	@Override
	public String run(String args, CommandExecutor exec) {
		exec.stopSystemLoop();
		return "Exiting program!";
	}

}
