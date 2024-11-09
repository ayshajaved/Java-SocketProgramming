package SMSSocketApp;
import java.util.Comparator;
public class IdComparing implements Comparator<SMS>{
    @Override
    public int compare(SMS sms1, SMS sms2) {
        return sms2.getMessageId().compareTo(sms1.getMessageId());
    }
}