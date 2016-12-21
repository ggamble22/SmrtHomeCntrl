package com.theggambler.smrthomecntrl.hass;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.RequestFuture;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by Gene on 11/10/2016.
 */

public class HassDriver extends Service implements Response.Listener<Node>, Response.ErrorListener {
    private static String TAG = "HASS_LIB";
    private static String USER = "ggamble22";
    private static String PASS = /*"12345"; //*/"Gag032290";//*/

    private final String mHassUrl = /*"https://posttestserver.com/post.php?dir=gg_hass";//*/ "https://ggamble.duckdns.org";//*/
    private String serviceApiAddress = "/api/services";
    private String stateApiAddress = "/api/states/";
    private String domainString = "/light";
    private String commandOffString = "/turn_off";
    private String commandOnString = "/turn_on";
    private String bedroomString = "light.Bedroom";

    private Context mContext;
    private String mURL;

    private Node mNode;
    private GsonRequest<Node> mRequest;

    private Map<String, String> headers;
    HassState mState;

    private final Gson gson = new Gson();

    public HassDriver(Context context)
    {
        mContext = context;
        headers = new HashMap<String, String>();

        headers.put("x-ha-access", PASS);
        headers.put("Content-Type", "application/json");

        mNode = new Node();
        mNode.entity_id = bedroomString;
    }

    public void turnOnLight()
    {
        mURL = mHassUrl + serviceApiAddress + domainString + commandOnString;
        mRequest = new GsonRequest<>(mURL, Request.Method.POST, mNode, Node.class, headers, this, this);
        VolleyManager.get(this.mContext).enqueue(mRequest);
    }

    public void turnOffLight()
    {
        mURL = mHassUrl + serviceApiAddress + domainString + commandOffString;
        mRequest = new GsonRequest<>(mURL, Request.Method.POST, mNode, Node.class, headers, this, this);
        VolleyManager.get(this.mContext).enqueue(mRequest);
    }

    public void getLightState()
    {


        new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground( final Void ... params ) {
                // something you know that will take a few seconds
                mURL = mHassUrl + stateApiAddress + bedroomString;

                mState = new HassState();

                RequestFuture<HassState> future = RequestFuture.newFuture();
                GsonRequest<HassState> request = new GsonRequest<>(mURL, Request.Method.GET, null, HassState.class, headers, future, future);
                VolleyManager.get(mContext).enqueue(request);

                try {
                    HassState response = future.get(5, TimeUnit.SECONDS); // Blocks for at most 10 seconds.
                    System.out.print(response);
                } catch (InterruptedException e) {
                    // Exception handling
                } catch (ExecutionException e) {
                    // Exception handling
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }


                return null;
            }

            @Override
            protected void onPostExecute( final Void result ) {
                // continue what you are doing...
            }
        }.execute();

    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(Node response) {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
