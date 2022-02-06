package com.clarakreidy.projet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.OkHttpResponseListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

import okhttp3.Response;

public class RoomActivity extends AppCompatActivity {
    Integer id;
    String bearerToken;
    ArrayList<Sensor> sensors = new ArrayList<>();
    ArrayList<Device> devices = new ArrayList<>();
    String roomName;
    SensorAdapter sensorsAdapter;
    DeviceAdapter devicesAdapter;
    ListView sensorsListView;
    ListView devicesListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        TextView roomNameText = (TextView) findViewById(R.id.edit_room_name);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            roomNameText.setText(extras.getString("name"));
            this.id = extras.getInt("id");
        }
        roomName = extras.getString("name");

        bearerToken = "Bearer " + getSharedPreferences("Auth", MODE_PRIVATE).getString("token", "");
        sensorsListView = (ListView) findViewById(R.id.sensors_list);

        sensorsAdapter = new SensorAdapter(this, R.layout.sensors_layout, sensors);
        sensorsListView.setAdapter(sensorsAdapter);

        AndroidNetworking.get("https://myhouse.lesmoulinsdudev.com/sensors")
                .addHeaders("Authorization", bearerToken)
                .addQueryParameter("idRoom", String.valueOf(id))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String sensorList = response.getJSONArray("sensors").toString();
                            Gson gson = new Gson();
                            Type type = new TypeToken<ArrayList<Sensor>>(){}.getType();
                            sensors.clear();
                            sensors.addAll(gson.fromJson(sensorList, type));
                            sensorsAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });


        // Devices
        devicesListView = (ListView) findViewById(R.id.devices_list);

        devicesAdapter = new DeviceAdapter(this, R.layout.devices_layout, devices);
        devicesListView.setAdapter(devicesAdapter);

        AndroidNetworking.get("https://myhouse.lesmoulinsdudev.com/devices")
                .addHeaders("Authorization", bearerToken)
                .addQueryParameter("idRoom", String.valueOf(id))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String devicesName = response.getJSONArray("devices").toString();
                            Gson gson = new Gson();
                            Type type = new TypeToken<ArrayList<Device>>(){}.getType();
                            devices.clear();
                            devices.addAll(gson.fromJson(devicesName, type));
                            devicesAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }

    public void deleteRoom(View view) {
        Intent intent = new Intent(RoomActivity.this, RoomsViewListActivity.class);
        AndroidNetworking.post("https://myhouse.lesmoulinsdudev.com/room-delete")
                .addHeaders("Authorization", bearerToken)
                .addBodyParameter("idRoom", String.valueOf(id))
                .build()
                .getAsOkHttpResponse(new OkHttpResponseListener() {
                    @Override
                    public void onResponse(Response response) {
                        switch (response.code()) {
                            case 200:
                                generateToast("Success.");
                                startActivity(intent);
                                break;
                            case 400:
                                generateToast("An error has occurred. Try updating the app.");
                                break;
                            default:
                                generateToast("An error has occurred. Try again later.");
                                break;
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        generateToast("An error has occurred. Try again later.");
                    }
                });
    }

    public void generateToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void addSensor(View view) {
        CreateSensors dialog = new CreateSensors();

        Bundle args = new Bundle();
        args.putInt("roomId", id);
        args.putString("roomName", roomName);
        dialog.setArguments(args);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        dialog.show(fragmentTransaction, "DomotiqueFragment");
    }

    public void deleteSensors(View view) {
        int count = sensorsAdapter.getCount();
        for (int i = 0; i < count; i++) {
            Sensor sensor = (Sensor) sensorsListView.getItemAtPosition(i);
            if (sensor.isChecked()) {
                AndroidNetworking.post("https://myhouse.lesmoulinsdudev.com/sensor-delete")
                        .addHeaders("Authorization", bearerToken)
                        .addBodyParameter("idSensor", String.valueOf(sensor.getId()))
                        .build()
                        .getAsOkHttpResponse(new OkHttpResponseListener() {
                            @Override
                            public void onResponse(Response response) {
                                sensors.remove(sensor);
                                sensorsAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onError(ANError anError) {

                            }
                        });
            }
        }
    }

    public void addDevice(View view) {
        CreateDevices dialog = new CreateDevices();

        Bundle args = new Bundle();
        args.putInt("roomId", id);
        args.putString("roomName", roomName);
        dialog.setArguments(args);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        dialog.show(fragmentTransaction, "DomotiqueFragment");
    }

    public void deleteDevices(View view) {
        for (int i = 0; i < devicesListView.getCount(); i++) {
            Device device = (Device) devicesListView.getItemAtPosition(i);
            if (device.isChecked()) {
                AndroidNetworking.post("https://myhouse.lesmoulinsdudev.com/device-delete")
                        .addHeaders("Authorization", bearerToken)
                        .addBodyParameter("idDevice", String.valueOf(device.getId()))
                        .build()
                        .getAsOkHttpResponse(new OkHttpResponseListener() {
                            @Override
                            public void onResponse(Response response) {
                                devices.remove(device);
                                devicesAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onError(ANError anError) {

                            }
                        });
            }
        }
    }
}