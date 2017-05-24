package app.projectortalapplication.viewComponents;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import app.projectortalapplication.R;
import app.projectortalapplication.core.Article;
import app.projectortalapplication.utils.Utils;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddNewArticleFragment extends Fragment {


    Context context;
    Activity activity;
    EditText articleSetHeadline;
    EditText articleSetContent;
    ProgressDialog loading;
    int publisherID;

    public AddNewArticleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_add_new_article, container, false);
        context = view.getContext();
        activity= getActivity();

        articleSetHeadline = (EditText) view.findViewById(R.id.articleSetHeadline);
        articleSetContent = (EditText) view.findViewById(R.id.articleSetContent);
        if(Utils.getInstance().loadMemberFromPrefs(context) != null){
            publisherID = Utils.getInstance().loadMemberFromPrefs(context).getMemberID();
        }

        Button btnAddNewArticle = (Button) view.findViewById(R.id.btnAddNewArticle);
        btnAddNewArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                if(validate(new EditText[]{articleSetHeadline, articleSetContent})){
                    Article articleToAdd = new Article();

                    articleToAdd.setHeadline(articleSetHeadline.getText().toString());
                    articleToAdd.setContent(articleSetContent.getText().toString());
                    articleToAdd.setAuthorID(publisherID);
                    //eventToAdd.setImageCount();

                    loading = ProgressDialog.show(activity,getString(R.string.ui_register_progress_dialog_message),getString(R.string.ui_register_progress_dialog_title),false,false);

                    String url = Utils.ADD_NEW_ARTICLE;

                    JsonObjectRequest request_json = new JsonObjectRequest(url, articleToAdd.toJsonObject(),
                            new com.android.volley.Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    loading.dismiss();
                                    Toast.makeText(activity,getString(R.string.article_add_new_article_successfully_submit_form),Toast.LENGTH_SHORT).show();
                                    getFragmentManager().popBackStack();
                                }
                            }, new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            loading.dismiss();
                            Toast.makeText(activity,getString(R.string.article_add_new_article_error_submit_form),Toast.LENGTH_SHORT).show();
                            getFragmentManager().popBackStack();
                        }
                    });
                    RequestQueue requestQueue = Volley.newRequestQueue(activity);
                    requestQueue.add(request_json);
                }

            }
        });

        Button btnCancelNewArticle = (Button)view.findViewById(R.id.btnCancelNewArticle);
        btnCancelNewArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });

        return view;
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
