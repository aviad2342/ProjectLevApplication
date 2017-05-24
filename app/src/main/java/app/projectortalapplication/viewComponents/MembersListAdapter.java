package app.projectortalapplication.viewComponents;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.List;

import app.projectortalapplication.R;
import app.projectortalapplication.core.Member;

/**
 * Created by user-pc on 26/01/2017.
 */

public class MembersListAdapter extends ArrayAdapter<Member> {


    private List<Member> members = null;
    private Activity context = null;

    public MembersListAdapter(Context context, List<Member> members) {
        super(context, R.layout.members_list, members);
        this.members = members;
        this.context = (Activity)context;
    }
    @Override
    public int getCount() {
        return members.size();
    }

    @Override
    public Member getItem(int position) {
        return members.get(position);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        View row = view;
        if(row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getLayoutInflater();
            row = inflater.inflate(R.layout.members_list, null, true);
        }
        TextView txtName = (TextView) row.findViewById(R.id.txtName);
        ImageView image = (ImageView) row.findViewById(R.id.icon);
        TextView txtBirthDate = (TextView) row.findViewById(R.id.txtBirthDate);

        Member member = members.get(position);

        txtName.setText(member.getFullName());
        Picasso.with(context).load(member.getProfilePic()).into(image);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        txtBirthDate.setText(format.format(member.getBirthDate()));

        return row;

    }


}
