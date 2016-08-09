package elsuper.david.com.spacetravel.ui.view.apod.list.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import elsuper.david.com.spacetravel.R;

/**
 * Created by Andrés David García Gómez
 */
public class NasaApodViewHolder extends RecyclerView.ViewHolder{

    @BindView(R.id.item_apodImage)
    ImageView itemApodImage;
    @BindView(R.id.item_apodTitle)
    TextView itemApodTitle;

    public NasaApodViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }
}
