package app.projectlevapplication.utils;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.icu.util.Calendar;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.Date;

import app.projectlevapplication.R;

/**
 * Created by Aviad on 10/02/2017.
 */

public class DateSettings implements DatePickerDialog.OnDateSetListener {

    public static Date date;

    public DateSettings() {
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        date = new Date(year,month,dayOfMonth);
    }
}
