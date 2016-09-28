package nt.hai.themoviedb.data.remote;

import nt.hai.themoviedb.data.model.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface Api {
    @GET("movie/popular")
    Observable<Response> getPopularMovies(@Query("api_key") String apiKey);
}
