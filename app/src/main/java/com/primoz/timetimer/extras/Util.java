package com.primoz.timetimer.extras;

public class Util {

    public static int getMinutes(int totalSeconds) {
        return (totalSeconds % 3600) / 60;
    }

    public static int getSeconds(int totalSeconds) {
        return totalSeconds % 60;
    }

    public static int getHours(int totalSeconds) {
        return totalSeconds / 3600;
    }

    /*Getting seconds from cTimeTrain*/
    public static int getTotalSecondsFromString(String time) {
        String[] timeArray = time.split(":");
        int minutes = Integer.valueOf(timeArray[0]);
        return Integer.valueOf(timeArray[1]) + minutes * 60;
    }
}
