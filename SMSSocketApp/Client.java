package SMSSocketApp;
import java.io.*;
import java.net.*;
import java.util.*;

public class Client implements Runnable{
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private List<SMS> messages;
    private Scanner scanner;

    public Client() {
        scanner = new Scanner(System.in);
        messages = new ArrayList<>();
        addMessages();
    }
    public void addMessages() {
        messages.add(new SMS("Hy!"));
        messages.add( new SMS("OK! Let's go!"));
        messages.add(new SMS("I am fine"));
        messages.add(new SMS("Ok you too.."));
        messages.add(new SMS("Take care!"));
        messages.add(new SMS("Let's meet up"));
    }
    public void startClient(String ip, int port) throws Exception {
        socket = new Socket(ip, port);
        System.out.println("**************");
        System.out.println("CLIENT STARTED");
        System.out.println("**************\n");

        out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        // Start client communication in a new thread
        new Thread(this).start();
    //seperate thread for receiving messages
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
                                closeClient();
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
    
    public void handleCommunication() throws InputMismatchException,Exception {
        while (true) {
             // Display menu for client-side operations
             System.out.println("\nClient Menu:");
             sendMenu();
             int clientChoice = scanner.nextInt();
             scanner.nextLine(); // Clear the buffer
             handleClientRequest(clientChoice);
             if (clientChoice == 8) {
                 closeClient();
                 break;
             }
            // Receive a message from the server
            if (in.ready()) {
                String serverMessage = in.readLine();
                if (serverMessage != null) {
                    System.out.println("Server: " + serverMessage);
                    messages.add(new SMS(serverMessage));
                    if (serverMessage.contains("exit")) {
                        closeClient();
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

    private void handleClientRequest(int choice) throws InputMismatchException, Exception {
        switch (choice) {
            case 1 -> sendMessage();
            case 2 -> displayMessages();
            case 3 -> findMessage();
            case 4 -> deleteMessage();
            case 5 -> sortById();
            case 6 -> sortBySentTime();
            case 7 -> sortByMessageContent();
            case 8 -> {
                out.println("Client chose to exit.");
                closeClient();
            }
            default -> System.out.println("Invalid choice. Please try again.");
        }
    }
    private void sendMessage() throws InputMismatchException, Exception {
        System.out.println("Enter the message to send:");
        String message = scanner.nextLine();
        if (message != null && !message.isEmpty()) {
            messages.add(new SMS(message));
            out.println(message);
            System.out.println("Message sent to Server successfully!");
        } else {
            System.out.println("Message cannot be empty.");
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
        boolean removed = messages.removeIf(msg -> msg.getMessageContent().contains(content));
        if (removed) {
            System.out.println("Message deleted successfully.");
        } else {
            System.out.println("No message found containing: " + content);
        }
    }
    public void closeClient() throws Exception {
        System.out.println("Closing client...");
        in.close();
        out.close();
        socket.close();
        System.out.println("Client closed successfully.");
    }
    public static void main(String[] args) {
        Client client = new Client();
        try {
            client.startClient("localhost", 1234);//change localhost to the IP of the server.
        }
        catch (InputMismatchException e) {
            System.out.println("Input Error: " + e.getMessage());
        }
        catch (Exception e) {
            System.out.println("Client Error: " + e.getMessage());
        }
    }
}
