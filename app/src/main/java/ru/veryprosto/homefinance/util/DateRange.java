package ru.veryprosto.homefinance.util;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateRange {
    private Date start;
    private Date end;

    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("dd.MM.yyyy");

    public DateRange() {
    }

    public DateRange(Date start, Date end) {
        this.start = start;
        this.end = end;
    }

    public Date getStart() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return start == null ? calendar.getTime() : start;
    }

    public Date getStartIncludeStartDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getStart());
        calendar.add(Calendar.DATE, -1);
        return calendar.getTime();
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DATE));
        return end == null ? calendar.getTime() : end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public String getStringStart() {
        return dateToString(getStart());
    }

    public String getStringEnd() {
        return dateToString(getEnd());
    }

    public static String dateToString(Date date) {
        return FORMAT.format(date);
    }

    public static Date stringToDate(String stringDate) {
        try {
            return FORMAT.parse(stringDate);
        } catch (ParseException e) {
            throw new RuntimeException(e); //todo обработать!
        }
    }

    public boolean isInDiapason(Date date) {
        return date.after(getStartIncludeStartDate()) && date.before(getEnd());
    }
}
