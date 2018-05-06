package com.rkouchoo.webserver;

/**
 * A custom exception class for the web server.
 * 
 * @author Copyright Paul Mutton, http://www.jibble.org/
 */
public class WebServerException extends Exception {
    
    public WebServerException(String e) {
        super(e);
    }
    
}