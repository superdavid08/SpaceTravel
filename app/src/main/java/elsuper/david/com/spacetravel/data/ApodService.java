package elsuper.david.com.spacetravel.data;

import elsuper.david.com.spacetravel.model.Apod;
import elsuper.david.com.spacetravel.model.MarsRovertResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Andrés David García Gómez.
 */
public interface ApodService {

    //Estableciendo los Endpoints

    @GET("planetary/apod?api_key=J0U8OnXkzemf1OF32OotEIYYrdOfWyUsdGKnxjaj")
    Call<Apod> getTodayApod();

    @GET("planetary/apod")
    Call<Apod> getTodayApodWithQuery(@Query("api_key") String apiKey);

    @GET("mars-photos/api/v1/rovers/curiosity/photos")
    Call<MarsRovertResponse> getTodayMarsRovertWithQuery(@Query("sol") int sol, @Query("api_key") String apiKey);
}
