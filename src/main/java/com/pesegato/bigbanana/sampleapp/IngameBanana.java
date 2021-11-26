package com.pesegato.bigbanana.sampleapp;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Quad;
import com.pesegato.bigbanana.BBFocusTarget;
import com.simsilica.lemur.input.FunctionId;
import com.simsilica.lemur.input.InputState;

public class IngameBanana {
    public Geometry geo;

    public IngameBanana(AssetManager assetManager, int width, int height) {
        geo = new Geometry("MyQuad", new Quad(width, height));
        Material m = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        m.setColor("Color", ColorRGBA.Yellow);
        geo.setMaterial(m);
        geo.addControl(new AAFocusTarget());
    }

    class AAFocusTarget extends BBFocusTarget {

        @Override
        public void onFocusGain() {
            geo.getMaterial().setColor("Color", ColorRGBA.Blue);
        }

        @Override
        public void onFocusLost() {
            geo.getMaterial().setColor("Color", ColorRGBA.Yellow);
        }

        @Override
        public boolean isFocusable() {
            return true;
        }

        @Override
        public void onValueChanged(FunctionId func, InputState value, double tpf) {
            /*
            if( pressed && value == InputState.Off ) {
                // Do click processing... the mouse does click processing before
                // up processing so we will too
                runClick();
            }
            // Only mapped to one function so no need to distinguish
setPressed(isEnabled() && value == InputState.Positive);
             */
            if (value == InputState.Positive) {
                geo.getMaterial().setColor("Color", ColorRGBA.Red);
            } else {
                geo.getMaterial().setColor("Color", ColorRGBA.Blue);
            }
        }
    }

}
