package com.tecdatum.Tracking.School.fragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.google.android.gms.common.api.GoogleApiClient;
import com.tecdatum.Tracking.School.R;
import com.tecdatum.Tracking.School.newConstants.Url_new;
import com.tecdatum.Tracking.School.newhelpers.GetVehicleRoutes_List;
import com.tecdatum.Tracking.School.newhelpers.GetVehicleRoutes_List_student;
import com.tecdatum.Tracking.School.newhelpers.Samplemyclass;
import com.tecdatum.Tracking.School.volley.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Admin_Fragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ProgressDialog progressDialog;
    private String mParam1;
    private String mParam2;
    View view;
    RadioGroup daily_weekly_button_view;
    private String GetVehicleRoutes = Url_new.GetVehicleRoutes;
    private String GetRouteAssign = Url_new.GetRouteAssign;
    private String RoutesMaster = Url_new.RoutesMaster;
    private String PointsMaster = Url_new.PointsMaster;
    private String Studentslist = Url_new.Studentslist;
    private String UpdateVehicleAssigns = Url_new.UpdateVehicleAssigns;
    private String VehicleListForUpdate = Url_new.VehicleListForUpdate;


    Spinner sp_vehicle;

    ArrayList<GetVehicleRoutes_List> arraylist = new ArrayList<GetVehicleRoutes_List>();
    ArrayList<GetVehicleRoutes_List_student> arraylist_student = new ArrayList<GetVehicleRoutes_List_student>();
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView, my_recycler_view_student;
    private HeroAdapter adapter;
    LinearLayout ll_studentdata;
    ImageView menu_ic;
    ExpandableRelativeLayout[] expandableLayout_Hiraracy;
    TextView bt_go;
    ArrayList<Samplemyclass> RoutesMaster_arraylist = new ArrayList<>();
    ArrayList<Samplemyclass> Pickup_arraylist = new ArrayList<>();
    Spinner sp_Route;
    String sp_Route_id, sp_Route_Name;
    HorizontalScrollView ll_horizantal_route;
    ArrayList<Samplemyclass> droplist = new ArrayList<>();
    String sp_pickup_ID, sp_pickup_Name;
    Spinner sp_pickup;
    String sp_droprouteD_id, sp_droprouteD_Name;
    Spinner sp_droprouteD;
    String sp_vehicle_VehicleID, sp_vehicle_VehicleName;
    ArrayList<Samplemyclass> arraylist_vehiclelist = new ArrayList<>();

    public Admin_Fragment() {
        // Required empty public constructor
    }


    public static Admin_Fragment newInstance(String param1, String param2) {
        Admin_Fragment fragment = new Admin_Fragment();
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


        view = inflater.inflate(R.layout.fragment_admin_, container, false);
        daily_weekly_button_view = view.findViewById(R.id.daily_weekly_button_view);
        TextView tv_fragmentName = (TextView) view.findViewById(R.id.tv_fragmentName);
        tv_fragmentName.setText("View");
        recyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        ll_studentdata = view.findViewById(R.id.ll_studentdata);
        my_recycler_view_student = view.findViewById(R.id.my_recycler_view_student);
        menu_ic = view.findViewById(R.id.menu_ic);
        sp_Route = view.findViewById(R.id.sp_Route);
        bt_go = view.findViewById(R.id.bt_go);
        sp_droprouteD = view.findViewById(R.id.sp_droprouteD);
        ll_horizantal_route = view.findViewById(R.id.ll_horizantal_route);
        sp_pickup = view.findViewById(R.id.sp_pickup);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        my_recycler_view_student.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        my_recycler_view_student.setLayoutManager(layoutManager);
        my_recycler_view_student.setItemAnimator(new DefaultItemAnimator());
        expandableLayout_Hiraracy = new ExpandableRelativeLayout[1];
        makeJsonObjReq_RouteList();
        widgetOnclick();


        return view;
    }

    private void spinnerData() {
        getoutes();
        pickpoints();
        getpickpoints(sp_pickup_Name, sp_Route_id);

    }

    private void pickpoints() {
        droplist.clear();
        droplist.add(new Samplemyclass("0", "Select"));
        droplist.add(new Samplemyclass("1", "PickPoints"));
        droplist.add(new Samplemyclass("2", "DropPoints"));

        ArrayAdapter<Samplemyclass> adapter = new ArrayAdapter<Samplemyclass>(getContext(), R.layout.spinner_item, droplist);
        sp_pickup.setAdapter(adapter);
        sp_pickup.setSelection(0);

        sp_pickup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (view != null) {

                }
                int pos = adapterView.getSelectedItemPosition();
                if (pos != 0) {

                    Samplemyclass country = (Samplemyclass) adapterView.getSelectedItem();

                    sp_pickup_ID = country.getId();
                    sp_pickup_Name = country.getName();
                    getpickpoints(sp_pickup_Name, sp_Route_id);

                } else {
                    sp_pickup_ID = "";
                    sp_pickup_Name = "";
                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }


    private void getpickpoints_Master(ArrayList<Samplemyclass> str1) {


        ArrayAdapter<Samplemyclass> adapter = new ArrayAdapter<Samplemyclass>(getActivity(),
                R.layout.spinner_item, str1);

        sp_droprouteD.setAdapter(adapter);
        Calendar cal = Calendar.getInstance();
        sp_droprouteD.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (view != null) {
                    ((TextView) adapterView.getChildAt(0)).setTextColor(Color.parseColor("#000000"));
                }
                int pos = adapterView.getSelectedItemPosition();
                if (pos != 0) {
                    ((TextView) adapterView.getChildAt(0)).setTextColor(Color.parseColor("#000000"));
                    Samplemyclass country = (Samplemyclass) adapterView.getSelectedItem();
                    sp_droprouteD_id = country.getId();
                    sp_droprouteD_Name = country.getName();

                    Log.e("sp_gpname_name", "sp_gpname_name" + sp_droprouteD_id);
                } else {
                    sp_droprouteD_id = "";
                    sp_droprouteD_Name = "";
                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void getpickpoints(String sp_pickup_Name, String pointId) {

        JSONObject jsonBody = new JSONObject();
        if (sp_pickup_Name == null) {
            sp_pickup_Name = "";
        }
        if (pointId == null) {
            pointId = "";
        }

        try {
            jsonBody.put("ActionName", "" + sp_pickup_Name);
            jsonBody.put("RouteID", "" + pointId);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        final String mRequestBody = jsonBody.toString();
        Log.e("VOLLEY", "PointsMaster" + mRequestBody);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, PointsMaster, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("VOLLEY", "PointsMaster" + response);
                try {
                    JSONObject object = new JSONObject(response);
                    String code = object.optString("Code");
                    String message = object.optString("Message");

                    Pickup_arraylist.clear();
                    if (code.equalsIgnoreCase("0")) {

                        JSONArray jArray = object.getJSONArray("PointData");
                        int number = jArray.length();
                        String num = Integer.toString(number);
                        if (number == 0) {
                            Toast.makeText(getActivity(), " No Data Found", Toast.LENGTH_LONG).show();
                        } else {

                            Samplemyclass wp0 = new Samplemyclass("0", "Select");
                            Pickup_arraylist.add(wp0);
                            for (int i = 0; i < number; i++) {

                                JSONObject json_data = jArray.getJSONObject(i);
                                String e_id = json_data.getString("PointID");
                                String e_n = json_data.getString("PointName");
                                Samplemyclass wp = new Samplemyclass(e_id, e_n);
                                Pickup_arraylist.add(wp);
                            }
                        }
                        if (Pickup_arraylist.size() > 0) {
                            getpickpoints_Master(Pickup_arraylist);
                        }


                    } else {
                        Samplemyclass wp0 = new Samplemyclass("0", "Select");
                        // Binds all strings into an array
                        Pickup_arraylist.add(wp0);
                        if (Pickup_arraylist.size() > 0) {
                            getpickpoints_Master(Pickup_arraylist);
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

    }


    private void getoutes_Master(ArrayList<Samplemyclass> str1) {


        ArrayAdapter<Samplemyclass> adapter = new ArrayAdapter<Samplemyclass>(getActivity(),
                R.layout.spinner_item, str1);

        sp_Route.setAdapter(adapter);

        sp_Route.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                int pos = adapterView.getSelectedItemPosition();
                if (pos != 0) {
                    ((TextView) adapterView.getChildAt(0)).setTextColor(Color.parseColor("#000000"));
                    Samplemyclass country = (Samplemyclass) adapterView.getSelectedItem();
                    sp_Route_id = country.getId();
                    sp_Route_Name = country.getName();
                    pickpoints();
                } else {


                    sp_Route_id = "";
                    sp_Route_Name = "";
                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void getoutes() {

        JSONObject jsonBody = new JSONObject();

        final String mRequestBody = jsonBody.toString();
        Log.e("VOLLEY", "RoutesMaster" + mRequestBody);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, RoutesMaster, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("VOLLEY", "RoutesMaster" + response);
                try {
                    JSONObject object = new JSONObject(response);
                    String code = object.optString("Code");
                    String message = object.optString("Message");
                    RoutesMaster_arraylist.clear();
                    if (code.equalsIgnoreCase("0")) {

                        JSONArray jArray = object.getJSONArray("RoutesData");
                        int number = jArray.length();
                        String num = Integer.toString(number);
                        if (number == 0) {
                            Toast.makeText(getActivity(), " No Data Found", Toast.LENGTH_LONG).show();
                        } else {

                            Samplemyclass wp0 = new Samplemyclass("0", "Select");
                            RoutesMaster_arraylist.add(wp0);
                            for (int i = 0; i < number; i++) {

                                JSONObject json_data = jArray.getJSONObject(i);
                                String e_id = json_data.getString("RouteCode");
                                String e_n = json_data.getString("RouteName");
                                Samplemyclass wp = new Samplemyclass(e_id, e_n);
                                RoutesMaster_arraylist.add(wp);
                            }
                        }
                        if (RoutesMaster_arraylist.size() > 0) {
                            getoutes_Master(RoutesMaster_arraylist);
                        }


                    } else {
                        Samplemyclass wp0 = new Samplemyclass("0", "Select");
                        // Binds all strings into an array
                        RoutesMaster_arraylist.add(wp0);
                        if (RoutesMaster_arraylist.size() > 0) {
                            getoutes_Master(RoutesMaster_arraylist);
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

    }

    private void widgetOnclick() {
        daily_weekly_button_view.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int selectedId = daily_weekly_button_view.getCheckedRadioButtonId();
                RadioButton radiolngButton = (RadioButton) view.findViewById(selectedId);


                if (radiolngButton.getText().toString().equalsIgnoreCase("Route Allocation List")) {
                    ll_studentdata.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    menu_ic.setVisibility(View.GONE);
                    ll_horizantal_route.setVisibility(View.VISIBLE);
//                    expandableLayout_Hiraracy[0].collapse();
                    makeJsonObjReq_RouteList();

                }
                if (radiolngButton.getText().toString().equalsIgnoreCase("Sudent List")) {
                    ll_studentdata.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    menu_ic.setVisibility(View.VISIBLE);
                    ll_horizantal_route.setVisibility(View.GONE);
                    makeJsonObjReq_RouteList_student();
                }

            }


        });

        menu_ic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandableLayout_Hiraracy[0] = (ExpandableRelativeLayout) view.findViewById(R.id.expandableLayout);
                expandableLayout_Hiraracy[0].toggle();
                spinnerData();
            }
        });
        bt_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if (sp_Route != null && sp_Route.getSelectedItem() != null) {
                    if (!(sp_Route.getSelectedItem().toString().trim() == "Select")) {

                        expandableLayout_Hiraracy[0].collapse();

                        makeJsonObjReq_RouteList_student();
                    } else {
                        Toast.makeText(getContext(), "Please Select Route", Toast.LENGTH_SHORT).show();

                    }
                } else {
                    Toast.makeText(getContext(), "Please Select Route", Toast.LENGTH_SHORT).show();

                }


            }
        });
    }

    private void makeJsonObjReq_RouteList_student() {

        try {

            progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage("Loading, Please Wait...");
            progressDialog.show();
            progressDialog.setCancelable(false);

            if (sp_Route_id == null) {
                sp_Route_id = "";
            }
            if (sp_pickup_Name == null) {
                sp_pickup_Name = "";
            }
            if (sp_droprouteD_id == null) {
                sp_droprouteD_id = "";
            }

            JSONObject jsonBody = new JSONObject();
            jsonBody.put("ActionName", "DataByPointID");
            jsonBody.put("RouteID", "" + sp_Route_id);
            jsonBody.put("PointType", "" + sp_pickup_ID);
            jsonBody.put("PointID", "" + sp_droprouteD_id);

            final String mRequestBody = jsonBody.toString();
            Log.i("VOLLEY", "Request" + mRequestBody);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Studentslist, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("VOLLEY", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        int code = Integer.parseInt(object.optString("Code").toString());
                        String message = object.optString("Message").toString();
                        if (code == 0) {
                            //    progressDialog.dismiss();
                            my_recycler_view_student.setVisibility(View.VISIBLE);
                            JSONArray jArray = object.getJSONArray("StudentData");
                            int number = jArray.length();

                            String num = Integer.toString(number);
                            if (number == 0) {
                                Toast.makeText(getContext(), " No Data Found", Toast.LENGTH_LONG).show();
                            } else {
                                arraylist_student.clear();
                                for (int i = 0; i < number; i++) {
                                    JSONObject json_data = jArray.getJSONObject(i);

                                    String StudentMasterID = json_data.getString("StudentMasterID").toString();
                                    String StudentName = json_data.getString("StudentName").toString();
                                    String StudentID = json_data.getString("StudentID").toString();
                                    String ClassName = json_data.getString("ClassName").toString();
                                    String ParentName = json_data.getString("ParentName").toString();
                                    String ParentMobile = json_data.getString("ParentMobile").toString();
                                    String ParentMobile2 = json_data.getString("ParentMobile2").toString();
                                    String RouteID = json_data.getString("RouteID").toString();
                                    String PickPointID = json_data.getString("PickPointID").toString();
                                    String DropPointID = json_data.getString("DropPointID").toString();

                                    String RouteName = json_data.getString("RouteName").toString();
                                    String PickVehicleNo = json_data.getString("PickVehicleNo").toString();
                                    String DropVehicleNo = json_data.getString("DropVehicleNo").toString();


                                    String PickName = json_data.getString("PickName").toString();

                                    String DropName = json_data.getString("DropName").toString();
                                    GetVehicleRoutes_List_student wp = new GetVehicleRoutes_List_student(StudentMasterID, StudentName, StudentID, ClassName, ParentName,
                                            ParentMobile, ParentMobile2, RouteID, PickPointID, DropPointID, PickName, DropName, RouteName, PickVehicleNo, DropVehicleNo);

                                    arraylist_student.add(wp);

                                }

                                progressDialog.dismiss();
                                HeroAdapter_student adapter = new HeroAdapter_student(arraylist_student);
                                my_recycler_view_student.setAdapter(adapter);

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
                                my_recycler_view_student.setVisibility(View.GONE);
                                arraylist_student.clear();
                                progressDialog.dismiss();
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
                    progressDialog.dismiss();
                    arraylist_student.clear();
                    my_recycler_view_student.setVisibility(View.GONE);
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

    private void makeJsonObjReq_RouteList() {

        try {

            progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage("Loading, Please Wait...");
            progressDialog.show();
            progressDialog.setCancelable(false);


            JSONObject jsonBody = new JSONObject();


            final String mRequestBody = jsonBody.toString();
            Log.i("VOLLEY", mRequestBody);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, GetRouteAssign, new Response.Listener<String>() {
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
                            JSONArray jArray = object.getJSONArray("RouteAssignData");
                            int number = jArray.length();

                            String num = Integer.toString(number);
                            if (number == 0) {
                                Toast.makeText(getContext(), " No Data Found", Toast.LENGTH_LONG).show();
                            } else {
                                arraylist.clear();
                                for (int i = 0; i < number; i++) {
                                    JSONObject json_data = jArray.getJSONObject(i);

                                    String RouteNumber = json_data.getString("RouteNumber").toString();
                                    String RouteType = json_data.getString("RouteType").toString();
                                    String RouteName = json_data.getString("RouteName").toString();
                                    String VehicleMasterID = json_data.getString("VehicleMasterID").toString();
                                    String Vehiclenumber = json_data.getString("Vehiclenumber").toString();
                                    String StartingPointName = json_data.getString("StartingPointName").toString();
                                    String LastStopPointID = json_data.getString("LastStopPointID").toString();
                                    String LastStopPointName = json_data.getString("LastStopPointName").toString();
                                    String PointsType = json_data.getString("PointsType").toString();
                                    String StopPoints = json_data.getString("StopPoints").toString();
                                    String RouteID = json_data.getString("RouteID").toString();


                                    GetVehicleRoutes_List wp = new GetVehicleRoutes_List(RouteNumber, RouteType, RouteName, VehicleMasterID, Vehiclenumber, StartingPointName,
                                            LastStopPointID, LastStopPointName, PointsType, StopPoints, RouteID);

                                    arraylist.add(wp);

                                }

                                progressDialog.dismiss();
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
                                recyclerView.setVisibility(View.GONE);
                                arraylist.clear();
                                progressDialog.dismiss();
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
                    progressDialog.dismiss();
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class HeroAdapter extends RecyclerView.Adapter<HeroAdapter.HeroViewHolder> implements Filterable {


        private List<GetVehicleRoutes_List> heroList;
        private Context context;
        private List<GetVehicleRoutes_List> exampleListFull;
        private int currentPosition;
        protected GoogleApiClient mGoogleApiClient;
        boolean isclickoverviewlay = true;
        NewFilter mfilter;

        public HeroAdapter(List<GetVehicleRoutes_List> heroList) {
            this.heroList = heroList;
            this.context = context;
            exampleListFull = new ArrayList<>(heroList);
            mfilter = new NewFilter(adapter);
        }

        @Override
        public HeroViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_layout_routelist, parent, false);
            return new HeroViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final HeroViewHolder holder, final int position) {
            final GetVehicleRoutes_List hero = heroList.get(position);
            holder.bind(hero, position);

            holder.tv_vehiclenumber.setText(hero.getVehiclenumber());
            holder.tv_RouteType.setText(hero.getRouteType());
            holder.tv_RouteDescription.setText(hero.getRouteName());
            holder.tv_Routenumber.setText(hero.getRouteNumber());
            holder.tv_vehiclenumber.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    holder.Alertdialog = new Dialog(getContext(), R.style.MyAlertDialogStyle);
                    holder.Alertdialog.setContentView(R.layout.viewlistdataalert_adinvehicleupdate);
                    sp_vehicle = holder.Alertdialog.findViewById(R.id.sp_vehicle);

                    holder.bt_submit = holder.Alertdialog.findViewById(R.id.bt_submit);

                    holder.bt_submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            update();
                        }

                        private void update() {


                            final RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                            final JSONObject jsonObject = new JSONObject();
                            try {
                                jsonObject.put("RouteID", hero.getRouteID());
                                jsonObject.put("RouteType", hero.getRouteType());
                                jsonObject.put("VehicleID", sp_vehicle_VehicleID);


                                Log.d("Request", String.valueOf(jsonObject));
                                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, UpdateVehicleAssigns, jsonObject, new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        Log.d("Response", String.valueOf(response));

                                        String code = response.optString("Code", "0");
                                        String message = response.optString("Message", "Success");
                                        if (code.equalsIgnoreCase("1")) {
                                            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                                            makeJsonObjReq_RouteList();
                                            holder.Alertdialog.dismiss();
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

                    });
                    getvehiclelist(hero.getVehicleMasterID());
                    holder.Alertdialog.show();


                    notifyDataSetChanged();


                }
            });
        }

        @Override
        public int getItemCount() {
            return heroList.size();
            // return heroList == null ? 0 : heroList.size();
        }

        @Override
        public Filter getFilter() {
            return exampleFilter;

        }

        private void VehicleList(ArrayList<Samplemyclass> str1) {

            ArrayAdapter<Samplemyclass> adapter = new ArrayAdapter<Samplemyclass>(getContext(),
                    R.layout.spinner_item, str1);

            sp_vehicle.setAdapter(adapter);

            sp_vehicle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    int pos = adapterView.getSelectedItemPosition();


                    if (pos != 0) {
                        Samplemyclass country = (Samplemyclass) adapterView.getSelectedItem();

                        sp_vehicle_VehicleID = country.getId();
                        sp_vehicle_VehicleName = country.getName();

                    } else {
                        sp_pickup_ID = "";
                        sp_pickup_Name = "";
                    }

                    Log.e("VOLLEY", "VehicleType_id" + sp_vehicle_VehicleID);

                }


                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

        }


        private void getvehiclelist(String vehicleid) {
            final RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            final JSONObject jsonObject = new JSONObject();


            try {


                JSONObject jsonBody = new JSONObject();
                jsonBody.put("VehicleId", vehicleid);


                Log.d("Request", String.valueOf(jsonObject));
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, VehicleListForUpdate, jsonBody, new Response.Listener<JSONObject>() {
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
                                            String id = jsonObject.getString("VehicleId".toString());
                                            String data = jsonObject.getString("VehicleName".toString());
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

        private Filter exampleFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<GetVehicleRoutes_List> filteredList = new ArrayList<>();

                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(exampleListFull);
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();

                    for (GetVehicleRoutes_List item : exampleListFull) {
                        if (item.getVehiclenumber().toLowerCase().contains(filterPattern)) {
                            filteredList.add(item);
                        }
                    }
                }

                FilterResults results = new FilterResults();
                results.values = filteredList;

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                heroList.clear();
                heroList.addAll((List) results.values);
                notifyDataSetChanged();
            }
        };

        class HeroViewHolder extends RecyclerView.ViewHolder {
            TextView tv_Routenumber, tv_RouteType, tv_RouteDescription, tv_vehiclenumber;
            Dialog Alertdialog;


            Button bt_submit;

            HeroViewHolder(final View itemView) {
                super(itemView);
                tv_vehiclenumber = itemView.findViewById(R.id.tv_vehiclenumber);
                tv_Routenumber = itemView.findViewById(R.id.tv_Routenumber);
                tv_RouteType = itemView.findViewById(R.id.tv_RouteType);
                tv_RouteDescription = itemView.findViewById(R.id.tv_RouteDescription);

            }


            private void bind(final GetVehicleRoutes_List movie, int position) {


                if (position == currentPosition) {


                } else {


                }


                if (position == currentPosition) {

                } else {

                }

            }


        }


        public class NewFilter extends Filter {
            public HeroAdapter mAdapter;

            public NewFilter(HeroAdapter mAdapter) {
                super();
                this.mAdapter = mAdapter;
            }


            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                arraylist.clear();
                final FilterResults results = new FilterResults();
                if (charSequence.length() == 0) {
                    arraylist.addAll(arraylist);
                } else {
                    final String filterPattern = charSequence.toString().toLowerCase().trim();
                    for (GetVehicleRoutes_List listcountry : arraylist) {
                        if (listcountry.getVehiclenumber().toLowerCase().startsWith(filterPattern)) {
                            arraylist.add(listcountry);
                        }
                    }
                }
                results.values = arraylist;
                results.count = arraylist.size();
                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                this.mAdapter.notifyDataSetChanged();
            }
        }
    }

    public class HeroAdapter_student extends RecyclerView.Adapter<HeroAdapter_student.HeroViewHolder_student> implements Filterable {


        private List<GetVehicleRoutes_List_student> heroList;
        private Context context;
        private List<GetVehicleRoutes_List_student> exampleListFull;
        private int currentPosition;
        protected GoogleApiClient mGoogleApiClient;
        boolean isclickoverviewlay = true;
        NewFilter mfilter;

        public HeroAdapter_student(List<GetVehicleRoutes_List_student> heroList) {
            this.heroList = heroList;
            this.context = context;
            exampleListFull = new ArrayList<>(heroList);
            mfilter = new NewFilter(adapter);
        }

        @Override
        public HeroViewHolder_student onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_layout_studentlist, parent, false);
            return new HeroViewHolder_student(v);
        }

        @Override
        public void onBindViewHolder(final HeroViewHolder_student holder, final int position) {
            final GetVehicleRoutes_List_student hero = heroList.get(position);

            holder.tv_SudentName.setText(hero.getStudentName());
            holder.tv_ClassName.setText(hero.getClassName());
            holder.tv_ParentName.setText(hero.getParentName());
            holder.tv_routeame.setText(hero.getRouteName());
            holder.tv_ViewMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.Alertdialog = new Dialog(getContext(), R.style.MyAlertDialogStyle);
                    holder.Alertdialog.setContentView(R.layout.viewlistdataalert_student);

                    ImageView iv_cancel = holder.Alertdialog.findViewById(R.id.iv_cancel);
                    TextView tv_parentmobile = (TextView) holder.Alertdialog.findViewById(R.id.tv_parentmobile);
                    TextView tv_routename = (TextView) holder.Alertdialog.findViewById(R.id.tv_routename);
                    TextView tv_pickup = (TextView) holder.Alertdialog.findViewById(R.id.tv_pickup);
                    TextView tv_pickupvehicleno = (TextView) holder.Alertdialog.findViewById(R.id.tv_pickupvehicleno);
                    TextView tv_droppoint = (TextView) holder.Alertdialog.findViewById(R.id.tv_droppoint);
                    TextView tv_droppointveh = (TextView) holder.Alertdialog.findViewById(R.id.tv_droppointveh);


                    String str1 = hero.getParentMobile();
                    String str2 = hero.getRouteName();
                    String str3 = hero.getPickName();
                    String str4 = hero.getPickVehicleNo();
                    String str5 = hero.getDropName();
                    String str6 = hero.getDropVehicleNo();

                    if (str1 != null) {
                        tv_parentmobile.setText("" + str1);

                    } else {
                        tv_parentmobile.setText("");

                    }

                    if (str2 != null) {
                        tv_routename.setText("" + str2);

                    } else {
                        tv_routename.setText("");

                    }
                    if (str3 != null) {
                        tv_pickup.setText("" + str3);

                    } else {
                        tv_pickup.setText("");

                    }
                    if (str4 != null) {
                        tv_pickupvehicleno.setText("" + str4);

                    } else {
                        tv_pickupvehicleno.setText("");

                    }
                    if (str5 != null) {
                        tv_droppoint.setText("" + str5);

                    } else {
                        tv_droppoint.setText("");

                    }
                    if (str6 != null) {
                        tv_droppointveh.setText("" + str6);

                    } else {
                        tv_droppointveh.setText("");

                    }
                    iv_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            holder.Alertdialog.dismiss();
                        }
                    });
                    holder.Alertdialog.show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return heroList.size();
            // return heroList == null ? 0 : heroList.size();
        }

        @Override
        public Filter getFilter() {
            return exampleFilter;

        }


        private Filter exampleFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<GetVehicleRoutes_List_student> filteredList = new ArrayList<>();

                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(exampleListFull);
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();

                    for (GetVehicleRoutes_List_student item : exampleListFull) {
                        if (item.getStudentName().toLowerCase().contains(filterPattern)) {
                            filteredList.add(item);
                        }
                    }
                }

                FilterResults results = new FilterResults();
                results.values = filteredList;

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                heroList.clear();
                heroList.addAll((List) results.values);
                notifyDataSetChanged();
            }
        };

        class HeroViewHolder_student extends RecyclerView.ViewHolder {
            TextView tv_SudentName, tv_ClassName, tv_ParentName, tv_ViewMore, tv_routeame;
            Dialog Alertdialog;

            HeroViewHolder_student(final View itemView) {
                super(itemView);
                tv_SudentName = itemView.findViewById(R.id.tv_SudentName);
                tv_ClassName = itemView.findViewById(R.id.tv_ClassName);
                tv_ParentName = itemView.findViewById(R.id.tv_ParentName);
                tv_ViewMore = itemView.findViewById(R.id.tv_ViewMore);

                tv_routeame = itemView.findViewById(R.id.tv_routeame);
            }


        }


        public class NewFilter extends Filter {
            public HeroAdapter mAdapter;

            public NewFilter(HeroAdapter mAdapter) {
                super();
                this.mAdapter = mAdapter;
            }


            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                arraylist.clear();
                final FilterResults results = new FilterResults();
                if (charSequence.length() == 0) {
                    arraylist.addAll(arraylist);
                } else {
                    final String filterPattern = charSequence.toString().toLowerCase().trim();
                    for (GetVehicleRoutes_List listcountry : arraylist) {
                        if (listcountry.getVehiclenumber().toLowerCase().startsWith(filterPattern)) {
                            arraylist.add(listcountry);
                        }
                    }
                }
                results.values = arraylist;
                results.count = arraylist.size();
                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                this.mAdapter.notifyDataSetChanged();
            }
        }
    }

}
