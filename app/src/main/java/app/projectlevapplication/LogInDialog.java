package app.projectlevapplication;

import android.Manifest;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.ProgressDialog;
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
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

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
    public ProgressDialog loading;

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
        userName.setText("Admin");
        password.setText("Admin");

        Button login = (Button)view.findViewById(R.id.btnLogin);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                loading = ProgressDialog.show(activity,"Please wait...","Fetching...",false,false);
                String name = userName.getText().toString();
                String pass = password.getText().toString();

                    String url = "http://arianlev.esy.es/ArianLev_Community/api/api.php?key=W2jFgx1leQ&opt=0&user="+name+"&pass="+pass;

                    StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            loading.dismiss();
                            Member member;
                            if((member = Utils.getInstance().responseToMember(response)) != null){
                                Utils.getInstance().writeMemberToPrefs(activity.getPreferences(MODE_PRIVATE),member);
                                mListener.onDialogPositiveClick(LogInDialog.this);
                                dismiss();
                            }else{
                                mListener.onDialogRegisterClick(LogInDialog.this);
                                dismiss();
                            }

                        }
                    },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                }
                            });

                    RequestQueue requestQueue = Volley.newRequestQueue(activity);
                    requestQueue.add(stringRequest);
                }
        });

        return view;
    }


}