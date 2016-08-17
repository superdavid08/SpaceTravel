package elsuper.david.com.spacetravel.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import elsuper.david.com.spacetravel.BuildConfig;
import elsuper.david.com.spacetravel.R;
import elsuper.david.com.spacetravel.data.ApodService;
import elsuper.david.com.spacetravel.data.Data;
import elsuper.david.com.spacetravel.model.Apod;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Andrés David García Gómez.
 */
public class FragmentApod extends Fragment {

    @BindView(R.id.fragApod_image) ImageView imageApod;
    @BindView(R.id.fragApod_tvDate) TextView tvDate;
    @BindView(R.id.fragApod_tvTitle) TextView tvTitle;
    @BindView(R.id.fragApod_tvExplanation) TextView tvExplanation;
    @BindView(R.id.fragApod_tvCopyright) TextView tvCopyright;

    private String urlImageApod;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Inflamos la vista
        View view = inflater.inflate(R.layout.fragment_apod,container,false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Utilizando Retrofit
        ApodService apodService = Data.getRetrofitInstance().create(ApodService.class);

        //Call<Apod> callApodService = apodService.getTodayApod();
        Call<Apod> callApodService = apodService.getTodayApodWithQuery(BuildConfig.NASA_API_KEY);

        callApodService.enqueue(new Callback<Apod>() {
            @Override
            public void onResponse(Call<Apod> call, Response<Apod> response) {

                //Asignando valores
                if(response.body().getMediaType().equals("image")) {
                    urlImageApod = response.body().getHdurl();
                    Picasso.with(getActivity()).load(response.body().getHdurl()).into(imageApod);
                }
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
    }

    //2016-08-13
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.apod_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.menu_shareTodayApod:
                shareText(urlImageApod);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void shareText(String text){
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, text);
        startActivity(Intent.createChooser(shareIntent, getString(R.string.fragments_share)));
    }
}