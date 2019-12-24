package com.tecdatum.Tracking.School.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

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
import com.tecdatum.Tracking.School.newactivities.LoginActivity_New;
import com.tecdatum.Tracking.School.newhelpers.Samplemyclass;
import com.tecdatum.Tracking.School.volley.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;
import static com.tecdatum.Tracking.School.newactivities.ParentRegistrationActivity.StudentDetailsForLogin;
import static com.tecdatum.Tracking.School.newactivities.SplashScreen.MY_PREFS_NAME;

public class EditParent_Details extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String UpdateRegistrationData = Url_new.UpdateRegistrationData;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String ParentMobile, UserName, DisplayName, StudentID, StudentClass, ParentName;
    View view;
    EditText et_oldnumber, et_newnumber;
    String S_et_oldnumber, S_et_newnumber, S_et_Displayname;
    Button bt_updateinfo;
    TextView tv_studentname;
    EditText et_ParentName, et_Displayname;
    ArrayList<Samplemyclass> StudentClass_arraylist = new ArrayList<>();
    Spinner sp_class;
    public static final String Master = Url_new.Master;
    String sp_class_id, sp_class_Name;

    public EditParent_Details() {
    }

    public static EditParent_Details newInstance(String param1, String param2) {
        EditParent_Details fragment = new EditParent_Details();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_edit_parent__details, container, false);

        et_oldnumber = view.findViewById(R.id.et_oldnumber);
        et_newnumber = view.findViewById(R.id.et_newnumber);
        tv_studentname = view.findViewById(R.id.et_studentname);
        bt_updateinfo = view.findViewById(R.id.bt_updateinfo);
        et_ParentName = view.findViewById(R.id.et_ParentName);
        et_Displayname = view.findViewById(R.id.et_Displayname);
        sp_class = view.findViewById(R.id.sp_class);


        SharedPreferences bb = getContext().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        ParentMobile = bb.getString("ParentMobile", "");
        UserName = bb.getString("UserName", "");
        StudentID = bb.getString("StudentID", "");
        DisplayName = bb.getString("DisplayName", "");

        ParentName = bb.getString("ParentName", "");

        StudentClass = bb.getString("StudentClass", "");
        StudentClass();
        if (ParentMobile != null) {
            et_oldnumber.setText(ParentMobile);
        } else {
            et_oldnumber.setText("");
        }

        if (UserName != null) {
            tv_studentname.setText(UserName);
        } else {
            tv_studentname.setText("");

        }
        if (DisplayName != null) {
            et_Displayname.setText(DisplayName);
        } else {
            et_Displayname.setText("");

        }

        if (ParentName != null) {
            et_ParentName.setText(ParentName);
        } else {
            et_ParentName.setText("");

        }


        bt_updateinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringNotNul();

                if (sp_class != null && sp_class.getSelectedItem() != null) {
                    if (!(sp_class.getSelectedItem().toString().trim() == "Select")) {
                        if (!S_et_Displayname.equalsIgnoreCase("")) {
                            if (!S_et_oldnumber.equalsIgnoreCase("")) {
                                if (!S_et_newnumber.equalsIgnoreCase("")) {

                                    update();
                                } else {
                                    Toast.makeText(getContext(), "Please Enter New Phone Number", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                Toast.makeText(getContext(), "Old Number Must", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getContext(), "Please Enter Display Name", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "Please Select Class", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Please Select Class", Toast.LENGTH_SHORT).show();
                }


            }


        });

        return view;
    }

    private void update() {


        final RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("OldMobileNumber", S_et_oldnumber);
            jsonObject.put("NewMobileNumber", S_et_newnumber);
            jsonObject.put("StudentID", StudentID);
            jsonObject.put("DisplayName", S_et_Displayname);
            jsonObject.put("ClassID", sp_class_id);

            Log.d("Request", "VOLLEYATRINGREQUEST" + String.valueOf(jsonObject));
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, UpdateRegistrationData, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("Response", String.valueOf(response));

                    String code = response.optString("Code", "0");
                    String message = response.optString("Message", "Success");
                    if (code.equalsIgnoreCase("1")) {
                        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getContext(), LoginActivity_New.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();

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

    private void StringNotNul() {

        S_et_oldnumber = et_oldnumber.getText().toString();
        S_et_newnumber = et_newnumber.getText().toString();
        S_et_Displayname = et_Displayname.getText().toString();
        if (S_et_oldnumber == null) {
            S_et_oldnumber = "";
        } else {

        }

        if (S_et_newnumber == null) {
            S_et_newnumber = "";
        } else {

        }

        if (S_et_Displayname == null) {
            S_et_Displayname = "";
        } else {

        }
    }

    private void StudentClass_Master(ArrayList<Samplemyclass> str1) {


        ArrayAdapter<Samplemyclass> adapter = new ArrayAdapter<Samplemyclass>(getContext(),
                R.layout.spinner_item, str1);

        sp_class.setAdapter(adapter);
        for (int i = 0; i < StudentClass_arraylist.size(); i++) {
            if (StudentClass_arraylist.get(i).getName().equals(StudentClass)) {
                sp_class.setSelection(i);
            } else {
                Log.d("SELECTEDMONTH", "FALSE");

            }
        }

        sp_class.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (view != null) {
                    ((TextView) adapterView.getChildAt(0)).setTextColor(Color.parseColor("#000000"));
                }
                int pos = adapterView.getSelectedItemPosition();
                if (pos != 0) {
                    ((TextView) adapterView.getChildAt(0)).setTextColor(Color.parseColor("#000000"));
                    Samplemyclass country = (Samplemyclass) adapterView.getSelectedItem();
                    sp_class_id = country.getId();
                    sp_class_Name = country.getName();

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

    private void monthdayreport(String ID) {

        final RequestQueue requestQueue = Volley.newRequestQueue(getContext());
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
                                            et_oldnumber.setText(ParentMobile);
                                        } else {

                                        }


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }
                        } else {
                            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
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
                        if (code.equalsIgnoreCase("0")) {
                            StudentClass_arraylist.clear();
                            JSONArray jArray = object.getJSONArray("Data");
                            int number = jArray.length();
                            String num = Integer.toString(number);
                            if (number == 0) {
                                Toast.makeText(getContext(), " No Data Found", Toast.LENGTH_LONG).show();
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


}
