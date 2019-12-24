package com.tecdatum.Tracking.School.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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
import com.tecdatum.Tracking.School.newhelpers.DatePickerReports;
import com.tecdatum.Tracking.School.newhelpers.Monthdayreportitems;
import com.tecdatum.Tracking.School.newhelpers.Spinneritems;
import com.tecdatum.Tracking.School.volley.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static android.content.Context.MODE_PRIVATE;

public class PerformanceReportFragment extends Fragment {

    public static final String MY_PREFS_NAME = "MyPrefsFile";
    private String url_PerformanceReport = Url_new.PerformanceReport;
    String message, S_month, S_VehicleID;
    String S_VehicleName;
    String tv_vehcle, tv_date, tv_WDT, tv_MS, tv_D;
    private ArrayList<Spinneritems> list = new ArrayList<>();
    private ArrayList<Spinneritems> list1 = new ArrayList<>();
    private ArrayList<Monthdayreportitems> monthdayrepoer_list1 = new ArrayList<>();
    ExpandableHeightListView list_monthday_report;
    IdleReportFragment.MonthdayreportAdapter monthdayreportAdapter;
    Button bt_back;
    ProgressDialog progressDialog;
    String SUsername, SOrgnName, SOrgid, SBranchid, S_PassWord, IMEI, SUserid, Sstop1, Srun1, Sidle1, Suf1, Sstop, Srun, Sidle, Suf;
    TextView tv_p_t2, tv_p_t3, tv_p_t4, tv_p_t5, tv_p_t6, tv_p_t7, tv_p_drtn, tv_p_stop, tv_p_idle, tv_p_wrk, tv_p_ac_dtn;
    String default_From_date_report, default_To_date_report;
    String default_From_date_report1, default_To_date_report1;
    String p_TotalAcTime, p_Header, p_TotalDistance, p_TotalIdle, p_TotalStop, p_TotalWork;
    String p_Work, p_stop, p_Idle, p_AcTime, p_Distance, p_GeofenceOut, p_a, p_b, p_c, p_d, p_e, p_f, p_g, p_h;

    TextView tv_vehiclename, tv_preport_time;
    View v;
    String FromDate, ToDate;
    EditText et_fromdate, et_todate;
    Button bt_submit;
    DatePickerReports custom, custom1;
    LinearLayout ll_data;
    String default_From_date_new, default_From_date, default_To_date,
            Start_time, Stop_time, default_mode, S_speed;
    TextView tv_back;
    LinearLayout ll_performance;
    ImageView menu_ic;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.after_activity_performance_report, container, false);
        tv_p_t2 = (TextView) v.findViewById(R.id.tv_p_t2);
        tv_p_t3 = (TextView) v.findViewById(R.id.tv_p_t3);
        tv_p_t4 = (TextView) v.findViewById(R.id.tv_p_t4);
        tv_p_t5 = (TextView) v.findViewById(R.id.tv_p_t5);
        tv_p_t6 = (TextView) v.findViewById(R.id.tv_p_t6);
        tv_p_t7 = (TextView) v.findViewById(R.id.tv_p_t7);
        tv_p_drtn = (TextView) v.findViewById(R.id.tv_p_drtn);
        tv_p_wrk = (TextView) v.findViewById(R.id.tv_p_wrk);
        tv_p_stop = (TextView) v.findViewById(R.id.tv_p_stop);
        tv_p_idle = (TextView) v.findViewById(R.id.tv_p_idle);
        tv_p_ac_dtn = (TextView) v.findViewById(R.id.tv_p_ac_dtn);
        tv_preport_time = (TextView) v.findViewById(R.id.tv_preport_time);
        tv_vehiclename = (TextView) v.findViewById(R.id.tv_vehiclename);
        ll_performance = v.findViewById(R.id.ll_performance);
        et_fromdate = v.findViewById(R.id.et_fromdate);
        et_todate = v.findViewById(R.id.et_todate);
        bt_submit = v.findViewById(R.id.bt_submit);
        menu_ic = v.findViewById(R.id.menu_ic);
        ll_data = v.findViewById(R.id.ll_data);
        menu_ic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_data.setVisibility(View.VISIBLE);
                ll_performance.setVisibility(View.GONE);
            }
        });
        setdate();
        fromdata();
        final Bundle b = getArguments();
        if (b != null) {
            S_VehicleID = b.getString("Vehicle_ID");
            S_month = b.getString("Vehicle_Name");

            S_VehicleName = b.getString("Vehicle_Name");
            tv_vehiclename.setText("" + S_VehicleName);


            String Fromdate = default_From_date_report;

            String Todate = default_To_date_report;


            try {
                DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
                DateFormat outputFormat = new SimpleDateFormat("yyyy-MMM-dd HH:mm", Locale.UK);
                Date oneWayTripDate = inputFormat.parse(Fromdate);
                FromDate = outputFormat.format(oneWayTripDate);// parse input
            } catch (Exception ex) {
                System.out.println("Date error");

            }


            Log.d("VEHICLENAME", S_VehicleName);
        } else {

        }
        tv_back = v.findViewById(R.id.tv_back);
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MonthDayReport_Fragment fragment = new MonthDayReport_Fragment();
                opennfrag(fragment);

            }
        });
        // Report_Performance(S_VehicleID);
        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String MonthYear = et_fromdate.getText().toString();
                    String MonthYear1 = et_todate.getText().toString();
                    Date date1 = sdf.parse(MonthYear);
                    Date date3 = sdf.parse(MonthYear1);

                    System.out.println("Date1" + sdf.format(date1));
                    System.out.println("Date2" + sdf.format(date3));

                    System.out.println();

                    long diff = date3.getTime() - date1.getTime();
                    System.out.println("Days: " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));

                    if (date1.equals(date3)) {
                        Report_Performance(S_VehicleID);

                        System.out.println("Succes");
                    } else {
                        Toast.makeText(getContext(), "Please select From Date should equal to To Date", Toast.LENGTH_SHORT).show();


                        System.out.println("Date1 is after Date2");

                    }
//                            if ((date3.before(date2)) | (date3.equals(date2))) {
//
//                                if (!(date3.before(date1))) {


//                                } else {
//
//                                    Toast.makeText(getContext(), "Please Select Proper From And To Dates", Toast.LENGTH_SHORT).show();
//                                }
//                            } else {
//                                System.out.println("Date1 is after Date2");
//                                Toast.makeText(getContext(), "Please Select Proper From And To Date", Toast.LENGTH_SHORT).show();
//                            }
//                        } else {
//                            System.out.println("Date1 is after Date2");
//                            Toast.makeText(getContext(), "Please Select Proper From And To Date", Toast.LENGTH_SHORT).show();
//                        }


                } catch (
                        Exception e) {
                    e.printStackTrace();
                }


            }
        });
        return v;

    }

    private void fromdata() {
        // Toast.makeText(getContext(),"hello",Toast.LENGTH_LONG).show();

        // tv.setGravity(0);


        et_fromdate.setText(default_From_date);
        et_todate.setText(default_To_date);

        Start_time = et_fromdate.getText().toString();
        Stop_time = et_todate.getText().toString();

        Log.e("Start_time", Start_time);
        Log.e("Stop_time", Stop_time);


        custom = new DatePickerReports((Activity) getContext(),
                new DatePickerReports.ICustomDateTimeListener() {
                    @SuppressLint("WrongConstant")
                    @Override
                    public void onSet(Dialog dialog, java.util.Calendar calendarSelected,
                                      Date dateSelected, int year, String monthFullName,
                                      String monthShortName, int monthNumber, int date,
                                      String weekDayFullName, String weekDayShortName,
                                      int hour24, int hour12, int min, int sec,
                                      String AM_PM) {
//
//                        Toast.makeText(getContext(), "" + year
//                                + "-" + (monthNumber + 1) + "-" + calendarSelected.get(Calendar.DAY_OF_MONTH)
//                                + " " + hour24 + ":" + min
//                                + ":" + sec, Toast.LENGTH_LONG).show();
                        //                        ((TextInputEditText) findViewById(R.id.edtEventDateTime))
                        et_fromdate.setText("");
                        et_fromdate.setText(year
                                + "-" + (monthNumber + 1) + "-" + calendarSelected.get(Calendar.DAY_OF_MONTH));
                    }

                    @Override
                    public void onCancel() {
                    }
                });

        custom.set24HourFormat(true);
        /**
         * Pass Directly current data and time to show when it pop up
         */
        custom.setDate(java.util.Calendar.getInstance());


        custom1 = new DatePickerReports((Activity) getContext(),
                new DatePickerReports.ICustomDateTimeListener() {
                    @SuppressLint("WrongConstant")
                    @Override
                    public void onSet(Dialog dialog, java.util.Calendar calendarSelected,
                                      Date dateSelected, int year, String monthFullName,
                                      String monthShortName, int monthNumber, int date,
                                      String weekDayFullName, String weekDayShortName,
                                      int hour24, int hour12, int min, int sec,
                                      String AM_PM) {
//
//                        Toast.makeText(getContext(), "" + year
//                                + "-" + (monthNumber + 1) + "-" + calendarSelected.get(Calendar.DAY_OF_MONTH)
//                                + " " + hour24 + ":" + min
//                                + ":" + sec, Toast.LENGTH_LONG).show();
                        //                        ((TextInputEditText) findViewById(R.id.edtEventDateTime))
                        et_todate.setText("");
                        et_todate.setText(year
                                + "-" + (monthNumber + 1) + "-" + calendarSelected.get(Calendar.DAY_OF_MONTH));
                    }

                    @Override
                    public void onCancel() {
                    }
                });

        custom1.set24HourFormat(true);
        /**
         * Pass Directly current data and time to show when it pop up
         */
        custom1.setDate(java.util.Calendar.getInstance());
        et_fromdate.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        custom.showDialog();
                    }
                });

        et_todate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                custom1.showDialog();
            }
        });


        et_fromdate.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        et_todate.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);


    }


    private void Report_Performance(String VehId) {

        SharedPreferences bb = getContext().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        SOrgid = bb.getString("Orgid", "");

        default_From_date_new = et_fromdate.getText().toString();
        default_From_date = et_todate.getText().toString();


        try {
            JSONObject jsonBody = new JSONObject();

            jsonBody.put("Mnth", "Monthly");
            jsonBody.put("StartDate", default_From_date_new);
            jsonBody.put("EndDate", default_From_date);
            jsonBody.put("VehicleIds", VehId);
            jsonBody.put("OrgId", SOrgid);

            final String mRequestBody = jsonBody.toString();


            Log.e("VOLLEY", "request PerformanceReport" + mRequestBody);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url_PerformanceReport, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("VOLLEY", "" + "Response PerformanceReport" + response);
                    try {

                        JSONObject object = new JSONObject(response);
                        String code = object.optString("Code").toString();
                        message = object.optString("Message").toString();
                        p_TotalWork = object.optString("TotalWork").toString();
                        p_TotalStop = object.optString("TotalStop").toString();
                        p_TotalIdle = object.optString("TotalIdle").toString();
                        p_TotalAcTime = object.optString("TotalAcTime").toString();
                        p_TotalDistance = object.optString("TotalDistance").toString();

                        p_Header = object.optString("Header").toString();

                        tv_preport_time.setText("" + p_Header);
                        if (code.equalsIgnoreCase("0")) {
                            ll_data.setVisibility(View.GONE);
                            ll_performance.setVisibility(View.VISIBLE);
                            JSONArray jArray = object.getJSONArray("Data");
                            int number = jArray.length();
                            String num = Integer.toString(number);
                            if (number == 0) {
                            } else {
                                for (int i = 0; i < number; i++) {
                                    JSONObject json_data = jArray.getJSONObject(i);
                                    p_a = json_data.getString("A").toString();
                                    p_b = json_data.getString("B").toString();
                                    p_c = json_data.getString("C").toString();
                                    p_d = json_data.getString("D").toString();
                                    p_e = json_data.getString("E").toString();
                                    p_f = json_data.getString("F").toString();
                                    p_g = json_data.getString("G").toString();
                                    p_h = json_data.getString("H").toString();
                                    p_Work = json_data.getString("Work").toString();
                                    p_stop = json_data.getString("Stop").toString();
                                    p_Idle = json_data.getString("Idle").toString();
                                    p_AcTime = json_data.getString("AcTime").toString();
                                    p_Distance = json_data.getString("Distance").toString();
                                    p_GeofenceOut = json_data.getString("GeofenceOut").toString();


                                    tv_p_t2.setText("" + p_b);
                                    tv_p_t3.setText("" + p_c);
                                    tv_p_t4.setText("" + p_d);
                                    tv_p_t5.setText("" + p_e);
                                    tv_p_t6.setText("" + p_f);
                                    tv_p_t7.setText("" + p_g);

                                    tv_p_drtn.setText("" + p_h);
                                    tv_p_stop.setText("" + p_stop);
                                    tv_p_idle.setText("" + p_Idle);
                                    tv_p_wrk.setText("" + p_Work);
                                    tv_p_ac_dtn.setText("" + p_AcTime);


                                }
                            }

                        } else {
                            //  ll_performance.setVisibility(View.GONE);
                            tv_preport_time.setText("");
                            ll_performance.setVisibility(View.GONE);

                            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                            alertDialog.setTitle("Response");
                            //  alertDialog.setIcon(R.drawable.alert);
                            alertDialog.setMessage("" + "No Data Found");
                            alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Write your code here to invoke YES event
                                    dialog.cancel();
                                    ll_performance.setVisibility(View.GONE);
                                    tv_preport_time.setText("");

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
                    ll_performance.setVisibility(View.VISIBLE);
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
//                @Override
//                protected Response<String> parseNetworkResponse(NetworkResponse response) {
//                    String responseString = "";
//                    if (response != null) {
//
//                        responseString = String.valueOf(response);
//
//                        // can get more details such as response.headers
//                    }
//
//                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
//
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

    private void setdate() {

        SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MM-dd");
        Date from_date = new Date();
        long to_date = from_date.getTime();

        Log.e("TEST", from_date.getTime() + " - " + to_date);
        default_From_date = simpledateformat.format(to_date);
        default_To_date = simpledateformat.format(from_date);
        default_mode = "Both";
        Log.e("from", default_From_date);
        Log.e("to", default_To_date);
    }

}