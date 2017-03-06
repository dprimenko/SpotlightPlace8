package es.dpinfo.spotlightplace.interfaces;

import android.support.v4.app.Fragment;

import es.dpinfo.spotlightplace.adapters.PlacesAdapter;
import es.dpinfo.spotlightplace.models.SpotPlace;

/**
 * Created by dprimenko on 18/1/17.
 */

public interface IPlaceListMvp {

    interface View {
        PlacesAdapter getAdapter();
        void setMessageError(int error);
    }

    interface Presenter {
        void requestPlaces(Fragment context);
        void addPlaceToAdapter(SpotPlace spotPlace);
    }
}
