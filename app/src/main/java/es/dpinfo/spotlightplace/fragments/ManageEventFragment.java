package es.dpinfo.spotlightplace.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import es.dpinfo.spotlightplace.R;
import es.dpinfo.spotlightplace.interfaces.IManageEventMvp;
import es.dpinfo.spotlightplace.models.SpotPlace;
import es.dpinfo.spotlightplace.preferences.AccountPreferences;
import es.dpinfo.spotlightplace.presenters.ManageEventPresenter;
import es.dpinfo.spotlightplace.presenters.PlacesListPresenter;
import es.dpinfo.spotlightplace.repository.ApiDAO;

import com.google.android.gms.location.places.Place;

/**
 * Created by dprimenko on 3/01/17.
 */
public class ManageEventFragment extends Fragment implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener, IManageEventMvp.View, ApiDAO.UploadPlaceListener{

    private ManageEventPresenter presenter;

    private CoordinatorLayout clManageEvent;
    private PlacesListPresenter.ActionsFragmentListener mCallback;
    private FloatingActionButton fab;
    private Spinner categoriesSpinner;
    private Toolbar toolbarManageEvent;

    private RelativeLayout rlDateTimeFromManageEvent;
    private RelativeLayout rlDateTimeToManageEvent;

    private TimePickerDialog timePickerFrom;
    private TimePickerDialog timePickerTo;
    private DatePickerDialog datePickerFrom;
    private DatePickerDialog datePickerTo;

    private SimpleDateFormat formatterIn;
    private SimpleDateFormat formatterOut;
    private SimpleDateFormat formatterTime;
    private SimpleDateFormat formatterFullTime;

    private boolean fromSelected;

    private DateTimeZone timeZone;
    private DateTimeFormatter formatterSelectedDatetime;

    private String dateSelectedFrom;
    private String dateSelectedTo;
    private String timeSelectedFrom;
    private String timeSelectedTo;

    private TextView txvTimeFrom, txvDateFrom, txvTimeTo, txvDateTo;
    private EditText edtTitle, edtAddress, edtDescription;
    private String selectedMapMark;

    private ProgressDialog pd;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public static ManageEventFragment newInstance(Bundle bundle) {

        ManageEventFragment fragment = new ManageEventFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (PlacesListPresenter.ActionsFragmentListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement PlacesListPresenter.ActionsFragmentListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_manage_event, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        View rootView = view;

        if (rootView != null) {

            setupWidgets(rootView);
        }
    }

    private void setupWidgets(View rootView) {

        selectedMapMark = "";

        presenter = new ManageEventPresenter(this);

        fromSelected = false;
        formatterIn = new SimpleDateFormat("yyyy-MM-dd");
        formatterOut = new SimpleDateFormat("dd MMMM yyyy");
        formatterTime = new SimpleDateFormat("HH:mm");
        formatterFullTime = new SimpleDateFormat("HH:mm");
        //String.format("%s %s", formatterIn, formatterFullTime)
        formatterSelectedDatetime = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
        timeZone = DateTimeZone.getDefault();

        clManageEvent = (CoordinatorLayout) rootView.findViewById(R.id.cl_manage_event);
        toolbarManageEvent = (Toolbar) rootView.findViewById(R.id.toolbar_manage_event);
        fab = (FloatingActionButton) rootView.findViewById(R.id.fab_test);
        categoriesSpinner = (Spinner) rootView.findViewById(R.id.spinner_categories_manage_event);
        txvTimeFrom = (TextView) rootView.findViewById(R.id.txv_time_from_manage_event);
        txvTimeTo = (TextView) rootView.findViewById(R.id.txv_time_to_manage_event);
        txvDateFrom = (TextView) rootView.findViewById(R.id.txv_date_from_manage_event);
        txvDateTo = (TextView) rootView.findViewById(R.id.txv_date_to_manage_event);
        rlDateTimeFromManageEvent = (RelativeLayout) rootView.findViewById(R.id.rl_datetime_from_manage_event);
        rlDateTimeToManageEvent = (RelativeLayout) rootView.findViewById(R.id.rl_datetime_to_manage_event);

        Calendar now = Calendar.getInstance();
        TimeZone defaultTz = TimeZone.getDefault();
        now.setTimeZone(defaultTz);

        txvDateFrom.setText(formatterOut.format(now.getTime()));
        txvDateTo.setText(formatterOut.format(now.getTime()));
        txvTimeFrom.setText(String.valueOf(formatterTime.format(now.getTime())));
        txvTimeTo.setText(String.valueOf(formatterTime.format(now.getTime())));

        edtTitle = (EditText) rootView.findViewById(R.id.edt_title_manage_event);
        edtAddress = (EditText) rootView.findViewById(R.id.edt_address_manage_event);
        edtDescription = (EditText) rootView.findViewById(R.id.edt_description_manageEvent);

        setDefaultDatetime(now);

        datePickerFrom = DatePickerDialog.newInstance(
            ManageEventFragment.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );

        datePickerTo = DatePickerDialog.newInstance(
                ManageEventFragment.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );

        timePickerFrom = TimePickerDialog.newInstance(
                ManageEventFragment.this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                now.get(Calendar.SECOND),
                true
        );

        timePickerTo = TimePickerDialog.newInstance(
                ManageEventFragment.this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                now.get(Calendar.SECOND),
                true
        );

        datePickerFrom.setMinDate(now);
        datePickerTo.setMinDate(now);

        loadMenuManageEvent();

        categoriesSpinner.setAdapter(new ArrayAdapter(getActivity(),R.layout.support_simple_spinner_dropdown_item, getResources().getStringArray(R.array.array_categories)));
        categoriesSpinner.setSelection(3);

        rlDateTimeFromManageEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerFrom.show(getActivity().getFragmentManager(), "DatePickerFrom");
            }
        });

        rlDateTimeToManageEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerTo.show(getActivity().getFragmentManager(), "DatePickerTo");
            }
        });


        edtAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                Intent intent;

                try {
                    intent = builder.build(getActivity());
                    startActivityForResult(intent, 1);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == getActivity().RESULT_OK) {
                Place place = PlacePicker.getPlace(data, getActivity());
                selectedMapMark = place.getLatLng().latitude + "," + place.getLatLng().longitude;
                edtAddress.setText(place.getAddress());
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        Date date = null;

        try {
            date = formatterIn.parse(String.format("%s-%s-%s", String.valueOf(year), String.valueOf((monthOfYear + 1)), String.valueOf(dayOfMonth)));

            if (view.getTag().equals("DatePickerFrom")) {
                fromSelected = true;
                txvDateFrom.setText(formatterOut.format(date));
                dateSelectedFrom = formatterIn.format(date);
                timePickerFrom.show(getActivity().getFragmentManager(), "TimePickerFrom");

            } else if(view.getTag().equals("DatePickerTo")) {
                fromSelected = false;
                txvDateTo.setText(formatterOut.format(date));
                dateSelectedTo = formatterIn.format(date);
                timePickerTo.show(getActivity().getFragmentManager(), "TimePickerTo");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        Date time = null;
        Date timeCompact = null;
        try {
            time = formatterTime.parse(String.valueOf(hourOfDay) + ":" + String.valueOf(minute) + ":" + String.valueOf(second));
            timeCompact = formatterTime.parse(String.valueOf(hourOfDay) + ":" + String.valueOf(minute));
            if (fromSelected) {
                txvTimeFrom.setText(formatterTime.format(timeCompact));
                timeSelectedFrom = formatterTime.format(timeCompact);
            } else {
                txvTimeTo.setText(formatterTime.format(timeCompact));
                timeSelectedTo = formatterTime.format(timeCompact);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void setDefaultDatetime(Calendar now) {
        Date dateTmp = null;

        try {
            dateTmp = formatterOut.parse(txvDateFrom.getText().toString());
            dateSelectedFrom = formatterIn.format(dateTmp);
            dateTmp = formatterOut.parse(txvDateTo.getText().toString());
            dateSelectedTo = formatterIn.format(dateTmp);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        timeSelectedFrom = formatterTime.format(now.getTime());
        timeSelectedTo = formatterTime.format(now.getTime());
    }

    @Override
    public void onUploadPlaceSuccess(JSONObject response) {
        pd.dismiss();
        mCallback.onMainFragment();
    }

    @Override
    public void onUploadPlaceError(String error) {
        Log.d("ErrorUpload", error);
        pd.dismiss();
    }

    @Override
    public void setMessageError(int messageError) {
        Snackbar.make(clManageEvent, getString(messageError), Snackbar.LENGTH_LONG).show();
    }

    private void loadMenuManageEvent() {
        toolbarManageEvent.inflateMenu(R.menu.menu_manage_event);
        toolbarManageEvent.setNavigationIcon(R.drawable.arrow_left);
        toolbarManageEvent.setTitle("");

        toolbarManageEvent.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if (item.getItemId() == R.id.action_manage_event)  {

                    DateTime dateTimeFrom = formatterSelectedDatetime.parseDateTime(String.format("%s %s", dateSelectedFrom, timeSelectedFrom));
                    String iso8601 = dateTimeFrom.toString();
                    DateTime finalDateTimeFrom = new DateTime(iso8601, timeZone);

                    DateTime dateTimeTo = formatterSelectedDatetime.parseDateTime(String.format("%s %s", dateSelectedTo, timeSelectedTo));
                    iso8601 = dateTimeTo.toString();
                    DateTime finalDateTimeTo = new DateTime(iso8601, timeZone);

                    SpotPlace place = new SpotPlace();

                    place.setmCreatorId(AccountPreferences.getInstance(getActivity()).getId());
                    place.setmTitle(edtTitle.getText().toString());
                    place.setmImg("http://xpenology.org/wp-content/themes/qaengine/img/default-thumbnail.jpg");
                    place.setmAddress(selectedMapMark);
                    place.setmDescription(edtDescription.getText().toString());
                    place.setmCategory(categoriesSpinner.getSelectedItem().toString());
                    place.setmDateTimeFrom(finalDateTimeFrom.toString());
                    place.setmDateTimeTo(finalDateTimeTo.toString());
                    place.setmUsersIn(new ArrayList<String>());


                    if (presenter.validateFields(place)) {
                        presenter.uploadPlace(ManageEventFragment.this, place);
                        pd = new ProgressDialog(getActivity());
                        pd.setMessage(getResources().getString(R.string.upload_message));
                        pd.show();
                    }
                }
                return true;
            }
        });

        toolbarManageEvent.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onMainFragment();
            }
        });
    }
}
