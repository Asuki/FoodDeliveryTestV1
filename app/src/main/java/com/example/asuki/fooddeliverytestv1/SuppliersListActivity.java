package com.example.asuki.fooddeliverytestv1;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Asuki on 2017. 11. 24..
 */

public class SuppliersListActivity extends AppCompatActivity {

    Button btnAddSupplier, btnSendShippingList, btnModifySupplierName;
    DatabaseHelper databaseHelper;
    EditText editTextSupplier, editTextSupplierNewName;
    ListView listvSuppliers;
    private final String TAG = "SuppliersListActivity";
    private int supplierID;
    private String supplierName;

    private FileManager fileManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurant_suppliers);

        databaseHelper = new DatabaseHelper(this);
        btnAddSupplier = findViewById(R.id.btn_add_supplier);
        editTextSupplier = findViewById(R.id.editText_supplier_name);
        listvSuppliers = findViewById(R.id.listV_suppliers);
        supplierID = -1;
        supplierName = "";
        editTextSupplierNewName = findViewById(R.id.editText_supplier_new_name);

        fileManager = new FileManager();

        requestPermissions();

        btnAddSupplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String puffer = editTextSupplier.getText().toString();
                if (puffer != "" && puffer != null && !puffer.isEmpty()) {
                    databaseHelper.addSupplier(puffer);
                }
                populateListView();
                editTextSupplier.setText("");
            }
        });

        btnSendShippingList = findViewById(R.id.btn_send_shipping_list);
        btnSendShippingList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor data = databaseHelper.getSupplierID(editTextSupplier.getText().toString());
                int supplierId = -1;

                while (data.moveToNext()) {
                    supplierId = data.getInt(databaseHelper.ID_POSITION);
                }
                if (-1 == supplierId)
                    toastMessage("Válassz ki egy kiszállítót!");
                else {
                    databaseHelper.createShippingTable(supplierId);
                    // Saving important data to csv for sending via bluetooth
                    fileManager.SaveCursorToCSV("fdl_clients.csv", databaseHelper.getTable(databaseHelper.CLIENTS_TABLE));
                    fileManager.SaveCursorToCSV("fdl_food.csv", databaseHelper.getTable(databaseHelper.FOOD_TABLE));
                    fileManager.SaveCursorToCSV("fdl_shipping.csv", databaseHelper.getTable(databaseHelper.DELIVERY_TABLE));
                }
            }
        });

        btnModifySupplierName = findViewById(R.id.btn_modifySupplierName);
        btnModifySupplierName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldSupplierName = editTextSupplier.getText().toString();
                String newSupplierName =  editTextSupplierNewName.getText().toString();
                if(!oldSupplierName.isEmpty() && !newSupplierName.isEmpty()) {
                    if(databaseHelper.modifySupplierName(oldSupplierName, newSupplierName)) {
                        int supplierId = -1;
                        Cursor data = databaseHelper.getSupplierID(oldSupplierName);
                        while (data.moveToNext()){
                            supplierId = data.getInt(databaseHelper.ID_POSITION);
                        }
                        if (-1 != supplierId) {
                            editTextSupplier.setText("");
                            editTextSupplierNewName.setText("");
                            toastMessage("A " + oldSupplierName + " felhasználónév módosult a következőre: " + newSupplierName);
                            populateListView();
                        }
                        else{
                            toastMessage("Hibás a kiszállító neve. Próbáljon a listára kattintva választani");
                        }
                    }
                    else {
                        toastMessage("Hiba a módosítás során.");
                    }
                }
                else{
                    toastMessage("A felhasználónevet és az új nevet is meg kell adni");
                }
            }
        });
        populateListView();
    }

    private void requestPermissions(){
        int permissionCheck = ContextCompat.checkSelfPermission(SuppliersListActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED){
            Log.d(TAG, "Write to external storage permission is denied");
            if (ActivityCompat.shouldShowRequestPermissionRationale(SuppliersListActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            }
            ActivityCompat.requestPermissions(SuppliersListActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

        permissionCheck = ContextCompat.checkSelfPermission(SuppliersListActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED){
            Log.d(TAG, "Write to external storage permission is denied");
            if (ActivityCompat.shouldShowRequestPermissionRationale(SuppliersListActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
            }
            ActivityCompat.requestPermissions(SuppliersListActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
    }

    public void populateListView(){
        Log.d(TAG, "Displaying registrations in listView");
        final Cursor data = databaseHelper.getTable(databaseHelper.DELIVERS_TABLE);

        ArrayList<String> arrayList = new ArrayList<>();
        String puffer;
        while (data.moveToNext()){
            puffer = data.getString(1);
            arrayList.add(puffer);
        }

        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList);
        listvSuppliers.setAdapter(adapter);

        listvSuppliers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                supplierName = parent.getItemAtPosition(position).toString();
                editTextSupplier.setText(supplierName);
                Log.d(TAG, "onItemClick: You clicked on " + supplierName);

                Cursor data = databaseHelper.getSupplierID(supplierName);
                supplierID  = -1;

                while (data.moveToNext()){
                    supplierID = data.getInt(databaseHelper.ID_POSITION);
                }
                Log.d(TAG, "onItemClick: the supplierID after click = " + supplierID);
                if (-1 == supplierID) {
                    toastMessage("Hiba a kiszállító törlése során");
                    supplierName = "";
                }
            }
        });
    }

    private void toastMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}






























