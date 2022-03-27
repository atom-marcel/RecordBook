package com.example.recordbook;

import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Layout;
import android.text.Spannable;
import android.text.method.MovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.recordbook.databinding.FragmentFirstBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.time.Month;
import java.util.Calendar;
import java.util.Locale;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    Calendar selectedDate = Calendar.getInstance();

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this).navigate(FirstFragmentDirections.actionFirstFragmentToSecondFragment());
            }
        });

        binding.firstIcLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View click_view) {
                if(selectedDate.get(Calendar.DAY_OF_MONTH) == 1) {
                    if(selectedDate.get(Calendar.MONTH) == 0) {
                        selectedDate.set(Calendar.YEAR, selectedDate.get(Calendar.YEAR) - 1);
                        selectedDate.set(Calendar.MONTH, 11);
                    } else {
                        selectedDate.set(Calendar.MONTH, selectedDate.get(Calendar.MONTH) - 1);
                    }
                    selectedDate.set(Calendar.DAY_OF_MONTH, selectedDate.getActualMaximum(Calendar.DAY_OF_MONTH));
                } else {
                    selectedDate.set(Calendar.DAY_OF_MONTH, selectedDate.get(Calendar.DAY_OF_MONTH) - 1);
                }
                updateView(view, selectedDate);
            }
        });

        binding.firstIcRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View click_view) {
                if(selectedDate.get(Calendar.DAY_OF_MONTH) == selectedDate.getActualMaximum(Calendar.DAY_OF_MONTH)) {
                    if(selectedDate.get(Calendar.MONTH) == 11) {
                        selectedDate.set(Calendar.YEAR, selectedDate.get(Calendar.YEAR) + 1);
                        selectedDate.set(Calendar.MONTH, 0);
                    } else {
                        selectedDate.set(Calendar.MONTH, selectedDate.get(Calendar.MONTH) + 1);
                    }
                    selectedDate.set(Calendar.DAY_OF_MONTH, 1);
                } else {
                    selectedDate.set(Calendar.DAY_OF_MONTH, selectedDate.get(Calendar.DAY_OF_MONTH) + 1);
                }
                updateView(view, selectedDate);
            }
        });

        binding.firstDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View click_view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        selectedDate.set(Calendar.YEAR, i);
                        selectedDate.set(Calendar.MONTH, i1);
                        selectedDate.set(Calendar.DAY_OF_MONTH, i2);
                        updateView(view, selectedDate);
                    }
                }, year, month, day);
                dialog.show();
            }
        });
        updateView(view, selectedDate);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void updateView(View view, Calendar date) {
        TextView txtdate = view.findViewById(R.id.first_date);
        String str_day_of_week = date.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.GERMANY);
        txtdate.setText(str_day_of_week + " " + date.get(Calendar.DAY_OF_MONTH) + "." + (date.get(Calendar.MONTH) + 1) + "." + date.get(Calendar.YEAR));

        // Load Json Array from file
        String json = Handlers.getFileString(getActivity(), "data.json");
        JSONArray array = new JSONArray();
        try {
            array = new JSONArray(json);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("JSON", e.getMessage());
        }
        JSONArray selectedArray = Handlers.getFromSelectedDate(selectedDate, array);

        // Order the selected array
        TreeNode root = new TreeNode(null);
        for(int i = 0; i < selectedArray.length(); i++) {
            try {
                root.insertOrdered(root, selectedArray.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("JSON", e.getMessage());
            }
        }
        selectedArray = new JSONArray();
        selectedArray = root.getOrderedJSONArray(root, selectedArray);

        // Set Layout parameters
        LinearLayout layout = view.findViewById(R.id.first_frag_linear);
        layout.setPadding(10, 30, 10, 30);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        params.setMargins(0, 25, 0, 25);
        layout.removeAllViews();

        // Insert selected Entries
        for(int i = 0; i < selectedArray.length(); i++) {
            CardView cardview = new CardView(getContext());
            cardview.setCardBackgroundColor(Color.rgb(245, 152, 0));
            String start = "";
            String end = "";
            String activity = "";
            try {
                JSONObject obj = selectedArray.getJSONObject(i);
                start = obj.get("start").toString();
                end = obj.get("end").toString();
                activity = obj.get("activity").toString();

                cardview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View click_view) {
                        FirstFragmentDirections.ActionFirstFragmentToEditFragment action = FirstFragmentDirections.actionFirstFragmentToEditFragment(obj.toString());
                        NavHostFragment.findNavController(FirstFragment.this).navigate(action);
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("JSON", e.getMessage());
            }

            float dec_hours = Handlers.getDecimalHourFromTimeSpan(start, end);

            // Draw the Containers
            cardview.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            TextView txt = new TextView(getContext());
            txt.setText(start + " - " + end + " | " + String.format("%.2fh", dec_hours) + " : " + activity);
            txt.setTextSize(18);
            cardview.addView(txt);
            layout.addView(cardview, params);
        }
    }

}