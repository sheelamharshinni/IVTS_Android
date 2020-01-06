package com.tecdatum.Tracking.School.newactivities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.tecdatum.Tracking.School.MapsActivity;
import com.tecdatum.Tracking.School.R;
import com.tecdatum.Tracking.School.newConstants.Url_new;
import com.tecdatum.Tracking.School.volley.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import static com.tecdatum.Tracking.School.newConstants.Url_new.HolidayStatus;
import static com.tecdatum.Tracking.School.newactivities.SplashScreen.MY_PREFS_NAME;


public class LoginActivity_New extends AppCompatActivity {
    Button sign_in;
    EditText et_mobilenumber;
    String S_et_mobilenumber;
    String VersionCode, VersionName;
    private String TrackingLogin = Url_new.TrackingLogin;
    String message, Code;
    TextView error_message;
    String LevelName;
    TextView sign_up;
    private Boolean saveLogin;
    private SharedPreferences.Editor loginPrefsEditor;
    TextView tv_VersionCode;
    SharedPreferences loginPreferences;
    SharedPreferences permissionStatus;
    CheckBox savecredentials;
    Integer Datetime;
    String StartTime, EndTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login__new);
        sign_in = findViewById(R.id.sign_in);
        et_mobilenumber = findViewById(R.id.et_mobilenumber);
        error_message = findViewById(R.id.tv_error_tr);
        tv_VersionCode = findViewById(R.id.tv_VersionCode);
        savecredentials = findViewById(R.id.savecredentials);
        tv_VersionCode.setVisibility(View.VISIBLE);
        Calendar c = Calendar.getInstance();
        Datetime = c.get(Calendar.HOUR_OF_DAY);
//        SimpleDateFormat sdf = new SimpleDateFormat("hh");
//        Datetime = sdf.format(c.getTime());
        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                S_et_mobilenumber = et_mobilenumber.getText().toString();
                if (savecredentials.isChecked()) {
                    loginPrefsEditor.putBoolean("saveLogin", true);
                    loginPrefsEditor.putString("username", S_et_mobilenumber);
                    loginPrefsEditor.commit();
                } else {
                    loginPrefsEditor.clear();
                    loginPrefsEditor.commit();
                }
                if (S_et_mobilenumber == null) {
                    S_et_mobilenumber = "";
                } else {

                }

                if (!S_et_mobilenumber.equals("")) {
                    if (S_et_mobilenumber.length() == 10) {
                        GetDataFromServer();


                    } else {
                        et_mobilenumber.setError("Registered Mobile Number Should be 10 digits");
                    }
                } else {
                    et_mobilenumber.setError("Please Enter Registered Mobile Number");

                }

            }


        });
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        if (saveLogin == true) {
            et_mobilenumber.setText(loginPreferences.getString("username", ""));
            savecredentials.setChecked(true);
        }
        permissionStatus = getSharedPreferences("permissionStatus", MODE_PRIVATE);
        sign_up = findViewById(R.id.sign_up);
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ParentRegistrationActivity.class);
                startActivity(intent);
            }
        });
        tv_VersionCode.setText("" + getVersionName());
    }

    private void GetDataFromServer() {
        try {
            VersionCode = String.valueOf(getVersionCode());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            VersionName = String.valueOf(getVersionName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JSONObject obj = new JSONObject();
        try {
            obj.put("MobileNumber", S_et_mobilenumber);
            obj.put("VersionCode", "" + VersionCode);
            obj.put("VersionName", VersionName);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String mRequestBody = obj.toString();
        Log.e("VOLLEY", "Login" + "Input" + mRequestBody);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                TrackingLogin, obj, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.e("VOLLEY", "Login" + "Response" + response.toString());
                int id = Integer.parseInt(response.optString("Code").toString());
                message = response.optString("Message").toString();

                Code = response.optString("Code").toString();

                if (Code.equals("0")) {
                    String UserName = response.optString("UserName").toString();
                    String UserType = response.optString("UserType").toString();
                    String HierarchyId = response.optString("HierarchyId").toString();
                    LevelName = response.optString("LevelName").toString();
                    String HierarchyName = response.optString("HierarchyName").toString();
                    String OrgName = response.optString("OrgName").toString();
                    String Orgid = response.optString("Orgid").toString();
                    String CenterLatitude = response.optString("CenterLatitude").toString();
                    String CenterLongitude = response.optString("CenterLongitude").toString();
                    String ParentMobile = response.optString("ParentMobile").toString();
                    String ParentMobile2 = response.optString("ParentMobile2").toString();
                    String RouteID = response.optString("RouteID").toString();
                    String RouteName = response.optString("RouteName").toString();
                    String PickPointID = response.optString("PickPointID").toString();
                    String Pickpoint = response.optString("Pickpoint").toString();
                    String DropPointID = response.optString("DropPointID").toString();
                    String DropPoint = response.optString("DropPoint").toString();
                    String StudentClass = response.optString("StudentClass").toString();
                    String StudentID = response.optString("StudentID").toString();
                    String UserID = response.optString("UserID").toString();
                    String LevelId = response.optString("LevelId").toString();
                    String ParentName = response.optString("ParentName").toString();
                    String VehicleID = response.optString("VehicleID".toString());
                    String VehicleNumber = response.optString("VehicleNumber".toString());
                    String Live_Latitude = response.optString("Live_Latitude".toString());
                    String Live_Longitude = response.optString("Live_Longitude".toString());
                    String DisplayName = response.optString("DisplayName".toString());
                    String PointLatitude = response.optString("PointLatitude".toString());
                    String PointLongitude = response.optString("PointLongitude".toString());
                    StartTime = response.optString("StartTime", "");
                    EndTime = response.optString("EndTime", "");


                    SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                    SharedPreferences.Editor edit = prefs.edit();
                    edit.putString("UserName", UserName);
                    edit.putString("UserType", UserType);
                    edit.putString("HierarchyId", HierarchyId);
                    edit.putString("LevelName", LevelName);
                    edit.putString("HierarchyName", HierarchyName);
                    edit.putString("LevelId", LevelId);
                    edit.putString("OrgName", OrgName);
                    edit.putString("Orgid", Orgid);
                    edit.putString("CenterLatitude", CenterLatitude);
                    edit.putString("CenterLongitude", CenterLongitude);
                    edit.putString("ParentMobile", ParentMobile);
                    edit.putString("ParentMobile2", ParentMobile2);
                    edit.putString("RouteID", RouteID);
                    edit.putString("RouteName", RouteName);
                    edit.putString("PickPointID", PickPointID);
                    edit.putString("Pickpoint", Pickpoint);
                    edit.putString("DropPointID", DropPointID);
                    edit.putString("DropPoint", DropPoint);
                    edit.putString("StudentClass", StudentClass);
                    edit.putString("StudentID", StudentID);
                    edit.putString("UserID", UserID);
                    edit.putString("ParentName", ParentName);
                    edit.putString("VehicleID", VehicleID);
                    edit.putString("VehicleNumber", VehicleNumber);
                    edit.putString("Live_Latitude", Live_Latitude);
                    edit.putString("Live_Longitude", Live_Longitude);
                    edit.putString("DisplayName", DisplayName);
                    edit.putString("PointLatitude", PointLatitude);
                    edit.putString("PointLongitude", PointLongitude);
                    edit.putString("StartTime", StartTime);
                    edit.putString("EndTime", EndTime);

                    edit.commit();


                    if ((LevelName.equalsIgnoreCase("Parent"))) {
                        GetDataFromServer_holiday(HierarchyId);


                    } else {
                        Intent i = new Intent(getApplicationContext(), HomeActivity_new.class);
                        startActivity(i);
                    }


                    Toast.makeText(LoginActivity_New.this, message, Toast.LENGTH_SHORT).show();
                    error_message.setText("");
                } else {
                    if (Code.equalsIgnoreCase("-2")) {
                        Toast.makeText(LoginActivity_New.this, message, Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(LoginActivity_New.this, message, Toast.LENGTH_LONG).show();

                    }

                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {


                error_message.setVisibility(View.VISIBLE);
                error_message.setText("Unable to Connect to Server");
            }
        });
        VolleySingleton.getInstance().getRequestQueue().add(jsonObjReq);
    }

    private void GetDataFromServer_holiday(String holiday) {


        JSONObject obj = new JSONObject();
        try {
            obj.put("HierarchyID", holiday);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String mRequestBody = obj.toString();
        Log.e("VOLLEY", "Login" + "Input" + mRequestBody);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                HolidayStatus, obj, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.e("VOLLEY", "Login" + "Response" + response.toString());
                int id = Integer.parseInt(response.optString("Code").toString());
                message = response.optString("Message").toString();

                Code = response.optString("Code").toString();

                if (Code.equals("1")) {
                    String Message = response.optString("Message").toString();
                    String Reason = response.optString("Reason").toString();


                    SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                    SharedPreferences.Editor edit = prefs.edit();

                    edit.putString("Message", Message);
                    edit.putString("Reason", Reason);
                    edit.commit();
                    Intent i = new Intent(getApplicationContext(), HolidatStatusActivity.class);
                    startActivity(i);



                } else {
                    if (Datetime >= Integer.valueOf(StartTime) && Datetime < Integer.valueOf(EndTime)) { //checkes whether the current time is between 14:49:00 and 20:11:13.
                            Intent i = new Intent(getApplicationContext(), MapsActivity.class);
                            startActivity(i);

                    } else {

                        Intent i = new Intent(getApplicationContext(), HolidayActivity.class);
                        startActivity(i);
                    }
                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {


                error_message.setVisibility(View.VISIBLE);
                error_message.setText("Unable to Connect to Server");
            }
        });
        VolleySingleton.getInstance().getRequestQueue().add(jsonObjReq);
    }

    public int getVersionCode() {
        int v = 0;
        try {
            v = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {

        }
        return v;
    }

    public String getVersionName() {
        String v = "";
        try {
            v = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {

        }
        return v;
    }
}
