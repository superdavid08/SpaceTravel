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
import elsuper.david.com.spacetravel.model.Favoritie;

/**
 * Created by Andrés David García Gómez
 */
public class NasaFavoritieViewHolder extends RecyclerView.ViewHolder{

    //Para manejar el click en la foto
    private NasaFavoritieAdapter.OnItemClickListener onItemListener;
    private NasaFavoritieAdapter.OnItemLongClickListener onItemLongListener;
    private Favoritie favoritie;

    @BindView(R.id.itemFavoritie_sdvImage) SimpleDraweeView itemImage;
    @BindView(R.id.itemFavoritie_tvTitle) TextView itemTitle;
    @BindView(R.id.itemFavoritie_tvDate) TextView itemDate;
    @BindView(R.id.itemFavoritie_tvId) TextView itemId;

    public NasaFavoritieViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

    //Para manejar el click en la foto
    public void setItemClick(Favoritie favoritie, NasaFavoritieAdapter.OnItemClickListener onItemListener){
        this.favoritie = favoritie;
        this.onItemListener = onItemListener;
    }

    public void setItemLongClick(Favoritie favoritie, NasaFavoritieAdapter.OnItemLongClickListener onItemLongListener){
        this.favoritie = favoritie;
        this.onItemLongListener = onItemLongListener;
    }

    //Para manejar el click en la foto
    @OnClick(R.id.itemFavoritie_sdvImage)
    public void onViewClick(View view){
        if(onItemListener != null)
            onItemListener.onItemClick(favoritie);
    }

    @OnLongClick(R.id.itemFavoritie_sdvImage)
    public boolean onViewLongClick(View view){
        if(onItemLongListener != null) {
            onItemLongListener.onItemLongClick(favoritie);
            return true;
        }
        else
            return false;
    }
}
