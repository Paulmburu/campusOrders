/*
 * Copyright (c) 2018.  Que Base Technologies
 *
 * Joe Nyugoh 20/ 10/ 2018.
 * MIT License
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
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import campusorders.com.quebasetech.joe.campusorders.model.User;
import campusorders.com.quebasetech.joe.campusorders.utils.utils;

import static campusorders.com.quebasetech.joe.campusorders.utils.utils.CURRENT_USER;
import static campusorders.com.quebasetech.joe.campusorders.utils.utils.USER_ID;
import static campusorders.com.quebasetech.joe.campusorders.utils.utils.USER_LOCATION;
import static campusorders.com.quebasetech.joe.campusorders.utils.utils.USER_NAME;
import static campusorders.com.quebasetech.joe.campusorders.utils.utils.USER_PHONE;

public class AccountSetup extends AppCompatActivity {
    private Button mCompleteButton;
    private EditText mName, mPhoneNumber, mHostel, mRoom;
    private CheckBox mContestCheckbox;
    DatabaseReference usersRef;
    Context context;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_setup);

        Bundle extra = getIntent().getExtras();
        // No user record in database
        //allow setup to continue
        context = this;
        initMemory();
        setupEvents();
        /* On set-a/c completion, save user to prefs */
    }

    private void initMemory() {
        mCompleteButton = (Button) findViewById(R.id.save_button);
        mName = (EditText) findViewById(R.id.full_name);
        mPhoneNumber = (EditText) findViewById(R.id.phone_number);
        mHostel = (EditText) findViewById(R.id.location_hostel);
        mRoom = (EditText) findViewById(R.id.location_room);
        mContestCheckbox = (CheckBox) findViewById(R.id.consent_check);

        usersRef = FirebaseDatabase.getInstance().getReference("users");
        progressDialog = new ProgressDialog(context);
    }

    private void setupEvents() {
        mCompleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mName.getText().toString().trim();
                String hostel = mHostel.getText().toString().trim();
                String room = mRoom.getText().toString().trim();
                String phoneNumber = mPhoneNumber.getText().toString().trim();
                String id;

                if(!mContestCheckbox.isChecked()) {
                    mContestCheckbox.setError("You must agree to terms before continuing.");
                    mContestCheckbox.setTextColor(getResources().getColor(R.color.colorOrange));
                    return;
                }
                progressDialog.setMessage("Setting up account...");
                progressDialog.show();
                if(name.isEmpty() || hostel.isEmpty() || room.isEmpty() || phoneNumber.isEmpty()){
                    progressDialog.dismiss();
                    Toast.makeText(context, "All Fields are Required", Toast.LENGTH_SHORT).show();
                    return;
                }
                // generate a user id
                id = usersRef.push().getKey();
                // get user email
                String email = utils.getCurrentUserEmail();
                if(email.isEmpty()){
                    progressDialog.dismiss();
                    Toast.makeText(context, "Error setting your account try again", Toast.LENGTH_SHORT).show();
                    return;
                }
//                String location = String.join(" ", hostel, room);
                String location = hostel + " " + room;
                final User user = new User(id, name, email, phoneNumber, location);
                usersRef.child(user.getId())
                        .setValue(user)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                progressDialog.dismiss();
                                addUserToPrefs(user);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(context, "Error setting your account try again: "+ e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    private void addUserToPrefs(User user) {
        SharedPreferences userPrefs = getSharedPreferences(CURRENT_USER, MODE_PRIVATE);
        SharedPreferences.Editor editor = userPrefs.edit();
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
