package com.example.music_player;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
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
import com.google.firebase.auth.AuthResult;
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

    FirebaseAuth fAuth;

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
        fAuth = FirebaseAuth.getInstance();

        if (fAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        loadingBar = new ProgressDialog(this);

        loadingBar.setTitle("Creating account...");
        loadingBar.setMessage("Please wait ...");
        loadingBar.setCanceledOnTouchOutside(false);

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id = regId.getText().toString().trim();
                password = regPassword.getText().toString().trim();
                email = regEmail.getText().toString().trim();
                phone = regMobile.getText().toString().trim();

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
        }else if (password.length() < 6) {
            Toast.makeText(this, "Password must be at least 6 character", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "Please enter your phone !!!", Toast.LENGTH_SHORT).show();
        } else {
            loadingBar.show();

            fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        loadingBar.dismiss();
                        Toast.makeText(RegisterActivity.this, "Register successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        loadingBar.dismiss();
                        Toast.makeText(RegisterActivity.this, "Error : " + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public void redlogBtn(View view) {
        finish();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
    }
}