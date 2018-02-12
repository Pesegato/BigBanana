package com.pesegato.bigbanana.sampleapp;

import com.jme3.app.SimpleApplication;
import com.jme3.system.AppSettings;
import com.pesegato.bigbanana.BBBindings;
import com.pesegato.bigbanana.BigBananaAppState;
import com.pesegato.bigbanana.BigBananaPeel;
import com.pesegato.bigbanana.RemapInputAppState;
import com.simsilica.lemur.GuiGlobals;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;


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
        settings.setUseJoysticks(true);
        app.start(); // start the game
    }

    @Override
    public void simpleInitApp() {
        GuiGlobals.initialize(this);
        GuiGlobals globals = GuiGlobals.getInstance();
//        BaseStyles.loadGlassStyle();
        globals.getStyles().setDefaultStyle("glass");
        try {
            prop.load(new FileReader(getFilePath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        getStateManager().attachAll(
                new BigBananaAppState(this),
                new MainMenuAppState());
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
            case "move.up":
                return "W";
            case "move.down":
                return "S";
            case "move.right":
                return "D";
            case "move.left":
                return "A";
            case MY_COOL_ACTION:
                return "P";
        }
        return null;
    }

    @Override
    public String getDefaultPadBind(String key) {
        switch (key) {
            case MY_COOL_ACTION:
                return "Mouse Button 1";
        }
        return null;
    }

}
