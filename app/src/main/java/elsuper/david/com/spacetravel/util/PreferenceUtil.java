package elsuper.david.com.spacetravel.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import elsuper.david.com.spacetravel.model.Preference;

/**
 * Created by Andrés David García Gómez
 */
public class PreferenceUtil {

    private  static final String FILE_NAME = "space_travel_preferences";
    private final SharedPreferences sp;

    public PreferenceUtil(Context context) {
        sp = context.getSharedPreferences(FILE_NAME, context.MODE_PRIVATE);
    }

    public boolean savePreference(Preference modelPreference){
        if(modelPreference != null){
            sp.edit().putInt("pref_numSol", modelPreference.getNumSol()).apply();
            return true;
        }
        else
            return false;
    }

    public Preference getPreference(){
        int numSol = sp.getInt("pref_numSol",0);

        if(numSol == 0)
            return  null;

        return new Preference(numSol);
    }
}
