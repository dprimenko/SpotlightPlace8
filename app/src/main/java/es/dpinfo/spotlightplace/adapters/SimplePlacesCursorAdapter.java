package es.dpinfo.spotlightplace.adapters;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dpinfo.spotlightplace.R;
import es.dpinfo.spotlightplace.SpotlightApplication;
import es.dpinfo.spotlightplace.db.DatabaseContract;
import es.dpinfo.spotlightplace.interfaces.IListPresenter;
import es.dpinfo.spotlightplace.models.SpotPlace;
import es.dpinfo.spotlightplace.preferences.AccountPreferences;
import es.dpinfo.spotlightplace.repository.ApiDAO;

/**
 * Created by dprimenko on 2/03/17.
 */
public class SimplePlacesCursorAdapter extends CursorAdapter implements ApiDAO.GmapsRequestStatus {

    private PlaceHolder holder;

    public SimplePlacesCursorAdapter() {
        super(SpotlightApplication.getContext(), null, IListPresenter.PLACE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.simple_item_place, parent, false);

        holder = new PlaceHolder();

        holder.imvItemPlacesList = (ImageView) view.findViewById(R.id.imv_item_places_list_simple);
        holder.txvTitleItemPlacesList = (TextView) view.findViewById(R.id.txv_title_item_places_list_simple);
        holder.txvDescriptionItemPlacesList = (TextView) view.findViewById(R.id.txv_description_item_places_list_simple);
        holder.txvAddressItemPlacesList = (TextView) view.findViewById(R.id.txv_address_item_places_list_simple);

        view.setTag(holder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        holder = (PlaceHolder) view.getTag();
        Picasso.with(context).load(cursor.getString(cursor.getColumnIndex(DatabaseContract.Places.COL_IMG))).into(holder.imvItemPlacesList);
        holder.txvTitleItemPlacesList.setText(cursor.getString(cursor.getColumnIndex(DatabaseContract.Places.COL_TITLE)));
        holder.txvDescriptionItemPlacesList.setText(cursor.getString(cursor.getColumnIndex(DatabaseContract.Places.COL_DESCRIPTION)));
        ApiDAO.getInstance().setAddressFormatted(this, cursor.getString(cursor.getColumnIndex(DatabaseContract.Places.COL_ADDRESS)));

    }

    @Override
    public Object getItem(int position) {

        getCursor().moveToPosition(position);

        SpotPlace place = new SpotPlace();

        place.setmId(String.valueOf(getCursor().getInt(getCursor().getColumnIndex(DatabaseContract.Places._ID))));
        place.setmCreatorId(getCursor().getString(getCursor().getColumnIndex(DatabaseContract.Places.COL_CREATOR)));
        place.setmTitle(getCursor().getString(getCursor().getColumnIndex(DatabaseContract.Places.COL_TITLE)));
        place.setmImg(getCursor().getString(getCursor().getColumnIndex(DatabaseContract.Places.COL_IMG)));
        place.setmAddress(getCursor().getString(getCursor().getColumnIndex(DatabaseContract.Places.COL_ADDRESS)));
        place.setmDescription(getCursor().getString(getCursor().getColumnIndex(DatabaseContract.Places.COL_DESCRIPTION)));
        place.setmCategory(getCursor().getString(getCursor().getColumnIndex(DatabaseContract.Places.COL_CATEGORY)));
        place.setmDateTimeFrom(getCursor().getString(getCursor().getColumnIndex(DatabaseContract.Places.COL_DATETIME_FROM)));
        place.setmDateTimeTo(getCursor().getString(getCursor().getColumnIndex(DatabaseContract.Places.COL_DATETIME_TO)));
        place.setmUsersInInt(getCursor().getInt(getCursor().getColumnIndex(DatabaseContract.Places.COL_USERS_IN)));

        return place;
    }

    public boolean checkDateTime(String from, String to) {
        boolean result = false;

        DateTimeFormatter parser = ISODateTimeFormat.dateTime();
        DateTime dateFrom = parser.parseDateTime(from);
        DateTime dateTo = parser.parseDateTime(to);

        if (dateFrom.isBeforeNow() && dateTo.isAfterNow()) {
            result = true;
        }

        return result;
    }

    @Override
    public void onGmapsRequestResponseSuccess(JSONObject response) {
        try {
            holder.txvAddressItemPlacesList.setText(response.getJSONArray("results").getJSONObject(0).getString("formatted_address"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onGmapsRequestErrorResponse(CharSequence messageError) {
        holder.txvAddressItemPlacesList.setText(SpotlightApplication.getContext().getResources().getText(R.string.error_address));
    }

    class PlaceHolder {
        ImageView imvItemPlacesList;
        TextView txvTitleItemPlacesList, txvDescriptionItemPlacesList, txvAddressItemPlacesList;
    }
}
