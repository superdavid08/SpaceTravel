package elsuper.david.com.spacetravel;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;
import elsuper.david.com.spacetravel.model.Favorite;
import elsuper.david.com.spacetravel.sql.ApodDataSource;
import elsuper.david.com.spacetravel.sql.CameraDataSource;
import elsuper.david.com.spacetravel.sql.CameraSecondaryDataSource;
import elsuper.david.com.spacetravel.sql.PhotoDataSource;
import elsuper.david.com.spacetravel.sql.RoverDataSource;
import elsuper.david.com.spacetravel.util.ConnectionUtil;

public class FavoriteDetailActivity extends AppCompatActivity {

    //Controles de la Activity
    @BindView(R.id.favoriteDetail_toolbar) Toolbar toolbar;
    @BindView(R.id.favoriteDetail_sdvImage) SimpleDraweeView itemImage;

    //Para almacenar la url de la imagen seleccionada
    private String urlImage;
    //Para validar la conexión a internet
    private ConnectionUtil connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_detail);
        //Acceso a controles
        ButterKnife.bind(this);
        //Creamos la instancia para validar la conexión
        connection = new ConnectionUtil(FavoriteDetailActivity.this);

        //Agregando el toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        //Obtenemos los Extras
        Intent intent = getIntent();
        if(intent != null) {
            //Le avisamos al usuario si no hay conexión, pero en este caso no es necesario detener el llenado del adapter
            if(!connection.isConnected()){
                Toast.makeText(FavoriteDetailActivity.this,getString(R.string.connectionRequired), Toast.LENGTH_LONG).show();
            }

            Bundle bundle = intent.getExtras().getBundle("key_bundleFavorites");
            Favorite favorite = (Favorite) bundle.getSerializable("key_imageFavorites");

            //Extraemos los datos del objeto Favorite y los asignamos
            itemImage.setImageURI(favorite.getUrl());
            urlImage = favorite.getUrl();
        }
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
            case R.id.menu_shareDetail:
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
