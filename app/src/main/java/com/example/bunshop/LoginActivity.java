package com.example.bunshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
public class LoginActivity extends AppCompatActivity implements TextWatcher {

    private static final String TAG = "SignIn";
    private FirebaseAuth mAuth;
    private EditText emailET;
    private EditText passwordET;
    private TextView errorTV;
    private Button btnSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        emailET = findViewById(R.id.textEmailLogin);
        emailET.addTextChangedListener(this);
        passwordET = findViewById(R.id.textPasswordLogin);
        passwordET.addTextChangedListener(this);
        errorTV = findViewById(R.id.tvErrorLogin);
        errorTV.setVisibility(View.GONE);
        //Sign In
        btnSignIn = findViewById(R.id.btnSignIn);
        btnSignIn.setEnabled(false);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
    }

    private void signIn() {
        String email = emailET.getText().toString();
        String password = passwordET.getText().toString();
        if (email.isEmpty() || password.isEmpty()) {
            errorTV.setVisibility(View.VISIBLE);
            errorTV.setText(R.string.errorEmpty);
            return;
        }
        //
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            goMenu();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            setError(R.string.errorFirebase);
                            //Toast.makeText(LoginActivity.this, "Authentication failed.",
                            //        Toast.LENGTH_SHORT).show();
                        }
                        // ...
                    }
                });
    }

    private void goMenu() {
        Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
        String userName = ((EditText) findViewById(R.id.textEmailLogin)).getText().toString();
        intent.putExtra("userName", userName);
        startActivity(intent);
        finish();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (charSequence.length() > 0)
            btnSignIn.setEnabled(true);
    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (editable.toString().isEmpty()) {
            setError(R.string.errorEmpty);
        }
        else {
            btnSignIn.setEnabled(true);
            errorTV.setVisibility(View.GONE);
        }
    }

    private void setError(int errorStrCode) {
        errorTV.setVisibility(View.VISIBLE);
        errorTV.setText(getString(errorStrCode));
    }
}