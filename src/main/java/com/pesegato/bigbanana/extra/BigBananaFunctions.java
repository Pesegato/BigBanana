package com.pesegato.bigbanana.extra;

import com.simsilica.lemur.input.Button;
import com.simsilica.lemur.input.FunctionId;
import com.simsilica.lemur.input.InputMapper;

public class BigBananaFunctions {
    public static final String GROUP_BIGBANANA = "BigBanana navigation";

    public static final FunctionId F_START = new FunctionId(GROUP_BIGBANANA, "start game");
    public static final FunctionId F_BACK = new FunctionId(GROUP_BIGBANANA, "back");
    public static final FunctionId F_SELECT = new FunctionId(GROUP_BIGBANANA, "menu");

    public static void initializeDefaultMappings(InputMapper inputMapper) {

        if (!inputMapper.hasMappings(F_START)) {
            //for testing
            //inputMapper.map(F_START, com.jme3.input.KeyInput.KEY_1);
            inputMapper.map(F_START, Button.JOYSTICK_START);
        }
        if (!inputMapper.hasMappings(F_BACK)) {
            //for testing
            //inputMapper.map(F_BACK, com.jme3.input.KeyInput.KEY_2);
            inputMapper.map(F_BACK, Button.JOYSTICK_BUTTON4);
        }
        if (!inputMapper.hasMappings(F_SELECT)) {
            //for testing
            //inputMapper.map(F_BACK, com.jme3.input.KeyInput.KEY_3);
            inputMapper.map(F_SELECT, Button.JOYSTICK_SELECT);
        }
    }
}
