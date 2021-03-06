package com.clarakreidy.projet;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.OkHttpResponseListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;

import okhttp3.Response;

public class DeviceAdapter extends ArrayAdapter<Device> {
    ArrayList<Device> devices;
    Context context;
    int resource;

    public DeviceAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Device> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.devices = objects;
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
        ImageView imageView = view.findViewById(R.id.device_img);
        TextView textName = view.findViewById(R.id.device_name);
        CheckBox checkBox = view.findViewById(R.id.checkbox_to_delete_device);
        Switch deviceSwitch = view.findViewById(R.id.device_switch);

        //getting the hero of the specified position
        Device device = devices.get(position);

        //adding values to the list item
        Glide.with(context)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .load("https://myhouse.lesmoulinsdudev.com/" + device.getPicture())
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
        textName.setText(device.getName());

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                device.setChecked(b);
            }
        });

        deviceSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) device.setStatus(1);
                else device.setStatus(0);

                String bearerToken = "Bearer " + context.getSharedPreferences("Auth", MODE_PRIVATE).getString("token", "");

                AndroidNetworking.post("https://myhouse.lesmoulinsdudev.com/device-status")
                        .addHeaders("Authorization", bearerToken)
                        .addBodyParameter("idDevice", String.valueOf(device.getId()))
                        .addBodyParameter("status", String.valueOf(device.getStatus()))
                        .build()
                        .getAsOkHttpResponse(new OkHttpResponseListener() {
                            @Override
                            public void onResponse(Response response) {
                                // API n'existe pas
                                if(response.code() == 200) {
                                    // toast
                                }
                            }

                            @Override
                            public void onError(ANError anError) {

                            }
                        });
            }
        });

        //finally returning the view
        return view;
    }
}

