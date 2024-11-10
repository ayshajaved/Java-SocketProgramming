package SMSSocketApp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SMS {
    private String messageContent;
    private String messageId;
    private LocalDateTime sentTime;
    private boolean isReceived = false; //if the message was received
    private boolean isEdited = false; //if the message is original(not edited)
    private static int counter = 0;

    SMS(String content){
        this.messageId = String.format("%04d", ++counter);
        this.messageContent = content;
        this.sentTime = LocalDateTime.now().plusSeconds(counter);
    }

    public void editMessageContent(String messageContent) {
        isEdited = true; //Set message status to edited
        this.messageContent = messageContent;
        this.sentTime = LocalDateTime.now().plusSeconds(counter);
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getMessageContent() {
        return messageContent;
    }
    public String getMessageId() {
        return messageId;
    }
    public LocalDateTime getSentTime() {
        return sentTime;
    }

    public void setSentTime(LocalDateTime sentTime) {
        this.sentTime = sentTime;
    }

    // Formats time for display purposes
    private String formatTime(LocalDateTime time) {
        if (time == null) {
            return "Not Seen";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return time.format(formatter);
    }
    // Marks the SMS as received and updates the sentTime to received time
    public void receive() {
        this.isReceived = true;
    }

    // Displays the SMS details, showing "ReceiveTime" if marked as received
    public String display() {
        String timeLabel = isReceived ? "ReceiveTime" : "SentTime";
        String smsStatus = isEdited ? "Edited": "Original";
        return 
                smsStatus+"\n----------"+
                "\nSMS: " + getMessageId() +
               "\n"+getMessageContent() +
               "\n" + timeLabel + ": " + formatTime(getSentTime()) +
               "\n==============================================\n";
    }
}
