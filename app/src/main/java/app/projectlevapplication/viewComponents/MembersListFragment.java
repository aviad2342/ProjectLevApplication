package app.projectlevapplication.viewComponents;


import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.Serializable;

import app.projectlevapplication.R;
import app.projectlevapplication.core.Member;
import app.projectlevapplication.utils.Utils;

/**
 * A simple {@link Fragment} subclass.
 */
public class MembersListFragment extends Fragment {

    ListView list;
    MembersListAdapter adapter;
    public ProgressDialog loading;
    Context context;
    Activity activity;
    FragmentManager fragmentManager;

    public MembersListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_members_list, container, false);
        context = view.getContext();
        activity= getActivity();
        fragmentManager = activity.getFragmentManager();

        list = (ListView) view.findViewById(R.id.myMembersList);
        loadList();


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Member selectedItem = adapter.getItem(position);
                Fragment fragment = new MemberProfileFragment();
                Bundle args = new Bundle();
                args.putSerializable("mMember",(Serializable)selectedItem);
                fragment.setArguments(args);
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right);
                ft.replace(R.id.frame_container, fragment,"MemberProfileFragment" );
                ft.addToBackStack("MemberProfileFragment");
                ft.commit();
            }
        });
        return view;
    }

    public void loadList(){
        loading = ProgressDialog.show(context,"בבקשה המתן...","מחזיר מידע...",false,false);

        String url = Utils.ALL_COMMUNITY_MEMBERS;

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //System.out.print(response);
                loading.dismiss();
                // Utils.getInstance().responseToMembersList(response);
                adapter = new MembersListAdapter(activity, Utils.getInstance().responseToMembersList(response));
                list.setAdapter(adapter);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        Toast.makeText(context,"error", Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
    }

}
