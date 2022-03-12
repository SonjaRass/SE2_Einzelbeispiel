package com.example.se2_einzelbeispiel;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.AsyncTaskLoader;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    Button send;
    Button sort;
    EditText input;
    TextView answer;

    Tcp client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//legt Richtlinen fest welche Aktion im Thread ausgeführt werden soll
        StrictMode.ThreadPolicy check = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(check);

        send = (Button) findViewById((R.id.send));
        sort = (Button) findViewById(R.id.sort);
        input = findViewById(R.id.input);
        answer = findViewById(R.id.answer);

        new connect().execute("execute");

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = input.getText().toString();

                if (client != null) {
                    client.send(message);
                }
                send.setEnabled(false);
            }
        });

        sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               int[]mNr = new int[input.length()];
               for(int i = 0; i<input.length(); i++){
                   mNr[i] = Character.getNumericValue(input.getText().charAt(i));
               }
               answer.setText(sort(mNr));

            }
        });

    }
     public String sort(int[] mNr){
      String sortFinish = new String();
         ArrayList<Integer> even = new ArrayList<>();
         ArrayList<Integer> uneven = new ArrayList<>();

         for (int i = 0; i<mNr.length; i++){
             if(mNr[i]%2 == 1){
                 uneven.add(mNr[i]);
             }

             if(mNr[i]%2 == 0){
                 even.add(mNr[i]);
             }
         }
         Collections.sort(even);
         Collections.sort(uneven);

         for (int i = 0; i<even.size();i++){
             sortFinish = sortFinish + even.get(i).toString();
         }

         for (int i = 0; i <uneven.size(); i++){
             sortFinish = sortFinish+uneven.get(i).toString();
         }
         return sortFinish;
     }


    protected class connect extends AsyncTask<String,String,Tcp> {
         //im Hintergrund - laufend abrufen auf neue Nachrichten
        protected Tcp doInBackground(String...message){
            client = new Tcp(new Tcp.OnMessageReceived(){

                public void messageReceived(String message) {
                     publishProgress(message);
                }
            });
            client.run();
            return null;


        }

        protected void onProgressUpdate(String...value){
            super.onProgressUpdate(value);
            String back = value[0]; //message from server wird in array liste gespeichert
            answer.setText(back);

        }
    }


    }
