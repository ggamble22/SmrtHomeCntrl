package com.theggambler.smrthomecntrl.mqtt;


import android.content.Context;
import android.util.Log;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;

/**
 * Created by Gene on 10/1/2016.
 */

public class MqttDriver {
    private static String TAG = "MQTT_APP";
    private static String USER = "ggamble22";
    private static String PASS = "df2e55c4b8d443d4846563b17cf6b41e";
    private Context m_context;
    private boolean m_bUse_3_1 = false;
    MqttAndroidClient client;
    boolean m_bStatus = false;

    String m_url;
    String m_id;

    public MqttDriver(Context context, String url) {
        m_context = context;
        m_url = url;
        m_id = MqttClient.generateClientId();
        int m_port = 8883;

        try {

            client = new MqttAndroidClient(m_context, m_url, m_id);
            client.setCallback(new MqttCallbackExtended() {
                @Override
                public void connectComplete(boolean reconnect, String serverURI) {
                    subscribe();
                }

                @Override
                public void connectionLost(Throwable cause) {

                }

                @Override
                public void messageArrived(String topic, MqttMessage msg) throws Exception {
                    Log.d(TAG, "Rx Topic:" + topic);

                    Log.d(TAG, "Payload: " + new String(msg.getPayload()));

                    if(topic == "ggamble/feeds/bedroom-light") {

                        if(new String(msg.getPayload()) == "ON")
                        {
                            //m_bStatus = true;
                        }
                        else if(new String(msg.getPayload()) == "OFF")
                        {
                            //m_bStatus = false;
                        }
                    }
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {

                }
            });


            MqttConnectOptions options = new MqttConnectOptions();
            options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_DEFAULT);
            options.setUserName(USER);
            options.setPassword(PASS.toCharArray());
            if(m_bUse_3_1) {
                options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1);
            }

            client.connect(options,context, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    Log.d(TAG, "onSuccess");

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    Log.e(TAG, "onFailure", exception);

                }
            });

        } catch(MqttException ex) {
            Log.d(TAG, "ERROR");
        }
    }

    public void sendBedroom()
    {
        String topic = "ggamble22/feeds/bedroom-light";
        String payload;

        if(m_bStatus) {
            payload = "OFF";
            m_bStatus = false;
        }
        else {
            payload = "ON";
            m_bStatus = true;
        }

        sendMessage(topic, payload);
    }

    public void sendMessage(String topic, String payload)
    {
        byte[] encodedPayload = new byte[0];
        try {
            encodedPayload = payload.getBytes("UTF-8");
            MqttMessage message = new MqttMessage(encodedPayload);
            client.publish(topic, message);
        } catch (UnsupportedEncodingException | MqttException e) {
            e.printStackTrace();
        }
    }

    public void subscribe()
    {
        String topic = "ggamble22/feeds/bedroom-light";
        int qos = 1;
        try {
            IMqttToken subToken = client.subscribe(topic, qos);
            subToken.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // The message was published
                    Log.d(TAG, "Subscribed");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken,
                                      Throwable exception) {
                    // The subscription could not be performed, maybe the user was not
                    // authorized to subscribe on the specified topic e.g. using wildcards
                    Log.d(TAG, "Subscribe Error", exception);
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
