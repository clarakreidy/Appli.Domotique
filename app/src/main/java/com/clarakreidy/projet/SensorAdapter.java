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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class SensorAdapter extends ArrayAdapter<Sensor> {
    ArrayList<Sensor> sensors;
    Context context;
    int resource;

    public SensorAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Sensor> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.sensors = objects;
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
        ImageView imageView = view.findViewById(R.id.sensor_img);
        TextView textName = view.findViewById(R.id.sensor_name);
        CheckBox checkBox = view.findViewById(R.id.checkbox_to_delete_sensor);
        TextView sensorValue = view.findViewById(R.id.sensor_value);

        //getting the hero of the specified position
        Sensor sensor = sensors.get(position);

        //Thermometer Typo Handling
        if(sensor.getPicture().contains("termometer"))
        {
            sensor.setPicture("img/thermometer.png");
        }

        //adding values to the list item
        Glide.with(context)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .load("https://myhouse.lesmoulinsdudev.com/" + sensor.getPicture())
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
        textName.setText(sensor.getName());

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                sensor.setChecked(b);
            }
        });

        String bearerToken = "Bearer " + context.getSharedPreferences("Auth", MODE_PRIVATE).getString("token", "");

        AndroidNetworking.get("https://myhouse.lesmoulinsdudev.com/sensor-value")
                .addHeaders("Authorization", bearerToken)
                .addQueryParameter("idSensor", String.valueOf(sensor.getId()))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    class SensorValue {
                        float value;
                        String unit;
                    }

                    @Override
                    public void onResponse(JSONObject response) {
                        Gson gson = new Gson();
                        Type type = new TypeToken<SensorValue>(){}.getType();
                        SensorValue sv = gson.fromJson(response.toString(), type);

                        sensor.setValue(sv.value);
                        sensor.setUnit(sv.unit);

                        sensorValue.setText(sensor.getValue() + sensor.getUnit());
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });

        //finally returning the view
        return view;
    }
}

