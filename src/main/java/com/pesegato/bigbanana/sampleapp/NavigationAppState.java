package com.pesegato.bigbanana.sampleapp;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;
import com.pesegato.bigbanana.BBFocusTraversal;
import com.pesegato.bigbanana.BigBananaAppState;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.input.AnalogFunctionListener;
import com.simsilica.lemur.input.FunctionId;
import com.simsilica.lemur.input.InputState;
import com.simsilica.lemur.input.StateFunctionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.pesegato.bigbanana.BigBananaAppState.LEFT_STICK_X;
import static com.pesegato.bigbanana.BigBananaAppState.LEFT_STICK_Y;
import static com.pesegato.bigbanana.extra.BigBananaFunctions.F_BACK;

public class NavigationAppState extends BaseAppState implements StateFunctionListener, AnalogFunctionListener {

    static Logger log = LoggerFactory.getLogger(NavigationAppState.class);

    Geometry leftStick;

    BigBananaAppState bbas;
    Node stateGuiNode = new Node();

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

        //uncomment to try out unfocusable IngameBanana
        //bbft.setEnabled(false, 1, 1, 2, 1);

        GuiGlobals.getInstance().requestFocus(b[0][0].geo);

        leftStick = new Geometry("MyQuad", new Quad(10, 10));
        Material m = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        m.setColor("Color", ColorRGBA.Green);
        leftStick.setMaterial(m);
        leftStick.setLocalTranslation(400, 50, 0);
        stateGuiNode.attachChild(leftStick);

        //BigBananaFunctions.initializeDefaultMappings(inputMapper);
        bbas = getState(BigBananaAppState.class);
        bbas.mapBack(this);

    }

    @Override
    protected void cleanup(Application app) {

    }

    @Override
    protected void onEnable() {
        bbas.activate(this, this);
        //GuiGlobals.getInstance().getInputMapper().activateGroup(GROUP_BIGBANANA);
        ((SimpleApplication) getApplication()).getGuiNode().attachChild(stateGuiNode);
        getApplication().getInputManager().setCursorVisible(true);
    }

    @Override
    protected void onDisable() {
        bbas.deactivate();
        //GuiGlobals.getInstance().getInputMapper().deactivateGroup(GROUP_BIGBANANA);
        stateGuiNode.removeFromParent();
    }

    @Override
    public void valueChanged(FunctionId func, InputState value, double tpf) {
        if (func.equals(F_BACK)) {
            if (value.equals(InputState.Off)) {
                System.out.println("Pressed " + func);
                setEnabled(false);
                getState(SampleMenuAppState.class).setEnabled(true);
            }
        }
    }

    float valueX = 0;
    float valueY = 0;

    @Override
    public void valueActive(FunctionId func, double value, double tpf) {
        log.info("received {} {}", func, value);
        if (func.equals(LEFT_STICK_X))
            valueX = (float) value;
        if (func.equals(LEFT_STICK_Y))
            valueY = (float) value;
        leftStick.setLocalTranslation((float) (400 + valueX * 50), 50 + valueY * 50, 0);
    }
}
