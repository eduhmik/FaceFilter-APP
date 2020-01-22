package com.example.facefilter.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.facefilter.sharedprefs.SharedPrefs;
import com.example.facefilter.tools.SweetAlertDialog;
import com.google.gson.Gson;

import androidx.fragment.app.Fragment;

public abstract class BaseFragment extends Fragment {

    public SweetAlertDialog _sweetAlertDialog;
    public SharedPrefs sharedPrefs;
    public static Gson gson;

    public BaseFragment() {
        // Required empty public constructor
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPrefs = new SharedPrefs(getActivity());
        //Firebase.setAndroidContext(this);
        gson = new Gson();
    }
    /**
     * show sweet alert with default duration
     * @param message to be displayed
     * @param title of alert
     * @param dialogType
     */
    public void showSweetDialog(String title, String message, int dialogType){
        _sweetAlertDialog = new SweetAlertDialog(getActivity(), dialogType);
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
        _sweetAlertDialog = new SweetAlertDialog(getActivity(), dialogType);
        _sweetAlertDialog.setTitleText(title);
        _sweetAlertDialog.setContentText(message);
        _sweetAlertDialog.setConfirmText(confirmText);
        _sweetAlertDialog.setConfirmClickListener(listener);
        _sweetAlertDialog.show();
    }
    /**
     * show toast with default duration
     * @param message to be displayed
     */
    public void showToast(String message)
    {
        try {
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void startNewActivity(Class<? extends Activity> clazz){
        startActivity(new Intent(getActivity(),clazz));
    }
}
