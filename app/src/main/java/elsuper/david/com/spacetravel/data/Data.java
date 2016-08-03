package elsuper.david.com.spacetravel.data;

import elsuper.david.com.spacetravel.BuildConfig;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Andrés David García Gómez.
 */
public class Data {

    public static Retrofit getRetrofitInstance(){

        //Para monitorear el log
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        //Para el correcto almacenamiento de cada petición
        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();
        okHttpClient.addInterceptor(httpLoggingInterceptor);

        return new Retrofit.Builder().baseUrl(BuildConfig.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient.build())
                .build();
    }
}
