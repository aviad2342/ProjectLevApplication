package app.projectlevapplication.viewComponents;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import app.projectlevapplication.R;
import app.projectlevapplication.utils.DateSettings;
import app.projectlevapplication.utils.InputValidation;
import app.projectlevapplication.utils.Utils;

/**
 * Created by Aviad on 09/02/2017.
 */

public class RegisterDialog extends DialogFragment{

    private static final int READ_EXTERNAL_STORAGE_REQUEST = 1;
    private static final int CAMERA_REQUEST = 2;
    private static final int WRITE_EXTERNAL_STORAGE_REQUEST = 3;
    public static final int CODE_CROP_PHOTO_REQUEST = 4;

    // For photo picker selection:
    public static final int ID_PHOTO_PICKER_FROM_CAMERA = 0;

    // For photo picker selection:
    public static final int ID_PHOTO_PICKER_FROM_GALLERY = 1;
    private static final String IMAGE_UNSPECIFIED = "image/*";
    private static final String URI_INSTANCE_STATE_KEY = "saved_uri";

    private Uri mImageCaptureUri;
    private boolean isTakenFromCamera;

    Context context;
    Activity activity;
    public ProgressDialog loading;
    EditText txtUserName;
    EditText txtPassword;
    EditText txtPasswordConfirmation;
    EditText txtEmail;
    EditText txtFirstName;
    EditText txtLastName;
    TextView txtDateOfBirth;
    EditText txtPhone1;
    ImageView registerImage;
    CheckBox cbAddsConfirm;
    CheckBox cbPhone1Privacy;
    RadioGroup phone1Group;
    RadioButton radioButton;
    View mView;
    Calendar myCalendar = Calendar.getInstance();
    EditText ageError;
    private int year;
    private int month;
    private int day;





    public interface DialogFragmentListener {
        public void onDialogPositive(RegisterDialog dialog);
        public void onDialogNegative(RegisterDialog dialog);
    }

    DialogFragmentListener mListener;

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
        try {
            // Instantiate the MyAlertDialogFragmentListener so we can send events to the host
            mListener = (DialogFragmentListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString() + " must implement EditPlaceDialogListener");
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.register_dialog_fragment,null);
        mView = view;
        txtUserName = (EditText) view.findViewById(R.id.txtUserName);
        txtPassword = (EditText) view.findViewById(R.id.txtPassword);
        txtPasswordConfirmation = (EditText) view.findViewById(R.id.txtPasswordConfirmation);
        txtEmail = (EditText) view.findViewById(R.id.txtEmail);
        txtFirstName = (EditText) view.findViewById(R.id.txtFirstName);
        txtLastName = (EditText) view.findViewById(R.id.txtLastName);
        txtDateOfBirth = (TextView) view.findViewById(R.id.txtDateOfBirth);
        txtPhone1 = (EditText) view.findViewById(R.id.txtPhone1);
        registerImage = (ImageView) view.findViewById(R.id.registerImage);
        cbAddsConfirm = (CheckBox) view.findViewById(R.id.cbAddsConfirm);
        cbPhone1Privacy = (CheckBox) view.findViewById(R.id.cbPhone1Privacy);
        phone1Group = (RadioGroup) view.findViewById(R.id.phone1Group);
        ageError = (EditText) view.findViewById(R.id.ageError);

        Drawable x = getResources().getDrawable(R.drawable.asterisk, null);
        Drawable cal = getResources().getDrawable(R.drawable.calendar, null);
        x.setBounds(0, 0, x.getIntrinsicWidth(), x.getIntrinsicHeight());
        cal.setBounds(0, 0, cal.getIntrinsicWidth(), cal.getIntrinsicHeight());
        txtUserName.setCompoundDrawables(x, null, null, null);
        txtPassword.setCompoundDrawables(x, null, null, null);
        txtPasswordConfirmation.setCompoundDrawables(x, null, null, null);
        txtEmail.setCompoundDrawables(x, null, null, null);
        txtFirstName.setCompoundDrawables(x, null, null, null);
        txtLastName.setCompoundDrawables(x, null, null, null);
        txtDateOfBirth.setCompoundDrawables(x, null, cal, null);
        txtPhone1.setCompoundDrawables(x, null, null, null);

        Button btnChooseImage = (Button)view.findViewById(R.id.btnChooseImage);
        btnChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayDialog(); // open camera / gallery Dialog to set the place image
            }
        });

        Button btnSubmit = (Button)view.findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        Button btnCancelRegister = (Button)view.findViewById(R.id.btnCancelRegister);
        btnCancelRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onDialogNegative(RegisterDialog.this);
                dismiss();
            }
        });

        phone1Group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radioButton = (RadioButton)mView.findViewById(checkedId);
            }
        });
        // --------------------------------------------------------Input Validation----------------------------------------------------------------------------
        txtEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if(!InputValidation.isEmailValid(txtEmail) && txtEmail.length() > 0){
                        txtEmail.setError(getString(R.string.error_validation_maile));
                    }
                }
            }
        });
        txtFirstName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if(!InputValidation.isHebrewValid(txtFirstName) && txtFirstName.length() > 0){
                        txtFirstName.setError(getString(R.string.error_validation_hebrew));
                    }
                }
            }
        });
        txtLastName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if(!InputValidation.isHebrewValid(txtLastName) && txtLastName.length() > 0){
                        txtLastName.setError(getString(R.string.error_validation_hebrew));
                    }
                }
            }
        });

        ImageButton imageButton = (ImageButton)view.findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getActivity(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        txtDateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ageError.setVisibility(View.GONE);
                DatePickerDialog pickerDialog = new DatePickerDialog(getActivity(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
                pickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "ביטול", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == DialogInterface.BUTTON_NEGATIVE) {
                            // Do Stuff
                        }
                    }
                });
                pickerDialog.getDatePicker().setCalendarViewShown(false);
                pickerDialog.getDatePicker().setSpinnersShown(true);
                pickerDialog.show();
            }
        });

        return view;
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
            if(!InputValidation.isOver18(year,monthOfYear,dayOfMonth)){
                ageError.setVisibility(View.VISIBLE);
                ageError.setError(getString(R.string.error_validation_age_over_18));
            }else{
                ageError.setVisibility(View.GONE);
                ageError.setError(null);
            }
        }
    };

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        txtDateOfBirth.setText(sdf.format(myCalendar.getTime()));
    }

    public void rbClick(View view){
        int selectedRadioButton = phone1Group.getCheckedRadioButtonId();
        radioButton = (RadioButton)view.findViewById(selectedRadioButton);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save the image capture uri before the activity goes into background
        outState.putParcelable(URI_INSTANCE_STATE_KEY, mImageCaptureUri);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode != activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case CAMERA_REQUEST:
                // Send image taken from camera for cropping
                cropImage();
                break;
            case CODE_CROP_PHOTO_REQUEST:
                Bundle extras = data.getExtras();
                // Set the picture image in UI
                if (extras != null) {
                    registerImage.setImageBitmap((Bitmap) extras.getParcelable("data"));
                }

                if (isTakenFromCamera) {
                    File f = new File(mImageCaptureUri.getPath());
                    if (f.exists())
                        f.delete();
                }
                break;
            case ID_PHOTO_PICKER_FROM_GALLERY:
                // Send image taken from camera for cropping
                mImageCaptureUri = data.getData();
                cropImage();
                break;
        }
    }

    // ******* Photo picker dialog related functions ************//
    public void displayDialog() {
        final CharSequence[] items = { "צלם תמונה", "בחר מגלריית התמונות", "ביטול" };

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.ui_profile_photo_picker_title);

        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                // boolean result=Utility.checkPermission(MainActivity.this);
                switch (item) {
                    case 0:
                        onPhotoPickerItemSelected(ID_PHOTO_PICKER_FROM_CAMERA);
                        break;
                    case 1:
                        onPhotoPickerItemSelected(ID_PHOTO_PICKER_FROM_GALLERY);
                        break;
                    case 2:
                        return;
                }
            }
        });
        builder.show();
    }

    public void onPhotoPickerItemSelected(int item) {
        Intent intent;
        switch (item) {
            case ID_PHOTO_PICKER_FROM_CAMERA:
                // Take photo from camera，Construct an intent with action MediaStore.ACTION_IMAGE_CAPTURE
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // Construct temporary image path and name to save the taken photo
                mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "tmp_" + String.valueOf(System.currentTimeMillis()) + ".jpg"));
                intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
                intent.putExtra("return-data", true);
                try {
                    // Start a camera capturing activity REQUEST_CODE_TAKE_FROM_CAMERA is an integer tag you defined to identify the activity in onActivityResult() when it returns
                    startActivityForResult(intent, CAMERA_REQUEST);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
                isTakenFromCamera = true;
                break;
            case ID_PHOTO_PICKER_FROM_GALLERY:
                // Take photo from camera，Construct an intent with action MediaStore.ACTION_IMAGE_CAPTURE
                intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // Construct temporary image path and name to save the taken photo
                // mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "tmp_" + String.valueOf(System.currentTimeMillis()) + ".jpg"));
                intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
                intent.putExtra("return-data", true);
                try {
                    // Start a camera capturing activity REQUEST_CODE_TAKE_FROM_CAMERA is an integer tag you defined to identify the activity in onActivityResult() when it returns
                    startActivityForResult(intent, ID_PHOTO_PICKER_FROM_GALLERY);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
                isTakenFromCamera = false;
                break;

            default:
                return;
        }

    }


    // Crop and resize the image for profile
    private void cropImage() {
        // Use existing crop activity.
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(mImageCaptureUri, IMAGE_UNSPECIFIED);

        // Specify image size
        intent.putExtra("outputX", 100);
        intent.putExtra("outputY", 100);

        // Specify aspect ratio, 1:1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", true);
        // REQUEST_CODE_CROP_PHOTO is an integer tag you defined to
        // identify the activity in onActivityResult() when it returns
        startActivityForResult(intent, CODE_CROP_PHOTO_REQUEST);
    }

}
