package com.tecdatum.Tracking.School.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.paolorotolo.expandableheightlistview.ExpandableHeightListView;
import com.tecdatum.Tracking.School.R;
import com.tecdatum.Tracking.School.newConstants.Url_new;
import com.tecdatum.Tracking.School.newhelpers.CustomDateTimePicker;
import com.tecdatum.Tracking.School.newhelpers.Monthdayreportitems_Idle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static androidx.appcompat.content.res.AppCompatResources.getDrawable;
import static com.tecdatum.Tracking.School.volley.MyApplication.TAG;


public class IdleReportFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static final String IdleReport = Url_new.IdleReport;

    private ArrayList<Monthdayreportitems_Idle> monthdayrepoer_list1 = new ArrayList<>();
    MonthdayreportAdapter monthdayreportAdapter;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View v;
    private OnFragmentInteractionListener mListener;
    String message, S_VehicleName, S_month, S_VehicleID, S_Speed;
    TextView tv_vehiclename;
    ProgressDialog progressDialog;
    ExpandableHeightListView list_monthday_report;
    String default_From_date_report, default_To_date_report;
    ImageView menu_ic;
    String    default_From_date, default_To_date,
            Start_time, Stop_time, default_mode, S_speed;
    TextView tv_fromandtodate;
    CustomDateTimePicker custom, custom1;
    LinearLayout ll_Datafound;
    TextView tv_back;
    Calendar date_fm, date_to;

    public IdleReportFragment() {
    }

    public static IdleReportFragment newInstance(String param1, String param2) {
        IdleReportFragment fragment = new IdleReportFragment();
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
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_idle_report, container, false);
        tv_vehiclename = (TextView) v.findViewById(R.id.tv_vehiclename);
        list_monthday_report = (ExpandableHeightListView) v.findViewById(R.id.list_monthday_report);
        tv_fromandtodate = v.findViewById(R.id.tv_fromandtodate);
        ll_Datafound = v.findViewById(R.id.ll_Datafound);
        tv_back = v.findViewById(R.id.tv_back);
        setdate();
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MonthDayReport_Fragment fragment = new MonthDayReport_Fragment();
                opennfrag(fragment);

            }
        });
        final Bundle b = getArguments();
        if (b != null) {
            S_VehicleID = b.getString("Vehicle_ID");
            S_month = b.getString("Vehicle");
            S_Speed = b.getString("Speed");
            S_month = b.getString("Vehicle");

            default_To_date_report = b.getString("todate");
            default_From_date_report = b.getString("fromdate");

            S_VehicleName = b.getString("Vehicle_Name");
            tv_vehiclename.setText("" + S_VehicleName);
            monthdayreport("3");
            Log.d("VEHICLENAME", S_VehicleName);
        } else {

        }


        menu_ic = v.findViewById(R.id.menu_ic);
        menu_ic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setdate();
                fromdata1();
            }
        });
        return v;
    }

    private void opennfrag(final Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.tabFrameLayout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }

    private void monthdayreport(String Speed) {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Getting Data from Server , Please Wait...");
        progressDialog.show();
        final RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("VehicleId", S_VehicleID);
            jsonObject.put("IdleDuration", Speed);
            jsonObject.put("StartDateTime", default_From_date);
            jsonObject.put("EndDateTime", default_To_date);

            Log.d("Request", String.valueOf(jsonObject));
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, IdleReport, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("Response", String.valueOf(response));
                    try {
                        JSONObject object = new JSONObject(String.valueOf(response));
                        String code = response.optString("Code", "0");
                        message = response.optString("Message", "Success");
                        if (code.equalsIgnoreCase("0")) {
                            list_monthday_report.setVisibility(View.VISIBLE);
                            progressDialog.dismiss();
                            monthdayrepoer_list1.clear();
                            ll_Datafound.setVisibility(View.VISIBLE);
                            JSONArray jsonArray = object.getJSONArray("IdleData");
                            int number = jsonArray.length();
                            String num = Integer.toString(number);
                            if (number == 0) {

                            } else {
                                for (int i = 0; i < number; i++) {
                                    try {

                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        Log.d("Request", String.valueOf(jsonObject));
                                        // Log.d("Request", routename);

                                        String tv_vehcle = jsonObject.getString("VehicleNo".toString());
                                        String vehicleName = jsonObject.getString("vehicleName".toString());
                                        String VehicleType = jsonObject.getString("VehicleType".toString());
                                        String Location = jsonObject.getString("Location".toString());
                                        String StartDate = jsonObject.getString("StartDate".toString());
                                        String StartTime = jsonObject.getString("StartTime".toString());
                                        String EndDate = jsonObject.getString("EndDate".toString());
                                        String EndTime = jsonObject.getString("EndTime".toString());
                                        String DurationinHours = jsonObject.getString("DurationinHours".toString());
                                        String DurationinMins = jsonObject.getString("DurationinMins".toString());


                                        Monthdayreportitems_Idle dataSet = new Monthdayreportitems_Idle(tv_vehcle, vehicleName, VehicleType, Location, StartDate, StartTime, EndDate, EndTime, DurationinHours, DurationinMins);

                                        monthdayrepoer_list1.add(dataSet);


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    monthdayreportAdapter = new MonthdayreportAdapter(getContext(), monthdayrepoer_list1);
                                    list_monthday_report.setAdapter(monthdayreportAdapter);

                                }
                            }
                        } else {
                            ll_Datafound.setVisibility(View.GONE);
                            progressDialog.dismiss();
                            list_monthday_report.setVisibility(View.GONE);
                            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                            alertDialog.setTitle("Respnse");
                            alertDialog.setMessage("No Data Available");


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
                    ll_Datafound.setVisibility(View.GONE);
                    progressDialog.dismiss();
                    list_monthday_report.setVisibility(View.GONE);
                }
            }
            );


            requestQueue.add(jsonObjectRequest);
        } catch (JSONException e) {
            progressDialog.dismiss();
            e.printStackTrace();
        }
    }

    public class MonthdayreportAdapter extends BaseAdapter {

        Context mContext;
        public static final String MY_PREFS_NAME = "MyPrefsFile";
        LayoutInflater inflater;

        ArrayList<Monthdayreportitems_Idle> list1;

        public MonthdayreportAdapter(Context context, List<Monthdayreportitems_Idle> list) {
            mContext = context;

            inflater = LayoutInflater.from(mContext);
            this.list1 = new ArrayList<Monthdayreportitems_Idle>();
            this.list1.addAll(list);
        }

        public class ViewHolder {
            TextView tv_IdleStart, tv_IdleEnd, tv_Duration, tv_Location;
        }

        @Override
        public int getCount() {
            return list1.size();
        }

        @Override
        public Monthdayreportitems_Idle getItem(int position) {
            return list1.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public View getView(final int position, View view, ViewGroup parent) {
            final ViewHolder holder;
            if (view == null) {
                holder = new ViewHolder();
                view = inflater.inflate(R.layout.after_list_idle, null);

                // holder.tv_vehcle = (TextView) view.findViewById(R.id.tv_vehcle);
                holder.tv_IdleStart = (TextView) view.findViewById(R.id.tv_IdleStart);

                holder.tv_IdleEnd = (TextView) view.findViewById(R.id.tv_IdleEnd);

                holder.tv_Duration = (TextView) view.findViewById(R.id.tv_Duration);

                holder.tv_Location = (TextView) view.findViewById(R.id.tv_Location);


                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            holder.tv_IdleStart.setText(list1.get(position).getStartDate() + "|" + list1.get(position).getStartTime());
            holder.tv_IdleEnd.setText(list1.get(position).getEndDate() + "|" + list1.get(position).getEndTime());
            holder.tv_Duration.setText(list1.get(position).getDurationinHours());
            holder.tv_Location.setText(list1.get(position).getLocation());

            return view;
        }


    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void setdate() {

        SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
        Date from_date = new Date();
        long to_date = from_date.getTime() - 1440 * 60 * 1000;
        Log.e("TEST", from_date.getTime() + " - " + to_date);
        default_From_date = simpledateformat.format(to_date);
        default_To_date = simpledateformat.format(from_date);
        default_mode = "Both";
        Log.e("from", default_From_date);
        Log.e("to", default_To_date);
        tv_fromandtodate.setText(default_From_date + "to " + default_To_date);
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
        final TextView from = new TextView(getContext());
        final TextView from_Date = new TextView(getContext());
        final TextView To_Date = new TextView(getContext());

        from.setText("Duration : ");
        from_Date.setText("From Date : ");
        To_Date.setText("To Date : ");

        from.setTextColor(Color.parseColor("#004c8e"));
        from.setTextSize(18f);


        from_Date.setTextColor(Color.parseColor("#004c8e"));
        from_Date.setTextSize(18f);


        To_Date.setTextColor(Color.parseColor("#004c8e"));
        To_Date.setTextSize(18f);

        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout linearLayout_fromdate = new LinearLayout(getContext());
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout linearLayout_todate = new LinearLayout(getContext());
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);


        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                400,
                LinearLayout.LayoutParams.MATCH_PARENT,
                1.0f
        );
        params.setMargins(5, 5, 5, 5);

        LinearLayout.LayoutParams speed_param = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,

                LinearLayout.LayoutParams.MATCH_PARENT
        );
        speed_param.setMargins(0, 0, 0, 5);


        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT,
                1.0f
        );
        params1.setMargins(5, 5, 5, 5);


        from.setGravity(View.TEXT_ALIGNMENT_CENTER);
        from.setLayoutParams(params);
        from_Date.setLayoutParams(params);
        from_Date.setGravity(View.TEXT_ALIGNMENT_CENTER);
        To_Date.setLayoutParams(params);
        To_Date.setGravity(View.TEXT_ALIGNMENT_CENTER);
        speed.setInputType(InputType.TYPE_CLASS_NUMBER);

        speed.setBackgroundDrawable(getDrawable(getContext(), R.drawable.edittext_baground));

        fm.setBackgroundDrawable(getDrawable(getContext(), R.drawable.edittext_baground));
        toT.setBackgroundDrawable(getDrawable(getContext(), R.drawable.edittext_baground));
        toT.setLayoutParams(params1);
        fm.setLayoutParams(params1);
        speed.setLayoutParams(params1);
        linearLayout.addView(from);
        linearLayout.addView(speed);

        linearLayout_fromdate.addView(from_Date);
        linearLayout_fromdate.addView(fm);

        linearLayout_todate.addView(To_Date);
        linearLayout_todate.addView(toT);
        tv.setText("Select");
        tv.setLayoutParams(speed_param);
        tv.setPadding(20, 20, 20, 20);
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(19f);
        tv.setTextColor(Color.WHITE);
        tv.setBackgroundDrawable(getDrawable(getContext(), R.color.textcolor_color));

        speed.setPadding(10, 10, 10, 10);
        fm.setPadding(10, 10, 10, 10);
        toT.setPadding(10, 10, 10, 10);


        fm.setText(" " + default_From_date);
        toT.setText(" " + default_To_date);
        speed.setText("3");
        Start_time = fm.getText().toString();
        Stop_time = toT.getText().toString();
        S_speed = speed.getText().toString();
        Log.e("Start_time", Start_time);
        Log.e("Stop_time", Stop_time);


        custom = new CustomDateTimePicker(getActivity(),
                new CustomDateTimePicker.ICustomDateTimeListener() {
                    @Override
                    public void onSet(Dialog dialog, Calendar calendarSelected,
                                      Date dateSelected, int year, String monthFullName,
                                      String monthShortName, int monthNumber, int date,
                                      String weekDayFullName, String weekDayShortName,
                                      int hour24, int hour12, int min, int sec,
                                      String AM_PM) {
                        fm.setText("");
                        fm.setText(year
                                + "-" + (monthNumber + 1) + "-" + calendarSelected.get(Calendar.DAY_OF_MONTH)
                                + " " + hour24 + ":" + min
                                + ":" + sec);
                    }

                    @Override
                    public void onCancel() {
                    }
                });


        custom.set24HourFormat(true);
        custom.setDate(Calendar.getInstance());


        custom1 = new CustomDateTimePicker(getActivity(),
                new CustomDateTimePicker.ICustomDateTimeListener() {
                    @Override
                    public void onSet(Dialog dialog, Calendar calendarSelected,
                                      Date dateSelected, int year, String monthFullName,
                                      String monthShortName, int monthNumber, int date,
                                      String weekDayFullName, String weekDayShortName,
                                      int hour24, int hour12, int min, int sec,
                                      String AM_PM) {
                        toT.setText("");
                        toT.setText(year
                                + "-" + (monthNumber + 1) + "-" + calendarSelected.get(Calendar.DAY_OF_MONTH)
                                + " " + hour24 + ":" + min
                                + ":" + sec);
                    }

                    @Override
                    public void onCancel() {
                    }
                });

        custom1.set24HourFormat(true);
        custom1.setDate(Calendar.getInstance());
        fm.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        //custom.showDialog();
                        final Calendar currentDate = Calendar.getInstance();
                        date_fm = Calendar.getInstance();
                        DatePickerDialog DatePickerDialog1 = new DatePickerDialog(getActivity(),
                                new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                        // view.setMaxDate(System.currentTimeMillis());
                                        view.setMaxDate(currentDate.getTimeInMillis());

                                        date_fm.set(year, monthOfYear, dayOfMonth);
                                        new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                                            @RequiresApi(api = Build.VERSION_CODES.N)
                                            @Override
                                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                                                date_fm.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                                date_fm.set(Calendar.MINUTE, minute);

                                                Log.v(TAG, "The choosen one " + date_fm.getTime());


                                                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
                                                fm.setText("");
                                                fm.setText("" + df.format(date_fm.getTime()));


                                            }
                                        }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();
                                    }


                                }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE));
                        DatePickerDialog1.getDatePicker().setMaxDate(System.currentTimeMillis());
                        DatePickerDialog1.show();

                    }
                });

        toT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // custom1.showDialog();

                final Calendar currentDate = Calendar.getInstance();
                date_to = Calendar.getInstance();
                DatePickerDialog DatePickerDialog2 = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        date_to.set(year, monthOfYear, dayOfMonth);
                        new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                            @RequiresApi(api = Build.VERSION_CODES.N)
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                                date_to.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                date_to.set(Calendar.MINUTE, minute);

                                Log.v(TAG, "The choosen one " + date_to.getTime());

                                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");

                                toT.setText("");
                                toT.setText("" + df.format(date_to.getTime()));


                            }
                        }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();
                    }
                }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE));
                DatePickerDialog2.getDatePicker().setMaxDate(System.currentTimeMillis());
                DatePickerDialog2.show();

            }
        });


        fm.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        toT.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);


        LinearLayout ll = new LinearLayout(getContext());
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.addView(tv);
        ll.addView(linearLayout);
        ll.addView(linearLayout_fromdate);
        ll.addView(linearLayout_todate);
        // ll.addView(spnr);
//        ll.addView(spnr1);
        alertDialog.setView(ll);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Submit",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        default_From_date = fm.getText().toString();
                        default_To_date = toT.getText().toString();
                        S_speed = speed.getText().toString();
                        Log.e("changed_from", default_From_date);
                        Log.e("changed_to", default_To_date);
                        tv_fromandtodate.setText(fm.getText().toString() + "    to  " + toT.getText().toString());

                        monthdayreport(S_speed);
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

}
