package elsuper.david.com.spacetravel.fragment;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import elsuper.david.com.spacetravel.BuildConfig;
import elsuper.david.com.spacetravel.R;
import elsuper.david.com.spacetravel.data.ApodService;
import elsuper.david.com.spacetravel.data.Data;
import elsuper.david.com.spacetravel.model.Apod;
import elsuper.david.com.spacetravel.sql.ApodDataSource;
import elsuper.david.com.spacetravel.util.ConnectionUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Andrés David García Gómez.
 */
public class FragmentApod extends Fragment {

    //Controles del fragment
    @BindView(R.id.fragApod_image) ImageView imageApod;
    @BindView(R.id.fragApod_tvDate) TextView tvDate;
    @BindView(R.id.fragApod_tvTitle) TextView tvTitle;
    @BindView(R.id.fragApod_tvExplanation) TextView tvExplanation;
    @BindView(R.id.fragApod_tvCopyright) TextView tvCopyright;
    @BindView(R.id.fragApod_btnSelectDate) TextView btnSelectDate;

    //Para la consulta del servicio web con Retrofit
    private ApodService apodService;
    //Para almacenar la url de la imagen seleccionada
    private String urlImageApod;
    //Para almacenar la url del video seleccionado
    private String urlVideo;
    //Bandaera para validar si es un video o una imagen
    private boolean isVideo = false;
    //Día
    private int iDay;
    //mes
    private int iMonth;
    //año
    private int iYear;
    //Para obtener la fecha actual
    private Calendar calendar;
    //Para establecer el formato de fecha del endpoint
    private StringBuilder date;
    //Para usar la base de datos
    private ApodDataSource apodDataSource;
    //Para guardar el modelo en la tabla apod de la base de datos "favoritos"
    private Apod modelApod;
    //Para validar la conexión a internet
    private ConnectionUtil connection;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Inflamos la vista
        View view = inflater.inflate(R.layout.fragment_apod,container,false);
        //Acceso a controles del fragment
        ButterKnife.bind(this,view);

        //Creamos la instancia para acceder a la tabla
        apodDataSource = new ApodDataSource(getActivity());
        //Creamos la instancia para validar la conexión
        connection = new ConnectionUtil(getActivity());

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /*********************************************************************/
        /* El parámetro date del endpoint debe tener el formato YYYY-MM-DD   */
        /* Por ello, agregamos un cero a la izquierda tanto del número de día*/
        /* como del mes actual, en caso de ser menor a 10.                    */
        /*********************************************************************/
        //Obtenemos una instancia del calendario (la fecha actual)
        calendar = Calendar.getInstance();
        iYear = calendar.get(Calendar.YEAR);
        iMonth = calendar.get(Calendar.MONTH) + 1;
        iDay = calendar.get(Calendar.DAY_OF_MONTH);

        //Ponemos el día, el mes y el año actual por default (YYYY-MM-DD)
        date = new StringBuilder();
        date.append(iYear).append("-");
        if(iMonth < 10) date.append("0");
        date.append(iMonth).append("-");
        if(iDay < 10) date.append("0");
        date.append(iDay);
        /***************************************************************/

        //Instanciamos el servicio apod para poder usar Retrofit
        apodService = Data.getRetrofitInstance().create(ApodService.class);

        if(apodService != null) {
            //LLamamos al método que establece su callback
            apodServiceEnqueue(apodService);
        }
    }

    private void apodServiceEnqueue(ApodService apodService) {

        //Validamos la conexión a internet
        if(!connection.isConnected()){
            Toast.makeText(getActivity(),getString(R.string.connectionRequired), Toast.LENGTH_LONG).show();
            return;
        }

        //Consultamos el elemento Apod del día específico
        Call<Apod> callApodService = apodService.getTodayApodWithAllQuery(
                date.toString(), BuildConfig.NASA_API_KEY);

        //Definimos su callback
        callApodService.enqueue(new Callback<Apod>() {
            @Override
            public void onResponse(Call<Apod> call, Response<Apod> response) {
                //Validando posibles respuestas del webservice
                if(response != null && (response.code() != 500 || response.code() != 500 || response.body() != null)) {
                    //Asignamos la información del Apod recibido a los controles y variables de clase
                    if (response.body().getMediaType().equals("image")) { //Si es imagen
                        //Url de la foto
                        urlImageApod = response.body().getUrl();
                        Picasso.with(getActivity()).load(response.body().getUrl()).into(imageApod);
                        isVideo = false;
                    } else { //Si es video
                        urlVideo = response.body().getUrl();
                        imageApod.setImageResource(R.drawable.play);
                        isVideo = true;
                    }

                    tvDate.setText(response.body().getDate());
                    tvTitle.setText(response.body().getTitle());
                    tvExplanation.setText(response.body().getExplanation());

                    //No siempre viene con copyright
                    String copyright = TextUtils.isEmpty(response.body().getCopyright()) ? "" :
                            response.body().getCopyright();
                    tvCopyright.setText(copyright);

                    //Construimos el objeto apod por si el usuario decide agregarlo a favoritos
                    modelApod = new Apod();
                    modelApod.setCopyright(copyright);
                    modelApod.setDate(response.body().getDate());
                    modelApod.setExplanation(response.body().getExplanation());
                    modelApod.setHdurl(response.body().getHdurl());
                    modelApod.setMediaType(response.body().getMediaType());
                    modelApod.setServiceVersion(response.body().getServiceVersion());
                    modelApod.setTitle(response.body().getTitle());
                    modelApod.setUrl(response.body().getUrl());
                }
                else{
                    Toast.makeText(getActivity(),getString(R.string.fragApod_msgNoInformation), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<Apod> call, Throwable t) {

            }
        });
    }

    //region Menú
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.apod_menu,menu);
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
            case R.id.menu_shareTodayApod:
                //Compartimos la url de la imagen seleccionada
                shareText(urlImageApod);
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
        startActivity(Intent.createChooser(shareIntent, getString(R.string.fragments_msgShare)));
    }
    //endregion

    //region Clicks de los controles
    @OnClick(R.id.fragApod_btnSelectDate)
    public void onClickBtnSelectDate(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                android.R.style.Theme_DeviceDefault_Light_Panel,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        //Le damos el formato requerido a la fecha seleccionada
                        date = new StringBuilder();
                        date.append(year).append("-");
                        if((monthOfYear+1) < 10) date.append("0");
                        date.append(monthOfYear+1).append("-");
                        if(dayOfMonth < 10) date.append("0");
                        date.append(dayOfMonth);

                        //LLamamos al método que establece su callback
                        apodServiceEnqueue(apodService);
                    }
                }, iYear, iMonth, iDay);
        datePickerDialog.show();
        //datePickerDialog.setTitle(getString(R.string.fragApod_msgSelectDate));
        //Sólo puede seleccionar hasta la fecha actual
        datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());

        //Le restamos 16 años a la fecha actual para ponerla como fecha mínima de selección
        Calendar calendarTemp = Calendar.getInstance();
        calendarTemp.add(calendar.YEAR,-16);
        datePickerDialog.getDatePicker().setMinDate(calendarTemp.getTimeInMillis());
    }

    @OnClick(R.id.fragApod_image)
    public void onClickImage(){
        //Si es un video, le damos al usuario la opción de verlo
        if(isVideo){
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse(urlVideo));
            getActivity().startActivity(intent);
        }
    }

    /*El click en la imagen sirve para agregar el objeto Apod a favoritos*/
    @OnLongClick(R.id.fragApod_image)
    public boolean onLongClickImage(){

        //Si es un video no lo puede poner en favoritos
        if(isVideo){ return true; }

        //Si no existe la foto en la lista de favoritos, se puede agregar
        if(apodDataSource.getApod(modelApod.getTitle(),modelApod.getDate()) == null) {
            new AlertDialog.Builder(getActivity())
                    .setTitle(getString(R.string.fragments_msgAddToFavorites))
                    .setMessage(String.format(getString(R.string.fragments_msgQuestionAdd), modelApod.getTitle()))
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Guardamos el objeto Apod en la base de datos
                            long result = apodDataSource.saveApod(modelApod);
                            if(result != -1) {
                                Toast.makeText(getActivity(), getString(R.string.fragments_msgSuccessfullyAdded),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }).setCancelable(false).create().show();
        }
        else{
            Toast.makeText(getActivity(), getString(R.string.fragments_msgAlreadyExist),Toast.LENGTH_SHORT).show();
        }
        return true;
    }
    //endregion
}