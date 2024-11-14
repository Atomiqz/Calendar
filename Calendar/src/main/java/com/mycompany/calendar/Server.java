/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.calendar;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author josho
 */
public class Server {

    private static ServerSocket servSock; // listen for incoming connections
    private static final int PORT = 1234; // set port number
    private static int clientConnections = 0; // variable to keep track of number of clients

    public static void main(String[] args) {
        System.out.println("Opening port...\n");
        try {
            servSock = new ServerSocket(PORT); // initialise server socket
        } catch (IOException e) {
            System.out.println("Unable to attach to port!");
            System.exit(1);
        }

        // continuously listen for client connections
        while (true) {
            run(); // each connection in a new thread
        }
    }

    private static void run() {
        Socket link;
        try {
            link = servSock.accept(); // accept incoming connection
            clientConnections++; // increment number of clients
            String clientID = "Client " + clientConnections; // create client ID
            Runnable resource = new ClientConnection(link, clientID); // create new client connection
            Thread t = new Thread(resource); // create new thread for each client
            t.start(); // start thread
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
