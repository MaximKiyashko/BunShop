package com.example.bunshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity implements TextWatcher {

    private static final String TAG = "SignUp";
    //
    private FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    //
    private EditText emailTV;
    private EditText passwordTV;
    private EditText nameTV;
    private EditText phoneTV;
    private TextView errorTV;
    private Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        //Input Fields
        emailTV = findViewById(R.id.emailET);
        emailTV.addTextChangedListener(this);
        passwordTV = findViewById(R.id.passwordET);
        passwordTV.addTextChangedListener(this);
        nameTV = findViewById(R.id.nameETsignUp);
        nameTV.addTextChangedListener(this);
        phoneTV = findViewById(R.id.phoneETSignUp);
        phoneTV.addTextChangedListener(this);
        //
        errorTV = findViewById(R.id.tvErrorSignUp);
        errorTV.setVisibility(View.GONE);
        //
        btnSignUp = findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp();
            }
        });
    }

    private void signUp() {
        String email = emailTV.getText().toString();
        String password = passwordTV.getText().toString();
        String name = nameTV.getText().toString();
        String phone = phoneTV.getText().toString();

        if (email.isEmpty() || password.isEmpty() || name.isEmpty() || phone.isEmpty()) {
            errorTV.setVisibility(View.VISIBLE);
            errorTV.setText(R.string.errorEmpty);
            return;
        }
        //
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            userSaveToCloudFireStore();
                            if (user != null)
                                goMenu(user);
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            if (task.getException() instanceof FirebaseAuthWeakPasswordException) {
                                Log.i(TAG, "Weak Password");
                                setError(R.string.errorWeakPassword);
                            } else if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Log.i(TAG, "Bad email");
                                setError(R.string.errorBadEmail);
                            } else if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                Log.i(TAG, "User exist");
                                setError(R.string.errorUserExist);
                            }
                        }

                        // ...
                    }
                });
    }

    private void goMenu(FirebaseUser user) {
        Intent intent = new Intent(SignUpActivity.this, MenuActivity.class);
        intent.putExtra("userName", user.getEmail());
        startActivity(intent);
        finish();
    }

    private void userSaveToCloudFireStore() {
        Map<String, Object> user = new HashMap<>();
        user.put("email", emailTV.getText().toString());
        user.put("name", nameTV.getText().toString());
        user.put("phone", phoneTV.getText().toString());
        //
        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "onSuccess: User add " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: User not add " + e);
                    }
                });
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (charSequence.length() > 0)
            btnSignUp.setEnabled(true);
    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (editable.toString().isEmpty()) {
            setError(R.string.errorEmpty);
        }
        else {
            btnSignUp.setEnabled(true);
            errorTV.setVisibility(View.GONE);
        }
    }

    private void setError(int errorStrCode) {
        errorTV.setVisibility(View.VISIBLE);
        errorTV.setText(getString(errorStrCode));
    }

}