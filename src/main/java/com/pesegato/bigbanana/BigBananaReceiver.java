package com.pesegato.bigbanana;


import com.jme3.input.event.MouseButtonEvent;
import com.jme3.input.event.MouseMotionEvent;
import com.jme3.scene.Spatial;
import com.simsilica.lemur.event.DefaultMouseListener;
import com.simsilica.lemur.event.MouseEventControl;

import java.util.ArrayList;

public class BigBananaReceiver {

    int focus=0;
    ArrayList<FocusHandler> focusable=new ArrayList<>();

    public void addFocusable(FocusHandler fc){
        focusable.add(fc);
        System.out.println("added "+fc);
        MouseEventControl.addListenersToSpatial(fc.getGeometry(),
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

    }

    public void pressedOK() {
        focusable.get(focus).press();
    }


    public void pressedNext() {
        if (focus!=-1)
            focusable.get(focus).unfocus();
        focus++;
        if (focus>=focusable.size())
            focus=0;
        focusable.get(focus).focus();
    }

    public void pressedDown() {
        System.out.println("Down!");
    }
}
