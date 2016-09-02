package elsuper.david.com.spacetravel.ui.view.apod.list.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import elsuper.david.com.spacetravel.R;
import elsuper.david.com.spacetravel.model.Photo;

/**
 * Created by Andrés David García Gómez
 */
public class NasaApodViewHolder extends RecyclerView.ViewHolder{

    //Para manejar el click y el click sostenido en la foto
    private NasaApodAdapter.OnItemClickListener onItemListener;
    private NasaApodAdapter.OnItemLongClickListener onItemLongListener;
    private Photo photo;

    //Controles del nasa_apod_item
    @BindView(R.id.item_sdvImage) SimpleDraweeView itemImage;
    @BindView(R.id.item_tvTitle) TextView itemTitle;

    public NasaApodViewHolder(View itemView) {
        super(itemView);
        //Acceso a controles
        ButterKnife.bind(this,itemView);
    }

    //Seteando el click para cada foto
    public void setItemClick(Photo photo, NasaApodAdapter.OnItemClickListener onItemListener){
        this.photo = photo;
        this.onItemListener = onItemListener;
    }

    //Seteando el click sostenido para cada foto
    public void setItemLongClick(Photo photo, NasaApodAdapter.OnItemLongClickListener onItemLongListener){
        this.photo = photo;
        this.onItemLongListener = onItemLongListener;
    }

    //Definiendo los "escucha" para el click y click sostenido
    @OnClick(R.id.item_sdvImage)
    public void onViewClick(View view){
        if(onItemListener != null)
            onItemListener.onItemClick(photo);
    }

    @OnLongClick(R.id.item_sdvImage)
    public boolean onViewLongClick(View view){
        if(onItemLongListener != null) {
            onItemLongListener.onItemLongClick(photo);
            return true;
        }
        else
            return false;
    }
}
