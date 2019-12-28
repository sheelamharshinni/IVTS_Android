package com.tecdatum.Tracking.School.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Html;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.tecdatum.Tracking.School.R;
import com.tecdatum.Tracking.School.newConstants.Url_new;
import com.tecdatum.Tracking.School.newactivities.Connectivity;
import com.tecdatum.Tracking.School.newhelpers.CustomDateTimePicker;
import com.tecdatum.Tracking.School.newhelpers.QvVehiclesList;
import com.tecdatum.Tracking.School.newhelpers.Samplemyclass;
import com.tecdatum.Tracking.School.volley.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.MODE_PRIVATE;
import static com.tecdatum.Tracking.School.fragments.MonthReportFragment.MY_PREFS_NAME;
import static com.tecdatum.Tracking.School.volley.MyApplication.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DashboardVehiclelist_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DashboardVehiclelist_Fragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View v;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    RadioButton rg_runing, rg_stop, rg_idle, rg_nosignal;
    TextView tv_back;
    RadioGroup daily_weekly_button_view;
    String Vehiclestatus, VehicleTpe;
    private String DashboardVehicleList1 = Url_new.DashboardVehicleList;

    private String url_LiveTracking = Url_new.LiveTracking;
    private String DashBoardChart = Url_new.DashBoardChart;
    private String HistoryTracking = Url_new.HistoryTracking;
    private String HistoryTrackInformation = Url_new.HistoryTrackInformation;
    private String url1 = Url_new.LiveTracking;

    ArrayList cl0, fixedtime;
    CustomDateTimePicker custom, custom1;
    String default_From_date_report, formattedDate;
    String Orgid, UserID, LevelId, HierarchyId;
    ProgressDialog progressDialog;
    ArrayList<QvVehiclesList> arraylist = new ArrayList<>();
    private HeroAdapter adapter;
    Marker marker, marker1, marker2, marker3;
    Calendar date_fm, date_to;
    ArrayList<LatLng> MarkerPoints = new ArrayList<>();
    double latt;
    double lngg;
    LatLng position;
    ArrayList<String> Locatn_time = new ArrayList<>();
    ArrayList<LatLng> Points = new ArrayList<>();
    private LocationRequest mLocationRequest;
    protected GoogleApiClient mGoogleApiClient;
    private static final int REQUEST_CHECK_SETTINGS = 0x01;
    private static final int READ_CONTACTS_PERMISSIONS_REQUEST = 50;
    private static final int READ_CAMERA_PERMISSIONS_REQUEST = 20;
    private static final int WRITE_SETTINGS_PERMISSION = 20;
    private boolean active = false;
    protected Location mLastLocation;
    private static int UPDATE_INTERVAL = 60000;
    String VehicleName, speed, lat, lng, loc, statuscolor, direction, S_sincefrom, ignition, liveedate, vehicletype, DriverName, MobNo, VehicleId;
    Double lattitude, Lonitude;
    LatLng sydney;
    PolylineOptions polyOptions;

    String a_Total_Distance;
    String a_VehicleNo, a_StartDateTime, a_StartLocation, a_EndDateTime, a_EndLocation, a_Distance, a_TravelTime, a_IdleTime, a_StopTime, message;
    String vehicle, default_From_date, default_To_date, Lo, DT, Dire, Ign, vehicleT, Current_Address, S_PassWord, IMEI, myDataFromActivity, Start_time, Stop_time, default_mode;
    ArrayList<String> xVals = new ArrayList<>();
    ArrayList<Entry> yVals = new ArrayList<>();
    ArrayList<String> arraylist_Time = new ArrayList<String>();
    ArrayList<String> arraylist_Distance = new ArrayList<String>();
    ArrayList<Integer> arraylist_Distance1 = new ArrayList<Integer>();
    ArrayList<Integer> arraylist_Time1 = new ArrayList<Integer>();
    public static ArrayList<String> x_axis = new ArrayList<String>();
    public static ArrayList<String> y_axis = new ArrayList<String>();

    private OnFragmentInteractionListener mListener;

    public DashboardVehiclelist_Fragment() {
        // Required empty public constructor
    }


    public static DashboardVehiclelist_Fragment newInstance(String param1, String param2) {
        DashboardVehiclelist_Fragment fragment = new DashboardVehiclelist_Fragment();
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
        v = inflater.inflate(R.layout.fragment_dashboard_vehiclelist, container, false);
        WidgetInitialization();
        WidgetoNcLICKS();
        return v;

    }

    private void WidgetoNcLICKS() {
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dashboard fr11 = new Dashboard();
                opennfrag(fr11);
            }
        });

    }

    private void WidgetInitialization() {

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
        tv_back = v.findViewById(R.id.tv_back);
        daily_weekly_button_view = v.findViewById(R.id.daily_weekly_button_view);
        final Bundle b = getArguments();

        if (b != null) {
            VehicleTpe = b.getString("TYPE");


            Vehiclestatus = b.getString("STATUS");

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
                    makeJsonObjReq(Vehiclestatus, "Car");

                }
                if (radiolngButton.getText().toString().equalsIgnoreCase("Stop")) {
                    Vehiclestatus = "Stop";
                    makeJsonObjReq(Vehiclestatus, "Car");
                }
                if (radiolngButton.getText().toString().equalsIgnoreCase("Idle")) {
                    Vehiclestatus = "Idle";
                    makeJsonObjReq(Vehiclestatus, "Car");
                }
                if (radiolngButton.getText().toString().equalsIgnoreCase("In Repair")) {
                    Vehiclestatus = "Nosignal";
                    makeJsonObjReq(Vehiclestatus, "Car");

                }
            }
        });
        CountDownTimer newtimer = new CountDownTimer(1000000000, 1000) {
            public void onTick(long millisUntilFinished) {

                SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MM-dd");
                Date from_date = new Date();
                //   long to_date = from_date.get() - 720 * 60 * 1000;
                //  Log.e("TEST", from_date.getTime() + " - " + to_date);
                default_From_date_report = simpledateformat.format(from_date);
                // default_To_date = simpledateformat.format(to_date);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date myDate = dateFormat.parse(default_From_date_report);
                    Calendar cal1 = Calendar.getInstance();
                    cal1.setTime(myDate);
                    cal1.add(Calendar.DAY_OF_YEAR, -1);
                    Date previousDate = cal1.getTime();
                    formattedDate = simpledateformat.format(previousDate);
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            public void onFinish() {
            }
        };
        newtimer.start();
    }

    private void makeJsonObjReq(String status, String VehicleTyp) {

        try {
            SharedPreferences bb = getContext().getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);

            UserID = bb.getString("UserID", "");
            Orgid = bb.getString("Orgid", "");
            LevelId = bb.getString("LevelId", "");
            HierarchyId = bb.getString("HierarchyId", "");

//            progressDialog = new ProgressDialog(getContext());
//            progressDialog.setMessage("Loading, Please Wait...");
//            progressDialog.show();
//            progressDialog.setCancelable(false);


            JSONObject jsonBody = new JSONObject();
            jsonBody.put("OrgId", Orgid);
            jsonBody.put("VehicleNo", "");
            jsonBody.put("UserId", UserID);
            jsonBody.put("VehicleType", VehicleTyp);
            jsonBody.put("Fromstatus", status);
            jsonBody.put("LevelId", LevelId);
            jsonBody.put("HierarchyId", HierarchyId);


            final String mRequestBody = jsonBody.toString();
            Log.i("VOLLEY", "DASHBOARDVEHICLE" + mRequestBody);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, DashboardVehicleList1, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("VOLLEY", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        int code = Integer.parseInt(object.optString("Code").toString());
                        String message = object.optString("Message").toString();
                        if (code == 0) {
                            //progressDialog.cancel();
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

                                    String vehicle_Id = json_data.getString("Id").toString();
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
                                    String date = Sincefrom;
                                    try {
                                        DateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
                                        DateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy hh:mm");
                                        Date oneWayTripDate = inputFormat.parse(date);
                                        Sincefrom = outputFormat.format(oneWayTripDate);// parse input
                                    } catch (Exception ex) {
                                        System.out.println("Date error");
                                    }

                                    QvVehiclesList wp = new QvVehiclesList(vehicle_Id, VehicleRegNo, Vehicle, Vehicletype, Sincefrom,
                                            Livedatetime, DriverName, MobileNo, VehicleStatus, Location, Latitude, Longitude, AcStatus, Gpsstatus, Speed, Ignition, Model);

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
                                recyclerView.setVisibility(View.GONE);
                                arraylist.clear();
                                //progressDialog.dismiss();
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
                    // progressDialog.dismiss();
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void opennfrag(final Fragment fragment) {

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.tabFrameLayout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }

    public class HeroAdapter extends RecyclerView.Adapter<HeroAdapter.HeroViewHolder> implements Filterable {


        private List<QvVehiclesList> heroList;
        private Context context;
        private List<QvVehiclesList> exampleListFull;
        private int currentPosition;
        protected GoogleApiClient mGoogleApiClient;
        boolean isclickoverviewlay = true;
        NewFilter mfilter;

        public HeroAdapter(List<QvVehiclesList> heroList) {
            this.heroList = heroList;
            this.context = context;
            exampleListFull = new ArrayList<>(heroList);
            mfilter = new NewFilter(adapter);
        }

        @Override
        public HeroViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_layout_heroes, parent, false);
            return new HeroViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final HeroViewHolder holder, final int position) {
            final QvVehiclesList hero = heroList.get(position);
            holder.bind(hero, position);


            holder.card_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentPosition = position;
                    boolean expanded = hero.isExpanded();
                    hero.setExpanded(!expanded);
                    notifyItemChanged(position);
                    notifyDataSetChanged();


                }
            });

            holder.rde_type.setChecked(true);

            holder.textViewName.setText(hero.getVehicle());
            holder.textViewVersion.setText(hero.getLocation());
            holder.sincefrom.setText(hero.getSincefrom());
            if (hero.getVehicleStatus().equals("Run")) {
                if (hero.getVehicletype().equalsIgnoreCase("Car")) {
                    holder.imageView.setImageResource(R.drawable.bus_running);
                } else {
                }

            }
            if (hero.getVehicleStatus().equals("Stop") | hero.getVehicleStatus().equals("stop")) {
                if (hero.getVehicletype().equalsIgnoreCase("Car")) {
                    holder.imageView.setImageResource(R.drawable.bus_stop);
                } else {
                }
            }
            if (hero.getVehicleStatus().equals("Idle")) {
                if (hero.getVehicletype().equalsIgnoreCase("Car")) {
                    holder.imageView.setImageResource(R.drawable.bus_idle);
                } else {
                }
            }

            if (hero.getVehicleStatus().equals("Nosignal") | hero.getVehicleStatus().equals("Nosignals") | hero.getVehicleStatus().equals("UnderFailure") | (hero.getVehicleStatus().equals("NoSignal"))) {
                if (hero.getVehicletype().equalsIgnoreCase("Car")) {
                    holder.imageView.setImageResource(R.drawable.bus_inrepair);
                } else {
                }
            }
            if (hero.getVehicleStatus().equals("") | hero.getVehicleStatus().equals("") | (hero.getVehicleStatus().equals(""))) {
                if (hero.getVehicletype().equalsIgnoreCase("Car")) {
                    holder.imageView.setImageResource(R.drawable.bus_inrepair);
                } else {
                }
            }


        }

        @Override
        public int getItemCount() {
            return heroList.size();
        }

        @Override
        public Filter getFilter() {
            return exampleFilter;

        }


        private Filter exampleFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<QvVehiclesList> filteredList = new ArrayList<>();

                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(exampleListFull);
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();

                    for (QvVehiclesList item : exampleListFull) {
                        if (item.getVehicleRegNo().toLowerCase().contains(filterPattern)) {
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

        class HeroViewHolder extends RecyclerView.ViewHolder implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
            TextView textViewName, textViewRealName, textViewTeam, textViewFirstAppearance,
                    textViewCreatedBy, textViewPublisher, textViewBio, textViewVersion, sincefrom;
            ImageView imageView, expand, collapse;
            LinearLayout linearLayout, ll_linearmaps;
            CardView card_view;
            RadioGroup rdo_type, rg_type;
            RadioButton rde_type, rdr_type, rdr_Livetracking;
            GoogleMap mapCurrent;
            MapView map;
            SupportMapFragment mapFragment;
            LinearLayout ll_vehiclesadapter;
            ProgressDialog pDialog;
            private LineChart mChart, mChart1;
            TextView tv_estimatetime, tv_ql_StartDatetime, tv_ql_StartLocation, tv_ql_EndDatetime, tv_ql_EndLocation, tv_ql_Distance,
                    tv_ql_TravelTime, tv_ql_IdleTime, tv_ql_StopTime;
            TextView tv_more, tv1_chart;
            Timer timer = new Timer();
            int period = 30000;
            Dialog Alertdialog;
            TextView tv_trackinfotext;
            boolean isclickoverviewlay = true;

            CardView cv_cardview;
            ImageView Track_info;
            Dialog C_dialog;
            ImageView btn_selectTimeDate;

            HeroViewHolder(final View itemView) {
                super(itemView);
                textViewVersion = itemView.findViewById(R.id.textViewVersion);
                textViewName = (TextView) itemView.findViewById(R.id.textViewName);
                btn_selectTimeDate = (ImageView) itemView.findViewById(R.id.select);
                imageView = (ImageView) itemView.findViewById(R.id.imageView);
                card_view = itemView.findViewById(R.id.card_view);
                collapse = (ImageView) itemView.findViewById(R.id.collapse);
                expand = (ImageView) itemView.findViewById(R.id.expand);
                linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout);
                tv1_chart = itemView.findViewById(R.id.tv1_chart);
                sincefrom = itemView.findViewById(R.id.sincefrom);
                rg_type = itemView.findViewById(R.id.rg_type);
                mChart = (LineChart) itemView.findViewById(R.id.linechart_distance);
                tv_more = itemView.findViewById(R.id.tv_more);
                rde_type = itemView.findViewById(R.id.rde_type);
                rdr_type = itemView.findViewById(R.id.rdr_type);
                rdr_Livetracking = itemView.findViewById(R.id.rdr_Livetracking);
                tv_estimatetime = (TextView) itemView.findViewById(R.id.tv_estimatetime);
                tv_ql_StartDatetime = (TextView) itemView.findViewById(R.id.tv_ql_StartDatetime);
                tv_ql_StartLocation = (TextView) itemView.findViewById(R.id.tv_ql_StartLocation);
                tv_ql_EndDatetime = (TextView) itemView.findViewById(R.id.tv_ql_EndDatetime);
                tv_ql_EndLocation = (TextView) itemView.findViewById(R.id.tv_ql_EndLocation);
                tv_ql_Distance = (TextView) itemView.findViewById(R.id.tv_ql_Distance);
                tv_ql_TravelTime = (TextView) itemView.findViewById(R.id.tv_ql_TravelTime);
                tv_ql_IdleTime = (TextView) itemView.findViewById(R.id.tv_ql_IdleTime);
                tv_ql_StopTime = (TextView) itemView.findViewById(R.id.tv_ql_StopTime);
                map = (MapView) itemView.findViewById(R.id.list_item_map_view_mapview);
                ll_vehiclesadapter = (LinearLayout) itemView.findViewById(R.id.ll_vehiclesadapter);
                Track_info = itemView.findViewById(R.id.Track_info);
                if (map != null) {
                    map.onCreate(null);
                    map.onResume();
                    map.getMapAsync(this);
                }
                fixedtime = new ArrayList<>();
                fixedtime.add(new Samplemyclass("0", "Select Duration"));
                fixedtime.add(new Samplemyclass("1", "1 Hour"));
                fixedtime.add(new Samplemyclass("2", "3 Hour"));
                fixedtime.add(new Samplemyclass("3", "Today"));
                fixedtime.add(new Samplemyclass("4", "Yesterday"));
                fixedtime.add(new Samplemyclass("5", "Set Duration"));
            }

            private void makeJsonObjReq_LiveTracking(String VehId) {
                try {


                    JSONObject jsonBody = new JSONObject();

                    jsonBody.put("VehicleId", VehId);
                    final String mRequestBody = jsonBody.toString();
                    Log.i("VOLLEY", "Livetracking" + mRequestBody);
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url_LiveTracking, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("VOLLEY", "Livetracking" + response);
                            try {
                                JSONObject object = new JSONObject(response);
                                int code = Integer.parseInt(object.optString("Code").toString());
                                String message = object.optString("Message").toString();
                                Log.i(" RESPONSE ", code + message);
                                //   Toast.makeText(getContext(), " RESPONSE "+ code+message, Toast.LENGTH_LONG).show();
                                if (code == 0) {
                                    SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                                    SharedPreferences.Editor edit = prefs.edit();
                                    edit.putString("query", "");
                                    JSONArray jArray = object.getJSONArray("LivevehicleDetails");
                                    int number = jArray.length();
                                    String num = Integer.toString(number);
                                    for (int i = 0; i < number; i++) {
                                        JSONObject json_data = jArray.getJSONObject(i);
                                        VehicleName = json_data.getString("VehicleName").toString();
                                        speed = json_data.getString("Speed").toString();
                                        lat = json_data.getString("Lattitude").toString();
                                        lng = json_data.getString("Longitude").toString();
                                        loc = json_data.getString("Location").toString();
                                        statuscolor = json_data.getString("fromStatus").toString();
                                        direction = json_data.getString("Direction").toString();
                                        S_sincefrom = json_data.getString("SinceFrom").toString();
                                        ignition = json_data.getString("Ignition").toString();
                                        liveedate = json_data.getString("LiveDate").toString();
                                        vehicletype = json_data.getString("VehicleType").toString();
                                        DriverName = json_data.getString("DriverName").toString();
                                        MobNo = json_data.getString("MobileNo").toString();
                                        VehicleId = json_data.getString("VehicleId").toString();

                                        lattitude = Double.parseDouble(lat.toString());
                                        Lonitude = Double.parseDouble(lng.toString());

                                    }
                                    sydney = new LatLng(lattitude, Lonitude);

                                    MarkerPoints.add(sydney);

                                    if (MarkerPoints.size() > 0) {
                                        for (int j = 0; j < MarkerPoints.size(); j++) {

                                            if (statuscolor.equals("Run")) {
                                                if (marker != null) {
                                                    marker.remove();
                                                }


                                                if (vehicletype.equalsIgnoreCase("Blue Colts")) {
                                                    marker = mapCurrent.addMarker(new MarkerOptions()
                                                            .position(MarkerPoints.get(j)).title(VehicleName)
                                                            .snippet(vehicletype).draggable(false)
                                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.mc_run)));
                                                }
                                                if (vehicletype.equalsIgnoreCase("M/C")) {
                                                    marker = mapCurrent.addMarker(new MarkerOptions()
                                                            .position(MarkerPoints.get(j)).title(VehicleName)
                                                            .snippet(vehicletype).draggable(false)
                                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.mc_run)));
                                                }
                                                if (vehicletype.equalsIgnoreCase("Patrol Mobiles")) {
                                                    marker = mapCurrent.addMarker(new MarkerOptions()
                                                            .position(MarkerPoints.get(j)).title(VehicleName)
                                                            .snippet(vehicletype).draggable(false)
                                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.bus_running)));
                                                }
                                                if (vehicletype.equalsIgnoreCase("Car")) {
                                                    marker = mapCurrent.addMarker(new MarkerOptions()
                                                            .position(MarkerPoints.get(j)).title(VehicleName)
                                                            .snippet(vehicletype).draggable(false)
                                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.bus_running)));
                                                }

                                            }
                                            if (statuscolor.equals("stop") | statuscolor.equals("Stop")) {
                                                if (marker != null) {
                                                    marker.remove();
                                                }
                                                if (vehicletype.equalsIgnoreCase("Blue Colts")) {
                                                    marker = mapCurrent.addMarker(new MarkerOptions()
                                                            .position(MarkerPoints.get(j)).title(VehicleName)
                                                            .snippet(vehicletype).draggable(false)
                                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.mc_stop)));
                                                }
                                                if (vehicletype.equalsIgnoreCase("M/C")) {
                                                    marker = mapCurrent.addMarker(new MarkerOptions()
                                                            .position(MarkerPoints.get(j)).title(VehicleName)
                                                            .snippet(vehicletype).draggable(false)
                                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.mc_stop)));
                                                }
                                                if (vehicletype.equalsIgnoreCase("Patrol Mobiles")) {
                                                    marker = mapCurrent.addMarker(new MarkerOptions()
                                                            .position(MarkerPoints.get(j)).title(VehicleName)
                                                            .snippet(vehicletype).draggable(false)
                                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.bus_stop)));
                                                }
                                                if (vehicletype.equalsIgnoreCase("Car")) {
                                                    marker = mapCurrent.addMarker(new MarkerOptions()
                                                            .position(MarkerPoints.get(j)).title(VehicleName)
                                                            .snippet(vehicletype).draggable(false)
                                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.bus_stop)));
                                                }
                                            }
                                            if (statuscolor.equals("Idle")) {
                                                if (marker != null) {
                                                    marker.remove();
                                                }


                                                if (vehicletype.equalsIgnoreCase("Blue Colts")) {
                                                    marker = mapCurrent.addMarker(new MarkerOptions()
                                                            .position(MarkerPoints.get(j)).title(VehicleName)
                                                            .snippet(vehicletype).draggable(false)
                                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.mc_idle)));
                                                }
                                                if (vehicletype.equalsIgnoreCase("M/C")) {
                                                    marker = mapCurrent.addMarker(new MarkerOptions()
                                                            .position(MarkerPoints.get(j)).title(VehicleName)
                                                            .snippet(vehicletype).draggable(false)
                                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.mc_idle)));
                                                }
                                                if (vehicletype.equalsIgnoreCase("Patrol Mobiles")) {
                                                    marker = mapCurrent.addMarker(new MarkerOptions()
                                                            .position(MarkerPoints.get(j)).title(VehicleName)
                                                            .snippet(vehicletype).draggable(false)
                                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.bus_idle)));
                                                }
                                                if (vehicletype.equalsIgnoreCase("Car")) {
                                                    marker = mapCurrent.addMarker(new MarkerOptions()
                                                            .position(MarkerPoints.get(j)).title(VehicleName)
                                                            .snippet(vehicletype).draggable(false)
                                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.bus_idle)));
                                                }
                                            }

                                            if (statuscolor.equals("NoSignal") | statuscolor.equals("Nosignals") | statuscolor.equals("Nosignal")) {

                                                if (marker != null) {
                                                    marker.remove();
                                                }
                                                if (vehicletype.equalsIgnoreCase("Blue Colts")) {
                                                    marker = mapCurrent.addMarker(new MarkerOptions()
                                                            .position(MarkerPoints.get(j)).title(VehicleName)
                                                            .snippet(vehicletype).draggable(false)
                                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.mc_nosignal)));
                                                }
                                                if (vehicletype.equalsIgnoreCase("M/C")) {
                                                    marker = mapCurrent.addMarker(new MarkerOptions()
                                                            .position(MarkerPoints.get(j)).title(VehicleName)
                                                            .snippet(vehicletype).draggable(false)
                                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.mc_nosignal)));
                                                }
                                                if (vehicletype.equalsIgnoreCase("Patrol Mobiles")) {
                                                    marker = mapCurrent.addMarker(new MarkerOptions()
                                                            .position(MarkerPoints.get(j)).title(VehicleName)
                                                            .snippet(vehicletype).draggable(false)
                                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.bus_inrepair)));
                                                }
                                                if (vehicletype.equalsIgnoreCase("Car")) {
                                                    marker = mapCurrent.addMarker(new MarkerOptions()
                                                            .position(MarkerPoints.get(j)).title(VehicleName)
                                                            .snippet(vehicletype).draggable(false)
                                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.bus_inrepair)));
                                                }
                                            }

                                            if (statuscolor.equals("") | statuscolor.equals("")) {
                                                if (marker != null) {
                                                    marker.remove();
                                                }
                                                if (vehicletype.equalsIgnoreCase("Blue Colts")) {
                                                    marker = mapCurrent.addMarker(new MarkerOptions()
                                                            .position(MarkerPoints.get(j)).title(VehicleName)
                                                            .snippet(vehicletype).draggable(false)
                                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.mc_nosignal)));
                                                }
                                                if (vehicletype.equalsIgnoreCase("M/C")) {
                                                    marker = mapCurrent.addMarker(new MarkerOptions()
                                                            .position(MarkerPoints.get(j)).title(VehicleName)
                                                            .snippet(vehicletype).draggable(false)
                                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.mc_nosignal)));
                                                }
                                                if (vehicletype.equalsIgnoreCase("Patrol Mobiles")) {
                                                    marker = mapCurrent.addMarker(new MarkerOptions()
                                                            .position(MarkerPoints.get(j)).title(VehicleName)
                                                            .snippet(vehicletype).draggable(false)
                                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.bus_inrepair)));
                                                }
                                                if (vehicletype.equalsIgnoreCase("Car")) {
                                                    marker = mapCurrent.addMarker(new MarkerOptions()
                                                            .position(MarkerPoints.get(j)).title(VehicleName)
                                                            .snippet(vehicletype).draggable(false)
                                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.bus_inrepair)));
                                                }
                                            }
                                        }
                                    }
                                    drawPolyLineOnMap(MarkerPoints);


                                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                                    for (LatLng latLng : MarkerPoints) {
                                        builder.include(sydney);
                                        builder.include(latLng);
                                    }
                                    int width = getResources().getDisplayMetrics().widthPixels;
                                    int height = getResources().getDisplayMetrics().heightPixels;
                                    int padding = (int) (width * 0.12);
                                    final LatLngBounds bounds = builder.build();
                                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
                                    mapCurrent.animateCamera(cu);
                                    CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(10).build();
                                    mapCurrent.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                                    // mapCurrent.addMarker(new MarkerOptions().position(sydney).title("Vehicle Information"));
                                    mapCurrent.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                                        @Override
                                        public View getInfoWindow(Marker arg0) {
                                            return null;
                                        }

                                        @Override
                                        public View getInfoContents(Marker arg0) {
                                            View v = getLayoutInflater().inflate(R.layout.info, null);

                                            String t1 = "<font color='#0000FF'>  Vehicle :</font>";
                                            String t2 = "<font color='#0000FF'>  Vehicle Type :</font>";
                                            String t4 = "<font color='#0000FF'>  Direction :</font>";
                                            String t5 = "<font color='#0000FF'>  Current Location :</font>";
                                            String t6 = "<font color='#0000FF'>  Ignition :</font>";
                                            String t7 = "<font color='#0000FF'>  Current Date And Time :</font>";
                                            String t8 = "<font color='#0000FF'>  Speed(KMPH) :</font>";
                                            String t9 = "<font color='#0000FF'>  Driver Name :</font>";
                                            String t10 = "<font color='#0000FF'>  Mobile No :</font>";
                                            String t11 = "<font color='#0000FF'>  Since From :</font>";
                                            String t12 = "<font color='#0000FF'>  Vehicle Status:</font>";
                                            TextView tvV = (TextView) v.findViewById(R.id.tv_vehicle);
                                            if (VehicleName != null) {
                                                tvV.setText(Html.fromHtml(t1 + VehicleName));
                                            }
                                            TextView tvVT = (TextView) v.findViewById(R.id.tv_vehicletype);
                                            if (vehicletype != null) {
                                                tvVT.setText(Html.fromHtml(t2 + vehicletype));
                                            }
                                            TextView tvsts = (TextView) v.findViewById(R.id.tv_vehiclestatus);
                                            if (statuscolor != null) {
                                                tvsts.setText(Html.fromHtml(t12 + statuscolor));
                                            }
                                            TextView tvDt = (TextView) v.findViewById(R.id.tv_dateandtime);
                                            if (S_sincefrom != null) {
                                                tvDt.setText(Html.fromHtml(t11 + S_sincefrom));
                                            }
                                            TextView tvLoc = (TextView) v.findViewById(R.id.tv_location);
                                            if (loc != null) {
                                                tvLoc.setText(Html.fromHtml(t5 + loc));
                                            }
                                            TextView tvSped = (TextView) v.findViewById(R.id.tv_speed);
                                            if (speed != null) {
                                                tvSped.setText(Html.fromHtml(t8 + speed));
                                            }
                                            TextView tvign = (TextView) v.findViewById(R.id.tv_ignition);
                                            if (ignition != null) {
                                                tvign.setText(Html.fromHtml(t6 + ignition));
                                            }
                                            TextView tvdirction = (TextView) v.findViewById(R.id.tv_direction);
                                            if (direction != null) {
                                                tvdirction.setText(Html.fromHtml(t4 + direction));
                                            }
                                            TextView tvsfm = (TextView) v.findViewById(R.id.tv_livedate);
                                            if (liveedate != null) {
                                                tvsfm.setText(Html.fromHtml(t7 + liveedate));
                                            }
                                            TextView tvdn = (TextView) v.findViewById(R.id.tv_drivername);
                                            if (DriverName != null) {
                                                tvdn.setText(Html.fromHtml(t9 + DriverName));
                                            }
                                            TextView tvmbno = (TextView) v.findViewById(R.id.tv_mobno);
                                            if (MobNo != null) {
                                                tvmbno.setText(Html.fromHtml(t10 + MobNo));
                                            }
                                            return v;
                                        }
                                    });
                                } else {
                                    // Toast.makeText(getContext(), " Error" + message, Toast.LENGTH_LONG).show();
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
                    VolleySingleton.getInstance().getRequestQueue().add(stringRequest);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            private void LiveTracking(String VehId) {
                try {


                    JSONObject jsonBody = new JSONObject();

                    jsonBody.put("VehicleId", "" + VehId);
                    final String mRequestBody = jsonBody.toString();

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url_LiveTracking, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("VOLLEY", response);
                            try {
                                JSONObject object = new JSONObject(response);
                                int code = Integer.parseInt(object.optString("Code").toString());
                                String message = object.optString("Message").toString();
                                Log.i("RESPONSE ", code + message);
                                //   Toast.makeText(getContext(), " RESPONSE "+ code+message, Toast.LENGTH_LONG).show();
                                if (mapCurrent != null) {
                                    mapCurrent.clear();
                                }
                                if (marker != null) {
                                    marker.remove();
                                }
                                MarkerPoints.clear();
                                if (code == 0) {


                                    JSONArray jArray = object.getJSONArray("LivevehicleDetails");
                                    int number = jArray.length();
                                    String num = Integer.toString(number);
                                    for (int i = 0; i < number; i++) {
                                        JSONObject json_data = jArray.getJSONObject(i);
                                        VehicleName = json_data.getString("VehicleName").toString();
                                        speed = json_data.getString("Speed").toString();
                                        lat = json_data.getString("Lattitude").toString();
                                        lng = json_data.getString("Longitude").toString();
                                        loc = json_data.getString("Location").toString();
                                        // statuscolor = json_data.getString("Statuscolor").toString();
                                        statuscolor = json_data.getString("fromStatus").toString();
                                        direction = json_data.getString("Direction").toString();
                                        S_sincefrom = json_data.getString("SinceFrom").toString();
                                        ignition = json_data.getString("Ignition").toString();
                                        liveedate = json_data.getString("LiveDate").toString();
                                        vehicletype = json_data.getString("VehicleType").toString();
                                        DriverName = json_data.getString("DriverName").toString();
                                        MobNo = json_data.getString("MobileNo").toString();
                                        VehicleId = json_data.getString("VehicleId").toString();

                                        lattitude = Double.parseDouble(lat.toString());
                                        Lonitude = Double.parseDouble(lng.toString());
                                        // Toast.makeText(getContext(), " Error" + lat +loc , Toast.LENGTH_LONG).show();
                                    }
                                    sydney = new LatLng(lattitude, Lonitude);
                                    //tv_vname.setText(VehicleName);


                                    MarkerPoints.add(sydney);

                                    //showOrHideInfoWindows(true);
                                    if (MarkerPoints.size() > 0) {
                                        for (int j = 0; j < MarkerPoints.size(); j++) {

                                            if (statuscolor.equals("Run")) {
                                                if (marker != null) {
                                                    marker.remove();
                                                }

                                                if (vehicletype.equalsIgnoreCase("Blue Colts")) {
                                                    marker = mapCurrent.addMarker(new MarkerOptions()
                                                            .position(MarkerPoints.get(j)).title(VehicleName)
                                                            .snippet(vehicletype).draggable(false)
                                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.mc_run)));
                                                    marker.hideInfoWindow();
                                                }
                                                if (vehicletype.equalsIgnoreCase("M/C")) {
                                                    marker = mapCurrent.addMarker(new MarkerOptions()
                                                            .position(MarkerPoints.get(j)).title(VehicleName)
                                                            .snippet(vehicletype).draggable(false)
                                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.mc_run)));
                                                    marker.hideInfoWindow();
                                                }
                                                if (vehicletype.equalsIgnoreCase("Tab")) {
                                                    marker = mapCurrent.addMarker(new MarkerOptions()
                                                            .position(MarkerPoints.get(j)).title(VehicleName)
                                                            .snippet(vehicletype).draggable(false)
                                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.mc_run)));
                                                    marker.hideInfoWindow();
                                                }
                                                if (vehicletype.equalsIgnoreCase("Car")) {
                                                    marker = mapCurrent.addMarker(new MarkerOptions()
                                                            .position(MarkerPoints.get(j)).title(VehicleName)
                                                            .snippet(vehicletype).draggable(false)
                                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.bus_running)));
                                                    marker.hideInfoWindow();


                                                }
                                                if (vehicletype.equalsIgnoreCase("Patrol Mobiles")) {
                                                    marker = mapCurrent.addMarker(new MarkerOptions()
                                                            .position(MarkerPoints.get(j)).title(VehicleName)
                                                            .snippet(vehicletype).draggable(false)
                                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.bus_running)));
                                                    marker.hideInfoWindow();
                                                    Log.d("VEHCICLEPOINT", "" + lattitude);
                                                    Log.d("VEHCICLEPOINT", "" + Lonitude);
                                                    Log.d("VEHCICLEPOINT", "" + sydney);
                                                }
                                            }
                                            if (statuscolor.equals("stop") | statuscolor.equals("Stop")) {
                                                if (marker != null) {
                                                    marker.remove();
                                                }
                                                if (vehicletype.equalsIgnoreCase("M/C")) {
                                                    marker = mapCurrent.addMarker(new MarkerOptions()
                                                            .position(MarkerPoints.get(j)).title(VehicleName)
                                                            .snippet(vehicletype).draggable(false)
                                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.mc_stop)));
                                                    marker.hideInfoWindow();
                                                }
                                                if (vehicletype.equalsIgnoreCase("Blue Colts")) {
                                                    marker = mapCurrent.addMarker(new MarkerOptions()
                                                            .position(MarkerPoints.get(j)).title(VehicleName)
                                                            .snippet(vehicletype).draggable(false)
                                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.mc_stop)));
                                                    marker.hideInfoWindow();
                                                }
                                                if (vehicletype.equalsIgnoreCase("Tab")) {
                                                    marker = mapCurrent.addMarker(new MarkerOptions()
                                                            .position(MarkerPoints.get(j)).title(VehicleName)
                                                            .snippet(vehicletype).draggable(false)
                                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.mc_run)));
                                                    marker.hideInfoWindow();
                                                }
                                                if (vehicletype.equalsIgnoreCase("Car")) {
                                                    marker = mapCurrent.addMarker(new MarkerOptions()
                                                            .position(MarkerPoints.get(j)).title(VehicleName)
                                                            .snippet(vehicletype).draggable(false)
                                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.bus_stop)));
                                                    marker.hideInfoWindow();
                                                }
                                                if (vehicletype.equalsIgnoreCase("Patrol Mobiles")) {
                                                    marker = mapCurrent.addMarker(new MarkerOptions()
                                                            .position(MarkerPoints.get(j)).title(VehicleName)
                                                            .snippet(vehicletype).draggable(false)
                                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.bus_stop)));
                                                    marker.hideInfoWindow();
                                                }
                                            }
                                            if (statuscolor.equals("Idle")) {
                                                if (marker != null) {
                                                    marker.remove();
                                                }
                                                if (vehicletype.equalsIgnoreCase("M/C")) {
                                                    marker = mapCurrent.addMarker(new MarkerOptions()
                                                            .position(MarkerPoints.get(j)).title(VehicleName)
                                                            .snippet(vehicletype).draggable(false)
                                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.mc_idle)));
                                                    marker.hideInfoWindow();
                                                }
                                                if (vehicletype.equalsIgnoreCase("Blue Colts")) {
                                                    marker = mapCurrent.addMarker(new MarkerOptions()
                                                            .position(MarkerPoints.get(j)).title(VehicleName)
                                                            .snippet(vehicletype).draggable(false)
                                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.mc_idle)));
                                                    marker.hideInfoWindow();
                                                }
                                                if (vehicletype.equalsIgnoreCase("Tab")) {
                                                    marker = mapCurrent.addMarker(new MarkerOptions()
                                                            .position(MarkerPoints.get(j)).title(VehicleName)
                                                            .snippet(vehicletype).draggable(false)
                                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.mc_run)));
                                                    marker.hideInfoWindow();
                                                }
                                                if (vehicletype.equalsIgnoreCase("Car")) {
                                                    marker = mapCurrent.addMarker(new MarkerOptions()
                                                            .position(MarkerPoints.get(j)).title(VehicleName)
                                                            .snippet(vehicletype).draggable(false)
                                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.bus_idle)));
                                                    marker.hideInfoWindow();
                                                }
                                                if (vehicletype.equalsIgnoreCase("Patrol Mobiles")) {
                                                    marker = mapCurrent.addMarker(new MarkerOptions()
                                                            .position(MarkerPoints.get(j)).title(VehicleName)
                                                            .snippet(vehicletype).draggable(false)
                                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.bus_idle)));
                                                    marker.hideInfoWindow();
                                                }
                                            }
                                            if (statuscolor.equals("NoSignal") | statuscolor.equals("Nosignal") | statuscolor.equals("Nosignals")) {
                                                if (marker != null) {
                                                    marker.remove();
                                                }
                                                if (vehicletype.equalsIgnoreCase("M/C")) {
                                                    marker = mapCurrent.addMarker(new MarkerOptions()
                                                            .position(MarkerPoints.get(j)).title(VehicleName)
                                                            .snippet(vehicletype).draggable(false)
                                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.mc_nosignal)));
                                                    marker.hideInfoWindow();
                                                }
                                                if (vehicletype.equalsIgnoreCase("Blue Colts")) {
                                                    marker = mapCurrent.addMarker(new MarkerOptions()
                                                            .position(MarkerPoints.get(j)).title(VehicleName)
                                                            .snippet(vehicletype).draggable(false)
                                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.mc_nosignal)));
                                                    marker.hideInfoWindow();
                                                }
                                                if (vehicletype.equalsIgnoreCase("Tab")) {
                                                    marker = mapCurrent.addMarker(new MarkerOptions()
                                                            .position(MarkerPoints.get(j)).title(VehicleName)
                                                            .snippet(vehicletype).draggable(false)
                                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.mc_run)));
                                                    marker.hideInfoWindow();
                                                }
                                                if (vehicletype.equalsIgnoreCase("Car")) {
                                                    marker = mapCurrent.addMarker(new MarkerOptions()
                                                            .position(MarkerPoints.get(j)).title(VehicleName)
                                                            .snippet(vehicletype).draggable(false)
                                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.bus_inrepair)));
                                                    marker.hideInfoWindow();
                                                }
                                                if (vehicletype.equalsIgnoreCase("Patrol Mobiles")) {
                                                    marker = mapCurrent.addMarker(new MarkerOptions()
                                                            .position(MarkerPoints.get(j)).title(VehicleName)
                                                            .snippet(vehicletype).draggable(false)
                                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.bus_inrepair)));
                                                    marker.hideInfoWindow();
                                                }
                                            }

                                            if (statuscolor.equals("") | statuscolor.equals("")) {
                                                if (marker != null) {
                                                    marker.remove();
                                                }
                                                if (vehicletype.equalsIgnoreCase("M/C")) {
                                                    marker = mapCurrent.addMarker(new MarkerOptions()
                                                            .position(MarkerPoints.get(j)).title(VehicleName)
                                                            .snippet(vehicletype).draggable(false)
                                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.mc_nosignal)));
                                                    marker.hideInfoWindow();
                                                }
                                                if (vehicletype.equalsIgnoreCase("Blue Colts")) {
                                                    marker = mapCurrent.addMarker(new MarkerOptions()
                                                            .position(MarkerPoints.get(j)).title(VehicleName)
                                                            .snippet(vehicletype).draggable(false)
                                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.mc_nosignal)));
                                                    marker.hideInfoWindow();
                                                }
                                                if (vehicletype.equalsIgnoreCase("Tab")) {
                                                    marker = mapCurrent.addMarker(new MarkerOptions()
                                                            .position(MarkerPoints.get(j)).title(VehicleName)
                                                            .snippet(vehicletype).draggable(false)
                                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.mc_run)));
                                                    marker.hideInfoWindow();
                                                }
                                                if (vehicletype.equalsIgnoreCase("Car")) {
                                                    marker = mapCurrent.addMarker(new MarkerOptions()
                                                            .position(MarkerPoints.get(j)).title(VehicleName)
                                                            .snippet(vehicletype).draggable(false)
                                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.bus_inrepair)));
                                                    marker.hideInfoWindow();
                                                }
                                                if (vehicletype.equalsIgnoreCase("Patrol Mobiles")) {
                                                    marker = mapCurrent.addMarker(new MarkerOptions()
                                                            .position(MarkerPoints.get(j)).title(VehicleName)
                                                            .snippet(vehicletype).draggable(false)
                                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.bus_inrepair)));
                                                    marker.hideInfoWindow();
                                                }
                                            }
                                        }
                                    }
                                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                                    for (LatLng latLng : MarkerPoints) {
                                        builder.include(sydney);
                                        builder.include(latLng);

                                    }
                                    CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(10).build();
                                    mapCurrent.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                                    // mapCurrent.addMarker(new MarkerOptions().position(sydney).title("Vehicle Information"));
                                    mapCurrent.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                                        @Override
                                        public View getInfoWindow(Marker arg0) {
                                            return null;
                                        }

                                        @Override
                                        public View getInfoContents(Marker arg0) {
                                            View v = getLayoutInflater().inflate(R.layout.info, null);

                                            String t1 = "<font color='#0000FF'>  Vehicle :</font>";
                                            String t2 = "<font color='#0000FF'>  Vehicle Type :</font>";
                                            String t4 = "<font color='#0000FF'>  Direction :</font>";
                                            String t5 = "<font color='#0000FF'>  Current Location :</font>";
                                            String t6 = "<font color='#0000FF'>  Ignition :</font>";
                                            String t7 = "<font color='#0000FF'>  Current Date And Time :</font>";
                                            String t8 = "<font color='#0000FF'>  Speed(KMPH) :</font>";
                                            String t9 = "<font color='#0000FF'>  Driver Name :</font>";
                                            String t10 = "<font color='#0000FF'>  Mobile No :</font>";
                                            String t11 = "<font color='#0000FF'>  Since From :</font>";
                                            String t12 = "<font color='#0000FF'>  Vehicle Status:</font>";
                                            TextView tvV = (TextView) v.findViewById(R.id.tv_vehicle);
                                            if (VehicleName != null) {
                                                tvV.setText(Html.fromHtml(t1 + VehicleName));
                                            }
                                            TextView tvVT = (TextView) v.findViewById(R.id.tv_vehicletype);
                                            if (vehicletype != null) {
                                                tvVT.setText(Html.fromHtml(t2 + vehicletype));
                                            }
                                            TextView tvsts = (TextView) v.findViewById(R.id.tv_vehiclestatus);
                                            if (statuscolor != null) {
                                                tvsts.setText(Html.fromHtml(t12 + statuscolor));
                                            }
                                            TextView tvDt = (TextView) v.findViewById(R.id.tv_dateandtime);
                                            if (S_sincefrom != null) {
                                                tvDt.setText(Html.fromHtml(t11 + S_sincefrom));
                                            }
                                            TextView tvLoc = (TextView) v.findViewById(R.id.tv_location);
                                            if (loc != null) {
                                                tvLoc.setText(Html.fromHtml(t5 + loc));
                                            }
                                            TextView tvSped = (TextView) v.findViewById(R.id.tv_speed);
                                            if (speed != null) {
                                                tvSped.setText(Html.fromHtml(t8 + speed));
                                            }
                                            TextView tvign = (TextView) v.findViewById(R.id.tv_ignition);
                                            if (ignition != null) {
                                                tvign.setText(Html.fromHtml(t6 + ignition));
                                            }
                                            TextView tvdirction = (TextView) v.findViewById(R.id.tv_direction);
                                            if (direction != null) {
                                                tvdirction.setText(Html.fromHtml(t4 + direction));
                                            }
                                            TextView tvsfm = (TextView) v.findViewById(R.id.tv_livedate);
                                            if (liveedate != null) {
                                                tvsfm.setText(Html.fromHtml(t7 + liveedate));
                                            }
                                            TextView tvdn = (TextView) v.findViewById(R.id.tv_drivername);
                                            if (DriverName != null) {
                                                tvdn.setText(Html.fromHtml(t9 + DriverName));
                                            }
                                            TextView tvmbno = (TextView) v.findViewById(R.id.tv_mobno);
                                            if (MobNo != null) {
                                                tvmbno.setText(Html.fromHtml(t10 + MobNo));
                                            }
                                            return v;
                                        }
                                    });
                                } else {
                                    // Toast.makeText(getContext(), " Error" + message, Toast.LENGTH_LONG).show();
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
                    VolleySingleton.getInstance().getRequestQueue().add(stringRequest);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            private void HistoryTracking(String veh_id) {
                //   makeJsonObjReq_Vehiclelist();

                pDialog = new ProgressDialog(getContext());
                pDialog.setMessage("Please Wait While Redirect..");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(true);
                pDialog.show();
                setdate();
                try {

                    JSONObject jsonBody = new JSONObject();


                    jsonBody.put("Mode", default_mode);
                    jsonBody.put("VehicleId", veh_id);
                    jsonBody.put("StartDate", default_From_date);
                    jsonBody.put("EndDate", default_To_date);
                    jsonBody.put("Duration", "5");
                    final String mRequestBody = jsonBody.toString();

                    Log.e(TAG, "request HistoryTracking" + mRequestBody);
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, HistoryTracking, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i(TAG, "" + "Response HistoryTracking" + response);
                            try {

                                JSONObject object = new JSONObject(response);
                                String code = object.optString("Code").toString();

                                if (code.equalsIgnoreCase("0")) {
                                    //  Track_info.setVisibility(View.VISIBLE);
                                    JSONArray jArray = object.getJSONArray("HistoryData");
                                    int number = jArray.length();
                                    String num = Integer.toString(number);
                                    if (number == 0) {
                                    } else {


                                        mapCurrent.clear();

                                        if (Points != null) {
                                            Points.clear();
                                        }
                                        for (int i = 0; i < number; i++) {
                                            JSONObject json_data = jArray.getJSONObject(i);
                                            a_VehicleNo = json_data.getString("Vehicle").toString();
                                            lat = json_data.getString("Latitude").toString();
                                            lng = json_data.getString("Longitude").toString();
                                            Lo = json_data.getString("Location").toString();
                                            DT = json_data.getString("Datetime").toString();
                                            Dire = json_data.getString("Speed").toString();
                                            Ign = json_data.getString("Ignition").toString();
                                            vehicleT = json_data.getString("VehicleType").toString();
                                            latt = Double.valueOf(lat);
                                            lngg = Double.valueOf(lng);
                                            position = new LatLng(latt, lngg);
                                            Locatn_time.add("" + DT);
                                            Points.add(position);
                                            mapCurrent.addMarker(new MarkerOptions()
                                                    .position(position).title(vehicle)
                                                    .snippet(Lo).icon(BitmapDescriptorFactory.fromResource(R.drawable.points)));

                                            //  .icon(BitmapDescriptorFactory.fromResource(R.drawable.points))).showInfoWindow();
                                        }
                                        pDialog.dismiss();
                                        showOrHideInfoWindows(false);
                                        drawPolyLineOnMap(Points);
                                        Log.i("history la", String.valueOf(Points));

                                        mapCurrent.addMarker(new MarkerOptions()
                                                .position(Points.get(0)).title("Start")
                                                .snippet(Locatn_time.get(0)).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_start)));

                                        // .icon(BitmapDescriptorFactory.fromResource(R.drawable.strtbtn))).showInfoWindow();

                                        mapCurrent.addMarker(new MarkerOptions()
                                                .position(Points.get(Points.size() - 1)).title("End")

                                                .snippet(Locatn_time.get(Points.size() - 1)).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_stop)));
                                        // .icon(BitmapDescriptorFactory.fromResource(R.drawable.stopbtn))).showInfoWindow();
                                    }
                                } else {
                                    mapCurrent.clear();
                                    pDialog.dismiss();
                                    //  Track_info.setVisibility(View.GONE);
                                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                                    alertDialog.setTitle("Alert");
                                    alertDialog.setMessage("" + message);
                                    alertDialog.setIcon(R.drawable.alert);
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
                            pDialog.dismiss();
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
                            25000000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    VolleySingleton.getInstance().getRequestQueue().add(stringRequest);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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

            public void onMapReady(GoogleMap googleMap) {
                //   MapsInitializer.initialize(getContext());
                mapCurrent = googleMap;

                mapCurrent = googleMap;
                mapCurrent.getUiSettings().setCompassEnabled(true);
                mapCurrent.getUiSettings().setMyLocationButtonEnabled(true);
                mapCurrent.getUiSettings().setRotateGesturesEnabled(true);
                mapCurrent.getUiSettings().setMapToolbarEnabled(false);
                mapCurrent.getUiSettings().setZoomControlsEnabled(true);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(getContext(),
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        buildGoogleApiClient();
                        mapCurrent.setMyLocationEnabled(true);
                    }
                } else {
                    buildGoogleApiClient();
                    mapCurrent.setMyLocationEnabled(true);
                }
                mapCurrent.animateCamera(CameraUpdateFactory.zoomTo(5));

                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
            }


            private void bind(final QvVehiclesList movie, int position) {
                boolean expanded = movie.isExpanded();


                if (position == currentPosition) {
//                    ll_vehiclesadapter.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.backrunning));
                    linearLayout.setVisibility(expanded ? View.VISIBLE : View.GONE);
                    expand.setVisibility(View.GONE);
                    collapse.setVisibility(View.VISIBLE);
                    LiveTracking(movie.getVehicle_Id());
                    graphCall(formattedDate, movie.getVehicle_Id());

                    Track_info.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialogue_Conform();
                        }
                    });
                    btn_selectTimeDate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            fromdata(movie.getVehicle_Id());
                        }
                    });

                    TrackingINFO(movie.getVehicle_Id(), movie.getVehicle());

                    rg_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                            int selectedId = rg_type.getCheckedRadioButtonId();
                            RadioButton radiolngButton = (RadioButton) v.findViewById(selectedId);


                            if (radiolngButton.getText().toString().equalsIgnoreCase("History Tracking")) {
                                Track_info.setVisibility(View.VISIBLE);
                                btn_selectTimeDate.setVisibility(View.VISIBLE);
                                HistoryTracking(movie.getVehicle_Id());
                            }
                            if (radiolngButton.getText().toString().equalsIgnoreCase("Live Status")) {

                                btn_selectTimeDate.setVisibility(View.GONE);
                                try {
                                    Log.e("cehckLta", "");
                                    timer.schedule(new TimerTask() {
                                        @Override
                                        public void run() {
                                            ConnectivityManager connec = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                                            if (connec != null && (
                                                    (connec.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) ||
                                                            (connec.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED))) {

                                                LiveTracking(movie.getVehicle_Id());
                                            } else if (connec != null && (
                                                    (connec.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.DISCONNECTED) ||
                                                            (connec.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.DISCONNECTED))) {
                                                FragmentManager fm = getActivity().getSupportFragmentManager();
                                                Connectivity td = new Connectivity();
                                                td.show(fm, "NO CONNECTION");
                                            }
                                        }
                                    }, 1, period);
                                } catch (Throwable e) {
                                    e.printStackTrace();
                                }
                            }

                            if (radiolngButton.getText().toString().equalsIgnoreCase("Live Tracking")) {
                                makeJsonObjReq_LiveTracking(movie.getVehicle_Id());
                                try {
                                    Log.e("cehckLta", "");
                                    timer.schedule(new TimerTask() {
                                        @Override
                                        public void run() {
                                            ConnectivityManager connec = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                                            if (connec != null && (
                                                    (connec.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) ||
                                                            (connec.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED))) {

                                                makeJsonObjReq_LiveTracking(movie.getVehicle_Id());
                                            } else if (connec != null && (
                                                    (connec.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.DISCONNECTED) ||
                                                            (connec.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.DISCONNECTED))) {
                                                FragmentManager fm = getActivity().getSupportFragmentManager();
                                                Connectivity td = new Connectivity();
                                                td.show(fm, "NO CONNECTION");
                                            }
                                        }
                                    }, 1, period);
                                } catch (Throwable e) {
                                    e.printStackTrace();
                                }

                            }

                        }
                    });
                    tv_more.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Alertdialog = new Dialog(getContext(), R.style.MyAlertDialogStyle);
                            Alertdialog.setContentView(R.layout.viewlistdataalert);

                            ImageView iv_cancel = Alertdialog.findViewById(R.id.iv_cancel);
                            TextView ql_vnam = (TextView) Alertdialog.findViewById(R.id.tv_ql_name);
                            TextView ql_vt = (TextView) Alertdialog.findViewById(R.id.tv_ql_vt);
                            TextView ql_sf = (TextView) Alertdialog.findViewById(R.id.tv_ql_sf);
                            TextView ql_st = (TextView) Alertdialog.findViewById(R.id.tv_ql_st);
                            TextView ql_speed = (TextView) Alertdialog.findViewById(R.id.tv_ql_speed);
                            TextView ql_ac = (TextView) Alertdialog.findViewById(R.id.tv_ql_ac);
                            TextView ql_gps = (TextView) Alertdialog.findViewById(R.id.tv_ql_gps);
                            TextView ql_loc = (TextView) Alertdialog.findViewById(R.id.tv_ql_loc);
                            TextView ql_dn = (TextView) Alertdialog.findViewById(R.id.tv_ql_dn);
                            TextView ql_dph = (TextView) Alertdialog.findViewById(R.id.tv_ql_dph);
                            String str1 = movie.getVehicle();
                            String str2 = movie.getVehicle();
                            String str3 = movie.getVehicletype();
                            String str4 = movie.getLocation();
                            String str5 = movie.getSincefrom();
                            String str6 = movie.getVehicleStatus();
                            String str7 = movie.getSpeed();
                            String str8 = movie.getAcStatus();
                            String str9 = movie.getGpsstatus();
                            String str10 = movie.getDriverName();
                            String str11 = movie.getMobileNo();

                            if (str2 != null) {
                                ql_vnam.setText("" + str2);

                            } else {
                                ql_vnam.setText("");

                            }
                            if (str3 != null) {
                                ql_vt.setText("" + str3);

                            } else {
                                ql_vt.setText("");

                            }
                            if (str5 != null) {
                                ql_sf.setText("" + str5);

                            } else {
                                ql_sf.setText("");

                            }
                            if (str6 != null) {
                                ql_st.setText("" + str6);

                            } else {
                                ql_st.setText("");

                            }
                            if (str7 != null) {
                                ql_speed.setText("" + str7);

                            } else {
                                ql_speed.setText("");

                            }
                            if (str9 != null) {
                                ql_gps.setText("" + str9);

                            } else {
                                ql_gps.setText("");

                            }
                            if (str8 != null) {
                                ql_ac.setText("" + str8);

                            } else {
                                ql_ac.setText("");

                            }
                            if (str4 != null) {
                                ql_loc.setText("" + str4);

                            } else {
                                ql_loc.setText("");

                            }
                            if (str10 != null) {
                                ql_dn.setText("" + str10);

                            } else {
                                ql_dn.setText("");

                            }
                            if (str11 != null) {
                                ql_dph.setText("" + str11);

                            } else {
                                ql_dph.setText("");

                            }
                            iv_cancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Alertdialog.dismiss();
                                }
                            });
                            Alertdialog.show();


                        }
                    });

                    Button btn_cctv_Confm = (Button) itemView.findViewById(R.id.btn_cctv_Confm);
                    btn_cctv_Confm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            MonthDayReport_Fragment fragment = new MonthDayReport_Fragment();
                            Bundle args = new Bundle();
                            args.putString("Vehicle_ID", (movie.getVehicle_Id()));
                            Log.d("VEHICLEID", movie.getVehicle_Id());
                            args.putString("Vehicle_Name", (movie.getVehicle()));
                            fragment.setArguments(args);
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.tabFrameLayout, fragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        }
                    });

                } else {
                    if (mapCurrent != null) {
                        mapCurrent.clear();
                    }

                    linearLayout.setVisibility(View.GONE);
                    expand.setVisibility(View.VISIBLE);
                    collapse.setVisibility(View.GONE);
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
//                        ll_vehiclesadapter.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.backdashboard));
                    }
                } else {
                    expand.setVisibility(View.VISIBLE);
                    collapse.setVisibility(View.GONE);
                    tv_more.setVisibility(View.GONE);
//                    ll_vehiclesadapter.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.backdashboard));
                }

            }

            private void fromdata(final String VehicleId) {
                // Toast.makeText(getApplicationContext(),"hello",Toast.LENGTH_LONG).show();
                AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                //  alertDialog.setTitle("Select Date and time");
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View dialogView = inflater.inflate(R.layout.dialog_datetime, null);
                alertDialog.setView(dialogView);
                final TextView tv = new TextView(getContext());
                final EditText fm = new EditText(getContext());
                final EditText toT = new EditText(getContext());
                final Spinner spnr = new Spinner(getContext());
                final Spinner spnr1 = new Spinner(getContext());

                fm.setInputType(InputType.TYPE_NULL);
                toT.setInputType(InputType.TYPE_NULL);
                tv.setText("Select Date");
                // tv.setGravity(0);

                tv.setGravity(Gravity.CENTER);
                tv.setTextSize(19f);
                tv.setTextColor(Color.BLACK);

                fm.setText(default_From_date);
                toT.setText(default_To_date);
                ArrayAdapter<Samplemyclass> adapter1 = new ArrayAdapter<Samplemyclass>
                        (getContext(), android.R.layout.simple_spinner_dropdown_item, fixedtime);
                spnr1.setAdapter(adapter1);
                spnr1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        ((TextView) parent.getChildAt(0)).setTextColor(Color.parseColor("#1a2164"));
                        int pos = parent.getSelectedItemPosition();
                        if (pos != 0) {
                            fixedtime.add(new Samplemyclass("0", "Select Duration"));
                            fixedtime.add(new Samplemyclass("1", "1 Hour"));
                            fixedtime.add(new Samplemyclass("2", "3 Hour"));
                            fixedtime.add(new Samplemyclass("3", "Today"));
                            fixedtime.add(new Samplemyclass("4", "Yesterday"));
                            fixedtime.add(new Samplemyclass("5", "Set Duration"));
                            if (pos != 0) {
                                if (pos == 1) {
                                    SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                    Date from_date = new Date();
                                    long to_date = from_date.getTime() - 60 * 60 * 1000;
                                    Log.e("TEST", from_date.getTime() + " - " + to_date);
                                    default_From_date = simpledateformat.format(to_date);
                                    default_To_date = simpledateformat.format(from_date);
                                    default_mode = "Both";

                                    Log.e("from", default_From_date);
                                    Log.e("to", default_To_date);
                                }
                                if (pos == 2) {
                                    SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                    Date from_date = new Date();


                                    Calendar calendar = Calendar.getInstance();
                                    calendar.setTime(from_date);
                                    calendar.set(Calendar.HOUR_OF_DAY, 23);
                                    calendar.set(Calendar.MINUTE, 59);
                                    calendar.set(Calendar.SECOND, 59);
                                    calendar.set(Calendar.MILLISECOND, 999);

                                    Log.e("calendar", "" + calendar.getTime());

                                    Calendar calendar1 = Calendar.getInstance();
                                    calendar1.setTime(from_date);
                                    calendar1.set(Calendar.HOUR_OF_DAY, 0);
                                    calendar1.set(Calendar.MINUTE, 0);
                                    calendar1.set(Calendar.SECOND, 0);
                                    calendar1.set(Calendar.MILLISECOND, 0);
                                    Log.e("calendar", "" + calendar1.getTime());
                                }
                                if (pos == 3) {
                                    SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                    Date from_date = new Date();
                                    long to_date = from_date.getTime() - 60 * 60 * 1000;
                                    Log.e("TEST", from_date.getTime() + " - " + to_date);
                                    default_From_date = simpledateformat.format(to_date);
                                    default_To_date = simpledateformat.format(from_date);
                                    default_mode = "Both";

                                    Log.e("from", default_From_date);
                                    Log.e("to", default_To_date);
                                }
                                if (pos == 4) {
                                    SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                    Date from_date = new Date();
                                    long to_date = from_date.getTime() - 60 * 60 * 1000;
                                    Log.e("TEST", from_date.getTime() + " - " + to_date);
                                    default_From_date = simpledateformat.format(to_date);
                                    default_To_date = simpledateformat.format(from_date);
                                    default_mode = "Both";
                                    Log.e("from", default_From_date);
                                    Log.e("to", default_To_date);
                                }
                                if (pos == 5) {
                                    ((TextView) parent.getChildAt(0)).setTextColor(Color.parseColor("#192060"));
                                    default_mode = parent.getItemAtPosition(position).toString();
                                    // Toast.makeText(getContext(), "You are "+default_mode, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

                Start_time = fm.getText().toString();
                Stop_time = toT.getText().toString();


                Log.e("Start_time", Start_time);
                Log.e("Stop_time", Stop_time);

                spnr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        ((TextView) parent.getChildAt(0)).setTextColor(Color.parseColor("#1a2164"));
                        int pos = parent.getSelectedItemPosition();
                        if (pos != 0) {
                            ((TextView) parent.getChildAt(0)).setTextColor(Color.parseColor("#192060"));
                            default_mode = parent.getItemAtPosition(position).toString();
                            // Toast.makeText(getContext(), "You are "+default_mode, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

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
                                       );
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
                                        );
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


                                                        SimpleDateFormat      df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
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

                                        Log.v(TAG, "The To Date " + df.format(date_to.getTime()));

                                    }
                                }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();
                            }
                        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE));
                        DatePickerDialog2.getDatePicker().setMaxDate(System.currentTimeMillis());
                        DatePickerDialog2.show();

                    }
                });

                LinearLayout ll = new LinearLayout(getContext());
                ll.setOrientation(LinearLayout.VERTICAL);
                ll.addView(tv);
                ll.addView(fm);
                ll.addView(toT);

                alertDialog.setView(ll);
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Submit",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                default_From_date = fm.getText().toString();
                                default_To_date = toT.getText().toString();
                                Log.e("changed_from", default_From_date);
                                Log.e("changed_to", default_To_date);
                                Log.e("changed_mode", default_mode);
                                //  makeJsonObjReq();
                                HistoryTracking(VehicleId);
                                makeJsonObjReq1(VehicleId);
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

            private void makeJsonObjReq1(String VehicleID) {
                try {

                    JSONObject jsonBody = new JSONObject();

                    jsonBody.put("VehicleId", VehicleID);
                    final String mRequestBody = jsonBody.toString();

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url1, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("VOLLEY", "TabActivity" + response);
                            try {
                                JSONObject object = new JSONObject(response);
                                int code = Integer.parseInt(object.optString("Code").toString());
                                String message = object.optString("Message").toString();
                                Log.i(" RESPONSE ", code + message);
                                //   Toast.makeText(getContext(), " RESPONSE "+ code+message, Toast.LENGTH_LONG).show();
                                if (code == 0) {
//
                                    JSONArray jArray = object.getJSONArray("LivevehicleDetails");
                                    int number = jArray.length();
                                    String num = Integer.toString(number);
                                    for (int i = 0; i < number; i++) {
                                        JSONObject json_data = jArray.getJSONObject(i);
                                        String VehicleName = json_data.getString("VehicleName").toString();

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

                tv_estimatetime.setText("From-" + "" + default_From_date + "\t" + "To-" + "" + default_To_date);

                try {
                    JSONObject jsonBody = new JSONObject();


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

            @Override
            public void onConnected(Bundle bundle) {

                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                        mGoogleApiClient);
                mapCurrent.setMyLocationEnabled(true);
                if (mLastLocation != null) {
                    lattitude = mLastLocation.getLatitude();
                    Lonitude = mLastLocation.getLongitude();
                    String p_lat = String.valueOf(lattitude);
                    String p_longi = String.valueOf(Lonitude);
                    Log.d(TAG, "activity location" + "latlong" + lattitude + Lonitude);
                    Log.d(TAG, "String location" + "latlong" + p_lat + p_longi);
                    LatLng ll = new LatLng(lattitude, Lonitude);
                    marker = mapCurrent.addMarker(new MarkerOptions()
                            .position(ll).title("Current Location")
                            .draggable(true).icon(BitmapDescriptorFactory
                                    .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
                    marker.hideInfoWindow();
                    //makeJsonObjReq(p_lat, p_longi,"2");
                } else {
                    Log.d(TAG, "No Latlong found");
                }
            }

            @Override
            public void onConnectionSuspended(int i) {
            }

            @Override
            public void onConnectionFailed(ConnectionResult connectionResult) {
            }


            protected synchronized void buildGoogleApiClient() {
                mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this)
                        .addApi(LocationServices.API)
                        .build();
                mGoogleApiClient.connect();
            }

            public void FineLocationPermission() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(
                                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                                //settingsDialog();
                                Toast.makeText(getContext(), "Grant Permission for Accessing your location and provide best search results", Toast.LENGTH_LONG).show();
                                Log.d(TAG, "Grant Permission for Accessing your location and provide best search results");
                                // Snackbar.make(findViewById(android.R.id.content), "Grant Permission for Accessing your location and provide best search results", Snackbar.LENGTH_INDEFINITE).show();
                            }
                        }
                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                READ_CONTACTS_PERMISSIONS_REQUEST);
                    } else {
                        settingsDialog();
                    }

                } else {
                    settingsDialog();
                }
            }

            private void settingsDialog() {
                buildGoogleApiClient();
                mLocationRequest = new LocationRequest();
                mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                mLocationRequest.setInterval(UPDATE_INTERVAL);
                mLocationRequest.setFastestInterval(UPDATE_INTERVAL);
                LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                        .addLocationRequest(mLocationRequest);
                builder.setAlwaysShow(true); //this is the key ingredient
                PendingResult<LocationSettingsResult> result =
                        LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());

                result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                    @Override
                    public void onResult(LocationSettingsResult result) {
                        final Status status = result.getStatus();
                        final LocationSettingsStates States = result.getLocationSettingsStates();
                        switch (status.getStatusCode()) {
                            case LocationSettingsStatusCodes.SUCCESS:
                                break;
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                try {
                                    status.startResolutionForResult(
                                            getActivity(),
                                            REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException e) {
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                break;
                        }
                    }
                });
            }

            private void showOrHideInfoWindows(boolean shouldShow) {

                if (shouldShow)
                    marker.showInfoWindow();
                else
                    marker.hideInfoWindow();

            }

            public void drawPolyLineOnMap(List<LatLng> list) {
                Log.e("histry", String.valueOf(list));
                polyOptions = new PolylineOptions();
                polyOptions.color(Color.BLUE);
                polyOptions.width(5);
                polyOptions.addAll(list);
                polyOptions.geodesic(true);
                mapCurrent.addPolyline(polyOptions);

                //    pDialog.dismiss();
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                for (LatLng latLng : list) {
                    builder.include(latLng);
                }
                final LatLngBounds bounds = builder.build();
                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 80);
                mapCurrent.animateCamera(cu);
            }

            private void graphCall(String Date, String vehicleid) {
                mChart.setNoDataTextDescription("or" + "\n" + "Loading Chart Data");
                try {


                    JSONObject jsonBody = new JSONObject();
                    jsonBody.put("VehicleId", vehicleid);
                    jsonBody.put("Date", Date);


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


            private ArrayList<String> setXAxisValues_Speed() {
                ArrayList<String> xVals = new ArrayList<String>();
                xVals.add("1");
                xVals.add("2");
                xVals.add("3");
                xVals.add("3");
                xVals.add("4");
                xVals.add("5");
                xVals.add("6");
                xVals.add("7");
                xVals.add("8");
                xVals.add("9");
                xVals.add("10");
                return xVals;
            }

            private ArrayList<Entry> setYAxisValues_Speed() {
                ArrayList<Entry> yVals = new ArrayList<Entry>();
                yVals.add(new Entry(0, 0));
                yVals.add(new Entry(18, 1));
                yVals.add(new Entry(37.5f, 2));
                yVals.add(new Entry(56, 3));
                yVals.add(new Entry(75.9f, 4));
                yVals.add(new Entry(64, 5));
                yVals.add(new Entry(16, 6));
                yVals.add(new Entry(22.5f, 7));
                yVals.add(new Entry(18, 8));
                yVals.add(new Entry(58.9f, 9));
                yVals.add(new Entry(10.9f, 10));
                return yVals;
            }

            private void alertDialogue_Conform() {
                C_dialog = new Dialog(getActivity(), R.style.MyAlertDialogStyle);
                C_dialog.setContentView(R.layout.track_info);
                C_dialog.show();

                tv_ql_StartDatetime = (TextView) C_dialog.findViewById(R.id.tv_ql_StartDatetime);
                tv_ql_StartLocation = (TextView) C_dialog.findViewById(R.id.tv_ql_StartLocation);
                tv_ql_EndDatetime = (TextView) C_dialog.findViewById(R.id.tv_ql_EndDatetime);
                tv_ql_EndLocation = (TextView) C_dialog.findViewById(R.id.tv_ql_EndLocation);
                tv_ql_Distance = (TextView) C_dialog.findViewById(R.id.tv_ql_Distance);
                tv_ql_TravelTime = (TextView) C_dialog.findViewById(R.id.tv_ql_TravelTime);
                tv_ql_IdleTime = (TextView) C_dialog.findViewById(R.id.tv_ql_IdleTime);
                tv_ql_StopTime = (TextView) C_dialog.findViewById(R.id.tv_ql_StopTime);
                tv_trackinfotext = (TextView) C_dialog.findViewById(R.id.tv_trackinfotext);
                tv_trackinfotext.setText("  Track Information");
                Button btn_cctv_Confm = (Button) C_dialog.findViewById(R.id.btn_cctv_Confm);
                btn_cctv_Confm.setVisibility(View.GONE);


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

                if (a_Distance == null) {
                    tv_ql_Distance.setText("");
                } else {
                    tv_ql_Distance.setText("" + a_Distance);
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
                Button C_confm = (Button) C_dialog.findViewById(R.id.btn_cctv_Confm);

                C_confm.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        C_dialog.dismiss();
                    }
                });


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
                    for (QvVehiclesList listcountry : arraylist) {
                        if (listcountry.getVehicle().toLowerCase().startsWith(filterPattern)) {
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
