package com.example.facefilter.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.facefilter.interfaces.OnFragmentInteractionListener;
import com.google.ar.core.Config;
import com.google.ar.core.Session;
import com.google.ar.core.exceptions.UnavailableApkTooOldException;
import com.google.ar.core.exceptions.UnavailableArcoreNotInstalledException;
import com.google.ar.core.exceptions.UnavailableDeviceNotCompatibleException;
import com.google.ar.core.exceptions.UnavailableException;
import com.google.ar.core.exceptions.UnavailableSdkTooOldException;
import com.google.ar.sceneform.ux.ArFragment;

import java.util.EnumSet;
import java.util.Set;

import androidx.annotation.Nullable;

public class CustomArFragment extends ArFragment {

    private String TAG = "SESSION_MESSSAGE";

    private OnFragmentInteractionListener mListener;

    public CustomArFragment() {
        // Required empty constructor
        super();
    }

    public static CustomArFragment newInstance() {
        return new CustomArFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        FrameLayout frameLayout = (FrameLayout) super.onCreateView(inflater, container, savedInstanceState);

        getPlaneDiscoveryController().hide();
        getPlaneDiscoveryController().setInstructionView(null);
        return frameLayout;
    }

    @Override
    protected void handleSessionException(UnavailableException sessionException) {
        String message;
        if (sessionException instanceof UnavailableArcoreNotInstalledException) {
            message = "Please install ARCore";
        } else if (sessionException instanceof UnavailableApkTooOldException) {
            message = "Please update ARCore";
        } else if (sessionException instanceof UnavailableSdkTooOldException) {
            message = "Please update this app";
        } else if (sessionException instanceof UnavailableDeviceNotCompatibleException) {
            message = "This device does not support AR";
        } else {
            message = "Failed to create AR session";
        }
        Log.e(TAG, "Error: " + message, sessionException);
        Toast.makeText(requireActivity(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    protected Config getSessionConfiguration(Session session) {
        Config config = new Config(session);
        config.setAugmentedFaceMode(Config.AugmentedFaceMode.MESH3D);

        this.getArSceneView().setupSession(session);

        return config;
    }

    @Override
    protected Set<Session.Feature> getSessionFeatures() {
        return EnumSet.of(Session.Feature.FRONT_CAMERA);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
