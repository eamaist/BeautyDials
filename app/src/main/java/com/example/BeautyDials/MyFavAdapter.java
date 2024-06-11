package com.example.BeautyDials;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class MyFavAdapter extends ArrayAdapter<Add> implements Filterable {

    Context ctx;
    LayoutInflater lain;
    ArrayList<Add> adds;
    ArrayList<Add> addsAll;

    MyFavAdapter(Context context, ArrayList<Add> adds){
        super(context, R.layout.fragment_ad, adds);
        this.ctx = context;
        this.adds = adds;
        this.addsAll = new ArrayList<>(adds);
        lain = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null){
            view = lain.inflate(R.layout.fragment_ad, parent, false);
        }

        Add a = getItem(position);
        ((TextView) view.findViewById(R.id.name)).setText(a.title);
        ((TextView) view.findViewById(R.id.price)).setText(a.price+"");
        ((TextView) view.findViewById(R.id.seller)).setText(a.seller);
        if (a.image != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(a.image, 0, a.image.length);
            ((ImageView) view.findViewById(R.id.ani)).setImageBitmap(bitmap);
        }
        ((TextView) view.findViewById(R.id.adId)).setText(a.id+"");
        return view;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                if (constraint == null || constraint.length() == 0) {
                    results.values = addsAll;
                    results.count = addsAll.size();
                } else {
                    ArrayList<Add> filterList = new ArrayList<>();
                    for (Add a : addsAll) {
                        if (a.like) {
                            filterList.add(a);
                        }
                    }
                    results.values = filterList;
                    results.count = filterList.size();
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                adds = (ArrayList<Add>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    ArrayList<Add> getBox(){
        ArrayList<Add> box = new ArrayList<Add>();
        for (Add a: adds){
            if (a.like){
                box.add(a);
            }
        }
        return box;
    }
}
