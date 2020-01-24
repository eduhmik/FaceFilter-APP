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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.facefilter.R;
import com.example.facefilter.base.BaseActivity;
import com.example.facefilter.fragments.CustomArFragment;
import com.example.facefilter.interfaces.OnFragmentInteractionListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.ar.core.AugmentedFace;
import com.google.ar.core.Frame;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.Renderable;
import com.google.ar.sceneform.rendering.Texture;
import com.google.ar.sceneform.ux.AugmentedFaceNode;

import java.util.Collection;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, OnFragmentInteractionListener {

    @BindView(R.id.save_button)
    FloatingActionButton saveButton;
    private ModelRenderable modelRenderable;
    private Texture texture;
    public static Bitmap bitmap;
    private boolean isAdded = false;
    Camera camera1;
    private Camera.PictureCallback myPictureCallback_JPG;
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
        customArFragment = (CustomArFragment) getSupportFragmentManager().findFragmentById(R.id.arFragment);
        isStoragePermissionGranted();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkMultiplePermissions(REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS, this);
        } else {
            // Open your camera here.
        }
        renderFilters();
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



    @Override
    public void onFragmentInteraction(int action) {

    }

    @OnClick(R.id.save_button)
    public void onViewClicked() {
//            camera1.takePicture(null, null, myPictureCallback_JPG);
    }
}
