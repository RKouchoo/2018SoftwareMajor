package com.rkouchoo.interfaces;

public interface WantedStringSupplierInterface {
	
	public String getCommand();

	public String getArgs();
	
	public String getRaw();

	public WantedStringSupplierInterface setCommand(String command);

	public WantedStringSupplierInterface setArgs(String args);
	
	public WantedStringSupplierInterface setRaw(String raw);
	
	public WantedStringSupplierInterface setCommandOnly(boolean info);
	
	public boolean isCommandOnly();
	
}
