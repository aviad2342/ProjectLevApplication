package app.projectortalapplication.utils;

import android.app.DatePickerDialog;
import android.widget.DatePicker;

import java.util.Date;

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
