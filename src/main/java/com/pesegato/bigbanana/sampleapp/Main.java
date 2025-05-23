package com.pesegato.bigbanana.sampleapp;

import com.jme3.app.SimpleApplication;
import com.jme3.system.AppSettings;
import com.pesegato.bigbanana.*;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.input.BBGuiGlobals;
import com.simsilica.lemur.style.BaseStyles;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import static com.pesegato.bigbanana.BigBananaAppState.*;


public class Main extends SimpleApplication implements BigBananaPeel {

    public static final String MY_COOL_ACTION = "my.cool.action";
    public static final String ANOTHER_ACTION = "another.action";

    public static void main(String[] args) {
        BBBindings.addKeyMapping(MY_COOL_ACTION);
        BBBindings.addPadMapping(MY_COOL_ACTION);
        BBBindings.addPadMapping(ANOTHER_ACTION);
        Main app = new Main();
        app.setShowSettings(false);
        AppSettings settings = new AppSettings(true);
        //No need to use setUseJoysticks!
        //settings.setUseJoysticks(true);
        app.setSettings(settings);
        app.start(); // start the game
    }

    @Override
    public void simpleInitApp() {
        BBGuiGlobals.initialize(this);
        //GuiGlobals.initialize(this);
        GuiGlobals globals = GuiGlobals.getInstance();
        BaseStyles.loadGlassStyle();
        globals.getStyles().setDefaultStyle("glass");
        try {
            prop.load(new FileReader(getFilePath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        getStateManager().attachAll(
                new BigBananaAppState(this),
                new SampleMenuAppState(SampleMenuAppState.class));
    }

    Properties prop = new Properties();

    @Override
    public String getFilePath() {
        return "mapping.properties";
    }

    @Override
    public Properties getProperties() {
        return prop;
    }

    @Override
    public String getDefaultKeyBind(String key) {
        switch (key) {
            case BB_MOVEUP:
                return "W";
            case BB_MOVEDOWN:
                return "S";
            case BB_MOVERIGHT:
                return "D";
            case BB_MOVELEFT:
                return "A";
            case MY_COOL_ACTION:
                return "P";
        }
        return null;
    }

    @Override
    public BBInput getDefaultPadBind(String key) {
        switch (key) {
            case MY_COOL_ACTION:
                return BBInput.BB_BUTTON_X;
            case ANOTHER_ACTION:
                return BBInput.BB_BUTTON_Y;
        }
        return null;
    }

    /*
    @Override
    public String getDefaultAxisBind(String key) {
        switch (key) {
            case BB_MOVE_HORIZONTAL:
                return "Joystick (left) Left/Right";
            case BB_MOVE_VERTICAL:
                return "Joystick (left) Up/Down";
        }
        return null;
    }
    */
}
