package com.example.facefilter.sharedprefs;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefs {
    SharedPreferences accountPreferences;
    SharedPreferences.Editor accountPreferencesEditor;
    Context context;
    private static final String PREF_NAME = "sessionPref";
    final String IS_LOGGED_IN = "Is logged in";
    final String USER = "User";
    final String SHARED_PREFS = "User";
    final String ACCESS_TOKEN = "Token";
    final String QR_NUMBER = "Number";
    final String CSQ = "csq";
    final String IMSI = "imsi";
    final String VOLT = "volts";
    final String ASUTEST = "asu";
    final String VOLTTEST = "volt";
    final String TESTID = "testid";
    final String FINALTEST = "test";
    final String DATE = "date";
    final String SITEID = "siteid";

    public SharedPrefs(Context context){
        this.context=context;
        accountPreferences=context.getSharedPreferences("Account", Context.MODE_PRIVATE);
        accountPreferencesEditor=accountPreferences.edit();
    }
    public void setIsloggedIn(boolean isloggedIn) {
        accountPreferencesEditor.putBoolean(IS_LOGGED_IN,isloggedIn).commit();
    }
    public boolean getIsloggedIn() {
        return accountPreferences.getBoolean(IS_LOGGED_IN,false);
    }

    public void setVoltTest(String voltTest) {
        accountPreferencesEditor.putString(VOLTTEST, voltTest).commit();
    }
    public String  getVoltTest() {
        return accountPreferences.getString(VOLTTEST,"");
    }

    public void setAsuTest(String asuTest) {
        accountPreferencesEditor.putString(ASUTEST, asuTest).commit();
    }
    public String getAsuTest() {
        return accountPreferences.getString(ASUTEST,"");
    }

    public void setDate(String date) {
        accountPreferencesEditor.putString(DATE, date).commit();
    }
    public String  getDate() {
        return accountPreferences.getString(DATE,"");
    }

    public void setTestID(String testID) {
        accountPreferencesEditor.putString(TESTID, testID).commit();
    }
    public String  getTestID() {
        return accountPreferences.getString(TESTID,"");
    }

    public void setFinalTest(String finalTest) {
        accountPreferencesEditor.putString(FINALTEST, finalTest).commit();
    }
    public String  getFinalTest() {
        return accountPreferences.getString(FINALTEST,"");
    }

    public void setIMSI(String imsi) {
        accountPreferencesEditor.putString(IMSI, imsi).commit();
    }
    public String  getIMSI() {
        return accountPreferences.getString(IMSI,"");
    }

    public void setVOLT(String voltage) {
        accountPreferencesEditor.putString(VOLT, voltage).commit();
    }
    public String  getVOLT() {
        return accountPreferences.getString(VOLT,"");
    }

    public void setQR_NUMBER(String qr_number) {
        accountPreferencesEditor.putString(QR_NUMBER, qr_number).commit();
    }
    public String  getQR_NUMBER() {
        return accountPreferences.getString(QR_NUMBER,"");
    }

    public void setCSQ(String csq) {
        accountPreferencesEditor.putString(CSQ, csq).commit();
    }

    public String getCSQ() {
        return accountPreferences.getString(CSQ, "");
    }

    public void setAccessToken(String token) {
        accountPreferencesEditor.putString(ACCESS_TOKEN, token).commit();
    }
    public String  getAccessToken() {
        return accountPreferences.getString(ACCESS_TOKEN,"");
    }


    public void setSiteID(String site_id) {
        accountPreferencesEditor.putString(SITEID, site_id).commit();
    }

    public void saveEmail(Context context, String email){
        context = context;
        accountPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = accountPreferences.edit();
        editor.putString("EMAIL", email);
        editor.commit();
    }

    public void saveId(Context context, String id){
        context = context;
        accountPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = accountPreferences.edit();
        editor.putString("ID", id);
        editor.commit();
    }

    public String getUserEmail(){
        accountPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return accountPreferences.getString("EMAIL", null);
    }

    public String getId(){
        accountPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return accountPreferences.getString("ID", null);
    }


    public void saveName(Context context, String name){
        context = context;
        accountPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = accountPreferences.edit();
        editor.putString("NAME", name);
        editor.commit();
    }

    public String getName(){
        accountPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return accountPreferences.getString("NAME", null);
    }

    public void saveRole(Context context, String role){
        context = context;
        accountPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = accountPreferences.edit();
        editor.putString("ROLE", role);
        editor.commit();
    }

    public String getRole(){
        accountPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return accountPreferences.getString("ROLE", null);
    }

    public void savePhone(Context context, String phone){
        context = context;
        accountPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = accountPreferences.edit();
        editor.putString("PHONE", phone);
        editor.commit();
    }

    public String getPhone(){
        accountPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return accountPreferences.getString("PHONE", null);
    }

    public void clear(){
        accountPreferencesEditor.clear();
        accountPreferencesEditor.apply();
    }


}
