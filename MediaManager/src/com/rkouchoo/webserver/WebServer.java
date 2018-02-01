package com.rkouchoo.webserver;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * The central class to the Jibble Web Server.  This is instantiated
 * by the WebServerMain class and listens for connections on the
 * specified port number before starting a new RequestThread to
 * allow connections to be dealt with concurrently.
 * 
 */
public class WebServer {
    
    public WebServer(String rootDir, int port) throws WebServerException {
        try {
            _rootDir = new File(rootDir).getCanonicalFile();
        }
        catch (IOException e) {
            throw new WebServerException("Unable to determine the canonical path of the web root directory.");
        }
        if (!_rootDir.isDirectory()) {
            throw new WebServerException("The specified root directory does not exist or is not a directory.");
        }
        _port = port;
    }
    
    public void activate() throws WebServerException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(_port);
        }
        catch (Exception e) {
            throw new WebServerException("Cannot start the web server on port " + _port + ".");
        }
        
        // Keep all RequestThreads within their own thread group for tidyness.
        ThreadGroup threadGroup = new ThreadGroup("HTTP Request Thread Group");
        while (_active) {
            try {
                // Pass the socket to a new thread so that it can be dealt with
                // while we can go and get ready to accept another connection.
                Socket socket = serverSocket.accept();
                RequestThread requestThread = new RequestThread(socket, _rootDir);
                Thread thread = new Thread(threadGroup, requestThread);
                thread.start();
            }
            catch (Exception e) {
                throw new WebServerException("Error processing new connection: " + e);
            }
        }
    }
    
    private File _rootDir;
    private int _port;
    private boolean _active = true;

}