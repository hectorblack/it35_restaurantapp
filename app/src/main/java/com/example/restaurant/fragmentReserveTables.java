package com.example.restaurant;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Locale;

import model.ReserveTable;
import model.User;


public class fragmentReserveTables extends Fragment {

    DatePickerDialog datePickerDialog;
    Button fbtnDate, fbtnTime,fbtnIncrement, fbtnDecrement,fbtnReserve;
    int hour, minute;
    int count=1;
    TextView numOfPersons;
    int newKeyValue;
    Boolean finish=false;


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public fragmentReserveTables() {
        // Required empty public constructor
    }


    public static fragmentReserveTables newInstance(String param1, String param2) {
        fragmentReserveTables fragment = new fragmentReserveTables();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    public void onCreate(LayoutInflater inflater, ViewGroup container,
                         Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View thisview = inflater.inflate(R.layout.fragment_reserve_tables, container, false);
        initDatePicker();

        fbtnDate = (Button) thisview.findViewById(R.id.btnDate);
        fbtnTime = (Button) thisview.findViewById(R.id.btnTime);
        fbtnIncrement = (Button) thisview.findViewById(R.id.btnIncrement);
        fbtnDecrement = (Button) thisview.findViewById(R.id.btnDecrement);
        fbtnReserve = (Button) thisview.findViewById(R.id.btnReserve);
        numOfPersons = (TextView) thisview.findViewById(R.id.values);


        fbtnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatePicker(view);
                fbtnDate.setText(getTodaysDate());
            }
        });

        fbtnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popTimePicker(view);
            }
        });

        fbtnIncrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(count==12)
                    count=12;
                else
                    count++;

                numOfPersons.setText(""+count);
            }
        });

        fbtnDecrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(count==1)
                    count=1;
                else
                    count--;

                numOfPersons.setText(""+count);
            }
        });

        fbtnDate.addTextChangedListener(ReserveButtonWatcher);
        fbtnTime.addTextChangedListener(ReserveButtonWatcher);

        fbtnReserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    //INSERT DATA TO FIREBASE
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    final DatabaseReference table_reserveTable = database.getReference("Reservation");

                    table_reserveTable.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                           if(finish){

                           }
                           else{
                               ReserveTable tableReserve = new ReserveTable(fbtnDate.getText().toString(),numOfPersons.getText().toString(),SignIn.userid,fbtnTime.getText().toString());

                               table_reserveTable.push().setValue(tableReserve);
                               finish=true;
                               Toast.makeText(getContext(),"Saved to the database",Toast.LENGTH_SHORT).show();

                               Intent goBack = new Intent(getContext(),MainHome.class);
                               startActivity(goBack);
                           }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });



            }
        });

        return thisview;
    }

    private TextWatcher ReserveButtonWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String dateInput = fbtnDate.getText().toString();
            String timeInput = fbtnTime.getText().toString();

            fbtnReserve.setEnabled(!dateInput.equals("select a date") && !timeInput.equals("select time"));
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };


    private String getTodaysDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day)
            {
                month = month + 1;
                String date = makeDateString(day, month, year);
                fbtnDate.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(getContext(), style, dateSetListener, year, month, day);

    }

    private String makeDateString(int day, int month, int year) {
        return getMonthFormat(month) + " " + day + " " + year;
    }

    private String getMonthFormat(int month) {
        switch (month){
            case 1: return "January";

            case 2: return "February";

            case 3: return "March";

            case 4: return "April";

            case 5: return "May";

            case 6: return "June";

            case 7: return "July";

            case 8: return "August";

            case 9: return "September";

            case 10: return "October";

            case 11: return "November";

            case 12: return "December";

            default: return "January";
        }
    }

    public void openDatePicker(View view) {
        datePickerDialog.show();
    }

    public void popTimePicker(View view) {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener()
        {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute)
            {
                hour = selectedHour;
                minute = selectedMinute;
                fbtnTime.setText(String.format(Locale.getDefault(), "%02d:%02d",hour, minute));
            }
        };


        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), /*style,*/ onTimeSetListener, hour, minute, true);

        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }


}