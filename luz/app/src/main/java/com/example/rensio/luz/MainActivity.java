package com.example.rensio.luz;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Service;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.telephony.TelephonyManager;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import android.util.Log;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import java.util.regex.Pattern;


public class MainActivity extends AppCompatActivity implements SensorEventListener {


    SensorManager sm;
    TextView tv_passo;
    EditText ips;
    Sensor sensor;
    boolean run = false;

    private static Socket s;
    private static ServerSocket ss;
    private static PrintWriter printWriter;
    private static BufferedReader br;
    private static InputStreamReader isr;
    AccountManager manager;


    String mensagem = "";
    String ip= "";
    String gmail = null;
    String email = "";
    String[] parts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_passo = (TextView) findViewById(R.id.textView2);
        ips = (EditText) findViewById(R.id.Edit);
        sm = (SensorManager) getSystemService(Service.SENSOR_SERVICE);

        sensor = sm.getDefaultSensor(Sensor.TYPE_LIGHT);

        manager = AccountManager.get(this);




    }

    public void conectar(View v) {

        ip = ips.getText().toString();
        conectar cnt = new conectar();
        cnt.execute();


        Toast.makeText(getApplicationContext(), "Conectado!", Toast.LENGTH_LONG).show();
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(ips.getWindowToken(), 0);

    }

    public void send_text(View v) {

        mensagem = tv_passo.getText().toString();
        myTask mt = new myTask();
        mt.execute();

        Toast.makeText(getApplicationContext(), "Analise iniciada!", Toast.LENGTH_LONG).show();
    }


    class myTask extends AsyncTask<Void, Void, Void> {


        protected Void doInBackground(Void... params) {

            mensagem = tv_passo.getText().toString();

            String fabricante = Build.MANUFACTURER;

            myTask mt = new myTask();

            try {

                s = new Socket(ip, 5001);
                printWriter = new PrintWriter(s.getOutputStream());
                printWriter.write(mensagem+" ");
                printWriter.write("- " + fabricante);
                printWriter.flush();
                printWriter.close();
                s.close();


                mt.execute();


            } catch (IOException e) {

                e.printStackTrace();
            }

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }


            return null;

        }
    }


    class conectar extends AsyncTask<Void, Void, Void> {


        protected Void doInBackground(Void... params) {

            String fabricante = Build.MANUFACTURER;


            try {

                s = new Socket(ip, 5001);
                printWriter = new PrintWriter(s.getOutputStream());

                printWriter.write( fabricante+" Conectado");
                printWriter.flush();
                printWriter.close();
                s.close();



            } catch (IOException e) {

                e.printStackTrace();
            }


            return null;

        }
    }


    @Override
    protected void onResume() {

        super.onResume();


        sm.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    protected void onPause() {
        super.onPause();

        sm.unregisterListener(this);
    }


    @Override
    public void onSensorChanged(SensorEvent event) {


        if (event.sensor.getType() == Sensor.TYPE_LIGHT) {

            tv_passo.setText("" + event.values[0]);

        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {


    }
}