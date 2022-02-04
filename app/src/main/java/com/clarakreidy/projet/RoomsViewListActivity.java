package com.clarakreidy.projet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class RoomsViewListActivity extends AppCompatActivity {

    ArrayList<Room> rooms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rooms_view_list);

        rooms = new ArrayList<Room>();
        ListView listView = (ListView) findViewById(R.id.room_view_list);
        rooms.add(new Room(1, "test", "test"));

        String bearerToken = "Bearer " + getPreferences(MODE_PRIVATE).getString("token", "");

        AndroidNetworking.post("https://myhouse.lesmoulinsdudev.com/rooms")
                .addHeaders("Authorization", bearerToken)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String roomsList = response.getJSONArray("rooms").toString();
                            Gson gson = new Gson();
                            Type type = new TypeToken<ArrayList<Room>>(){}.getType();
                            rooms = gson.fromJson(roomsList, type);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
        RoomAdapter adapter = new RoomAdapter(this, R.layout.room_item, rooms);
        listView.setAdapter(adapter);
    }
}