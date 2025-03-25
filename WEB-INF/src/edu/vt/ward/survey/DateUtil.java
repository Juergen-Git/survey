package edu.vt.ward.survey;

import java.util.*;
import java.text.*;

/*
  A collection of date related utility functions.
*/

/** Date-related utility methods.
 * @version $Id: DateUtil.java,v 1.1.1.1 2002/12/16 15:41:27 jrode Exp $
 */
public class DateUtil {

    /** Use this with SimpleDateFormat for ISO-4601 formatted dates.
     */
    public static String ISO4601DateFormat = "yyyy-MM-dd HH:mm:ss";

    /** alternate DateFormatSymbols object for lowercase am/pm strings
     */
    public static DateFormatSymbols lowerCaseAmPm = new DateFormatSymbols();

    /** use this Calendar object inside our methods, so we only have to create one
     */
    private static Calendar privateCalendar = Calendar.getInstance();


    static
    {
        lowerCaseAmPm.setAmPmStrings ( new String[] { "am", "pm" } );
    }

    /** parse a Date string
     * @param dateString a Date
     * @param format the format String
     * @return the formatted Date object
     */
    public static Date parseDate ( String dateString, String format )
    {
        SimpleDateFormat formatter = new SimpleDateFormat ( format );
        Date date = formatter.parse ( dateString, new ParsePosition ( 0 ) );
        return date;
    }

    /** Format a Date object into a String
     * @param format the Date format String
     * @param date the Date object
     * @return a formatted Date String
     */
    public static String formatDate ( String format, Date date ) {
        return new SimpleDateFormat ( format ).format ( date );
    }

    /** Format a Date object into a String
     * @param format the Date format String
     * @param date the Date object
     * @param symbols DateFormatSymbols to apply to date
     * @return a formatted Date String
     */
    public static String formatDate ( String format, Date date, DateFormatSymbols symbols ) {
        return new SimpleDateFormat ( format, symbols ).format ( date );
    }

    /** Get the beginning of the present month.
     * @return a Date object
     */
    public static Date getBeginningOfMonth() {
        return getBeginningOfMonth ( new Date() );
    }

    /** Get the beginning of the month that includes date.
     * @param date the Date object to inspect
     * @return a Date object
     */
    public static Date getBeginningOfMonth ( Date date ) {
        privateCalendar.setTime ( date );
        privateCalendar.set ( Calendar.DAY_OF_MONTH, 1 );
        privateCalendar.set ( Calendar.HOUR_OF_DAY, 0 );
        privateCalendar.set ( Calendar.MINUTE, 0 );
        privateCalendar.set ( Calendar.SECOND, 0 );
        privateCalendar.set ( Calendar.MILLISECOND, 0 );
        return privateCalendar.getTime();
    }


    /** Get the end of the present month.
     * @return a Date object
     */
    public static Date getEndOfMonth() {
        return getEndOfMonth ( new Date() );
    }


    /** Get the end of the month that includes date.
     * @param date the Date object to inspect
     * @return a Date object
     */
    public static Date getEndOfMonth ( Date date ) {
        privateCalendar.setTime ( date );
        privateCalendar.set ( Calendar.DAY_OF_MONTH, privateCalendar.getActualMaximum ( Calendar.DAY_OF_MONTH ) );
        privateCalendar.set ( Calendar.HOUR_OF_DAY, 23 );
        privateCalendar.set ( Calendar.MINUTE, 59 );
        privateCalendar.set ( Calendar.SECOND, 59 );
        privateCalendar.set ( Calendar.MILLISECOND, 999 );
        return privateCalendar.getTime();
    }

    /** Get the beginning of the present week.
     * @return a Date object
     */
    public static Date getBeginningOfWeek() {
        return getBeginningOfWeek ( new Date() );
    }

    /** Get the beginning of the week that include date.
     * @param date the Date object to inspect
     * @return a Date object
     */
    public static Date getBeginningOfWeek ( Date date ) {
        privateCalendar.setTime ( date );
        privateCalendar.add ( Calendar.DAY_OF_MONTH, -1 * privateCalendar.get ( Calendar.DAY_OF_WEEK ) + 1 );
        privateCalendar.set ( Calendar.HOUR_OF_DAY, 0 );
        privateCalendar.set ( Calendar.MINUTE, 0 );
        privateCalendar.set ( Calendar.SECOND, 0 );
        privateCalendar.set ( Calendar.MILLISECOND, 0 );
        return privateCalendar.getTime();
    }

    /** Get the end of the present week.
     * @return a Date object
     */
    public static Date getEndOfWeek() {
        return getEndOfWeek ( new Date() );
    }

    /** Get the end of the week that includes date.
     * @param date the Date object to inspect
     * @return a Date object
     */
    public static Date getEndOfWeek ( Date date ) {
        privateCalendar.setTime ( date );
        privateCalendar.add ( Calendar.DAY_OF_MONTH, -1 * privateCalendar.get ( Calendar.DAY_OF_WEEK ) + 7 );
        privateCalendar.set ( Calendar.HOUR_OF_DAY, 23 );
        privateCalendar.set ( Calendar.MINUTE, 59 );
        privateCalendar.set ( Calendar.SECOND, 59 );
        privateCalendar.set ( Calendar.MILLISECOND, 999 );
        return privateCalendar.getTime();
    }

    /** Get the beginning of the week of the beginning of the month that
     includes date.
     * @param date the Date object to inspect
     * @return a Date object
     */
    public static Date getBeginningOfWeekOfMonth ( Date date ) {
        return getBeginningOfWeek ( getBeginningOfMonth ( date ) );
    }

    /** Get the beginning of the week of the beginning of the month that
     includes the current date.
     * @return a Date object
     */
    public static Date getBeginningOfWeekOfMonth() {
        return getBeginningOfWeek ( getBeginningOfMonth ( new Date() ) );
    }

    /** Get the end of the week of the end of the month that includes date.
     * @param date the Date object to inspect
     * @return a Date object
     */
    public static Date getEndOfWeekOfMonth ( Date date ) {
        return getEndOfWeek ( getEndOfMonth ( date ) );
    }

    /** Get the end of the week of the end of the month that
     includes the current date.
     * @return a Date object
     */
    public static Date getEndOfWeekOfMonth() {
        return getEndOfWeek ( getEndOfMonth ( new Date() ) );
    }

    /** Get the beginning of the present year.
     * @return a Date object
     */
    public static Date getBeginningOfYear() {
        return getBeginningOfYear ( new Date() );
    }

    /** Get the beginning of the year that includes date.
     * @param date the Date object to inspect
     * @return a Date object
     */
    public static Date getBeginningOfYear ( Date date ) {
        privateCalendar.setTime ( date );
        // months are 0-based!    0==January
        privateCalendar.set ( Calendar.MONTH, 0 );
        privateCalendar.set ( Calendar.DAY_OF_MONTH, 1 );
        privateCalendar.set ( Calendar.HOUR_OF_DAY, 0 );
        privateCalendar.set ( Calendar.MINUTE, 0 );
        privateCalendar.set ( Calendar.SECOND, 0 );
        privateCalendar.set ( Calendar.MILLISECOND, 0 );
        return privateCalendar.getTime();
    }

    /** Get the end of the present year.
     * @return a Date object
     */
    public static Date getEndOfYear() {
        return getEndOfYear ( new Date() );
    }

    /** Get the end of the year that includes date.
     * @param date the Date object to inspect
     * @return a Date object
     */
    public static Date getEndOfYear ( Date date ) {
        privateCalendar.setTime ( date );
        // months are 0-based!  11==December
        privateCalendar.set ( Calendar.MONTH, 11 );
        privateCalendar.set ( Calendar.DAY_OF_MONTH, privateCalendar.getActualMaximum ( Calendar.DAY_OF_MONTH ) );
        privateCalendar.set ( Calendar.HOUR_OF_DAY, 23 );
        privateCalendar.set ( Calendar.MINUTE, 59 );
        privateCalendar.set ( Calendar.SECOND, 59 );
        privateCalendar.set ( Calendar.MILLISECOND, 999 );
        return privateCalendar.getTime();
    }


    /** Get the end of the year that includes date.
     * @return a Date object
     */
    public static Date getBeginningOfDay() {
        return getBeginningOfDay ( new Date() );
    }

    /** Get the beginning of the day that includes date.
     * @param date the Date object to inspect
     * @return a Date object
     */
    public static Date getBeginningOfDay ( Date date ) {
        privateCalendar.setTime ( date );
        privateCalendar.set ( Calendar.HOUR_OF_DAY, 0 );
        privateCalendar.set ( Calendar.MINUTE, 0 );
        privateCalendar.set ( Calendar.SECOND, 0 );
        privateCalendar.set ( Calendar.MILLISECOND, 0 );
        return privateCalendar.getTime();
    }


    /** Get the end of the present day.
     * @return a Date object
     */
    public static Date getEndOfDay() {
        return getEndOfDay ( new Date() );
    }

    /** Get the end of the day that includes date.
     * @param date the Date object to inspect
     * @return a Date object
     */
    public static Date getEndOfDay ( Date date ) {
        privateCalendar.setTime ( date );
        privateCalendar.set ( Calendar.HOUR_OF_DAY, 23 );
        privateCalendar.set ( Calendar.MINUTE, 59 );
        privateCalendar.set ( Calendar.SECOND, 59 );
        privateCalendar.set ( Calendar.MILLISECOND, 999 );
        return privateCalendar.getTime();
    }


    /** Return a date field for the present date.
     * @param field the field number to get
     * @return the Calendar field
     */
    public static int getDateField ( int field ) {
        return Calendar.getInstance().get ( field );
    }

    /** Return a date field for the supplied date.
     * @param date the Date object to get the field from
     * @param field the field number to get
     * @return the Calendar field
     */
    public static int getDateField ( Date date, int field ) {
        privateCalendar.setTime ( date );
        return privateCalendar.get ( field );
    }
}