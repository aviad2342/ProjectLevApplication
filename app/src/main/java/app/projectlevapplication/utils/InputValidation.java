package app.projectlevapplication.utils;

import android.widget.EditText;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Aviad on 09/02/2017.
 */

public class InputValidation {

    /**
     * Validate Email Address
     * @param mail
     * @return boolean
     */
    public static boolean isEmailValid(EditText mail)
    {
        String email = mail.getText().toString();
        String regExpn =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        +"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        +"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        +"([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(regExpn,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if(matcher.matches())
            return true;
        else
            return false;
    }

    /**
     * Validate Only Letters And Numbers In Field With Length Between 4 And 12
     * @param editText
     * @return boolean
     */
    public static boolean isUserNameOnlyLettersAndNumbers(EditText editText)
    {
        String text = editText.getText().toString();
        String regExpn = "^(?=.*[0-9])(?=.*[a-z])(?=\\S+$).{4,12}$";

        CharSequence inputStr = text;

        Pattern pattern = Pattern.compile(regExpn,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if(matcher.matches())
            return true;
        else
            return false;
    }

    /**
     * Validate Only Letters And Numbers In Field With Length Between 5 And 12
     * @param editText
     * @return boolean
     */
    public static boolean isPasswordOnlyLettersAndNumbers(EditText editText)
    {
        String text = editText.getText().toString();
        String regExpn = "^(?=.*[0-9])(?=.*[a-z])(?=\\S+$).{5,12}$";

        CharSequence inputStr = text;

        Pattern pattern = Pattern.compile(regExpn,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if(matcher.matches())
            return true;
        else
            return false;
    }

    /**
     * Validate Only Digits In Field With Length Between 9 And 10
     * @param editText
     * @return boolean
     */
    public static boolean isOnlyDigits(EditText editText)
    {
        String text = editText.getText().toString();
        String regExpn = "([0-9]+).{9,10}$";

        CharSequence inputStr = text;

        Pattern pattern = Pattern.compile(regExpn,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if(matcher.matches())
            return true;
        else
            return false;
    }

    /**
     * Validate Only Hebrew Letters In Field
     * @param editText
     * @return boolean
     */
    public static boolean isHebrewValid(EditText editText)
    {
        String text = editText.getText().toString();
       // String regExpn ="[א-ת]+$+";
        String regExpn ="[א-ת]+([ '-][א-ת]+)*";
        CharSequence inputStr = text;

        Pattern pattern = Pattern.compile(regExpn,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if(matcher.matches())
            return true;
        else
            return false;
    }

    /**
     * Validate Member Age Is Over 18
     * @param year
     * @param month
     * @param day
     * @return
     */
    public static boolean isOver18(int year, int month, int day){
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
            age--;
        }
        return (age >= 18);
    }
}
