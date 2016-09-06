package elsuper.david.com.spacetravel.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Andrés David García Gómez.
 */
public class ConnectionUtil {

    private Context context;

    public ConnectionUtil(Context context) {
        this.context = context;
    }

    //Valida si hay conexión a internet
    public Boolean isConnected(){
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null) {
                if (info.isConnected()) {
                    return true;
                }
            }
        }
        return false;
    }
}
