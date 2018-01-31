package com.example.oscarmeanwell.cwk1;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class DisplayEntry extends AppCompatActivity {

    String[] lines;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_entry);
        super.setTitle("Entry for: "+NewIntent.EntryName());
        final EditText diaryEntry = (EditText)findViewById(R.id.diaryEntry);
        final Button btn1 = (Button)findViewById(R.id.btn1);
        final Button btn2 = (Button)findViewById(R.id.btn2);
        diaryEntry.setKeyListener(null); //make editText uneditable
        String tmp = "";
        FileInputStream br = null;
        String line = null;
        String coord = "";
        final String coord1 = "";
        try {
            br = openFileInput(MainActivity.UserName()+"Diary123.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            int x;
            while ((x = br.read()) != -1){
                line = line + Character.toString((char)x);
            }
            lines = line.split(System.getProperty("line.separator"));
            int c = 0;
            int count = 0;
            for (String z : lines) {
                if (z.equals(NewIntent.EntryName())){
                    c = 1;
                }
                if (z.equals("}") && c==1){
                    c = 0;
                    break;
                }
                if (c==1) {
                    tmp += z;
                    tmp += System.getProperty("line.separator");//get the coordinates here you loony.
                    if (z.contains("Coordinates")) {
                        char[] tmpA = z.toCharArray();
                        for (char p : tmpA) {
                            if (Character.toString(p).equals(")")) {
                                count = 100;
                            }
                            if (count == 1) {
                                coord += Character.toString(p);

                            }
                            if (Character.toString(p).equals("(")) {
                                count = 1;
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        final String [] coor = coord.split(",");
        diaryEntry.setText(tmp, TextView.BufferType.EDITABLE);
        String tmp1 = MainActivity.UserName()+NewIntent.EntryName()+".jpg";
        tmp1 = tmp1.replace("/", "");
        tmp1 = tmp1.replace(":", "");
        tmp1 = tmp1.replace(" ", "");

        try{
            ImageView image1 = (ImageView)findViewById(R.id.image1);
            Bitmap imageBitmap = BitmapFactory.decodeFile(getFilesDir()+"/"+tmp1);
            image1.setImageBitmap(imageBitmap);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        ImageView imgx = (ImageView)findViewById(R.id.image1);
        final String finalTmp = tmp1;
        imgx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newAc = new Intent(DisplayEntry.this, DisplayImage.class);
                newAc.putExtra("img", finalTmp);
                startActivity(newAc);
            }
        });

        btn1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //extract coordinates and pass to intent as string extra
                if (!(coor[0].equals("0.0") && coor[1].equals("0.0"))){
                    Intent i = new Intent(DisplayEntry.this, ViewLocationOnMap.class);
                    i.putExtra("coord", coor[0]);
                    i.putExtra("coord1", coor[1]);
                    i.putExtra("activity", "DisplayEntry");
                    startActivity(i);
                }
                else{
                    Toast.makeText(DisplayEntry.this, "Sorry, no coordinates with this entry", Toast.LENGTH_LONG).show();
                }

            }
        });

        final String finalLine = line;
        btn2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //Loop through file and if its not equal to this entry add to string and
                //then re-write to file of the same name.
                //Neat right?
                //remove associated pictures
                new AlertDialog.Builder(DisplayEntry.this)
                        .setTitle("Title")
                        .setMessage("Do you really want to delete this entry and associated pictures?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                int counter = 0;
                                String tmp = "";

                                for (String x : lines){
                                    if (x.equals(NewIntent.EntryName())){
                                        counter = 1;
                                    }
                                    if (counter==0){
                                        tmp += x + System.getProperty("line.separator");
                                    }
                                    if (counter==1 && x.equals("}")){
                                        counter = 0;
                                        tmp += x + System.getProperty("line.separator");
                                    }
                                }
                                String filename = MainActivity.UserName() + "Diary123.txt";
                                FileOutputStream outputStream;
                                try {
                                    outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                                    outputStream.write(tmp.getBytes());
                                    outputStream.close();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                //remove associated pictures
                                String tmp1 = NewIntent.EntryName()+".jpg";
                                tmp1 = tmp1.replace("/", "");
                                tmp1 = tmp1.replace(":", "");
                                tmp1 = tmp1.replace(" ", "");
                                File toDelete = new File(getFilesDir()+"/"+tmp1);
                                toDelete.delete();
                                Toast.makeText(DisplayEntry.this, "Deleted!", Toast.LENGTH_SHORT).show();
                                Intent x = new Intent(DisplayEntry.this, NewIntent.class);
                                startActivity(x);
                            }})
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });
    }
}
