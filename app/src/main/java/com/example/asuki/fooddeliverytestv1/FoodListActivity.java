package com.example.asuki.fooddeliverytestv1;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Asuki on 2017. 11. 25..
 */

public class FoodListActivity extends AppCompatActivity {

    private final String TAG = "FoodListActivity";
    private Button btnAdd;
    private DatabaseHelper databaseHelper;
    private EditText editTextMenuCode, editTextMenuName;
    private ListView listViewMenu;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurant_menu);

        databaseHelper = new DatabaseHelper(this);
        editTextMenuCode = (EditText) findViewById(R.id.editText_menu_code);
        editTextMenuName = (EditText) findViewById(R.id.editText_menu_name);
        listViewMenu = (ListView) findViewById(R.id.listV_menu);

        btnAdd = (Button) findViewById(R.id.btn_menuAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(databaseHelper.addFood(editTextMenuCode.getText().toString(), editTextMenuName.getText().toString())) {
                    populateListView();
                    editTextMenuCode.setText("");
                    editTextMenuName.setText("");
                }
                else
                    toastMessage("Hiba a menü hozzáadása során");
            }
        });
        populateListView();
    }

    private void populateListView(){
        Log.d(TAG, "Displaying food list");

        Cursor data = databaseHelper.getTable(databaseHelper.FOOD_TABLE);
        ArrayList<Food> arrayList = new ArrayList<>();

        while (data.moveToNext()){
            Food tmp = new Food(data.getString(1), data.getString(2));
            arrayList.add(tmp);
        }

        ListAdapter adapter = new FoodListAdapter(this, R.layout.adapter_view_double_col, arrayList);
        listViewMenu.setAdapter(adapter);

    }

    private void toastMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
