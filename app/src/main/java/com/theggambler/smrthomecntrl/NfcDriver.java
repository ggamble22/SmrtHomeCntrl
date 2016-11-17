package com.theggambler.smrthomecntrl;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareUltralight;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.nio.charset.Charset;

public class NfcDriver
{
    public IntentFilter[] intentFiltersArray;
    NfcAdapter mAdapter;
    public PendingIntent pendingIntent;
    public String[][] techListsArray;

    public NfcDriver(Context context, PendingIntent intent)
    {
        pendingIntent = intent; /*PendingIntent.getActivity(
                context, 0, new Intent(context, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);*/

        mAdapter = NfcAdapter.getDefaultAdapter(context);
        IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);

        try {
            ndef.addDataType("*/*");    /* Handles all MIME based dispatches.
                                       You should specify only the ones that you need. */
        }
        catch (IntentFilter.MalformedMimeTypeException e) {
            throw new RuntimeException("fail", e);
        }
        intentFiltersArray = new IntentFilter[] {ndef };

        techListsArray = new String[][] { new String[] { MifareUltralight.class.getName() } };

    }

    public void onPause(Activity activity) {
        mAdapter.disableForegroundDispatch(activity);
    }

    public void onResume(Activity activity) {
        mAdapter.enableForegroundDispatch(activity, pendingIntent, intentFiltersArray, techListsArray);
    }

    public void receiveData(Activity activity, Parcelable data)
    {
        MifareUltralight mifare = MifareUltralight.get((Tag)data);
        try {
            mifare.connect();
            String sPayload = new String(mifare.readPages(4), Charset.forName("US-ASCII"));
            sPayload = sPayload.substring(9, sPayload.length());
            Log.d(MainActivity.TAG, "Rx: " + sPayload);
            Toast.makeText(activity.getApplicationContext(), "NFC Data: " + sPayload, Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (mifare != null) {
                try {
                    mifare.close();
                }
                catch (IOException e) {
                    Log.e(MainActivity.TAG, "Error closing tag...", e);
                }
            }
        }
    }
}