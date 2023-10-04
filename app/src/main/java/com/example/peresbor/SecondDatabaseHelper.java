package com.example.peresbor;
import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
public class SecondDatabaseHelper extends SQLiteOpenHelper{
    private static final String DATABASE_NAME = "my_second_database.db";
    private static final int DATABASE_VERSION = 1;
    private Context context;
    public SecondDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Определите структуру таблицы для хранения данных AircraftItem.
        String createTableQuery = "CREATE TABLE IF NOT EXISTS aircraft_items (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "aircraft_type TEXT," +
                "item_name TEXT," +
                "item_location TEXT," +
                "start_date TEXT," +
                "end_date TEXT," +
                "is_on_board INTEGER);";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Обновите структуру таблицы, если это необходимо при изменении версии базы данных.
    }

    // Методы для работы с объектами AircraftItem:

    public void insertAircraftItem(AircraftItem aircraftItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("aircraft_type", aircraftItem.getAircraftType());
        values.put("item_name", aircraftItem.getItemName());
        values.put("item_location", aircraftItem.getItemLocation());
        values.put("start_date", aircraftItem.getStartDate());
        values.put("end_date", aircraftItem.getEndDate());
        values.put("is_on_board", aircraftItem.isOnBoard() ? 1 : 0);
        db.insert("aircraft_items", null, values);
        db.close();
    }

    public List<AircraftItem> getAllAircraftItems() {
        List<AircraftItem> aircraftItemList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM aircraft_items", null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("id"));
                @SuppressLint("Range") String aircraftType = cursor.getString(cursor.getColumnIndex("aircraft_type"));
                @SuppressLint("Range") String itemName = cursor.getString(cursor.getColumnIndex("item_name"));
                @SuppressLint("Range") String itemLocation = cursor.getString(cursor.getColumnIndex("item_location"));
                @SuppressLint("Range") String startDate = cursor.getString(cursor.getColumnIndex("start_date"));
                @SuppressLint("Range") String endDate = cursor.getString(cursor.getColumnIndex("end_date"));
                @SuppressLint("Range") int isOnBoardInt = cursor.getInt(cursor.getColumnIndex("is_on_board"));
                boolean isOnBoard = isOnBoardInt == 1;

                AircraftItem aircraftItem = new AircraftItem(id, aircraftType, itemName, itemLocation, startDate, endDate, isOnBoard);
                aircraftItemList.add(aircraftItem);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return aircraftItemList;
    }

    public void updateAircraftItem(AircraftItem aircraftItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("aircraft_type", aircraftItem.getAircraftType());
        values.put("item_name", aircraftItem.getItemName());
        values.put("item_location", aircraftItem.getItemLocation());
        values.put("start_date", aircraftItem.getStartDate());
        values.put("end_date", aircraftItem.getEndDate());
        values.put("is_on_board", aircraftItem.isOnBoard() ? 1 : 0);
        db.update("aircraft_items", values, "id=?", new String[]{String.valueOf(aircraftItem.getId())});
        db.close();
    }

    public void deleteAircraftItem(int itemId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("aircraft_items", "id=?", new String[]{String.valueOf(itemId)});
        db.close();
    }
    public List<AircraftItem> getItemsWithIsOnBoardTrue() {
        List<AircraftItem> itemList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {
                "id",
                "aircraft_type",
                "item_name",
                "item_location",
                "start_date",
                "end_date",
                "is_on_board"
        };

        String selection = "is_on_board = ?";
        String[] selectionArgs = { "1" }; // 1 означает true

        Cursor cursor = db.query("aircraft_items", columns, selection, selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("id"));
                @SuppressLint("Range") String aircraftType = cursor.getString(cursor.getColumnIndex("aircraft_type"));
                @SuppressLint("Range") String itemName = cursor.getString(cursor.getColumnIndex("item_name"));
                @SuppressLint("Range") String itemLocation = cursor.getString(cursor.getColumnIndex("item_location"));
                @SuppressLint("Range") String startDate = cursor.getString(cursor.getColumnIndex("start_date"));
                @SuppressLint("Range") String endDate = cursor.getString(cursor.getColumnIndex("end_date"));
                boolean isOnBoard = true;

                AircraftItem item = new AircraftItem(id, aircraftType, itemName, itemLocation, startDate, endDate, isOnBoard);
                itemList.add(item);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return itemList;
    }
    public void updateIsOnBoardStatus(String tagId, boolean isOnBoard) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("is_on_board", isOnBoard ? 1 : 0);

        int rowsAffected = db.update("aircraft_items", values, "id=?", new String[]{tagId});

        // Вывести сообщение для отладки
        Log.d("UpdateStatus", "Rows affected: " + rowsAffected);
        Log.d("UpdateStatus", "TagId: " + tagId);
        Log.d("UpdateStatus", "Values: " + values.toString());

        if (rowsAffected > 0) {
            Toast.makeText(context, "Состояние is_on_board обновлено успешно.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Не удалось обновить состояние is_on_board.", Toast.LENGTH_SHORT).show();
        }

        db.close();
    }
    public List<AircraftItem> getItemsWithIsOnBoardFalse() {
        List<AircraftItem> itemList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {
                "id",
                "aircraft_type",
                "item_name",
                "item_location",
                "start_date",
                "end_date",
                "is_on_board"
        };

        String selection = "is_on_board = ?";
        String[] selectionArgs = { "0" }; // 0 означает false

        Cursor cursor = db.query("aircraft_items", columns, selection, selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("id"));
                @SuppressLint("Range") String aircraftType = cursor.getString(cursor.getColumnIndex("aircraft_type"));
                @SuppressLint("Range") String itemName = cursor.getString(cursor.getColumnIndex("item_name"));
                @SuppressLint("Range") String itemLocation = cursor.getString(cursor.getColumnIndex("item_location"));
                @SuppressLint("Range") String startDate = cursor.getString(cursor.getColumnIndex("start_date"));
                @SuppressLint("Range") String endDate = cursor.getString(cursor.getColumnIndex("end_date"));
                @SuppressLint("Range") boolean isOnBoard = cursor.getInt(cursor.getColumnIndex("is_on_board")) == 1;

                AircraftItem item = new AircraftItem(id, aircraftType, itemName, itemLocation, startDate, endDate, isOnBoard);
                itemList.add(item);
            } while (cursor.moveToNext());
        }

        cursor.close();


        return itemList;
    }
    public List<AircraftItem> getItemsExpiringSoon() {
        List<AircraftItem> itemList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Получите текущую дату в формате "yyyy-MM-dd"
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        String currentDate = sdf.format(calendar.getTime());

        // Вычислите дату, которая находится через 1 месяц от текущей даты
        calendar.add(Calendar.MONTH, 1);
        String oneMonthLaterDate = sdf.format(calendar.getTime());

        String[] columns = {
                "id",
                "aircraft_type",
                "item_name",
                "item_location",
                "start_date",
                "end_date",
                "is_on_board"
        };

        String selection = "end_date >= ? AND end_date <= ?";
        String[] selectionArgs = { currentDate, oneMonthLaterDate };

        Cursor cursor = db.query("aircraft_items", columns, selection, selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("id"));
                @SuppressLint("Range") String aircraftType = cursor.getString(cursor.getColumnIndex("aircraft_type"));
                @SuppressLint("Range") String itemName = cursor.getString(cursor.getColumnIndex("item_name"));
                @SuppressLint("Range") String itemLocation = cursor.getString(cursor.getColumnIndex("item_location"));
                @SuppressLint("Range") String startDate = cursor.getString(cursor.getColumnIndex("start_date"));
                @SuppressLint("Range") String endDate = cursor.getString(cursor.getColumnIndex("end_date"));
                @SuppressLint("Range") boolean isOnBoard = cursor.getInt(cursor.getColumnIndex("is_on_board")) == 1;

                AircraftItem item = new AircraftItem(id, aircraftType, itemName, itemLocation, startDate, endDate, isOnBoard);
                itemList.add(item);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return itemList;
    }
    public void setAllIsOnBoardToFalse() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("is_on_board", 0); // Устанавливаем все значения is_on_board в 0 (false)

        int rowsAffected = db.update("aircraft_items", values, null, null);

        // Вывести сообщение для отладки
        Log.d("UpdateStatus", "Rows affected: " + rowsAffected);
        Log.d("UpdateStatus", "Values: " + values.toString());



    }
    public List<AircraftItem> getItemsWithExpiryLessThanAMonth() {
        List<AircraftItem> itemsWithExpiryLessThanAMonth = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Выполняем SQL-запрос, чтобы получить записи, у которых срок годности меньше месяца
        Cursor cursor = db.rawQuery("SELECT * FROM aircraft_items WHERE strftime('%s', end_date) - strftime('%s', 'now') < 30*24*3600", null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("id"));
                    @SuppressLint("Range") String aircraftType = cursor.getString(cursor.getColumnIndex("aircraft_type"));
                    @SuppressLint("Range") String itemName = cursor.getString(cursor.getColumnIndex("item_name"));
                    @SuppressLint("Range") String itemLocation = cursor.getString(cursor.getColumnIndex("item_location"));
                    @SuppressLint("Range") String startDate = cursor.getString(cursor.getColumnIndex("start_date"));
                    @SuppressLint("Range") String endDate = cursor.getString(cursor.getColumnIndex("end_date"));

                    // Получите значение is_on_board из курсора (0 или 1) и преобразуйте его в boolean
                    @SuppressLint("Range") int isOnBoardInt = cursor.getInt(cursor.getColumnIndex("is_on_board"));
                    boolean isOnBoard = isOnBoardInt == 1;

                    AircraftItem aircraftItem = new AircraftItem(id, aircraftType, itemName, itemLocation, startDate, endDate, isOnBoard);
                    itemsWithExpiryLessThanAMonth.add(aircraftItem);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        db.close();

        return itemsWithExpiryLessThanAMonth;
    }

}
