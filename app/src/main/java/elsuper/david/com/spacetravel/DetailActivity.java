package elsuper.david.com.spacetravel;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;
import elsuper.david.com.spacetravel.model.Photo;

public class DetailActivity extends AppCompatActivity {

    @BindView(R.id.detail_toolbar) Toolbar toolbar;
    @BindView(R.id.detail_sdvImage) SimpleDraweeView itemImage;
    @BindView(R.id.detail_tvTitle) TextView itemTitle;
    @BindView(R.id.detail_tvId) TextView itemId;
    @BindView(R.id.detail_tvSol) TextView itemSol;
    @BindView(R.id.detail_tvEarthDate) TextView itemEarthDate;
    @BindView(R.id.detail_tvCameraId) TextView itemCameraId;
    @BindView(R.id.detail_tvCameraName) TextView itemCameraName;
    @BindView(R.id.detail_tvCameraRoverId) TextView itemCameraRoverId;
    @BindView(R.id.detail_tvCameraFullName) TextView itemCameraFullName;

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
        itemTitle.setText(photo.getCamera().getFullName());
        itemId.setText(photo.getId().toString());
        itemSol.setText(photo.getSol().toString());
        itemEarthDate.setText(photo.getEarthDate());
        itemCameraId.setText(photo.getCamera().getId().toString());
        itemCameraName.setText(photo.getCamera().getName());
        itemCameraRoverId.setText(photo.getCamera().getRoverId().toString());
        itemCameraFullName.setText(photo.getCamera().getFullName());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
