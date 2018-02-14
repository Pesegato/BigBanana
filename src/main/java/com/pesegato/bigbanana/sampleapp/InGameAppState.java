package com.pesegato.bigbanana.sampleapp;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
import com.jme3.scene.Node;
import com.pesegato.bigbanana.BBFocusTraversal;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.input.FunctionId;
import com.simsilica.lemur.input.InputMapper;
import com.simsilica.lemur.input.InputState;
import com.simsilica.lemur.input.StateFunctionListener;

import static com.pesegato.bigbanana.extra.BigBananaFunctions.F_BACK;

public class InGameAppState extends BaseAppState implements StateFunctionListener {

    InputMapper inputMapper;
    Node stateGuiNode=new Node();

    @Override
    protected void initialize(Application app) {
        IngameBanana b[][] = new IngameBanana[3][3];
        BBFocusTraversal bbft = new BBFocusTraversal();
        stateGuiNode.addControl(bbft);
        for (int i = 0; i < b.length; i++) {
            for (int j = 0; j < b[i].length; j++) {
                b[i][j] = new IngameBanana(app.getAssetManager(), 50, 50);
                b[i][j].geo.setLocalTranslation(100 + i * 100, 400 - j * 100, 0);
                stateGuiNode.attachChild(b[i][j].geo);
                bbft.addFocusable(b[i][j].geo, j, i, 1, 1);
            }
        }
        IngameBanana bottom = new IngameBanana(app.getAssetManager(), 250, 50);
        bottom.geo.setLocalTranslation(100, 100, 0);
        stateGuiNode.attachChild(bottom.geo);
        bbft.addFocusable(bottom.geo, 3, 0, 3, 1);

        GuiGlobals.getInstance().requestFocus(b[0][0].geo);

        inputMapper = GuiGlobals.getInstance().getInputMapper();
        //BigBananaFunctions.initializeDefaultMappings(inputMapper);

    }

    @Override
    protected void cleanup(Application app) {

    }

    @Override
    protected void onEnable() {
        inputMapper.addStateListener(this, F_BACK);
        //GuiGlobals.getInstance().getInputMapper().activateGroup(GROUP_BIGBANANA);
        ((SimpleApplication) getApplication()).getGuiNode().attachChild(stateGuiNode);
    }

    @Override
    protected void onDisable() {
        inputMapper.removeStateListener(this, F_BACK);
        //GuiGlobals.getInstance().getInputMapper().deactivateGroup(GROUP_BIGBANANA);
        stateGuiNode.removeFromParent();
    }

    @Override
    public void valueChanged(FunctionId func, InputState value, double tpf) {
        if (value.equals(InputState.Off)) {
            System.out.println("Pressed " + func);
            setEnabled(false);
            getState(SampleMenuAppState.class).setEnabled(true);
        }
    }
}
