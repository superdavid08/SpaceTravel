package elsuper.david.com.spacetravel;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import elsuper.david.com.spacetravel.data.ApodService;
import elsuper.david.com.spacetravel.data.Data;
import elsuper.david.com.spacetravel.model.APOD;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private Button btnVideo;
    private ImageView imageApod;
    private TextView tvDate;
    private TextView tvTitle;
    private TextView tvExplanation;
    private TextView tvCopyright;
    private String urlVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("BuildConfig", BuildConfig.URL);

        //Acceedemos a los controles
        btnVideo = (Button) findViewById(R.id.main_btnVideo);
        imageApod = (ImageView)findViewById(R.id.main_imageApod);
        tvDate = (TextView)findViewById(R.id.main_tvDate);
        tvTitle = (TextView)findViewById(R.id.main_tvTitle);
        tvExplanation = (TextView)findViewById(R.id.main_tvExplanation);
        tvCopyright = (TextView)findViewById(R.id.main_tvCopyright);

        //Utilizando Retrofit
        ApodService apodService = Data.getRetrofitInstance().create(ApodService.class);

        //Call<APOD> callApodService = apodService.getTodayApod();
        Call<APOD> callApodService = apodService.getTodayApodWithQuery("J0U8OnXkzemf1OF32OotEIYYrdOfWyUsdGKnxjaj");

        callApodService.enqueue(new Callback<APOD>() {
            @Override
            public void onResponse(Call<APOD> call, Response<APOD> response) {

                Log.d("APOD", response.body().getTitle());

                //Si es imagen
                if(response.body().getMediaType().equals("image")) {
                    imageApod.setVisibility(View.VISIBLE);
                    imageApod.setMaxHeight(350);
                    btnVideo.setVisibility(View.INVISIBLE);
                    Picasso.with(getApplicationContext()).load(response.body().getHdurl()).into(imageApod);
                }
                else{ //Si es video
                    imageApod.setVisibility(View.INVISIBLE);
                    btnVideo.setVisibility(View.VISIBLE);
                    urlVideo = response.body().getUrl();

                    //Manejamos su evento click para ver el video de youtube
                    btnVideo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.setData(Uri.parse(urlVideo));
                            getApplicationContext().startActivity(intent);
                        }
                    });
                }

                //Asignando los otros valores
                tvDate.setText(response.body().getDate());
                tvTitle.setText(response.body().getTitle());
                tvExplanation.setText(response.body().getExplanation());

                //No siempre viene con copyright
                String copyright = TextUtils.isEmpty(response.body().getCopyright())? "" :
                        response.body().getCopyright();
                tvCopyright.setText(copyright);
            }

            @Override
            public void onFailure(Call<APOD> call, Throwable t) {

            }
        });
    }
}
