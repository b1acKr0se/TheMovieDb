package nt.hai.themoviedb.util;


public class UrlBuilder {
    private static final String BASE_POSTER_URL = "http://image.tmdb.org/t/p/w342%s";

    public static String getPosterUrl(String path) {
        return String.format(BASE_POSTER_URL, path);
    }
}
