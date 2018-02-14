/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pesegato.bigbanana;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.input.KeyInput;
import com.jme3.input.KeyNames;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.input.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

import static com.simsilica.lemur.focus.FocusNavigationFunctions.*;

/**
 * @author Pesegato
 */
public class BigBananaAppState extends BaseAppState {

    static Logger log = LoggerFactory.getLogger(BigBananaAppState.class);

    public static final String BB_MOVEUP = "move.up";
    public static final String BB_MOVEDOWN = "move.down";
    public static final String BB_MOVERIGHT = "move.right";
    public static final String BB_MOVELEFT = "move.left";

    public static Axis PAD_MOVE_VERTICAL;
    public static Axis PAD_MOVE_HORIZONTAL;
    public static int KEYBOARD_MOVE_UP;
    public static int KEYBOARD_MOVE_DOWN;
    public static int KEYBOARD_MOVE_RIGHT;
    public static int KEYBOARD_MOVE_LEFT;

    public static int BIGBANANA_KEYBOARD[];
    public static Button BIGBANANA_BUTTON[];

    BigBananaPeel peel;

    public BigBananaAppState(BigBananaPeel peel) {
        this.peel = peel;
    }

    private int getKeyboardInput(String key, String deflt) {
        String val = peel.getProperties().getProperty("keyboard." + key, deflt);
        for (int i = 0; i < 0xff; i++) {
            if (KeyNames.getName(i).equals(val)) {
                return i;
            }
        }
        //return KeyInput.class.getField(props.getProperty(key, deflt)).getInt(null);
        return KeyInput.KEY_UNKNOWN;
    }

    private Button getButtonInput(String key, String deflt) throws IllegalAccessException {
        String val = peel.getProperties().getProperty("pad." + key, deflt);
        for (Field field : Button.class.getFields()) {
            Button button = (Button) field.get(null);
            if (button.getName().equals(val))
                return button;
        }
        return null;
    }

    @Override
    protected void initialize(Application app) {
        try {
            BigBananaAppState.KEYBOARD_MOVE_UP = getKeyboardInput(BB_MOVEUP, peel.getDefaultKeyBind(BB_MOVEUP));
            BigBananaAppState.KEYBOARD_MOVE_DOWN = getKeyboardInput(BB_MOVEDOWN, peel.getDefaultKeyBind(BB_MOVEDOWN));
            BigBananaAppState.KEYBOARD_MOVE_RIGHT = getKeyboardInput(BB_MOVERIGHT, peel.getDefaultKeyBind(BB_MOVERIGHT));
            BigBananaAppState.KEYBOARD_MOVE_LEFT = getKeyboardInput(BB_MOVELEFT, peel.getDefaultKeyBind(BB_MOVELEFT));

            BIGBANANA_KEYBOARD = new int[BBBindings.getKeySize()];
            BIGBANANA_BUTTON = new Button[BBBindings.getPadSize()];
            for (int i = 0; i < BBBindings.getKeySize(); i++) {
                String action = BBBindings.keybbmapping.get(i);
                BIGBANANA_KEYBOARD[i] = getKeyboardInput(action, peel.getDefaultKeyBind(action));
            }
            for (int i = 0; i < BBBindings.getPadSize(); i++) {
                String action = BBBindings.padbbmapping.get(i);
                BIGBANANA_BUTTON[i] = getButtonInput(action, peel.getDefaultPadBind(action));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        InputMapper inputMapper = GuiGlobals.getInstance().getInputMapper();
        inputMapper.map(F_X_AXIS, InputState.Negative, PAD_MOVE_VERTICAL);
        inputMapper.map(F_X_AXIS, KEYBOARD_MOVE_RIGHT);
        inputMapper.map(F_X_AXIS, InputState.Negative, KEYBOARD_MOVE_LEFT);
        inputMapper.map(F_Y_AXIS, InputState.Negative, PAD_MOVE_HORIZONTAL);
        inputMapper.map(F_Y_AXIS, KEYBOARD_MOVE_UP);
        inputMapper.map(F_Y_AXIS, InputState.Negative, KEYBOARD_MOVE_DOWN);
    }

    @Override
    protected void cleanup(Application app) {
    }

    @Override
    protected void onEnable() {
        GuiGlobals.getInstance().getInputMapper().activateGroup(UI_NAV);
    }

    @Override
    protected void onDisable() {
        GuiGlobals.getInstance().getInputMapper().deactivateGroup(UI_NAV);
    }
}
