package thingswithworth.org.transittimes.model;

/**
 * Created by andrey on 9/3/15.
 *
 * Time of day as measured by seconds since start of day.
 * Same model as in multigtfs.
 */

public class SecondsTime {
    private int seconds;

    public SecondsTime(int seconds) {
        this.seconds = seconds;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public String toString(){
        int hours = seconds/3600;
        int minutes = (seconds%3600)/60;
        int sec = seconds % 60;
        return String.format("%d:%02d:%02d",hours,minutes,sec);
    }

    public String toString(boolean includeSeconds,boolean verbose){
        int hours = seconds/3600;
        int minutes = (seconds%3600)/60;
        int sec = seconds % 60;
        String hoursStr = hours>1 && hours!=0?"hours":"hour";
        String minutesStr = minutes>1 && minutes!=0?"minutes":"minute";
        String secondsStr = sec>1 && sec!=0?"seconds":"second";

        if(verbose){
                if(includeSeconds)
                    return String.format("%d %s, %d %s, and %d %s ",hours,minutes,sec,
                                                                    hoursStr,minutesStr,secondsStr);
                else
                    return String.format("%d %s and %d %s",hours,minutes,
                                                            hoursStr,minutesStr);
        }

        if(includeSeconds)
            return String.format("%d:%02d:%02d",hours,minutes,sec);
        else
            return String.format("%d:%02d",hours,minutes);
    }

    public SecondsTime difference(SecondsTime other){
        return new SecondsTime(other.seconds-seconds);
    }
}
