package com.tecdatum.Tracking.School.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.paolorotolo.expandableheightlistview.ExpandableHeightListView;
import com.tecdatum.Tracking.School.R;
import com.tecdatum.Tracking.School.newConstants.Url_new;
import com.tecdatum.Tracking.School.newhelpers.Monthdayreportitems;
import com.tecdatum.Tracking.School.newhelpers.Samplemyclass;
import com.tecdatum.Tracking.School.newhelpers.Spinneritems;
import com.tecdatum.Tracking.School.volley.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class MonthReportFragment extends Fragment {
    public static final String MY_PREFS_NAME = "MyPrefsFile";

    private String url_MonthSummaryReport = Url_new.MonthSummaryReport;
    String message, S_month, S_VehicleID;
    String S_VehicleName;
    private ArrayList<Samplemyclass> list1 = new ArrayList<>();
    String m_Division, m_CircleName, vehicle, m_VehicleType, m_work, m_stop, m_idle, m_nogps,
            m_maxspd, m_avgspd, m_distance, m_DistrictName, m_ACTime;

    TextView tv_m_wrk, tv_m_dist, tv_m_stop, tv_m_idle, tv_m_nosignal, tv_m_mspeed, tv_m_ac;
    TextView tv_vehiclename, tv_Month_time;
    Spinner sp_month_alert;
    Button bt_submit;
    LinearLayout ll_Datafound;
    View v;
    TextView tv_back;
    public static final String url_month_year = Url_new.Master;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.after_activity_month_report, container, false);
        tv_vehiclename = (TextView) v.findViewById(R.id.tv_vehiclename);

        tv_m_wrk = (TextView) v.findViewById(R.id.tv_m_wrk);
        tv_m_ac = (TextView) v.findViewById(R.id.tv_m_ac);
        tv_m_dist = (TextView) v.findViewById(R.id.tv_m_dist);
        tv_m_stop = (TextView) v.findViewById(R.id.tv_m_stop);
        tv_m_idle = (TextView) v.findViewById(R.id.tv_m_idle);
        tv_m_nosignal = (TextView) v.findViewById(R.id.tv_m_nosignal);
        tv_m_mspeed = (TextView) v.findViewById(R.id.tv_m_mspeed);
        ll_Datafound = v.findViewById(R.id.ll_Datafound);
        sp_month_alert = v.findViewById(R.id.sp_month_alert);
        tv_Month_time = (TextView) v.findViewById(R.id.tv_Month_time);
        tv_back = v.findViewById(R.id.tv_back);
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MonthDayReport_Fragment fragment = new MonthDayReport_Fragment();
                opennfrag(fragment);

            }
        });
        bt_submit = v.findViewById(R.id.bt_submit);
        getmonthyear();


        final Bundle b = getArguments();
        if (b != null) {
            S_VehicleID = b.getString("Vehicle_ID");
            S_month = b.getString("Vehicle");
            S_VehicleName = b.getString("Vehicle_Name");
            tv_vehiclename.setText("" + S_VehicleName);

        } else {

        }
        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Report_monthly(S_VehicleID);
            }
        });

        return v;

    }

    private void getmonthyear() {


        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Action", "MonthYear");

            Log.d("Request", String.valueOf(jsonObject));

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url_month_year, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("Responce", String.valueOf(response));

                    JSONObject object = null;
                    try {
                        object = new JSONObject(String.valueOf(response));
                        String code = response.optString("Code", "");
                        String message = response.optString("Message", "");

                        if (code.equalsIgnoreCase("0")) {


                            JSONArray jsonArray = null;
                            try {
                                list1.clear();
                                Samplemyclass wp1 = new Samplemyclass("0", "Select Month");
                                list1.add(wp1);
                                jsonArray = object.getJSONArray("Data");
                                int number = jsonArray.length();

                                for (int i = 0; i < number; i++) {
                                    try {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                                        Log.d("Request", String.valueOf(jsonObject));
                                        String id = jsonObject.getString("Id".toString());
                                        String data = jsonObject.getString("Data".toString());
                                        Samplemyclass dataSet = new Samplemyclass(id, data);

                                        list1.add(dataSet);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    if (list1.size() > 0) {
                                        Month_si(list1);
                                    }
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });

            requestQueue.add(jsonObjectRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void Month_si(ArrayList<Samplemyclass> str1) {

        ArrayAdapter<Samplemyclass> adapter = new ArrayAdapter<Samplemyclass>(getContext(),
                R.layout.spinner_item, str1);

        sp_month_alert.setAdapter(adapter);
        sp_month_alert.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (view != null) {
                    try {
                        ((TextView) adapterView.getChildAt(0)).setTextColor(Color.parseColor("#000000"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                int pos = adapterView.getSelectedItemPosition();

                try {
                    ((TextView) adapterView.getChildAt(0)).setTextColor(Color.parseColor("#000000"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Samplemyclass dataSet = (Samplemyclass) adapterView.getSelectedItem();

                dataSet.getName();
                dataSet.getId();
                S_month = dataSet.getName();
            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void Report_monthly(String VehId) {


        tv_Month_time.setText("" + S_month);

        try {


            JSONObject jsonBody = new JSONObject();

            jsonBody.put("VehicleIds", VehId);
            jsonBody.put("monthyear", S_month);
            final String mRequestBody = jsonBody.toString();

            Log.e("VOLLEY", "request MonthSummaryReport" + mRequestBody);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url_MonthSummaryReport, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("VOLLEY", "" + "Response MonthSummaryReport" + response);
                    try {

                        JSONObject object = new JSONObject(response);
                        String code = object.optString("Code").toString();
                        message = object.optString("Message").toString();
                        if (code.equalsIgnoreCase("0")) {
                            ll_Datafound.setVisibility(View.VISIBLE);
                            JSONArray jArray = object.getJSONArray("Data");
                            int number = jArray.length();
                            String num = Integer.toString(number);
                            Log.i("history lat", num);
                            if (number == 0) {
                            } else {
                                for (int i = 0; i < number; i++) {
                                    JSONObject json_data = jArray.getJSONObject(i);
                                    vehicle = json_data.getString("VehicleNo").toString();
                                    m_VehicleType = json_data.getString("VehicleType").toString();
                                    m_work = json_data.getString("work").toString();
                                    m_idle = json_data.getString("idle").toString();
                                    m_stop = json_data.getString("stop").toString();
                                    m_nogps = json_data.getString("nogps").toString();
                                    m_maxspd = json_data.getString("maxspd").toString();
                                    m_avgspd = json_data.getString("avgspd").toString();
                                    m_distance = json_data.getString("distance").toString();
                                    m_ACTime = json_data.getString("ACTime").toString();
                                    m_Division = json_data.getString("SubDivision_Zone").toString();
                                    m_CircleName = json_data.getString("Circle_Division").toString();


                                    tv_m_wrk.setText("" + m_work);
                                    tv_m_dist.setText("" + m_distance);
                                    tv_m_stop.setText("" + m_stop);
                                    tv_m_idle.setText("" + m_idle);
                                    tv_m_nosignal.setText("" + m_nogps);
                                    tv_m_mspeed.setText("" + m_maxspd);
                                    tv_m_ac.setText("" + m_ACTime);

                                }
                            }

                        } else {
                            ll_Datafound.setVisibility(View.GONE);
                            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                            alertDialog.setTitle("Response");
                            alertDialog.setIcon(R.drawable.alert);
                            alertDialog.setMessage("" + message);
                            alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
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
                    ll_Datafound.setVisibility(View.GONE);
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

    private void opennfrag(final Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.tabFrameLayout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }
}