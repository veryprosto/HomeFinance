package ru.veryprosto.homefinance.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {

    private static SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");

    public static String dateToString(Date date) {
        return format.format(date);
    }

    public static Date stringToDate(String stringDate) {
        try {
            return format.parse(stringDate);
        } catch (ParseException e) {
            throw new RuntimeException(e); //todo обработать!
        }
    }
}
