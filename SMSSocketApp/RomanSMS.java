package SMSSocketApp;

public class RomanSMS extends SMS{
    RomanSMS(String sms){
        super(sms);
        String content = this.getMessageContent();
        content = getMessageContent()+" (Roman SMS)";
        setMessageContent(content);
    }
}
