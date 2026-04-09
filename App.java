package main.java.com.example.src.test.main.java.com.src.main.com.example;
import java.io.*;
import java.net.*;

public class App {
    public static void main(String[] args) {
        new Thread(() -> startServer(5000)).start(); // Server on Port 5000
        sendMessage("localhost", 5000, "Hello from Port A!"); // Client
    }

    public static void startServer(int port) {
        try (ServerSocket server = new ServerSocket(port)) {
            Socket socket = server.accept();
            DataInputStream in = new DataInputStream(socket.getInputStream());
            System.out.println("Received: " + in.readUTF());
        } catch (IOException e) { e.printStackTrace(); }
    }

    public static void sendMessage(String host, int port, String msg) {
        try (Socket socket = new Socket(host, port)) {
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            out.writeUTF(msg);
        } catch (IOException e) { e.printStackTrace(); }
    }
}
