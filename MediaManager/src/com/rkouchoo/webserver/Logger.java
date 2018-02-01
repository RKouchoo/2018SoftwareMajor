package com.rkouchoo.webserver;


/**
 * A logging class which prefixes messages to the standard output with
 * human readable timestamps.
 * 
 */
public class Logger {
    
    private Logger() {
        // Prevent this class from being constructed.
    }
    
    public static void log(String ip, String request, int code) {
        System.out.println("[" + new java.util.Date().toString() + "] " + ip + " \"" + request + "\" " + code);
    }
    
}