package com.theggambler.smrthomecntrl;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Gene on 8/31/2016.
 */
public class FluxCntrlService {

    public static final  Map<Integer, String> COLOR_MAP;

    static {
        COLOR_MAP = new HashMap<Integer, String>();
        COLOR_MAP.put(0x25, "seven_color_cross_fade");
        COLOR_MAP.put(0x26, "redGradualChange");
        COLOR_MAP.put(0x27, "green_gradual_change");
        COLOR_MAP.put(0x28, "blue_gradual_change");
        COLOR_MAP.put(0x29, "yellow_gradual_change");
        COLOR_MAP.put(0x2a, "cyan_gradual_change");
        COLOR_MAP.put(0x2b, "purple_gradual_change");
        COLOR_MAP.put(0x2c, "white_gradual_change");
        COLOR_MAP.put(0x2d, "red_green_cross_fade");
        COLOR_MAP.put(0x2e, "red_blue_cross_fade");
        COLOR_MAP.put(0x2f, "green_blue_cross_fade");
        COLOR_MAP.put(0x30, "seven_color_strobe_flash");
        COLOR_MAP.put(0x31, "red_strobe_flash");
        COLOR_MAP.put(0x32, "green_strobe_flash");
        COLOR_MAP.put(0x33, "blue_stobe_flash");
        COLOR_MAP.put(0x34, "yellow_strobe_flash");
        COLOR_MAP.put(0x35, "cyan_strobe_flash");
        COLOR_MAP.put(0x36, "purple_strobe_flash");
        COLOR_MAP.put(0x37, "white_strobe_flash");
        COLOR_MAP.put(0x38, "seven_color_jumping");
    }

    private class WifiLight
    {
        String mIP;
        int mPort = 0;

        WifiLight(String ip, int port)
        {
            try {
                mIP = ip;
            }catch (NullPointerException e)
            {
                e.printStackTrace();
            }
            mPort = port;
        }

        private String determineMode(int wwlevel, int code)
        {
            String mode = "";

            if(code == 0x61 || code == 0x62) {
                if(wwlevel != 0)
                    mode = "ww";
                else
                    mode = "color";
            }
            else if(code == 0x60) {
                mode = "custom";
            }
            else if(COLOR_MAP.containsKey(code)){
                mode = "preset";
            }

            return mode;
        }

        private boolean refreshState()
        {
            boolean state = false;

            return state;
        }
    }
}

