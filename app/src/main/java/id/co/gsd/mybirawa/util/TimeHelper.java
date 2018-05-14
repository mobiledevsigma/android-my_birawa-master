package id.co.gsd.mybirawa.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by aang on 28/05/2017.
 */

public class TimeHelper {

    public String getTimeNow() {

        long yourmilliseconds = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date resultdate = new Date(yourmilliseconds);

        return sdf.format(resultdate);

    }

    public long getElapsedDay(String time) {

        Date statusDate = null;
        try {
            statusDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(time);
            System.out.println(time);
            //date2 = new SimpleDateFormat("MMM d, yyyy hh:mm:ss").parse(DateFormat.getDateTimeInstance().format(new Date()));

            System.out.println(statusDate.toString());

            long different = System.currentTimeMillis() - statusDate.getTime();
            System.out.println(different);

            long secondsInMilli = 1000;
            long minutesInMilli = secondsInMilli * 60;
            long hoursInMilli = minutesInMilli * 60;
            long daysInMilli = hoursInMilli * 24;

            long elapsedDays = different / daysInMilli;
            different = different % daysInMilli;

            long elapsedHours = different / hoursInMilli;
            different = different % hoursInMilli;

            long elapsedMinutes = different / minutesInMilli;
            different = different % minutesInMilli;

            long elapsedSeconds = different / secondsInMilli;

            // if(elapsedDays!=0){
            return elapsedDays;
//            }else if(elapsedHours!=0){
//                if(elapsedHours==1){
//                    return elapsedHours+" hour ago";
//                }else{
//                    return elapsedHours+" hours ago";
//                }
//            }else if(elapsedMinutes!=0){
//                if(elapsedMinutes==1){
//                    return elapsedMinutes+" minute ago";
//                }else{
//                    return elapsedMinutes+" minutes ago";
//                }
//            }else{
//                if(elapsedSeconds==1){
//                    return elapsedSeconds+" second ago";
//                }else{
//                    return elapsedSeconds+" seconds ago";
//                }
//            }else{
//                return ;
            // }
        } catch (Exception e) {
            System.out.println(e);
            return 0;
        }

    }

    public String getElapsedTimeFormatted(String time) {

        Date statusDate = null;
        try {
            statusDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(time);
            System.out.println(time);
            //date2 = new SimpleDateFormat("MMM d, yyyy hh:mm:ss").parse(DateFormat.getDateTimeInstance().format(new Date()));

            System.out.println(statusDate.toString());

            long different = System.currentTimeMillis() - statusDate.getTime();
            System.out.println(different);

            long secondsInMilli = 1000;
            long minutesInMilli = secondsInMilli * 60;
            long hoursInMilli = minutesInMilli * 60;
            long daysInMilli = hoursInMilli * 24;

            long elapsedDays = different / daysInMilli;
            different = different % daysInMilli;

            long elapsedHours = different / hoursInMilli;
            different = different % hoursInMilli;

            long elapsedMinutes = different / minutesInMilli;
            different = different % minutesInMilli;

            long elapsedSeconds = different / secondsInMilli;

            if (elapsedDays != 0) {
                if (elapsedDays == 1) {
                    return elapsedDays + " day ago";
                } else {
                    if (elapsedDays > 10) {
                        statusDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(time);
                        String date = new SimpleDateFormat("MMM dd").format(statusDate);
                        return date;
                    } else {
                        return elapsedDays + " days ago";
                    }
                }
            } else if (elapsedHours != 0) {
                if (elapsedHours == 1) {
                    return elapsedHours + " hour ago";
                } else {
                    return elapsedHours + " hours ago";
                }
            } else if (elapsedMinutes != 0) {
                if (elapsedMinutes == 1) {
                    return elapsedMinutes + " minute ago";
                } else {
                    return elapsedMinutes + " minutes ago";
                }
            } else {
                if (elapsedSeconds == 1) {
                    return elapsedSeconds + " second ago";
                } else {
                    return elapsedSeconds + " seconds ago";
                }
            }
        } catch (Exception e) {
            System.out.println(e);
            return "";
        }

    }

}
