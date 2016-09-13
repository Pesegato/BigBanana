package com.pesegato.bigbanana.sampleapp;


import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
import com.pesegato.bigbanana.BigBananaReceiver;

public class BananizedAppState extends BaseAppState {

    BigBananaReceiver bbr;

    public BananizedAppState(BigBananaReceiver bbr){
        this.bbr=bbr;
    }

    @Override
    protected void initialize(Application app) {
        // Now create the simple test scene
        for( int i = 0; i < 5; i++ ) {
            TestCube testCube=new TestCube(getApplication().getAssetManager());
            testCube.geom.setLocalTranslation(-8 + i * 4, 0, -4);

            bbr.addFocusable(testCube);

            ((SimpleApplication)getApplication()).getRootNode().attachChild(testCube.geom);
        }

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
