package com.example.facefilter.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.facefilter.R;
import com.example.facefilter.adapters.FiltersAdapter;
import com.example.facefilter.application.Config;
import com.example.facefilter.base.BaseActivity;
import com.example.facefilter.fragments.CustomArFragment;
import com.example.facefilter.interfaces.OnFragmentInteractionListener;
import com.example.facefilter.model.Auth;
import com.example.facefilter.model.TrendingFilters;
import com.example.facefilter.retrofit.AuthRequests;
import com.example.facefilter.retrofit.FiltersRequests;
import com.example.facefilter.retrofit.ListResponse;
import com.example.facefilter.retrofit.ObjectResponse;
import com.example.facefilter.retrofit.ServiceGenerator;
import com.example.facefilter.sharedprefs.SharedPrefs;
import com.example.facefilter.tools.SweetAlertDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.ar.core.AugmentedFace;
import com.google.ar.core.Frame;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.Renderable;
import com.google.ar.sceneform.rendering.Texture;
import com.google.ar.sceneform.ux.AugmentedFaceNode;

import java.util.ArrayList;
import java.util.Collection;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, OnFragmentInteractionListener {

    SharedPrefs sharedPrefs;
    @BindView(R.id.save_button)
    FloatingActionButton saveButton;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private FiltersAdapter filtersAdapter;
    private ArrayList<TrendingFilters> trendingFiltersArrayList = new ArrayList<>();
    private ModelRenderable modelRenderable;
    private Texture texture;
    private String token;
    public static Bitmap bitmap;
    private boolean isAdded = false;
    CustomArFragment customArFragment = CustomArFragment.newInstance();
    public final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 1;
    private String CAMERA_PERMISSION = Manifest.permission.CAMERA;

    private String READ_EXTERNAL_STORAGE_PERMISSION = Manifest.permission.READ_EXTERNAL_STORAGE;

    private String WRITE_EXTERNAL_STORAGE_PERMISSION = Manifest.permission.WRITE_EXTERNAL_STORAGE;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        customArFragment = (CustomArFragment) getSupportFragmentManager().findFragmentById(R.id.arFragment);
        isStoragePermissionGranted();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkMultiplePermissions(REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS, this);
        } else {
            // Open your camera here.
        }
        AuthenticateAPI(Config.API_KEY);
        renderFilters();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                fetchTrendingFilters(token);
            }
        }, 10000);
    }

    private Camera.PictureCallback getPictureCallback() {
        Camera.PictureCallback picture = new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                Intent intent = new Intent(MainActivity.this, PictureActivity.class);
                startActivity(intent);
            }
        };
        return picture;
    }


    public void renderFilters() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            ModelRenderable.builder()
                    .setSource(this, R.raw.fox_face)
                    .build()
                    .thenAccept(renderable -> {
                        modelRenderable = renderable;

                        modelRenderable.setShadowCaster(false);
                        modelRenderable.setShadowReceiver(false);
                    });
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Texture.builder()
                    .setSource(this, R.drawable.fox_face_mesh_texture)
                    .build()
                    .thenAccept(texture -> this.texture = texture);
        }

        if (customArFragment != null) {
            customArFragment.getArSceneView().setCameraStreamRenderPriority(Renderable.RENDER_PRIORITY_FIRST);
        }

        customArFragment.getArSceneView().getScene().addOnUpdateListener(frameTime -> {
            if (modelRenderable == null || texture == null)
                return;

            Frame frame = customArFragment.getArSceneView().getArFrame();

            Collection<AugmentedFace> augmentedFaces = frame.getUpdatedTrackables(AugmentedFace.class);

            for (AugmentedFace augmentedFace : augmentedFaces) {
                if (isAdded)
                    return;
                AugmentedFaceNode augmentedFaceNode = new AugmentedFaceNode(augmentedFace);
                augmentedFaceNode.setParent(customArFragment.getArSceneView().getScene());
                augmentedFaceNode.setFaceMeshTexture(texture);
                augmentedFaceNode.setFaceRegionsRenderable(modelRenderable);

                isAdded = true;
            }
        });
    }


    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted");
                return true;
            } else {

                Log.v(TAG, "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted");
            return true;
        }
    }

    // Camera permission needed
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS:
                boolean showRationale1 = shouldShowRequestPermissionRationale(CAMERA_PERMISSION);
                boolean showRationale2 = shouldShowRequestPermissionRationale(READ_EXTERNAL_STORAGE_PERMISSION);
                boolean showRationale3 = shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE_PERMISSION);
                if (showRationale1 && showRationale2 && showRationale3) {
                    //explain to user why we need the permissions
                } else {
                    //explain to user why we need the permissions and ask him to go to settings to enable it
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    //check for camera and storage access permissions
    @TargetApi(Build.VERSION_CODES.M)
    private void checkMultiplePermissions(int permissionCode, Context context) {

        String[] PERMISSIONS = {CAMERA_PERMISSION, READ_EXTERNAL_STORAGE_PERMISSION, WRITE_EXTERNAL_STORAGE_PERMISSION};
        if (!hasPermissions(context, PERMISSIONS)) {
            ActivityCompat.requestPermissions((Activity) context, PERMISSIONS, permissionCode);
        } else {
            // Open your camera here.
        }
    }

    private boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void AuthenticateAPI(String apiKey) {
        showSweetDialog("Authenticating", "Authenticating. Please wait...", SweetAlertDialog.PROGRESS_TYPE);
        AuthRequests service = ServiceGenerator.createService(AuthRequests.class);
        Call<ObjectResponse<Auth>> call = service.authenticateAPI(apiKey);
        call.enqueue(new Callback<ObjectResponse<Auth>>() {
            @Override
            public void onResponse(Call<ObjectResponse<Auth>> call, Response<ObjectResponse<Auth>> response) {
                _sweetAlertDialog.dismissWithAnimation();
                Log.e("Password reset", gson.toJson(response.body()));
                if (response.body() != null && response.isSuccessful()) {
                    if (TextUtils.equals(response.body().getSuccess(), "true")) {
                        token = response.body().getToken();
                        showSweetDialog("Successful!!!", "Authentication Successful", SweetAlertDialog.SUCCESS_TYPE);
                        _sweetAlertDialog.dismissWithAnimation();

                    } else if (TextUtils.equals(response.body().getSuccess(), "false")) {
                        showToast(response.body().getMessage());
                        showSweetDialog("Authentication Failed!", response.body().getMessage(), SweetAlertDialog.ERROR_TYPE, "Got it!",new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                            }
                        });
                    }
                } else {
                    showToast("No response from server");
                }
            }
            @Override
            public void onFailure(Call<ObjectResponse<Auth>> call, Throwable t) {
                Log.e("", t.getMessage());
                _sweetAlertDialog.dismissWithAnimation();
            }
        });
    }

    private void fetchTrendingFilters(String token) {
        FiltersRequests service = ServiceGenerator.createService(FiltersRequests.class);
        Call<ListResponse<TrendingFilters>> call = service.getTrendingFilters(token);
        call.enqueue(new Callback<ListResponse<TrendingFilters>>() {
            @Override
            public void onResponse(Call<ListResponse<TrendingFilters>> call, Response<ListResponse<TrendingFilters>> response) {
                try {
                    if (response != null) {
                        Log.e("Filters list", gson.toJson(response.body()));
                        if (TextUtils.equals(response.body().getSuccess(), "true")) {
                            ArrayList<TrendingFilters> responze = response.body().getMedia();
                            trendingFiltersArrayList.clear();
                            trendingFiltersArrayList.addAll(responze);
                            filtersAdapter = new FiltersAdapter(getBaseContext(), trendingFiltersArrayList);
                            recyclerView.setAdapter(filtersAdapter);
                        } else {
                            showToast("Please try again");
                        }
                    } else {
                        showToast("No response from server");
                    }
                } catch (Exception e) {
                    trendingFiltersArrayList.clear();
                    filtersAdapter.notifyDataSetChanged();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ListResponse<TrendingFilters>> call, Throwable t) {
                Log.e("tag", t.getMessage());
            }
        });
    }


    @Override
    public void onFragmentInteraction(int action) {

    }

    @OnClick(R.id.save_button)
    public void onViewClicked() {
//            camera1.takePicture(null, null, myPictureCallback_JPG);
    }
}
