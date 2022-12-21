package com.example.restaurant;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.FirebaseError;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import model.AddToCart;


public class fragmentOrders extends Fragment {
    RecyclerView recyclerView;
    DatabaseReference database;
    myCartAdapter myAdapter;
    ArrayList<AddToCart> list;
    Button btnPlace, btnClear;

    DatePickerDialog datePickerDialog;
    Button fbtnDate, fbtnTime;
    int hour, minute;



    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    public fragmentOrders() {
        // Required empty public constructor
    }


    public static fragmentOrders newInstance(String param1, String param2) {
        fragmentOrders fragment = new fragmentOrders();
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

        View thisview = inflater.inflate(R.layout.fragment_orders, container, false);
        initDatePicker();
        recyclerView = thisview.findViewById(R.id.orderlist);
        database = FirebaseDatabase.getInstance().getReference("Cart/"+SignIn.userid);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        list = new ArrayList<>();
        myAdapter = new myCartAdapter(getContext(),list);
        recyclerView.setAdapter(myAdapter);
        btnPlace = (Button) thisview.findViewById(R.id.btnPlaceOrder);
        btnClear = (Button) thisview.findViewById(R.id.btnClear);

        fbtnDate = (Button) thisview.findViewById(R.id.btnDate);
        fbtnTime = (Button) thisview.findViewById(R.id.btnTime);

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase delete = FirebaseDatabase.getInstance();
                DatabaseReference deleteThis = delete.getReference("Cart/"+SignIn.userid);
                deleteThis.setValue(null);
            }
        });

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


        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                    AddToCart addToCart = dataSnapshot.getValue(AddToCart.class);
                    list.add(addToCart);


                }
                myAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        fbtnDate.addTextChangedListener(ReserveButtonWatcher);
        fbtnTime.addTextChangedListener(ReserveButtonWatcher);



        btnPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String key = fbtnDate.getText().toString() + "," + fbtnTime.getText().toString();

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference from = database.getReference("Cart/"+SignIn.userid);
                final DatabaseReference to = database.getReference("PlacedOrders/"+SignIn.userid+"/"+key);

                moveFirebaseRecord(from,to);

                Toast.makeText(getContext(),"Order Placed",Toast.LENGTH_SHORT).show();

                Intent goBack = new Intent(getContext(),MainHome.class);
                startActivity(goBack);
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

            btnPlace.setEnabled(!dateInput.equals("select a date") && !timeInput.equals("select time"));
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

    public void moveFirebaseRecord(final DatabaseReference fromPath, final DatabaseReference toPath)
    {
        fromPath.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                toPath.setValue(snapshot.getValue(), new DatabaseReference.CompletionListener(){

                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        if (error != null) {
                            //System.out.println("Copy failed");
                        } else {
                            //System.out.println("Success");
                            fromPath.setValue(null);

                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}