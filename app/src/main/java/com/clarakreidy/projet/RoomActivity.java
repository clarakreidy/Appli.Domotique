package com.clarakreidy.projet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.OkHttpResponseListener;

import java.util.ArrayList;

import okhttp3.Response;

public class RoomActivity extends AppCompatActivity {
    Integer id;
    String bearerToken;
    ArrayList<Domotique> domotiques = new ArrayList<>();

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
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        dialog.show(fragmentTransaction, "DomotiqueFragment");
    }
}