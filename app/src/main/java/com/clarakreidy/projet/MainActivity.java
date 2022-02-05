package com.clarakreidy.projet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.OkHttpResponseListener;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void toRegister(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void login(View view) {
        EditText email = (EditText) findViewById(R.id.login_email);
        EditText password = (EditText) findViewById(R.id.login_password);

        String emailValue = email.getText().toString();
        String passwordValue = password.getText().toString();

        if(emailValue.isEmpty() || passwordValue.isEmpty()) {
            generateToast("All fields are required.");
            return;
        }

        Intent intent = new Intent(this, RoomsViewListActivity.class);

        AndroidNetworking.post("https://myhouse.lesmoulinsdudev.com/auth")
                .addBodyParameter("login", emailValue)
                .addBodyParameter("password", passwordValue)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            preferences = getSharedPreferences("Auth", MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("token", response.getString("token"));
                            editor.apply();
                            generateToast("Success.");
                            startActivity(intent);
                        } catch (JSONException e) {
                            generateToast("An error has occurred. Try updating the app.");
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        generateToast("Authentication failed.");
                    }
                });
    }

    public void generateToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}