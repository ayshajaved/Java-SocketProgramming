package SMSSocketApp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Server {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private List<String> messages;
    private List<Contact> contacts;
    private Scanner scanner;

    public Server() {
        messages = new ArrayList<>();
        contacts = new ArrayList<>();
        scanner = new Scanner(System.in);
        addContacts();
        addMessages();
    }
    public void addContacts() {
        contacts.add(new Contact("Ali", "ali@gmail.com", new Address("Lahore","Pakistan")));
        contacts.add(new Contact("Hammad", "Hammad@gmail.com", new Address("Lahore", "Pakistan")));
        contacts.add(new Contact("Hassan", "hassan@gmail.com", new Address("Islamabad", "Pakistan")));
        contacts.add(new Contact("Tom", "tom@gmail.com", new Address("New York", "USA")));
        contacts.add(new Contact("Jerry", "jerry@gmail.com", new Address("Paris", "France")));
        contacts.add(new Contact("Ayesha", "ayesha@gmail.com", new Address("Faislabad", "Pakistan")));
    }
    public void addMessages() {
        messages.add("Hello");
        messages.add("Hi");
        messages.add("How are you?");
        messages.add("Goodbye");
        messages.add("Bye");
        messages.add("See you later");
    }
    public void startServer(int port) throws Exception {
        serverSocket = new ServerSocket(port);
        System.out.println("**************");
        System.out.println("SERVER STARTED");
        System.out.println("**************\n");

        clientSocket = serverSocket.accept();
        System.out.println("Connected to the Client...\n");

        out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        // Start server communication in a new thread
        new Thread(() -> {
            try {
                handleCommunication();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void handleCommunication() throws Exception {
        while (true) {
            // Receive a message from the client
            if (in.ready()) {
                String clientMessage = in.readLine();
                if (clientMessage != null) {
                    int clientChoice = Integer.parseInt(clientMessage);
                    handleClientRequest(clientChoice);
                }
            }

            // Display menu for server-side operations
            System.out.println("\nServer Menu:");
            sendMenu();
            int serverChoice = scanner.nextInt();
            scanner.nextLine(); // Clear the buffer
            handleServerRequest(serverChoice);
            if (serverChoice == 8) {
                closeServer();
                break;
            }
        }
    }

    public void sendMenu() {
        System.out.println(
                "1: Send Message\n" +
                "2: Display Messages\n" +
                "3: Find Message\n" +
                "4: Delete Message\n" +
                "5: Display Contacts\n" +
                "6: Add Contact\n" +
                "7: Delete Contact\n" +
                "8: Exit\n");
    }

    private void handleClientRequest(int choice) throws Exception {
        System.out.println("Client chose option: " + choice);

        switch (choice) {
            case 1 -> sendMessage();
            case 2 -> displayMessages();
            case 3 -> findMessage();
            case 4 -> deleteMessage();
            case 5 -> displayContacts();
            case 6 -> addContact();
            case 7 -> deleteContact();
            case 8 -> closeServer();
            default -> out.println("Invalid choice from client.");
        }
    }

    private void handleServerRequest(int choice) throws Exception {
        switch (choice) {
            case 1 -> sendMessage();
            case 2 -> displayMessages();
            case 3 -> findMessage();
            case 4 -> deleteMessage();
            case 5 -> displayContacts();
            case 6 -> addContact();
            case 7 -> deleteContact();
            case 8 -> {
                out.println("Server chose to exit.");
                closeServer();
            }
            default -> System.out.println("Invalid choice. Please try again.");
        }
    }
    private void sendMessage() throws Exception {
        System.out.println("Enter message to send:");
        String message = scanner.nextLine();
        if (!message.isEmpty()) {
            messages.add(message);
            out.println("Message from Server: " + message);
            System.out.println("Message sent to client successfully!");
        }
    }

    private void displayMessages() {
        if (messages.isEmpty()) {
            System.out.println("No messages to display.");
        } else {
            System.out.println("Messages:");
            for (String msg : messages) {
                System.out.println("- " + msg);
            }
        }
    }

    private void findMessage() throws Exception {
        System.out.println("Enter the message content to search:");
        String search = scanner.nextLine();
        boolean found = false;
        for (String msg : messages) {
            if (msg.contains(search)) {
                System.out.println("Found: " + msg);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No messages found containing: " + search);
        }
    }

    private void deleteMessage() throws Exception {
        System.out.println("Enter the message content to delete:");
        String content = scanner.nextLine();
        boolean removed = messages.removeIf(msg -> msg.contains(content));
        if (removed) {
            System.out.println("Message deleted successfully.");
        } else {
            System.out.println("No message found containing: " + content);
        }
    }

    private void displayContacts() {
        if (contacts.isEmpty()) {
            System.out.println("No contacts to display.");
        } else {
            System.out.println("Contacts:");
            for (Contact contact : contacts) {
                System.out.println(contact);
            }
        }
    }

    private void addContact() throws Exception {
        System.out.println("Enter the contact name to add:");
        String contactName = scanner.nextLine();
        System.out.println("Enter the contact email to add:");
        String contactEmail = scanner.nextLine();
        System.out.println("Enter the contact city to add:");
        String city = scanner.nextLine();
        System.out.println("Enter the contact country to add:");
        String country = scanner.nextLine();
        if (!contacts.isEmpty()) {
            contacts.add(new Contact(contactName, contactEmail, new Address(city, country)));
            System.out.println("Contact added successfully!");
        }
    }

    private void deleteContact() throws Exception {
        System.out.println("Enter the contact name to delete:");
        String contactName = scanner.nextLine();
        boolean removed = contacts.removeIf(contact -> contact.getName().equals(contactName));
        if (removed) {
            System.out.println("Contact deleted successfully.");
        } else {
            System.out.println("No contact found with name: " + contactName);
        }
    }

    public void closeServer() throws Exception {
        System.out.println("Closing server...");
        in.close();
        out.close();
        clientSocket.close();
        serverSocket.close();
        System.out.println("Server closed successfully.");
    }

    public static void main(String[] args) {
        Server server = new Server();
        try {
            server.startServer(1234);
        } catch (Exception e) {
            System.out.println("Server Error: " + e.getMessage());
        }
    }
}