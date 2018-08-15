package com.example.asuki.fooddeliverytestv1;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Asuki on 2017. 11. 26..
 */

public class AddOrderActivity extends AppCompatActivity {

    private final String TAG = "AddOrderActivity";
    private EditText editTextOrdersName, editTextOrdersAddress, editTextOrdersMobile, editTextOrdersEmail;
    private Button btnAddOrder, btnFindClient;
    private Spinner spinnerSupplier, spinnerFood;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurant_order);

        databaseHelper = new DatabaseHelper(this);

        editTextOrdersName = findViewById(R.id.editText_orders_name);
        editTextOrdersAddress = findViewById(R.id.editText_orders_address);
        editTextOrdersMobile = findViewById(R.id.editText_orders_mobile);
        editTextOrdersEmail = findViewById(R.id.editText_orders_email);

        btnAddOrder = findViewById(R.id.btn_addOrder);
        btnFindClient = findViewById(R.id.btn_find);

        spinnerSupplier = findViewById(R.id.spinner_suppliers);
        spinnerFood = findViewById(R.id.spinner_foods);

        // TODO: Create a menu to modify the supplier of a client
        btnAddOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobile = editTextOrdersMobile.getText().toString();
                String name = editTextOrdersName.getText().toString();
                String address = editTextOrdersAddress.getText().toString();
                // The mobile, name and address fields are required
                if(!mobile.isEmpty() && !name.isEmpty() && !address.isEmpty()) {
                    Cursor dataSupplier = databaseHelper.getSupplierID(spinnerSupplier.getSelectedItem().toString());
                    Cursor dataClient = databaseHelper.getClientList(mobile);
                    Cursor dataFood = databaseHelper.getFoodID(spinnerFood.getSelectedItem().toString().substring(0, 1));
                    int supplierID = -1;
                    int clientID = -1;
                    int foodID = -1;

                    // Get the supplierID
                    while (dataSupplier.moveToNext()) {
                        supplierID = dataSupplier.getInt(databaseHelper.ID_POSITION);
                    }
                    // Get the clientID
                    while (dataClient.moveToNext()){
                        clientID = dataClient.getInt(databaseHelper.ID_POSITION);
                    }
                    // Get the foodID
                    while(dataFood.moveToNext()){
                        foodID = dataFood.getInt(databaseHelper.ID_POSITION);
                    }

                    // if the clientID is -1, there is no such a client, so we have to add it into clients table
                    if (clientID == -1) {
                        Log.d(TAG, "-- Adding new client --");
                        databaseHelper.addClient(name, address, mobile,
                                editTextOrdersEmail.getText().toString(), supplierID);
                        // Get the clientID again
                        dataClient = databaseHelper.getClientList(mobile);
                        while (dataClient.moveToNext()){
                            clientID = dataClient.getInt(0);
                        }
                        toastMessage("Új kliens hozzáadva: " + name);
                    }
                    Log.d(TAG, "-------------- " + supplierID + " " + clientID + " " +foodID);
                    databaseHelper.addOrder(supplierID, clientID, foodID, getToday());

                    clearFields();
                    editTextOrdersMobile.setText("");
                    toastMessage("Rendelés hozzáadva");
                }
                else{
                    toastMessage("A név, a cím és a telefonszám kitöltése kötelező! ");
                }
            }
        });

        btnFindClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearFields();
                String name =  "", address = "",  mobile = "", email = "";
                int supplierId = -1;
                Cursor data = databaseHelper.getClientList(editTextOrdersMobile.getText().toString());
                while (data.moveToNext()){
                    name = data.getString(1);
                    address = data.getString(2);
                    mobile = data.getString(3);
                    email = data.getString(4);
                    supplierId = data.getInt(5);
                }
                if (mobile != "" && mobile != null && !mobile.isEmpty()){
                    editTextOrdersName.setText(name);
                    editTextOrdersAddress.setText(address);
                    editTextOrdersEmail.setText(email);
                }
                Log.d(TAG, "Button find client was clicked. The supplierID is: " + supplierId);
                populateSupplierSpinner(supplierId);
            }
        });
        getToday();
        populateFoddSpinner();
        populateSupplierSpinner(-1);
    }

    private String getToday()
    {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => "+c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c.getTime());
        // formattedDate have current date
        return formattedDate;
    }

    private boolean textIsEmpty(String text){
        Log.d(TAG, "Checking text if empty: " + text);
        if (text == "" || text == null || textIsEmpty(text))
            return true;
        return false;
    }

    private void clearFields(){
        editTextOrdersName.setText("");
        editTextOrdersEmail.setText("");
        editTextOrdersAddress.setText("");
    }

    private void populateSupplierSpinner(int id){
        if (-1 == id){
            Log.d(TAG, "populateSupplierSpinner: listing all suppliers");
            Cursor data = databaseHelper.getTable(databaseHelper.DELIVERS_TABLE);
            ArrayList<String> deliverList = new ArrayList<>();

            while(data.moveToNext()){
                deliverList.add(data.getString(1));
            }

            SpinnerAdapter spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, deliverList);
            spinnerSupplier.setAdapter(spinnerAdapter);
        }
        else{
            Log.d(TAG, "populateSupplierSpinner: listing the supplier with the next id: " + id);
            Cursor data = databaseHelper.getSupplier(id);
            ArrayList<String> supplierList = new ArrayList<>();

            while(data.moveToNext()){
                String name = data.getString(1);
                supplierList.add(name);
            }

            SpinnerAdapter spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, supplierList);
            spinnerSupplier.setAdapter(spinnerAdapter);
        }
    }

    private void populateFoddSpinner(){
        Log.d(TAG, "Adding foods to spinnerSupplier");
        Cursor data = databaseHelper.getTable(databaseHelper.FOOD_TABLE);
        StringBuilder sb;

        ArrayList<String> arrayList = new ArrayList<>();
        while (data.moveToNext()){
            sb = new StringBuilder();
            sb.append(data.getString(1));
            sb.append(" - ");
            sb.append(data.getString(2));
            arrayList.add(sb.toString());
        }

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayList);
        spinnerFood.setAdapter(adapter);

    }

    private void toastMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}



















