package elsuper.david.com.spacetravel;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;
import elsuper.david.com.spacetravel.model.Photo;

public class DetailActivity extends AppCompatActivity {

    private String fullName;
    private String imgSrc;
    private String earthDate;
    private String cameraName;

    @BindView(R.id.detail_sdvImage)
    SimpleDraweeView itemImage;
    @BindView(R.id.detail_tvTitle)
    TextView itemTitle;
    @BindView(R.id.detail_tvEarthDate)
    TextView itemEarthDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        //Obtenemos los Extras

        //El siguiente bloque se comentó porque ya no se va a recibir uno por uno sino el objeto completo
        /*fullName = getIntent().getExtras().getString("key_fullName");
        imgSrc = getIntent().getExtras().getString("key_imgsrc");
        earthDate = getIntent().getExtras().getString("key_earthDate");
        cameraName = getIntent().getExtras().getString("key_cameraName");*/

        Bundle bundle = getIntent().getExtras().getBundle("key_bundle");
        Photo photo = (Photo) bundle.getSerializable("key_photo");

        //Extraemos los datos del objeto Photo
        fullName = photo.getCamera().getFullName();
        imgSrc = photo.getImgSrc();
        earthDate = photo.getEarthDate();
        cameraName = photo.getCamera().getName();

        //Asignamos valores a cada control
        itemTitle.setText(fullName);
        itemImage.setImageURI(imgSrc);
        itemEarthDate.setText(cameraName + " " + earthDate);
    }
}
