package app.projectlevapplication;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import app.projectlevapplication.core.Member;
import app.projectlevapplication.utils.Utils;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by user-pc on 21/01/2017.
 */

public class LogInDialog extends DialogFragment {

    Context context;
    Activity activity;
    EditText userName;
    EditText password;
    TextView registerLink;
    public ProgressDialog loading;
    SharedPreferences mPrefs;

    public interface DialogFragmentListener {
        public void onDialogPositiveClick(LogInDialog dialog);
        public void onDialogNegativeClick(LogInDialog dialog);
        public void onDialogRegisterClick(LogInDialog dialog);
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
        View view = inflater.inflate(R.layout.login_dialog_fragment, null);
        userName = (EditText) view.findViewById(R.id.loginUserName);
        password = (EditText) view.findViewById(R.id.loginPassword);
        mPrefs = activity.getPreferences(MODE_PRIVATE);
        Drawable user = getResources().getDrawable(R.drawable.user_name, null);
        Drawable eye = getResources().getDrawable(R.drawable.eye, null);
        user.setBounds(0, 0, user.getIntrinsicWidth(), user.getIntrinsicHeight());
        eye.setBounds(0, 0, eye.getIntrinsicWidth(), eye.getIntrinsicHeight());
        userName.setCompoundDrawables(null, null, user, null);
        password.setCompoundDrawables(null, null, eye, null);


        password.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
               if(password.getText().length() == 0){
                   password.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
               }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                password.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            }
        });

        Button login = (Button)view.findViewById(R.id.btnLogin);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                loading = ProgressDialog.show(context,getString(R.string.progress_dialog_message),getString(R.string.progress_dialog_title),false,false);
                String name = userName.getText().toString();
                String pass = password.getText().toString();

                    String url = "http://arianlev.esy.es/ArianLev_Community/api/api.php?key=W2jFgx1leQ&opt=0&user="+name+"&pass="+pass;

                    StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            loading.dismiss();
                            Member member;
                            if(response.length() > 0){
                                member = Utils.getInstance().responseToMember(response);
                                Utils.getInstance().writeMemberToPrefs(member,activity);
                                mListener.onDialogPositiveClick(LogInDialog.this);
                                dismiss();
                            }else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setMessage(R.string.login_error_dialog_message);
                                builder. setTitle(R.string.login_error_dialog_title);
                                builder.setPositiveButton(R.string.login_error_dialog_ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                    }
                                });
                                builder.setNegativeButton(R.string.login_error_dialog_abort, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Toast.makeText(context, getString(R.string.login_error_abort),Toast.LENGTH_SHORT).show();
                                        mListener.onDialogNegativeClick(LogInDialog.this);
                                        dismiss();
                                    }
                                });
                                AlertDialog dialog = builder.create();
                                dialog.show();

                            }

                        }
                    },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    loading.dismiss();
                                }
                            });

                    RequestQueue requestQueue = Volley.newRequestQueue(activity);
                    requestQueue.add(stringRequest);
                }
        });

        Button btnCancel = (Button)view.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onDialogNegativeClick(LogInDialog.this);
                dismiss();
            }
        });

        registerLink = (TextView)view.findViewById(R.id.registerLink);
        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onDialogRegisterClick(LogInDialog.this);
                dismiss();
            }
        });

        return view;
    }


}
