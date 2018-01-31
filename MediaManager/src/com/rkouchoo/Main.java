package com.rkouchoo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

	private static BufferedReader reader;
	private static String lastName;
	private static String name;
	private static Thread readerThread;
	
	public static void main(String[] args) throws IOException {
		
		// Enter data using a buffered reader for the console
		reader = new BufferedReader(new InputStreamReader(System.in));
		
		// Create a new thread to handle input reading
		readerThread = new Thread(new Runnable() {
	         @Override
	         public void run()  {
	        	 while (name == lastName) {
	        		 try {
	        			 System.out.println(read());
	        		 } catch (IOException ex) {
	        			 System.out.println(ex);
	        		 }	
	     		}	 
	         }
	});		

		readerThread.start();
		
	}
	
	public static String read() throws IOException {
		 name = reader.readLine();
		 lastName = name;
		 return name;
	}
	
	
}
