/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.calendar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 *
 * @author josho
 */
public class Client {

    private static InetAddress host; // stores the server's address
    private static final int PORT = 1234; // port number

    public static void main(String[] args) {
        try {
            host = InetAddress.getLocalHost(); // retrieves local host address
            run(); // starts the client
        } catch (UnknownHostException e) {
            // handles the exception if the host address is not found
            System.out.println("Host ID not found!");
            System.exit(1);
        }
    }

    private static void run() {
        Socket link = null; // initialise socket connection to null
        try {
            link = new Socket(host, PORT); // sets socket connection to server
            
            // input and output streams
            BufferedReader in = new BufferedReader(new InputStreamReader(link.getInputStream()));
            PrintWriter out = new PrintWriter(link.getOutputStream(), true);

            // Set up stream for user input
            BufferedReader userEntry = new BufferedReader(new InputStreamReader(System.in));
            String message;

            // welcome message and instructions
            System.out.println("***************************************");
            System.out.println("   Welcome to Dublin Events Calendar   ");
            System.out.println("***************************************");
            System.out.println("Here, you can manage and view events happening in Dublin.");
            System.out.println("Please use the following commands to interact with the calendar:");
            System.out.print("""
                1. Type 'add' to add a new event
                2. Type 'remove' to remove an existing event
                3. Type 'STOP' to exit the application
                """);
            System.out.println("\nTo add or remove events, please use the following format:");
            System.out.println("Command format: add/remove; date (DD MMM YYYY); time; description\n");

            // loop to keep client running until STOP is recieved
            while (true) {
                System.out.print("Enter message to be sent to server (or type STOP to exit): ");
                message = userEntry.readLine(); // read user input
                out.println(message); // send message to server

                // if STOP, break loop and close connection to server
                if (message.equalsIgnoreCase("STOP")) {
                    String response = in.readLine();
                    System.out.println("\nSERVER RESPONSE> " + response);
                    break;
                }

                // read and display server response
                String response = in.readLine();
                System.out.println("\nSERVER RESPONSE> " + response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                System.out.println("\n* Closing connection... *");
                if (link != null) {
                    link.close();
                }
            } catch (IOException e) {
                System.out.println("Unable to disconnect/close!");
                System.exit(1);
            }
        }
    }
}

// SAMPLE ADD ENTRIES
// add; 2 November 2024; 6pm; Study
// add; 2 November 2024; 6pm; Fireworks Dublin City Centre
// add; 2 November 2024; 12pm; Food Market IFSC Square
// add; 2 November 2024; 7.30pm; Jersey Boys Bord Gais Energy Theatre
// SAMPLE REMOVE ENTRIES
// remove; 2 November 2024; 6pm; Study
// remove; 2 November 2024; 6pm; Fireworks Dublin City Centre
// remove; 2 November 2024; 12pm; Food Market IFSC Square
// remove; 2 November 2024; 7.30pm; Jersey Boys Bord Gais Energy Theatre
