package com.pesegato.bigbanana.sampleapp;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.pesegato.bigbanana.BigBananaAppState;
import com.pesegato.bigbanana.FocusHandler;

public class TestCube implements FocusHandler {

    Geometry geom;

    TestCube(AssetManager assetM){
        Box b = new Box(1, 1, 1);
        geom = new Geometry("Box", b);

        Material m = new Material(assetM, "Common/MatDefs/Misc/Unshaded.j3md");
        m.setColor("Color", ColorRGBA.Blue);
        BigBananaAppState.focusMap.put(geom, this);
        geom.setMaterial(m);
    }

    public void focus(){
        Material m = geom.getMaterial();
        m.setColor("Color", ColorRGBA.Yellow);
    }

    public void unfocus(){
        Material m = geom.getMaterial();
        m.setColor("Color", ColorRGBA.Blue);
    }

    public void press(){
        Material m = geom.getMaterial();
        m.setColor("Color", ColorRGBA.Red);
    }

}
