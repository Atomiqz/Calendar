/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.calendar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author josho
 */
public class ClientConnection implements Runnable {

    private Socket clientLink;
    private String clientID;
    private static ArrayList<Event> events = new ArrayList<>();
    private static final Object lock = new Object(); // lock object for thread-safe operations - adapted from: https://www.baeldung.com/java-synchronized

    public ClientConnection(Socket connection, String cID) {
        this.clientLink = connection;
        this.clientID = cID;
    }

    public void run() {
        try {
            // input and output streams
            BufferedReader in = new BufferedReader(new InputStreamReader(clientLink.getInputStream()));
            PrintWriter out = new PrintWriter(clientLink.getOutputStream(), true);
            String message;

            // process incoming messages from clients
            while ((message = in.readLine()) != null) {

                // if stop message received, terminate session
                if (message.equalsIgnoreCase("STOP")) {
                    out.println("TERMINATE");
                    break;
                }

                try {
                    System.out.println("Message received from " + clientID + ": " + message); // log message to server console
                    String response = Message(message); // process message
                    out.println(response); // send response to client
                } catch (IncorrectActionException e) {
                    out.println("Error: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // close client
                System.out.println("\n* Closing connection with the client " + clientID + " ... *");
                clientLink.close();
            } catch (IOException e) {
                System.out.println("Unable to disconnect!");
            }
        }
    }

    // process client message and return response
    private String Message(String message) throws IncorrectActionException {
        String[] parts = message.split("; "); // split message into parts
        if (parts.length < 4) {
            // if message doesn't follow expect format, throw exception
            throw new IncorrectActionException("Message must be in the format: action; date; time; description");
        }

        // parse message into parts and assign them to variables
        String action = parts[0].trim();
        String date = parts[1].trim();
        String time = parts[2].trim();
        String description = parts[3].trim();

        // synchronised block to perform add or remove
        synchronized (lock) {
            if (action.equalsIgnoreCase("add")) {
                return addEvent(date, time, description);
            } else if (action.equalsIgnoreCase("remove")) {
                return removeEvent(date, time, description);
            } else {
                // throw exception for unsupported actions
                throw new IncorrectActionException("Invalid action: " + action);
            }
        }

    }

    // add new event to ArrayList, return list of events on same day
    private String addEvent(String date, String time, String description) {
        Event newEvent = new Event(date, time, description);

        synchronized (lock) {
            events.add(newEvent); // add event to events list
            return getEventsOnDate(date); // return events on same day as added event
        }
    }

    // removes event and returns updated events list on same day as removed event
    private String removeEvent(String date, String time, String description) {
        boolean eventFound = false; // boolean to determine if event is found
        synchronized (lock) {
            // loops through events list looking for matching event
            for (int i = 0; i < events.size(); i++) {
                Event event = events.get(i);
                // if event is found
                if (event.getDate().equals(date) && event.getTime().equals(time) && event.getDesc().equals(description)) {
                    events.remove(i); // event removed
                    eventFound = true; // mark event as found
                    break; // break loop
                }
            }
        }

        // returns updated events list on same day as removed event
        if (eventFound) {
            return getEventsOnDate(date);
        } else {
            return "No event found for: " + date + ", " + time + ", " + description; // if no event found, return message
        }

    }

    // gets all events on specified date and returns a formatted string list
    private String getEventsOnDate(String date) {
        StringBuilder response = new StringBuilder();

        synchronized (events) {
            for (Event event : events) {
                if (event.getDate().equals(date)) {
                    // Insert the date only once at the start if it's the first matching event
                    if (response.length() == 0) {
                        response.append(date).append("; ");
                    }
                    // Append the event time and description
                    response.append(event.getEvent());
                }
            }
        }

        if (response.isEmpty()) {
            return "No events for that date."; // Return a message if no events are found
        } else {
            return response.toString().trim();
        }

    }

    // custom exception class for handling incorrect actions
    public static class IncorrectActionException extends Exception {

        public IncorrectActionException(String message) {
            super(message);
        }
    }
}
