package com.example.oscarmeanwell.cwk1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    Boolean flag = false;
    Boolean pwdFlag = false;
    String[] usrA;
    String[] usrpA;

    private static String username;
    private static String SALT = "x8i}R1Mk4veS\\~z/ZPq^";
    public static String UserName(){
        return username;
    }
    public static String Salt(){
        return SALT;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        super.setTitle("My Diary - Login");
        Button button = (Button) findViewById(R.id.button);
        Button btn = (Button) findViewById(R.id.btn);
        final EditText etUsrName = (EditText) findViewById(R.id.etUsr);
        final EditText etPwd = (EditText) findViewById(R.id.etPwd);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //change to read from local app storage
                FileInputStream br = null;
                String line = "";
                String line2 = "";
                username = etUsrName.getText().toString();
                try{
                    br = openFileInput("UserNames.txt");
                    int x;
                    while (( x = br.read()) != -1) {
                        line = line + Character.toString((char) x);
                    }
                    usrA = line.split(System.getProperty("line.separator"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try{
                    br = openFileInput("Passwords.txt");
                    int x;
                    while (( x = br.read()) != -1){
                        line2 = line2 + Character.toString((char) x);
                    }
                    //load usernames into array 
                    usrpA = line2.split(System.getProperty("line.separator"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //loop through usrA and find inputted username index
                //compare password at index with inputted password
                //set flag x i
                try{
                    int indexo = Arrays.asList(usrA).indexOf(etUsrName.getText().toString());
                    if (etPwd.getText().toString().equals(usrpA[indexo])){
                        flag = true;
                    }
                    else if (indexo != -1){
                        //if user exists put password incorrect
                        pwdFlag = true;
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                
                if (flag) {
                    Intent newAc = new Intent(MainActivity.this, NewIntent.class);
                    startActivity(newAc);
                } else if (pwdFlag) {
                    Toast.makeText(MainActivity.this, "Password Incorrect",
                            Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(MainActivity.this, "UserName Incorrect",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newAc = new Intent(MainActivity.this, AddUser.class);
                startActivity(newAc);
            }
        });
    }
}
