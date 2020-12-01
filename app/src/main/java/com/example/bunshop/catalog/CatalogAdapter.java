package com.example.bunshop.catalog;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.bunshop.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class CatalogAdapter extends BaseAdapter {

    private Context ctx;
    private LayoutInflater layoutInflater;
    private ArrayList<Catalog> catalog;

    public CatalogAdapter(Context context, ArrayList<Catalog> catalog) {
        this.ctx = context;
        this.catalog = catalog;
        this.layoutInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return catalog.size();
    }

    @Override
    public Object getItem(int i) {
        return catalog.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view = convertView;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.catalog_item, viewGroup, false);
        }

        Catalog category = getCategory(i);

        ((TextView) view.findViewById(R.id.tvCategoryCaption)).setText(category.getName());
        GlideApp.with(ctx)
                .load(category.getReference())
                .into((ImageView) view.findViewById(R.id.ivCategoryIcon));
        return view;
    }

    Catalog getCategory(int position) { return ((Catalog) getItem(position)); }
}
