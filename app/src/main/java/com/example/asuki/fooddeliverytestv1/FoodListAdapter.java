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
 * Created by Asuki on 2017. 11. 25..
 */

public class FoodListAdapter extends ArrayAdapter<Food> {

    private final String Tag = "FoodListAdapter";
    private Context context;
    private int resource;
    private ArrayList<Food> objects;

    public FoodListAdapter(@NonNull Context context, int resource, ArrayList<Food> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String code = getItem(position).getCode();
        String foodName = getItem(position).getName();

        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resource, parent, false);

        TextView textViewCode = convertView.findViewById(R.id.textView_left);
        TextView textViewName = convertView.findViewById(R.id.textView_right);

        textViewCode.setText(code);
        textViewName.setText(foodName);

        return convertView;
    }
}
