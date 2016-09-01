package com.theggambler.smrthomecntrl;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "HNAP Tester";
    private Button sendBtn;
    private Button loginBtn;
    private TextView responseTextView;
    private EditText ipEditText;
    private EditText cmdEditText;
    HttpTransportSE httpTransport;
    SoapSerializationEnvelope envelope;
    SoapObject request;

    Intent intent = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);*/
        System.setProperty("http.keepAlive", "false");

        setContentView(R.layout.activity_main);
        responseTextView = (TextView)findViewById(R.id.responseTextView);
        sendBtn = (Button)findViewById(R.id.sendBtn);
        loginBtn = (Button)findViewById(R.id.loginBtn);
        ipEditText = (EditText)findViewById(R.id.ipEditText);
        cmdEditText = (EditText)findViewById(R.id.cmdEditText);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendCmdOn();
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendCmdOff();
            }
        });

        intent = new Intent(this, W215CntrlService.class);
    }

    public void sendCmdOn()
    {
        intent.putExtra(W215CntrlService.W215_URL, "192.168.0.157");
        intent.putExtra(W215CntrlService.W215_CMD, W215CntrlService.W215_CMD_ON);
        startService(intent);
    }

    public void sendCmdOff()
    {
        intent.putExtra(W215CntrlService.W215_URL, "192.168.0.157");
        intent.putExtra(W215CntrlService.W215_CMD, W215CntrlService.W215_CMD_OFF);
        startService(intent);
    }
}
