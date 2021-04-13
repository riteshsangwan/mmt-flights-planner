package com.mmt.flights.planner.util;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public final class TimeUtil {

    /**
     * We don't want anyone to instantiate this class
     */
    private TimeUtil() {
    }

    /**
     * Add the specified number of mins to the given time and return the new time as 24 hour format
     *
     * @param time24Hour the given time
     * @param minsToAdd  the  number of mins to add
     * @return the new time
     */
    public static String addMins24HourFormat(String time24Hour, int minsToAdd) {
        LocalTime first = LocalTime.parse(time24Hour, DateTimeFormatter.ofPattern("HHmm"));
        LocalTime added = first.plusMinutes(minsToAdd);
        return added.format(DateTimeFormatter.ofPattern("HHmm"));
    }

    /**
     * Pad with leading zeros if needed. for ex: 1 am is represented as 0100
     *
     * @param time the input time
     * @return the padded time (the length of this return string is guaranteed to be 4)
     */
    public static String pad24HourTimeFormat(String time) {
        int padding = 4 - time.length();
        return "0".repeat(padding) + time;
    }

    /**
     * Calculate the difference in mins between a and b
     * time will always wrap around midnight
     * difference between 2350 and 0120 would be 90 mins
     *
     * @param a the first time
     * @param b the second time
     * @return the difference in mins
     */
    public static int differenceInMins(String a, String b) {
        LocalTime first = LocalTime.parse(a, DateTimeFormatter.ofPattern("HHmm"));
        LocalTime second = LocalTime.parse(b, DateTimeFormatter.ofPattern("HHmm"));
        return differenceInMins(first, second);
    }

    /**
     * Difference in mins between two local time instances
     * If second is less than first we wrap around midnight
     *
     * @param first  the first instant
     * @param second the second instant
     * @return differen in mins
     */
    public static int differenceInMins(LocalTime first, LocalTime second) {
        long diff = Duration.between(first, second).toMinutes();
        if (diff < 0) {
            return (int) (1440 + diff);
        }

        return (int) diff;
    }
}
