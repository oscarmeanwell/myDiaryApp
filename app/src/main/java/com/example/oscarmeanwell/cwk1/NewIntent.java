package com.example.oscarmeanwell.cwk1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;


public class NewIntent extends AppCompatActivity {

    private static ListView list_view;
    private static ArrayList<String> titleLst = new ArrayList<>();
    private static String entryNM;
    public static String EntryName(){
        return entryNM;
    }
    public static ArrayList<String> co = new ArrayList<>();
    public static String [] jpgfilelst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_intent);
        super.setTitle("Diary - My Entries");
        titleLst.clear(); //To only include entries once, if back button pressed
        FileInputStream br = null;
        String line = "";
        try {
            br = openFileInput(MainActivity.UserName()+"Diary123.txt");
            try {
                int x;
                while ((x = br.read()) != -1){
                    line = line + Character.toString((char)x);
                }
                int c = 0;
                String[] lines = line.split(System.getProperty("line.separator"));

                for(String z : lines){
                    if (c == 1){
                        if (!z.equals("}")) //remove empty (deleted) entries
                            titleLst.add(z);
                        c = 0;
                    }
                    if (z.equals("{"))
                        c = 1;
                    if (z.contains("Coordinates")){
                        char[] tmpA = z.toCharArray();
                        int count = 0;
                        String coord = "";
                        for (char p : tmpA) {
                            if (Character.toString(p).equals(")")) {
                                count = 100;
                                co.add(coord);
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
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        popLst();

        //add a new button listener here to open a new activity to add an entry
        Button btn1 = (Button)findViewById(R.id.btn1);
        Button btn2 = (Button)findViewById(R.id.btn2);
        Button btn3 = (Button)findViewById(R.id.btn3);
        Button btn4 = (Button)findViewById(R.id.btn4);
        btn1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //open new activity here
                Intent newAc = new Intent(NewIntent.this, AddEntry.class);
                startActivity(newAc);
            }
        });
        final String finalLine = line.replace("{", "").replace("}", "");
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //do clever email stuff here
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setType("message/rfc822");
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "DIARY ARCHIVE FOR: '"+MainActivity.UserName()+"'");
                emailIntent.putExtra(Intent.EXTRA_TEXT, finalLine);
                try{
                    startActivity(Intent.createChooser(emailIntent, "Pick a provider"));
                }
                catch (Exception e){
                    Toast.makeText(NewIntent.this, "Please add an email account to your email client",
                            Toast.LENGTH_LONG).show();
                }

            }
        });
        btn3.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                //here get all locations add markers and display map
                Intent i = new Intent(NewIntent.this, ViewLocationOnMap.class);
                i.putExtra("activity", "NewIntent");
                startActivity(i);
            }
        });
        btn4.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                //create list of image files and set to static
                //issue is this is all images, need to get only images specific to them.
                File directory = new File(getFilesDir().toString());
                File[] fi = directory.listFiles();
                String [] jpgs = new String[100];
                int count = 0;
                for (File x : fi){
                    if (x.getName().toString().contains(".jpg") && x.getName().contains(MainActivity.UserName())) {
                        jpgs[count] = getFilesDir()+"/"+x.getName().toString();
                        count++;
                    }
                }
                jpgfilelst = jpgs;
                Intent z = new Intent(NewIntent.this, ImageGallery.class);
                startActivity(z);
            }
        });
    }
    public void popLst(){
        list_view = (ListView)findViewById(R.id.listView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.entry_list, titleLst);
        list_view.setAdapter(adapter);
        list_view.setOnItemClickListener(
                new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String value = (String)list_view.getItemAtPosition(position);
                        Intent newc = new Intent(NewIntent.this, DisplayEntry.class);
                        newc.putExtra("entry", value);
                        entryNM = value;
                        startActivity(newc);
                    }
                }
        );
    }
}
