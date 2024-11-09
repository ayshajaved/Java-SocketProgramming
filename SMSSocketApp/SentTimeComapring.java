package SMSSocketApp;
import java.util.Comparator;

public class SentTimeComapring implements Comparator<SMS>{
    @Override
    public int compare(SMS o1, SMS o2) {
        return o2.getSentTime().compareTo(o1.getSentTime());
    }
}
