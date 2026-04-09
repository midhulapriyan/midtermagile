package main.java.com.example.src.test.main.java.com;

package com.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.io.*;
import java.net.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import static org.junit.jupiter.api.Assertions.*;

public class AppTest {

    @Test
    @DisplayName("Test Message Transmission between two ports")
    public void testMessageTransmission() throws Exception {
        int testPort = 5005; // Using a different port for testing to avoid conflicts
        String testMessage = "CI_JUnit_Validation_Success";

        // 1. Start the Server in a background thread to listen for the message
        CompletableFuture<String> serverResult = CompletableFuture.supplyAsync(() -> {
            try (ServerSocket serverSocket = new ServerSocket(testPort)) {
                // Set a 5-second timeout so the test doesn't hang forever if it fails
                serverSocket.setSoTimeout(5000); 
                
                try (Socket clientSocket = serverSocket.accept();
                     DataInputStream in = new DataInputStream(clientSocket.getInputStream())) {
                    return in.readUTF(); // This captures the message sent by the client
                }
            } catch (IOException e) {
                return "Error: " + e.getMessage();
            }
        });

        // 2. Small pause to ensure the server thread is up and listening
        Thread.sleep(500);

        // 3. Call your actual App's sendMessage method to transmit the message
        App.sendMessage("localhost", testPort, testMessage);

        // 4. Retrieve the message the server actually received
        String actualReceivedMessage = serverResult.get(10, TimeUnit.SECONDS);

        // 5. Assertion: If these don't match, Jenkins will mark the 'Test' stage as FAILED
        assertEquals(testMessage, actualReceivedMessage, 
            "The message received by the server must match the message sent by the client.");
    }
}
