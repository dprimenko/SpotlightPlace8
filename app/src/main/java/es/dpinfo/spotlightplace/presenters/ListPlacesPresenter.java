package es.dpinfo.spotlightplace.presenters;

import android.content.ContentValues;

import es.dpinfo.spotlightplace.interfaces.IListPlacesPresenter;
import es.dpinfo.spotlightplace.interfaces.IListPresenter;
import es.dpinfo.spotlightplace.models.SpotPlace;
import es.dpinfo.spotlightplace.provider.SpotlightContractProvider;

/**
 * Created by dprimenko on 2/03/17.
 */
public class ListPlacesPresenter extends ListPresenterImpl implements IListPlacesPresenter.Presenter {

    public ListPlacesPresenter(IListPresenter.View view) {
        super(view, IListPresenter.PLACE);
    }

    @Override
    public void addPlace(SpotPlace place) {
        getContext().getContentResolver().insert(SpotlightContractProvider.Places.CONTENT_URI, getContentValues(place));
    }

    private ContentValues getContentValues(SpotPlace place) {
        ContentValues values = new ContentValues();

        values.put(SpotlightContractProvider.Places.CREATOR, place.getmCreatorId());
        values.put(SpotlightContractProvider.Places.TITLE, place.getmTitle());
        values.put(SpotlightContractProvider.Places.IMG, place.getmImg());
        values.put(SpotlightContractProvider.Places.DESCRIPTION, place.getmDescription());
        values.put(SpotlightContractProvider.Places.ADDRESS, place.getmAddress());
        values.put(SpotlightContractProvider.Places.CATEGORY, place.getmCategory());
        values.put(SpotlightContractProvider.Places.DATETIME_FROM, place.getmDateTimeFrom());
        values.put(SpotlightContractProvider.Places.DATETIME_TO, place.getmDateTimeTo());
        values.put(SpotlightContractProvider.Places.USERS_IN, place.getmUsersInInt());

        return values;
    }


}
