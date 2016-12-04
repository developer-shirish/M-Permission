package com.example.shirish.m_permission;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button mBtnMultiPermission;
    private static final int PERMISSION_CALLBACK_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    private SharedPreferences PREFpermission;
    private boolean sentToSettings = false;

    String[] permissionRequired = new String[]{Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PREFpermission = getSharedPreferences("PERMISSION", MODE_PRIVATE);
        mBtnMultiPermission = (Button) findViewById(R.id.btnMultiplePermission);

        mBtnMultiPermission.setOnClickListener(new View.OnClickListener() {
                                                   @Override
                                                   public void onClick(View view) {

                                                       if (ActivityCompat.checkSelfPermission(MainActivity.this, permissionRequired[0]) != PackageManager.PERMISSION_GRANTED
                                                               || ActivityCompat.checkSelfPermission(MainActivity.this, permissionRequired[1]) != PackageManager.PERMISSION_GRANTED
                                                               || ActivityCompat.checkSelfPermission(MainActivity.this, permissionRequired[2]) != PackageManager.PERMISSION_GRANTED
                                                               || ActivityCompat.checkSelfPermission(MainActivity.this, permissionRequired[3]) != PackageManager.PERMISSION_GRANTED) {

                                                           if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permissionRequired[0])
                                                                   || ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permissionRequired[1])
                                                                   || ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permissionRequired[2])
                                                                   || ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permissionRequired[3])) {
                                                               ActivityCompat.requestPermissions(MainActivity.this, permissionRequired, PERMISSION_CALLBACK_CONSTANT);
                                                           } else if (PREFpermission.getBoolean(permissionRequired[0], false)) {

                                                               Toast.makeText(MainActivity.this, "You need to grant some permission. Redirecting to Setting.", Toast.LENGTH_LONG).show();
                                                               sentToSettings = true;
                                                               Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                                               Uri uri = Uri.fromParts("package", getPackageName(), null);
                                                               intent.setData(uri);
                                                               startActivityForResult(intent, REQUEST_PERMISSION_SETTING);

                                                           } else {
                                                               ActivityCompat.requestPermissions(MainActivity.this, permissionRequired, PERMISSION_CALLBACK_CONSTANT);
                                                           }

                                                           SharedPreferences.Editor editor = PREFpermission.edit();
                                                           editor.putBoolean(permissionRequired[0], true);
                                                           editor.commit();


                                                       } else {

                                                           Toast.makeText(MainActivity.this, "You already grant permission.", Toast.LENGTH_SHORT).show();
                                                       }
                                                   }

                                               }

        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_CALLBACK_CONSTANT) {

            boolean allGrant = false;

            for (int i = 0; i < grantResults.length; i++) {

                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    allGrant = true;
                } else {

                    allGrant = false;
                    break;
                }
            }

            if (allGrant) {

            } else if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permissionRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permissionRequired[1])
                    || ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permissionRequired[2])
                    || ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permissionRequired[3])) {

                ActivityCompat.requestPermissions(MainActivity.this, permissionRequired, PERMISSION_CALLBACK_CONSTANT);


            } else {
                Toast.makeText(this, "Unable to get permission.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_PERMISSION_SETTING) {
            if (ActivityCompat.checkSelfPermission(MainActivity.this, permissionRequired[0]) == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(this, "onActivityResult() - We got permission granted.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        if (sentToSettings) {
            if (ActivityCompat.checkSelfPermission(MainActivity.this, permissionRequired[0]) == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(this, "onPostResume()", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
