package ru.veryprosto.homefinance.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateRange {
    private Date start;
    private Date end;

    public DateRange() {
    }

    public DateRange(Date start, Date end) {
        this.start = start;
        this.end = end;
    }

    public Date getStart() {
        return start == null ? new GregorianCalendar(1901, 0, 1).getTime() : start;
    }

    public Date getStartIncudeStartDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getStart());
        calendar.add(Calendar.DATE, -1);
        return calendar.getTime();
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end == null ? new GregorianCalendar(5000, 0, 1).getTime() : end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public String getStringStart() {
        return Util.dateToString(getStart());
    }

    public String getStringEnd() {
        return Util.dateToString(getEnd());
    }
}
