package com.pesegato.bigbanana.sampleapp;


import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.input.event.MouseMotionEvent;
import com.jme3.scene.Spatial;
import com.pesegato.bigbanana.BigBananaAppState;
import com.pesegato.bigbanana.BigBananaReceiver;
import com.pesegato.bigbanana.FocusHandler;
import com.simsilica.lemur.event.DefaultMouseListener;
import com.simsilica.lemur.event.MouseEventControl;

import java.util.ArrayList;

public class BananizedAppState extends BaseAppState implements BigBananaReceiver {

    int focus=0;
    ArrayList<FocusHandler> focusable=new ArrayList<>();

    @Override
    protected void initialize(Application app) {
        // Now create the simple test scene
        for( int i = 0; i < 5; i++ ) {
            TestCube testCube=new TestCube(getApplication().getAssetManager());
            testCube.geom.setLocalTranslation(-8 + i * 4, 0, -4);

            focusable.add(testCube);

            MouseEventControl.addListenersToSpatial(testCube.geom,
                    new DefaultMouseListener() {
                        @Override
                        protected void click(MouseButtonEvent event, Spatial target, Spatial capture ) {
                            FocusHandler fc=BigBananaAppState.focusMap.get(target);
                            fc.press();
                            /*
                            button filtering is beyond the current scope of BigBanana
                            if( event.getButtonIndex() == MouseInput.BUTTON_LEFT ) {
                                target.move(0, 0.1f, 0);
                            } else {
                                target.move(0, -0.1f, 0);
                            }
                            */
                        }

                        @Override
                        public void mouseEntered(MouseMotionEvent event, Spatial target, Spatial capture ) {
                            FocusHandler fc= BigBananaAppState.focusMap.get(target);
                            fc.focus();
                            focus=focusable.indexOf(fc);
                        }

                        @Override
                        public void mouseExited( MouseMotionEvent event, Spatial target, Spatial capture ) {
                            FocusHandler fc=BigBananaAppState.focusMap.get(target);
                            fc.unfocus();
                        }
                    });


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

    @Override
    public void pressedOK() {
        focusable.get(focus).press();
    }

    @Override
    public void pressedCancel() {
        if (focus!=-1)
            focusable.get(focus).unfocus();
        focus++;
        if (focus>=focusable.size())
            focus=0;
        focusable.get(focus).focus();
    }


    @Override
    public void pressedDown() {
        System.out.println("Down!");
    }

}
