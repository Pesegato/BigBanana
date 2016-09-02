package com.pesegato.bigbanana.sampleapp;


import com.jme3.app.SimpleApplication;
import com.jme3.app.StatsAppState;
import com.jme3.renderer.RenderManager;
import com.pesegato.bigbanana.BigBananaAppState;
import com.simsilica.lemur.GuiGlobals;

/** Sample 1 - how to get started with the most simple JME 3 application.
 * Display a blue 3D cube and view from all sides by
 * moving the mouse and pressing the WASD keys. */
public class Main extends SimpleApplication {

    public static void main(String[] args){
        Main app = new Main();
        app.start(); // start the game
    }

    public Main() {
        super(new StatsAppState());
    }

    @Override
    public void simpleInitApp() {

        // Initialize Lemur subsystems and setup the default
        // camera controls.
        GuiGlobals.initialize(this);

        BigBananaAppState bbas=new BigBananaAppState();
        BananizedAppState ban=new BananizedAppState();
        bbas.setReceiver(ban);
        stateManager.attachAll(bbas,ban);

    }

    @Override
    public void simpleUpdate(float tpf) {
        //TODO: add update code
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
}
