package elsuper.david.com.spacetravel;

import android.content.Intent;
import android.graphics.Matrix;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;
import elsuper.david.com.spacetravel.model.Photo;

public class DetailActivity extends AppCompatActivity {

    @BindView(R.id.detail_toolbar) Toolbar toolbar;
    @BindView(R.id.detail_sdvImage) SimpleDraweeView itemImage;
    @BindView(R.id.detail_tvId) TextView itemId;
    @BindView(R.id.detail_tvSol) TextView itemSol;
    @BindView(R.id.detail_tvEarthDate) TextView itemEarthDate;
    @BindView(R.id.detail_tvCameraId) TextView itemCameraId;
    @BindView(R.id.detail_tvCameraName) TextView itemCameraName;
    @BindView(R.id.detail_tvCameraRoverId) TextView itemCameraRoverId;
    @BindView(R.id.detail_tvCameraFullName) TextView itemCameraFullName;
    @BindView(R.id.detail_tvRoverId) TextView itemRoverId;
    @BindView(R.id.detail_tvRoverName) TextView itemRoverName;
    @BindView(R.id.detail_tvRoverLandingDate) TextView itemRoverLandingDate;
    @BindView(R.id.detail_tvRoverMaxSol) TextView itemRoverMaxSol;
    @BindView(R.id.detail_tvRoverMaxDate) TextView itemRoverMaxDate;
    @BindView(R.id.detail_tvRoverTotalPhotos) TextView itemRoverTotalPhotos;

    //Para almacenar la url de la imagen seleccionada
    private String urlImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        //Obtenemos los Extras
        Bundle bundle = getIntent().getExtras().getBundle("key_bundle");
        Photo photo = (Photo) bundle.getSerializable("key_photo");

        //Extraemos los datos del objeto Photo y los asignamos valores a cada control
        itemImage.setImageURI(photo.getImgSrc());
        urlImage = photo.getImgSrc();
        itemId.setText(itemId.getText().toString() + " " +  photo.getId().toString());
        itemSol.setText(itemSol.getText().toString() + " " +   photo.getSol().toString());
        itemEarthDate.setText(itemEarthDate.getText().toString() + " " +  photo.getEarthDate());
        itemCameraId.setText(photo.getCamera().getId().toString());
        itemCameraName.setText(" " + photo.getCamera().getName());
        itemCameraRoverId.setText(photo.getCamera().getRoverId().toString());
        itemCameraFullName.setText(" " + photo.getCamera().getFullName());
        itemRoverId.setText(photo.getRover().getId().toString());
        itemRoverName.setText(" " + photo.getRover().getName());
        itemRoverLandingDate.setText(" " + photo.getRover().getLandingDate());
        itemRoverMaxSol.setText(" " + photo.getRover().getMaxSol().toString());
        itemRoverMaxDate.setText(" " + photo.getRover().getMaxDate());
        itemRoverTotalPhotos.setText(" " + photo.getRover().getTotalPhotos().toString());
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
