package com.example.rmaci.crownmovies;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static java.lang.Math.abs;

/**
 * Created by Richie on 12/20/2017.
 */

public class DateChecker {
    private Date todaysDate;

    DateChecker(){
        Calendar calendar = Calendar.getInstance();
        Calendar today = Calendar.getInstance();
        today.clear();
        today.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));
        today.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
        todaysDate = today.getTime();
    }

    public boolean inBdayRange(String bday){
        Date bdayDate = convertStringtoDate(bday);
        long days = getDateDiff(todaysDate,bdayDate,TimeUnit.DAYS);
        //Returns true if Not past 30 days from bday to Today
        //And Not negative(Before bday)
        return(!(days >30) && (days<0));
    }

    //Expect CSV to have bday and Month only, else parse out the year by splitting string. ask mike.
    private Date convertStringtoDate(String date){
        DateFormat format = new SimpleDateFormat("MM/d", Locale.ENGLISH);
        Date formattedDate = null;
        try {
            formattedDate = format.parse(date);
            return formattedDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    //Difference is Todays date - bday Date.
    //Negative means today is before Bday
    //Over 30 means expired!
    //This returns Difference in days from mm/d
    public long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
    }


}
