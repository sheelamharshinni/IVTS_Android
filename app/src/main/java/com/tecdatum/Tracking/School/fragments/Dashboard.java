package com.tecdatum.Tracking.School.fragments;


import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.formatter.XAxisValueFormatter;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.tecdatum.Tracking.School.R;
import com.tecdatum.Tracking.School.newConstants.Url_new;
import com.tecdatum.Tracking.School.volley.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import static android.content.Context.MODE_PRIVATE;
import static com.tecdatum.Tracking.School.newactivities.SplashScreen.MY_PREFS_NAME;

public class Dashboard extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View v;
    TextView tv_fragmentName, tv_totalhehicles;
    TextView tv_runningvehicles_bus, tv_stopvehicles_bus, tv_idlevehicles_bus, tv_nosignalvehicles_bus;
    CardView bankcardId_bus, cardciew_Stop_bus, cardciew_Idle_bus, cardciew_Inrepair_bus;
    BarChart mChart_bar_bus;
    private String Homedashboard = Url_new.Homedashboard;
    String ParentMobile;
    String Srun_bus, Sstop_bus, Sidle_bus, Suf_bus;
    public static ArrayList<Integer> y_axis_bar = new ArrayList<Integer>();
    private OnFragmentInteractionListener mListener;

    public Dashboard() {
        // Required empty public constructor
    }


    public static Dashboard newInstance(String param1, String param2) {
        Dashboard fragment = new Dashboard();
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
        v = inflater.inflate(R.layout.fragment_dashboard, container, false);
        Widgetinitialization();

        return v;
    }

    private void Widgetinitialization() {
        tv_fragmentName = (TextView) v.findViewById(R.id.tv_fragmentName);
        tv_fragmentName.setText("Dashboard");
        tv_totalhehicles = v.findViewById(R.id.tv_totalhehicles);
        bankcardId_bus = v.findViewById(R.id.bankcardId_bus);
        tv_runningvehicles_bus = v.findViewById(R.id.tv_runningvehicles_bus);
        tv_stopvehicles_bus = v.findViewById(R.id.tv_stopvehicles_bus);
        tv_idlevehicles_bus = v.findViewById(R.id.tv_idlevehicles_bus);
        tv_nosignalvehicles_bus = v.findViewById(R.id.tv_nosignalvehicles_bus);
        cardciew_Stop_bus = v.findViewById(R.id.cardciew_Stop_bus);
        cardciew_Inrepair_bus = v.findViewById(R.id.cardciew_Inrepair_bus);
        cardciew_Idle_bus = v.findViewById(R.id.cardciew_Idle_bus);
        mChart_bar_bus = v.findViewById(R.id.mChart_bar_bus);
        DashboardVales_bus();
    }

    private void DashboardVales_bus() {
        tv_totalhehicles.setText("");
        try {
            SharedPreferences bb = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            ParentMobile = bb.getString("ParentMobile", "");

            JSONObject jsonBody = new JSONObject();
            jsonBody.put("MobileNumber", "" + ParentMobile);


            final String mRequestBody = jsonBody.toString();
            Log.i("VOLLEY", "Request" + mRequestBody);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Homedashboard, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("VOLLEY", "Response" + response);
                    try {
                        JSONObject object = new JSONObject(response);
                        int id = Integer.parseInt(object.optString("Code").toString());
                        String message = object.optString("Message").toString();


                        Srun_bus = object.optString("CarRun").toString();
                        Sstop_bus = object.optString("Carstop").toString();
                        Sidle_bus = object.optString("CarIdle").toString();
                        Suf_bus = object.optString("CarUnderFailure").toString();


                        final int mnum1_bus1 = Integer.parseInt(Srun_bus);
                        final int mnum2_bus1 = Integer.parseInt(Sstop_bus);
                        final int mnum3_bus1 = Integer.parseInt(Sidle_bus);
                        final int mnum4_bus1 = Integer.parseInt(Suf_bus);
                        int res_bikes = mnum1_bus1 + mnum2_bus1 + mnum3_bus1 + mnum4_bus1;


                        tv_runningvehicles_bus.setText(Srun_bus);
                        tv_stopvehicles_bus.setText(Sstop_bus);
                        tv_idlevehicles_bus.setText(Sidle_bus);
                        tv_nosignalvehicles_bus.setText(Suf_bus);
                        int j = mnum1_bus1;
                        int k = mnum2_bus1;
                        int l = mnum3_bus1;
                        int m = mnum4_bus1;

                        if (j > 0 || k > 0 || l > 0 || m > 0) {
                            //  ll_bcs.setVisibility(View.VISIBLE);
                            setBARCHART_RUNdata_bus(mnum1_bus1, mnum2_bus1, mnum3_bus1, mnum4_bus1);
                        } else {

                        }
                        tv_totalhehicles.setText("Total  :   " + Integer.toString(res_bikes));

                        if (!(tv_runningvehicles_bus.getText().toString()).equals("0")) {

                            bankcardId_bus.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    DashboardVehiclelist_Fragment fragment = new DashboardVehiclelist_Fragment();
                                    Bundle args = new Bundle();
                                    args.putString("TYPE", "Car");
                                    args.putString("STATUS", "Run");
                                    fragment.setArguments(args);
                                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.replace(R.id.tabFrameLayout, fragment);
                                    fragmentTransaction.addToBackStack(null);
                                    fragmentTransaction.commit();
                                }
                            });
                        }
                        if (!(tv_stopvehicles_bus.getText().toString()).equals("0")) {

                            cardciew_Stop_bus.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {


                                    DashboardVehiclelist_Fragment fragment = new DashboardVehiclelist_Fragment();
                                    Bundle args = new Bundle();


                                    args.putString("TYPE", "Car");
                                    args.putString("STATUS", "Stop");

                                    fragment.setArguments(args);
                                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.replace(R.id.tabFrameLayout, fragment);
                                    fragmentTransaction.addToBackStack(null);
                                    fragmentTransaction.commit();

                                }
                            });

                        }

                        if (!(tv_idlevehicles_bus.getText().toString()).equals("0")) {
                            cardciew_Idle_bus.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {


                                    DashboardVehiclelist_Fragment fragment = new DashboardVehiclelist_Fragment();
                                    Bundle args = new Bundle();

                                    args.putString("TYPE", "Car");
                                    args.putString("STATUS", "Idle");
                                    fragment.setArguments(args);
                                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.replace(R.id.tabFrameLayout, fragment);
                                    fragmentTransaction.addToBackStack(null);
                                    fragmentTransaction.commit();

                                }
                            });

                        }


                        if (!(tv_nosignalvehicles_bus.getText().toString()).equals("0")) {

                            cardciew_Inrepair_bus.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {


                                    DashboardVehiclelist_Fragment fragment = new DashboardVehiclelist_Fragment();
                                    Bundle args = new Bundle();

                                    args.putString("TYPE", "Car");
                                    args.putString("STATUS", "Nosignal");

                                    fragment.setArguments(args);
                                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.replace(R.id.tabFrameLayout, fragment);
                                    fragmentTransaction.addToBackStack(null);
                                    fragmentTransaction.commit();

                                }
                            });
                        }


                        //  }
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
                    5500000,
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

    private void setBARCHART_RUNdata_bus(int num1, int num2, int num3, int num4) {
        y_axis_bar.clear();
        ArrayList<String> labels = new ArrayList<>();
        labels.add("");//
        labels.add("");
        labels.add("");
        labels.add("");//


        y_axis_bar.add(Integer.parseInt(String.valueOf(num1)));
        y_axis_bar.add(Integer.parseInt(String.valueOf(num2)));
        y_axis_bar.add(Integer.parseInt(String.valueOf(num3)));
        y_axis_bar.add(Integer.parseInt(String.valueOf(num4)));


        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(y_axis_bar.get(0), 0));
        entries.add(new BarEntry(y_axis_bar.get(1), 1));
        entries.add(new BarEntry(y_axis_bar.get(2), 2));
        entries.add(new BarEntry(y_axis_bar.get(3), 3));


        BarDataSet dataSet = new BarDataSet(entries, "");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        dataSet.setColors(new int[]{R.color.green, R.color.red, R.color.yellow, R.color.gray}, getActivity());
        // dataSet.setColor(Color.argb(155, 0, 0,0));
        BarData data = new BarData(labels, dataSet);
        data.setValueFormatter(new MyValueFormatter());
        mChart_bar_bus.setVisibleXRange(1, 12);

        XAxis xAxis = mChart_bar_bus.getXAxis();
        xAxis.setLabelsToSkip(3);
        // xAxis.setLabelRotationAngle(-90);
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);

        //Y-axis
        YAxis leftAxis = mChart_bar_bus.getAxisLeft();
        leftAxis.setValueFormatter(new LargeValueFormatter());
        leftAxis.setDrawGridLines(true);
        leftAxis.setSpaceTop(35f);


        leftAxis.setValueFormatter(new YAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, YAxis yAxis) {
                return String.valueOf((int) value);
            }
        });

        xAxis.setValueFormatter(new XAxisValueFormatter() {
            @Override
            public String getXValue(String original, int index, ViewPortHandler viewPortHandler) {
                return String.valueOf(index);
            }
        });

        mChart_bar_bus.getAxisRight().setEnabled(false);
        mChart_bar_bus.setData(data);
        mChart_bar_bus.setDescription("Bar Chart");
        mChart_bar_bus.invalidate();


    }
    public class MyValueFormatter implements ValueFormatter {
        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            return Math.round(value) + "";
        }
    }

}
