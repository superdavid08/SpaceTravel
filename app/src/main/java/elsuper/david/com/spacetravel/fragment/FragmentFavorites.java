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
import elsuper.david.com.spacetravel.FavoriteDetailActivity;
import elsuper.david.com.spacetravel.R;
import elsuper.david.com.spacetravel.model.Apod;
import elsuper.david.com.spacetravel.model.Camera;
import elsuper.david.com.spacetravel.model.CameraSecondary;
import elsuper.david.com.spacetravel.model.Favorite;
import elsuper.david.com.spacetravel.model.Photo;
import elsuper.david.com.spacetravel.model.Rover;
import elsuper.david.com.spacetravel.sql.ApodDataSource;
import elsuper.david.com.spacetravel.sql.CameraDataSource;
import elsuper.david.com.spacetravel.sql.CameraSecondaryDataSource;
import elsuper.david.com.spacetravel.sql.PhotoDataSource;
import elsuper.david.com.spacetravel.sql.RoverDataSource;
import elsuper.david.com.spacetravel.ui.view.apod.list.adapter.NasaFavoriteAdapter;
import elsuper.david.com.spacetravel.util.ConnectionUtil;

/**
 * Created by Andrés David García Gómez.
 */
public class FragmentFavorites extends Fragment {

    //Controles del fragment
    @BindView(R.id.fragFavorites_marsRover) RecyclerView marsRoverFavoritesRecycler;
    @BindView(R.id.fragFavorites_tvUserName) TextView tvUserName;

    //Adaptador
    private final NasaFavoriteAdapter nasaFavoriteAdapter = new NasaFavoriteAdapter();
    //Para usar la base de datos
    private PhotoDataSource photoDataSource;
    private CameraDataSource cameraDataSource;
    private RoverDataSource roverDataSource;
    private CameraSecondaryDataSource cameraSecondaryDataSource;
    private ApodDataSource apodDataSource;
    //Para validar la conexión a internet
    private ConnectionUtil connection;

    public static FragmentFavorites newInstance(String userName)
    {
        FragmentFavorites f = new FragmentFavorites();
        Bundle b = new Bundle();
        b.putString("key_userName",userName);
        f.setArguments(b);

        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Inflamos la vista
        View view = inflater.inflate(R.layout.fragment_favorites,container,false);
        //Acceso a controles del fragment
        ButterKnife.bind(this,view);

        //Creamos las instancias para acceder a las tablas
        photoDataSource = new PhotoDataSource(getActivity());
        cameraDataSource = new CameraDataSource(getActivity());
        roverDataSource = new RoverDataSource(getActivity());
        cameraSecondaryDataSource = new CameraSecondaryDataSource(getActivity());
        apodDataSource = new ApodDataSource(getActivity());
        //Creamos la instancia para validar la conexión
        connection = new ConnectionUtil(getActivity());

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Extraemos los argumentos
        Bundle arguments = getArguments();
        if(arguments != null){
            tvUserName.setText(arguments.getString("key_userName"));
        }

        //Establecemos el layout en que se mostrará nuestro listado
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        marsRoverFavoritesRecycler.setLayoutManager(linearLayoutManager);

        //Llenamos el adapter
        List<Favorite> favoritesList = getFavorites();
        if(favoritesList.size() == 0)
            tvUserName.setText(getString(R.string.fragFavorites_msgEmpty));

        //Le avisamos al usuario que no hay conexión, pero en este caso no es necesario detener el llenado del adapter
        if(!connection.isConnected()){
            Toast.makeText(getActivity(),getString(R.string.connectionRequired), Toast.LENGTH_LONG).show();
        }

        nasaFavoriteAdapter.setFavorites(favoritesList);
        marsRoverFavoritesRecycler.setAdapter(nasaFavoriteAdapter);



        //Para manejar el click en la foto, envía a su detalle (Imagen ampliada)
        nasaFavoriteAdapter.setOnItemClickListener(new NasaFavoriteAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(Favorite favorite) {
                Intent intent = new Intent(getActivity(), FavoriteDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("key_imageFavorites",favorite);
                intent.putExtra("key_bundleFavorites",bundle);
                startActivity(intent);
            }
        });

        //Se agrega el click sostenido. Borra el elemento seleccionado
        nasaFavoriteAdapter.setOnItemLongClickListener(new NasaFavoriteAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(final Favorite favoritie) {

                //Para el mensaje de borrado
                String title;
                if(favoritie.getIsApod()) title = favoritie.getTitle();
                else title = String.valueOf(favoritie.getId());

                new AlertDialog.Builder(getActivity())
                        .setTitle(getString(R.string.fragments_msgDeleteFavorites))
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
                                nasaFavoriteAdapter.setFavorites(getFavorites());
                                marsRoverFavoritesRecycler.setAdapter(nasaFavoriteAdapter);

                            }
                        }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setCancelable(false).create().show();
            }
        });
    }

    private List<Favorite> getFavorites() {
        //Aqui se guardarán todos los favoritos
        List<Favorite> favoritesList = new ArrayList<>();

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
            Favorite favorite = new Favorite();
            favorite.setIsApod(false);
            favorite.setId(photo.getId());
            favorite.setTitle(photo.getCamera().getFullName());
            favorite.setDate(photo.getEarthDate());
            favorite.setUrl(photo.getImgSrc());
            favoritesList.add(favorite);
        }

        //También los Apod
        List<Apod> apodList = apodDataSource.getAllApods();

        //Los agregamos a Favoritos
        for (Apod apod: apodList) {
            Favorite favorite = new Favorite();
            favorite.setIsApod(true);
            favorite.setId(apod.getId());
            favorite.setTitle(apod.getTitle());
            favorite.setDate(apod.getDate());
            favorite.setUrl(apod.getUrl());
            favoritesList.add(favorite);
        }

        return favoritesList;
    }
}
