package com.example.oscarmeanwell.cwk1;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class AddEntry extends AppCompatActivity {

    String dt = new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime());
    String dt1 = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
    public static double lat;
    public static double lon;
    LocationListener listener;
    LocationManager locationManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_entry);
        super.setTitle("Add Entry for: "+dt);
        final EditText et = (EditText)findViewById(R.id.editText3);
        et.setText(dt+" : "+dt1+System.getProperty("line.separator"));
        getLocation();
        et.append("Coordinates("+lat+","+lon+")"+System.getProperty("line.separator"));
        Button btn1 = (Button)findViewById(R.id.btn1);
        btn1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //filename should be usrnm + currentdate
                FileOutputStream outputStream;
                String filename = MainActivity.UserName() + "Diary123.txt";
                //construct the string to write
                String tmp = "{" + System.getProperty("line.separator") + et.getText().toString() + System.getProperty("line.separator") + "}" + System.getProperty("line.separator");
                try{
                    outputStream = openFileOutput(filename, Context.MODE_APPEND);
                    outputStream.write(tmp.getBytes());
                    outputStream.close();
                    Intent newAc = new Intent(AddEntry.this, NewIntent.class);
                    startActivity(newAc);
                } catch (Exception e){
                    e.printStackTrace();
                }
                //write to file thats specific to the app, not in the assets folder.
                //do date and time for entry
            }
        });
        Button btn2 = (Button)findViewById(R.id.btnC);
        btn2.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();

            }
        });
    }
    static final int IMG_CAPTURE = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, IMG_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == IMG_CAPTURE && resultCode == RESULT_OK) {
            ImageView image1 = (ImageView) findViewById(R.id.image);
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            image1.setImageBitmap(imageBitmap);
            FileOutputStream out = null;
            String tmp1 = MainActivity.UserName()+dt + dt1 + ".jpg";
            tmp1 = tmp1.replace("/", "");
            tmp1 = tmp1.replace(":", "");
            tmp1 = tmp1.replace(" ", "");
            File file = new File(getFilesDir(), tmp1); //add meaningfull filename here
            try {
                out = new FileOutputStream(file);
                imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void getLocation(){
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                lat = location.getLatitude();
                lon = location.getLongitude();
            }
            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
            }

            @Override
            public void onProviderEnabled(String s) {
            }

            @Override
            public void onProviderDisabled(String s) {
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates("gps", 5000, 0, listener);
    }
}
