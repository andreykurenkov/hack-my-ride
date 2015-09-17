package thingswithworth.org.transittimes.net.service;

import android.widget.TextView;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by andrey on 9/17/15.
 */
public class RestUtil {

    public static int getSecondsOfDay(){
        GregorianCalendar cal = new GregorianCalendar();
        int currentSecond = cal.get(Calendar.SECOND);
        int currentMinute = cal.get(Calendar.MINUTE);
        int currentHour = cal.get(Calendar.HOUR_OF_DAY);
        return currentSecond + currentMinute * 60 + currentHour * 3600;
    }

    public static int getDayOfWeek(){
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DAY_OF_WEEK)-1;
    }
}
