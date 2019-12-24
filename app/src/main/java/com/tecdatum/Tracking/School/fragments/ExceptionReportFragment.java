package com.tecdatum.Tracking.School.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.github.paolorotolo.expandableheightlistview.ExpandableHeightListView;
import com.tecdatum.Tracking.School.R;
import com.tecdatum.Tracking.School.newConstants.Url_new;
import com.tecdatum.Tracking.School.newhelpers.Monthdayreportitems;
import com.tecdatum.Tracking.School.newhelpers.Spinneritems;
import com.tecdatum.Tracking.School.volley.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class ExceptionReportFragment extends Fragment {

    public static final String MY_PREFS_NAME = "MyPrefsFile";
    private String url_ExceptionReport = Url_new.ExceptionReport;
    String message, S_month, S_VehicleID;
    String S_VehicleName;
    Button bt_back;
    String  SOrgid, UserID;
    TextView tv_e_t1, tv_e_t2, tv_e_t3, tv_e_t4, tv_e_t5, tv_e_t7, tv_e_t6;
    String default_From_date_report, default_To_date_report;
    String e_Movementof2to4PM, e_Movementof2to6AM, e_Location, e_PoliceStation,
             e_Movementof6to9PM, e_Distance, e_GeofencingCrossed;
    TextView tv_vehiclename, tv_Exception_time;
    TextView tv_A, tv_B, tv_C;
    String MovementofTimeA, MovementofTimeB, MovementofTimeC, Header;
    View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.after_exceptionreport, container, false);

        bt_back = (Button) v.findViewById(R.id.bt_back);
        tv_vehiclename = (TextView) v.findViewById(R.id.tv_vehiclename);
        tv_A = (TextView) v.findViewById(R.id.tv_A);
        tv_B = (TextView) v.findViewById(R.id.tv_B);
        tv_C = (TextView) v.findViewById(R.id.tv_C);
        tv_e_t1 = (TextView) v.findViewById(R.id.tv_e_t1);
        tv_e_t2 = (TextView) v.findViewById(R.id.tv_e_t2);
        tv_e_t3 = (TextView) v.findViewById(R.id.tv_e_t3);
        tv_e_t4 = (TextView) v.findViewById(R.id.tv_e_t4);
        tv_e_t5 = (TextView) v.findViewById(R.id.tv_e_t5);
        tv_e_t6 = (TextView) v.findViewById(R.id.tv_e_t6);
        tv_e_t7 = (TextView) v.findViewById(R.id.tv_e_t7);

        tv_Exception_time = (TextView) v.findViewById(R.id.tv_Exception_time);


        bt_back.setVisibility(View.GONE);


        final Bundle b = getArguments();
        if (b != null) {
            S_VehicleID = b.getString("Vehicle_ID");
            S_month = b.getString("Vehicle_Name");

            default_To_date_report = b.getString("todate");
            default_From_date_report = b.getString("fromdate");
            S_VehicleName = b.getString("Vehicle_Name");
            tv_vehiclename.setText("" + S_VehicleName);
            Report_Exception(S_VehicleID);
            Log.d("VEHICLENAME", S_VehicleName);
        } else {

        }
        return v;

    }


    private void Report_Exception(String VehId) {


        SharedPreferences bb = getContext().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        SOrgid = bb.getString("Orgid", "");
        UserID = bb.getString("UserID", "");


        try {


            JSONObject jsonBody = new JSONObject();
            jsonBody.put("VehicleIds", VehId);
            jsonBody.put("DateType", "");
            jsonBody.put("UserId", "" + UserID);
            jsonBody.put("StartDate", default_To_date_report);
            jsonBody.put("EndDate", default_From_date_report);
            jsonBody.put("MonthYear", "");


            final String mRequestBody = jsonBody.toString();


            tv_Exception_time.setText("" + "\t" + "From - " + "" + default_To_date_report + "\t" + "To - " + "" + default_From_date_report);

            Log.e("VOLLEY", "request ExceptionReport" + mRequestBody);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url_ExceptionReport, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("VOLLEY", "" + "Response ExceptionReport" + response);
                    try {

                        JSONObject object = new JSONObject(response);
                        String code = object.optString("Code").toString();
                        message = object.optString("Message").toString();
                        if (code.equalsIgnoreCase("0")) {
                            MovementofTimeA = object.optString("MovementofTimeA").toString();
                            MovementofTimeB = object.optString("MovementofTimeB").toString();
                            MovementofTimeC = object.optString("MovementofTimeC").toString();
                            Header = object.optString("Header").toString();

                            tv_A.setText("" + MovementofTimeA);
                            tv_B.setText("" + MovementofTimeB);
                            tv_C.setText("" + MovementofTimeC);
                            JSONArray jArray = object.getJSONArray("Data");
                            int number = jArray.length();
                            String num = Integer.toString(number);
                            Log.i("history lat", num);

                            if (number == 0) {
                            } else {
                                for (int i = 0; i < number; i++) {
                                    JSONObject json_data = jArray.getJSONObject(i);
                                    e_Movementof2to4PM = json_data.getString("A").toString();
                                    e_Movementof2to6AM = json_data.getString("B").toString();
                                    e_Movementof6to9PM = json_data.getString("C").toString();
                                    e_Distance = json_data.getString("Distance").toString();
                                    e_GeofencingCrossed = json_data.getString("GeofencingCrossed").toString();
                                    e_Location = json_data.getString("Location").toString();
                                    e_PoliceStation = json_data.getString("PoliceStation").toString();

                                    tv_e_t1.setText("" + e_Location);
                                    tv_e_t2.setText("" + e_Movementof2to4PM);
                                    tv_e_t3.setText("" + e_Movementof2to6AM);
                                    tv_e_t4.setText("" + e_Movementof6to9PM);
                                    tv_e_t5.setText("" + e_GeofencingCrossed);
                                    tv_e_t6.setText("" + e_Distance);

                                }
                            }

                        } else {
                            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                            alertDialog.setTitle("Response");
                            alertDialog.setIcon(R.drawable.alert);
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
                    Log.e("VOLLEY", error.toString());
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                        return null;
                    }
                }
            };
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    5000000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleySingleton.getInstance().getRequestQueue().add(stringRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}