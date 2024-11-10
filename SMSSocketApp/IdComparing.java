package SMSSocketApp;
import java.util.Comparator;
public class IdComparing implements Comparator<SMS>{
    @Override
    public int compare(SMS sms1, SMS sms2) {//id comparing by id that is ascending order
        return sms1.getMessageId().compareTo(sms2.getMessageId());
    }
}