import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.clarakreidy.projet.R;

import java.util.ArrayList;

public class RoomAdapter extends ArrayAdapter<Room> {
    //the list values in the List of type hero
    ArrayList<Room> rooms;

    //activity context
    Context context;

    //the layout resource file for the list items
    int resource;

    //constructor initializing the values
    public RoomAdapter(Context context, int resource, ArrayList<Room> rooms) {
        super(context, resource, rooms);
        this.context = context;
        this.resource = resource;
        this.rooms = rooms;
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
        ImageView imageView = view.findViewById(R.id.room_img);
        TextView textName = view.findViewById(R.id.room_name);

        //getting the hero of the specified position
        Room room = rooms.get(position);

        //adding values to the list item
//        imageView.setImageDrawable(context.getResources().getDrawable(room.getUrl()));
        textName.setText(room.getName());

        //finally returning the view
        return view;
    }
}
