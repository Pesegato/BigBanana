package com.pesegato.bigbanana.sampleapp;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;
import com.pesegato.bigbanana.BigBananaAppState;
import com.simsilica.lemur.input.AnalogFunctionListener;
import com.simsilica.lemur.input.FunctionId;
import com.simsilica.lemur.input.InputState;
import com.simsilica.lemur.input.StateFunctionListener;

import static com.pesegato.bigbanana.BigBananaAppState.LEFT_STICK_X;
import static com.pesegato.bigbanana.BigBananaAppState.LEFT_STICK_Y;
import static com.pesegato.bigbanana.extra.BigBananaFunctions.F_BACK;
import static com.pesegato.bigbanana.extra.BigBananaFunctions.GROUP_BIGBANANA;
import static com.pesegato.bigbanana.sampleapp.Main.ANOTHER_ACTION;
import static com.pesegato.bigbanana.sampleapp.Main.MY_COOL_ACTION;

public class InGameAppState extends BaseAppState implements StateFunctionListener, AnalogFunctionListener {

    public static final FunctionId F_COOLACTION = new FunctionId(GROUP_BIGBANANA, "cool action");
    public static final FunctionId F_ANOTHER_ACTION = new FunctionId(GROUP_BIGBANANA, "another action");

    Geometry leftStick, coolAction;

    BigBananaAppState bbas;
    Node stateGuiNode = new Node();

    @Override
    protected void initialize(Application app) {
        leftStick = new Geometry("MyQuad", new Quad(10, 10));
        Material m = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        m.setColor("Color", ColorRGBA.Green);
        leftStick.setMaterial(m);
        leftStick.setLocalTranslation(400, 50, 0);
        stateGuiNode.attachChild(leftStick);

        coolAction = new Geometry("CoolAction", new Quad(50, 50));
        Material mc = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        mc.setColor("Color", ColorRGBA.DarkGray);
        coolAction.setMaterial(mc);
        coolAction.setLocalTranslation(200, 100, 0);
        stateGuiNode.attachChild(coolAction);

        //BigBananaFunctions.initializeDefaultMappings(inputMapper);
        bbas = getState(BigBananaAppState.class);
        bbas.map(F_COOLACTION, MY_COOL_ACTION, this);
        bbas.map(F_ANOTHER_ACTION, ANOTHER_ACTION, this);
        bbas.mapBack(this);

    }

    @Override
    protected void cleanup(Application app) {
    }

    @Override
    protected void onEnable() {
        bbas.activate(this, this);
        bbas.enableUINavigationMode(false);
        //GuiGlobals.getInstance().getInputMapper().activateGroup(GROUP_BIGBANANA);
        ((SimpleApplication) getApplication()).getGuiNode().attachChild(stateGuiNode);
    }

    @Override
    protected void onDisable() {
        bbas.deactivate();
        //GuiGlobals.getInstance().getInputMapper().deactivateGroup(GROUP_BIGBANANA);
        stateGuiNode.removeFromParent();
    }

    @Override
    public void valueChanged(FunctionId func, InputState value, double tpf) {
        if (func.equals(F_COOLACTION)) {
            if (value.equals(InputState.Off)) {
                System.out.println("Wow, cool action!");
                coolAction.getMaterial().setColor("Color", ColorRGBA.DarkGray);
            } else {
                coolAction.getMaterial().setColor("Color", ColorRGBA.Red);
            }
        }
        if (func.equals(F_ANOTHER_ACTION)) {
            if (value.equals(InputState.Off)) {
                System.out.println("Another action!");
                coolAction.getMaterial().setColor("Color", ColorRGBA.DarkGray);
            } else {
                coolAction.getMaterial().setColor("Color", ColorRGBA.Green);
            }
        }

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
        if (func.equals(LEFT_STICK_X))
            valueX = (float) value;
        if (func.equals(LEFT_STICK_Y))
            valueY = (float) value;
        leftStick.setLocalTranslation((float) (400 + valueX * 50), 50 + valueY * 50, 0);
    }
}
