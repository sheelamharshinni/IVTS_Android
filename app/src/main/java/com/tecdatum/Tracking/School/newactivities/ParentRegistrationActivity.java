package com.tecdatum.Tracking.School.newactivities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tecdatum.Tracking.School.R;
import com.tecdatum.Tracking.School.newConstants.Url_new;
import com.tecdatum.Tracking.School.newhelpers.Samplemyclass;
import com.tecdatum.Tracking.School.volley.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;


public class ParentRegistrationActivity extends AppCompatActivity {
    Spinner sp_class, sp_StudentID;
    String sp_class_id, sp_class_Name, sp_StudentID_id, sp_StudentID_Name;
    public static final String Master = Url_new.Master;
    public static final String StudentIDs = Url_new.StudentIDs;
    public static final String StudentDetailsForLogin = Url_new.StudentDetailsForLogin;
    public static final String SaveRegistrationInfo = Url_new.SaveRegistrationInfo;


    ArrayList<Samplemyclass> StudentClass_arraylist = new ArrayList<>();
    ArrayList<Samplemyclass> StudentID_arraylist = new ArrayList<>();
    EditText et_ParentName, et_DisplayName, et_MobileNumber;
    String S_et_ParentName, S_et_DisplayName, S_et_MobileNumber;
    Button bt_submit;
    String IMEI_Number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_registration);
        widgetinitialization();
        StudentClass();
    }

    private void widgetinitialization() {

        sp_class = findViewById(R.id.sp_class);
        sp_StudentID = findViewById(R.id.sp_StudentID);
        et_ParentName = findViewById(R.id.et_ParentName);
        et_DisplayName = findViewById(R.id.et_DisplayName);
        et_MobileNumber = findViewById(R.id.et_MobileNumber);
        bt_submit = findViewById(R.id.bt_submit);
        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stringnotnull();
                if (sp_class != null && sp_class.getSelectedItem() != null) {
                    if ((sp_class.getSelectedItem().toString().trim() == "Select")) {
                        if (sp_StudentID != null && sp_StudentID.getSelectedItem() != null) {
                            if ((sp_StudentID.getSelectedItem().toString().trim() == "Select")) {
                                if (!S_et_ParentName.equals("")) {
                                    if (!S_et_DisplayName.equals("")) {
                                        if (!S_et_MobileNumber.equals("")) {
                                            register();
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Please Enter Mobile Number", Toast.LENGTH_SHORT).show();

                                        }
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Please Enter Display Name", Toast.LENGTH_SHORT).show();

                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), "Please Enter Parent", Toast.LENGTH_SHORT).show();

                                }

                            } else {
                                Toast.makeText(getApplicationContext(), "Please Select Studet Name", Toast.LENGTH_SHORT).show();

                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Please Select Studet Name", Toast.LENGTH_SHORT).show();

                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Please Select Studet Class", Toast.LENGTH_SHORT).show();

                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please Select Studet Class", Toast.LENGTH_SHORT).show();

                }

            }


        });
    }


    private void stringnotnull() {

        S_et_ParentName = et_ParentName.getText().toString();
        if (S_et_ParentName != null) {

        } else {
            S_et_ParentName = "";
        }
        S_et_DisplayName = et_DisplayName.getText().toString();
        if (S_et_DisplayName != null) {

        } else {
            S_et_DisplayName = "";
        }

        S_et_MobileNumber = et_MobileNumber.getText().toString();
        if (S_et_MobileNumber != null) {

        } else {
            S_et_MobileNumber = "";
        }


    }

    @SuppressLint("ResourceAsColor")
    private void StudentClass_Master(ArrayList<Samplemyclass> str1) {


        ArrayAdapter<Samplemyclass> adapter = new ArrayAdapter<Samplemyclass>(getApplicationContext(),
                R.layout.spinner_item, str1);
        adapter.setDropDownViewResource(R.layout.spinner_item1);

        sp_class.setAdapter(adapter);
        sp_class.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (view != null) {
                }
                int pos = adapterView.getSelectedItemPosition();
                if (pos != 0) {

                    Samplemyclass country = (Samplemyclass) adapterView.getSelectedItem();
                    sp_class_id = country.getId();
                    sp_class_Name = country.getName();
                    et_ParentName.setText("");
                    et_DisplayName.setText("");
                    et_MobileNumber.setText("");
                    StudentID(sp_class_id);
                    Log.e("sp_class_id_name", "sp_class_id_name" + sp_class_id);
                } else {
                    sp_class_id = "";
                    sp_class_Name = "";
                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void StudentClass() {

        JSONObject jsonBody = new JSONObject();

        try {
            jsonBody.put("Action", "" + "StudentClass");
            final String mRequestBody = jsonBody.toString();
            Log.e("VOLLEY", "GetQuarterdropdown" + mRequestBody);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Master, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("VOLLEY", "GetQuarterdropdown" + response);
                    try {
                        JSONObject object = new JSONObject(response);
                        String code = object.optString("Code");
                        String message = object.optString("Message");

                        StudentClass_arraylist.clear();
                        StudentID_arraylist.clear();
                        et_ParentName.setText("");
                        et_DisplayName.setText("");
                        et_MobileNumber.setText("");
                        if (code.equalsIgnoreCase("0")) {
                            StudentClass_arraylist.clear();
                            JSONArray jArray = object.getJSONArray("Data");
                            int number = jArray.length();
                            String num = Integer.toString(number);
                            if (number == 0) {
                                Toast.makeText(getApplicationContext(), " No Data Found", Toast.LENGTH_LONG).show();
                            } else {

                                Samplemyclass wp0 = new Samplemyclass("0", "Select");
                                StudentClass_arraylist.add(wp0);
                                for (int i = 0; i < number; i++) {

                                    JSONObject json_data = jArray.getJSONObject(i);
                                    String e_id = json_data.getString("Id");
                                    String e_n = json_data.getString("Data");
                                    Samplemyclass wp = new Samplemyclass(e_id, e_n);
                                    StudentClass_arraylist.add(wp);
                                }
                            }
                            if (StudentClass_arraylist.size() > 0) {
                                StudentClass_Master(StudentClass_arraylist);
                            }


                        } else {
                            Samplemyclass wp0 = new Samplemyclass("0", "Select");
                            // Binds all strings into an array
                            StudentClass_arraylist.add(wp0);
                            if (StudentClass_arraylist.size() > 0) {
                                StudentClass_Master(StudentClass_arraylist);
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VOLLEY", error.toString());
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public byte[] getBody() throws AuthFailureError {
                    return mRequestBody == null ? null : mRequestBody.getBytes(StandardCharsets.UTF_8);
                }
            };
            VolleySingleton.getInstance().getRequestQueue().add(stringRequest);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void StudentID_Master(ArrayList<Samplemyclass> str1) {


        ArrayAdapter<Samplemyclass> adapter = new ArrayAdapter<Samplemyclass>(getApplicationContext(),
                R.layout.spinner_item, str1);

        sp_StudentID.setAdapter(adapter);
        Calendar cal = Calendar.getInstance();

        sp_StudentID.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (view != null) {
                }
                int pos = adapterView.getSelectedItemPosition();
                if (pos != 0) {
                    Samplemyclass country = (Samplemyclass) adapterView.getSelectedItem();
                    sp_StudentID_id = country.getId();
                    sp_StudentID_Name = country.getName();

                    monthdayreport(sp_StudentID_id);
                    Log.e("sp_gpname_name", "sp_gpname_name" + sp_StudentID_id);
                } else {
                    sp_StudentID_id = "";
                    sp_StudentID_Name = "";
                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void StudentID(String _id) {

        JSONObject jsonBody = new JSONObject();

        try {
            jsonBody.put("ActionName", "" + "GetStudentIDsDDL");
            jsonBody.put("ClassID", "" + _id);
            final String mRequestBody = jsonBody.toString();
            Log.e("VOLLEY", "GetQuarterdropdown" + mRequestBody);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, StudentIDs, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("VOLLEY", "GetQuarterdropdown" + response);
                    try {
                        JSONObject object = new JSONObject(response);
                        String code = object.optString("Code");
                        String message = object.optString("Message");

                        StudentID_arraylist.clear();
                        if (code.equalsIgnoreCase("0")) {
                            StudentID_arraylist.clear();
                            JSONArray jArray = object.getJSONArray("Data");
                            int number = jArray.length();
                            String num = Integer.toString(number);
                            if (number == 0) {
                                Toast.makeText(getApplicationContext(), " No Data Found", Toast.LENGTH_LONG).show();
                            } else {

                                Samplemyclass wp0 = new Samplemyclass("0", "Select");
                                StudentID_arraylist.add(wp0);
                                for (int i = 0; i < number; i++) {

                                    JSONObject json_data = jArray.getJSONObject(i);
                                    String e_id = json_data.getString("Id");
                                    String e_n = json_data.getString("Data");
                                    Samplemyclass wp = new Samplemyclass(e_id, e_n);
                                    StudentID_arraylist.add(wp);
                                }
                            }
                            if (StudentID_arraylist.size() > 0) {
                                StudentID_Master(StudentID_arraylist);
                            }


                        } else {

                            et_ParentName.setText("");
                            et_DisplayName.setText("");
                            et_MobileNumber.setText("");

                            Samplemyclass wp0 = new Samplemyclass("0", "Select");
                            // Binds all strings into an array
                            StudentID_arraylist.add(wp0);
                            if (StudentID_arraylist.size() > 0) {
                                StudentID_Master(StudentID_arraylist);
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VOLLEY", error.toString());
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public byte[] getBody() throws AuthFailureError {
                    return mRequestBody == null ? null : mRequestBody.getBytes(StandardCharsets.UTF_8);
                }
            };
            VolleySingleton.getInstance().getRequestQueue().add(stringRequest);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void monthdayreport(String ID) {

        final RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("ActionName", "GetStudentDetailsByID");
            jsonObject.put("StudentMasterID", ID);


            Log.d("Request", String.valueOf(jsonObject));
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, StudentDetailsForLogin, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("Response", String.valueOf(response));
                    try {
                        JSONObject object = new JSONObject(String.valueOf(response));
                        String code = response.optString("Code", "0");
                        String message = response.optString("Message", "Success");
                        if (code.equalsIgnoreCase("0")) {
                            JSONArray jsonArray = object.getJSONArray("StudentData");
                            int number = jsonArray.length();
                            String num = Integer.toString(number);
                            if (number == 0) {

                            } else {
                                for (int i = 0; i < number; i++) {
                                    try {

                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        Log.d("Request", String.valueOf(jsonObject));

                                        String StudentMasterID = jsonObject.getString("StudentMasterID".toString());
                                        String StudentName = jsonObject.getString("StudentName".toString());
                                        String StudentID = jsonObject.getString("StudentID".toString());
                                        String ClassName = jsonObject.getString("ClassName".toString());
                                        String ParentName = jsonObject.getString("ParentName".toString());
                                        String ParentMobile = jsonObject.getString("ParentMobile".toString());
                                        String ParentMobile2 = jsonObject.getString("ParentMobile2".toString());
                                        String RouteID = jsonObject.getString("RouteID".toString());
                                        String PickPointID = jsonObject.getString("PickPointID".toString());
                                        String DropPointID = jsonObject.getString("DropPointID".toString());

                                        if (ParentName != null) {
                                            et_ParentName.setText(ParentName);
                                        } else {

                                        }
                                        if (ParentMobile != null) {
                                            et_MobileNumber.setText(ParentMobile);
                                        } else {

                                        }


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }
                        } else {
                            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getApplicationContext());
                            alertDialog.setTitle("Response");
                            alertDialog.setMessage("" + message);

                            alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Write your code here to invoke YES event
                                    dialog.cancel();
                                }
                            });
                            alertDialog.show();

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }
            );


            requestQueue.add(jsonObjectRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void register() {

        TelephonyManager telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        IMEI_Number = telephonyManager.getDeviceId();

        final RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("ClassMasterID", sp_class_id);
            jsonObject.put("StudentMasterID", sp_StudentID_id);
            jsonObject.put("ParentName", S_et_ParentName);
            jsonObject.put("DisplayName", S_et_DisplayName);
            jsonObject.put("RegisteredMobileNo", S_et_MobileNumber);
            jsonObject.put("IMEINumber", IMEI_Number);


            Log.d("Request", String.valueOf(jsonObject));
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, SaveRegistrationInfo, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("Response", String.valueOf(response));

                    String code = response.optString("Code", "0");
                    String message = response.optString("Message", "Success");
                    if (code.equalsIgnoreCase("1")) {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(getApplicationContext(), LoginActivity_New.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                    }

                }


            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }
            );


            requestQueue.add(jsonObjectRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
