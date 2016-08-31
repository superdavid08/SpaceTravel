package elsuper.david.com.spacetravel;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;
import elsuper.david.com.spacetravel.model.Favoritie;
import elsuper.david.com.spacetravel.model.Photo;

public class FavoritieDetailActivity extends AppCompatActivity {

    @BindView(R.id.favoritieDetail_toolbar) Toolbar toolbar;
    @BindView(R.id.favoritieDetail_sdvImage) SimpleDraweeView itemImage;

    //Para almacenar la url de la imagen seleccionada
    private String urlImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoritie_detail);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        //Obtenemos los Extras
        Bundle bundle = getIntent().getExtras().getBundle("key_bundleFavorities");
        Favoritie favoritie = (Favoritie) bundle.getSerializable("key_imageFavorities");

        //Extraemos los datos del objeto Photo y los asignamos valores a cada control
        itemImage.setImageURI(favoritie.getUrl());
        urlImage = favoritie.getUrl();
    }

    //region Menú
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.menu_shareTodayApod:
                //Compartimos la url de la imagen seleccionada
                shareText(urlImage);
                return true;
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void shareText(String text){
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, text);
        //Compartimos la url de la imagen en la aplicación que el usuario seleccione
        startActivity(Intent.createChooser(shareIntent, getString(R.string.fragments_msgShare)));
    }
    //endregion
}
