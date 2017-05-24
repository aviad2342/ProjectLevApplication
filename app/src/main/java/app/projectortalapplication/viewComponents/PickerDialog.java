package app.projectortalapplication.viewComponents;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import java.util.Calendar;
import android.os.Bundle;

import app.projectortalapplication.utils.DateSettings;

/**
 * Created by Aviad on 10/02/2017.
 */

public class PickerDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        DateSettings dateSettings =  new DateSettings();
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(getActivity(),dateSettings,year ,month ,day);

        return dialog;
    }
}
