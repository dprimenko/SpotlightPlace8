package es.dpinfo.spotlightplace.presenters;

import android.content.ContentValues;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import java.security.Provider;

import es.dpinfo.spotlightplace.R;
import es.dpinfo.spotlightplace.SpotlightApplication;
import es.dpinfo.spotlightplace.interfaces.IManageEventMvp;
import es.dpinfo.spotlightplace.models.SpotPlace;
import es.dpinfo.spotlightplace.provider.SpotlightContractProvider;
import es.dpinfo.spotlightplace.repository.ApiDAO;

/**
 * Created by dprimenko on 27/01/17.
 */
public class ManageEventPresenter implements IManageEventMvp.Presenter {

    private IManageEventMvp.View view;

    public ManageEventPresenter(IManageEventMvp.View view) {
        this.view = view;
    }

    @Override
    public boolean validateFields(SpotPlace place) {

        boolean result = false;

        if (TextUtils.isEmpty(place.getmTitle())) {
            view.setMessageError(R.string.data_empty);
        } else {
            result = true;
        }

        return result;
    }

    @Override
    public void uploadPlace(Fragment fragment, SpotPlace spotPlace) {
        ApiDAO.getInstance().uploadPlace(fragment, spotPlace);

        ContentValues values = new ContentValues();

        values.put("creator", spotPlace.getmCreatorId());
        values.put("title", spotPlace.getmTitle());
        values.put("img", spotPlace.getmImg());
        values.put("address", spotPlace.getmAddress());
        values.put("description", spotPlace.getmDescription());
        values.put("category", 1);
        values.put("datetime_from", spotPlace.getmDateTimeFrom());
        values.put("datetime_to", spotPlace.getmDateTimeTo());
        values.put("users_in", spotPlace.getmUsersInInt());

        SpotlightApplication.getContext().getContentResolver().insert(SpotlightContractProvider.Places.CONTENT_URI, values);
    }
}
