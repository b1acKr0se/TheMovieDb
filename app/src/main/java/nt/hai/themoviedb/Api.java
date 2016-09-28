package nt.hai.themoviedb;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

interface Api {
    @GET("movie/popular")
    Observable<Response> getPopularMovies(@Query("api_key") String apiKey);
}
