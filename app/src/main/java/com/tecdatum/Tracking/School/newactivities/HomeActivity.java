package com.tecdatum.Tracking.School.newactivities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.tecdatum.Tracking.School.R;
import com.tecdatum.Tracking.School.fragments.Admin_Fragment;
import com.tecdatum.Tracking.School.fragments.ContactUS;
import com.tecdatum.Tracking.School.fragments.Dashboard;
import com.tecdatum.Tracking.School.fragments.DashboardVehicleList;
import com.tecdatum.Tracking.School.fragments.MonthDayReport_Fragment;

import static com.tecdatum.Tracking.School.newactivities.SplashScreen.MY_PREFS_NAME;


public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final float END_SCALE = 0.7f;
    String UserName;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                HomeActivity.this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);
        drawer.setScrimColor(Color.TRANSPARENT);
        drawer.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
                                     @Override
                                     public void onDrawerSlide(View drawerView, float slideOffset) {

                                         final float diffScaledOffset = slideOffset * (1 - END_SCALE);
                                         final float offsetScale = 1 - diffScaledOffset;

                                     }

                                     @Override
                                     public void onDrawerClosed(View drawerView) {
                                     }
                                 }
        );
        textView = findViewById(R.id.textView);
        SharedPreferences bb = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        UserName = bb.getString("UserName", "");
        textView.setText("Welcome to " + UserName);
        Dashboard fr11 = new Dashboard();
        opennfrag(fr11);
    }

    private void opennfrag(final Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.tabFrameLayout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // adds item to action bar
        getMenuInflater().inflate(R.menu.menu_main, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {


            SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            SharedPreferences.Editor edit = prefs.edit();
            edit.clear();
            edit.commit();
            Intent a = new Intent(getApplicationContext(), LoginActivity_New.class);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            a.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(a);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.navigation_dashboard) {
            Dashboard fr11 = new Dashboard();
            opennfrag(fr11);

        }
        if (id == R.id.navigation_qv) {


        }

        if (id == R.id.navigation_nbv) {


        }
        if (id == R.id.nav_contact) {
            ContactUS frag = new ContactUS();

            FragmentManager fragmentManager1 = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
            fragmentTransaction1.replace(R.id.tabFrameLayout, frag);
            fragmentTransaction1.addToBackStack(null);
            fragmentTransaction1.commit();

        }
        if (id == R.id.navigation_ls) {


        } else if (id == R.id.navigation_logout) {

            SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            SharedPreferences.Editor edit = prefs.edit();
            edit.clear();
            edit.commit();
            Intent a = new Intent(getApplicationContext(), LoginActivity_New.class);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            a.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(a);
            finish();
        } else if (id == R.id.navigation_report) {

            MonthDayReport_Fragment frag = new MonthDayReport_Fragment();

            FragmentManager fragmentManager1 = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
            fragmentTransaction1.replace(R.id.tabFrameLayout, frag);
            fragmentTransaction1.addToBackStack(null);
            fragmentTransaction1.commit();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            boolean selected = false;
            switch (item.getItemId()) {
                case R.id.navigation_dashboard:
                    //  toolbar.setTitle("Dashboard");
                    Dashboard fr = new Dashboard();
                    opennfrag(fr);
                    selected=true;
                    break;
                case R.id.navigation_qv:
                    DashboardVehicleList fragmet = new DashboardVehicleList();
                    Bundle args = new Bundle();
                    args.putString("Car", "Car");
                    args.putString("Run", "Run");
                    fragmet.setArguments(args);
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.tabFrameLayout, fragmet);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    // toolbar.setTitle("QuickView");
                    selected=true;
                    break;

                case R.id.navigation_administartion:


                    Admin_Fragment adm = new Admin_Fragment();
                    Bundle admd = new Bundle();
                    admd.putString("Car", "Car");
                    admd.putString("Run", "Run");
                    adm.setArguments(admd);
                    fragmentManager = getSupportFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.tabFrameLayout, adm);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    selected=true;
                    break;
                case R.id.navigation_report:

                    MonthDayReport_Fragment frag = new MonthDayReport_Fragment();

                    FragmentManager fragmentManager1 = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
                    fragmentTransaction1.replace(R.id.tabFrameLayout, frag);
                    fragmentTransaction1.addToBackStack(null);
                    fragmentTransaction1.commit();
                    // toolbar.setTitle("Reports");
                    selected=true;
                    break;
                    default:
                        Dashboard frs = new Dashboard();
                        opennfrag(frs);
                        break;
            }
            return  selected;
        }
    };

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
