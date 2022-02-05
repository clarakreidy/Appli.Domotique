package com.clarakreidy.projet;

import static android.content.Context.MODE_PRIVATE;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class CreateSensors extends DialogFragment {

    ArrayList<Domotique> sensors = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle state) {
        super.onCreateView(inflater, parent, state);
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_domotique, parent, false);
        String bearerToken = "Bearer " + getActivity().getSharedPreferences("Auth", MODE_PRIVATE).getString("token", "");
        DomotiqueAdapter adapter = new DomotiqueAdapter(getContext(), R.id.domotique_img_spinner, sensors);
        Spinner spinner = (Spinner) view.findViewById(R.id.domotique_img_spinner);

        AndroidNetworking.get("https://myhouse.lesmoulinsdudev.com/sensor-types")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String result = response.getJSONArray("sensor-types").toString();
                            Gson gson = new Gson();
                            Type type = new TypeToken<ArrayList<Domotique>>(){}.getType();
                            sensors.clear();
                            sensors.addAll(gson.fromJson(result, type));

                            ArrayAdapter<Domotique> arrayAdapter = new ArrayAdapter<>(
                                    getContext(),
                                    android.R.layout.simple_spinner_dropdown_item,
                                    sensors
                            );
                            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner.setAdapter(arrayAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });

        return view;
    }
}