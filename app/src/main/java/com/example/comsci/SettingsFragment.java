package com.example.comsci;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;

public class SettingsFragment extends Fragment /*implements AdapterView.OnItemSelectedListener*/ {

    private RadioButton radioButton;
    private RadioGroup radioGroup;
    private boolean isMale;
    private DatePickerDialog datePickerDialog;
    private Button dateButton;
    private Spinner activitySpinner;
    private DatabaseReference databaseReference;
    private EditText weightEditText;
    private EditText heightEditText;
    private Button calculateCaloriesButton;
    private LocalDate today;
    private LocalDate birthDate;
    private int age;
    private TextView calorieGoalTextView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initDatePicker();
        dateButton = getView().findViewById(R.id.btnDateOfBirth);
        dateButton.setText(getTodaysDate());

        radioGroup = getView().findViewById(R.id.radioGroupGender);
        activitySpinner = getView().findViewById(R.id.spinnerActivity);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.Activity, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activitySpinner.setAdapter(adapter);

        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = getView().findViewById(radioId);
        switch(radioButton.getText().toString()){
            case "Male":
                isMale = true;
                break;
            case "Female":
                isMale = false;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + radioButton.getText());
        }

        weightEditText = getView().findViewById(R.id.editTextWeight);
        heightEditText = getView().findViewById(R.id.editTextHeight);
        calorieGoalTextView = getView().findViewById(R.id.textViewCalorieGoal);

        calculateCaloriesButton = getView().findViewById(R.id.btnCalculateCalories);

        databaseReference = FirebaseDatabase.getInstance("https://csia-53c8a-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("users");

        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String currentUID = currentFirebaseUser.getUid();
        //spinner.setOnItemSelectedListener(this);
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker(v);
            }
        });


        calculateCaloriesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int weight = Integer.parseInt(weightEditText.getText().toString());//will crash if left blank, do an if statement
                int height = Integer.parseInt(heightEditText.getText().toString());
                int recommendedCalories;
                String activity = activitySpinner.getSelectedItem().toString();
                age = Period.between(birthDate, today).getYears();

                databaseReference.child(currentUID).child("weight").setValue(weight);
                databaseReference.child(currentUID).child("height").setValue(height);
                databaseReference.child(currentUID).child("male").setValue(isMale);

                if(isMale)
                {
                    if(activity.equals("Sedentary: little or no exercise"))
                    {
                        recommendedCalories = (int) (66.5 + (13.75*weight) + (5.003*height) - (6.775*age));
                    }
                    else if(activity.equals("Light: exercise 1-3 times/week")){
                        recommendedCalories = (int) ((66.5 + (13.75*weight) + (5.003*height) - (6.775*age))*1.18);
                    }
                    else if(activity.equals("Moderate: exercise 4-5 times/week")){
                        recommendedCalories = (int) ((66.5 + (13.75*weight) + (5.003*height) - (6.775*age))*1.36);
                    }
                    else if(activity.equals("Active: daily exercise")){
                        recommendedCalories = (int) ((66.5 + (13.75*weight) + (5.003*height) - (6.775*age))*1.54);
                    }
                    else if(activity.equals("Very Active: intense exercise 6-7 times/week")){
                        recommendedCalories = (int) ((66.5 + (13.75*weight) + (5.003*height) - (6.775*age))*1.72);
                    }
                    else{
                        recommendedCalories = (int) ((66.5 + (13.75*weight) + (5.003*height) - (6.775*age))*1.9);
                    }
                }
                else{
                    if(activity.equals("Sedentary: little or no exercise"))
                    {
                        recommendedCalories = (int) (655.1 + (9.563 * weight) + (1.850 * height) - (4.676*age));
                    }
                    else if(activity.equals("Light: exercise 1-3 times/week")){
                        recommendedCalories = (int) ((655.1 + (9.563 * weight) + (1.850 * height) - (4.676*age))*1.18);
                    }
                    else if(activity.equals("Moderate: exercise 4-5 times/week")){
                        recommendedCalories = (int) ((655.1 + (9.563 * weight) + (1.850 * height) - (4.676*age))*1.36);
                    }
                    else if(activity.equals("Active: daily exercise")){
                        recommendedCalories = (int) ((655.1 + (9.563 * weight) + (1.850 * height) - (4.676*age))*1.54);
                    }
                    else if(activity.equals("Very Active: intense exercise 6-7 times/week")){
                        recommendedCalories = (int) ((655.1 + (9.563 * weight) + (1.850 * height) - (4.676*age))*1.72);
                    }
                    else{
                        recommendedCalories = (int) ((655.1 + (9.563 * weight) + (1.850 * height) - (4.676*age))*1.9);
                    }
                }

                calorieGoalTextView.setText("Calorie Goal: " + String.valueOf(recommendedCalories));
                databaseReference.child(currentUID).child("calorieGoal").setValue(recommendedCalories);
            }
        });
    }

    private String getTodaysDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day, month, year);
                dateButton.setText(date);
                today = LocalDate.now();
                birthDate = LocalDate.of(year, month, day);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(getActivity(), style, dateSetListener, year, month, day);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

    }

    private String makeDateString(int day, int month, int year) {
        return getMonthFormat(month) + " " + day + " " + year;
    }

    private String getMonthFormat(int month) {
        if(month == 1)
        {
            return "JAN";
        }
        if(month == 2)
        {
            return "FEB";
        }
        if(month == 3)
        {
            return "MAR";
        }
        if(month == 4)
        {
            return "APR";
        }
        if(month == 5)
        {
            return "MAY";
        }
        if(month == 6)
        {
            return "JUN";
        }
        if(month == 7)
        {
            return "JUL";
        }
        if(month == 8)
        {
            return "AUG";
        }
        if(month == 9)
        {
            return "SEP";
        }
        if(month == 10)
        {
            return "OCT";
        }
        if(month == 11)
        {
            return "NOV";
        }
        if(month == 12)
        {
            return "DEC";
        }
        return "JAN";
    }

    public void openDatePicker(View view)
    {
        datePickerDialog.show();
    }

    /*@Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        //Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }*/
}