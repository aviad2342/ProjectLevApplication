package app.projectortalapplication.viewComponents;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import app.projectortalapplication.R;
import app.projectortalapplication.core.Event;
import app.projectortalapplication.utils.Utils;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditEventFragment extends Fragment implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{

    Context context;
    Activity activity;
    Event eventToEdit;
    EditText eventSetTitle;
    EditText eventSetCity;
    EditText eventSetStreet;
    EditText eventSetHomeNumber;
    NumberPicker numberOfParticipants;
    EditText eventSetDescription;
    TextView eventSetDate;
    TextView eventSetTime;
    int publisherID;
    int eventID;
    Calendar myCalendar = Calendar.getInstance();
    ProgressDialog loading;
    LayoutInflater inflater;
    LayoutInflater mInflater;
    DatePicker datePicker;
    TimePicker timePicker;
    //AlertDialog.Builder builder;
    DatePickerDialog.Builder builder;
    TimePickerDialog.Builder TimeBuilder;
    AlertDialog eventDatePicker;
    AlertDialog eventTimePicker;
    LinearLayout streetAndNumber;

    public EditEventFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_add_new_event, container, false);
        context = view.getContext();
        activity = getActivity();
        eventSetTitle = (EditText) view.findViewById(R.id.eventSetTitle);
        eventSetCity = (EditText) view.findViewById(R.id.eventSetCity);
        eventSetStreet = (EditText) view.findViewById(R.id.eventSetStreet);
        eventSetHomeNumber = (EditText) view.findViewById(R.id.eventSetHomeNumber);
        eventSetDescription = (EditText) view.findViewById(R.id.eventSetDescription);
        streetAndNumber = (LinearLayout) view.findViewById(R.id.streetAndNumber);
        numberOfParticipants = (NumberPicker) view.findViewById(R.id.numberOfParticipants);
        numberOfParticipants.setMinValue(0);
        //Specify the maximum value/number of NumberPicker
        numberOfParticipants.setMaxValue(1000);

        //Gets whether the selector wheel wraps when reaching the min/max value.
        numberOfParticipants.setWrapSelectorWheel(true);

        eventSetDate = (TextView) view.findViewById(R.id.eventSetDate);
        eventSetTime = (TextView) view.findViewById(R.id.eventSetTime);
        streetAndNumber.setVisibility(View.GONE);
        eventSetCity.setHint(getString(R.string.event_edit_location_hint_label));

        Bundle args = getArguments();
        eventToEdit = (Event) args.getSerializable("mEventToEdit");

       // eventID = eventToEdit.getEventID();
        eventSetTitle.setText(eventToEdit.getTitle());
        eventSetCity.setText(eventToEdit.getLocation());
        eventSetDescription.setText(Html.fromHtml(eventToEdit.getDescription()));
        numberOfParticipants.setValue(eventToEdit.getCapacity());
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        eventSetDate.setText(format.format(eventToEdit.getPublishDate()));
        format = new SimpleDateFormat("HH:mm");
        eventSetTime.setText(format.format(eventToEdit.getPublishDate()));


        if(Utils.getInstance().loadMemberFromPrefs(context) != null){
            publisherID = Utils.getInstance().loadMemberFromPrefs(context).getMemberID();
        }


        Button btnAddEvent = (Button)view.findViewById(R.id.btnAddEvent);
        btnAddEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                if(validate(new EditText[]{eventSetTitle, eventSetCity, eventSetDescription}) && validateDate() && validateTime()){

                    //eventToEdit.setEventID(eventID);
                    eventToEdit.setTitle(eventSetTitle.getText().toString());
                    //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    eventToEdit.setPublishDate(myCalendar.getTime());
                    eventToEdit.setCapacity(numberOfParticipants.getValue());
                    eventToEdit.setLocation(eventSetCity.getText().toString());
                    eventToEdit.setDescription(eventSetDescription.getText().toString());
                    eventToEdit.setPublisherID(publisherID);
                    //eventToAdd.setImageCount();

                    loading = ProgressDialog.show(activity,getString(R.string.ui_register_progress_dialog_message),getString(R.string.ui_register_progress_dialog_title),false,false);

                    String url = Utils.EDIT_EVENT;

                    JsonObjectRequest request_json = new JsonObjectRequest(url, eventToEdit.toEditJsonObject(),
                            new com.android.volley.Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    loading.dismiss();
                                    Toast.makeText(activity,getString(R.string.event_edit_event_successfully_submit_form),Toast.LENGTH_SHORT).show();
                                    getFragmentManager().popBackStack();
                                }
                            }, new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            loading.dismiss();
                            Toast.makeText(activity,getString(R.string.event_edit_event_error_submit_form),Toast.LENGTH_SHORT).show();
                            getFragmentManager().popBackStack();
                        }
                    });
                    RequestQueue requestQueue = Volley.newRequestQueue(activity);
                    requestQueue.add(request_json);
                }

            }
        });

        Button btnCancelNewEvent = (Button)view.findViewById(R.id.btnCancelNewEvent);
        btnCancelNewEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });
        eventSetDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eventSetDate.setError(null);
                show();
            }
        });

        eventSetTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eventSetTime.setError(null);
                showTime();
            }
        });

        inflater = activity.getLayoutInflater();
        datePicker = (DatePicker) inflater.inflate(R.layout.my_date_picker,null);
        builder = new DatePickerDialog.Builder(activity);
        builder.setTitle(getString(R.string.event_date_picker_title));
        builder.setPositiveButton(getString(R.string.ui_register_date_picker_ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    myCalendar.set(Calendar.YEAR, datePicker.getYear());
                    myCalendar.set(Calendar.MONTH, datePicker.getMonth());
                    myCalendar.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
                    String myFormat = "dd/MM/yyyy"; //In which you need put here
                    SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
                    eventSetDate.setText(sdf.format(myCalendar.getTime()));
                }
            }
        });
        builder.setNegativeButton(getString(R.string.ui_register_date_picker_cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_NEGATIVE) {

                }
            }
        });
        builder.setView(datePicker);
        eventDatePicker = builder.create();

        timePicker = (TimePicker) inflater.inflate(R.layout.my_time_picker,null);
        timePicker.setIs24HourView(true);
        TimeBuilder = new TimePickerDialog.Builder(activity);
        TimeBuilder.setTitle(getString(R.string.event_time_picker_title));
        TimeBuilder.setPositiveButton(getString(R.string.ui_register_date_picker_ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    myCalendar.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
                    myCalendar.set(Calendar.MINUTE, timePicker.getMinute());
                    String myFormat = "HH:mm"; //In which you need put here
                    SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
                    eventSetTime.setText(sdf.format(myCalendar.getTime()));
                }
            }
        });
        TimeBuilder.setNegativeButton(getString(R.string.ui_register_date_picker_cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_NEGATIVE) {

                }
            }
        });
        TimeBuilder.setView(timePicker);
        eventTimePicker = TimeBuilder.create();
        return view;
    }

    /**
     * Show month year picker dialog.
     */
    public void show() {
        eventDatePicker.show();

    }

    public void showTime() {
        eventTimePicker.show();

    }

    private boolean validate(EditText[] fields){
        for(int i=0; i<fields.length; i++){
            if(fields[i].length()== 0){
                fields[i].setError("זהו שדה חובה!");
                fields[i].requestFocus();
                AlertDialog();
                return false;
            }
        }
        return true;
    }
    public void AlertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(R.string.event_error_validation_must_fill_all_fields);
        builder.setTitle(R.string.event_error_validation_alert_dialog_title);
        builder.setPositiveButton(R.string.error_validation_alert_dialog_ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        hideKeyboard();
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private boolean validateDate(){
        if(eventSetDate.length() > 0){
            return true;
        }
        eventSetDate.setError("");
        AlertDialog();
        eventSetDate.requestFocus();
        return false;
    }

    private boolean validateTime(){
        if(eventSetTime.length() > 0){
            return true;
        }
        eventSetTime.setError("");
        AlertDialog();
        eventSetTime.requestFocus();
        return false;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        myCalendar.set(Calendar.YEAR, year);
        myCalendar.set(Calendar.MONTH, month);
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        eventSetDate.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        myCalendar.set(Calendar.MINUTE, minute);
        String myFormat = "HH:mm"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        eventSetTime.setText(sdf.format(myCalendar.getTime()));
    }
    private void hideKeyboard() {
        try {
            View view = activity.getCurrentFocus();
            if (view != null) {
                final InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

}
