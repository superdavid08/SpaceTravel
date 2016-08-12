package elsuper.david.com.spacetravel.app;

import android.app.Application;
import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by Andrés David García Gómez
 */
public class NasaApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
    }
}
