package com.rkouchoo.command;

import com.rkouchoo.interfaces.WantedStringSupplierInterface;

public class WantedStringSupplier implements WantedStringSupplierInterface {

	private String args = "";
	private String command = "";
	private String raw = "";
	
	private boolean isCommandOnly = false;
	
	@Override
	public String getCommand() {
		return command;
	}

	@Override
	public String getArgs() {
		return args;
	}

	@Override
	public String getRaw() {
		return raw;
	}
	
	@Override
	public WantedStringSupplierInterface setCommand(String command) {
		this.command = command;
		return this;
	}

	@Override
	public WantedStringSupplierInterface setArgs(String args) {
		this.args = args;
		return this;
	}
	
	@Override
	public WantedStringSupplierInterface setRaw(String raw) {
		this.raw = raw;
		return this;
	}

	@Override
	public WantedStringSupplierInterface setCommandOnly(boolean info) {
		this.isCommandOnly = info;
		return this;
	}

	@Override
	public boolean isCommandOnly() {
		return isCommandOnly;
	}

}
