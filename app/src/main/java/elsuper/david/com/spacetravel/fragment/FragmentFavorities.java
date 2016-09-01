package elsuper.david.com.spacetravel.fragment;

import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import elsuper.david.com.spacetravel.DetailActivity;
import elsuper.david.com.spacetravel.FavoritieDetailActivity;
import elsuper.david.com.spacetravel.R;
import elsuper.david.com.spacetravel.model.Apod;
import elsuper.david.com.spacetravel.model.Camera;
import elsuper.david.com.spacetravel.model.CameraSecondary;
import elsuper.david.com.spacetravel.model.Favoritie;
import elsuper.david.com.spacetravel.model.Photo;
import elsuper.david.com.spacetravel.model.Rover;
import elsuper.david.com.spacetravel.sql.ApodDataSource;
import elsuper.david.com.spacetravel.sql.CameraDataSource;
import elsuper.david.com.spacetravel.sql.CameraSecondaryDataSource;
import elsuper.david.com.spacetravel.sql.PhotoDataSource;
import elsuper.david.com.spacetravel.sql.RoverDataSource;
import elsuper.david.com.spacetravel.ui.view.apod.list.adapter.NasaApodAdapter;
import elsuper.david.com.spacetravel.ui.view.apod.list.adapter.NasaFavoritieAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Andrés David García Gómez.
 */
public class FragmentFavorities extends Fragment {

    @BindView(R.id.fragFavorities_marsRover) RecyclerView marsRoverFavoritiesRecycler;
    @BindView(R.id.fragFavorities_tvUserName) TextView tvUserName;

    //Para usar la base de datos
    private PhotoDataSource photoDataSource;
    private CameraDataSource cameraDataSource;
    private RoverDataSource roverDataSource;
    private CameraSecondaryDataSource cameraSecondaryDataSource;
    private ApodDataSource apodDataSource;

    public static FragmentFavorities newInstance(String userName)
    {
        FragmentFavorities f = new FragmentFavorities();
        Bundle b = new Bundle();
        b.putString("key_userName",userName);
        f.setArguments(b);

        return f;
    }


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
        apodDataSource = new ApodDataSource(getActivity());

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String userName = getArguments().getString("key_userName");
        tvUserName.setText(userName);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        marsRoverFavoritiesRecycler.setLayoutManager(linearLayoutManager);
        //linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);//


        //final NasaApodAdapter nasaApodAdapter = new NasaApodAdapter();
        final NasaFavoritieAdapter nasaFavoritieAdapter = new NasaFavoritieAdapter();

        //Llenamos el adapter
        //nasaApodAdapter.setMarsPhotos(getPhotos());
        List<Favoritie> favoritiesList = getFavorities();

        if(favoritiesList.size() == 0)
            tvUserName.setText(getString(R.string.fragFavorities_msgEmpty));

        nasaFavoritieAdapter.setFavorities(favoritiesList);
        marsRoverFavoritiesRecycler.setAdapter(nasaFavoritieAdapter);

        //Para manejar el click en la foto
        nasaFavoritieAdapter.setOnItemClickListener(new NasaFavoritieAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(Favoritie favoritie) {
                Intent intent = new Intent(getActivity(), FavoritieDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("key_imageFavorities",favoritie);
                intent.putExtra("key_bundleFavorities",bundle);
                startActivity(intent);
            }
        });

        //Se agrega el click largo
        nasaFavoritieAdapter.setOnItemLongClickListener(new NasaFavoritieAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(final Favoritie favoritie) {

                //Para el mensaje de borrado
                String title;
                if(favoritie.getIsApod()) title = favoritie.getTitle();
                else title = String.valueOf(favoritie.getId());

                new AlertDialog.Builder(getActivity())
                        .setTitle(getString(R.string.fragments_msgDeleteFavorities))
                        .setMessage(String.format(getString(R.string.fragments_msgQuestionDelete), title))
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Si es un apod
                                if(favoritie.getIsApod()){
                                    //Consultamos el favorito
                                    Apod apod = apodDataSource.getApod(favoritie.getTitle(),favoritie.getDate());
                                    //Eliminamos el objeto de la base de datos
                                    apodDataSource.deleteApod(apod.getId());
                                }
                                else{//Si es una foto del Mars Rovert
                                    //Eliminamos el objeto de la base de datos
                                    cameraSecondaryDataSource.deleteCamerasSecondariesByIdPhoto(favoritie.getId());
                                    roverDataSource.deleteRoversByIdPhoto(favoritie.getId());
                                    cameraDataSource.deleteCamerasByIdPhoto(favoritie.getId());
                                    photoDataSource.deletePhoto(favoritie.getId());
                                }

                                Toast.makeText(getActivity(),getString(R.string.fragments_msgSuccessfullyDeleted),
                                        Toast.LENGTH_SHORT).show();

                                //Recargamos la lista
                                nasaFavoritieAdapter.setFavorities(getFavorities());
                                marsRoverFavoritiesRecycler.setAdapter(nasaFavoritieAdapter);

                            }
                        }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setCancelable(false).create().show();
            }
        });
    }

    private List<Favoritie> getFavorities() {
        //Aqui se guardarán todos los favoritos
        List<Favoritie> favoritiesList = new ArrayList<>();

        /**************************************************************/
        /* Consultamos los datos en la base                           */
        /**************************************************************/
        //Primero las fotos del Rover
        List<Photo> photoList = photoDataSource.getAllPhotos();
        for (Photo photo: photoList) {
            Camera camera = cameraDataSource.getCamera(photo.getId());
            photo.setCamera(camera);

            List<CameraSecondary> cameraSecondaryList =
                    cameraSecondaryDataSource.getAllCamerasSecondariesByIdPhoto(photo.getId());

            Rover rover = roverDataSource.getRover(photo.getId());
            rover.setCameras(cameraSecondaryList);
        }

        //Los agregamos a Favoritos
        for (Photo photo: photoList) {
            Favoritie favoritie = new Favoritie();
            favoritie.setIsApod(false);
            favoritie.setId(photo.getId());
            favoritie.setTitle(photo.getCamera().getFullName());
            favoritie.setDate(photo.getEarthDate());
            favoritie.setUrl(photo.getImgSrc());
            favoritiesList.add(favoritie);
        }

        //También los Apod
        List<Apod> apodList = apodDataSource.getAllApods();

        //Los agregamos a Favoritos
        for (Apod apod: apodList) {
            Favoritie favoritie = new Favoritie();
            favoritie.setIsApod(true);
            favoritie.setId(apod.getId());
            favoritie.setTitle(apod.getTitle());
            favoritie.setDate(apod.getDate());
            favoritie.setUrl(apod.getHdurl());
            favoritiesList.add(favoritie);
        }

        return favoritiesList;
    }
}
