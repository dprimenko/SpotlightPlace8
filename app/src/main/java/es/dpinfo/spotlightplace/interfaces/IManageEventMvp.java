package es.dpinfo.spotlightplace.interfaces;

import android.support.v4.app.Fragment;

import es.dpinfo.spotlightplace.models.SpotPlace;

/**
 * Created by dprimenko on 27/01/17.
 */
public interface IManageEventMvp {

    interface View {
        void setMessageError(int messageError);
    }

    interface Presenter {
        boolean validateFields(SpotPlace place);
        void uploadPlace(Fragment fragment, SpotPlace spotPlace);

    }
}
