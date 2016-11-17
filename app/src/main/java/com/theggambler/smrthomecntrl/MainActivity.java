package com.theggambler.smrthomecntrl;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.theggambler.smrthomecntrl.hass.HassDriver;
import com.theggambler.smrthomecntrl.mqtt.MqttDriver;


public class MainActivity extends Activity {
    public static final String TAG = "HNAP Tester";

    private Button onBtn;
    private Button offBtn;
    private Button infoBtn;
    private TextView responseTextView;

    private MqttDriver mMqttDriver;
    private NfcDriver mNfcDriver;
    private HassDriver hassDriver;

    PendingIntent pendingIntent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        responseTextView = (TextView) findViewById(R.id.responseTextView);
        onBtn = (Button) findViewById(R.id.onBtn);
        offBtn = (Button) findViewById(R.id.offBtn);
        infoBtn = (Button) findViewById(R.id.infoBtn);

        onBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendCmdOn();
            }
        });
        offBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendCmdOff();
            }
        });

        infoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendGetCmd();
            }
        });

        pendingIntent = PendingIntent.getActivity(
                this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        mNfcDriver = new NfcDriver(this.getApplicationContext(), pendingIntent);
        hassDriver = new HassDriver(this.getApplicationContext());
    }

    public void sendCmdOn() {
        //mMqttDriver.sendBedroom();
        hassDriver.turnOnLight();
    }

    public void sendCmdOff() {
        hassDriver.turnOffLight();
    }

    public void sendGetCmd() {
        hassDriver.getLightState();
    }

    @Override
    public void onPause() {
        super.onPause();
        mNfcDriver.onPause(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mNfcDriver.onResume(this);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        mNfcDriver.receiveData(this, intent.getParcelableExtra(NfcAdapter.EXTRA_TAG));
    }

}
