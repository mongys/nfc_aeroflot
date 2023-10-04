package com.example.peresbor;
import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
public class CSVUtils {
    public static boolean saveToCSVFile(Context context, String fileName, String csvData) {
        try {
            // Получите внутреннее хранилище приложения
            File dir = context.getFilesDir();

            // Создайте файл CSV
            File file = new File(dir, fileName);
            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(csvData.getBytes());
            outputStream.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}