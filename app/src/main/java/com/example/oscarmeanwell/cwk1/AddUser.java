package com.example.oscarmeanwell.cwk1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Arrays;

public class AddUser extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        super.setTitle("Diary - User Creation");
        final Button btn1 = (Button)findViewById(R.id.btn1);
        final EditText usrnm = (EditText)findViewById(R.id.etUsrNm);
        final EditText pwd1 = (EditText)findViewById(R.id.etpwd1);
        final EditText pwd2 = (EditText)findViewById(R.id.etpwd2);
        btn1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //if username in file.
                FileInputStream br = null;
                String line = "";
                try {
                    br = openFileInput("UserNames.txt");
                    int x;
                    while ((x = br.read()) != -1){
                        line = line + Character.toString((char)x);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String[] lines = line.split(System.getProperty("line.separator"));
                if (Arrays.asList(lines).contains(usrnm.getText().toString())){
                    Toast.makeText(AddUser.this, "User Name already exists. Please choose a new one",
                            Toast.LENGTH_LONG).show();
                }
                else if (usrnm.getText().toString().length()<4){
                    Toast.makeText(AddUser.this, "User Name must be longer than 4 Characters",
                            Toast.LENGTH_LONG).show();
                }
                else if (pwd1.getText().toString().isEmpty() || pwd2.getText().toString().isEmpty()){
                    Toast.makeText(AddUser.this, "Both password fields must be filled",
                            Toast.LENGTH_LONG).show();
                }
                else if (!pwd1.getText().toString().equals(pwd2.getText().toString()))
                {
                    Toast.makeText(AddUser.this, "Passwords do not match",
                            Toast.LENGTH_LONG).show();
                }
                else if (pwd1.getText().toString().length()<4){
                    Toast.makeText(AddUser.this, "Password must be longer than 4 Characters",
                            Toast.LENGTH_LONG).show();
                }
                //if passwords match
                else{
                    //if passwords match, add username and password to file
                    FileOutputStream outputStream;
                    //construct the string to write
                    String tmp = usrnm.getText().toString() + System.getProperty("line.separator");
                    String tmpp = pwd1.getText().toString()
                            + System.getProperty("line.separator");

                    try{
                        outputStream = openFileOutput("UserNames.txt", Context.MODE_APPEND);
                        outputStream.write(tmp.getBytes());
                        outputStream.close();
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                    try{
                        outputStream = openFileOutput("Passwords.txt", Context.MODE_APPEND);
                        outputStream.write(tmpp.getBytes());
                        outputStream.close();
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                    Intent newAc = new Intent(AddUser.this, MainActivity.class);
                    startActivity(newAc);
                }
            }
        });
    }
}
