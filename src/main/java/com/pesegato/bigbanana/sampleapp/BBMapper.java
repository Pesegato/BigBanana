package com.pesegato.bigbanana.sampleapp;


import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.input.KeyInput;
import com.pesegato.bigbanana.InputBindings;

public class BBMapper extends BaseAppState{

    /*
    This is a placeholder. Input remapping should be game specific
     */

    public static void init(){
        try {
            InputBindings.MOVEUP_K = KeyInput.class.getField("KEY_W").getInt(null);
            InputBindings.MOVEDN_K = KeyInput.class.getField("KEY_S").getInt(null);
            InputBindings.MOVERI_K = KeyInput.class.getField("KEY_D").getInt(null);
            InputBindings.MOVELE_K = KeyInput.class.getField("KEY_A").getInt(null);
            InputBindings.OK_KEY = KeyInput.class.getField("KEY_RETURN").getInt(null);
            InputBindings.CANCEL_KEY = KeyInput.class.getField("KEY_BACK").getInt(null);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void initialize(Application app) {
    }

    @Override
    protected void cleanup(Application app) {

    }

    @Override
    protected void onEnable() {

    }

    @Override
    protected void onDisable() {

    }
}
