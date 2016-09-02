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
import elsuper.david.com.spacetravel.model.Favorite;

/**
 * Created by Andrés David García Gómez
 */
public class NasaFavoriteViewHolder extends RecyclerView.ViewHolder{

    //Para manejar el click y el click sostenido en la foto
    private NasaFavoriteAdapter.OnItemClickListener onItemListener;
    private NasaFavoriteAdapter.OnItemLongClickListener onItemLongListener;
    private Favorite favorite;

    //Controles del nasa_favorite_item
    @BindView(R.id.itemFavorite_sdvImage) SimpleDraweeView itemImage;
    @BindView(R.id.itemFavorite_tvTitle) TextView itemTitle;
    @BindView(R.id.itemFavorite_tvDate) TextView itemDate;
    @BindView(R.id.itemFavorite_tvId) TextView itemId;

    public NasaFavoriteViewHolder(View itemView) {
        super(itemView);
        //Acceso a controles
        ButterKnife.bind(this,itemView);
    }

    //Seteando el click para cada foto
    public void setItemClick(Favorite favorite, NasaFavoriteAdapter.OnItemClickListener onItemListener){
        this.favorite = favorite;
        this.onItemListener = onItemListener;
    }

    //Seteando el click sostenido para cada foto
    public void setItemLongClick(Favorite favorite, NasaFavoriteAdapter.OnItemLongClickListener onItemLongListener){
        this.favorite = favorite;
        this.onItemLongListener = onItemLongListener;
    }

    //Definiendo los "escucha" para el click y click sostenido
    @OnClick(R.id.itemFavorite_sdvImage)
    public void onViewClick(View view){
        if(onItemListener != null)
            onItemListener.onItemClick(favorite);
    }

    @OnLongClick(R.id.itemFavorite_sdvImage)
    public boolean onViewLongClick(View view){
        if(onItemLongListener != null) {
            onItemLongListener.onItemLongClick(favorite);
            return true;
        }
        else
            return false;
    }
}
