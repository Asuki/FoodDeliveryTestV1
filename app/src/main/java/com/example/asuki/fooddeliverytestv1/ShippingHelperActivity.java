package com.example.asuki.fooddeliverytestv1;

import android.database.Cursor;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Asuki on 2017. 12. 02..
 */

public class ShippingHelperActivity extends AppCompatActivity {
    private static final String TAG = "ShippingHelperActivity";
    private ListView listViewShipping;
    private DatabaseHelper databaseHelper;
    private TextToSpeech textToSpeech;
    int speachPosition;
    Button btnNext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.supplier_shipping);

        listViewShipping = findViewById(R.id.listV_shipping);
        databaseHelper = new DatabaseHelper(this);

        speachPosition = 0;
        textToSpeech=new TextToSpeech(ShippingHelperActivity.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status==TextToSpeech.SUCCESS)
                {
                }
                else
                {
                    toastMessage("A felolvasási funkció nem elérhető ezen az eszközön!");
                }
            }
        });

        btnNext = findViewById(R.id.btn_next);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String puffer = listViewShipping.getAdapter().getItem(speachPosition++).toString();
                    textToSpeech.speak(puffer, TextToSpeech.QUEUE_FLUSH, null);
                }
                catch (IndexOutOfBoundsException e){
                    speachPosition = 0;
                    textToSpeech.speak("Az összes elem végig olvasva!", TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        });

        populateListView();
    }

    /**
     * Fill up the list view with sihpping data
     */
    private void populateListView(){
        Log.d(TAG, "Listing shipping list");

        Cursor data = databaseHelper.getTable(databaseHelper.DELIVERY_TABLE);
        ArrayList<String> arrayList = new ArrayList<>();

        while(data.moveToNext()){
            arrayList.add("Név: " + data.getString(2) + ". Cím: " + data.getString(3) + ". Menü: " + data.getString(6) + "! " + data.getString(7));
        }

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayList);
        listViewShipping.setAdapter(adapter);
    }

    private void TTS(String textToSpeach){

    }

    private void toastMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
