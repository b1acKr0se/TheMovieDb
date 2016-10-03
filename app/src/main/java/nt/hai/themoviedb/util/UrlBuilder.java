package nt.hai.themoviedb.util;


import java.util.Locale;

public class UrlBuilder {
    private static final String BASE_POSTER_URL = "http://image.tmdb.org/t/p/w%d%s";


    public static String getPosterUrl(String path) {
        return String.format(Locale.getDefault(), BASE_POSTER_URL, 342, path);
    }

    public static String getBackdropUrl(String path) {
        return String.format(Locale.getDefault(), BASE_POSTER_URL, 780, path);
    }

    public static String getCastUrl(String path) {
        return String.format(Locale.getDefault(), BASE_POSTER_URL, 342, path);
    }
}
