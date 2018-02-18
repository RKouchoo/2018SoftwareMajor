package com.rkouchoo.command.doCommand;

import com.rkouchoo.command.CommandExecutor;
import com.rkouchoo.interfaces.DoCommandInterface;

public class DoHelp implements DoCommandInterface {

	@Override
	public String run(String args, CommandExecutor exec) {
		String help = 
		"Command list: \n \n" +
		"> start		-> start the web server and GUI. \n" + // Looks messy but allows it to print nicely.
		"> stop		-> depreciated! use to stop the webserver. \n"+
		"> echo [args]	-> echo what you put as args. \n" +
		"> reload	-> reload all elements and webserver. \n" +
		"> exit		-> exit the program killing the webserver. \n>";
		return help;
	}

}
