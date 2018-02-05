package com.pesegato.bigbanana.sampleapp;

import com.jme3.app.SimpleApplication;
import com.jme3.system.AppSettings;
import com.pesegato.bigbanana.BBBindings;
import com.pesegato.bigbanana.BigBananaAppState;
import com.pesegato.bigbanana.BigBananaPeel;
import com.pesegato.bigbanana.RemapInputAppState;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.style.BaseStyles;

import java.io.InputStream;


public class Main extends SimpleApplication implements BigBananaPeel{

    public static final String MY_COOL_ACTION="my.cool.action";

    public static void main(String[] args) {
        BBBindings.addMapping(MY_COOL_ACTION);
        try {
            //BigBananaAppState.preInit("BigBanana-sampleapp", "mapping",Main.class.getClassLoader(),new Main());
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        getStateManager().attachAll(
                new BigBananaAppState(this),
                new RemapInputAppState());
    }

    @Override
    public String getDefaultBind(String key) {
        switch (key) {
            case "keyboard.move.up":
                return "W";
            case "keyboard.move.down":
                return "S";
            case "keyboard.move.right":
                return "D";
            case "keyboard.move.left":
                return "A";
            case MY_COOL_ACTION:
                return "P";
        }
        return null;
    }

}
