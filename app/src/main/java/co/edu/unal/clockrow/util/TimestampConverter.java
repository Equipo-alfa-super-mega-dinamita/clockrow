package co.edu.unal.clockrow.util;

import android.util.Log;

import androidx.room.TypeConverter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class TimestampConverter {

    private static final String TAG = "TimestampConverter";
    private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss'Z'", Locale.getDefault());
    static {
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    @TypeConverter
    public static Date timeToDate(String value) {
        if (value != null) {
            try {
                return df.parse(value);
            } catch (ParseException e) {
                Log.e(TAG, e.getMessage());
            }
            return null;
        } else {
            return null;
        }
    }

    @TypeConverter
    public static String dateToTime(Date value) {
        if (value != null) {
            return df.format(value);
        } else {
            return null;
        }
    }

}