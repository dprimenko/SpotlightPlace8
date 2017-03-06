package es.dpinfo.spotlightplace.fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dpinfo.spotlightplace.R;
import es.dpinfo.spotlightplace.SpotlightApplication;
import es.dpinfo.spotlightplace.adapters.SimplePlacesCursorAdapter;
import es.dpinfo.spotlightplace.interfaces.IListPresenter;
import es.dpinfo.spotlightplace.models.SpotPlace;
import es.dpinfo.spotlightplace.preferences.AccountPreferences;
import es.dpinfo.spotlightplace.presenters.ListPresenterImpl;
import es.dpinfo.spotlightplace.presenters.PlacesListPresenter;
import es.dpinfo.spotlightplace.receivers.PlaceDeletedReceiver;

/**
 * Created by dprimenko on 29/01/17.
 */
public class ProfileFragment extends Fragment implements IListPresenter.View {

    private CircleImageView imvProfileImg;
    private TextView txvUserFullName;
    private PlacesListPresenter.ActionsFragmentListener mCallback;
    private ProfileFragmentListener profileFragmentListener;
    private ListView lwPastPlaces;
    private SimplePlacesCursorAdapter adapter;
    private ListPresenterImpl presenter;


    public interface ProfileFragmentListener {
        void onEditProfileFragment();
    }

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    public ProfileFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        presenter = new ListPresenterImpl(this, IListPresenter.PLACE);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (PlacesListPresenter.ActionsFragmentListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement PlacesListPresenter.ActionsFragmentListener");
        }

        try {
            profileFragmentListener = (ProfileFragmentListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement ProfileFragmentListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imvProfileImg = (CircleImageView) view.findViewById(R.id.imv_profile_img);
        txvUserFullName = (TextView) view.findViewById(R.id.txv_user_fullname);
        lwPastPlaces = (ListView) view.findViewById(R.id.lw_past_places);
        setProfileInfo();

        adapter = new SimplePlacesCursorAdapter();
        lwPastPlaces.setAdapter(adapter);

        registerForContextMenu(lwPastPlaces);

        Bundle args = new Bundle();
        args.putString("selection", "creator=?");
        args.putStringArray("selectionArgs", new String[]{AccountPreferences.getInstance(getActivity()).getId()});
        presenter.getAllFields(args);
    }



    private void setProfileInfo() {

        AccountPreferences preferences = AccountPreferences.getInstance(getActivity());

        ((AppCompatActivity)getActivity()).getSupportActionBar().setIcon(null);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_left);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(preferences.getUsername());
        Picasso.with(getActivity()).load(preferences.getProfileImg()).into(imvProfileImg);
        txvUserFullName.setText(preferences.getFullName());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.menu_profile, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_edit_profile) {
            profileFragmentListener.onEditProfileFragment();
        } else if(item.getItemId() == android.R.id.home) {
            mCallback.onMainFragment();
        }

        return true;
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        String titlePlace = ((SpotPlace)adapter.getItem(info.position)).getmTitle();
        Log.d("Title", titlePlace);

        if (item.getItemId() == R.id.action_delete_place) {
            presenter.deletePlaceLocal(Integer.parseInt(((SpotPlace)adapter.getItem(info.position)).getmId()));

            Intent intent = new Intent();
            intent.setAction(PlaceDeletedReceiver.ACTION);
            intent.putExtra("place", titlePlace);

            SpotlightApplication.getContext().sendBroadcast(intent);
        }

        return true;
    }

    @Override
    public CursorAdapter getCursorAdapter() {
        return adapter;
    }

    @Override
    public void setCursor(Cursor cursor) {
        if (cursor != null) {
            adapter.swapCursor(cursor);
        }
    }
}
