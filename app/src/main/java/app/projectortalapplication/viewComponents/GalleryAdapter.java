package app.projectortalapplication.viewComponents;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Aviad on 18/02/2017.
 */

public class GalleryAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> bitmapList;

    public GalleryAdapter(Context context, ArrayList<String> bitmapList) {
        this.context = context;
        this.bitmapList = bitmapList;
    }

    @Override
    public int getCount() {
        return this.bitmapList.size();
    }

    @Override
    public String getItem(int position) {
        return bitmapList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(this.context);
           // imageView.setLayoutParams(new GridView.LayoutParams(115, 115));
           //imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            imageView = (ImageView) convertView;
        }
        Picasso.with(context).load(this.bitmapList.get(position)).resize(250,250).centerCrop().into(imageView);
        //imageView.setImageBitmap(this.bitmapList.get(position));
        return imageView;
    }
}
