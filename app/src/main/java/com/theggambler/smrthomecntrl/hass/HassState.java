package com.theggambler.smrthomecntrl.hass;

import com.android.volley.Response;

import java.util.Arrays;

/**
 * Created by Gene on 11/13/2016.
 */

public class HassState implements Response.Listener<HassState> {
    private String entity_id;
    private String state;
    private String last_changed;
    private HassAttributes attributes;

    @Override
    public void onResponse(HassState response) {
        this.entity_id = response.entity_id;
        this.state = response.state;
        this.last_changed = response.last_changed;
        this.attributes = response.attributes;

        System.out.print(this.toString());
    }

    @Override
    public String toString()
    {
        return "\r\nEntity ID: " + entity_id + "\r\nState: " + state + "\r\nAttributes: " + attributes.toString();
    }

    public static class HassAttributes {
        int brightness;
        String friendly_name;
        int[] rgb_color;
        int supported_features;

        @Override
        public String toString()
        {
            return "\r\n\tBrightness: " + brightness + "\r\n\tFriendly_Name: " + friendly_name + "\r\n\tRGB_Color: " + Arrays.toString(rgb_color) + "\r\n\tSupported_Features: " + supported_features;
        }
    }
}
