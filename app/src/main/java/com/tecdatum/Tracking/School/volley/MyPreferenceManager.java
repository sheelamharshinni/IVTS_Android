package com.tecdatum.Tracking.School.volley;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;


public class MyPreferenceManager {

    private String TAG = MyPreferenceManager.class.getSimpleName();

    SharedPreferences pref;

    SharedPreferences.Editor editor;

    Context _context;

    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "ritrac_amaze";

    private static final String KEY_VALUE = "valu";
    private static final String KEY_EMPLOYEENAME = "e_name";
    private static final String KEY_PHNO = "e_Phno";
    private static final String KEY_LOCATION = "location";
    private static final String KEY_VEHICLE_ID = "vehicle_id";
    // All Shared Preferences Keys
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_USER_EMAIL = "user_email";
    private static final String KEY_USER_MOBILE = "user_mobile";

    private static final String KEY_LAT = "KEY_LAT";
    private static final String KEY_LNG = "KEY_LNG";
    private static final String KEY_ID = "KEY_ID";
    private static final String BOTTOM_SLIDER = "BOTTOM_SLIDER";
    private static final String TOP_SLIDER = "TOP_SLIDER";
    private static final String ONLINE_STATUS = "ONLINE_STATUS";
    private static final String DEALSURL = "DEALSURL";
    private static final String KEY_ROW = "KEY_ROW";
    private static final String KEY_TIME = "KEY_TIME";

    private static final String KEY_URL = "URL_DETAILS";
    private static final String IsValuesPresent = "IsValuesPresent";
    private static final String FCM_TOKEN = "FCM_TOKEN";
    private static final String KEY_EMPLOYEEDESIG = "designation";
    private static final String KEY_EMPLOYEEDESIGID = "designation id";
    private static final String KEY_EMPLOYEESHIFT = "Shift";
    private static final String KEY_EMPLOYEESHIFTID = "Shiftid";


    // Constructor
    public MyPreferenceManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }



    public void setE_Id(String id) {
        editor.putString(KEY_VALUE, id);
        editor.commit();
        Log.e(TAG, "time IS " + id);
    }

    public String getE_Id(){
        String id = pref.getString(KEY_VALUE, null);
        return id;
    }



    public void setTime(String id) {
        editor.putString(KEY_TIME, id);
        editor.commit();
        Log.e(TAG, "time IS " + id);
    }
    public String getTime(){
        String id = pref.getString(KEY_TIME, null);
        return id;
    }
    public void setEmployeName(String id) {
        editor.putString(KEY_EMPLOYEENAME, id);
        editor.commit();
        Log.e(TAG, "EmployeName IS " + id);
    }

    public String getEmployeName(){
        String id = pref.getString(KEY_EMPLOYEENAME, null);
        return id;
    }
    public void setEmployeDesigntnName(String id) {
        editor.putString(KEY_EMPLOYEEDESIG, id);
        editor.commit();
        Log.e(TAG, "EmployeName IS " + id);
    }
    public String getEmployeDesigntn(){
        String id = pref.getString(KEY_EMPLOYEEDESIG, null);
        return id;
    }
    public void setEmployeDesigntnID(String id) {
        editor.putString(KEY_EMPLOYEEDESIGID, id);
        editor.commit();
        Log.e(TAG, "EmployeName IS " + id);
    }
    public String getEmployeDesigntnID(){
        String id = pref.getString(KEY_EMPLOYEEDESIGID, null);
        return id;
    }
    public void setEmployeShiftname(String id) {
        editor.putString(KEY_EMPLOYEESHIFT, id);
        editor.commit();
        Log.e(TAG, "EmployeName IS " + id);
    }
    public String getEmployeShift(){
        String id = pref.getString(KEY_EMPLOYEESHIFT, null);
        return id;
    }

    public void setEmployeShiftID(String id) {
        editor.putString(KEY_EMPLOYEESHIFTID, id);
        editor.commit();
        Log.e(TAG, "EmployeName IS " + id);
    }
    public String getEmployeShiftID(){
        String id = pref.getString(KEY_EMPLOYEESHIFTID, null);
        return id;
    }
    public void setPhNo(String id) {
        editor.putString(KEY_PHNO, id);
        editor.commit();
        Log.e(TAG, "PHNO IS " + id);
    }
    public String getPhNo(){
        String id = pref.getString(KEY_PHNO, null);
        return id;
    }
    public void setCurrentLocation(String id) {
        editor.putString(KEY_LOCATION, id);
        editor.commit();
        Log.e(TAG, "loction IS " + id);
    }

    public String getCurrentLocation(){
        String id = pref.getString(KEY_LOCATION, null);
        return id;
    }
    public void setVehicleID(String id) {
        editor.putString(KEY_VEHICLE_ID, id);
        editor.commit();
        Log.e(TAG, "VEHICLE ID IS " + id);
    }

    public String getVehicleID(){
        String id = pref.getString(KEY_VEHICLE_ID, null);
        return id;
    }
    public void setVehicleName(String id) {
        editor.putString(KEY_VEHICLE_ID, id);
        editor.commit();
        Log.e(TAG, "VEHICLE NAME IS " + id);
    }

    public String getVehicleName(){
        String id = pref.getString(KEY_VEHICLE_ID, null);
        return id;
    }



    public void storeToken(String token) {
        editor.putString(FCM_TOKEN, token);
        editor.commit();

        Log.e(TAG, "FCM TOKEN IS " + token);
    }

    public String getToken() {
        String strID = pref.getString(FCM_TOKEN, null);
        return strID;

    }



    public void storeLat(String id) {
        editor.putString(KEY_LAT, id);
        editor.commit();

        Log.e(TAG, "KEY_LAT IS " + id);
    }


    public void storeLng(String id) {
        editor.putString(KEY_LNG, id);
        editor.commit();

        Log.e(TAG, "KEY_LNG IS " + id);
    }



    public void storeLatLng(String mLat, String mLng) {
        editor.putString(KEY_LAT, mLat);
        editor.putString(KEY_LNG, mLng);
        editor.commit();

        Log.e(TAG, "LAT & LNG DETAILS ARE. " + mLat + ", " + mLng);
    }



    public String getLat() {
        String strLat = pref.getString(KEY_LAT, null);
        return strLat;

    }

    public String getLng() {
        String strLng = pref.getString(KEY_LNG, null);
        return strLng;

    }

    public void setRowID(String id) {
        editor.putString(KEY_ROW, id);
        editor.commit();

        Log.e(TAG, "ID OF THE PRODUCT " + id);
    }

    public String getRowID() {
        String strID = pref.getString(KEY_ROW, null);
        return strID;

    }


    public void startTIme(String id) {
        editor.putString(KEY_TIME, id);
        editor.commit();

        Log.e(TAG, "ID OF THE PRODUCT " + id);
    }

    public String endTIme() {
        String strID = pref.getString(KEY_TIME, null);
        return strID;

    }


    public void setOnlineStatus(int id) {
        editor.putInt(ONLINE_STATUS, id);
        editor.commit();

        Log.e(TAG, "ID OF THE PRODUCT " + id);
    }

    public int getOnlineStatus() {
        int strID = pref.getInt(ONLINE_STATUS, 0);
        return strID;

    }


}
