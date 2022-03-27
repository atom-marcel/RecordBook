package com.example.recordbook;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;

public class Handlers {
    public static String getFileString(Activity act, String path) {
        File in = new File(path);
        StringBuilder out = new StringBuilder();
        try {
            FileInputStream fis = act.openFileInput(path);
            InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
            BufferedReader br = new BufferedReader(isr);
            String line;
            while((line = br.readLine()) != null) {
                out.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("FileInput", e.getMessage());
        }
        return out.toString();
    }

    public static void writeFileString(Activity act, String path, String str) {
        try {
            FileOutputStream out = act.openFileOutput(path, Context.MODE_PRIVATE);
            out.write(str.getBytes());
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("FileOutput", e.getMessage());
        }
    }

    public static JSONArray getFromSelectedDate(Calendar cal, JSONArray input) {
        JSONArray array = new JSONArray();
        int month = cal.get(Calendar.MONTH) + 1;
        String date = cal.get(Calendar.DAY_OF_MONTH) + "." + month + "." + cal.get(Calendar.YEAR);
        for(int i = 0; i < input.length(); i++) {
            try {
                JSONObject obj = input.getJSONObject(i);
                if(obj.get("date").equals(date)) {
                    array.put(obj);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("JSON", e.getMessage());
            }
        }
        return array;
    }

    public static float getDecimalHourFromTimeSpan(String start, String end) {
        String[] arr_start = start.split(":");
        String[] arr_end = end.split(":");
        float f_start = Float.parseFloat(arr_start[0]);
        float f_end = Float.parseFloat(arr_end[0]);
        float f_start_minutes = Float.parseFloat(arr_start[1]) / 60;
        float f_end_minutes = Float.parseFloat(arr_end[1]) / 60;
        f_start += f_start_minutes;
        f_end += f_end_minutes;
        float result = f_end - f_start;
        return result;
    }
}
