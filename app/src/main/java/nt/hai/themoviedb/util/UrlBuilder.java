package nt.hai.themoviedb.util;


public class UrlBuilder {
    private static final String BASE_POSTER_URL = "http://image.tmdb.org/t/p/w342%s";
    private static final String BASE_BACKDROP_URL = "http://image.tmdb.org/t/p/w780%s";

    public static String getPosterUrl(String path) {
        return String.format(BASE_POSTER_URL, path);
    }

    public static String getBackdropUrl(String path) {
        return String.format(BASE_BACKDROP_URL, path);
    }
}
