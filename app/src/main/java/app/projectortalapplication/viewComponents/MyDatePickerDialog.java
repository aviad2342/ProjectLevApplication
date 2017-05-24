package app.projectortalapplication.viewComponents;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import app.projectortalapplication.R;

/**
 * Created by Aviad on 19/02/2017.
 */


public class MyDatePickerDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    Context context;
    Activity activity;
    DatePicker datePicker;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }
    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.date_picker_dialog, null);
        datePicker = (DatePicker)view.findViewById(R.id.datePicker);
        return view;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

            /*
                Initialize a new DatePickerDialog

                DatePickerDialog(Context context, DatePickerDialog.OnDateSetListener callBack,
                    int year, int monthOfYear, int dayOfMonth)
             */
        DatePickerDialog dpd = new DatePickerDialog(getActivity(),this,year,month,day);
        return  dpd;

    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar myCalendar = Calendar.getInstance();
        myCalendar.set(Calendar.YEAR, year);
        myCalendar.set(Calendar.MONTH, month);
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        TextView txtDateOfBirth = (TextView) view.findViewById(R.id.txtDateOfBirth);
        txtDateOfBirth.setText(sdf.format(myCalendar.getTime()));

    }
}
