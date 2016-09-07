package elsuper.david.com.spacetravel.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import elsuper.david.com.spacetravel.R;
import elsuper.david.com.spacetravel.model.Preference;
import elsuper.david.com.spacetravel.util.PreferenceUtil;

import static elsuper.david.com.spacetravel.R.string.fragConfig_msgRequired;

/**
 * Created by Andrés David García Gómez.
 */
public class FragmentConfigurations extends Fragment{

    //Controles del fragment
    @BindView(R.id.fragConfig_tvCurrentValue) TextView tvCurrentValue;
    @BindView(R.id.fragConfig_chkRandom) CheckBox chkRandom;
    @BindView(R.id.fragConfig_linearHide) LinearLayout linearHide;
    @BindView(R.id.fragConfig_etNewSol) EditText etNewSol;
    @BindView(R.id.fragConfig_btnApply) Button btnApply;

    //Para guardar o leer el parámetro "sol" de las preferencias
    private PreferenceUtil preferenceUtil;
    //Número máximo "sol" al 26/08/2016
    private static final int solNumberMax = 1388;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Inflamos la vista
        View view = inflater.inflate(R.layout.fragment_configurations,container,false);
        //Acceso a controles del fragment
        ButterKnife.bind(this,view);
        //Instanciamos el objeto de las preferencias
        preferenceUtil = new PreferenceUtil(getActivity());

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Preference preference = preferenceUtil.getPreference();
        if(preference != null) {
            tvCurrentValue.setText(String.valueOf(preference.getNumSol()));
            chkRandom.setChecked(false);
            linearHide.setVisibility(View.VISIBLE);
        }
        else {
            tvCurrentValue.setText(getString(R.string.fragConfig_msgRandom));
            chkRandom.setChecked(true);
            linearHide.setVisibility(View.GONE);
        }
    }

    //region Clicks de los controles
    @OnCheckedChanged(R.id.fragConfig_chkRandom)
    public void onCheckedChange(){
        if(chkRandom.isChecked()) {
            linearHide.setVisibility(View.GONE);
        }
        else{
            linearHide.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.fragConfig_btnApply)
    public void onClick(){

        Boolean success = false;

        if(chkRandom.isChecked()) {
            success = preferenceUtil.savePreference(new Preference(0));
        }
        else{
            //Primero validamos que el usuario haya puesto algo en la caja de texto
            if(TextUtils.isEmpty(etNewSol.getText().toString())){
                Toast.makeText(getActivity(),getText(R.string.fragConfig_msgRequired),Toast.LENGTH_LONG).show();
                return;
            }

            int newSol = Integer.parseInt(etNewSol.getText().toString());
            if(newSol > 0 && newSol <= solNumberMax){
                success = preferenceUtil.savePreference(new Preference(newSol));
            }
            else{
                Toast.makeText(getActivity(),R.string.fragConfig_msgRange,Toast.LENGTH_LONG).show();
                return;
            }
        }

        //Si es exitoso el cambio, redirigimos al fragment del listado con el nuevo valor de "sol"
        if(success) {

            Toast.makeText(getActivity(),R.string.fragConfig_msgSaved,Toast.LENGTH_LONG).show();

            //Si quedaron guardados los cambios
            getFragmentManager().beginTransaction().replace(R.id.listNav_FragmentFolder, new FragmentListing()).commit();
            Snackbar.make(getActivity().findViewById(android.R.id.content),
                    getString(R.string.listingNavigationMenu_images), Snackbar.LENGTH_SHORT).show();
        }

    }
    //endregion
}
