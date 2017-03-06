package es.dpinfo.spotlightplace.interfaces;

import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;

/**
 * Created by dprimenko on 2/03/17.
 */
public interface IListPresenter {

    int PLACE = 1;
    int CATEGORY = 3;

    interface View {
        CursorAdapter getCursorAdapter();
        void setCursor(Cursor cursor);
    }

    interface Presenter {
        Context getContext();

        void getAllFields(Bundle args);
        void restartLoader(CursorAdapter adapter);
        void deletePlaceLocal(int id);
    }
}
