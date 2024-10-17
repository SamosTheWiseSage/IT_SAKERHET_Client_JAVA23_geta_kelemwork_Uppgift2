package org.example;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class TimeCapsuleClient {
    private static final String SERVER_URL = "localhost"; // Use localhost for local testing
    private static final int SERVER_PORT = 8080;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String jwt = null;

        while (true) {
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Create Time Capsule");
            System.out.println("4. Read Time Capsules");
            System.out.println("5. Exit");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.print("Email: ");
                    String email = scanner.nextLine();
                    System.out.print("Password: ");
                    String password = scanner.nextLine();
                    System.out.println(sendRequest("register," + email + "," + password));
                    break;
                case 2:
                    System.out.print("Email: ");
                    email = scanner.nextLine();
                    System.out.print("Password: ");
                    password = scanner.nextLine();
                    jwt = sendRequest("login," + email + "," + password);
                    System.out.println("JWT: " + jwt);
                    break;
                case 3:
                    if (jwt != null) {
                        System.out.print("Enter message for time capsule: ");
                        String message = scanner.nextLine();
                        System.out.println(sendRequest("create," + message + "," + jwt));
                    } else {
                        System.out.println("You must log in first.");
                    }
                    break;
                case 4:
                    if (jwt != null) {
                        System.out.println("Reading time capsules...");
                        System.out.println(sendRequest("read," + jwt));
                    } else {
                        System.out.println("You must log in first.");
                    }
                    break;
                case 5:
                    System.exit(0);
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static String sendRequest(String request) {
        try (Socket socket = new Socket(SERVER_URL, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            out.println(request);
            return in.readLine();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error communicating with server.";
        }
    }
}
