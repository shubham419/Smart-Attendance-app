package com.example.smartattendance.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.smartattendance.MainActivity;
import com.example.smartattendance.R;
import com.example.smartattendance.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;


public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    private FirebaseAuth mAuth;
    boolean flag =false ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.login_activity_bg));
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
        }
        Objects.requireNonNull(getSupportActionBar()).hide();
        ProgressDialog pd = new ProgressDialog(this);
        binding.userNameEt.requestFocus();
        mAuth = FirebaseAuth.getInstance();


        binding.loginFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.userNameEt.getText().toString() , password = "123456";

                if (!email.equals("")) {


                FirebaseDatabase.getInstance().getReference().child("canUserLogin").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        String canUserLogin = snapshot.getValue(String.class);
                        assert canUserLogin != null;
                        flag = canUserLogin.equals("yes");
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {
                        Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }

                });


                if (flag) {
                    pd.show();
                    pd.setMessage("please wait...");
                        mAuth.signInWithEmailAndPassword(email +"@jcoe.in", password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {

                                if (task.isSuccessful()) {
                                    Toast.makeText(LoginActivity.this, "login successful", Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    LoginActivity.this.startActivity(intent);
                                    pd.dismiss();
                                    LoginActivity.this.finish();
                                } else {
                                    pd.dismiss();
                                    Toast.makeText(LoginActivity.this, "Incorrect credential", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

                } else {
                    Toast.makeText(LoginActivity.this, "Security alert!\n Please contact admin.", Toast.LENGTH_SHORT).show();
                }
            }
                else {
                    binding.userNameEt.setError("Empty");
                }

        }
        });

    }
}

