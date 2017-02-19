package app.projectlevapplication.viewComponents;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app.projectlevapplication.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddNewArticleFragment extends Fragment {


    Context context;
    Activity activity;
    public AddNewArticleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_members_list, container, false);
        context = view.getContext();
        activity= getActivity();
        return view;
    }

}
