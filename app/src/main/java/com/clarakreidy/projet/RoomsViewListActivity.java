package com.clarakreidy.projet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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

        rooms = new ArrayList<>();
        ListView listView = (ListView) findViewById(R.id.room_view_list);

        String bearerToken = "Bearer " + getSharedPreferences("Auth", MODE_PRIVATE).getString("token", "");

        RoomAdapter adapter = new RoomAdapter(this, R.layout.room_item, rooms);
        listView.setAdapter(adapter);

        AndroidNetworking.get("https://myhouse.lesmoulinsdudev.com/rooms")
                .addHeaders("Authorization", bearerToken)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String roomsList = response.getJSONArray("rooms").toString();
                            Gson gson = new Gson();
                            Type type = new TypeToken<ArrayList<Room>>(){}.getType();
                            rooms.clear();
                            rooms.addAll(gson.fromJson(roomsList, type));
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Room room = (Room) listView.getItemAtPosition(i);
                Intent intent = new Intent(RoomsViewListActivity.this, RoomActivity.class);
                intent.putExtra("name", room.getName());
                intent.putExtra("id", room.getId());
                startActivity(intent);
            }
        });
    }

    public void addRoom(View view) {
        AddRoomActivity dialog = new AddRoomActivity();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        dialog.show(fragmentTransaction, "AddRoomActivity");
    }

}