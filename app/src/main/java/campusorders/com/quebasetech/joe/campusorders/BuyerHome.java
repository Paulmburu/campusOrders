/*
 * Copyright (c) 2018.  Que Base Technologies
 *
 * Joe Nyugoh 18/ 10/ 2018.
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

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import campusorders.com.quebasetech.joe.campusorders.fragments.BuyerDashboard;
import campusorders.com.quebasetech.joe.campusorders.fragments.SellerItems;
import campusorders.com.quebasetech.joe.campusorders.fragments.SellerOrders;
import campusorders.com.quebasetech.joe.campusorders.fragments.SellerStats;
import campusorders.com.quebasetech.joe.campusorders.fragments.Transactions;
import campusorders.com.quebasetech.joe.campusorders.fragments.UserSettings;
import campusorders.com.quebasetech.joe.campusorders.utils.utils;

public class BuyerHome extends AppCompatActivity {
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;
    private boolean isBuyer;
    private Context context;
    private FirebaseUser mAuthUser;
    private FirebaseAuth  firebaseAuth;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_home);
        context = this;

        // Get the current user profile
        mPreferences = getSharedPreferences(Settings_Profile.PROFILE_SETTINGS, MODE_PRIVATE);
        mEditor = mPreferences.edit();
        isBuyer = mPreferences.getBoolean(Settings_Profile.IS_BUYER, true);

        Toast.makeText(this, getSharedPreferences(utils.CURRENT_USER, MODE_PRIVATE).getString(utils.USER_NAME, "None"), Toast.LENGTH_SHORT).show();
        firebaseAuth = FirebaseAuth.getInstance();
        mAuthUser = firebaseAuth.getCurrentUser();

        if (isBuyer) {
            // Load default user fragment
            loadFragment(new BuyerDashboard());
        } else {
            // Load default user fragment
            loadFragment(new SellerOrders());
        }
        setupBottomNavigation();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_settings, menu);
        MenuItem user = menu.findItem(R.id.username_menu_item);
//        Toast.makeText(BuyerHome.this, mAuthUser.getEmail(), Toast.LENGTH_LONG).show();
        user.setTitle("Logged in as: "+mAuthUser.getEmail());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.current_profile:
                Intent settings = new Intent(context, Settings_Profile.class);
                startActivity(settings);
                return true;
            case R.id.logout_button:
                firebaseAuth.signOut();
                // Clear user prefs
                getSharedPreferences(utils.CURRENT_USER, MODE_PRIVATE).edit().clear().commit();
                finish();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    private void setupBottomNavigation() {
        if (isBuyer) {
            bottomNavigationView = (BottomNavigationView) findViewById(R.id.buyer_navigation);
            bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment fragment = null;
                    switch (menuItem.getItemId()) {
                        case R.id.navigation_home:
                            fragment = new BuyerDashboard();
                            break;
                        case R.id.navigation_transactions:
                            fragment = new Transactions();
                            break;
                        case R.id.navigation_settings:
                            fragment = new UserSettings();
                            break;
                    }
                    return loadFragment(fragment);
                }
            });
            findViewById(R.id.seller_navigation).setVisibility(View.GONE);
        } else {
            bottomNavigationView = (BottomNavigationView) findViewById(R.id.seller_navigation);
            bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment fragment = null;
                    switch (menuItem.getItemId()) {
                        case R.id.navigation_orders:
                            fragment = new SellerOrders();
                            break;
                        case R.id.navigation_items:
                            fragment = new SellerItems();
                            break;
                        case R.id.navigation_stats:
                            fragment = new SellerStats();
                            break;
                    }
                    return loadFragment(fragment);
                }
            });
            findViewById(R.id.buyer_navigation).setVisibility(View.GONE);
        }

    }
}
