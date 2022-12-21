package com.example.restaurant;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import model.AddToCart;
import model.ReserveTable;


public class fragmentFoodList extends Fragment{

    ArrayAdapter<String> arrayAdapter;

    Boolean finish=false;
    ListView mListView;
    SearchView searchView;
    TextView subheading;
    private String[] names =
            {
                    "Bulalo", "Corn Soup", "Pork Sinigang",
                    "Palabok", "Carbonara", "Malabon", "Guisado", "Spaghetti",
                    "Breaded Pork", "Crispy Pata", "Grilled Pork", "Adobo", "Sisig", "Spicy Ribs", "SS Pork",
                    "Chicken Curry", "Honey Chickn", "Cordon Bleu",
                    "Calamares", "SS Fish", "Gambas", "Grilled Fish",
                    "Kangkong", "Garden Salad", "Chopsuey"
            };

    private int[] images = {
            R.drawable.soupbulalo, R.drawable.soupcornsoup, R.drawable.soupsinigang,
            R.drawable.ppnpalabok, R.drawable.ppncarbonara, R.drawable.ppnmalabon, R.drawable.ppnguisado, R.drawable.ppnredsaucespaget,
            R.drawable.porkbreadedpork, R.drawable.porkpata, R.drawable.porkgrilledpork, R.drawable.porkadobo, R.drawable.porksisig, R.drawable.porkspareribs, R.drawable.porksweetandsour,
            R.drawable.chickencurry, R.drawable.chickenhoney, R.drawable.chickencordon,
            R.drawable.seafoodcalamares, R.drawable.seafoodsweetandsourfish, R.drawable.seafoodgarlicbuttershrimp, R.drawable.seafoodgrilledfish,
            R.drawable.veggiesadobokang, R.drawable.veggiesgarden, R.drawable.veggieschopsuey
    };

    private String[] desc = {
            "₱150.00", "₱150.00", "₱150.00",
            "₱130.00", "₱130.00", "₱130.00", "₱130.00", "₱130.00",
            "₱150.00", "₱250.00", "₱250.00", "₱120.00", "₱120.00", "₱250.00", "₱150.00",
            "₱140.00", "₱130.00", "₱150.00",
            "₱150.00", "₱130.00", "₱170.00", "₱120.00",
            "₱120.00", "₱120.00", "₱120.00"
    };


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    public fragmentFoodList() {
        // Required empty public constructor
    }


    public static fragmentFoodList newInstance(String param1, String param2) {
        fragmentFoodList fragment = new fragmentFoodList();
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


        View thisview = inflater.inflate(R.layout.fragment_food_list, container, false);

        mListView = (ListView) thisview.findViewById(R.id.listview);
        searchView = (SearchView) thisview.findViewById(R.id.searchView);
        subheading = (TextView) thisview.findViewById(R.id.subheading);
        myAdapter adapter = new myAdapter();
        mListView.setAdapter(adapter);



        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() { //IF AN ITEM FROM THE LIST IS CLICKED
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(getContext(), "User " + SignIn.userid + " has ordered " + names[i].toString(), Toast.LENGTH_LONG).show();

                finish=false; //<-- VERY IMPORTANT
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference table_reserveTable = database.getReference("Cart/"+SignIn.userid);

                table_reserveTable.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if(finish){

                        }
                        else{
                            AddToCart addToCart = new AddToCart(names[i].toString(),desc[i].toString());

                            table_reserveTable.push().setValue(addToCart);
                            finish=true;
                            Toast.makeText(getContext(),""+names[i].toString()+"Added to cart",Toast.LENGTH_SHORT).show();

                            //Intent goBack = new Intent(getContext(),MainHome.class);
                            //(goBack);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                for(int i=0; i<names.length;i++){
                    if(names[i].toString().startsWith(s)){
                        //subheading.setText(s);
                        mListView.setSelection(i);
                    }
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                for(int i=0; i<names.length;i++){
                    if(names[i].toString().startsWith(s)){
                        //subheading.setText(s);
                        mListView.setSelection(i);
                    }
                }


                return false;
            }
        });

        return thisview;

    }


    public class myAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return images.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }


        @Override
        public View getView(int i, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.card, parent, false);

            ImageView mImageView = convertView.findViewById(R.id.foodPic);
            TextView mTextViewFoodName = convertView.findViewById(R.id.Foodname);
            TextView mTextViewFoodDesc = convertView.findViewById(R.id.foodDesc);
            //  TextView mTextViewFoodDesc = view.findViewById(R.id.foodDesc);

            mTextViewFoodName.setText(names[i]);
            mTextViewFoodDesc.setText(desc[i]);
            mImageView.setImageResource(images[i]);

            return convertView;
        }

    }
}