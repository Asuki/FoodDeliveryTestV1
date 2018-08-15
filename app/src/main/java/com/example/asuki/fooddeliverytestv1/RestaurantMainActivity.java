package com.example.asuki.fooddeliverytestv1;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Asuki on 2017. 11. 22..
 */

public class RestaurantMainActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private TextView textView;
    private Button btnLogut, btnEditOrders, btnEditSuppliers, btnEditMenu, btnEditClients;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurant_main);
        databaseHelper = new DatabaseHelper(this);
        textView = findViewById(R.id.textView_restaurant);
        textView.setText(getLoginName());

        btnEditClients = findViewById(R.id.btn_edit_clients);
        btnEditClients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RestaurantMainActivity.this, ClientListActivity.class);
                startActivity(intent);
            }
        });

        btnEditOrders = findViewById(R.id.btn_edit_orders);
        btnEditOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RestaurantMainActivity.this, AddOrderActivity.class);
                startActivity(intent);
            }
        });

        btnLogut = findViewById(R.id.btn_logoutRestaurant);
        btnLogut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseHelper.logout();
                finish();
                toastMessage("Sikeres kilépés");
            }
        });

        btnEditSuppliers = findViewById(R.id.btn_edit_suppliers);
        btnEditSuppliers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RestaurantMainActivity.this, SuppliersListActivity.class);
                startActivity(intent);
            }
        });

        btnEditMenu = findViewById(R.id.btn_edit_menu);
        btnEditMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RestaurantMainActivity.this, FoodListActivity.class);
                startActivity(intent);
            }
        });

    }

    private String getLoginName(){
        // TODO: check login
        Cursor data = databaseHelper.getLogins();
        String result = "";
        while (data.moveToNext()){
            result = data.getString(1);
        }
        return result;
    }

    private void toastMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
