/*
 * Copyright (c) 2018.  Que Base Technologies
 *
 * MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package campusorders.com.quebasetech.joe.campusorders;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import campusorders.com.quebasetech.joe.campusorders.model.User;
import campusorders.com.quebasetech.joe.campusorders.utils.utils;

import static campusorders.com.quebasetech.joe.campusorders.utils.utils.CURRENT_USER;
import static campusorders.com.quebasetech.joe.campusorders.utils.utils.USER_ID;
import static campusorders.com.quebasetech.joe.campusorders.utils.utils.USER_LOCATION;
import static campusorders.com.quebasetech.joe.campusorders.utils.utils.USER_NAME;
import static campusorders.com.quebasetech.joe.campusorders.utils.utils.USER_PHONE;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText mEmail;
    private EditText mPassword;
    private ProgressDialog progressDialog;
    private Button loginBtn, registerButton;
    private TextView errorView;
    private static final String TAG = "LoginActivity";
    private SharedPreferences userPrefs;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginBtn = (Button) findViewById(R.id.login_button);
        registerButton = (Button) findViewById(R.id.login_button);
        mEmail = (EditText) findViewById(R.id.email_field);
        mPassword = (EditText) findViewById(R.id.password_field);
        errorView = (TextView) findViewById(R.id.message_view);
        progressDialog = new ProgressDialog(LoginActivity.this);
        userPrefs = getSharedPreferences(CURRENT_USER, MODE_PRIVATE);
        editor = userPrefs.edit();
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) updateUI(currentUser);
    }

    public void registerUser(View view) {
        String email = mEmail.getText().toString().trim();
        String password = mPassword.getText().toString().trim();
        if(email.isEmpty() || password.isEmpty()) {
//            Toast.makeText(getApplicationContext(), "All fields are required.", Toast.LENGTH_SHORT).show();
            errorView.setText("All fields are required");
            return;
        }
        progressDialog.setMessage("Registering, Please Wait...");
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success
                            Toast.makeText(getApplicationContext(), "Registration Successful.", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            //TODO:: Launch Set-up activity
                            Intent intent = new Intent(getApplicationContext(), AccountSetup.class);
                            intent.putExtra("FROM", "REGISTER");
                            startActivity(intent);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
//                            Log.w(TAG, "signInWithEmail:failure", task.getException());
//                            Toast.makeText(getApplicationContext(), "Registration failed."+ task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            errorView.setText(task.getException().getMessage());
                        }
                        progressDialog.dismiss();
                    }
                });
    }


    public void loginUser(View view) {
        String email = mEmail .getText().toString().trim();
        String password = mPassword.getText().toString().trim();
        if(email.isEmpty() || password.isEmpty()) {
            errorView.setText("All fields are required");
            return;
        }

        progressDialog.setMessage("Logging in, Please Wait...");
        progressDialog.show();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            errorView.setText(task.getException().getMessage());
                            updateUI(null);
                        }
                    }
                });
    }

    public void showHome() {
        Intent home = new Intent(getApplicationContext(), BuyerHome.class);
        startActivity(home);
        finish();
    }

    private void updateUI(final FirebaseUser user) {
        if (user != null) {
            String id = userPrefs.getString(utils.USER_ID, "NONE");
            if(id.equals("NONE")){ // Not present
                progressDialog.setMessage("Setting up account...");
                // Check for a/c info in prefs
                DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
                Query userRef = usersRef.orderByChild("email").equalTo(utils.getCurrentUserEmail());
                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        // Check if they have setup a/c details
                        if(!dataSnapshot.exists()){
                            Intent intent = new Intent(getApplicationContext(), AccountSetup.class);
                            intent.putExtra("FROM", "LOGIN");
                            startActivity(intent);
                            Toast.makeText(getApplicationContext(), "You have not setup your account", Toast.LENGTH_SHORT).show();
                        } else {
                            for(DataSnapshot userSnap : dataSnapshot.getChildren()) {
                                addUserToPrefs(userSnap.getValue(User.class));
                            }
                        }
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(), "Error: "+ databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
            } else {
                progressDialog.dismiss();
                showHome();
            }
        } else {
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), "Can't login right now, ensure you have an internet connection", Toast.LENGTH_LONG).show();
        }
    }

    private void addUserToPrefs(User user) {
        // Get their profile
        editor.clear(); // Remove any stored prefs
        editor.putString(USER_ID, user.getId());
        editor.putString(USER_NAME, user.getName());
        editor.putString(USER_LOCATION, user.getLocation());
        editor.putString(USER_PHONE, user.getPhoneNumber());
        editor.commit();// Save all
        Intent home = new Intent(getApplicationContext(), BuyerHome.class);
        startActivity(home);
        finish();
    }
}
