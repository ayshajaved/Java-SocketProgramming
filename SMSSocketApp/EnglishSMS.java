package SMSSocketApp;

public class EnglishSMS extends SMS{
    EnglishSMS(String sms){
        super(sms);
        String content = this.getMessageContent();
        content = getMessageContent()+" (English SMS)";
        setMessageContent(content);
    }
}

