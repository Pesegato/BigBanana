package com.pesegato.bigbanana.sampleapp;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
import com.jme3.scene.Node;
import com.pesegato.bigbanana.BBFocusTraversal;
import com.simsilica.lemur.GuiGlobals;

public class InGameAppState extends BaseAppState {
    @Override
    protected void initialize(Application app) {
        Node gui = ((SimpleApplication) getApplication()).getGuiNode();
        IngameBanana b[][] = new IngameBanana[3][3];
        BBFocusTraversal bbft = new BBFocusTraversal();
        gui.addControl(bbft);
        for (int i = 0; i < b.length; i++) {
            for (int j = 0; j < b[i].length; j++) {
                b[i][j] = new IngameBanana(app.getAssetManager(),50,50);
                b[i][j].geo.setLocalTranslation(100 + i * 100, 400 - j * 100, 0);
                gui.attachChild(b[i][j].geo);
                bbft.addFocusable(b[i][j].geo, j, i, 1, 1);
            }
        }
        IngameBanana bottom=new IngameBanana(app.getAssetManager(),250,50);
        bottom.geo.setLocalTranslation(100,100,0);
        gui.attachChild(bottom.geo);
        bbft.addFocusable(bottom.geo,3,0,3,1);

        GuiGlobals.getInstance().requestFocus(b[0][0].geo);

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
