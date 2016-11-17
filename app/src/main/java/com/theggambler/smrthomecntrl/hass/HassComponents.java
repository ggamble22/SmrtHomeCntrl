package com.theggambler.smrthomecntrl.hass;

/**
 * Created by Gene on 11/14/2016.
 */

public class HassComponents {

    public static class HassNodeComponent{
        private HassAttributes attributes;
        private String entity_id;
        private String last_changed;
        private String last_updated;
        private String state;
    }

    public static class HassAttributes{
        private boolean auto;
        private String[] entity_id;
        private String friendly_name;
        private boolean hidden;
        private int order;
        private String supported_features;
    }
}
