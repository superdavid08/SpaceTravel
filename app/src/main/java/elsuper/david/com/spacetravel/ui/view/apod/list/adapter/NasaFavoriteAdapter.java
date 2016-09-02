package elsuper.david.com.spacetravel.ui.view.apod.list.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import elsuper.david.com.spacetravel.R;
import elsuper.david.com.spacetravel.model.Favorite;

/**
 * Created by Andrés David García Gómez
 */
public class NasaFavoriteAdapter extends RecyclerView.Adapter<NasaFavoriteViewHolder> {

    //Lista de favoritos
    private List<Favorite> favorites;
    //interfaces
    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;

    public NasaFavoriteAdapter(){}
    public NasaFavoriteAdapter(List<Favorite> favorites) {
        this.favorites = favorites;
    }

    @Override
    public NasaFavoriteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NasaFavoriteViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.nasa_favorite_item,parent,false));
    }

    @Override
    public void onBindViewHolder(NasaFavoriteViewHolder holder, int position) {
        //Obtenemos el elemento por su posición y asignamos sus valores en el holder
        Favorite favorite = favorites.get(position);
        holder.itemId.setText(String.valueOf(favorite.getId()));
        holder.itemTitle.setText(favorite.getTitle());
        holder.itemDate.setText(favorite.getDate());
        holder.itemImage.setImageURI(favorite.getUrl());
        holder.setItemClick(favorite,onItemClickListener);
        holder.setItemLongClick(favorite,onItemLongClickListener);
    }

    @Override
    public int getItemCount() { return favorites != null? favorites.size() : 0; }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener){
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public void setFavorites(List<Favorite> favorities){
        this.favorites = favorities;
    }

    //Interface para manejar el click en el favorito (foto)
    public interface OnItemClickListener{
        void onItemClick(Favorite favorite);
    }

    //Interface para manejo del click sostenido en el favorito (foto)
    public interface OnItemLongClickListener{
        void onItemLongClick(Favorite favorite);
    }
}
