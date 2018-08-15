package com.example.asuki.fooddeliverytestv1;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Asuki on 2017. 12. 03..
 */

public class ClientsAdapter extends ArrayAdapter<Clients> {

    private final String Tag = "ClientsAdapter";
    private Context context;
    private int resource;
    private ArrayList<Clients> objects;

    public ClientsAdapter(@NonNull Context context, int resource, ArrayList<Clients> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String name = getItem(position).getName();
        String address = getItem(position).getAddress();
        String mobile = getItem(position).getMobile();

        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resource, parent, false);

        TextView textViewName = convertView.findViewById(R.id.textView_1);
        TextView textViewAddress = convertView.findViewById(R.id.textView_2);
        TextView textViewMobile = convertView.findViewById(R.id.textView_3);

        textViewName.setText(name);
        textViewAddress.setText(address);
        textViewMobile.setText(mobile);

        return convertView;
    }
}
