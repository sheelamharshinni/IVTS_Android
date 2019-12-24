package com.tecdatum.Tracking.School.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.tecdatum.Tracking.School.R;
import com.tecdatum.Tracking.School.newConstants.Url_new;
import com.tecdatum.Tracking.School.newhelpers.DatePickerReports;
import com.tecdatum.Tracking.School.newhelpers.QvVehiclesList;
import com.tecdatum.Tracking.School.newhelpers.Samplemyclass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;

public class MonthDayReport_Fragment extends Fragment {
    View v;

    DatePickerReports custom, custom1;
    String vehicle, Select, default_From_date_new, default_From_date, default_To_date,
            Start_time, Stop_time, default_mode, S_speed;
    Spinner sp_vehiclelist;
    public static final String url_Vehiclelist = Url_new.VehicleList;
    String S_month, S_VehicleName, S_VehicleID;
    Button rb_performance, rb_Distance, rb_Exception, rb_Monthly, Submit;
    String id, data;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    public static final String url_month_year = Url_new.Master;
    String SOrgnName, SUsername, SOrgid, SBranchid, LevelId, SUserid;
    String S_Vehicleid;
    ArrayList<Samplemyclass> arraylist_vehiclelist = new ArrayList<Samplemyclass>();
    private ArrayList<Samplemyclass> list1 = new ArrayList<>();

    Dialog C_dialog;


    ArrayList<QvVehiclesList> arraylist = new ArrayList<QvVehiclesList>();
    Spinner sp_month_alert;
    Button bt_Speed, bt_Idle, bt_Stopage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for getContext() fragment
        v = inflater.inflate(R.layout.after_activity_month_day_report, container, false);

        sp_vehiclelist = (Spinner) v.findViewById(R.id.sp_vehiclelist);

        bt_Speed = v.findViewById(R.id.bt_Speed);
        bt_Idle = v.findViewById(R.id.bt_Idle);
        bt_Stopage = v.findViewById(R.id.bt_Stopage);

        Submit = (Button) v.findViewById(R.id.bt_submit);
        rb_Monthly = (Button) v.findViewById(R.id.rb_Monthly);
        rb_performance = (Button) v.findViewById(R.id.rb_performance);
        rb_Distance = (Button) v.findViewById(R.id.rb_Distance);

        SharedPreferences bb = getContext().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        SOrgid = bb.getString("Orgid", "");
        SOrgnName = bb.getString("Orgname", "");
        SBranchid = bb.getString("Branchid", "");
        SUserid = bb.getString("Userid", "");
        SUsername = bb.getString("Username", "");


        final Bundle b = getArguments();
        if (b != null) {
            S_Vehicleid = b.getString("Vehicle_ID");
        } else {

        }

        Button_Onclicks();
        setdate1();
        getvehiclelist();

        hideSoftKeyboard();
        return v;

    }

    public void hideSoftKeyboard() {
        //   getContext().  getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    public void Button_Onclicks() {
        rb_performance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                boolean error = false;

                if (sp_vehiclelist != null && sp_vehiclelist.getSelectedItem() != null) {
                    if ((sp_vehiclelist.getSelectedItem().toString().trim() == "Please Select Vehicle")) {

                        error = true;
                        Toast.makeText(getContext(), "Please Select Vehicle", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    error = true;
                    //Toast.makeText(getContext(), "Please Select Vehicle", Toast.LENGTH_SHORT).show();
                }

                if (!error) {
                    MonthReportFragment fragment = new MonthReportFragment();
                    Bundle args = new Bundle();
                    args.putString("Vehicle_ID", S_VehicleID);
                    args.putString("Vehicle", S_month);
                    args.putString("Vehicle_Name", S_VehicleName);

                    fragment.setArguments(args);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.tabFrameLayout, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                } else {
                    Toast.makeText(getContext(), "Please Select Vehicle", Toast.LENGTH_SHORT).show();
                }

            }
        });
        rb_Monthly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean error = false;

                if (sp_vehiclelist != null && sp_vehiclelist.getSelectedItem() != null) {
                    if ((sp_vehiclelist.getSelectedItem().toString().trim() == "Please Select Vehicle")) {

                        error = true;
                        Toast.makeText(getContext(), "Please Select Vehicle", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    error = true;
                    //Toast.makeText(getContext(), "Please Select Vehicle", Toast.LENGTH_SHORT).show();
                }

                if (!error) {
                    Select = "3";
                    Report_DetailFragment fragment = new Report_DetailFragment();
                    Bundle args = new Bundle();
                    args.putString("Vehicle_ID", S_VehicleID);
                    args.putString("Vehicle", S_month);
                    args.putString("Vehicle_Name", S_VehicleName);
                    fragment.setArguments(args);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.tabFrameLayout, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                } else {
                    Toast.makeText(getContext(), "Please Select Vehicle", Toast.LENGTH_SHORT).show();
                }


            }
        });




        bt_Speed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean error = false;

                if (sp_vehiclelist != null && sp_vehiclelist.getSelectedItem() != null) {
                    if ((sp_vehiclelist.getSelectedItem().toString().trim() == "Please Select Vehicle")) {

                        error = true;
                        Toast.makeText(getContext(), "Please Select Vehicle", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    error = true;
                    //Toast.makeText(getContext(), "Please Select Vehicle", Toast.LENGTH_SHORT).show();
                }

                if (!error) {
                    Select = "4";
                    SpeedReportFragment fragment = new SpeedReportFragment();
                    Bundle args = new Bundle();
                    args.putString("Vehicle_ID", S_VehicleID);
                    args.putString("Vehicle", S_month);
                    args.putString("Vehicle_Name", S_VehicleName);
                    fragment.setArguments(args);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.tabFrameLayout, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                } else {
                    Toast.makeText(getContext(), "Please Select Vehicle", Toast.LENGTH_SHORT).show();
                }

            }
        });


        bt_Idle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean error = false;

                if (sp_vehiclelist != null && sp_vehiclelist.getSelectedItem() != null) {
                    if ((sp_vehiclelist.getSelectedItem().toString().trim() == "Please Select Vehicle")) {

                        error = true;
                        Toast.makeText(getContext(), "Please Select Vehicle", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    error = true;
                    //Toast.makeText(getContext(), "Please Select Vehicle", Toast.LENGTH_SHORT).show();
                }

                if (!error) {
                    setdate();
                    Select = "5";
                    IdleReportFragment fragment = new IdleReportFragment();
                    Bundle args = new Bundle();
                    args.putString("Vehicle_ID", S_VehicleID);
                    args.putString("Vehicle", S_month);
                    args.putString("Vehicle_Name", S_VehicleName);
                    args.putString("Speed", S_speed);
                    Log.d("VEHICLENAME", S_VehicleName);

                    fragment.setArguments(args);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.tabFrameLayout, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                } else {
                    Toast.makeText(getContext(), "Please Select Vehicle", Toast.LENGTH_SHORT).show();
                }

            }
        });

        bt_Stopage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean error = false;

                if (sp_vehiclelist != null && sp_vehiclelist.getSelectedItem() != null) {
                    if ((sp_vehiclelist.getSelectedItem().toString().trim() == "Please Select Vehicle")) {

                        error = true;
                        Toast.makeText(getContext(), "Please Select Vehicle", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    error = true;
                    //Toast.makeText(getContext(), "Please Select Vehicle", Toast.LENGTH_SHORT).show();
                }

                if (!error) {
                    ;

                    StoppageReportFragment fragment = new StoppageReportFragment();
                    Bundle args = new Bundle();
                    args.putString("Vehicle_ID", S_VehicleID);
                    args.putString("Vehicle", S_month);
                    args.putString("Vehicle_Name", S_VehicleName);

                    fragment.setArguments(args);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.tabFrameLayout, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                } else {
                    Toast.makeText(getContext(), "Please Select Vehicle", Toast.LENGTH_SHORT).show();
                }

            }
        });


        rb_Distance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean error = false;

                if (sp_vehiclelist != null && sp_vehiclelist.getSelectedItem() != null) {
                    if ((sp_vehiclelist.getSelectedItem().toString().trim() == "Please Select Vehicle")) {

                        error = true;
                        Toast.makeText(getContext(), "Please Select Vehicle", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    error = true;
                    //Toast.makeText(getContext(), "Please Select Vehicle", Toast.LENGTH_SHORT).show();
                }

                if (!error) {
                    Select = "1";
                    PerformanceReportFragment fragment = new PerformanceReportFragment();
                    Bundle args = new Bundle();
                    args.putString("Vehicle_ID", S_VehicleID);
                    args.putString("Vehicle", S_month);
                    args.putString("Vehicle_Name", S_VehicleName);


                    fragment.setArguments(args);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.tabFrameLayout, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                } else {
                    Toast.makeText(getContext(), "Please Select Vehicle", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }


    private void VehicleList(ArrayList<Samplemyclass> str1) {

        ArrayAdapter<Samplemyclass> adapter = new ArrayAdapter<Samplemyclass>(getContext(),
                R.layout.spinner_item, str1);

        sp_vehiclelist.setAdapter(adapter);
//        // textView.setSelection(0);
//        sp_type.setTitle("Select Nationality Type");
//        // textView.setTitle();
//        sp_type.setPositiveButton("OK");
//        sp_type.setOnSearchTextChangedListener(new SearchableListDialog.OnSearchTextChanged() {
//            @Override
//            public void onSearchTextChanged(String strText) {
//                // Toast.makeText(getActivity(), strText + " selected", Toast.LENGTH_SHORT).show();
//            }
//        });

        for (int i = 0; i < arraylist_vehiclelist.size(); i++) {
            if (arraylist_vehiclelist.get(i).getId().equals(S_Vehicleid)) {
                sp_vehiclelist.setSelection(i);
            } else {

            }
        }
        sp_vehiclelist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

//                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                //  textView.setSelection(0);
                try {
                    ((TextView) adapterView.getChildAt(0)).setTextColor(Color.parseColor("#000000"));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //  Samplemyclass list = (Samplemyclass) sp_VehicleType.getSelectedItem();
                Samplemyclass country = (Samplemyclass) adapterView.getSelectedItem();

                S_VehicleID = country.getId();
                S_VehicleName = country.getName();


                Log.e("VOLLEY", "VehicleType_id" + S_VehicleID);

            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }


    private void getvehiclelist() {
        final RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        final JSONObject jsonObject = new JSONObject();


        try {

            SharedPreferences bb = getContext().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            SOrgid = bb.getString("Orgid", "");
            LevelId = bb.getString("LevelId", "");
            String HierarchyId = bb.getString("HierarchyId", "");


            JSONObject jsonBody = new JSONObject();
            jsonBody.put("OrgId", SOrgid);
            jsonBody.put("LevelId", LevelId);

            jsonBody.put("HierarchyId", HierarchyId);

            Log.d("Request", String.valueOf(jsonObject));
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url_Vehiclelist, jsonBody, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("Response", String.valueOf(response));
                    JSONObject object = null;
                    arraylist_vehiclelist.clear();
                    try {
                        object = new JSONObject(String.valueOf(response));

                        String code = response.optString("Code", "");
                        String message = response.optString("Message", "");


                        if (code.equalsIgnoreCase("0")) {
                            JSONArray jsonArray = null;
                            try {
                                Samplemyclass wp1 = new Samplemyclass("0", "Please Select Vehicle");
                                arraylist_vehiclelist.add(wp1);
                                jsonArray = object.getJSONArray("Vehicles");
                                int number = jsonArray.length();

                                for (int i = 0; i < number; i++) {
                                    try {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                                        Log.d("Request", String.valueOf(jsonObject));
                                        id = jsonObject.getString("VehicleId".toString());
                                        data = jsonObject.getString("VehicleName".toString());
                                        Samplemyclass wp = new Samplemyclass(id, data);
                                        arraylist_vehiclelist.add(wp);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    if (arraylist_vehiclelist.size() > 0) {
                                        VehicleList(arraylist_vehiclelist);
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
            }
            );


            requestQueue.add(jsonObjectRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void setdate1() {

        SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MM-dd");
        Date from_date = new Date();
        //   long to_date = from_date.get() - 720 * 60 * 1000;
        //  Log.e("TEST", from_date.getTime() + " - " + to_date);
        default_To_date = simpledateformat.format(from_date);
        // default_To_date = simpledateformat.format(to_date);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date myDate = dateFormat.parse(default_To_date);
            java.util.Calendar cal1 = java.util.Calendar.getInstance();
            cal1.setTime(myDate);
            cal1.add(java.util.Calendar.DAY_OF_YEAR, -1);
            Date previousDate = cal1.getTime();
            default_From_date = simpledateformat.format(previousDate);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Date myDate = dateFormat.parse(default_From_date);
            java.util.Calendar cal1 = java.util.Calendar.getInstance();
            cal1.setTime(myDate);
            cal1.add(java.util.Calendar.DAY_OF_YEAR, -1);
            Date previousDate = cal1.getTime();
            default_From_date_new = simpledateformat.format(previousDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("Date from", default_To_date);
        Log.e("Date from1", default_From_date);
        Log.e("Date to", default_From_date_new);
    }

    private void fromdata() {
        // Toast.makeText(getContext(),"hello",Toast.LENGTH_LONG).show();
        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_datetime, null);
        alertDialog.setView(dialogView);
        // View checkBoxView = View.inflate(getContext(), R.layout.dialog_datetime, null);
        // alertDialog.setContentView(R.layout.dialog_datetime);
//                 fm = (EditText) checkBoxView.findViewById(R.id.editText);
//                 toT = (EditText) checkBoxView.findViewById(R.id.editText2);

//          mode=(Spinner)alertDialog.findViewById(R.id.spinner);
//                 ArrayAdapter<Samplemyclass> adapter = new ArrayAdapter<Samplemyclass>
//                         (Tset.getContext(), android.R.layout.simple_spinner_dropdown_item, cl0);
//                 mode.setAdapter(adapter);
//                 mode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                    @Override
//                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                        ((TextView) parent.getChildAt(0)).setTextColor(Color.parseColor("#1a2164"));
//                        int pos = parent.getSelectedItemPosition();
//                        if (pos != 0) {
//                            ((TextView) parent.getChildAt(0)).setTextColor(Color.parseColor("#192060"));
//                           String selectedName = parent.getItemAtPosition(position).toString();
//                            Toast.makeText(getBaseContext(), "You are "+selectedName, Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                    @Override
//                    public void onNothingSelected(AdapterView<?> parent) {
//                    }
//                });
//                 alertDialog.setMessage("Values");
//                 alertDialog.setView(checkBoxView);

        final TextView tv = new TextView(getContext());
        final EditText fm = new EditText(getContext());
        final EditText toT = new EditText(getContext());
        tv.setText("Select Date");
        // tv.setGravity(0);

        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(19f);
        tv.setTextColor(Color.BLACK);

        fm.setText(default_From_date);
        toT.setText(default_From_date);

        Start_time = fm.getText().toString();
        Stop_time = toT.getText().toString();

        Log.e("Start_time", Start_time);
        Log.e("Stop_time", Stop_time);


        custom = new DatePickerReports((Activity) getContext(),
                new DatePickerReports.ICustomDateTimeListener() {
                    @SuppressLint({"SetTextI18n", "WrongConstant"})
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
                        fm.setText("");
                        fm.setText(year
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
                        toT.setText("");
                        toT.setText(year
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
        fm.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        custom.showDialog();
                    }
                });

        toT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                custom1.showDialog();
            }
        });
        fm.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        toT.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        LinearLayout ll = new LinearLayout(getContext());
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.addView(tv);
        ll.addView(fm);
        ll.addView(toT);
        // ll.addView(spnr);
//        ll.addView(spnr1);
        alertDialog.setView(ll);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Submit",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        default_From_date_new = fm.getText().toString();
                        default_From_date = toT.getText().toString();

                        Log.e("changed_from", default_From_date);
                        Log.e("changed_to", default_From_date_new);

                        //  makeJsonObjReq();

                        if (Select.equalsIgnoreCase("1")) {


//                            Intent intent = new Intent(getContext(), PerformanceReport.class);
//
//
//                            intent.putExtra("Vehicle_ID", S_VehicleID);
//                            intent.putExtra("Vehicle_Name", S_month);
//                            intent.putExtra("Vehicle", S_VehicleName);
//                            intent.putExtra("todate", default_From_date_new);
//                            intent.putExtra("fromdate", default_From_date);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            startActivity(intent);
                        } else {


                        }
                        dialog.dismiss();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.setCancelable(false);
        alertDialog.show();

    }

    private void fromdata1() {
        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_datetime_new, null);
        alertDialog.setView(dialogView);


        final TextView tv = new TextView(getContext());
        final EditText fm = new EditText(getContext());
        final EditText toT = new EditText(getContext());

        final EditText speed = new EditText(getContext());
        tv.setText("Select Date");

        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(19f);
        tv.setTextColor(Color.BLACK);

        fm.setText(default_From_date);
        toT.setText(default_From_date);
        speed.setText("40");
        Start_time = fm.getText().toString();
        Stop_time = toT.getText().toString();
        S_speed = speed.getText().toString();
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
                        fm.setText("");
                        fm.setText(calendarSelected.get(Calendar.DAY_OF_MONTH)
                                + "-" + (monthShortName) + "-" + year + " " + "" + hour24 + ":" + min + ":" + sec);
                    }

                    @Override
                    public void onCancel() {
                    }
                });

        custom.set24HourFormat(true);

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

                        toT.setText("");
                        toT.setText(calendarSelected.get(Calendar.DAY_OF_MONTH)
                                + "-" + (monthShortName) + "-" + year + " " + "" + hour24 + ":" + min + ":" + sec);
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
        fm.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        custom.showDialog();
                    }
                });

        toT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                custom1.showDialog();
            }
        });
        fm.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        toT.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        LinearLayout ll = new LinearLayout(getContext());
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.addView(tv);
        ll.addView(speed);
        ll.addView(fm);
        ll.addView(toT);
        // ll.addView(spnr);
//        ll.addView(spnr1);
        alertDialog.setView(ll);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Submit",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        default_From_date_new = fm.getText().toString();
                        default_From_date = toT.getText().toString();

                        Log.e("changed_from", default_From_date);
                        Log.e("changed_to", default_From_date_new);


                        if (Select.equalsIgnoreCase("4")) {

                        } else {
                        }
                        dialog.dismiss();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.setCancelable(false);
        alertDialog.show();

    }

    private void fromdata2() {
        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_datetime_new, null);
        alertDialog.setView(dialogView);
        final TextView tv = new TextView(getContext());
        final EditText fm = new EditText(getContext());
        final EditText toT = new EditText(getContext());

        final EditText speed = new EditText(getContext());
        tv.setText("Select Date");
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(19f);
        tv.setTextColor(Color.BLACK);

        fm.setText(default_From_date);
        toT.setText(default_From_date);
        speed.setText("3");
        Start_time = fm.getText().toString();
        Stop_time = toT.getText().toString();
        S_speed = speed.getText().toString();
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
                        fm.setText("");


                        fm.setText(calendarSelected.get(Calendar.DAY_OF_MONTH)
                                + "-" + (monthShortName) + "-" + year + " " + "" + hour24 + ":" + min + ":" + sec);
                    }

                    @Override
                    public void onCancel() {
                    }
                });

        custom.set24HourFormat(true);
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
                        toT.setText("");
                        toT.setText(calendarSelected.get(Calendar.DAY_OF_MONTH)
                                + "-" + (monthShortName) + "-" + year + " " + "" + hour24 + ":" + min + ":" + sec);
                    }

                    @Override
                    public void onCancel() {
                    }
                });

        custom1.set24HourFormat(true);
        custom1.setDate(java.util.Calendar.getInstance());
        fm.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        custom.showDialog();
                    }
                });

        toT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                custom1.showDialog();
            }
        });
        fm.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        toT.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        LinearLayout ll = new LinearLayout(getContext());
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.addView(tv);
        ll.addView(speed);
        ll.addView(fm);
        ll.addView(toT);
        alertDialog.setView(ll);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Submit",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        default_From_date_new = fm.getText().toString();
                        default_From_date = toT.getText().toString();

                        Log.e("changed_from", default_From_date);
                        Log.e("changed_to", default_From_date_new);
                        if (Select.equalsIgnoreCase("5")) {


                            IdleReportFragment fragment = new IdleReportFragment();
                            Bundle args = new Bundle();
                            args.putString("Vehicle_ID", S_VehicleID);
                            args.putString("Vehicle", S_month);
                            args.putString("Vehicle_Name", S_VehicleName);
                            args.putString("todate", default_From_date_new);
                            args.putString("fromdate", default_From_date);
                            args.putString("Speed", S_speed);
                            Log.d("VEHICLENAME", S_VehicleName);

                            fragment.setArguments(args);
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.tabFrameLayout, fragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        } else {


                        }
                        dialog.dismiss();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.setCancelable(false);
        alertDialog.show();

    }

    private void fromdata3() {
        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_datetime_new, null);
        alertDialog.setView(dialogView);
        final TextView tv = new TextView(getContext());
        final EditText fm = new EditText(getContext());
        final EditText toT = new EditText(getContext());

        final EditText speed = new EditText(getContext());
        tv.setText("Select Date");
        // tv.setGravity(0);

        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(19f);
        tv.setTextColor(Color.BLACK);

        fm.setText(default_From_date);
        toT.setText(default_From_date);
        speed.setText("50");
        Start_time = fm.getText().toString();
        Stop_time = toT.getText().toString();
        S_speed = speed.getText().toString();
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
                        fm.setText("");

                        fm.setText(calendarSelected.get(Calendar.DAY_OF_MONTH)
                                + "-" + (monthShortName) + "-" + year + " " + "" + hour24 + ":" + min + ":" + sec);
                    }

                    @Override
                    public void onCancel() {
                    }
                });

        custom.set24HourFormat(true);

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
                        toT.setText("");
                        toT.setText(calendarSelected.get(Calendar.DAY_OF_MONTH)
                                + "-" + (monthShortName) + "-" + year + " " + "" + hour24 + ":" + min + ":" + sec);
                    }

                    @Override
                    public void onCancel() {
                    }
                });

        custom1.set24HourFormat(true);

        custom1.setDate(java.util.Calendar.getInstance());
        fm.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        custom.showDialog();
                    }
                });

        toT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                custom1.showDialog();
            }
        });
        fm.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        toT.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        LinearLayout ll = new LinearLayout(getContext());
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.addView(tv);
        ll.addView(speed);
        ll.addView(fm);
        ll.addView(toT);
        alertDialog.setView(ll);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Submit",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        default_From_date_new = fm.getText().toString();
                        default_From_date = toT.getText().toString();

                        Log.e("changed_from", default_From_date);
                        Log.e("changed_to", default_From_date_new);
                        if (Select.equalsIgnoreCase("6")) {


                            StoppageReportFragment fragment = new StoppageReportFragment();
                            Bundle args = new Bundle();
                            args.putString("Vehicle_ID", S_VehicleID);
                            args.putString("Vehicle", S_month);
                            args.putString("Vehicle_Name", S_VehicleName);
                            args.putString("todate", default_From_date_new);
                            args.putString("fromdate", default_From_date);
                            args.putString("Speed", S_speed);
                            Log.d("VEHICLENAME", S_VehicleName);

                            fragment.setArguments(args);
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.tabFrameLayout, fragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        } else {


                        }
                        dialog.dismiss();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.setCancelable(false);
        alertDialog.show();

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
                                        id = jsonObject.getString("Id".toString());
                                        data = jsonObject.getString("Data".toString());
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

    private void alertDialogue_Conform() {
        C_dialog = new Dialog(getContext(), R.style.MyAlertDialogStyle);
        C_dialog.setContentView(R.layout.montly_report_alert);
        C_dialog.show();


        sp_month_alert = (Spinner) C_dialog.findViewById(R.id.sp_month_alert);
        Button bt_submit_alert = (Button) C_dialog.findViewById(R.id.bt_submit_alert);
        Button btn_cancel_alert = (Button) C_dialog.findViewById(R.id.btn_cancel_alert);
        getmonthyear();
        bt_submit_alert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean error = false;

                if (sp_month_alert != null && sp_month_alert.getSelectedItem() != null) {
                    if ((sp_month_alert.getSelectedItem().toString().trim() == "Select Month")) {

                        error = true;
                        Toast.makeText(getContext(), "Select Month ", Toast.LENGTH_SHORT).show();
                    }
                }

                if (!error) {
                    if (Select.equalsIgnoreCase("3")) {

//                        Intent intent = new Intent(getContext(), MonthReport.class);
//                        intent.putExtra("Vehicle_ID", S_VehicleID);
//                        intent.putExtra("Vehicle_Name", S_month);
//                        intent.putExtra("Vehicle", S_VehicleName);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(intent);


                        MonthReportFragment fragment = new MonthReportFragment();
                        Bundle args = new Bundle();
                        args.putString("Vehicle_ID", S_VehicleID);
                        args.putString("Vehicle", S_month);
                        args.putString("Vehicle_Name", S_VehicleName);
                        args.putString("todate", default_From_date_new);
                        args.putString("fromdate", default_From_date);
                        Log.d("VEHICLENAME", S_VehicleName);

                        fragment.setArguments(args);
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.tabFrameLayout, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        C_dialog.dismiss();
                    }

                    if (Select.equalsIgnoreCase("2")) {


                        Report_DetailFragment fragment = new Report_DetailFragment();
                        Bundle args = new Bundle();
                        args.putString("Vehicle_ID", S_VehicleID);
                        args.putString("Vehicle", S_month);
                        args.putString("Vehicle_Name", S_VehicleName);
                        args.putString("todate", default_From_date_new);
                        args.putString("fromdate", default_From_date);
                        Log.d("VEHICLENAME", S_VehicleName);

                        fragment.setArguments(args);
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.tabFrameLayout, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        C_dialog.dismiss();
                    }
                } else {
                    Toast.makeText(getContext(), "Select Month ", Toast.LENGTH_SHORT).show();
                }


            }
        });

        btn_cancel_alert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                C_dialog.dismiss();
            }
        });
    }

    private void setdate() {

        SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date from_date = new Date();
        long to_date = from_date.getTime() - 1440 * 60 * 1000;
        Log.e("TEST", from_date.getTime() + " - " + to_date);
        default_From_date = simpledateformat.format(to_date);
        default_To_date = simpledateformat.format(from_date);
        default_mode = "Both";
        Log.e("from", default_From_date);
        Log.e("to", default_To_date);
    }
}
