package com.example.asuki.fooddeliverytestv1;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Button btnSupplier, btnRestaurant;
    private final String LOGIN_TYPE_1 = "supplier";
    private final String LOGIN_TYPE_2 = "restaurant";
    private final String LOGIN_TYPE_3 = "logut";
    private final String TAG = "MainActivity";

    EditText regText;
    DatabaseHelper databaseHelper;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHelper = new DatabaseHelper(this);
        btnSupplier = (Button) findViewById(R.id.btn_supplier);
        btnRestaurant = (Button) findViewById(R.id.btn_restaurant);
        regText = (EditText) findViewById(R.id.editText_regName);
        listView = (ListView) findViewById(R.id.listV_Login);

        String login = getLoginType();

        if (LOGIN_TYPE_1 == login){
            Intent intent = new Intent(MainActivity.this, RestaurantMainActivity.class);
            startActivity(intent);
        }
        else if (LOGIN_TYPE_2 == login){
            Intent intent = new Intent(MainActivity.this, SupplierMainActivity.class);
            startActivity(intent);
        }

        populateListView();

        btnSupplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String puffer = regText.getText().toString();
                if(puffer != "" && puffer != null && !puffer.isEmpty()){
                    addRegistration(puffer, "");
                    databaseHelper.login("", puffer);
                    regText.setText("");
                }
                Intent intent = new Intent(MainActivity.this, SupplierMainActivity.class);
                startActivity(intent);
            }
        });

        btnRestaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String puffer = regText.getText().toString();
                if(puffer != "" && puffer != null && !puffer.isEmpty()){
                    addRegistration("", puffer);
                    databaseHelper.login(puffer, "");
                    regText.setText("");
                }
                Intent intent = new Intent(MainActivity.this, RestaurantMainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void addRegistration(String supplier, String restaurant){
        boolean insertData;
        insertData = databaseHelper.addRegistration(supplier, restaurant);
        if (insertData)
            toastMessage("Felhasználó/étterem létrehozva #" + supplier + restaurant + "#");
        else
            toastMessage("HIBA a regisztráció során!");
    }

    private String getLoginName(){
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        Cursor data = databaseHelper.getLogins();
        String result = "";
        while (data.moveToNext()){
            result = data.getString(1);
        }
        return result;
    }

    private String getLoginType(){
        Cursor data = databaseHelper.getLogins();
        String tmp;
        while(data.moveToNext()){
            tmp = data.getString(1);
            if (tmp != "" && tmp != null && !tmp.isEmpty()){
                return LOGIN_TYPE_1;
            }
            tmp = data.getString(2);
            if (tmp != "" && tmp != null && !tmp.isEmpty()){
                return LOGIN_TYPE_2;
            }
        }
        return LOGIN_TYPE_3;
    }

    private void populateListView(){
        Log.d(TAG, "Displaying registrations in listView");
        final Cursor data = databaseHelper.getTable(databaseHelper.REGISTRATIONS_TABLE);
        final int restaurantFirstItemPosition;

        ArrayList<String> arrayList = new ArrayList<>();
        String puffer;
        while (data.moveToNext()){
            puffer = data.getString(databaseHelper.REGISTRATION_SUPPLIER_FIELD);
            if (puffer != "" && puffer != null && !puffer.isEmpty()){
                arrayList.add(puffer);
            }
        }
        restaurantFirstItemPosition = arrayList.size();
        data.moveToFirst();
        while (data.moveToNext()){
            puffer = data.getString(databaseHelper.REGISTRATION_RESTAURANT_FIELD);
            if (puffer != "" && puffer != null && !puffer.isEmpty()){
                arrayList.add(puffer);
            }
        }

        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = parent.getItemAtPosition(position).toString();
                Log.d(TAG, "onItemClick: You Clicked on " + name);
                if (position < restaurantFirstItemPosition) {
                    databaseHelper.login("", name);
                    Intent intent = new Intent(MainActivity.this, SupplierMainActivity.class);
                    startActivity(intent);
                }
                else {
                    databaseHelper.login(name, "");
                    Intent intent = new Intent(MainActivity.this, RestaurantMainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        populateListView();
    }

    private void toastMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
