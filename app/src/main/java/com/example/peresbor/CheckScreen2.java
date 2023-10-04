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

import java.util.List;

public class CheckScreen2 extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_screen);
        SecondDatabaseHelper dbHelper = new SecondDatabaseHelper(this);
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
        Intent intent=new Intent(this, StandScreen.class);
        startActivity(intent);
    }
    public void switchClick(View v){
        finish();
        Intent intent=new Intent(this, CheckScreenTrue2.class);
        startActivity(intent);
    }
}