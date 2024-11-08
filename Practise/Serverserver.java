package Practise;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Serverserver {
    public static void main(String[] args) {
        // Set up server
        try (ServerSocket ss = new ServerSocket(1234)) {
            System.out.println("**************");
            System.out.println("SERVER STARTED");
            System.out.println("**************\n");
            // Accept client connection
            try (Socket socket = ss.accept()) {
                System.out.println("Connected to the Client...\n");
                //Printwriter is used to send data to the client
                PrintWriter p = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                String menu = 
                "============================================\n" +
                "Welcome to the Messaging App Functionalities!\n" +
                "============================================\n\n" +
                "Enter your Choice:\n" +
                "1: Send Message\n" +
                "2: Display Messages\n" +
                "3: Find Message\n" +
                "4: Delete Message\n" +
                "5: Display Contacts\n" +
                "6: Add Contact\n" +
                "7: Delete Contact\n" +
                "8: Exit\n";
                p.println(menu); //or write p.write("...\n"); including a new line
                p.flush(); // Ensure data is sent immediately
                
                // Receive client response
                BufferedReader b = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String choice = b.readLine();
                if (choice != null) {
                    int ch = Integer.parseInt(choice);
                    System.out.println("Client chose: "+ch+"\n");
    
                    // Respond based on client choice
                    switch (ch) {
                        case 1 -> System.out.println("Client chose to send a message.");
                        case 2 -> System.out.println("Displaying messages for the client.");
                        case 3 -> System.out.println("Client wants to find a message.");
                        case 4 -> System.out.println("Client wants to delete a message.");
                        case 5 -> System.out.println("Displaying contacts for the client.");
                        case 6 -> System.out.println("Adding a new contact for the client.");
                        case 7 -> System.out.println("Deleting a contact for the client.");
                        case 8 -> System.out.println("Client chose to exit.");
                        default -> System.out.println("Invalid choice received from client.");
                    }
                }
            } catch (Exception e) {
                System.err.println("Error during client interaction: " + e.getMessage());
            }
        } catch (Exception e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }
}
