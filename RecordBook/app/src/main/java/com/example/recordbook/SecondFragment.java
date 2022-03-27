package com.example.recordbook;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.recordbook.databinding.FragmentSecondBinding;
import com.mysql.cj.xdevapi.JsonParser;

import org.json.JSONException;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystemNotFoundException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentSecondBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Calendar cal = Calendar.getInstance();
        int mYear = cal.get(Calendar.YEAR);
        int mMonth = cal.get(Calendar.MONTH);
        mMonth++;
        int mDay = cal.get(Calendar.DAY_OF_MONTH);
        int mHour = cal.get(Calendar.HOUR_OF_DAY);
        int mMinute = cal.get(Calendar.MINUTE);

        binding.editTextDate.setText(mDay + "." + mMonth + "." + mYear);
        binding.editTextStartTime.setText(String.format("%02d", mHour) + ":" + String.format("%02d", mMinute));
        mHour++;
        binding.editTextEndTime.setText(String.format("%02d", mHour) + ":" + String.format("%02d", mMinute));

        binding.editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        int mymonth = i1 + 1;
                        binding.editTextDate.setText(i2 + "." + mymonth  + "." + i);
                    }
                }, year, month, day);
                dialog.show();
            }
        });

        binding.editTextStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int hour = cal.get(Calendar.HOUR_OF_DAY);
                int minute = cal.get(Calendar.MINUTE);

                TimePickerDialog dialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        binding.editTextStartTime.setText(String.format("%02d", i) + ":" + String.format("%02d", i1));
                    }
                }, hour, minute, true);

                dialog.show();
            }
        });

        binding.editTextEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int hour = cal.get(Calendar.HOUR_OF_DAY);
                int minute = cal.get(Calendar.MINUTE);

                TimePickerDialog dialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        binding.editTextEndTime.setText(String.format("%02d", i) + ":" + String.format("%02d", i1));
                    }
                }, hour, minute, true);

                dialog.show();
            }
        });

        binding.buttonAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Generate Values from User Input
                String date = binding.editTextDate.getText().toString();
                String starttime = binding.editTextStartTime.getText().toString();
                String endtime = binding.editTextEndTime.getText().toString();
                String path = "data.json";

                Map<String, String> map = new HashMap<>();
                map.put("date", date);
                map.put("start", starttime);
                map.put("end", endtime);
                map.put("activity", binding.editTextAction.getText().toString());

                JSONObject jo = new JSONObject(map);
                String json = Handlers.getFileString(getActivity(),path);
                if(json == "") {
                    json = "[]";
                }
                try {
                    JSONArray arr = new JSONArray(json);
                    arr.put(jo);
                    Handlers.writeFileString(getActivity(),path, arr.toString(4));
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("JSON", e.getMessage());
                }
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });

        binding.buttonDiscard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}