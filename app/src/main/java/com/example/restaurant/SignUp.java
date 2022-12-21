package com.example.restaurant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import model.User;

public class SignUp extends AppCompatActivity {

    Button btnSignUp;
    MaterialEditText txtPhone,txtName,txtPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        txtPhone = (MaterialEditText) findViewById(R.id.txtPhone);
        txtName = (MaterialEditText) findViewById(R.id.txtName);
        txtPassword = (MaterialEditText) findViewById(R.id.txtPassword);

        btnSignUp = (Button) findViewById(R.id.btnSignUp);

        //Initialize Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog mDialog = new ProgressDialog(SignUp.this);
                mDialog.setMessage("Loading...");
                mDialog.show();

                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //Check if the phone number already exists
                        if(snapshot.child(txtPhone.getText().toString()).exists()){
                            mDialog.dismiss();
                            Toast.makeText(SignUp.this,"Phone Number already in use...",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            mDialog.dismiss();

                            User user = new User(txtName.getText().toString(),txtPassword.getText().toString());
                            table_user.child(txtPhone.getText().toString()).setValue(user);
                            Toast.makeText(SignUp.this,"Sign up successful, Please sign in to continue...",Toast.LENGTH_SHORT).show();
                            finish();

                            Intent signIn = new Intent(SignUp.this,SignIn.class);
                            startActivity(signIn);



                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }
}