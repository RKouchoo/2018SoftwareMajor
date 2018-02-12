package com.rkouchoo.interfaces;

public interface ConsoleReaderInterface {
	
	public enum SendableType {
		COMMAND,
		ARGUMENTS,
		RAW
	}
	
	public WantedStringSupplierInterface getSupplier();

}
