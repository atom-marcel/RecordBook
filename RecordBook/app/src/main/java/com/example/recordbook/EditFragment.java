package com.example.recordbook;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.recordbook.databinding.FragmentEditBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EditFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditFragment newInstance(String param1, String param2) {
        EditFragment fragment = new EditFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String json = EditFragmentArgs.fromBundle(getArguments()).getJson();

        EditText editAction = view.findViewById(R.id.editTextActionEdit);
        EditText editDate = view.findViewById(R.id.editTextDateEdit);
        EditText editStart = view.findViewById(R.id.editTextStartTimeEdit);
        EditText editEnd = view.findViewById(R.id.editTextEndTimeEdit);

        // Create Json object and fill it with data
        JSONObject obj = new JSONObject();
        String action = "";
        String date = "";
        String start = "";
        String end = "";
        try {
            obj = new JSONObject(json);
            action = obj.get("activity").toString();
            date = obj.get("date").toString();
            start = obj.get("start").toString();
            end = obj.get("end").toString();

            editAction.setText(action);
            editDate.setText(date);
            editEnd.setText(end);
            editStart.setText(start);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("JSON", e.getMessage());
        }

        // Get the selected date and time
        String[] date_array = date.split("\\.");
        int year = Integer.parseInt(date_array[2]);
        int month = Integer.parseInt(date_array[1]);
        int day = Integer.parseInt(date_array[0]);
        String[] start_array = start.split(":");
        int start_hour = Integer.parseInt(start_array[0]);
        int start_minute = Integer.parseInt(start_array[1]);
        String[] end_array = end.split(":");
        int end_hour = Integer.parseInt(end_array[0]);
        int end_minute = Integer.parseInt(end_array[1]);

        String obj_json = obj.toString();

        // Set the button click listeners
        Button accept = view.findViewById(R.id.buttonAcceptEdit);
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View click_view) {
                deleteEntry(getActivity(), obj_json);
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("activity", editAction.getText().toString());
                    obj.put("date", editDate.getText().toString());
                    obj.put("start", editStart.getText().toString());
                    obj.put("end", editEnd.getText().toString());
                    addEntry(getActivity(), obj.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("JSON", e.getMessage());
                }
                NavHostFragment.findNavController(EditFragment.this).navigate(R.id.action_editFragment_to_FirstFragment);
            }
        });

        Button discard = view.findViewById(R.id.buttonDiscardEdit);
        discard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View click_view) {
                NavHostFragment.findNavController(EditFragment.this).navigate(R.id.action_editFragment_to_FirstFragment);
            }
        });

        Button delete = view.findViewById(R.id.buttonDeleteEdit);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View click_view) {
                deleteEntry(getActivity(), obj_json);
                NavHostFragment.findNavController(EditFragment.this).navigate(R.id.action_editFragment_to_FirstFragment);
            }
        });

        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View click_view) {
                DatePickerDialog dialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        editDate.setText(i2 + "." + (i1 + 1) + "." + i);
                    }
                }, year, month - 1, day);
                dialog.show();
            }
        });

        editStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View click_view) {
                TimePickerDialog dialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        editStart.setText(String.format("%02d:%02d", i, i1));
                    }
                }, start_hour, start_minute, true);
                dialog.show();
            }
        });

        editEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View click_view) {
                TimePickerDialog dialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        editEnd.setText(String.format("%02d:%02d", i, i1));
                    }
                }, end_hour, end_minute, true);
                dialog.show();
            }
        });
    }

    public void deleteEntry(Activity act, String json_obj) {
        String json_str = Handlers.getFileString(act, "data.json");
        try {
            JSONObject obj = new JSONObject(json_obj);
            JSONArray array = new JSONArray(json_str);
            for(int i = 0; i < array.length(); i++) {
                if(array.getJSONObject(i).toString().equals(obj.toString())) {
                    array.remove(i);
                }
            }
            Handlers.writeFileString(act, "data.json", array.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("JSON", e.getMessage());
        }
    }

    public void addEntry(Activity act, String json_obj) {
        String json_str = Handlers.getFileString(act, "data.json");
        try {
            JSONObject obj = new JSONObject(json_obj);
            JSONArray array = new JSONArray(json_str);
            array.put(obj);
            Handlers.writeFileString(act, "data.json", array.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("JSON", e.getMessage());
        }
    }
}