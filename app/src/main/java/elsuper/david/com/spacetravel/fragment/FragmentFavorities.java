package elsuper.david.com.spacetravel.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import elsuper.david.com.spacetravel.BuildConfig;
import elsuper.david.com.spacetravel.DetailActivity;
import elsuper.david.com.spacetravel.R;
import elsuper.david.com.spacetravel.data.ApodService;
import elsuper.david.com.spacetravel.data.Data;
import elsuper.david.com.spacetravel.model.Camera;
import elsuper.david.com.spacetravel.model.CameraSecondary;
import elsuper.david.com.spacetravel.model.MarsRoverResponse;
import elsuper.david.com.spacetravel.model.Photo;
import elsuper.david.com.spacetravel.model.Rover;
import elsuper.david.com.spacetravel.sql.CameraDataSource;
import elsuper.david.com.spacetravel.sql.CameraSecondaryDataSource;
import elsuper.david.com.spacetravel.sql.PhotoDataSource;
import elsuper.david.com.spacetravel.sql.RoverDataSource;
import elsuper.david.com.spacetravel.ui.view.apod.list.adapter.NasaApodAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Andrés David García Gómez.
 */
public class FragmentFavorities extends Fragment {

    @BindView(R.id.fragFavorities_marsRover) RecyclerView marsRoverFavoritiesRecycler;

    //Para usar la base de datos
    private PhotoDataSource photoDataSource;
    private CameraDataSource cameraDataSource;
    private RoverDataSource roverDataSource;
    private CameraSecondaryDataSource cameraSecondaryDataSource;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Inflamos la vista
        View view = inflater.inflate(R.layout.fragment_favorities,container,false);
        ButterKnife.bind(this,view);

        //Creamos las instancias para acceder a las tablas
        photoDataSource = new PhotoDataSource(getActivity());
        cameraDataSource = new CameraDataSource(getActivity());
        roverDataSource = new RoverDataSource(getActivity());
        cameraSecondaryDataSource = new CameraSecondaryDataSource(getActivity());

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        //GridLayoutManager gridLayoutManager = new GridLayoutManager(view.getContext(),2);
        //StaggeredGridLayoutManager staggeredGridLayoutManager =
                //new StaggeredGridLayoutManager(10,StaggeredGridLayoutManager.VERTICAL);
        marsRoverFavoritiesRecycler.setLayoutManager(linearLayoutManager);
        //marsRoverFavoritiesRecycler.setLayoutManager(gridLayoutManager);
        //marsRoverFavoritiesRecycler.setLayoutManager(staggeredGridLayoutManager);

        final NasaApodAdapter nasaApodAdapter = new NasaApodAdapter();

        //Consultamos los datos en la base
        List<Photo> photoList = photoDataSource.getAllPhotos();
        for (Photo photo: photoList) {
            Camera camera = cameraDataSource.getCamera(photo.getId());
            photo.setCamera(camera);

            List<CameraSecondary> cameraSecondaryList =
                    cameraSecondaryDataSource.getAllCamerasSecondariesByIdPhoto(photo.getId());

            Rover rover = roverDataSource.getRover(photo.getId());
            rover.setCameras(cameraSecondaryList);
        }
        //Llenamos el adapter
        nasaApodAdapter.setMarsPhotos(photoList);
        marsRoverFavoritiesRecycler.setAdapter(nasaApodAdapter);
    }
}
