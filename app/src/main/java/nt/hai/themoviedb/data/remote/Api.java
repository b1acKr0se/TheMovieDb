package nt.hai.themoviedb.data.remote;

import nt.hai.themoviedb.data.model.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface Api {
    @GET("movie/popular")
    Observable<Response> getPopularMovies(@Query("api_key") String apiKey);

    @GET("movie/now_playing")
    Observable<Response> getNowPlayingMovies(@Query("api_key") String apiKey);

    @GET("movie/{movie_id}/credits")
    Observable<String> getCastList(@Path("movie_id") int movieId, @Query("api_key") String apiKey);
 }
