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

    private List<Photo> marsPhotos;

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
        holder.itemApodTitle.setText(photo.getCamera().getFullName());
        Picasso.with(holder.itemApodImage.getContext())
                .load(photo.getImgSrc())
                .into(holder.itemApodImage);
    }

    @Override
    public int getItemCount() {
        return marsPhotos != null? marsPhotos.size() : 0;
    }
}