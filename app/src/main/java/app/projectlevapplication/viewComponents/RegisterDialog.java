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
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import app.projectlevapplication.MainActivity;
import app.projectlevapplication.R;
import app.projectlevapplication.core.Member;
import app.projectlevapplication.core.Phone;
import app.projectlevapplication.model.ApiService;
import app.projectlevapplication.model.Result;
import app.projectlevapplication.model.RetroClient;
import app.projectlevapplication.utils.DateSettings;
import app.projectlevapplication.utils.InputValidation;
import app.projectlevapplication.utils.Utils;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Aviad on 09/02/2017.
 */

public class RegisterDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener{

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
    boolean isUserUploadingImage;
    DatePickerDialog fromDatePickerDialog;
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
    String imagePath;
    String imageForNewMember;
    private SimpleDateFormat dateFormatter;
    LayoutInflater inflater;
    DatePicker datePicker;
    //AlertDialog.Builder builder;
    DatePickerDialog.Builder builder;
    AlertDialog bla;

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        myCalendar.set(Calendar.YEAR, year);
        myCalendar.set(Calendar.MONTH, month);
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        txtDateOfBirth.setText(sdf.format(myCalendar.getTime()));
    }

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
        radioButton  = (RadioButton) view.findViewById(R.id.rbMobile);
        ageError = (EditText) view.findViewById(R.id.ageError);
        imageForNewMember = Utils.NEW_MEMBER_DEFAULT_IMAGE;
        isUserUploadingImage = false;

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

        txtUserName.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if(txtUserName.getText().length() == 0){
                    txtUserName.setTextDirection(View.TEXT_DIRECTION_RTL);
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                txtUserName.setTextDirection(View.TEXT_DIRECTION_RTL);
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtUserName.setTextDirection(View.TEXT_DIRECTION_LTR);
            }
        });
        txtPassword.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if(txtPassword.getText().length() == 0){
                    txtPassword.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                txtPassword.setTextAlignment(View.TEXT_ALIGNMENT_INHERIT);
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtPassword.setTextAlignment(View.TEXT_ALIGNMENT_INHERIT);
            }
        });
        txtPasswordConfirmation.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if(txtPasswordConfirmation.getText().length() == 0){
                    txtPasswordConfirmation.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                txtPasswordConfirmation.setTextAlignment(View.TEXT_ALIGNMENT_INHERIT);
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtPasswordConfirmation.setTextAlignment(View.TEXT_ALIGNMENT_INHERIT);
            }
        });
        txtEmail.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if(txtEmail.getText().length() == 0){
                    txtEmail.setTextDirection(View.TEXT_DIRECTION_RTL);
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                txtEmail.setTextDirection(View.TEXT_DIRECTION_RTL);
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtEmail.setTextDirection(View.TEXT_DIRECTION_LTR);
            }
        });

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
                if(validate(new EditText[]{txtUserName, txtPassword, txtPasswordConfirmation, txtEmail, txtFirstName, txtLastName, txtPhone1})){
                    if(validateUserName() && isPasswordMatches() && validatePassword() && validateFirstName() && validateLastName() && validateMail() && validateBirthDate() && validatePhone() ){
                        if(isUserUploadingImage && !executeImageUploading()) {
                            uploadingImageAlertDialog();
                        }
                            Member memberToAdd = new Member();

                            memberToAdd.setUsername(txtUserName.getText().toString());
                            memberToAdd.setPassword(txtPassword.getText().toString());
                            memberToAdd.setFullName(txtFirstName.getText().toString() + " " + txtLastName.getText().toString());
                            memberToAdd.setBirthDate(myCalendar.getTime());
                            memberToAdd.setEmail(txtEmail.getText().toString());
                            memberToAdd.setPhoneNumber(txtPhone1.getText().toString());
                            memberToAdd.setType(Integer.parseInt(radioButton.getTag().toString()));
                            memberToAdd.setPublish(cbPhone1Privacy.isChecked());
                            memberToAdd.setProfilePic(imageForNewMember);
                            memberToAdd.setRegistrationDate(new Date());
                            memberToAdd.setSendMails(cbAddsConfirm.isChecked());

                        loading = ProgressDialog.show(activity,getString(R.string.ui_register_progress_dialog_message),getString(R.string.ui_register_progress_dialog_title),false,false);

                        String url = Utils.ADD_NEW_MEMBER;

                        JsonObjectRequest request_json = new JsonObjectRequest(url, memberToAdd.toJsonObject(),
                                new com.android.volley.Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        loading.dismiss();
                                        Toast.makeText(activity,getString(R.string.ui_register_successfully_submit_form),Toast.LENGTH_LONG).show();
                                        dismiss();
                                    }
                                }, new com.android.volley.Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                loading.dismiss();
                                Toast.makeText(activity,getString(R.string.ui_register_error_submit_form),Toast.LENGTH_LONG).show();
                            }
                        });
                        RequestQueue requestQueue = Volley.newRequestQueue(activity);
                        requestQueue.add(request_json);

                    }else {
                        AlertDialog();
                        //Toast.makeText(activity,getString(R.string.error_validation_must_fill_all_fields),Toast.LENGTH_LONG).show();
                    }
                }
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
        txtUserName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if(!InputValidation.isUserNameOnlyLettersAndNumbers(txtUserName) && txtUserName.length() > 0){
                        txtUserName.setError(getString(R.string.error_validation_user_name));
                    }
                }
            }
        });
        txtPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if(!InputValidation.isPasswordOnlyLettersAndNumbers(txtPassword) && txtPassword.length() > 0){
                        txtPassword.setError(getString(R.string.error_validation_password));
                    }
                }
            }
        });
        txtPasswordConfirmation.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if(!(txtPasswordConfirmation.getText().toString().trim().equals(txtPassword.getText().toString().trim())) && txtPasswordConfirmation.getText().length() > 0){
                        txtPasswordConfirmation.setError(getString(R.string.error_validation_password_confirmation));
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

        txtPhone1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if(!InputValidation.isOnlyDigits(txtPhone1) && txtPhone1.length() > 0){
                        txtPhone1.setError(getString(R.string.error_validation_phone));
                    }
                }
            }
        });

        ImageButton imageButton = (ImageButton)view.findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // new DatePickerDialog(getActivity(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

        txtDateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                MyDatePickerDialog pickerDialog = new MyDatePickerDialog();
//                pickerDialog.show(getFragmentManager(),"naab");
//                ageError.setVisibility(View.GONE);
//                DatePickerDialog pickerDialog = new DatePickerDialog(getActivity(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
//                pickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "ביטול", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        if (which == DialogInterface.BUTTON_NEGATIVE) {
//                            validateBirthDate();
//                        }
//                    }
//                });
//                pickerDialog.getDatePicker().setCalendarViewShown(false);
//                pickerDialog.getDatePicker().setSpinnersShown(true);
//                pickerDialog.show();
                show();
            }
        });
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        //setDateTimeField();
        inflater = activity.getLayoutInflater();
        datePicker = (DatePicker) inflater.inflate(R.layout.my_date_picker,null);
        builder = new DatePickerDialog.Builder(activity);

        builder.setTitle(getString(R.string.ui_register_date_picker_title));

        builder.setPositiveButton(getString(R.string.ui_register_date_picker_ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    myCalendar.set(Calendar.YEAR, datePicker.getYear());
                    myCalendar.set(Calendar.MONTH, datePicker.getMonth());
                    myCalendar.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
                    String myFormat = "dd/MM/yyyy"; //In which you need put here
                    SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
                    txtDateOfBirth.setText(sdf.format(myCalendar.getTime()));
                    if(!InputValidation.isOver18(datePicker.getYear(),datePicker.getMonth(),datePicker.getDayOfMonth())){
                        ageError.setVisibility(View.VISIBLE);
                        ageError.setError(getString(R.string.error_validation_age_over_18));
                    }else{
                        ageError.setVisibility(View.GONE);
                        ageError.setError(null);
                    }
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
        bla = builder.create();
        return view;
    }

    public void build(){
        builder = new DatePickerDialog.Builder(activity);
        builder.setView(datePicker);
        bla = builder.create();
    }

    /**
     * Show month year picker dialog.
     */
    public void show() {
        bla.show();

    }
//    private void setDateTimeField() {
//        txtDateOfBirth.setOnClickListener(this);
//
//        Calendar newCalendar = Calendar.getInstance();
//
//        fromDatePickerDialog = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {
//
//            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                Calendar newDate = Calendar.getInstance();
//                newDate.set(year, monthOfYear, dayOfMonth);
//                txtDateOfBirth.setText(dateFormatter.format(newDate.getTime()));
//            }
//        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
//    }

//    @Override
//    public void onClick(View v) {
//        if(v == txtDateOfBirth) {
//            //fromDatePickerDialog.show();
//            bla.show();
//        }
//    }

//    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
//
//        @Override
//        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//            myCalendar.set(Calendar.YEAR, year);
//            myCalendar.set(Calendar.MONTH, monthOfYear);
//            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//            updateLabel();
//            if(!InputValidation.isOver18(year,monthOfYear,dayOfMonth)){
//                ageError.setVisibility(View.VISIBLE);
//                ageError.setError(getString(R.string.error_validation_age_over_18));
//            }else{
//                ageError.setVisibility(View.GONE);
//                ageError.setError(null);
//            }
//        }
//    };

//    private void updateLabel() {
//        String myFormat = "dd/MM/yyyy"; //In which you need put here
//        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
//        txtDateOfBirth.setText(sdf.format(myCalendar.getTime()));
//    }

//    public void rbClick(View view){
//        int selectedRadioButton = phone1Group.getCheckedRadioButtonId();
//        radioButton = (RadioButton)view.findViewById(selectedRadioButton);
//    }


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
                if(!isTakenFromCamera){
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = activity.getContentResolver().query(mImageCaptureUri, filePathColumn, null, null, null);

                    if (cursor != null) {
                        cursor.moveToFirst();
                    }
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    imagePath = cursor.getString(columnIndex);
                }
                else  {
                    imagePath = mImageCaptureUri.getPath();
//                    File f = new File(mImageCaptureUri.getPath());
//                    if (f.exists())
//                        f.delete();
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

    public boolean executeImageUploading(){
        if (!TextUtils.isEmpty(imagePath)) {
            // Uploading AsyncTask
            if (Utils.checkConnection(activity)) {
                /******************Retrofit***************/
                uploadImage();
                return true;
            } else {
                return false;
                //Snackbar.make(mView, R.string.action_sign_in, Snackbar.LENGTH_INDEFINITE).show();
            }
        }
            //Snackbar.make(mView, R.string.action_sign_in, Snackbar.LENGTH_INDEFINITE).show();
        return false;
    }


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
                        isTakenFromCamera = true;
                        isUserUploadingImage = true;
                        onPhotoPickerItemSelected(ID_PHOTO_PICKER_FROM_CAMERA);
                        break;
                    case 1:
                        isTakenFromCamera = false;
                        isUserUploadingImage = true;
                        onPhotoPickerItemSelected(ID_PHOTO_PICKER_FROM_GALLERY);
                        break;
                    case 2:
                        isUserUploadingImage = false;
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
    private void uploadImage() {

        //Progressbar to Display
        loading = ProgressDialog.show(activity,"בבקשה המתן...","מחזיר מידע...",false,false);

        //Create Upload Server Client
        ApiService service = RetroClient.getApiService();

        //File creating from selected URL
        File file = new File(imagePath);

        // create RequestBody instance from file
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("uploaded_file", file.getName(), requestFile);

        Call<Result> resultCall = service.uploadImage(body);

        // finally, execute the request
        resultCall.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                loading.dismiss();
                // Response Success or Fail
                if (response.isSuccessful()) {
                    if (!response.body().getResult().equals("error")) {
                        imageForNewMember = response.body().getResult();
                        //Snackbar.make(mView, R.string.ui_register_successfully_submit_form, Snackbar.LENGTH_SHORT).show();
                    }else {
                        imageForNewMember = "null";
                        //Snackbar.make(mView, R.string.ui_register_error_submit_form, Snackbar.LENGTH_SHORT).show();
                    }
                } else {
                    imageForNewMember = "null";
                    //Snackbar.make(mView, R.string.ui_register_error_submit_form, Snackbar.LENGTH_SHORT).show();
                }
                imagePath = "";
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                loading.dismiss();
            }
        });
    }
    public void AlertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(R.string.error_validation_must_fill_all_fields);
        builder.setTitle(R.string.error_validation_alert_dialog_title);
        builder.setPositiveButton(R.string.error_validation_alert_dialog_ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void AlertDialogErr(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(R.string.error_validation_alert_dialog_message);
        builder.setTitle(R.string.error_validation_alert_dialog_title);
        builder.setPositiveButton(R.string.error_validation_alert_dialog_ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void uploadingImageAlertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(R.string.error_validation_uploading_image_message);
        builder.setTitle(R.string.error_validation_uploading_image_title);
        builder.setPositiveButton(R.string.error_validation_uploading_image_btn_ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                imageForNewMember = Utils.NEW_MEMBER_DEFAULT_IMAGE;
            }
        });
        builder.setNegativeButton(R.string.error_validation_uploading_image_btn_no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                mListener.onDialogNegative(RegisterDialog.this);
                dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private boolean validate(EditText[] fields){
        for(int i=0; i<fields.length; i++){
            if(fields[i].length()== 0){
                fields[i].setError("זהו שדה חובה!");
                fields[i].requestFocus();
                AlertDialogErr();
                return false;
            }
        }
        return true;
    }

    private boolean isPasswordMatches() {
        if (txtPasswordConfirmation.getText().length() > 0 && txtPassword.getText().length() > 0 ) {
            if (txtPasswordConfirmation.getText().toString().trim().equals(txtPassword.getText().toString().trim())) {
                return true;
           }
        }
        txtPasswordConfirmation.setError(getString(R.string.error_validation_password_confirmation));
        txtPasswordConfirmation.requestFocus();
        return false;
    }

    private boolean validateMail(){
        if(!InputValidation.isEmailValid(txtEmail) && txtEmail.length() > 0){
            txtEmail.setError(getString(R.string.error_validation_maile));
            return false;
        }
        return true;
    }
    private boolean validateUserName(){
        if(!InputValidation.isUserNameOnlyLettersAndNumbers(txtUserName) && txtUserName.length() > 0){
            txtUserName.setError(getString(R.string.error_validation_user_name));
            return false;
        }
        return true;
    }
    private boolean validatePassword(){
        if(!InputValidation.isPasswordOnlyLettersAndNumbers(txtPassword) && txtPassword.length() > 0){
            txtPassword.setError(getString(R.string.error_validation_password));
            return false;
        }
        return true;
    }
    private boolean validateFirstName(){
        if(!InputValidation.isHebrewValid(txtFirstName) && txtFirstName.length() > 0){
            txtFirstName.setError(getString(R.string.error_validation_hebrew));
            return false;
        }
        return true;
    }
    private boolean validateLastName(){
        if(!InputValidation.isHebrewValid(txtLastName) && txtLastName.length() > 0){
            txtLastName.setError(getString(R.string.error_validation_hebrew));
            return false;
        }
        return true;
    }
    private boolean validatePhone(){
        if(!InputValidation.isOnlyDigits(txtPhone1) && txtPhone1.length() > 0){
            txtPhone1.setError(getString(R.string.error_validation_phone));
            return false;
        }
        return true;
    }
    private boolean validateBirthDate(){
        if(txtDateOfBirth.length() > 0){
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            Date date = new Date();
            Calendar cal = Calendar.getInstance();
            try {
                date = format.parse(txtDateOfBirth.getText().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            cal.setTime(date);
            if(!InputValidation.isOver18(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH))){
                ageError.setVisibility(View.VISIBLE);
                ageError.setError(getString(R.string.error_validation_age_over_18));
                return false;
            }else {
                return true;
            }
        }
        return false;
    }
}
