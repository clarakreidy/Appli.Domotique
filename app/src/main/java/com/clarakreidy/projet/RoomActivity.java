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
    ArrayList<Domotique> sensors = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        TextView roomName = (TextView) findViewById(R.id.edit_room_name);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            roomName.setText(extras.getString("name"));
            this.id = extras.getInt("id");
        }

        bearerToken = "Bearer " + getSharedPreferences("Auth", MODE_PRIVATE).getString("token", "");
        ListView listView = (ListView) findViewById(R.id.sensors_list);

        DomotiqueAdapter adapter = new DomotiqueAdapter(this, R.layout.domotique_layout, sensors);
        listView.setAdapter(adapter);

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
                            Type type = new TypeToken<ArrayList<Domotique>>(){}.getType();
                            sensors.clear();
                            sensors.addAll(gson.fromJson(sensorList, type));
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
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
        dialog.setArguments(args);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        dialog.show(fragmentTransaction, "DomotiqueFragment");
    }

    public void listAllSensors() {

    }
}