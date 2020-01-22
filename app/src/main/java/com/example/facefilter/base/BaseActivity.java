package com.example.facefilter.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.facefilter.sharedprefs.SharedPrefs;
import com.example.facefilter.tools.SweetAlertDialog;
import com.google.gson.Gson;

import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {
    public ProgressDialog _dialog;
    public SweetAlertDialog _sweetAlertDialog;
    public SharedPrefs sharedPrefs;
    public Gson gson;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPrefs = new SharedPrefs(this);
        _dialog = new ProgressDialog(this);
        gson = new Gson();
    }
    /**
     * show sweet alert with default duration
     * @param message to be displayed
     * @param title of alert
     * @param dialogType
     */
    public void showSweetDialog(String title, String message, int dialogType){
        _sweetAlertDialog = new SweetAlertDialog(this, dialogType);
        _sweetAlertDialog.setTitleText(title);
        _sweetAlertDialog.setContentText(message);
        _sweetAlertDialog.show();
    }
    /**
     * show sweet alert with default duration
     * @param message to be displayed
     * @param title of alert
     * @param dialogType
     * @param listener neutral button listener
     */
    public void showSweetDialog(String title, String message, int dialogType, String confirmText, SweetAlertDialog.OnSweetClickListener listener){
        _sweetAlertDialog = new SweetAlertDialog(this, dialogType);
        _sweetAlertDialog.setTitleText(title);
        _sweetAlertDialog.setContentText(message);
        _sweetAlertDialog.setConfirmText(confirmText);
        _sweetAlertDialog.setCancelable(false);
        _sweetAlertDialog.setConfirmClickListener(listener);
        _sweetAlertDialog.show();
    }
    /**
     * show toast with default duration
     * @param message to be displayed
     */
    public void showToast(String message)
    {
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }
    /**
     * init progress dialog, called by network requests
     */
    public void showProgressDialog(String message)
    {
        _dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        _dialog.setMessage(message);
        _dialog.setIndeterminate(true);
        _dialog.setCanceledOnTouchOutside(false);
        _dialog.show();
    }

    public void startNewActivity(Class<? extends Activity> clazz){
        startActivity(new Intent(this,clazz));
    }
}