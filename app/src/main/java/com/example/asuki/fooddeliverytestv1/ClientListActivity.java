package com.example.asuki.fooddeliverytestv1;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Asuki on 2017. 12. 03..
 */

public class ClientListActivity extends AppCompatActivity{
    ListView listView;
    DatabaseHelper databaseHelper;
    private static final String TAG = "ClientListActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.client_list);

            listView = findViewById(R.id.listView_clients);
            databaseHelper = new DatabaseHelper(this);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                }
            });

            populateListView();
        }
        catch (Exception e){

        }
    }

    private void populateListView() {
        try {
            Cursor data = databaseHelper.getTable(databaseHelper.CLIENTS_TABLE);

            ArrayList<Clients> arrayList = new ArrayList<>();
            String name = "";
            String address = "";
            String mobile = "";
            String email = "";


            while (data.moveToNext()) {
                name = data.getString(1);
                address = data.getString(2);
                mobile = data.getString(3);
                email = data.getString(4);
                arrayList.add(new Clients(name, address, mobile, email));
            }
            Log.d(TAG, arrayList.size() + "");
            ListAdapter adapter = new ClientsAdapter(this, R.layout.adapter_view_4, arrayList);
            Log.d(TAG, adapter.toString() + "------------------");

            listView.setAdapter(adapter);
        }
        catch (Exception e){

        }
    }


}
