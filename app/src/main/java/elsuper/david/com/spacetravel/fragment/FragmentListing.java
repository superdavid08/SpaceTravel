package elsuper.david.com.spacetravel.fragment;

import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import elsuper.david.com.spacetravel.BuildConfig;
import elsuper.david.com.spacetravel.DetailActivity;
import elsuper.david.com.spacetravel.R;
import elsuper.david.com.spacetravel.data.ApodService;
import elsuper.david.com.spacetravel.data.Data;
import elsuper.david.com.spacetravel.model.CameraSecondary;
import elsuper.david.com.spacetravel.model.MarsRoverResponse;
import elsuper.david.com.spacetravel.model.Photo;
import elsuper.david.com.spacetravel.sql.CameraDataSource;
import elsuper.david.com.spacetravel.sql.CameraSecondaryDataSource;
import elsuper.david.com.spacetravel.sql.PhotoDataSource;
import elsuper.david.com.spacetravel.sql.RoverDataSource;
import elsuper.david.com.spacetravel.ui.view.apod.list.adapter.NasaApodAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentListing extends Fragment{

    @BindView(R.id.fragListing_marsRover) RecyclerView marsRoverListingRecycler;

    //Para usar la base de datos
    private PhotoDataSource photoDataSource;
    private CameraDataSource cameraDataSource;
    private RoverDataSource roverDataSource;
    private CameraSecondaryDataSource cameraSecondaryDataSource;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        //Inflamos la vista
        View view = inflater.inflate(R.layout.fragment_listing,container,false);
        ButterKnife.bind(this,view);

        //Creamos las instancias para acceder a las tablas
        photoDataSource = new PhotoDataSource(getActivity());
        cameraDataSource = new CameraDataSource(getActivity());
        roverDataSource = new RoverDataSource(getActivity());
        cameraSecondaryDataSource = new CameraSecondaryDataSource(getActivity());

        return view;
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(view.getContext(),2);
        StaggeredGridLayoutManager staggeredGridLayoutManager =
                new StaggeredGridLayoutManager(10,StaggeredGridLayoutManager.VERTICAL);
        //marsRoverListingRecycler.setLayoutManager(LinearLayoutManager);
        marsRoverListingRecycler.setLayoutManager(gridLayoutManager);
        //marsRoverListingRecycler.setLayoutManager(staggeredGridLayoutManager);

        //Para manejar el click en la foto
        final NasaApodAdapter nasaApodAdapter = new NasaApodAdapter();
        nasaApodAdapter.setOnItemClickListener(new NasaApodAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(Photo photo) {
                Intent intent = new Intent(view.getContext(),DetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("key_photo",photo);
                intent.putExtra("key_bundle",bundle);
                startActivity(intent);
            }
        });

        //Se agrega el click largo
        nasaApodAdapter.setOnItemLongClickListener(new NasaApodAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(final Photo photo) {
                //Si no existe la foto en la lista de favoritos
                if(photoDataSource.getPhoto(photo.getId()) == null) {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Agregar a Favoritos")
                            .setMessage("Deseas agregarlo a tu lista de favoritos?")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    //Guardamos el objeto en la base de datos
                                    photoDataSource.savePhoto(photo);
                                    cameraDataSource.saveCamera(photo.getCamera(), photo.getId());
                                    roverDataSource.saveRover(photo.getRover(), photo.getId());

                                    List<CameraSecondary> cameraSecondaryList = photo.getRover().getCameras();
                                    for (CameraSecondary cameraSecondary : cameraSecondaryList) {
                                        cameraSecondaryDataSource.saveCameraSecondary(cameraSecondary, photo.getId());
                                    }
                                }
                            }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).setCancelable(false).create().show();
                }
                else{
                    Toast.makeText(getActivity(),"El elemento ya existe en la lista de Favoritos",Toast.LENGTH_SHORT).show();
                }
            }
        });

        ApodService apodService = Data.getRetrofitInstance().create(ApodService.class);

        apodService.getTodayMarsRovertWithQuery(400, BuildConfig.NASA_API_KEY).enqueue(new Callback<MarsRoverResponse>() {
            @Override
            public void onResponse(Call<MarsRoverResponse> call, Response<MarsRoverResponse> response) {
                nasaApodAdapter.setMarsPhotos(response.body().getPhotos());
                marsRoverListingRecycler.setAdapter(nasaApodAdapter);
            }

            @Override
            public void onFailure(Call<MarsRoverResponse> call, Throwable t) {

            }
        });
    }

    //2016-08-13
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.list_rover_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.menu_shareListRover:
                shareText("Lista de fotos");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void shareText(String text){
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, text);
        startActivity(Intent.createChooser(shareIntent,getString(R.string.fragments_share)));
    }
}
