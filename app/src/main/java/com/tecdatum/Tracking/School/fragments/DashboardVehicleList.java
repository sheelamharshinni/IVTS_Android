package com.tecdatum.Tracking.School.fragments;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.android.material.tabs.TabLayout;
import com.tecdatum.Tracking.School.R;
import com.tecdatum.Tracking.School.newConstants.Url_new;
import com.tecdatum.Tracking.School.newhelpers.QvVehiclesList;
import com.tecdatum.Tracking.School.volley.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;
import static com.tecdatum.Tracking.School.fragments.MonthDayReport_Fragment.MY_PREFS_NAME;


public class DashboardVehicleList extends Fragment {
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    View v;
    RadioButton rg_runing, rg_stop, rg_idle, rg_nosignal;
    RadioGroup daily_weekly_button_view;
    String UserID, SPassword, SIMEI, SOrgid, SBranchid, SUserid,Orgid,LevelId,HierarchyId;
    String VehicleTpe, Vehiclestatus;
    private String DashboardVehicleList1 = Url_new.DashboardVehicleList;
    ArrayList<QvVehiclesList> arraylist = new ArrayList<>();
    String a_VehicleNo, a_StartDateTime, a_StartLocation, a_EndDateTime, a_EndLocation,
            a_Distance, a_TravelTime, a_IdleTime, a_StopTime, a_Total_Distance;
    HeroAdapter adapter;
    private String DashBoardChart = Url_new.DashBoardChart;
    private String HistoryTrackInformation = Url_new.HistoryTrackInformation;
    private String url_LiveTracking = Url_new.LiveTracking;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private List<QvVehiclesList> exampleListFull;
    private String mParam1;
    private String mParam2;
    ArrayList<String> xVals = new ArrayList<>();
    ArrayList<Entry> yVals = new ArrayList<>();
    ArrayList<String> arraylist_Time = new ArrayList<String>();
    ArrayList<String> arraylist_Distance = new ArrayList<String>();
    ArrayList<Integer> arraylist_Distance1 = new ArrayList<Integer>();
    ArrayList<Integer> arraylist_Time1 = new ArrayList<Integer>();
    public static ArrayList<String> x_axis = new ArrayList<String>();
    public static ArrayList<String> y_axis = new ArrayList<String>();

    String default_From_date, default_To_date, message,
            default_mode;

    public DashboardVehicleList() {
    }

    public static DashboardVehicleList newInstance(String param1, String param2) {
        DashboardVehicleList fragment = new DashboardVehicleList();
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
        v = inflater.inflate(R.layout.fragment_dashboard_vehiclelist, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        TextView tv_fragmentName = (TextView) v.findViewById(R.id.tv_fragmentName);
        tv_fragmentName.setText("Vehicle List");
        rg_runing = v.findViewById(R.id.rg_runing);
        rg_stop = v.findViewById(R.id.rg_stop);
        rg_idle = v.findViewById(R.id.rg_idle);
        rg_nosignal = v.findViewById(R.id.rg_nosignal);
        daily_weekly_button_view = v.findViewById(R.id.daily_weekly_button_view);
        final Bundle b = getArguments();
        if (b != null) {
            VehicleTpe = b.getString("TYPE");

            Vehiclestatus = b.getString("STATUS");

            Log.d("VEHICLETYPESTATUS", VehicleTpe);
            if (Vehiclestatus != null) {
                if (!Vehiclestatus.isEmpty()) {
                    if (Vehiclestatus.equals("Run")) {
                        rg_runing.setChecked(true);
                        if (rg_runing.isChecked()) {

                            makeJsonObjReq(Vehiclestatus, VehicleTpe);
                        } else {

                        }
                    } else if (Vehiclestatus.equals("Stop")) {
                        rg_stop.setChecked(true);
                        if (rg_stop.isChecked()) {
                            makeJsonObjReq(Vehiclestatus, VehicleTpe);
                        } else {

                        }
                    } else if (Vehiclestatus.equals("Idle")) {
                        rg_idle.setChecked(true);
                        if (rg_idle.isChecked()) {
                            makeJsonObjReq(Vehiclestatus, VehicleTpe);
                        } else {

                        }
                    } else if (Vehiclestatus.equals("Nosignal")) {
                        rg_nosignal.setChecked(true);
                        if (rg_nosignal.isChecked()) {
                            makeJsonObjReq(Vehiclestatus, VehicleTpe);
                        } else {

                        }
                    }
                }
            }


        } else {
            Vehiclestatus = "Run";
            VehicleTpe = "Car";
            rg_runing.setChecked(true);

        }
        if (rg_runing.isChecked()) {
            makeJsonObjReq("Run", "Car");
        }

        daily_weekly_button_view.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int selectedId = daily_weekly_button_view.getCheckedRadioButtonId();
                RadioButton radiolngButton = (RadioButton) v.findViewById(selectedId);


                if (radiolngButton.getText().toString().equalsIgnoreCase("Running")) {
                    Vehiclestatus = "Run";
                    makeJsonObjReq(Vehiclestatus, VehicleTpe);

                }
                if (radiolngButton.getText().toString().equalsIgnoreCase("Stop")) {
                    Vehiclestatus = "Stop";
                    makeJsonObjReq(Vehiclestatus, VehicleTpe);
                }
                if (radiolngButton.getText().toString().equalsIgnoreCase("Idle")) {
                    Vehiclestatus = "Idle";
                    makeJsonObjReq(Vehiclestatus, VehicleTpe);
                }
                if (radiolngButton.getText().toString().equalsIgnoreCase("In Repair")) {
                    Vehiclestatus = "Nosignal";
                    makeJsonObjReq(Vehiclestatus, VehicleTpe);

                }
            }
        });
        return v;

    }

    private void makeJsonObjReq(String status, String VehicleTyp) {

        try {
            SharedPreferences bb = getContext().getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
            UserID = bb.getString("UserID", "");
            Orgid = bb.getString("Orgid", "");
            LevelId = bb.getString("LevelId", "");
            HierarchyId = bb.getString("HierarchyId", "");


            JSONObject jsonBody = new JSONObject();
            jsonBody.put("OrgId", Orgid);
            jsonBody.put("VehicleNo", "");
            jsonBody.put("UserId", UserID);
            jsonBody.put("VehicleType", VehicleTyp);
            jsonBody.put("Fromstatus", status);;
            jsonBody.put("LevelId", LevelId);
            jsonBody.put("HierarchyId", HierarchyId);

            final String mRequestBody = jsonBody.toString();
            Log.i("VOLLEY", mRequestBody);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, DashboardVehicleList1, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("VOLLEY", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        int code = Integer.parseInt(object.optString("Code").toString());
                        String message = object.optString("Message").toString();
                        if (code == 0) {
                            //    progressDialog.dismiss();
                            recyclerView.setVisibility(View.VISIBLE);
                            JSONArray jArray = object.getJSONArray("VehicleList");
                            int number = jArray.length();

                            String num = Integer.toString(number);
                            if (number == 0) {
                                Toast.makeText(getContext(), " No Data Found", Toast.LENGTH_LONG).show();
                            } else {
                                arraylist.clear();
                                for (int i = 0; i < number; i++) {
                                    JSONObject json_data = jArray.getJSONObject(i);

                                    String  vehicle_Id = json_data.getString("Id").toString();
                                    String VehicleRegNo = json_data.getString("VehicleRegNo").toString();
                                    String Vehicle = json_data.getString("Vehicle").toString();
                                    String Vehicletype = json_data.getString("Vehicletype").toString();
                                    String Sincefrom = json_data.getString("Sincefrom").toString();
                                    String Livedatetime = json_data.getString("Livedatetime").toString();
                                    String DriverName = json_data.getString("DriverName").toString();
                                    String MobileNo = json_data.getString("MobileNo").toString();
                                    String VehicleStatus = json_data.getString("VehicleStatus").toString();
                                    String Location = json_data.getString("Location").toString();
                                    String Latitude = json_data.getString("Latitude").toString();
                                    String Longitude = json_data.getString("Longitude").toString();
                                    String AcStatus = json_data.getString("AcStatus").toString();
                                    String Gpsstatus = json_data.getString("Gpsstatus").toString();
                                    String Speed = json_data.getString("Speed").toString();
                                    String Ignition = json_data.getString("Ignition").toString();
                                    String Model = json_data.getString("Vehicletype").toString();
                                    String PickRoute=json_data.getString("PickRoute").toString();
                                    String DropRoute=json_data.getString("DropRoute").toString();

                                    String date = Sincefrom;


                                    try {
                                        DateFormat inputFormat = new SimpleDateFormat("dd MMM yyyy hh:mma");
                                        DateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy hh:mm");
                                        Date oneWayTripDate = inputFormat.parse(date);
                                        Sincefrom = outputFormat.format(oneWayTripDate);// parse input
                                        //holder.tv_DateofInspection.setText(s);
                                    } catch (Exception ex) {
                                        System.out.println("Date error");

                                    }


                                    QvVehiclesList wp = new QvVehiclesList(vehicle_Id, VehicleRegNo, Vehicle, Vehicletype, Sincefrom,
                                            Livedatetime, DriverName, MobileNo, VehicleStatus, Location, Latitude, Longitude, AcStatus, Gpsstatus, Speed, Ignition, Model,PickRoute,DropRoute);
                                    // Binds all strings into an array
                                    arraylist.add(wp);

                                }

                                adapter = new HeroAdapter(arraylist);
                                recyclerView.setAdapter(adapter);

                            }
                        } else {
                            //  progressDialog.dismiss();
                            try {
                                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                                alertDialog.setTitle("Response");
                                alertDialog.setMessage("" + message);
                                alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Write your code here to invoke YES event
                                        dialog.cancel();
                                    }
                                });

                                alertDialog.show();
                            } catch (Exception e) {
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
                    Log.e("VOLLEY", error.toString());

                    arraylist.clear();
                    recyclerView.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Unable to Connect to Server", Toast.LENGTH_LONG).show();

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
                    5010000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleySingleton.getInstance().getRequestQueue().add(stringRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public class HeroAdapter extends RecyclerView.Adapter<HeroAdapter.HeroViewHolder> implements Filterable {

        private List<QvVehiclesList> heroList;
        private Context context;
        private int currentPosition;
        boolean isclickoverviewlay = true;

        @Override
        public Filter getFilter() {
            return null;
        }

        public HeroAdapter(List<QvVehiclesList> heroList) {
            this.heroList = heroList;
            exampleListFull = heroList;
            this.context = context;
            exampleListFull = new ArrayList<>(heroList);

        }

        @NonNull
        @Override
        public HeroViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_layout_adapter, parent, false);
            return new HeroViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull final HeroViewHolder holder, final int position) {
            final QvVehiclesList hero = heroList.get(position);
            final QvVehiclesList hero1 = exampleListFull.get(position);
            holder.bind(hero, position);

            holder.textViewName.setText(hero.getVehicle());
            holder.textViewVersion.setText(hero.getLocation());
            holder.sincefrom.setText(hero.getSincefrom());

            if (hero.getVehicleStatus().equals("Run")) {
                if (hero.getVehicletype().equalsIgnoreCase("Blue Colts") | hero.getVehicletype().equals("M/C")) {
                    holder.imageView.setImageResource(R.drawable.mc_run);
                } else {
                    holder.imageView.setImageResource(R.drawable.bus_running);
                }

            }
            if (hero.getVehicleStatus().equals("Stop") | hero.getVehicleStatus().equals("stop")) {
                if (hero.getVehicletype().equalsIgnoreCase("Blue Colts") | hero.getVehicletype().equals("M/C")) {
                    holder.imageView.setImageResource(R.drawable.mc_stop);
                } else {
                    holder.imageView.setImageResource(R.drawable.bus_stop);
                }
            }
            if (hero.getVehicleStatus().equals("Idle")) {
                if (hero.getVehicletype().equalsIgnoreCase("Blue Colts") | hero.getVehicletype().equals("M/C")) {
                    holder.imageView.setImageResource(R.drawable.mc_idle);
                } else {
                    holder.imageView.setImageResource(R.drawable.bus_idle);
                }
            }

            if (hero.getVehicleStatus().equals("Nosignal") | hero.getVehicleStatus().equals("Nosignals") | hero.getVehicleStatus().equals("UnderFailure") | (hero.getVehicleStatus().equals("NoSignal"))) {
                if (hero.getVehicletype().equalsIgnoreCase("Blue Colts") | hero.getVehicletype().equals("M/C")) {
                    holder.imageView.setImageResource(R.drawable.mc_nosignal);
                } else {
                    holder.imageView.setImageResource(R.drawable.bus_inrepair);
                }
            }
            if (hero.getVehicleStatus().equals("") | hero.getVehicleStatus().equals("") | (hero.getVehicleStatus().equals(""))) {
                if (hero.getVehicletype().equalsIgnoreCase("Blue Colts") | hero.getVehicletype().equals("M/C")) {
                    holder.imageView.setImageResource(R.drawable.mc_nosignal);
                } else {
                    holder.imageView.setImageResource(R.drawable.bus_inrepair);
                }
            }


            if (hero.getVehicleStatus().equals("Run")) {
                if (hero.getVehicletype().equalsIgnoreCase("Blue Colts")) {
                    holder.imageView.setImageResource(R.drawable.mc_run);
                }
                if (hero.getVehicletype().equalsIgnoreCase("M/C")) {
                    holder.imageView.setImageResource(R.drawable.mc_run);
                }
                if (hero.getVehicletype().equalsIgnoreCase("Car")) {
                    holder.imageView.setImageResource(R.drawable.bus_running);
                }


            }
            if (hero.getVehicleStatus().equals("Stop") | hero.getVehicleStatus().equals("stop")) {
                if (hero.getVehicletype().equalsIgnoreCase("Blue Colts")) {
                    holder.imageView.setImageResource(R.drawable.mc_stop);
                }
                if (hero.getVehicletype().equalsIgnoreCase("M/C")) {
                    holder.imageView.setImageResource(R.drawable.mc_stop);
                }
                if (hero.getVehicletype().equalsIgnoreCase("Car")) {
                    holder.imageView.setImageResource(R.drawable.bus_stop);
                }


            }
            if (hero.getVehicleStatus().equals("Idle")) {
                if (hero.getVehicletype().equalsIgnoreCase("Blue Colts")) {
                    holder.imageView.setImageResource(R.drawable.mc_idle);
                }
                if (hero.getVehicletype().equalsIgnoreCase("M/C")) {
                    holder.imageView.setImageResource(R.drawable.mc_idle);
                }
                if (hero.getVehicletype().equalsIgnoreCase("Car")) {
                    holder.imageView.setImageResource(R.drawable.bus_idle);
                }


            }


            if (hero.getVehicleStatus().equals("Nosignal") | hero.getVehicleStatus().equals("Nosignals") | hero.getVehicleStatus().equals("UnderFailure") | (hero.getVehicleStatus().equals("NoSignal"))) {
                if (hero.getVehicletype().equalsIgnoreCase("Blue Colts")) {
                    holder.imageView.setImageResource(R.drawable.mc_nosignal);
                }
                if (hero.getVehicletype().equalsIgnoreCase("M/C")) {
                    holder.imageView.setImageResource(R.drawable.mc_nosignal);
                }
                if (hero.getVehicletype().equalsIgnoreCase("Car")) {
                    holder.imageView.setImageResource(R.drawable.bus_inrepair);
                }


            }
            if (hero.getVehicleStatus().equals("") | hero.getVehicleStatus().equals("") | (hero.getVehicleStatus().equals(""))) {
                if (hero.getVehicletype().equalsIgnoreCase("Blue Colts")) {
                    holder.imageView.setImageResource(R.drawable.mc_nosignal);
                }
                if (hero.getVehicletype().equalsIgnoreCase("M/C")) {
                    holder.imageView.setImageResource(R.drawable.mc_nosignal);
                }
                if (hero.getVehicletype().equalsIgnoreCase("Car")) {
                    holder.imageView.setImageResource(R.drawable.bus_inrepair);
                }


            }
            holder.card_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    currentPosition = position;

                    boolean expanded = hero.isExpanded();
                    SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                    SharedPreferences.Editor edit = prefs.edit();
                    edit.putString("VehicleID", heroList.get(position).getVehicle_Id());


                    edit.commit();
                    hero.setExpanded(!expanded);
                    notifyItemChanged(position);
                    notifyDataSetChanged();


                }
            });

        }

        @Override
        public int getItemCount() {
            return heroList.size();
        }

        class HeroViewHolder extends RecyclerView.ViewHolder {
            TextView textViewName, textViewVersion, sincefrom;
            ImageView imageView;
            CardView card_view;
            TabLayout tabLayout;
            ViewPager viewPager;
            LinearLayout linearLayout;
            private LineChart mChart;
            TextView tv_more;
            ImageView expand, collapse;
            LinearLayout ll_vehiclesadapter;
            TextView tv_estimatetime;
            TextView tv_ql_StartDatetime, tv_ql_StartLocation, tv_ql_EndDatetime, tv_ql_EndLocation, tv_ql_Distance,
                    tv_ql_TravelTime, tv_ql_IdleTime, tv_ql_StopTime;

            HeroViewHolder(final View itemView) {
                super(itemView);
                textViewVersion = itemView.findViewById(R.id.textViewVersion);
                textViewName = (TextView) itemView.findViewById(R.id.textViewName);
                sincefrom = itemView.findViewById(R.id.sincefrom);
                imageView = (ImageView) itemView.findViewById(R.id.imageView);
                card_view = itemView.findViewById(R.id.card_view);
                linearLayout = itemView.findViewById(R.id.linearLayout);


                viewPager = (ViewPager) itemView.findViewById(R.id.viewpager);
                SimpleFragmentPagerAdapter viewPagerAdapter = new SimpleFragmentPagerAdapter(getActivity().getSupportFragmentManager());
                viewPager.setAdapter(viewPagerAdapter);

                tabLayout = (TabLayout) itemView.findViewById(R.id.sliding_tabs);
                tabLayout.setupWithViewPager(viewPager);
                mChart = (LineChart) itemView.findViewById(R.id.linechart_distance);
                tv_more = itemView.findViewById(R.id.tv_more);
                ll_vehiclesadapter = itemView.findViewById(R.id.ll_vehiclesadapter);

                expand = itemView.findViewById(R.id.expand);
                collapse = itemView.findViewById(R.id.collapse);
                tv_estimatetime = (TextView) itemView.findViewById(R.id.tv_estimatetime);
                tv_ql_StartDatetime = (TextView) itemView.findViewById(R.id.tv_ql_StartDatetime);
                tv_ql_StartLocation = (TextView) itemView.findViewById(R.id.tv_ql_StartLocation);
                tv_ql_EndDatetime = (TextView) itemView.findViewById(R.id.tv_ql_EndDatetime);
                tv_ql_EndLocation = (TextView) itemView.findViewById(R.id.tv_ql_EndLocation);
                tv_ql_Distance = (TextView) itemView.findViewById(R.id.tv_ql_Distance);
                tv_ql_TravelTime = (TextView) itemView.findViewById(R.id.tv_ql_TravelTime);
                tv_ql_IdleTime = (TextView) itemView.findViewById(R.id.tv_ql_IdleTime);
                tv_ql_StopTime = (TextView) itemView.findViewById(R.id.tv_ql_StopTime);
            }

            private void bind(final QvVehiclesList movie, int position) {
                boolean expanded = movie.isExpanded();


                if (position == currentPosition) {

                    linearLayout.setVisibility(expanded ? View.VISIBLE : View.GONE);
                    tabLayout.setVisibility(expanded ? View.VISIBLE : View.GONE);
                    graphCall("", movie.getVehicle_Id());
                    TrackingINFO(movie.getVehicle_Id(), movie.getVehicle());
                } else {

                    linearLayout.setVisibility(View.GONE);
                    tabLayout.setVisibility(View.GONE);
                }

                if (position == currentPosition) {
                    if (expanded == true) {
                        expand.setVisibility(View.GONE);
                        collapse.setVisibility(View.VISIBLE);
                        tv_more.setVisibility(View.VISIBLE);


                    } else {

                        expand.setVisibility(View.VISIBLE);
                        collapse.setVisibility(View.GONE);
                        tv_more.setVisibility(View.GONE);

                    }
                } else {
                    expand.setVisibility(View.VISIBLE);
                    collapse.setVisibility(View.GONE);
                    tv_more.setVisibility(View.GONE);

                }
            }

            private void graphCall(String Date, String vehicleid) {
                mChart.setNoDataTextDescription("or" + "\n" + "Loading Chart Data");
                try {
                    SharedPreferences bb = getContext().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                    SOrgid = bb.getString("Orgid", "");
                    SPassword = bb.getString("PassWord", "");
                    SBranchid = bb.getString("Branchid", "");
                    SIMEI = bb.getString("IMEI", "");
                    SUserid = bb.getString("Userid", "");
                    //   SUsername= bb.getString("Username", "");
                    //   Toast.makeText(getApplicationContext(), " Result" +SOrgid+SUserid, Toast.LENGTH_LONG).show();

                    JSONObject jsonBody = new JSONObject();
                    jsonBody.put("Userid", SUserid);
                    jsonBody.put("Password", SPassword);
                    jsonBody.put("Imei", SIMEI);
                    jsonBody.put("Vehicleid", vehicleid);
                    //  jsonBody.put("Date", Date);

                    final String mRequestBody = jsonBody.toString();
                    Log.i("VOLLEY", "input to dashchart" + mRequestBody);
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, DashBoardChart, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("VOLLEY", "Output to dashchart" + response);
                            try {
                                JSONObject object = new JSONObject(response);
                                String code = object.optString("Code").toString();
                                String message = object.optString("Message").toString();


                                if (code.equalsIgnoreCase("0")) {
                                    xVals.clear();
                                    yVals.clear();
                                    arraylist_Time.clear();
                                    arraylist_Distance.clear();
                                    arraylist_Time1.clear();
                                    arraylist_Distance1.clear();
                                    x_axis.clear();
                                    y_axis.clear();
                                    a_Total_Distance = object.optString("TotalDistance").toString();


                                    JSONArray jArray = object.getJSONArray("Data");
                                    int number = jArray.length();

                                    String num = Integer.toString(number);
                                    if (number == 0) {
                                        Toast.makeText(getContext(), " No Data Found", Toast.LENGTH_LONG).show();
                                    } else {
                                        try {
                                            for (int i = 0; i < number; i++) {
                                                JSONObject json_data = jArray.getJSONObject(i);
                                                String Hour = json_data.getString("Hour").toString();
                                                String Distance = json_data.getString("Distance").toString();

                                                //i = Math.round(Hour);


                                                arraylist_Time.add(Hour);
                                                //  insertDB(Hour, Distance);

//                                    float distance = Float.parseFloat(Distance);
//                                    String jsonFormattedString = Hour.replaceAll(":", ".");
//                                    float Hour_f = Float.parseFloat(jsonFormattedString);
//                                    float result;
//                                    result = round(distance, 1);
//                                    int hour = (int) Hour_f;
//                                    xVals.add(String.valueOf(hour));
//                                    yVals.add(new Entry(result, Integer.valueOf(xVals.get(i))));
//                                    xVals1.add(String.valueOf(Math.round(hour)));
//                                    yVals1.add((float) Math.round(result));
//                                    insertDB(String.valueOf(Math.round(hour)), String.valueOf(Math.round(result)));

                                                float distance = Float.parseFloat(Distance);
                                                String jsonFormattedString = Hour.replaceAll(":", ".");
                                                float Time = Float.parseFloat(jsonFormattedString);
                                                Integer Hour_f = Math.round(Time);
                                                float result;
                                                result = round(distance, 2);
                                                int hour = (int) Hour_f;
                                                x_axis.add(String.valueOf(Math.round(Time)));
                                                y_axis.add(String.valueOf(result));


                                            }
                                            Log.e("Chart y", "" + yVals.size());
                                            Log.e("Chart x", "" + xVals.size());

                                            setLineChartData(setXAxisValues(), y_axis);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                } else {
                                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
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
                            500000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    VolleySingleton.getInstance().getRequestQueue().add(stringRequest);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            private void TrackingINFO(String veh_id, String veh_name) {

                setdate();
                SharedPreferences bb = getContext().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                String ID = bb.getString("Id", "");
                String VEHICLE_NAME = bb.getString("VehicleName", "");
                String quary = bb.getString("query", "");
                SPassword = bb.getString("PassWord", "");
                SBranchid = bb.getString("Branchid", "");
                SIMEI = bb.getString("IMEI", "");
                SUserid = bb.getString("Userid", "");
                Log.i("1", quary);
                tv_estimatetime.setText("From-" + "" + default_From_date + "\t" + "To-" + "" + default_To_date);

                try {
                    Log.d("Vehicle_Name", veh_id);
                    JSONObject jsonBody = new JSONObject();

                    jsonBody.put("UserId", SUserid);
                    jsonBody.put("Password", SPassword);
                    jsonBody.put("IMEI", SIMEI);

                    jsonBody.put("VehicleId", veh_id);
                    jsonBody.put("StartDate", default_From_date);
                    jsonBody.put("EndDate", default_To_date);


                    final String mRequestBody = jsonBody.toString();

                    Log.e(TAG, "request HistoryTrackInformation" + mRequestBody);
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, HistoryTrackInformation, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i(TAG, "" + "Response HistoryTrackInformation" + response);
                            try {

                                JSONObject object = new JSONObject(response);
                                String code = object.optString("Code").toString();
                                message = object.optString("Message").toString();

                                if (code.equalsIgnoreCase("0")) {
                                    a_VehicleNo = object.optString("VehicleNo").toString();
                                    a_StartDateTime = object.optString("StartDateTime").toString();
                                    a_StartLocation = object.optString("StartLocation").toString();
                                    a_EndDateTime = object.optString("EndDateTime").toString();
                                    a_EndLocation = object.optString("EndLocation").toString();
                                    //  a_Distance = object.optString("Distance").toString();
                                    a_TravelTime = object.optString("TravelTime").toString();
                                    a_IdleTime = object.optString("IdleTime").toString();
                                    a_StopTime = object.optString("StopTime").toString();
                                    //    a_NoSignalTime = object.optString("NoSignalTime").toString();

//                            if (a_NoSignalTime == null) {
//                                tv_ql_NoSignalTime.setText("");
//                            } else {
//                                tv_ql_NoSignalTime.setText("" + a_NoSignalTime);
//                            }
                                    if (a_StartDateTime == null) {
                                        tv_ql_StartDatetime.setText("");
                                    } else {
                                        tv_ql_StartDatetime.setText("" + a_StartDateTime);
                                    }
                                    if (a_StartLocation == null) {
                                        tv_ql_StartLocation.setText("");
                                    } else {
                                        tv_ql_StartLocation.setText("" + a_StartLocation);
                                    }
                                    if (a_EndDateTime == null) {
                                        tv_ql_EndDatetime.setText("");
                                    } else {
                                        tv_ql_EndDatetime.setText("" + a_EndDateTime);
                                    }

                                    if (a_EndLocation == null) {
                                        tv_ql_EndLocation.setText("");
                                    } else {
                                        tv_ql_EndLocation.setText("" + a_EndLocation);
                                    }

                                    if (a_Total_Distance == null) {
                                        tv_ql_Distance.setText("");
                                    } else {
                                        tv_ql_Distance.setText("" + a_Total_Distance);
                                    }

                                    if (a_TravelTime == null) {
                                        tv_ql_TravelTime.setText("");
                                    } else {
                                        tv_ql_TravelTime.setText("" + a_TravelTime);
                                    }
                                    if (a_IdleTime == null) {
                                        tv_ql_IdleTime.setText("");
                                    } else {
                                        tv_ql_IdleTime.setText("" + a_IdleTime);
                                    }
                                    if (a_StopTime == null) {
                                        tv_ql_StopTime.setText("");
                                    } else {
                                        tv_ql_StopTime.setText("" + a_StopTime);
                                    }


                                } else {


                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("VOLLEY", error.toString());
                            // pDialog.dismiss();
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

            private void setdate() {

                SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date from_date = new Date();
                long to_date = from_date.getTime() - 1440 * 60 * 1000;
                Log.e("TEST", from_date.getTime() + " - " + to_date);
                default_From_date = simpledateformat.format(to_date);
                default_To_date = simpledateformat.format(from_date);
                default_mode = "Both";
                Log.e("from", default_From_date);
                Log.e("to", default_To_date);

            }

            public float round(float d, int decimalPlace) {
                BigDecimal bd = new BigDecimal(Float.toString(d));
                bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
                return bd.floatValue();
            }

            private void setLineChartData(ArrayList<String> xVal, ArrayList<String> yVal) {

                List<Entry> entries1 = new ArrayList<Entry>();
                for (int i = 0; i < xVal.size(); i++) {
                    entries1.add(new Entry(Float.valueOf(yVal.get(i)), Integer.valueOf(xVal.get(i))));
                }

                LineDataSet dataSet = new LineDataSet(entries1, "Distance(km)(Y-axis) Vs Time Chart(hrs)(X-axis) ");
                Log.e("ChartData 23", "" + entries1 + "" + "\n");
                Log.e("ChartData 11", "" + xVal.size() + "" + "\n" + arraylist_Time.size() + "\n" + yVal.size());
                mChart.setDescription("---> Time (Hrs)");

                dataSet.setColor(Color.BLUE);
                dataSet.setColor(Color.BLUE);
                dataSet.setCircleColor(Color.BLUE);
                dataSet.setLineWidth(2f);
                dataSet.setCircleRadius(3f);
                dataSet.setDrawCircleHole(true);

                LineData data = new LineData(arraylist_Time, dataSet);
                XAxis xAxis = mChart.getXAxis();
                mChart.setData(data);
                mChart.invalidate();

            }

            private ArrayList<String> setXAxisValues() {

                xVals = new ArrayList<String>();

//        for (int i = 0; i < x_axis.size(); i++) {
//            xVals.add(x_axis.get(i));
//        }


                xVals.add("00");
                xVals.add("1");
                xVals.add("2");
                xVals.add("3");
                xVals.add("4");
                xVals.add("5");
                xVals.add("6");
                xVals.add("7");
                xVals.add("8");
                xVals.add("9");
                xVals.add("10");
                xVals.add("11");
                xVals.add("12");
                xVals.add("13");
                xVals.add("14");
                xVals.add("15");
                xVals.add("16");
                xVals.add("17");
                xVals.add("18");
                xVals.add("19");
                xVals.add("20");
                xVals.add("21");
                xVals.add("22");
                xVals.add("23");
                xVals.add("24");


//         [10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9]
                return xVals;
            }
        }


    }

    public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {

        public SimpleFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            if (position == 0) {
                LiveStatus fragment2 = new LiveStatus();

                return fragment2;
            } else if (position == 1) {
                LivetrackingFragment fragment1 = new LivetrackingFragment();

                return fragment1;

            } else if (position == 2) {

                HistoryTrackingFragment fragment = new HistoryTrackingFragment();

                return fragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String title = null;
            if (position == 0) {
                title = "Live Status";
            } else if (position == 1) {
                title = "Live Tracking";
            } else if (position == 2) {
                title = "History Tracking";
            }
            return title;
        }
    }

}
