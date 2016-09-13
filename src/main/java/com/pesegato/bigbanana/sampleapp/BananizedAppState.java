package com.pesegato.bigbanana.sampleapp;


import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
import com.jme3.input.MouseInput;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.input.event.MouseMotionEvent;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.pesegato.bigbanana.BigBananaReceiver;
import com.simsilica.lemur.event.DefaultMouseListener;
import com.simsilica.lemur.event.MouseEventControl;

import java.util.ArrayList;

public class BananizedAppState extends BaseAppState implements BigBananaReceiver {

    int focus=0;
    ArrayList<Spatial> focusable=new ArrayList<>();

    @Override
    protected void initialize(Application app) {
        // Now create the simple test scene
        for( int i = 0; i < 5; i++ ) {
            Box b = new Box(1, 1, 1);
            Geometry geom = new Geometry("Box", b);

            Material m = new Material(getApplication().getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
            m.setColor("Color", ColorRGBA.Blue);
            geom.setMaterial(m);
            geom.setLocalTranslation(-8 + i * 4, 0, -4);

            focusable.add(geom);

            MouseEventControl.addListenersToSpatial(geom,
                    new DefaultMouseListener() {
                        @Override
                        protected void click(MouseButtonEvent event, Spatial target, Spatial capture ) {
                            press(target);
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
                            focus(target);
                        }

                        @Override
                        public void mouseExited( MouseMotionEvent event, Spatial target, Spatial capture ) {
                            unfocus(target);
                        }
                    });


            ((SimpleApplication)getApplication()).getRootNode().attachChild(geom);
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
        press(focusable.get(focus));
    }

    @Override
    public void pressedCancel() {
        if (focus!=-1)
            unfocus(focusable.get(focus));
        focus++;
        if (focus>=focusable.size())
            focus=0;
        focus(focusable.get(focus));
    }

    public void focus(Spatial geo){
        Material m = ((Geometry)geo).getMaterial();
        m.setColor("Color", ColorRGBA.Yellow);
        focus=focusable.indexOf(geo);
    }

    public void unfocus(Spatial geo){
        Material m = ((Geometry)geo).getMaterial();
        m.setColor("Color", ColorRGBA.Blue);
    }

    public void press(Spatial geo){
        Material m = ((Geometry)geo).getMaterial();
        m.setColor("Color", ColorRGBA.Red);
    }

    @Override
    public void pressedDown() {
        System.out.println("Down!");
    }
}
