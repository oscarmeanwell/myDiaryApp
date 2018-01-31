package com.example.oscarmeanwell.cwk1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class DisplayImage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_image);
        super.setTitle("Entry Image View");
        final ImageView imgView = (ImageView)findViewById(R.id.image2);
        Bitmap imageBitmap = BitmapFactory.decodeFile(getFilesDir()+"/"+getIntent().getStringExtra("img"));
        imgView.setImageBitmap(imageBitmap);

        imgView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                BitmapDrawable drawable = (BitmapDrawable)imgView.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                try{
                    MediaStore.Images.Media.insertImage(getContentResolver(), bitmap,
                            "ImageExport.png" , "Diary_Export");
                    Toast.makeText(DisplayImage.this, "Added to Gallery", Toast.LENGTH_LONG).show();

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(DisplayImage.this, "Something went wrong...", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
