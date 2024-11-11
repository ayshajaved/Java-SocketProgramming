package SMSSocketApp;
import java.io.*;
import java.net.*;
import java.util.*;

public class Server implements Runnable  {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private Scanner scanner;
    private SMSapp app;
    static{
        System.out.println("****************************");
        System.out.println("THE SERVER INITIALIZATION!");
        System.out.println("****************************");
        System.out.println("Server is Waiting for client!");
        System.out.println("****************************\n");
    }
    Server(){
        scanner = new Scanner(System.in);
        app= new SMSapp();
    }
    public void startServer(int port){
        try{
            serverSocket = new ServerSocket(port);
            clientSocket = serverSocket.accept();
            System.out.print("Connected to the Client with PORT: "+port+"...\n");
            out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            // Start server communication in a new thread
            new Thread(this).start();
            new Thread(new MessageReceiver(in)).start();
        }
        catch(UnknownHostException e) {
            System.out.println("Unknown Host: "+e.getMessage());
        }
        catch(ConnectException e){
            System.out.println("Server is not available: "+e.getMessage());
        }
        catch(Exception e){
            System.out.println("IO Exception: "+e.getMessage());
        }
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
                String clientMessage;
                while (true) {
                    if (in.ready()) {  // Check if data is available to prevent blocking
                        clientMessage = in.readLine();
                        if(clientMessage.equalsIgnoreCase("Client chose to exit.")){
                            out.println("Client chose to exit.");
                            closeServer();                        
                            }
                        if (clientMessage != null) {
                            System.out.println("\nClient: " + clientMessage);
                            SMS sms = new SMS(clientMessage);
                            sms.receive();
                            app.getMessages().add(sms); // Add the message to the list
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
    
    public void handleCommunication() throws Exception {
        while (true) {
        // Display menu for server-side operations
        System.out.println("\nSERVER MENU");
        System.out.println("************");
        sendMenu();
        try{
            System.out.print("Enter your choice: ");
            int serverChoice = scanner.nextInt();
            scanner.nextLine(); // Clear the buffer
            handleServerRequest(serverChoice);
        }
        catch(InputMismatchException e){
            System.out.println("Input Mismatch Exception!\nTry Again..");
            scanner.nextLine(); // Clear the buffer so that while can execute again
        }
        catch(NullPointerException e){
            System.out.println("Null Pointer Exception");
        }
    }
    }

    public void sendMenu() {
        System.out.println(
            "1: SEND MESSAGE\n" +
            "2: DISPLAY MESSAGES\n" +
            "3: FIND BY ID/CONTENT\n" +
            "4: EDIT BY ID\n" +
            "5: DELETE BY ID/CONTENT\n" +
            "6: SORT-BY-ID\n" +
            "7: SORT-BY-TIME\n" +
            "8: SORT-BY-CONTENT\n" +
            "9: EXIT\n");
}
    private void handleServerRequest(int choice) throws Exception {
        MenuOption option = MenuOption.getValueOf(choice);
        
        switch (option) {
            case SEND -> sendMessage();
            case DISPLAY -> app.displayMessages();
            case FIND -> app.findMessage();
            case EDIT ->app.editMessage();
            case DELETE -> app.deleteMessage();
            case SORT_BY_ID -> app.sortById();
            case SORT_BY_TIME -> app.sortByTime();
            case SORT_BY_CONTENT -> app.sortByMessageContent();
            case EXIT -> {
                out.println("Server chose to exit.");
                closeServer();
            }
            default -> System.out.println("Invalid choice. Please try again.");
        }
    }
    public void sendMessage() {
        System.out.println("Enter the language of the message (English/Roman): ");
        String languageChoice = scanner.nextLine();
        System.out.println("Enter the message to send:");
        String messageContent = scanner.nextLine();
        SMS sms;
        switch (languageChoice.toLowerCase().trim()) { // Trim to remove spaces
            case "english":
                sms = new EnglishSMS(messageContent);
                break;
            case "roman":
                sms = new RomanSMS(messageContent);
                break;
            default:
                System.out.println("Invalid language choice, defaulting to English.");
                sms = new EnglishSMS(messageContent);
                break;
        }
        app.getMessages().add(sms);
        out.println(sms.getMessageContent());
        System.out.println("Message sent successfully!");
    }
    public void closeServer() throws Exception {
        System.out.println("Closing server...");
        in.close();
        out.close();
        clientSocket.close();
        serverSocket.close();
        System.out.println("Server closed successfully.");
        System.exit(0);
    }

    public static void main(String[] args) {
        Server server = new Server();
        try {
            server.startServer(1234);
        } 
        catch(InputMismatchException e){
            System.out.println("Input Exception: "+e.getMessage());
        }
        catch(NullPointerException e){
            System.out.println("Null Pointer Exception: "+e.getMessage());
        }
        catch (Exception e) {
            System.out.println("Server Error: " + e.getMessage());
        }
    }
}