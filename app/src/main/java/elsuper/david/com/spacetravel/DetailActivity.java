package elsuper.david.com.spacetravel;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    private String fullName;
    private String imgSrc;
    private String earthDate;
    private String cameraName;

    @BindView(R.id.detail_Image)
    SimpleDraweeView itemImage;
    @BindView(R.id.detail_Title)
    TextView itemTitle;
    @BindView(R.id.detail_EarthDate)
    TextView itemEarthDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        //Obtenemos los Extras
        fullName = getIntent().getExtras().getString("key_fullName");
        imgSrc = getIntent().getExtras().getString("key_imgsrc");
        earthDate = getIntent().getExtras().getString("key_earthDate");
        cameraName = getIntent().getExtras().getString("key_cameraName");

        itemTitle.setText(fullName);
        itemImage.setImageURI(imgSrc);
        itemEarthDate.setText(cameraName + " " + earthDate);
    }
}
