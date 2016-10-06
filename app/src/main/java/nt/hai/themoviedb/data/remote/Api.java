package nt.hai.themoviedb.data.remote;

import nt.hai.themoviedb.data.model.DetailResponse;
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
    Observable<DetailResponse> getCastList(@Path("movie_id") int movieId, @Query("api_key") String apiKey);

    @GET("movie/{movie_id}/images")
    Observable<DetailResponse> getImages(@Query("api_key") String apiKey);

    @GET("movie/{movie_id}/videos")
    Observable<DetailResponse> getVideos(@Query("api_key") String apiKey);

    @GET("search/multi")
    Observable<Response> search(@Query("api_key") String apiKey, @Query("query") String query);
}
