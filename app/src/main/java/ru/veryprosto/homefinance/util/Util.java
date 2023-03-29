package ru.veryprosto.homefinance.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {

    public static String dateToString(Date date) {
        SimpleDateFormat formater = new SimpleDateFormat("dd.MM.yyyy");
        return formater.format(date);
    }

    public static Date StringToDate(String stringDate, String format) {
        SimpleDateFormat formater = new SimpleDateFormat(format);
        try {
            return formater.parse(stringDate);
        } catch (ParseException e) {
            throw new RuntimeException(e); //todo
        }
    }
}
