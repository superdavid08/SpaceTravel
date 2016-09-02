package elsuper.david.com.spacetravel.fragment;

import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
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

    //Controles del fragment
    @BindView(R.id.fragListing_marsRover) RecyclerView marsRoverListingRecycler;
    @BindView(R.id.fragListing_btnNextPage) Button btnNextPage;
    @BindView(R.id.fragListing_btnPreviousPage) Button btnPreviousPage;
    @BindView(R.id.fragListing_tvNumPage) TextView tvNumPage;

    //Adaptador
    private final NasaApodAdapter nasaApodAdapter = new NasaApodAdapter();
    //Para la consulta del servicio web con Retrofit
    private ApodService apodService;
    //Para almacenar la url de la imagen seleccionada
    private String urlImageMarsRover;
    //Para usar la base de datos
    private PhotoDataSource photoDataSource;
    private CameraDataSource cameraDataSource;
    private RoverDataSource roverDataSource;
    private CameraSecondaryDataSource cameraSecondaryDataSource;
    //Número máximo "sol" al 26/08/2016
    private static final int solNumberMax = 1388;
    //Esta variable tomará un valor aleatorio entre 1 y 1388 (solNumberMax)
    private int solNumber;
    //Contador de páginas
    private int numPage = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        //Inflamos la vista
        View view = inflater.inflate(R.layout.fragment_listing,container,false);
        //Acceso a controles del fragment
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

        //Deshabilitamos el botón de "Anterior" cuando es la primera página
        if(Integer.parseInt(tvNumPage.getText().toString()) == 1) {
            btnPreviousPage.setEnabled(false);
            btnPreviousPage.setText("");
        }

        //Generamos un número aleatorio entre 1 y solNumberMax para que sea el solNumber
        Random random = new Random();
        solNumber = (int)(random.nextDouble() * (solNumberMax +1));
        if(solNumberMax == 0) solNumber = 1;

        //Establecemos el layout en que se mostrará nuestro listado
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(view.getContext(),2);
        marsRoverListingRecycler.setLayoutManager(gridLayoutManager);

        //Manejamos el click en la foto. Nos envía al detalle
        nasaApodAdapter.setOnItemClickListener(new NasaApodAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(Photo photo) {
                //Url de la foto seleccionada
                urlImageMarsRover = photo.getImgSrc();

                Intent intent = new Intent(view.getContext(),DetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("key_photo",photo);
                intent.putExtra("key_bundle",bundle);
                startActivity(intent);
            }
        });

        //Manejamos el click largo en la foto. Para agregar a favoritos
        nasaApodAdapter.setOnItemLongClickListener(new NasaApodAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(final Photo photo) {
                //Si no existe la foto en la lista de favoritos
                if(photoDataSource.getPhoto(photo.getId()) == null) {
                    //Url de la foto seleccionada
                    urlImageMarsRover = photo.getImgSrc();

                    new AlertDialog.Builder(getActivity())
                            .setTitle(getString(R.string.fragments_msgAddToFavorites))
                            .setMessage(String.format(getString(R.string.fragments_msgQuestionAdd), photo.getId()))
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

                                    Toast.makeText(getActivity(),getString(R.string.fragments_msgSuccessfullyAdded),
                                            Toast.LENGTH_SHORT).show();

                                }
                            }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).setCancelable(false).create().show();
                }
                else{
                    Toast.makeText(getActivity(),getString(R.string.fragments_msgAlreadyExist),Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Instanciamos el servicio apod para poder usar Retrofit
        apodService = Data.getRetrofitInstance().create(ApodService.class);

        if(apodService != null) {
            //LLamamos al método que establece su callback
            apodServiceEnqueue(apodService);
        }
    }

    private void apodServiceEnqueue(ApodService apodService) {

        //Consumimos el servicio web y definimos su callback
        apodService.getTodayMarsRovertWithAllQuery(solNumber,numPage, BuildConfig.NASA_API_KEY)
                .enqueue(new Callback<MarsRoverResponse>() {
            @Override
            public void onResponse(Call<MarsRoverResponse> call, Response<MarsRoverResponse> response) {
                if(response != null && response.body().getPhotos().size() > 0) {
                    //Seteamos el listado de fotos en el adaptador
                    nasaApodAdapter.setMarsPhotos(response.body().getPhotos());
                    marsRoverListingRecycler.setAdapter(nasaApodAdapter);
                }
                else{
                    Snackbar.make(getActivity().findViewById(android.R.id.content),
                            String.format(getString(R.string.fragListing_tvEndOfListing), solNumber),
                            Snackbar.LENGTH_LONG).show();

                    nasaApodAdapter.setMarsPhotos(null);
                    marsRoverListingRecycler.setAdapter(null);
                    btnNextPage.setText("");
                    btnNextPage.setEnabled(false);
                }
            }

            @Override
            public void onFailure(Call<MarsRoverResponse> call, Throwable t) {

            }
        });
    }

    /*
    //region Menú
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
                shareText(urlImageMarsRover);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void shareText(String text){
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, text);
        //Compartimos la url de la imagen en la aplicación que el usuario seleccione
        startActivity(Intent.createChooser(shareIntent,getString(R.string.fragments_msgShare)));
    }
    //endregion

    //region Clicks de los controles
    @OnClick(R.id.fragListing_btnNextPage)
    public void onClickBtnNextPage(){
        //Se muestra la siguiente página
        if(btnNextPage.isEnabled()) {
            numPage++;
            tvNumPage.setText(String.valueOf(numPage));
            apodServiceEnqueue(apodService);
        }

        if(!btnPreviousPage.isEnabled()) {
            btnPreviousPage.setEnabled(true);
            btnPreviousPage.setText(getString(R.string.fragListing_btnPreviousPage));
        }
    }

    @OnClick(R.id.fragListing_btnPreviousPage)
    public void onClickBtnPreviousPage(){
        //Se muestra la página prevía
        numPage--;
        tvNumPage.setText(String.valueOf(numPage));
        apodServiceEnqueue(apodService);

        if(numPage == 1) {
            btnPreviousPage.setText("");
            btnPreviousPage.setEnabled(false);
        }

        if(!btnNextPage.isEnabled()) {
            btnNextPage.setEnabled(true);
            btnNextPage.setText(getString(R.string.fragListing_btnNextPage));
        }
    }
    //endregion
    */
}
