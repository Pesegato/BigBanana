package com.pesegato.bigbanana.sampleapp;

import com.jme3.app.SimpleApplication;
import com.jme3.system.AppSettings;
import com.pesegato.bigbanana.BigBananaAppState;
import com.pesegato.bigbanana.BigBananaMapping;
import com.pesegato.bigbanana.RemapInputAppState;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.style.BaseStyles;


public class Main extends SimpleApplication {


    public static void main(String[] args) {
        BigBananaMapping.addMapping("my.cool.action");
        try {
            BigBananaAppState.preInit("BigBanana-sampleapp", "mapping",Main.class.getClassLoader());
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
                new BigBananaAppState(),
                new RemapInputAppState());
    }
}
