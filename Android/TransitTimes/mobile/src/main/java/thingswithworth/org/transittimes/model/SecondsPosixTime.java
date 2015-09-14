package thingswithworth.org.transittimes.model;

import android.util.Log;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by andrey on 9/3/15.
 *
 * Time of day as measured by seconds epoch.
 */

public class SecondsPosixTime {
    private long seconds;

    public SecondsPosixTime(long seconds) {
        this.seconds = seconds;
    }

    public long getEpochSeconds() {
        return seconds;
    }

    public void setEpochSeconds(long epochSeconds) {
        this.seconds = epochSeconds;
    }

    public SecondsTime getTimeOfDay() {
        //not done in constructor cause retrofit is silly
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTimeInMillis(1000*seconds);
        int dayseconds = cal.get(Calendar.SECOND)+cal.get(Calendar.MINUTE)*60+cal.get(Calendar.HOUR_OF_DAY)*3600;
        return new SecondsTime(dayseconds);
    }
}
