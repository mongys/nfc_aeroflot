package com.example.peresbor;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.UnsupportedEncodingException;
import java.util.List;
public class CheckScreen extends AppCompatActivity {
    public static final String Error_detected = "No NFC detected";
    NfcAdapter nfcAdapter;
    PendingIntent pendingIntent;
    IntentFilter tagDetected;
    boolean writeMode;
    Tag myTag;
    Context context;
    TextView nfc_contents;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_screen);
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

// Получите список AircraftItem
        List<AircraftItem> allAircraftItems = dbHelper.getItemsWithIsOnBoardFalse();

// Создайте экземпляр AircraftItemAdapter
        AircraftItemAdapter adapter = new AircraftItemAdapter(allAircraftItems);

// Настройте RecyclerView

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Создание и добавление записей в базу данных

        db.close();
    }

    public void backClick(View v){
        finish();
        Intent intent=new Intent(this, PlaneScreen.class);
        startActivity(intent);
    }
    public void switchClick(View v){
        finish();
        Intent intent=new Intent(this, CheckScreenTrue.class);
        startActivity(intent);
    }

}