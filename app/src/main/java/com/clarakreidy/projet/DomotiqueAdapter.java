package com.clarakreidy.projet;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.List;

public class DomotiqueAdapter extends ArrayAdapter<Domotique> {
    //the list values in the List of type hero
    ArrayList<Domotique> domotiques;

    //activity context
    Context context;

    //the layout resource file for the list items
    int resource;

    public DomotiqueAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Domotique> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.domotiques = objects;
    }

    //this will return the ListView Item as a View
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        //we need to get the view of the xml for our list item
        //And for this we need a layout inflater
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        //getting the view
        View view = layoutInflater.inflate(resource, null, false);

        //getting the view elements of the list from the view
        ImageView imageView = view.findViewById(R.id.domotique_img);
        TextView textName = view.findViewById(R.id.domotique_name);

        //getting the hero of the specified position
        Domotique domotique = domotiques.get(position);

        //adding values to the list item
        Glide.with(context)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .load("https://myhouse.lesmoulinsdudev.com/" + domotique.getPicture())
                .listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                }).into(imageView);
        textName.setText(domotique.getName());

        //finally returning the view
        return view;
    }
}
