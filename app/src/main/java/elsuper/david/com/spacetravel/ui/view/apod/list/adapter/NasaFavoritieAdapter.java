package elsuper.david.com.spacetravel.ui.view.apod.list.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import elsuper.david.com.spacetravel.R;
import elsuper.david.com.spacetravel.model.Favoritie;

/**
 * Created by Andrés David García Gómez
 */
public class NasaFavoritieAdapter extends RecyclerView.Adapter<NasaFavoritieViewHolder> {

    private List<Favoritie> favorities;
    //interface
    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;

    public NasaFavoritieAdapter(){}
    public NasaFavoritieAdapter(List<Favoritie> favorities) {
        this.favorities = favorities;
    }

    @Override
    public NasaFavoritieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NasaFavoritieViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.nasa_favoritie_item,parent,false));
    }

    @Override
    public void onBindViewHolder(NasaFavoritieViewHolder holder, int position) {
        //Obtenemos el elemento por su posición y asignamos sus valores en el holder
        Favoritie favoritie = favorities.get(position);
        holder.itemId.setText(String.valueOf(favoritie.getId()));
        holder.itemTitle.setText(favoritie.getTitle());
        holder.itemDate.setText(favoritie.getDate());
        holder.itemImage.setImageURI(favoritie.getUrl());
        holder.setItemClick(favoritie,onItemClickListener);
        holder.setItemLongClick(favoritie,onItemLongClickListener);
    }

    @Override
    public int getItemCount() { return favorities != null? favorities.size() : 0; }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener){
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public void setFavorities(List<Favoritie> favorities){
        this.favorities = favorities;
    }

    //Interface para manejar el click en la foto
    public interface OnItemClickListener{
        void onItemClick(Favoritie favoritie);
    }

    public interface OnItemLongClickListener{
        void onItemLongClick(Favoritie favoritie); //Se agrega el click largo
    }
}
