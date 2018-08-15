package com.example.asuki.fooddeliverytestv1;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by Asuki on 2017. 11. 21..
 */

public class SupplierMainActivity extends AppCompatActivity {
    private static final String TAG = "SupplierMainActivity";

    private TextView textView;
    private Button btnLogout, btnShipping, btnFileApplication;
    private DatabaseHelper databaseHelper;
    private FileManager fileManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.supplier_main);

        databaseHelper = new DatabaseHelper(this);
        textView = findViewById(R.id.textView_supplierText);
        textView.setText("Szia " + getLoginName() + "!");
        fileManager = new FileManager();

        btnLogout = findViewById(R.id.btn_logoutSupplier);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            // TODO C: float this.
            @Override
            public void onClick(View v) {
                databaseHelper.logout();
                finish();
                toastMessage("Sikeres kilépés");
            }
        });

        btnShipping = findViewById(R.id.btn_shipping);
        btnShipping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SupplierMainActivity.this, ShippingHelperActivity.class);
                startActivity(intent);
            }
        });

        btnFileApplication = findViewById(R.id.btn_file_apply);
        btnFileApplication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    fileManager.LoadCSVToDeliveryTable("fdl_shipping.csv", databaseHelper);
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d(TAG, e.toString());
                }
            }
        });

    }

    private String getLoginName(){
        // TODO: check login
        Cursor data = databaseHelper.getLogins();
        String result = "";
        while (data.moveToNext()){
            result = data.getString(2);
        }
        return result;
    }

    private void toastMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}
