package elsuper.david.com.spacetravel;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import elsuper.david.com.spacetravel.model.CameraSecondary;
import elsuper.david.com.spacetravel.model.Photo;
import elsuper.david.com.spacetravel.sql.CameraDataSource;
import elsuper.david.com.spacetravel.sql.CameraSecondaryDataSource;
import elsuper.david.com.spacetravel.sql.PhotoDataSource;
import elsuper.david.com.spacetravel.sql.RoverDataSource;

public class DetailActivity extends AppCompatActivity {

    //Controles de la Activity
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
    //Para usar la base de datos
    private PhotoDataSource photoDataSource;
    private CameraDataSource cameraDataSource;
    private RoverDataSource roverDataSource;
    private CameraSecondaryDataSource cameraSecondaryDataSource;
    //Elemento que se muestra en pantalla
    private Photo photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        //Acceso a controles
        ButterKnife.bind(this);

        //Creamos las instancias para acceder a las tablas
        photoDataSource = new PhotoDataSource(DetailActivity.this);
        cameraDataSource = new CameraDataSource(DetailActivity.this);
        roverDataSource = new RoverDataSource(DetailActivity.this);
        cameraSecondaryDataSource = new CameraSecondaryDataSource(DetailActivity.this);

        //Agregando el toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        //Obtenemos los Extras
        Intent intent = getIntent();
        if(intent != null) {
            Bundle bundle = intent.getExtras().getBundle("key_bundle");
            photo = (Photo) bundle.getSerializable("key_photo");

            //Extraemos los datos del objeto Photo y le asignamos valores a cada control
            itemImage.setImageURI(photo.getImgSrc());
            urlImage = photo.getImgSrc();
            itemId.setText(itemId.getText().toString() + " " + photo.getId().toString());
            itemSol.setText(itemSol.getText().toString() + " " + photo.getSol().toString());
            itemEarthDate.setText(itemEarthDate.getText().toString() + " " + photo.getEarthDate());
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

    //region Clicks de los controles
    @OnLongClick(R.id.detail_sdvImage)
    public boolean onLongClickSdvImage(){
        //Si no existe la foto en la lista de favoritos, se puede agregar
        if(photoDataSource.getPhoto(photo.getId()) == null) {

            new AlertDialog.Builder(DetailActivity.this)
                    .setTitle(getString(R.string.fragments_msgAddToFavorites))
                    .setMessage(String.format(getString(R.string.fragments_msgQuestionAdd), photo.getId()))
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            //Guardamos el objeto en la base de datos
                            photoDataSource.savePhoto(photo);
                            cameraDataSource.saveCamera(photo.getCamera(), photo.getId());
                            roverDataSource.saveRover(photo.getRover(), photo.getId());

                            List<CameraSecondary> cameraSecondaryList = photo.getRover().getCameras();
                            for (CameraSecondary cameraSecondary : cameraSecondaryList) {
                                cameraSecondaryDataSource.saveCameraSecondary(cameraSecondary, photo.getId());
                            }

                            Toast.makeText(DetailActivity.this,getString(R.string.fragments_msgSuccessfullyAdded),
                                    Toast.LENGTH_SHORT).show();

                        }
                    }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }).setCancelable(false).create().show();
        }
        else{
            Toast.makeText(DetailActivity.this,getString(R.string.fragments_msgAlreadyExist),Toast.LENGTH_SHORT).show();
        }

        return true;
    }
    //endregion
}
