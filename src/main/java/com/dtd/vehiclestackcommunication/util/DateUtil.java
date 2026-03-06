package com.dtd.vehiclestackcommunication.util;

import com.dtd.vehiclestackcommunication.exception.BadDataException;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;

public class DateUtil {

    private DateUtil() {
        throw new IllegalStateException();
    }

    public static LocalDate convertStringToDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        //convert String to LocalDate
        return LocalDate.parse(date, formatter);
    }

    public static String getCurrentTimeStamp() {
        SimpleDateFormat convert = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        try {
            Date date = convert.parse(convert.format(new Date()));
            return String.valueOf(new Timestamp(date.getTime()));
        } catch (ParseException e) {
            throw new BadDataException(e.getMessage());
        }
    }

    public static String getTime(Timestamp inputTimestamp) {
        Date date = new Date(inputTimestamp.getTime());
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss 'GMT'Z (z)", java.util.Locale.ENGLISH);
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata")); // Set the desired time zone
        return sdf.format(date);
    }

    public static String getDayAndDate(Timestamp inputTimestamp) {
        Date date = new Date(inputTimestamp.getTime());
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd yyyy");
        return sdf.format(date);
    }

    public static String getCurrentTimeInString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        Date currentDate = new Date();
        return sdf.format(currentDate);
    }

}
