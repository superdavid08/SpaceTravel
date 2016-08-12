package elsuper.david.com.spacetravel;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import elsuper.david.com.spacetravel.data.ApodService;
import elsuper.david.com.spacetravel.data.Data;
import elsuper.david.com.spacetravel.model.Apod;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Andrés David García Gómez
 */
public class MainActivity extends AppCompatActivity {

    //2016-08-05
    @BindView(R.id.main_imgApod) ImageView imageView;
    @BindView(R.id.main_tvDate) TextView tvDate;
    @BindView(R.id.main_tvTitle) TextView tvTitle;
    @BindView(R.id.main_tvExplanation) TextView tvExplanation;
    @BindView(R.id.main_tvCopyright) TextView tvCopyright;
    @BindView(R.id.main_btnGoList) Button btnGoToList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Acceedemos a los controles
        ButterKnife.bind(this);//2016-08-05
        Log.d("SpaceTravel", BuildConfig.URL);


        //Utilizando Retrofit
        ApodService apodService = Data.getRetrofitInstance().create(ApodService.class);
        //Call<Apod> callApodService = apodService.getTodayApod();
        Call<Apod> callApodService = apodService.getTodayApodWithQuery(BuildConfig.NASA_API_KEY);

        callApodService.enqueue(new Callback<Apod>() {
            @Override
            public void onResponse(Call<Apod> call, Response<Apod> response) {
                Log.d("SpaceTravel", response.body().getTitle());

                //Asignando valores
                if(response.body().getMediaType().equals("image"))
                    Picasso.with(MainActivity.this).load(response.body().getHdurl()).into(imageView);

                tvDate.setText(response.body().getDate());
                tvTitle.setText(response.body().getTitle());
                tvExplanation.setText(response.body().getExplanation());

                //No siempre viene con copyright
                String copyright = TextUtils.isEmpty(response.body().getCopyright())? "" :
                        response.body().getCopyright();
                tvCopyright.setText(copyright);
            }

            @Override
            public void onFailure(Call<Apod> call, Throwable t) {

            }
        });

        btnGoToList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ListActivity.class));
            }
        });
    }
}
