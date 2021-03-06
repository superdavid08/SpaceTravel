package elsuper.david.com.spacetravel.ui.view.apod.list.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.squareup.picasso.Picasso;
import java.util.List;

import elsuper.david.com.spacetravel.R;
import elsuper.david.com.spacetravel.model.Photo;

/**
 * Created by Andrés David García Gómez
 */
public class NasaApodAdapter extends RecyclerView.Adapter<NasaApodViewHolder> {

    //Lista de fotos
    private List<Photo> marsPhotos;
    //interfaces
    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;

    public NasaApodAdapter(){}
    public NasaApodAdapter(List<Photo> marsPhotos) {
        this.marsPhotos = marsPhotos;
    }

    @Override
    public NasaApodViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NasaApodViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.nasa_apod_item,parent,false));
    }

    @Override
    public void onBindViewHolder(NasaApodViewHolder holder, int position) {
        //Obtenemos el elemento por su posición y asignamos sus valores en el holder
        Photo photo = marsPhotos.get(position);
        holder.itemTitle.setText(photo.getCamera().getFullName());
        holder.itemImage.setImageURI(photo.getImgSrc());
        holder.setItemClick(photo,onItemClickListener);
        holder.setItemLongClick(photo,onItemLongClickListener);
    }

    @Override
    public int getItemCount() {
        return marsPhotos != null? marsPhotos.size() : 0;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener){
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public void setMarsPhotos(List<Photo> marsPhotos){
        this.marsPhotos = marsPhotos;
    }

    //Interface para manejo del click en la foto
    public interface OnItemClickListener{
        void onItemClick(Photo photo);
    }

    //Interface para manejo del click sostenido en la foto
    public interface OnItemLongClickListener{
        void onItemLongClick(Photo photo);
    }
}