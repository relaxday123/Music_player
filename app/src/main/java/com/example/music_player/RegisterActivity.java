package com.example.music_player;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    EditText regId, regPassword, regEmail, regMobile;
    String id, password, email, phone;
    Button regBtn;
    ProgressDialog loadingBar;

    FirebaseDatabase rootNode;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initViews();
        getSupportActionBar().hide();
    }

    private void initViews() {
        regId = findViewById(R.id.regId);
        regPassword = findViewById(R.id.regPassword);
        regEmail = findViewById(R.id.regEmail);
        regMobile = findViewById(R.id.regMobile);
        regBtn = findViewById(R.id.regBtn);

        loadingBar = new ProgressDialog(this);

        loadingBar.setTitle("Creating account...");
        loadingBar.setMessage("Please wait ...");
        loadingBar.setCanceledOnTouchOutside(false);

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id = regId.getText().toString();
                password = regPassword.getText().toString();
                email = regEmail.getText().toString();
                phone = regMobile.getText().toString();

                createNewAccount(id, password, email, phone);
            }
        });
    }

    private void createNewAccount(String id, String password, String email, String phone) {
        if (TextUtils.isEmpty(id)) {
            Toast.makeText(this, "Please enter your id !!!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter your password !!!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter your email !!!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "Please enter your phone !!!", Toast.LENGTH_SHORT).show();
        } else {
            loadingBar.show();

            final DatabaseReference mRef;
            mRef = FirebaseDatabase.getInstance().getReference();
            FirebaseDatabase.getInstance("https://vax-in-60807-default-rtdb.asia-southeast1.firebasedatabase.app");

            mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (!(snapshot.child("Users").child(phone).exists())) {
                        HashMap<String, Object> userdata = new HashMap<>();
                        userdata.put("phone", phone);
                        userdata.put("id", id);
                        userdata.put("password", password);
                        userdata.put("email", email);

                        mRef.child("Users").setValue(userdata)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            loadingBar.dismiss();
                                            Toast.makeText(RegisterActivity.this, "Registration successful!!!", Toast.LENGTH_SHORT).show();
                                        } else {
                                            loadingBar.dismiss();
                                            Toast.makeText(RegisterActivity.this, "Please try again!!!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    } else {
                        loadingBar.dismiss();
                        Toast.makeText(RegisterActivity.this, "User with this number already exist", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}