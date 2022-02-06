package com.clarakreidy.projet;

import static android.content.Context.MODE_PRIVATE;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.OkHttpResponseListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

import okhttp3.Response;

public class AddRoomActivity extends DialogFragment {

    ArrayList<Picture> imageUrls = new ArrayList<>();

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

        View view = getActivity().getLayoutInflater().inflate(R.layout.activity_add_room, parent, false);

        Spinner spinner = (Spinner) view.findViewById(R.id.picutres_spinner);
        ImageView imageView = (ImageView) view.findViewById(R.id.room_preview_img);
        Button button = (Button) view.findViewById(R.id.save_new_room_btn);
        EditText roomName = (EditText) view.findViewById(R.id.new_room_name);

        String bearerToken = "Bearer " + getActivity().getSharedPreferences("Auth", MODE_PRIVATE).getString("token", "");

        AndroidNetworking.get("https://myhouse.lesmoulinsdudev.com/pictures")
                .addQueryParameter("type", "room")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String pictureList = response.getJSONArray("pictures").toString();
                            Gson gson = new Gson();
                            Type type = new TypeToken<ArrayList<Picture>>(){}.getType();
                            imageUrls = gson.fromJson(pictureList, type);

                            ArrayAdapter<Picture> arrayAdapter = new ArrayAdapter<>(
                                    getContext(),
                                    android.R.layout.simple_spinner_dropdown_item,
                                    imageUrls
                            );
                            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner.setAdapter(arrayAdapter);
                            arrayAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Picture selected = (Picture) spinner.getSelectedItem();
                Glide.with(getContext())
                        .asBitmap()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .load("https://myhouse.lesmoulinsdudev.com/" + selected.getUrl())
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
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                try {
                    ImageView imageView = (ImageView) view.findViewById(R.id.room_preview_img);
                    imageView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_launcher_foreground));
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

            }
        });

        button.setOnClickListener(v -> {
            String name = roomName.getText().toString();
            Integer idPicture = ((Picture) spinner.getSelectedItem()).getId();
            Intent intent = new Intent(getContext(), RoomsViewListActivity.class);

            if (name.isEmpty())
            {
                generateToast("Error: Room name is required.");
            }
            else
            {
                AndroidNetworking.post("https://myhouse.lesmoulinsdudev.com/room-create")
                        .addHeaders("Authorization", bearerToken)
                        .addBodyParameter("name", name)
                        .addBodyParameter("idPicture", String.valueOf(idPicture))
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
        });
        return view;
    }

    public void generateToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}