package SMSSocketApp;
import java.io.*;
import java.net.*;
import java.util.*;

public class Server implements Runnable  {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private List<SMS> messages;
    private Scanner scanner;

    public Server(){
        messages = new ArrayList<>();
        scanner = new Scanner(System.in);
        addMessages();
    }
    public void addMessages() {
        messages.add(new SMS("Hello!"));
        messages.add( new SMS("Let's go shopping!"));
        messages.add(new SMS("How are you?"));
        messages.add(new SMS("Goodbye!Take Care.."));
        messages.add(new SMS("Bye!Bye!"));
        messages.add(new SMS("See you later!!"));
    }
    public void startServer(int port) throws InputMismatchException, Exception {
        serverSocket = new ServerSocket(port);
        System.out.println("**************");
        System.out.println("SERVER STARTED");
        System.out.println("**************\n");

        clientSocket = serverSocket.accept();
        System.out.println("Connected to the Client...\n");

        out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        // Start server communication in a new thread
        new Thread(this).start();

        new Thread(new MessageReceiver(in)).start();

    }
    @Override
    public void run() {
        try {
            handleCommunication();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    private class MessageReceiver implements Runnable {
        private BufferedReader in;
    
        public MessageReceiver(BufferedReader in) {
            this.in = in;
        }
    
        @Override
        public void run() {
            try {
                String serverMessage;
                while (true) {
                    if (in.ready()) {  // Check if data is available to prevent blocking
                        serverMessage = in.readLine();
                        if (serverMessage != null) {
                            System.out.println("\nServer: " + serverMessage);
                            SMS sms = new SMS(serverMessage);
                            sms.receive();
                            messages.add(sms); // Add the message to the list
                            if (serverMessage.contains("exit")) {
                                closeServer();
                                break;
                            }
                        }
                    } else {
                        // Brief sleep to avoid busy-waiting if no message is ready
                        Thread.sleep(100);
                    }
                }
            } catch (Exception e) {
                System.out.println("MessageReceiver Error: " + e.getMessage());
            }
        }
    }
    
    public void handleCommunication() throws InputMismatchException, Exception {
        while (true) {
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
            // Receive a message from the client
            if (in.ready()) {     //if the server is ready to receive data from the client
                String clientMessage = in.readLine();
                if (clientMessage != null) {
                    SMS sms = new SMS(clientMessage);
                    messages.add(sms);
                    System.out.println("Client: "+clientMessage);
                    if (clientMessage.contains("exit")) {
                        closeServer();
                        break;
                    }
                }
            }
            else{
                Thread.sleep(1000);
            }
        }
    }

    public void sendMenu() {
        System.out.println(
                "1: Send Message\n" +
                "2: Display Messages\n" +
                "3: Find Message\n" +
                "4: Delete Message\n" +
                "5: Sort Messages by ID\n" +
                "6: Sort Messages by Sent Time\n" +
                "7: Sort Messages by Content\n" +
                "8: Exit\n");
    }
    public void sortById(){
        Collections.sort(messages, new IdComparing());
        displayMessages();
    }
    public void sortBySentTime(){
        Collections.sort(messages, new SentTimeComapring());
        displayMessages();
    }
    public void sortByMessageContent(){
        Collections.sort(messages, new ContentComparing());
        displayMessages();
    }

    private void handleServerRequest(int choice) throws InputMismatchException, Exception {
        switch (choice) {
            case 1 -> sendMessage();
            case 2 -> displayMessages();
            case 3 -> findMessage();
            case 4 -> deleteMessage();
            case 5 -> sortById();
            case 6 -> sortBySentTime();
            case 7 -> sortByMessageContent();
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
            messages.add(new SMS(message));
            out.println(message);
            System.out.println("Message sent to client successfully!");
        }
    }

    private void displayMessages() {
        if (messages.isEmpty()) {
            System.out.println("No messages to display.");
        } else {
            System.out.println("Messages:");
            for (SMS msg : messages) {
                System.out.println(msg.display());
            }
        }
    }

    private void findMessage() throws Exception {
        System.out.println("Enter the message content to search:");
        String search = scanner.nextLine();
        boolean found = false;
        for (SMS msg : messages) {
            if (msg.getMessageContent().contains(search)) {
                System.out.println("Found: " + msg.getMessageContent());
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
        boolean removed = messages.removeIf(msg -> msg.getMessageContent().contains(content));
        if (removed) {
            System.out.println("Message deleted successfully.");
        } else {
            System.out.println("No message found containing: " + content);
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
        } 
        catch(InputMismatchException e){
            System.out.println("Input Exception: "+e.getMessage());
        }
        catch (Exception e) {
            System.out.println("Server Error: " + e.getMessage());
        }
    }
}