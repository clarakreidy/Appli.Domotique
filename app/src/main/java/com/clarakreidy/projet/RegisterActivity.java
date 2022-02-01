package com.clarakreidy.projet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.OkHttpResponseListener;

import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    public void register(View view) {
        Intent intent = new Intent(this, MainActivity.class);

        EditText name = (EditText) findViewById(R.id.register_name);
        EditText email = (EditText) findViewById(R.id.register_email);
        EditText password = (EditText) findViewById(R.id.register_password);
        EditText confirmPassword = (EditText) findViewById(R.id.confirm_register_password);

        String nameValue = name.getText().toString();
        String emailValue = email.getText().toString();
        String passwordValue = password.getText().toString();
        String confirmPasswordValue = confirmPassword.getText().toString();

        if (nameValue.isEmpty()) {
            generateToast("Error: Name is required.");
            return;
        } else if (emailValue.isEmpty()) {
            generateToast("Error: Email is required.");
            return;
        } else if(!passwordValue.equals(confirmPasswordValue) || passwordValue.isEmpty() || confirmPasswordValue.isEmpty()) {
            generateToast("Error: Passwords don't match.");
            return;
        } else

        AndroidNetworking.post("https://myhouse.lesmoulinsdudev.com/register")
                .addBodyParameter("name", nameValue)
                .addBodyParameter("login", emailValue)
                .addBodyParameter("password", passwordValue)
                .build()
                .getAsOkHttpResponse(new OkHttpResponseListener() {
                    @Override
                    public void onResponse(Response response) {
                        System.out.println(response.code());
                        switch (response.code()){
                            case 200:
                                generateToast("Success.");
                                startActivity(intent);
                                break;
                            case 400:
                                generateToast("An error has occurred. Try updating the app.");
                                break;
                            case 500:
                                generateToast("An error has occurred. Try again later.");
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
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}