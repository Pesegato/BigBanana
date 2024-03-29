package com.pesegato.bigbanana.sampleapp;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.pesegato.bigbanana.BigBananaAppState;
import com.pesegato.bigbanana.RemapInputAppState;
import com.pesegato.bigbanana.extra.BBStartable;
import com.pesegato.bigbanana.extra.LBBContainer;
import com.pesegato.bigbanana.extra.StartGameAppState;
import com.simsilica.lemur.ActionButton;
import com.simsilica.lemur.CallMethodAction;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.input.AnalogFunctionListener;
import com.simsilica.lemur.input.FunctionId;
import com.simsilica.lemur.input.InputState;
import com.simsilica.lemur.input.StateFunctionListener;
import com.simsilica.state.CompositeAppState;

public class SampleMenuAppState extends CompositeAppState implements BBStartable, AnalogFunctionListener, StateFunctionListener {

    BigBananaAppState bbas;
    LBBContainer mainWindow;

    public SampleMenuAppState(Class c) {
        super(new StartGameAppState(c));
    }

    public float getStandardScale() {
        int height = getApplication().getCamera().getHeight();
        return height / 720f;
    }

    @Override
    protected void initialize(Application app) {
        mainWindow = new LBBContainer();

        Label title = mainWindow.addChild(new Label("Main menu"));
        title.setFontSize(24);
        mainWindow.addLemurChild(new ActionButton(new CallMethodAction("Start UI navigation", this, "startNavigation")), 0, 0, 1, 1);
        mainWindow.addLemurChild(new ActionButton(new CallMethodAction("Start fake game", this, "startGame")), 1, 0, 1, 1);
        mainWindow.addLemurChild(new ActionButton(new CallMethodAction("Remap input", this, "remapInput")), 2, 0, 1, 1);
        mainWindow.addLemurChild(new ActionButton(new CallMethodAction("Quit", this, "quitGame")), 3, 0, 1, 1);
        mainWindow.addChild(new Label("Press START to play (sort of...)"));
        // Calculate a standard scale and position from the app's camera
        // height
        int height = app.getCamera().getHeight();
        Vector3f pref = mainWindow.getPreferredSize().clone();

        float standardScale = getStandardScale();
        pref.multLocal(1.5f * standardScale);

        // With a slight bias toward the top
        float y = height * 0.6f + pref.y * 0.5f;

        mainWindow.setLocalTranslation(100 * standardScale, y, 0);
        mainWindow.setLocalScale(1.5f * standardScale);

        bbas = getState(BigBananaAppState.class);
    }

    public void startNavigation() {
        setEnabled(false);
        getStateManager().attach(new NavigationAppState());
    }

    public void startGame() {
        setEnabled(false);
        getStateManager().attach(new InGameAppState());
    }

    public void remapInput() {
        setEnabled(false);
        getStateManager().attach(new RemapInputAppState());
    }

    public void quitGame() {
        System.exit(0);
    }

    @Override
    protected void cleanup(Application app) {

    }

    @Override
    protected void onEnable() {
        super.onEnable();
        bbas.enableUINavigationMode(true);
        bbas.activate(this, this);
        Node gui = ((SimpleApplication) getApplication()).getGuiNode();
        gui.attachChild(mainWindow);
        mainWindow.focus();

    }

    @Override
    protected void onDisable() {
        super.onDisable();
        bbas.deactivate();
        mainWindow.removeFromParent();
    }

    @Override
    public void valueActive(FunctionId func, double value, double tpf) {

    }

    @Override
    public void valueChanged(FunctionId func, InputState value, double tpf) {

    }
}
