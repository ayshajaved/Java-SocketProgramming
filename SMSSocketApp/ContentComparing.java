package SMSSocketApp;
import java.util.Comparator;
public class ContentComparing implements Comparator<SMS> {
    @Override
    public int compare(SMS o1, SMS o2) {
        return o1.getMessageContent().compareToIgnoreCase(o2.getMessageContent());//comparing content ignoring case
    }
   
}
