package com.tecdatum.Tracking.School.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

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
import com.tecdatum.Tracking.School.newhelpers.Monthdayreportitems;
import com.tecdatum.Tracking.School.newhelpers.Samplemyclass;
import com.tecdatum.Tracking.School.newhelpers.Spinneritems;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Report_DetailFragment extends Fragment {

    public static final String url_month_day_report = Url_new.MonthDayReport;
    String message, S_VehicleName, S_month, S_VehicleID;
    String tv_vehcle, tv_date, tv_WDT, tv_MS, tv_D;
    private ArrayList<Spinneritems> list = new ArrayList<>();
    private ArrayList<Samplemyclass> list1 = new ArrayList<>();
    private ArrayList<Monthdayreportitems> monthdayrepoer_list1 = new ArrayList<>();
    ExpandableHeightListView list_monthday_report;
    MonthdayreportAdapter monthdayreportAdapter;

    ProgressDialog progressDialog;
    TextView tv_vehiclename;
    View v;
    public static final String url_month_year = Url_new.Master;
    Spinner sp_month_alert;
    String S_SpinnerName, S_SpinnerID;
    LinearLayout ll_Datafound;
    TextView tv_back;
    Button bt_submit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.after_activity_report__detail, container, false);
        tv_vehiclename = (TextView) v.findViewById(R.id.tv_vehiclename);
        list_monthday_report = (ExpandableHeightListView) v.findViewById(R.id.list_monthday_report);
        ll_Datafound = v.findViewById(R.id.ll_Datafound);
        sp_month_alert = v.findViewById(R.id.sp_month_alert);
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
                monthdayreport();
                ;
            }
        });
        return v;

    }

    private void monthdayreport() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Getting Data from Server , Please Wait...");
        progressDialog.show();
        final RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("VehicleIds", S_VehicleID);
            jsonObject.put("MonthYear", S_SpinnerID);


            Log.d("Request", String.valueOf(jsonObject));
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url_month_day_report, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("Response", String.valueOf(response));
                    try {
                        JSONObject object = new JSONObject(String.valueOf(response));
                        String code = response.optString("Code", "0");
                        message = response.optString("Message", "Success");
                        if (code.equalsIgnoreCase("0")) {
                            progressDialog.dismiss();
                            ll_Datafound.setVisibility(View.VISIBLE);
                            monthdayrepoer_list1.clear();
                            JSONArray jsonArray = object.getJSONArray("Data");
                            int number = jsonArray.length();
                            String num = Integer.toString(number);
                            if (number == 0) {

                            } else {
                                for (int i = 0; i < number; i++) {
                                    try {

                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        Log.d("Request", String.valueOf(jsonObject));

                                        tv_vehcle = jsonObject.getString("VehicleNo".toString());
                                        tv_date = jsonObject.getString("Date".toString());
                                        tv_WDT = jsonObject.getString("WorkDuration".toString());
                                        tv_MS = jsonObject.getString("MaxSpeed".toString());
                                        tv_D = jsonObject.getString("Distance".toString());
                                        Monthdayreportitems dataSet = new Monthdayreportitems(tv_date, tv_WDT, tv_MS, tv_D);

                                        monthdayrepoer_list1.add(dataSet);


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    monthdayreportAdapter = new MonthdayreportAdapter(getContext(), monthdayrepoer_list1);
                                    list_monthday_report.setAdapter(monthdayreportAdapter);
                                    Log.e("znm", "arraylist" + monthdayrepoer_list1);

                                }
                            }
                        } else {
                            ll_Datafound.setVisibility(View.GONE);
                            progressDialog.dismiss();
                            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                            alertDialog.setTitle("Response");
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
                    ll_Datafound.setVisibility(View.GONE);
                    progressDialog.dismiss();
                }
            }
            );


            requestQueue.add(jsonObjectRequest);
        } catch (JSONException e) {
            progressDialog.dismiss();
            e.printStackTrace();
        }
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

                int pos = adapterView.getSelectedItemPosition();


                try {
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Samplemyclass dataSet = (Samplemyclass) adapterView.getSelectedItem();
                S_SpinnerName = dataSet.getName();
                S_SpinnerID = dataSet.getId();
                S_month = dataSet.getName();

            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void opennfrag(final Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.tabFrameLayout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }

    public static class MonthdayreportAdapter extends BaseAdapter {

        Context mContext;
        public static final String MY_PREFS_NAME = "MyPrefsFile";
        LayoutInflater inflater;

        ArrayList<Monthdayreportitems> list1;

        public MonthdayreportAdapter(Context context, List<Monthdayreportitems> list) {
            mContext = context;

            inflater = LayoutInflater.from(mContext);
            this.list1 = new ArrayList<Monthdayreportitems>();
            this.list1.addAll(list);
        }

        public class ViewHolder {
            TextView tv_date, tv_WDT, tv_MS, tv_D;

        }

        @Override
        public int getCount() {
            return list1.size();
        }

        @Override
        public Monthdayreportitems getItem(int position) {
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
                view = inflater.inflate(R.layout.after_list, null);

                holder.tv_date = (TextView) view.findViewById(R.id.tv_date);

                holder.tv_WDT = (TextView) view.findViewById(R.id.tv_WDT);

                holder.tv_MS = (TextView) view.findViewById(R.id.tv_MS);

                holder.tv_D = (TextView) view.findViewById(R.id.tv_D);


                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            holder.tv_date.setText(list1.get(position).getTv_date());
            holder.tv_WDT.setText(list1.get(position).getTv_WDT());
            holder.tv_MS.setText(list1.get(position).getTv_MS());
            holder.tv_D.setText(list1.get(position).getTv_D());

            return view;
        }


    }
}
