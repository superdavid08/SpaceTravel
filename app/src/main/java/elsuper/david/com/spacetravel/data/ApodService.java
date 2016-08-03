package elsuper.david.com.spacetravel.data;

import elsuper.david.com.spacetravel.model.APOD;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Andrés David García Gómez.
 */
public interface ApodService {

    @GET("planetary/apod?api_key=J0U8OnXkzemf1OF32OotEIYYrdOfWyUsdGKnxjaj")
    Call<APOD> getTodayApod();

    @GET("planetary/apod")
    Call<APOD> getTodayApodWithQuery(@Query("api_key") String apiKey);
}
