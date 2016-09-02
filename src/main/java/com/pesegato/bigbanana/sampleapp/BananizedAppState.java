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

public class BananizedAppState extends BaseAppState implements BigBananaReceiver {

    int focus=0;
    Material m[]=new Material[5];

    @Override
    protected void initialize(Application app) {
        // Now create the simple test scene
        for( int i = 0; i < 5; i++ ) {
            Box b = new Box(1, 1, 1);
            Geometry geom = new Geometry("Box", b);

            m[i] = new Material(getApplication().getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
            m[i].setColor("Color", ColorRGBA.Blue);
            geom.setMaterial(m[i]);
            geom.setLocalTranslation(-8 + i * 4, 0, -4);


            MouseEventControl.addListenersToSpatial(geom,
                    new DefaultMouseListener() {
                        @Override
                        protected void click(MouseButtonEvent event, Spatial target, Spatial capture ) {
                            Material m = ((Geometry)target).getMaterial();
                            m.setColor("Color", ColorRGBA.Red);
                            if( event.getButtonIndex() == MouseInput.BUTTON_LEFT ) {
                                target.move(0, 0.1f, 0);
                            } else {
                                target.move(0, -0.1f, 0);
                            }
                        }

                        @Override
                        public void mouseEntered(MouseMotionEvent event, Spatial target, Spatial capture ) {
                            Material m = ((Geometry)target).getMaterial();
                            m.setColor("Color", ColorRGBA.Yellow);
                        }

                        @Override
                        public void mouseExited( MouseMotionEvent event, Spatial target, Spatial capture ) {
                            Material m = ((Geometry)target).getMaterial();
                            m.setColor("Color", ColorRGBA.Blue);
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
        m[focus].setColor("Color", ColorRGBA.Red);
        System.out.println("OK!");
    }

    @Override
    public void pressedCancel() {
        System.out.println("next! Focus: "+focus);
        m[focus].setColor("Color", ColorRGBA.Blue);
        focus++;
        if (focus>4)
            focus=0;
        m[focus].setColor("Color", ColorRGBA.Yellow);
    }

    @Override
    public void pressedDown() {
        System.out.println("Down!");
    }
}
