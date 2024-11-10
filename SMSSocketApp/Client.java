package SMSSocketApp;
import java.io.*;
import java.net.*;
import java.util.*;

public class Client implements Runnable{
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private Scanner scanner = new Scanner(System.in);
    private SMSapp app;
    Client(){
        app = new SMSapp();
    }
    static{
        System.out.println("****************************");
        System.out.println("THE CLIENT INITIALIZATION!");
        System.out.println("****************************");
        System.out.println("Attempting to connect the server!");
        System.out.println("****************************\n");
    }
    public void Initializeclient(){
        try{
        System.out.println("Enter the Server IP to connect: ");
        String serverIP = scanner.nextLine();
        startClient(serverIP,1234);
        }
        catch(UnknownHostException e){
            System.out.println("Server IP is not valid: "+e.getMessage());
        }
        catch(ConnectException e){
            System.out.println("Server is not available: "+e.getMessage());
        }
        catch(Exception e){
            System.out.println("IO Exception: "+e.getMessage());
        }
    }
    public void startClient(String IP ,int port) throws Exception {
        socket = new Socket(IP,port);
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
                            app.getMessages().add(sms); // Add the received message to the list
                        }
                    } else {
                        // Brief sleep to avoid busy-waiting if no message is ready
                        Thread.sleep(100);
                    }
                }
            }
            catch(SocketException e){
                System.out.println("Socket Disconnected: "+e.getMessage());
            }
            catch (Exception e) {
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
    private void handleClientRequest(int choice) throws InputMismatchException, Exception {
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
                out.println("Client chose to exit.");
                closeClient();
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
        switch (languageChoice.toLowerCase().trim()) { //excluding empty space and considering lower case
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
    public void closeClient() throws Exception {
        System.out.println("Closing client...");
        in.close();
        out.close();
        socket.close();
        System.out.println("Client closed successfully.");
        System.exit(0);
    }
    public static void main(String[] args) {
        try {
            Client client = new Client();
            client.Initializeclient();
        }
        catch (InputMismatchException e) {
            System.out.println("Input Error: " + e.getMessage());
        }
        catch (Exception e) {
            System.out.println("Client Error: " + e.getMessage());
        }
    }
}
