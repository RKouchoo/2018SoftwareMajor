package com.rkouchoo.mm;

public class Constants {

	public static final String WINDOW_TITLE = "Media Manager"; // title of the window
	public static final int WINDOW_WIDTH = 1280; // screen resoution
	public static final int WINDOW_HEIGHT = 720;
	public static final boolean WINDOW_NATIVE_LOCATION = false;
	
	public static final int VISIBLE_ROW_COUNT = 15;
	
	public static final String[] TAB_NAMES = {
			"Icon", 
			"File", 
			"Path/name", 
			"File Type", 
			"Last Modified", 
			"R", 
			"W", 
			"E", 
			"Size (bytes)"
	};
	
	public static final String WINDOW_ICON_PROJECT_PATH = "/res/exp_icon.png";
	public static final int ROW_ICON_PADDING = 6;
	
	public static final String HIDDEN_FILE_NAME = ".fmeta"; // short for file meta
}
