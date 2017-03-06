package es.dpinfo.spotlightplace.adapters;

import android.content.Context;
import android.database.Cursor;
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
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dpinfo.spotlightplace.R;
import es.dpinfo.spotlightplace.SpotlightApplication;
import es.dpinfo.spotlightplace.db.DatabaseContract;
import es.dpinfo.spotlightplace.interfaces.IListPresenter;
import es.dpinfo.spotlightplace.models.SpotPlace;
import es.dpinfo.spotlightplace.repository.ApiDAO;

/**
 * Created by dprimenko on 2/03/17.
 */
public class PlacesCursorAdapter extends CursorAdapter implements ApiDAO.GmapsRequestStatus {

    public PlacesCursorAdapter() {
        super(SpotlightApplication.getContext(), null, IListPresenter.PLACE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_place, parent, false);

        PlaceHolder holder = new PlaceHolder();

        holder.cwItemPlace = (CardView) view.findViewById(R.id.cw_item_place);
        holder.imvItemPlacesList = (ImageView) view.findViewById(R.id.imv_item_places_list);
        holder.imvUserItemPlacesList = (CircleImageView) view.findViewById(R.id.imv_user_item_places_list);
        holder.txvTitleItemPlacesList = (TextView) view.findViewById(R.id.txv_title_item_places_list);
        holder.txvDescriptionItemPlacesList = (TextView) view.findViewById(R.id.txv_description_item_places_list);
        holder.txvAddressItemPlacesList = (TextView) view.findViewById(R.id.txv_address_item_places_list);
        holder.txvUserItemPlacesList = (TextView) view.findViewById(R.id.txv_user_item_places_list);
        holder.txvUsersInPlacesList = (TextView) view.findViewById(R.id.txv_users_in_item_places_list);
        holder.fabLocation = (FloatingActionButton) view.findViewById(R.id.fab_location_item_event_list);
        holder.fabViewMore = (FloatingActionButton) view.findViewById(R.id.fab_view_more_item_event_list);

        view.setTag(holder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        PlaceHolder holder = (PlaceHolder) view.getTag();

        holder.fabViewMore.setVisibility(View.VISIBLE);

        if (checkDateTime(cursor.getString(cursor.getColumnIndex(DatabaseContract.Places.COL_DATETIME_FROM)), cursor.getString(cursor.getColumnIndex(DatabaseContract.Places.COL_DATETIME_TO)))){
            holder.fabLocation.setVisibility(View.VISIBLE);
        }

        Picasso.with(context)
                .load(cursor.getString(cursor.getColumnIndex(DatabaseContract.Places.COL_IMG)))
                .into(holder.imvItemPlacesList);
        Picasso.with(context)
                .load("https://app.nimia.com/static/img/default_profile.png")
                .into(holder.imvUserItemPlacesList);
        holder.txvTitleItemPlacesList.setText(cursor.getString(cursor.getColumnIndex(DatabaseContract.Places.COL_TITLE)));
        holder.txvDescriptionItemPlacesList.setText(cursor.getString(cursor.getColumnIndex(DatabaseContract.Places.COL_DESCRIPTION)));
        holder.txvAddressItemPlacesList.setText(cursor.getString(cursor.getColumnIndex(DatabaseContract.Places.COL_ADDRESS)));
        holder.txvUsersInPlacesList.setText(String.valueOf(cursor.getString(cursor.getColumnIndex(DatabaseContract.Places.COL_USERS_IN))));
    }

    @Override
    public Object getItem(int position) {

        getCursor().moveToPosition(position);

        SpotPlace place = new SpotPlace();

        place.setmId(String.valueOf(getCursor().getInt(getCursor().getColumnIndex(DatabaseContract.Places._ID))));
        place.setmCreatorId(getCursor().getString(getCursor().getColumnIndex(DatabaseContract.Places.COL_CREATOR)));
        place.setmTitle(getCursor().getString(getCursor().getColumnIndex(DatabaseContract.Places.COL_CREATOR)));
        place.setmImg(getCursor().getString(getCursor().getColumnIndex(DatabaseContract.Places.COL_CREATOR)));
        place.setmAddress(getCursor().getString(getCursor().getColumnIndex(DatabaseContract.Places.COL_CREATOR)));
        place.setmDescription(getCursor().getString(getCursor().getColumnIndex(DatabaseContract.Places.COL_CREATOR)));
        place.setmCategory(getCursor().getString(getCursor().getColumnIndex(DatabaseContract.Places.COL_CREATOR)));
        place.setmDateTimeFrom(getCursor().getString(getCursor().getColumnIndex(DatabaseContract.Places.COL_CREATOR)));
        place.setmDateTimeTo(getCursor().getString(getCursor().getColumnIndex(DatabaseContract.Places.COL_CREATOR)));
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

    }

    @Override
    public void onGmapsRequestErrorResponse(CharSequence messageError) {

    }

    class PlaceHolder {
        CardView cwItemPlace;
        ImageView imvItemPlacesList;
        CircleImageView imvUserItemPlacesList;
        TextView txvTitleItemPlacesList, txvDescriptionItemPlacesList, txvAddressItemPlacesList, txvUserItemPlacesList, txvUsersInPlacesList;
        FloatingActionButton fabLocation, fabViewMore;
    }
}
