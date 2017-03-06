package es.dpinfo.spotlightplace.interfaces;

import es.dpinfo.spotlightplace.models.SpotPlace;

/**
 * Created by dprimenko on 2/03/17.
 */
public interface IListPlacesPresenter {

    interface View extends IListPresenter.View {
        void showMessageError(int error);
    }

    interface Presenter {
        void addPlace(SpotPlace place);
    }
}
