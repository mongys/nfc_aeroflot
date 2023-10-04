package com.example.peresbor;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.UnsupportedEncodingException;
import java.util.List;
import android.Manifest;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PlaneScreen extends AppCompatActivity {
    public static final String Error_detected = "No NFC detected";
    private static final int WRITE_STORAGE_PERMISSION_REQUEST_CODE = 1;
    NfcAdapter nfcAdapter;
    PendingIntent pendingIntent;
    IntentFilter tagDetected;
    boolean writeMode;
    Tag myTag;
    Context context;
    TextView nfc_contents;
    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plane_screen);
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
// Создаем новый объект AircraftItem


// Обновляем отображение, если необходимо

        nfc_contents = findViewById(R.id.nfc_contents);
        context = this;
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (nfcAdapter == null) {
            Toast.makeText(this, "This device does not support NFC", Toast.LENGTH_LONG).show();
            finish();
        }

        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);
        tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        tagDetected.addCategory(Intent.CATEGORY_DEFAULT);

        // Проверяем разрешение на WRITE_EXTERNAL_STORAGE и запрашиваем его, если не предоставлено
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_STORAGE_PERMISSION_REQUEST_CODE);
        }
    }



    public void backClick(View v){
        finish();
        Intent intent=new Intent(this, MainScreen.class);
        startActivity(intent);
    }

    public void reportClick(View v){
        finish();
        Intent intent=new Intent(this, CheckScreen.class);
        startActivity(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        readFromIntent(intent);
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
            myTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        WriteModeOff();
    }

    @Override
    public void onResume() {
        super.onResume();
        WriteModeOn();
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Retrieve the list of aircraft items from the database
        List<AircraftItem> allAircraftItems = dbHelper.getAllAircraftItems();

        // Update the views based on the data
        for (AircraftItem item : allAircraftItems) {
            String itemLocation = item.getItemLocation();
            int viewId = getResources().getIdentifier(itemLocation, "id", getPackageName());
            View locationView = findViewById(viewId);

            if (locationView != null) {
                if (item.isOnBoard()) {
                    // If isOnBoard is true, set the background color to green (or any other color)
                    locationView.setBackgroundColor(Color.GREEN);
                } else {
                    // If isOnBoard is false, set the background color to red (or any other color)
                    locationView.setBackgroundColor(Color.RED);
                }
            }
        }

        List<AircraftItem> itemsWithExpiryLessThanAMonth = dbHelper.getItemsWithExpiryLessThanAMonth();

        for (AircraftItem item : itemsWithExpiryLessThanAMonth) {
            String itemLocation = item.getItemLocation();
            int viewId = getResources().getIdentifier(itemLocation, "id", getPackageName());
            View locationView = findViewById(viewId);

            if (locationView != null && item.isOnBoard()) {
                // Устанавливаем цвет фона на желтый для элементов с просроченным сроком годности
                locationView.setBackgroundColor(Color.YELLOW);
            }
        }
    }

    public void WriteModeOn() {
        writeMode = true;
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, new IntentFilter[]{tagDetected}, null);
    }

    public void WriteModeOff() {
        writeMode = false;
        nfcAdapter.disableForegroundDispatch(this);
    }

    private void readFromIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            if (rawMsgs != null) {
                NdefMessage[] msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                }
                buildTagViews(msgs);
            }
        }
    }

    private void buildTagViews(NdefMessage[] msgs) {
        if (msgs == null || msgs.length == 0) return;
        String text = "";
        byte[] payload = msgs[0].getRecords()[0].getPayload();
        String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";
        int languageCodeLength = payload[0] & 0063;
        try {
            text = new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }



        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        dbHelper.updateIsOnBoardStatus(text, true);

}
    public void exportToCSV(View view) {
        // Создайте экземпляр DatabaseHelper для доступа к базе данных
        DatabaseHelper dbHelper = new DatabaseHelper(this);

        // Получите все записи из базы данных
        List<AircraftItem> aircraftItems = dbHelper.getAllAircraftItems();

        // Проверьте, есть ли записи для экспорта
        if (aircraftItems.isEmpty()) {
            Toast.makeText(this, "Нет данных для экспорта в CSV.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Создайте строку для записи заголовков CSV-файла
        StringBuilder csvData = new StringBuilder();
        csvData.append("Aircraft Type,Item Name,Item Location,Start Date,End Date,Is On Board\n");

        // Добавьте данные из базы данных в строку CSV
        for (AircraftItem item : aircraftItems) {
            String aircraftType = item.getAircraftType();
            String itemName = item.getItemName();
            String itemLocation = item.getItemLocation();
            String startDate = item.getStartDate();
            String endDate = item.getEndDate();
            String isOnBoard = item.isOnBoard() ? "Да" : "Нет";

            // Формируйте строку CSV для каждой записи
            String csvRow = String.format("%s,%s,%s,%s,%s,%s\n",
                    aircraftType, itemName, itemLocation, startDate, endDate, isOnBoard);
            csvData.append(csvRow);
        }

        // Сохраните данные в CSV-файле
        if (CSVUtils.saveToCSVFile(this, "aircraft_data.csv", csvData.toString())) {
            Toast.makeText(this, "Данные успешно экспортированы в CSV.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Не удалось экспортировать данные в CSV.", Toast.LENGTH_SHORT).show();
        }
    }





}