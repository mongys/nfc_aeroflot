package com.example.peresbor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.List;

public class MainScreen extends AppCompatActivity {

    private Spinner spinner;
    private Button confirmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        spinner = findViewById(R.id.spinner);
        confirmButton = findViewById(R.id.confirmButton);

        String[] options = {"B777-300", "Макет"};
        SecondDatabaseHelper dbMaker=new SecondDatabaseHelper(this);
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        SQLiteDatabase db2= dbMaker.getWritableDatabase();
        dbHelper.setAllIsOnBoardToFalse();
        dbMaker.setAllIsOnBoardToFalse();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String selectedItem = spinner.getSelectedItem().toString();


                Intent intent;
                if("B777-300".equals(selectedItem)) {
                    intent = new Intent(MainScreen.this, PlaneScreen.class);
                } else {
                    intent = new Intent(MainScreen.this, StandScreen.class);
                }

                startActivity(intent);
            }
        });



        ContentValues values = new ContentValues();
        values.put("is_on_board", 0); // 0 представляет false

    }
    public void personClick(View v){
        finish();
        Intent intent=new Intent(this, PersonCheck.class);
        startActivity(intent);
    }
}