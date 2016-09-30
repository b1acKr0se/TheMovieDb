package nt.hai.themoviedb.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtil {
    public static String format(String input) {
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        DateFormat outputFormat = new SimpleDateFormat("MMM d, yyyy", Locale.getDefault());
        try {
            Date inputDate = inputFormat.parse(input);
            return outputFormat.format(inputDate);
        } catch (ParseException e) {
            return input;
        }
    }
}
