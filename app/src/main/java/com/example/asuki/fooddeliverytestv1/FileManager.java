package com.example.asuki.fooddeliverytestv1;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.nfc.Tag;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.asuki.fooddeliverytestv1.CSV.CSVReader;
import com.example.asuki.fooddeliverytestv1.CSV.CSVWriter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Asuki on 2017. 12. 15..
 */

public class FileManager {
    private final String TAG = "FileManager";

    public FileManager() {
    }

    public void SaveCursorToCSV(String fileName, Cursor cursor) {
        File exportDir = new File(Environment.getExternalStorageDirectory(), Environment.DIRECTORY_DOWNLOADS );
        if (!exportDir.exists()) {
            exportDir.mkdirs();
        }
        File file = new File(exportDir, fileName);
        try {
            Log.d(TAG, "start saving to: " + file.getPath());
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            int cursorCount = cursor.getColumnCount();
            String puffer[] = new String [cursorCount];
            CSVWriter csvWriter = new CSVWriter(new FileWriter(file));
            while (cursor.moveToNext()){
                for (int i = 0; i < cursorCount; i++) {
                    puffer[i] = cursor.getString(i);
                }
                csvWriter.writeNext(puffer);
            }
            csvWriter.writeNext(cursor.getColumnNames());
            csvWriter.close();
            Log.d(TAG, "Save was successfull");
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }
    }

    public void sendFileViaBluetooth(String fileName){
        
    }

    public void LoadCSVToDeliveryTable(String fileName, DatabaseHelper databaseHelper) throws IOException {
        File importDir = new File(Environment.getExternalStorageDirectory(), Environment.DIRECTORY_DOWNLOADS );
        File file = new File(importDir, fileName);
        CSVReader csvReader = new CSVReader(new FileReader(file));
        try {
            String[] puffer;
            while ((puffer = csvReader.readNext()) != null) {
                // If puffer[0] is equals to "id", then the line is the headline, so we reached the end of the csv
                if (puffer[0].toLowerCase().equals("id"))
                    break;
                databaseHelper.addDeliveryItem(puffer[1], puffer[2], puffer[3], puffer[4], puffer[5], puffer[6], puffer[7]);
                Log.d(TAG, " Adding Delivery item was successful");
            }
        }
        catch (Exception e){
            Log.d(TAG, e.toString());
        }
        finally {
            csvReader.close();
        }
    }
}