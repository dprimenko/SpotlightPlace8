package es.dpinfo.spotlightplace;

import android.app.Application;
import android.content.Context;

/**
 * Created by dprimenko on 2/03/17.
 */
public class SpotlightApplication extends Application {

    private static SpotlightApplication singleton;

    public SpotlightApplication() {
        singleton = this;
    }

    public static Context getContext() {
        return singleton;
    }
}
