package SMSSocketApp;
import java.util.*;

public class SMSapp {
    private List<SMS> messages;
    private Scanner scanner;
   
    public SMSapp(){
        scanner = new Scanner(System.in);
        messages = new ArrayList<>();   
        addMessages();
    }
    public List<SMS> getMessages() {
        return messages;
    }
    public void setMessages(List<SMS> messages) {
        this.messages = messages;
    }
    public void addMessages() {
        messages.add(new EnglishSMS("Hy!"));
        messages.add( new EnglishSMS("OK! Let's go!"));
        messages.add(new RomanSMS("Mai theek hoon"));
        messages.add(new RomanSMS("ACha theek hai.."));
        messages.add(new EnglishSMS("Take care!"));
        messages.add(new RomanSMS("Ajao milty hain.."));
    }
    public void sortById(){
        Collections.sort(messages, new IdComparing());
        displayMessages();
    }
    public void sortByTime(){
        Collections.sort(messages, new SentTimeComapring());
        displayMessages();
    }
    public void sortByMessageContent(){
        Collections.sort(messages, new ContentComparing());
        displayMessages();
    }
    public void displayMessages() {
        if (messages.isEmpty()) {
            System.out.println("No messages to display.");
        } else {
            System.out.println("Messages:");
            for (SMS msg : messages) {
                System.out.println(msg.display());
            }
        }
    }
    public void findMessage() throws Exception{
        System.out.println("""
                Enter your choice:
                1: By Id/Content
                2: By SMS Type
                """);
        int find = scanner.nextInt();
        scanner.nextLine();
        switch (find){
            case 1:
                System.out.println("Enter the message ID or content to search:");
                String search = scanner.nextLine();
                boolean found = false;
                for (SMS msg : messages) {
                    if (msg.getMessageId().equals(search) || msg.getMessageContent().toLowerCase().contains(search)) {
                        System.out.println("Found: " + msg.getMessageContent());
                        found = true;
                    }
                }
                if (!found) {
                    System.out.println("No messages found containing: " + search);
                }
                break;
            case 2:
                System.out.println("Enter the SMS type to search: (1 for English, 2 for Roman)");
                int type = scanner.nextInt();
                scanner.nextLine();
                boolean foundType = false;
                for (SMS msg : messages) {
                    //checking msg if it is the instance of english sms class
                    if (msg instanceof EnglishSMS && type == 1) {
                        System.out.println("Found: " + msg.getMessageContent());
                        foundType = true;
                    }
                    //checking msg if it is the instance of roman sms class
                    if(msg instanceof RomanSMS && type == 2){
                        System.out.println("Found: " + msg.getMessageContent()); 
                        foundType = true;
                    }
                }
                if (!foundType) {
                    System.out.println("No messages found of type: " + type);
                }
                break;
            default:
                break;
        }
    }
    public void deleteMessage() throws Exception {
        System.out.println("Enter the message content or ID to delete:");
        String search = scanner.nextLine();
        boolean removed = messages.removeIf(msg -> msg.getMessageContent().toLowerCase().contains(search)||msg.getMessageId().equals(search));
        if (removed) {
            System.out.println("Message deleted successfully.");
        } else {
            System.out.println("No message found containing: " + search);
        }
    }
    public void editMessage(){
        System.out.println("Enter the message ID to edit: ");
        String id =scanner.nextLine();
        boolean edit = false;
        for(SMS msg:messages){
            if(msg.getMessageId().equals(id)){
                System.out.println("Enter the new message content: ");
                String newContent = scanner.nextLine();
                msg.editMessageContent(newContent);
                System.out.println("Message edited successfully.");
                edit = true;
            }
        }
        if(!edit){
            System.out.println("No message found with ID: " + id);
        }
    }
}
