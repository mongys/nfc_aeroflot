package com.example.peresbor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class CheckScreenExpired2 extends AppCompatActivity {

    public static final String Error_detected = "No NFC detected";
    NfcAdapter nfcAdapter;
    PendingIntent pendingIntent;
    IntentFilter tagDetected;
    boolean writeMode;
    Tag myTag;
    Context context;
    TextView nfc_contents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_check_screen_expired);
        SecondDatabaseHelper dbHelper = new SecondDatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        context = this;
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (nfcAdapter == null) {
            Toast.makeText(this, "This device does not support NFC", Toast.LENGTH_LONG).show();
            finish();
        }

        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);
        tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        tagDetected.addCategory(Intent.CATEGORY_DEFAULT);
// Получите список AircraftItem
        List<AircraftItem> allAircraftItems = dbHelper.getItemsExpiringSoon();

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
        Intent intent=new Intent(this, StandScreen.class);
        startActivity(intent);
    }
    public void switchClick(View v){
        finish();
        Intent intent=new Intent(this, CheckScreen2.class);
        startActivity(intent);
    }
}