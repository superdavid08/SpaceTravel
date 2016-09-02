package elsuper.david.com.spacetravel.data;

import elsuper.david.com.spacetravel.BuildConfig;
import elsuper.david.com.spacetravel.model.Apod;
import elsuper.david.com.spacetravel.model.MarsRoverResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Andrés David García Gómez.
 */
public interface ApodService {
    /********************************/
    /*Estableciendo los endpoints   */
    /********************************/
    @GET("planetary/apod?api_key=" + BuildConfig.NASA_API_KEY)
    Call<Apod> getTodayApod();

    @GET("planetary/apod")
    Call<Apod> getTodayApodWithQuery(@Query("api_key") String apiKey);

    @GET("planetary/apod")
    Call<Apod> getTodayApodWithAllQuery(@Query("date") String date, @Query("api_key") String apiKey);

    @GET("mars-photos/api/v1/rovers/curiosity/photos")
    Call<MarsRoverResponse> getTodayMarsRovertWithQuery(@Query("sol") int sol, @Query("api_key") String apiKey);

    @GET("mars-photos/api/v1/rovers/curiosity/photos")
    Call<MarsRoverResponse> getTodayMarsRovertWithAllQuery(@Query("sol") int sol,
                                                           @Query("page") int page, @Query("api_key") String apiKey);
}
